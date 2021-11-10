package com.example.demo.controller.dialogs;

import com.example.demo.infrastructure.LoginException;
import com.example.demo.model.User;
import com.example.demo.model.enums.Gender;
import com.example.demo.model.enums.Role;
import com.example.demo.service.UserService;
import java.util.function.Consumer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/** Controller that is responsible for business logic of dialog that adds new user to the system.
 *
 */
@Getter
@Controller
public class UserAddController {

  @Autowired
  private  UserService userService;

  @FXML
  private Button saveButton;
  @FXML
  private Label title;
  @FXML
  private Label message;
  @FXML
  private TextField firstName;
  @FXML
  private TextField lastName;
  @FXML
  private TextField loginField;
  @FXML
  private TextField passwordField;
  @FXML
  private ComboBox<Gender> genderBox;
  @FXML
  private ComboBox<Role> roleBox;

  private User user;
  private Consumer<User> saveHandler;

  /** Displays a dialog where admin creates new user.
   * @param saveHandler Consumes accepted object.
   * @param genderList Handing over list of genders.
   * @param roleList Handing over list of roles.
   */
  public static void addNew(Consumer<User> saveHandler, Gender[] genderList, Role[] roleList) {
    edit(null, saveHandler, genderList, roleList);
  }

  /** Displays a dialog where admin edits already created user.
   * @param user Handing over already created object.
   * @param saveHandler Consumes accepted object.
   * @param genderList Handing over list of genders.
   * @param roleList Handing over list of roles.
   */
  public static void edit(User user,
      Consumer<User> saveHandler,
      Gender[] genderList,
      Role[] roleList) {
    try {
      Stage stage = new Stage(StageStyle.UNDECORATED);
      FXMLLoader loader = new FXMLLoader(
          UserAddController.class.getClassLoader().getResource("fxml/dialogs/userAddDialog.fxml"));
      stage.setScene(new Scene(loader.load()));
      stage.initModality(Modality.APPLICATION_MODAL);

      UserAddController userAddController = loader.getController();
      userAddController.init(user, saveHandler, genderList, roleList);

      stage.show();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @FXML
  private void save() {
    try {
      user.setFirstName(firstName.getText());
      user.setLastName(lastName.getText());
      user.setLogin(loginField.getText());
      user.setPassword(passwordField.getText());
      user.setGender(genderBox.getValue());
      user.setRole(roleBox.getValue());

      saveHandler.accept(user);
      close();
    } catch (LoginException e) {
      message.setText(e.getMessage());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @FXML
  private void close() {
    roleBox.getScene().getWindow().hide();
  }

  private void init(User user, Consumer<User> saveHandler, Gender[] genderList, Role[] roleList) {

    this.user = user;
    this.saveHandler = saveHandler;
    genderBox.getItems().addAll(genderList);
    roleBox.getItems().addAll(roleList);

    if (user == null) {
      title.setText("Create new user");
      this.user = new User();
    } else {
      title.setText("Edit user");
    }

    // binding data to edit dialog
    firstName.setText(this.user.getFirstName());
    lastName.setText(this.user.getLastName());
    loginField.setText(this.user.getLogin());
    passwordField.setText(this.user.getPassword());
    genderBox.setValue(this.user.getGender());
    roleBox.setValue(this.user.getRole());
  }

}
