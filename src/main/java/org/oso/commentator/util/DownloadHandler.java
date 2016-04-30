package org.oso.commentator.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DownloadHandler {

  private static final Logger logger = LogManager.getRootLogger();
  
  public void download(String url, String fileName) {
    URL downloadURL;
    try {
      downloadURL = new URL(url);
      logger.debug(String.format("Downloading %s into %s...", url, fileName));
      ReadableByteChannel rbc = Channels.newChannel(downloadURL.openStream());
      if(!new File(fileName).exists()) {
        FileOutputStream fos = new FileOutputStream(fileName);
        FileChannel fileChannel = fos.getChannel();
        fileChannel.transferFrom(rbc, 0, Long.MAX_VALUE);
        fileChannel.close();
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
