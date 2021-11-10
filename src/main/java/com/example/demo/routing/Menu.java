package com.example.demo.routing;

/** Enum which has 4 views to choose from
 *
 */
public enum Menu {
  
  doctorMenu("Doctor panel"),
  receptionistMenu("Receptionist panel"),
  nurseMenu("Injection management"),
  adminMenu("Admin panel");

  public String title;

  Menu(String title) {
    this.title = title;
  }

  /**
   * @return String that represents chosen view by the user.
   */
  public String getTitle(){
    return title;
  }

  /**
   * @return String that contains an id of the menu that will be loaded through fxml.
   */
  public String getFxml(){
    return String.format("fxml/%s.fxml", name());
  }
}
