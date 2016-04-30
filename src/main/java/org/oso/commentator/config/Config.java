package org.oso.commentator.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {

  private static Config config;
  private Properties prop;
  private static final String CONFIG_FILE_NAME = "/config.properties";

  public static Config getInstance() {
    if (config == null) {
      config = new Config();
    }
    return config;
  }

  private Config() {
    prop = new Properties();
    InputStream input = null;

    try {
      input = Config.class.getResourceAsStream(CONFIG_FILE_NAME);
      // load a properties file
      prop.load(input);
    } catch (IOException ex) {
      ex.printStackTrace();
    } finally {
      if (input != null) {
        try {
          input.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }

  private String getProperty(String key) {
    return prop.getProperty(key);
  }
  
  public String getExportURL() {
    return getProperty("export.url");
  }
  
  public String getExportFileName() {
    return getProperty("export.filename");
  }
  
  public String getExportCommentsPath() {
    return getProperty("export.comments.path");
  }
  
  public String getExportCommentsMergedTxt() {
    return getProperty("export.comments.mergedtxt");
  }

}
