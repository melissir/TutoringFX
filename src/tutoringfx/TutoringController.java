package tutoringfx;

import java.net.URL;
import java.util.Collection;
import java.util.HashSet;
import java.util.ResourceBundle;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.DBProps;
import models.Interaction;
import models.ORM;
import models.Student;
import models.Subject;
import models.Tutor;

/**
 *
 * @author Melissa Rhein
 */
public class TutoringController implements Initializable {
  @FXML
  ListView<Student> studentlist;
  
  @FXML
  ListView<Tutor> tutorlist;
  
  @FXML
  TextArea display;
  
  @FXML
  Label studentOrderLabel;
  
  Collection<Integer> userStudIds = new HashSet<>();
  Collection<Integer> userTutorIds = new HashSet<>();
  
  
  @Override
  public void initialize(URL url, ResourceBundle rb) {
    StudentCellCallback studCellFactory = new StudentCellCallback();
    studentlist.setCellFactory(studCellFactory);
    studCellFactory.studIds = userStudIds;
    
    TutorCellCallback tutorCellFactory = new TutorCellCallback();
    tutorlist.setCellFactory(tutorCellFactory);
    tutorCellFactory.tutorIds = userTutorIds;
    
    try {
      ORM.init(DBProps.getProps());
      
      Collection<Student> student = ORM.findAll(Student.class, "order by name");
      for (Student studs : student) {
        studentlist.getItems().add(studs);
      }
 
      Collection<Tutor> tutors = ORM.findAll(Tutor.class, "order by name");
      for (Tutor tutor : tutors) {
        tutorlist.getItems().add(tutor);
      }
    }
    catch (Exception ex) {
      ex.printStackTrace(System.err);
      System.exit(1);
    }
    studentOrderLabel.setText("Name");
  }
  
  @FXML
  void studentSelect(Event event){
    Student student = studentlist.getSelectionModel().getSelectedItem();
    display.setText(studentInfo(student));
    try {
      Collection<Interaction> interact = ORM.findAll(Interaction.class,
        "where student_id=?", new Object[]{student.getId()});
      userTutorIds.clear();
      for (Interaction i : interact) {
        userTutorIds.add(i.getTutor_id());
      }
      tutorlist.refresh();
    }
    catch (Exception ex) {
      ex.printStackTrace(System.err);
      System.exit(1);
    }
  }
  
  @FXML
  void tutorSelect(Event event){
    Tutor tutor = tutorlist.getSelectionModel().getSelectedItem();
    display.setText(tutorInfo(tutor));
    try {
      Collection<Interaction> interact = ORM.findAll(Interaction.class,
        "where tutor_id=?", new Object[]{tutor.getId()});
      userStudIds.clear();
      for (Interaction i : interact) {
        userStudIds.add(i.getStudent_id());
      }
      studentlist.refresh();
    }
    catch (Exception ex) {
      ex.printStackTrace(System.err);
      System.exit(1);
    }
  }
  
  @FXML
  void studentOrderEnrolled(Event event){ 
    Tutor tutorSel = tutorlist.getSelectionModel().getSelectedItem();
    Student studentSel = studentlist.getSelectionModel().getSelectedItem();
    
    try {
      Collection<Student> students = ORM.findAll(Student.class, "order by enrolled");
      studentlist.getItems().clear();
      for (Student i : students) {
        studentlist.getItems().add(i);
      }
    }catch(Exception ex){
      ex.printStackTrace(System.err);
      System.exit(1);
    }
    studentlist.refresh();
    
    int studentSelIndex = studentlist.getItems().indexOf(studentSel);
    int tutorSelIndex = tutorlist.getItems().indexOf(tutorSel);
    studentlist.getSelectionModel().selectIndices(studentSelIndex);
    tutorlist.getSelectionModel().selectIndices(tutorSelIndex);
    
    studentOrderLabel.setText("Enrollment Date");
    
  }
  
  @FXML
  void studentOrderName(Event event){ 
    Tutor tutorSel = tutorlist.getSelectionModel().getSelectedItem();
    Student studentSel = studentlist.getSelectionModel().getSelectedItem();
    
    try {
      Collection<Student> students = ORM.findAll(Student.class, "order by name");
      studentlist.getItems().clear();
      for (Student i : students) {
        studentlist.getItems().add(i);
      }
    }catch(Exception ex){
      ex.printStackTrace(System.err);
      System.exit(1);
    }
    studentlist.refresh();
    
    int studentSelIndex = studentlist.getItems().indexOf(studentSel);
    int tutorSelIndex = tutorlist.getItems().indexOf(tutorSel);
    studentlist.getSelectionModel().selectIndices(studentSelIndex);
    tutorlist.getSelectionModel().selectIndices(tutorSelIndex);
    
    studentOrderLabel.setText("Name");
  }
  
  @FXML
  void clear(Event event) {
      studentlist.getSelectionModel().clearSelection();
      tutorlist.getSelectionModel().clearSelection();
      userStudIds.clear();
      userTutorIds.clear();
      studentlist.refresh();
      tutorlist.refresh();
      display.setText("");
  }
  
  @FXML
  void viewReport(Event event){
    Tutor tutor = tutorlist.getSelectionModel().getSelectedItem();
    Student student = studentlist.getSelectionModel().getSelectedItem();
    Interaction interact = new Interaction();
    
    try{
      if (student == null || tutor == null){
        throw new ExpectedException("must select student and tutor");
      }
      interact = ORM.findOne(Interaction.class,
          "where student_id=? and tutor_id=?", new Object[]{student.getId(), tutor.getId()});
      if (interact == null){
        throw new ExpectedException("tutor and student are not linked");
      }
    String reportText = reportInfo(interact, student, tutor);
    
    display.setText(reportText);
    
    }catch(ExpectedException ex){
      Alert alert = new Alert(Alert.AlertType.INFORMATION);
      alert.setContentText(ex.getMessage());
      alert.show();
    }catch (Exception ex){
      ex.printStackTrace(System.err);
      System.exit(1);
    }
  }
  
  @FXML
  void editReport(Event event){
    Tutor tutor = tutorlist.getSelectionModel().getSelectedItem();
    Student student = studentlist.getSelectionModel().getSelectedItem();
    Interaction interact = new Interaction();
    
    try{
      if (student == null || tutor == null){
        throw new ExpectedException("must select student and tutor");
      }
      interact = ORM.findOne(Interaction.class,
          "where student_id=? and tutor_id=?", new Object[]{student.getId(), tutor.getId()});
      if (interact == null){
        throw new ExpectedException("tutor and student are not linked");
      }
      
      URL fxml = getClass().getResource("EditReportDialog.fxml");
      FXMLLoader fxmlLoader = new FXMLLoader(fxml);
      fxmlLoader.load();
      EditReportDialogController dialogController = fxmlLoader.getController();
      Scene scene = new Scene(fxmlLoader.getRoot());
      Stage dialogStage = new Stage();
      dialogStage.setTitle("Edit Report");
      dialogStage.setScene(scene);
      dialogStage.initModality(Modality.APPLICATION_MODAL);
      dialogController.mainController = this;
      dialogStage.show();
      
      dialogController.studentLabel.setText(student.getName());
      dialogController.tutorLabel.setText(tutor.getName());
      dialogController.reportField.setText(interact.getReport().trim());
      dialogController.interactToModify = interact;
      dialogController.student = student;
      dialogController.tutor = tutor;

    }catch(ExpectedException ex){
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setContentText(ex.getMessage());
    alert.show();
    }catch (Exception ex){
      ex.printStackTrace(System.err);
      System.exit(1);
    }
  }
  
  @FXML
  void linkPair(){
    Tutor tutor = tutorlist.getSelectionModel().getSelectedItem();
    Student student = studentlist.getSelectionModel().getSelectedItem();
    Interaction interact = new Interaction();
    //Check that student and tutor are selected
    try{
      if (student == null || tutor == null){
        throw new ExpectedException("must select student and tutor");
      }
      interact = ORM.findOne(Interaction.class,
          "where student_id=? and tutor_id=?", new Object[]{student.getId(), tutor.getId()});
      if (interact != null){
        throw new ExpectedException("tutor and student are already linked");
      }
      //Check that student does not already have tutor in that subject: FIX
      Collection<Interaction> interactsForStudent = ORM.findAll(Interaction.class, 
                   "where student_id = ?", new Object[]{student.getId()});
      Tutor tutorOfStudent;                 
      for(Interaction i : interactsForStudent){
        tutorOfStudent = ORM.findOne(Tutor.class, "where id = ?", new Object[]{i.getTutor_id()});
        if(tutor.getSubject_id() == tutorOfStudent.getSubject_id()){
          throw new ExpectedException("student may not have more than one tutor for a subject");
        }
      }
      //Link the pair
      Interaction newInteraction = new Interaction(tutor.getId(), student.getId(), "");
      ORM.store(newInteraction);
      
      //Update visual
      Collection<Interaction> newInteract = ORM.findAll(Interaction.class,
        "where student_id=?", new Object[]{student.getId()});
      userTutorIds.clear();
      for (Interaction i : newInteract) {
        userTutorIds.add(i.getTutor_id());
      }
      
      newInteract = ORM.findAll(Interaction.class,
        "where tutor_id=?", new Object[]{tutor.getId()});
      userStudIds.clear();
      for (Interaction i : newInteract) {
        userStudIds.add(i.getStudent_id());
      }
      studentlist.refresh();
      tutorlist.refresh();
      
      studentlist.getSelectionModel().select(student);
      tutorlist.getSelectionModel().select(tutor);
      
    }catch(ExpectedException ex){
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setContentText(ex.getMessage());
    alert.show();
    }catch (Exception ex){
      ex.printStackTrace(System.err);
      System.exit(1);
    }
    
  }
  
  @FXML
  void removeTutor(){
    Tutor tutor = tutorlist.getSelectionModel().getSelectedItem();
    Student student = studentlist.getSelectionModel().getSelectedItem();
    try{
      if (tutor == null){
        throw new ExpectedException("must select tutor to remove");
      }
      //remove links
      Collection<Interaction> interact = ORM.findAll(Interaction.class, 
        "where tutor_id=?", new Object[]{tutor.getId()});
      for (Interaction i : interact) {
        ORM.remove(i);
      }
      
      ORM.remove(tutor);
      userTutorIds.remove(tutor.getId());
      tutorlist.getItems().remove(tutor);
      tutorlist.refresh();
      userStudIds.clear();
      studentlist.refresh();
      tutorlist.getSelectionModel().clearSelection();
      
      studentlist.getSelectionModel().select(student);
      if (student != null) {
        display.setText(studentInfo(student));
      }
      else {
        display.setText("");
      }
    }catch(ExpectedException ex){
      Alert alert = new Alert(Alert.AlertType.INFORMATION);
      alert.setContentText(ex.getMessage());
      alert.show();
    }catch(Exception ex){
      ex.printStackTrace(System.err);
      System.exit(1);
    }
  }
  
  @FXML
  void addSubject(Event event){
    try{
      URL fxml = getClass().getResource("addSubjectDialog.fxml");
      FXMLLoader fxmlLoader = new FXMLLoader(fxml);
      fxmlLoader.load();
      AddSubjectDialogController dialogController = fxmlLoader.getController();
      Scene scene = new Scene(fxmlLoader.getRoot());
      Stage dialogStage = new Stage();
      dialogStage.setTitle("Add Subject");
      dialogStage.setScene(scene);
      dialogStage.initModality(Modality.APPLICATION_MODAL);
      dialogController.mainController = this;
      dialogStage.show();
    }catch(Exception ex){
      ex.printStackTrace(System.err);
      System.exit(1);
    }
  }
  
  @FXML
  void addStudent(Event event){
    try{
      URL fxml = getClass().getResource("addStudentDialog.fxml");
      FXMLLoader fxmlLoader = new FXMLLoader(fxml);
      fxmlLoader.load();
      AddStudentDialogController dialogController = fxmlLoader.getController();
      Scene scene = new Scene(fxmlLoader.getRoot());
      Stage dialogStage = new Stage();
      dialogStage.setTitle("Add Student");
      dialogStage.setScene(scene);
      dialogStage.initModality(Modality.APPLICATION_MODAL);
      dialogController.mainController = this;
      dialogStage.show();
    }catch(Exception ex){
      ex.printStackTrace(System.err);
      System.exit(1);
    }
  }
  
  @FXML
  void addTutor(Event event){
    try{
      URL fxml = getClass().getResource("addTutorDialog.fxml");
      FXMLLoader fxmlLoader = new FXMLLoader(fxml);
      fxmlLoader.load();
      AddTutorDialogController dialogController = fxmlLoader.getController();
      Scene scene = new Scene(fxmlLoader.getRoot());
      Stage dialogStage = new Stage();
      dialogStage.setTitle("Add Tutor");
      dialogStage.setScene(scene);
      dialogStage.initModality(Modality.APPLICATION_MODAL);
      dialogController.mainController = this;
      dialogStage.show();
    }catch(Exception ex){
      ex.printStackTrace(System.err);
      System.exit(1);
    }
  }
  
  static String reportInfo(Interaction interact, Student student, Tutor tutor){
    return String.format(
        "student: %s\n"
            + "tutor: %s\n"
            + "-- report --\n"
            + "%s", student.getName(), tutor.getName(), interact.getReport());
  }
  
  static String studentInfo(Student student){
    return String.format(
                "id: %s\n"
                        + "name: %s\n"
                        + "email: %s\n"
                        + "enrolled: %s\n",
                student.getId(),
                student.getName(),
                student.getEmail(),
                student.getEnrolled()
        );
  } 
  
  static String tutorInfo(Tutor tutor){
    return String.format(
                "id: %s\n"
                        + "name: %s\n"
                        + "email: %s\n"
                        + "subject: %s\n",
                tutor.getId(),
                tutor.getName(),
                tutor.getEmail(),
                idToSubject(tutor.getSubject_id())
        );
  }
  
  static java.sql.Date currentDate() {
  long now = new java.util.Date().getTime();
  java.sql.Date date = new java.sql.Date(now);
  return date;
  }
  
  public static String idToSubject(int id){
      Subject sub = new Subject();
      try{
        sub = ORM.load(Subject.class, id);
      }
      catch(Exception E){
          E.printStackTrace(System.err);  
          System.exit(1);
      }
      return sub.getName();
    }
}  
  

