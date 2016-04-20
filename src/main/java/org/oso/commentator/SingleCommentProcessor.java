package org.oso.commentator;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

import org.apache.pdfbox.pdmodel.PDDocument;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class SingleCommentProcessor implements Runnable {

  private String url;
  private static final String URL_MODEL = "https://www.regulations.gov/contentStreamer?documentId=DOCUMENT_ID&attachmentNumber=1&disposition=attachment&contentType=pdf";
  private static final String DOCUMENT_ID_VARIABLE = "DOCUMENT_ID";
  
  public SingleCommentProcessor(String documentId) {
    this.url = new String(URL_MODEL).replace(DOCUMENT_ID_VARIABLE, documentId);
  }
  
  @Override
  public void run() {
    System.out.println(url);
    URL urlConn;
    try {
      urlConn = new URL(this.url);
      System.out.println(this.url);
      PDDocument doc = PDDocument.load(urlConn.openStream());
      doc.toString();
      doc.save(new File("c:\\users\\juan\\dev\\temp\\" + UUID.randomUUID().toString() + ".pdf"));
      doc.close();
    } catch (MalformedURLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    
  }

}
