package tutoringfx;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import models.ORM;
import models.Student;
import javafx.scene.control.Alert;
import models.Subject;

/**
 * FXML Controller class
 *
 * @author Melissa Rhein
 */
public class AddSubjectDialogController implements Initializable {

  @FXML
  Node top;
  
  @FXML
  TextField name;
 
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
    String nameInput = name.getText().trim();
    try {
      if (nameInput.length() < 3) {
        throw new ExpectedException("name length must be at least 3");
      }
      if (!nameInput.matches("[a-zA-Z ]+")) {
        throw new ExpectedException("name must contain only letters");
      }
 
      Subject subjectWithName 
        = ORM.findOne(Subject.class, "where name=?", new Object[]{nameInput});
      if (subjectWithName != null) {
        throw new ExpectedException("existing subject with same name");
      }
      Subject newSubject = new Subject(nameInput);
      ORM.store(newSubject);
      
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
