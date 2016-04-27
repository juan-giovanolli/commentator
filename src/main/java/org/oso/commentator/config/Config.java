package org.oso.commentator.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {

  private static Config config;
  private Properties prop;

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
      System.out.println(System.getProperty("user.dir"));;
      input = new FileInputStream("config.properties");

      // load a properties file
      prop.load(input);

      // get the property value and print it out
      System.out.println(prop.getProperty("database"));
      System.out.println(prop.getProperty("dbuser"));
      System.out.println(prop.getProperty("dbpassword"));

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

}
