package tutoringfx;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyEvent;
import models.Interaction;
import models.ORM;
import models.Student;
import models.Tutor;


/**
 * FXML Controller class
 *
 * @author Melissa Rhein
 */
public class EditReportDialogController implements Initializable {
  
  @FXML
  Node top;
  
  Interaction interactToModify;
  
  TutoringController mainController;
  
  Tutor tutor;
  
  Student student;
  
  Boolean reportChanged;
  
  @FXML
  TextArea reportField;
  
  @FXML 
  Label studentLabel;
  
  @FXML
  Label tutorLabel;
  

  /**
   * Initializes the controller class.
   */
  @Override
  public void initialize(URL url, ResourceBundle rb) {
    reportChanged = false; 
    reportField.setOnKeyPressed(new EventHandler<KeyEvent>() {
      @Override 
      public void handle(KeyEvent ke) {
           reportChanged = true;
       }
     });
   
  }
  
  @FXML
  void modify(Event event){
    try{
      String report = reportField.getText().trim();
      interactToModify.setStudent_id(student.getId());
      interactToModify.setTutor_id(tutor.getId());
      interactToModify.setReport(report);
      ORM.store(interactToModify);

      ListView<Student> studentlist = mainController.studentlist;
      ListView<Tutor> tutorlist = mainController.tutorlist;
      TextArea display = mainController.display;
      display.setText(TutoringController.reportInfo(interactToModify, student, tutor));
    }catch(Exception ex){
      ex.printStackTrace(System.err);
      System.exit(1);
    }

    top.getScene().getWindow().hide();
  }
  
  @FXML
  void cancel(Event event){
    if(reportChanged){
      Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
      alert.setContentText("Are you sure you want to exit without modifying?");
      Optional<ButtonType> result = alert.showAndWait();
      if (result.get() != ButtonType.OK) {
        return;
      }
    }
    top.getScene().getWindow().hide();
  }
  
  
    
}
