package com.example.demo.controller;

import com.example.demo.routing.Menu;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

/** AbstractController provides a method that sets the main title of every panel.
 * Depends which panel is active it sets a custom title.
 */
public abstract class AbstractController {

  @FXML
  private Label title;

  /**
   * @param menu decides which title is going to be displayed
   * <p>
   * There is 4 menus to be chosen from:
   * <ul>
   *     <li>doctorMenu</li>
   *     <li>receptionistMenu</li>
   *     <li>nurseMenu</li>
   *     <li>adminMenu</li>
   * </ul>
   */
  public void setTitle(Menu menu){
    this.title.setText(menu.getTitle());
  }
}
