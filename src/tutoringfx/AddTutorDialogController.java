package tutoringfx;

import java.net.URL;
import java.util.Collection;
import java.util.ResourceBundle;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import models.ORM;
import models.Student;
import models.Subject;
import models.Tutor;
import org.apache.commons.validator.routines.EmailValidator;

/**
 * FXML Controller class
 *
 * @author Melissa Rhein
 */
public class AddTutorDialogController implements Initializable {

  @FXML
  Node top;
  
  @FXML
  TextField lastName;
  
  @FXML
  TextField firstName;
  
  @FXML
  TextField email;
  
  @FXML
  ComboBox<String> subject;
 
  TutoringController mainController;
  
  /**
   * Initializes the controller class.
   */
  @Override
  public void initialize(URL url, ResourceBundle rb) {
    try{
      Collection<Subject> subjects = ORM.findAll(Subject.class);
      for (Subject i: subjects){
        subject.getItems().add(i.getName());
      }
      
    }catch(Exception ex){
      ex.printStackTrace(System.err);
      System.exit(1);
    }
    
  }  
  
  @FXML
  void add(Event event) {
    String firstInput = firstName.getText().trim();
    String lastInput = lastName.getText().trim();
    String emailInput = email.getText().trim();
    String subjectChoice = subject.getSelectionModel().getSelectedItem();
    
    try {
      if (firstInput.length() < 3 || lastInput.length() < 3) {
        throw new ExpectedException("name lengths must be at least 3");
      }
      if (!firstInput.matches("[a-zA-Z ]+") || !lastInput.matches("[a-zA-Z ]+")) {
        throw new ExpectedException("names must contain only letters");
      }
      
      String fullName = lastInput + "," + firstInput;
 
      Tutor tutorWithName 
        = ORM.findOne(Tutor.class, "where name=?", new Object[]{fullName});
      if (tutorWithName != null) {
        throw new ExpectedException("existing tutor with same name");
      }
      EmailValidator validator = EmailValidator.getInstance();
      if (!validator.isValid(emailInput)){
        throw new ExpectedException("e-mail address is not valid");
      }
      if(subjectChoice == null){
        throw new ExpectedException("must select a subject");
      }
      Subject subjectObj = ORM.findOne(Subject.class, "where name = ?", new Object[]{subjectChoice});
      Tutor newTutor = new Tutor(fullName, emailInput, subjectObj.getId());
      ORM.store(newTutor);
      
      ListView<Student> studentlist = mainController.studentlist;
      ListView<Tutor> tutorlist = mainController.tutorlist;
      TextArea display = mainController.display;
     
      tutorlist.getItems().clear();
      Collection<Tutor> tutors = ORM.findAll(Tutor.class);
      for (Tutor tutor : tutors) {
        tutorlist.getItems().add(tutor);
      }
      tutorlist.getSelectionModel().clearSelection();
      mainController.userStudIds.clear();
      mainController.userTutorIds.clear();
      studentlist.refresh();
      tutorlist.refresh();
      tutorlist.getSelectionModel().select(newTutor);
      tutorlist.scrollTo(newTutor);
      display.setText(TutoringController.tutorInfo(newTutor));
      
      top.getScene().getWindow().hide();
    }
    catch (ExpectedException ex) {
      Alert alert = new Alert(Alert.AlertType.INFORMATION);
      alert.setContentText(ex.getMessage());
      alert.show();
    }
    catch (Exception ex){
      ex.printStackTrace(System.err);
      System.exit(1);
    }   
    

  }  
  
}
