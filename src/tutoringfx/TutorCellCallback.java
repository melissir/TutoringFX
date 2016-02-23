package tutoringfx;

import java.util.Collection;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;
import models.ORM;
import models.Subject;
import models.Tutor;


/**
 *
 * @author Melissa Rhein
 */
public class TutorCellCallback implements Callback<ListView<Tutor>, ListCell<Tutor>>{
  
  Collection<Integer> tutorIds;

    @Override
    public ListCell<Tutor> call(ListView<Tutor> p) {
        ListCell<Tutor> cell = new ListCell<Tutor>() {
            @Override
            protected void updateItem(Tutor tut, boolean empty) {
                super.updateItem(tut, empty);
                if (empty) {
                    this.setText(null);
                    return;
                }
                this.setText(tut.getName() + " | " + idToSubject(tut.getSubject_id()));

                String css = "-fx-text-fill:#606; -fx-font-weight:bold;";
                if (tutorIds.contains(tut.getId())) {
                    this.setStyle(css);
                } else {
                    this.setStyle(null);
                }
            }

        };
            return cell;
    }
    public String idToSubject(int id){
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
