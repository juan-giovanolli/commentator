package org.oso.commentator;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.oso.commentator.ui.MainWindow;

/**
 * 
 *
 */
public class App {
  private static final Logger logger = LogManager.getRootLogger();
  private static MainWindow mainWindow;
  public static void main(String[] args) {
    mainWindow = new MainWindow();
    createDirectories();

  }
  
  public static void createDirectories() {
    String baseFolderName = System.getProperty("user.home") + "\\commentator";
    File mainFolder = new File(baseFolderName);
    if(mainFolder.exists()) {
      String datetime = new SimpleDateFormat("yyyy-dd-MM-HH-mm-ss").format(new Date());
      String backupFolderName = baseFolderName + datetime;
      mainFolder.renameTo(new File(backupFolderName));
      logger.info("Output Folder already exists. Backing it up to " + backupFolderName);
    }
    logger.info("Creating new Output Folder: " + baseFolderName + "\\comments");
    if(new File(baseFolderName + "\\comments").mkdirs()) {
      mainWindow.popup("Output Folder created successfully");
      logger.info("Output Folder created successfully");
    } else {
      mainWindow.popup("There was some problem creating the output folder. Close the program and open it again");
      logger.info("There was some problem creating the output folder. Close the program and open it again");
    }
  }
}
