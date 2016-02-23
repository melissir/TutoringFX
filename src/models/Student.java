package models;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Melissa Rhein
 */
public class Student extends Model{
  
  public static final String TABLE = "student";
  
  private int id = 0;
  private String name;
  private String email;
  private Date enrolled;
  
  public Student(){ }
  
  public Student(String name, String email, Date enrolled){
    this.name = name;
    this.email = email;
    this.enrolled = enrolled;
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
   * @return the enrolled
   */
  public Date getEnrolled() {
    return enrolled;
  }

  /**
   * @param enrolled the enrolled to set
   */
  public void setEnrolled(Date enrolled) {
    this.enrolled = enrolled;
  }
  
  @Override
  void load(ResultSet rs) throws SQLException {
    id = rs.getInt("id");
    name = rs.getString("name");
    email = rs.getString("email");
    enrolled = rs.getDate("enrolled");
  }
  
  @Override
  void insert() throws SQLException {
    Connection cx = ORM.connection();
    String sql = String.format(
      "insert into %s (name,email,enrolled) values (?,?,?)", TABLE);
    PreparedStatement st = cx.prepareStatement(sql);
    int i = 0;
    st.setString(++i, name);
    st.setString(++i, email);
    st.setDate(++i, enrolled);
    st.executeUpdate();
    id = ORM.getMaxId(TABLE);
  }

  @Override
  void update() throws SQLException {
    Connection cx = ORM.connection();
    String sql = String.format(
      "update %s set name=?,email=?,enrolled=? where id=?", TABLE);
    PreparedStatement st = cx.prepareStatement(sql);
    int i = 0;
    st.setString(++i, name);
    st.setString(++i, email);
    st.setDate(++i, enrolled);
    st.setInt(++i, id);
    st.executeUpdate();
  }
  
  @Override
  public String toString() {
    return String.format("(%s,%s,%s,%s)", id, name, email, enrolled);
  }
  
}
