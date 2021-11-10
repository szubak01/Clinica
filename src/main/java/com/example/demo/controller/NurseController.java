package com.example.demo.controller;

import com.example.demo.controller.dialogs.InjectionInfoController;
import com.example.demo.model.Injection;
import com.example.demo.model.enums.InjectionStatus;
import com.example.demo.service.InjectionService;
import java.time.LocalDate;
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

/** Controller that is responsible for business logic of Nurse view.
 *
 */
@Controller
public class NurseController extends AbstractController {

  private final InjectionService injectionService;

  /**
   * @param injectionService Injected service into InjectionController.
   */
  @Autowired
  public NurseController(InjectionService injectionService) {
    this.injectionService = injectionService;
  }

  // Observable list to store data from injection table
  private final ObservableList<Injection> obsPendingInj = FXCollections
      .observableArrayList();

  @FXML
  private TableView<Injection> injectionsPendingTable;

  @FXML
  private TableView<Injection> injectionsDoneTable;

  @FXML
  private TextField searchFieldInjection;

  @FXML
  private void initialize() {
    // pending
    refreshPendingInjectionTable();
    showPendingInjectionsTable();

    // done
    refreshDoneInjectionTable();
    showDoneInjectionsTable();

    // setting init value
    searchFieldInjection.setText(LocalDate.now().toString());
  }

  private void refreshPendingInjectionTable() {
    obsPendingInj.clear();
    obsPendingInj.addAll(injectionService.getAllPendingInjections());
  }

  private void refreshDoneInjectionTable() {
    injectionsDoneTable.getItems().clear();
    injectionsDoneTable.getItems().addAll(injectionService.getAllDoneInjections());
  }


  private void showPendingInjectionsTable() {

    FilteredList<Injection> filteredList = new FilteredList<>(obsPendingInj, b -> true);

    searchFieldInjection.textProperty().addListener((observable, oldValue, newValue) -> {
      filteredList.setPredicate(injection -> {

        if (newValue == null || newValue.isEmpty()) {
          return true;
        }
        String lowerCaseFilter = newValue.toLowerCase();
        if (injection.getDate().toString().toLowerCase().contains(lowerCaseFilter)) {
          return true;
        } else if (injection.getNurseName().toLowerCase().contains(lowerCaseFilter)) {
          return true;
        } else if(injection.getDoctorName().toLowerCase().contains(lowerCaseFilter)){
          return true;
        } else{
          return false;
        }
      });
    });

    SortedList<Injection> injectionSortedList = new SortedList<>(filteredList);
    injectionSortedList.comparatorProperty().bind(injectionsPendingTable.comparatorProperty());
    injectionsPendingTable.setItems(injectionSortedList);

    // Double click to open info view
    injectionsPendingTable.setOnMouseClicked(mouseEvent -> {
      if (mouseEvent.getClickCount() == 2) {
        Injection injection = injectionsPendingTable.getSelectionModel().getSelectedItem();
        if (injection != null) {
          InjectionInfoController.showInfo(injection);
        }
      }
    });
  }

  private void showDoneInjectionsTable() {

    // Double click to open edit view
    injectionsDoneTable.setOnMouseClicked(mouseEvent -> {
      if (mouseEvent.getClickCount() == 2) {
        Injection injection = injectionsDoneTable.getSelectionModel().getSelectedItem();
        if (injection != null) {
          InjectionInfoController.showInfo(injection);
        }
      }
    });
  }

  @FXML
  private void changeStatus() {
    Injection selectedInj = injectionsPendingTable.getSelectionModel().getSelectedItem();

    if (selectedInj == null) {
      Alert alert = new Alert(AlertType.INFORMATION);
      alert.setTitle("Unselected record");
      alert.setContentText("Please select record first!");
      alert.setHeaderText(null);
      alert.showAndWait();
    } else {
      selectedInj.setStatus(InjectionStatus.DONE);
      injectionService.save(selectedInj);
      refreshPendingInjectionTable();
      refreshDoneInjectionTable();
    }
  }
}
