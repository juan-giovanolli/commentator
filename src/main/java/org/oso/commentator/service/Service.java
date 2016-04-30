package org.oso.commentator.service;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.oso.commentator.config.Config;
import org.oso.commentator.ui.Messenger;
import org.oso.commentator.util.DownloadHandler;
import org.oso.commentator.worker.DownloadWorker;
import org.oso.commentator.worker.PdfToTextWorker;

public class Service {

  private static final String DOCUMENT_DETAIL_HEADER = "Document Detail";
  private static final String DOCUMENT_ID_PATTERN = ".+D=(.+)";
  private static final Logger logger = LogManager.getRootLogger();
  private static Config config = Config.getInstance();
  private Messenger messenger;
  
  public Service(Messenger messenger) {
    this.messenger = messenger;
  }
  
  public void downloadMainList(String url) {
    if(url == null) {
      url = config.getExportURL();
    }
    new DownloadHandler().download(url, System.getProperty("user.home") + config.getExportFileName());
  }
  
  public void downloadComments() {
    iterateRecords(new IteratorAction() {
      @Override
      public void doAction(ExecutorService executor, String documentId) {
        new DownloadWorker(executor, documentId);
      }
    });
  }
  
  public void mergeCommentsIntoTxt() {
    final FileWriter fw;
    try {
      fw = new FileWriter(System.getProperty("user.home") + config.getExportCommentsMergedTxt());
      iterateRecords(new IteratorAction() {
        
        @Override
        public void doAction(ExecutorService executor, String documentId) {
          new PdfToTextWorker(executor, documentId, fw);
          
        }
      });            
      fw.close();
      messenger.displayPopupMessage("Finished! Check the file " + System.getProperty("user.home") + config.getExportCommentsMergedTxt() + "to see the results");
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
  
  interface IteratorAction {
    void doAction(ExecutorService executor, String documentId);
  }  
  
  private static void iterateRecords(IteratorAction action) {
    BufferedReader in;
    ExecutorService executor = Executors.newFixedThreadPool(5);
    try {
      in = new BufferedReader(new FileReader(System.getProperty("user.home") + config.getExportFileName()));
      for(int j =0; j < 5; j++) {
        System.out.println(in.readLine());
      }
      Iterable<CSVRecord> records = CSVFormat.EXCEL.withIgnoreEmptyLines().withHeader().parse(in);
      int count = 0;
      for (CSVRecord record : records) {
        String documentId = record.get(DOCUMENT_DETAIL_HEADER);
        Pattern pattern = Pattern.compile(DOCUMENT_ID_PATTERN);
        
        Matcher matcher = pattern.matcher(documentId);
        while(matcher.find()) {
          documentId = matcher.group(1);
        }
        count++;
        action.doAction(executor, documentId);
        if(count==1)break;
      }
      executor.shutdown();
      while (!executor.isTerminated()) {
        
      }
      in.close();
      logger.info("Finished Iteration");
    } catch (FileNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }  
  
}
