package tutoringfx;

import java.net.URL;
import java.util.Collection;
import java.util.ResourceBundle;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import models.ORM;
import models.Student;
import org.apache.commons.validator.routines.EmailValidator;
import java.util.Date;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import models.Tutor;


/**
 * FXML Controller class
 *
 * @author Melissa Rhein
 */
public class AddStudentDialogController implements Initializable {

  @FXML
  Node top;
  
  @FXML
  TextField lastName;
  
  @FXML
  TextField firstName;
  
  @FXML
  TextField email;
 
  TutoringController mainController;
  
  /**
   * Initializes the controller class.
   */
  @Override
  public void initialize(URL url, ResourceBundle rb) {
    // TODO
  }  
  
  @FXML
  void add(Event event) {
    String firstInput = firstName.getText().trim();
    String lastInput = lastName.getText().trim();
    String emailInput = email.getText().trim();
    
    try {
      if (firstInput.length() < 3 || lastInput.length() < 3) {
        throw new ExpectedException("name lengths must be at least 3");
      }
      if (!firstInput.matches("[a-zA-Z ]+") || !lastInput.matches("[a-zA-Z ]+")) {
        throw new ExpectedException("names must contain only letters");
      }
      
      String fullName = lastInput + "," + firstInput;
 
      Student studentWithName 
        = ORM.findOne(Student.class, "where name=?", new Object[]{fullName});
      if (studentWithName != null) {
        throw new ExpectedException("existing student with same name");
      }
      EmailValidator validator = EmailValidator.getInstance();
      if (!validator.isValid(emailInput)){
        throw new ExpectedException("e-mail address is not valid");
      }
      Date utilDate = new Date();
      java.sql.Date enrollment = new java.sql.Date(utilDate.getTime());
      
      Student newStudent = new Student(fullName, emailInput, enrollment);
      ORM.store(newStudent);
      
      ListView<Student> studentlist = mainController.studentlist;
      ListView<Tutor> tutorlist = mainController.tutorlist;
      TextArea display = mainController.display;
      Label order = mainController.studentOrderLabel;
     
      studentlist.getItems().clear();
      Collection <Student> students;
      if (order.getText().trim().equals("Name")){
        students = ORM.findAll(Student.class, "order by name");
      }else{
        students = ORM.findAll(Student.class, "order by enrolled");
      }
      for (Student student : students) {
        studentlist.getItems().add(student);
      }
      tutorlist.getSelectionModel().clearSelection();
      mainController.userStudIds.clear();
      mainController.userTutorIds.clear();
      studentlist.refresh();
      tutorlist.refresh();
      studentlist.getSelectionModel().select(newStudent);
      studentlist.scrollTo(newStudent);
      display.setText(TutoringController.studentInfo(newStudent));
      
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
