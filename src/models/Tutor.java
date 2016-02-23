package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Melissa Rhein
 */
public class Tutor extends Model{
  
  public static final String TABLE = "tutor";
  
  private int id = 0;
  private String name;
  private String email;
  private int subject_id;
  
  public Tutor(){ }
  
  public Tutor(String name, String email, int subject_id){
    this.name = name;
    this.email = email;
    this.subject_id = subject_id;
  }

  /**
   * @return the id
   */
  @Override
  public int getId() {
    return id;
  }

  /**
   * @return the name
   */
  public String getName() {
    return name;
  }

  /**
   * @param name the name to set
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * @return the email
   */
  public String getEmail() {
    return email;
  }

  /**
   * @param email the email to set
   */
  public void setEmail(String email) {
    this.email = email;
  }

  /**
   * @return the subject_id
   */
  public int getSubject_id() {
    return subject_id;
  }

  /**
   * @param subject_id the subject_id to set
   */
  public void setSubject_id(int subject_id) {
    this.subject_id = subject_id;
  }
  
   @Override
  void load(ResultSet rs) throws SQLException {
    id = rs.getInt("id");
    name = rs.getString("name");
    email = rs.getString("email");
    subject_id = rs.getInt("subject_id");
  }
 
  @Override
  void insert() throws SQLException {
    Connection cx = ORM.connection();
    String sql = String.format(
      "insert into %s (name,email,subject_id) values (?,?,?)", TABLE);
    PreparedStatement st = cx.prepareStatement(sql);
    int i = 0;
    st.setString(++i, name);
    st.setString(++i, email);
    st.setInt(++i, subject_id);
    st.executeUpdate();
    id = ORM.getMaxId(TABLE);
  }

  @Override
  void update() throws SQLException {
    Connection cx = ORM.connection();
    String sql = String.format(
            "update %s set name=?,email=?,subject_id=? where id=?", TABLE);
    PreparedStatement st = cx.prepareStatement(sql);
    int i = 0;
    st.setString(++i, name);
    st.setString(++i, email);
    st.setInt(++i, subject_id);
    st.setInt(++i,id);
    st.executeUpdate();
  }
  
  @Override
  public String toString() {
    return String.format("(%s,%s,%s,%s)", id, name, email, subject_id);
  }
  
}
