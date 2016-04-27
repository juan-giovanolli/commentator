package org.oso.commentator.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DownloadHandler {

  private static final Logger logger = LogManager.getRootLogger();
  
  public void download(String url, String fileName) {
    //String url = "https://www.regulations.gov/exportdocket?docketId=EPA-HQ-OAR-2013-0602";
    URL downloadURL;
    //String fileName = "c:\\users\\juan\\dev\\rawlist.txt";
    try {
      downloadURL = new URL(url);
      ReadableByteChannel rbc = Channels.newChannel(downloadURL.openStream());
      if(!new File(fileName).exists()) {
        new FileOutputStream(fileName).getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
      } else {
        logger.info("Skipped rawlist: Already exists");
      }
    } catch (MalformedURLException e) {
      logger.info(url);
      e.printStackTrace();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      logger.info(e.getMessage());
    }
  }
}
