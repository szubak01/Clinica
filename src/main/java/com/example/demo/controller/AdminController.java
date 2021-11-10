package com.example.demo.controller;

import com.example.demo.controller.dialogs.UserAddController;
import com.example.demo.model.User;
import com.example.demo.model.enums.Gender;
import com.example.demo.model.enums.Role;
import com.example.demo.service.UserService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/** Controller that is responsible for business logic of Admin view.
 *
 */
@Controller
public class AdminController extends AbstractController {

  private final UserService userService;

  /**
   * @param userService Injected service into AdminController.
   */
  @Autowired
  public AdminController(UserService userService) {
    this.userService = userService;
  }

  private final ObservableList<User> observableUserList = FXCollections
      .observableArrayList();

  @FXML
  private TextField searchField;

  @FXML
  private TableView<User> userTableView;

  @FXML
  private void refreshTable() {
    observableUserList.clear();
    observableUserList.addAll(userService.getAllUsers());
  }

  @FXML
  private void initialize() {

    refreshTable();

    // ObservableList wrapped into FilteredList
    FilteredList<User> filteredList = new FilteredList<>(observableUserList, b -> true);

    // Setting filter Predicate whenever searchField changes
    searchField.textProperty().addListener((observable, oldValue, newValue) -> {
      filteredList.setPredicate(user -> {

        // If filter text is empty display all the data
        if (newValue == null || newValue.isEmpty()) {
          return true;
        }

        // Compare each column properties with filter text from searchField
        String lowerCaseFilter = newValue.toLowerCase();

        if (user.getFirstName().toLowerCase().contains(lowerCaseFilter)) {
          return true; // filter matches property
        } else if (user.getLastName().toLowerCase().contains(lowerCaseFilter)) {
          return true;
        } else if (user.getLogin().toLowerCase().contains(lowerCaseFilter)) {
          return true;
        } else if (user.getPassword().toLowerCase().contains(lowerCaseFilter)) {
          return true;
        } else if (String.valueOf(user.getGender()).toLowerCase().contains(lowerCaseFilter)) {
          return true;
        } else if (String.valueOf(user.getRole()).toLowerCase().contains(lowerCaseFilter)) {
          return true;
        } else {
          return false; // Doesn't match
        }
      });
    });

    // Wrap FilteredList into SortedList
    SortedList<User> userSortedList = new SortedList<>(filteredList);

    // Bind the SortedList comparator to the tableView comparator
    userSortedList.comparatorProperty().bind(userTableView.comparatorProperty());

    // Add sorted data to tableView
    userTableView.setItems(userSortedList);

    // Double click to open edit view
    userTableView.setOnMouseClicked(mouseEvent -> {
      if (mouseEvent.getClickCount() == 2) {
        User user = userTableView.getSelectionModel().getSelectedItem();
        if (user != null) {
          UserAddController.edit(user, this::saveUser, Gender.values(), Role.values());
        }
      }
    });
  }

  @FXML
  private void addNewUser() {
    UserAddController.addNew(this::saveUser, Gender.values(), Role.values());
  }

  private void saveUser(User user) {
    userService.save(user);
    refreshTable();
  }

  @FXML
  private void deleteUser() {
    User selectedUser = userTableView.getSelectionModel().getSelectedItem();

    if (selectedUser == null) {
      Alert alert = new Alert(AlertType.INFORMATION);
      alert.setTitle("Unselected record");
      alert.setContentText("Please select record first!");
      alert.setHeaderText(null);
      alert.showAndWait();
    }
    else if (selectedUser.getRole() == Role.ADMIN) {
      Alert alert = new Alert(AlertType.ERROR);
      alert.setTitle("Permission denied");
      alert.setContentText("You cannot delete admin!");
      alert.setHeaderText(null);
      alert.showAndWait();
    }
    else {
      Long selectedUserId = selectedUser.getId();
      userService.deleteUserById(selectedUserId);
      refreshTable();
    }
  }
}
