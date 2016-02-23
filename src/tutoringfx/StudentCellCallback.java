package tutoringfx;

import java.util.Collection;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;
import models.Student;

/**
 *
 * @author Melissa Rhein
 */
public class StudentCellCallback implements Callback<ListView<Student>, ListCell<Student>>{
  
  Collection<Integer> studIds;

    @Override
    public ListCell<Student> call(ListView<Student> p) {
        ListCell<Student> cell = new ListCell<Student>() {
            @Override
            protected void updateItem(Student stud, boolean empty) {
                super.updateItem(stud, empty);
                if (empty) {
                    this.setText(null);
                    return;
                }
                this.setText(stud.getName() + " | " + stud.getEnrolled());

                String css = "-fx-text-fill:#606; -fx-font-weight:bold;";
                if (studIds.contains(stud.getId())) {
                    this.setStyle(css);
                } else {
                    this.setStyle(null);
                }
            }

        };
            return cell;
    }
  
}
