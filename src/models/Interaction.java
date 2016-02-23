package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Melissa Rhein
 */
public class Interaction extends Model{
  
  public static final String TABLE = "interaction";
  
  private int id = 0;
  private int tutor_id;
  private int student_id;
  private String report;
  
  public Interaction(){ }
  
  public Interaction(int tutor_id, int student_id, String report){
    this.tutor_id = tutor_id;
    this.student_id = student_id;
    this.report = report;
  }

  /**
   * @return the id
   */
  @Override
  public int getId() {
    return id;
  }

  /**
   * @return the tutor_id
   */
  public int getTutor_id() {
    return tutor_id;
  }

  /**
   * @param tutor_id the tutor_id to set
   */
  public void setTutor_id(int tutor_id) {
    this.tutor_id = tutor_id;
  }

  /**
   * @return the student_id
   */
  public int getStudent_id() {
    return student_id;
  }

  /**
   * @param student_id the student_id to set
   */
  public void setStudent_id(int student_id) {
    this.student_id = student_id;
  }

  /**
   * @return the report
   */
  public String getReport() {
    return report;
  }

  /**
   * @param report the report to set
   */
  public void setReport(String report) {
    this.report = report;
  }
  
  

  @Override
  void load(ResultSet rs) throws SQLException {
    id = rs.getInt("id");
    student_id = rs.getInt("student_id");
    tutor_id = rs.getInt("tutor_id");
    report = rs.getString("report");
  }
  
  @Override
  void insert() throws SQLException {
    Connection cx = ORM.connection();
    String sql = String.format(
      "insert into %s (student_id,tutor_id,report) values (?,?,?)", TABLE);
    PreparedStatement st = cx.prepareStatement(sql);
    int i = 0;
    st.setInt(++i, student_id);
    st.setInt(++i, tutor_id);
    st.setString(++i, report);
    st.executeUpdate();
    id = ORM.getMaxId(TABLE);
  }

  @Override
  void update() throws SQLException {
    Connection cx = ORM.connection();
    String sql = String.format(
      "update %s set report=? where id=?", TABLE);
    PreparedStatement st = cx.prepareStatement(sql);
    int i = 0;
    st.setString(++i, report);
    st.setInt(++i, id);
    st.executeUpdate();
  }
  
  @Override
  public String toString() {
    return String.format("(%s,%s,%s,%s)", id, student_id, tutor_id, report);
  }
  
}
