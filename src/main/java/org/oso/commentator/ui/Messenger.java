package org.oso.commentator.ui;

import java.awt.Container;

import javax.swing.JOptionPane;

public class Messenger {
  private Container container;
  
  public Messenger(Container container) {
    this.container = container;
  }
  
  public void displayPopupMessage(String message) {
    JOptionPane.showMessageDialog(container, message);
  }
}
