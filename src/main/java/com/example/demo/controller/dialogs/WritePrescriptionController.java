package com.example.demo.controller.dialogs;


import com.example.demo.infrastructure.LoginException;
import com.example.demo.model.Patient;
import com.example.demo.model.Prescription;
import com.example.demo.model.User;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.function.Consumer;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.StringConverter;
import org.springframework.stereotype.Controller;

/** Controller that is responsible for business logic of dialog write prescription.
 *
 */
@Controller
public class WritePrescriptionController {

  public Stage stage;

  private static Long counter = 1L;

  @FXML
  private Label title;
  @FXML
  private Label message;
  @FXML
  private ComboBox<User> doctorsCBox;
  @FXML
  private ComboBox<Patient> patientCBox;
  @FXML
  private TextArea descriptionArea;
  @FXML
  private Button savePrescriptionAndPdfButton;

  private Prescription prescription;
  private Consumer<Prescription> saveHandler;

  @FXML
  private void cancelDialog() {
    patientCBox.getScene().getWindow().hide();
  }

  @FXML
  private void savePrescription() {
    try {
      prescription.setDate(LocalDate.now());
      prescription.setDescription(descriptionArea.getText());
      prescription.setDoctor(doctorsCBox.getValue());
      prescription.setPatient(patientCBox.getValue());

      saveHandler.accept(prescription);
      cancelDialog();
    } catch (LoginException e) {
      message.setText(e.getMessage());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /** Displays a dialog where user creates new prescription.
   * @param saveHandler Consumes accepted object.
   * @param patients Handing over list of patients.
   * @param doctors Handing over list of doctors.
   */
  public static void addNewPrescription(Consumer<Prescription> saveHandler, List<Patient> patients,
      List<User> doctors) {
    editPrescription(null, saveHandler, patients, doctors);
  }

  /** Displays a dialog where user edits already created prescription.
   * @param prescription Handing over already created object.
   * @param saveHandler Consumes accepted object.
   * @param patients Handing over list of patients.
   * @param doctors Handing over list of doctors.
   */
  public static void editPrescription(Prescription prescription, Consumer<Prescription> saveHandler,
      List<Patient> patients, List<User> doctors) {
    try {
      Stage stage = new Stage(StageStyle.UNDECORATED);
      FXMLLoader loader = new FXMLLoader(
          UserAddController.class.getClassLoader()
              .getResource("fxml/dialogs/writePrescription.fxml"));
      stage.setScene(new Scene(loader.load()));
      stage.initModality(Modality.APPLICATION_MODAL);
      stage.getIcons().add(new Image("images/logo.jpg"));
      stage.setTitle("Healthcare system");

      WritePrescriptionController prescriptionController = loader.getController();
      prescriptionController.init(prescription, saveHandler, patients, doctors, stage);

      stage.show();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void init(Prescription prescription, Consumer<Prescription> saveHandler,
      List<Patient> patients, List<User> doctors, Stage stage) {

    this.stage = stage;
    this.prescription = prescription;
    this.saveHandler = saveHandler;
    patientCBox.getItems().addAll(patients);
    doctorsCBox.getItems().addAll(doctors);

    if (prescription == null) {
      title.setText("Write a prescription");
      this.prescription = new Prescription();
    } else {
      title.setText("Edit prescription");
    }

    patientCBox.setConverter(new StringConverter<>() {
      @Override
      public String toString(Patient patient) {
        return patient.getFirstName() + " " + patient.getLastName();
      }

      @Override
      public Patient fromString(String s) {
        return null;
      }
    });

    doctorsCBox.setConverter(new StringConverter<>() {
      @Override
      public String toString(User doctor) {
        return doctor.getFirstName() + " " + doctor.getLastName();
      }

      @Override
      public User fromString(String s) {
        return null;
      }
    });

      savePrescriptionAndPdfButton.setOnAction(new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent actionEvent) {
          ++counter;
          savePrescription();
          // pdf handling, saving prescription to database, saving pdf
          try {

            if (doctorsCBox.getValue() == null) {
              throw new LoginException("Please pick a doctor.");
            }
            if (patientCBox.getValue() == null) {
              throw new LoginException("Please pick a patient.");
            }
            if (descriptionArea.getText() == null) {
              throw new LoginException("Please provide a description");
            }

            String patientName =
                patientCBox.getValue().getFirstName() + " " + patientCBox.getValue().getLastName();
            String doctorName =
                doctorsCBox.getValue().getFirstName() + " " + doctorsCBox.getValue().getLastName();
            String dateTime = LocalDate.now().toString();
            String description = descriptionArea.getText();
            String fileName = "Prescription-" + patientName + "-" + dateTime + ".pdf";

            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save prescription pdf");
            fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
            fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("PDF file", "*.pdf")
            );
            fileChooser.setInitialFileName(fileName);
            File file = fileChooser.showSaveDialog(stage);
            String filePath = "./";
            if (file == null) {
              // handle cancellation properly
              stage.getScene().getWindow().hide();
            } else {
              filePath = file.getAbsolutePath();
            }

            PdfGenerator pdf = new PdfGenerator(counter,
                patientName,
                doctorName,
                dateTime,
                description,
                filePath,
                fileName);

            pdf.createPrescription();

          } catch (IOException e) {
            e.printStackTrace();
          } catch (LoginException e) {
            message.setText(e.getMessage());
          }
        }
      });
    }

}

