package org.oso.commentator.ui;

import java.awt.Button;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JOptionPane;

import org.oso.commentator.config.Config;
import org.oso.commentator.service.Service;

public class MainWindow {

  /**
   * 
   */
  @SuppressWarnings("unused")
  private static final long serialVersionUID = -8035552003276945715L;
  private static Config config = Config.getInstance();
  private Service service;
  
  //UI
  private Frame mainFrame;
  private Panel panel;
  
  private Label exportURLLabel;
  private TextField exportURLValue;
  
  private Label exportFileNameLabel;
  private TextField exportFileNameValue;
  private Button exportFileNameButton;
  
  private Label exportCommentsPathLabel;
  private TextField exportCommentsPathValue;
  private Button exportCommentsPathButton;
  
  private Button exportButton;
  
  @SuppressWarnings("static-access")
  private void setupMainFrame() {
    mainFrame = new Frame("Commentator");
    mainFrame.setSize(500,500);
    mainFrame.addWindowListener(new WindowAdapter() {
       public void windowClosing(WindowEvent windowEvent){
          System.exit(0);
       }        
    });
    mainFrame.setVisible(true);//now frame will be visible, by default not visible 
    panel = new Panel();
    panel.setSize(500, 500);
    GridBagLayout gridbag = new GridBagLayout();
    GridBagConstraints constraints = new GridBagConstraints();
    panel.setLayout(gridbag);
    mainFrame.add(panel);
    panel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
    
    //ROW#1
    setupExportURL();
    constraints.fill = GridBagConstraints.BOTH;
    constraints.weightx = 1.0;
    setupComponentIntoPanel(exportURLLabel, gridbag, constraints);
    constraints.gridwidth = constraints.REMAINDER;
    setupComponentIntoPanel(exportURLValue, gridbag, constraints);
    
    //ROW#2
    setupExportFileName();
    constraints.gridwidth = 1;
    setupComponentIntoPanel(exportFileNameLabel, gridbag, constraints);
    setupComponentIntoPanel(exportFileNameValue, gridbag, constraints);
    constraints.gridwidth = constraints.REMAINDER;    
    setupComponentIntoPanel(exportFileNameButton, gridbag, constraints);
    
    //ROW#3
    setupExportCommentsPath();
    constraints.gridwidth = 1;
    setupComponentIntoPanel(exportCommentsPathLabel, gridbag, constraints);
    setupComponentIntoPanel(exportCommentsPathValue, gridbag, constraints);
    constraints.gridwidth = constraints.REMAINDER;   
    setupComponentIntoPanel(exportCommentsPathButton, gridbag, constraints);
    
    //ROW#4
    setupExportAction();
    constraints.gridwidth = 1;
    constraints.gridwidth = constraints.REMAINDER;
    setupComponentIntoPanel(exportButton, gridbag, constraints);
  }  
  
  private void setupComponentIntoPanel(Component component, GridBagLayout gridbag, GridBagConstraints constraints) {
    gridbag.setConstraints(component, constraints);
    panel.add(component, constraints);
  }

  
  private void setupExportURL() {
    exportURLLabel = new Label("URL to de downloaded: ");
    exportURLValue = new TextField(config.getExportURL());
  }
  
  private void setupExportFileName() {
    exportFileNameLabel = new Label("Exported File Name: ");
    exportFileNameValue = new TextField(config.getExportFileName());
    exportFileNameValue.setEnabled(false);
    exportFileNameButton = new Button("Browse...");
    exportFileNameButton.setEnabled(false);
  }
  
  private void setupExportCommentsPath() {
    exportCommentsPathLabel = new Label("Exported Comments Path: ");
    exportCommentsPathValue = new TextField(config.getExportCommentsPath());
    exportCommentsPathValue.setEnabled(false);
    exportCommentsPathButton = new Button("Browse...");
    exportCommentsPathButton.setEnabled(false);
  }
  
  private void setupExportAction() {
    exportButton = new Button("Export");
    exportButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        service.downloadMainList(exportURLValue.getText());
        service.downloadComments();
        service.mergeCommentsIntoTxt();
      }
    });
  }
  
  public MainWindow() {
    setupMainFrame();
    service = new Service(new Messenger(mainFrame));
  }
  
  public void popup(String message) {
    JOptionPane.showMessageDialog(mainFrame, message);
  }
}