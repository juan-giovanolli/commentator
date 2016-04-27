package org.oso.commentator;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.oso.commentator.config.Config;
import org.oso.commentator.util.DownloadHandler;
import org.oso.commentator.worker.DownloadWorker;
import org.oso.commentator.worker.PdfToTextWorker;
/**
 * 
 *
 */
public class App 
{
  private static final String DOCUMENT_DETAIL_HEADER = "Document Detail";
  private static final String DOCUMENT_ID_PATTERN = ".+D=(.+)";
  private static final Logger logger = LogManager.getRootLogger();
  private static Config config = Config.getInstance();
  
  
    public static void main( String[] args )
    {
        new DownloadHandler().download(config.getExportURL(), config.getExportFileName());
        
        iterateRecords(new IteratorAction() {
          
          @Override
          public void doAction(ExecutorService executor, String documentId) {
            new DownloadWorker(executor, documentId);
            
          }
        });

        final FileWriter fw;
        try {
          fw = new FileWriter(config.getExportFileName());
          iterateRecords(new IteratorAction() {
            
            @Override
            public void doAction(ExecutorService executor, String documentId) {
              new PdfToTextWorker(executor, documentId, fw);
              
            }
          });            
          
        } catch (IOException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
        
        
//        Reader in;
//        ExecutorService executor = Executors.newFixedThreadPool(5);
//        try {
//          in = new FileReader(config.getExportFileName());
//          Iterable<CSVRecord> records = CSVFormat.EXCEL.withHeader().parse(in);
//          logger.info(new Date());
//          int count = 0;
//          for (CSVRecord record : records) {
//              String documentId = record.get(DOCUMENT_DETAIL_HEADER);
//              Pattern pattern = Pattern.compile(DOCUMENT_ID_PATTERN);
//              
//              Matcher matcher = pattern.matcher(documentId);
//              while(matcher.find()) {
//                documentId = matcher.group(1);
//              }
//              count++;
//              new DownloadWorker(executor, documentId);
//              if(count==1)break;
//          }
//          executor.shutdown();
//          while (!executor.isTerminated()) {
//            
//          }
//          final FileWriter fw;
//          try {
//            fw = new FileWriter(config.getExportFileName());
//          } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//          }
//
//
//          
//          count =0;
//          
//          
//          in = new FileReader(config.getExportFileName());
//          records = CSVFormat.EXCEL.withHeader().parse(in);
//          executor = Executors.newFixedThreadPool(5);
//          for (CSVRecord record : records) {
//            String documentId = record.get(DOCUMENT_DETAIL_HEADER);
//            Pattern pattern = Pattern.compile(DOCUMENT_ID_PATTERN);
//            
//            Matcher matcher = pattern.matcher(documentId);
//            while(matcher.find()) {
//              documentId = matcher.group(1);
//            }
//            count++;
//            new PdfToTextWorker(executor, documentId, fw);
//            if(count==1)break;
//          }
//          executor.shutdown();
//          while (!executor.isTerminated()) {
//            
//          }
//          fw.close();
//          logger.info(new Date());
//        } catch (FileNotFoundException e) {
//          // TODO Auto-generated catch block
//          e.printStackTrace();
//        } catch (IOException e) {
//          // TODO Auto-generated catch block
//          e.printStackTrace();
//        }
        
    }
    
    interface IteratorAction {
      void doAction(ExecutorService executor, String documentId);
    }
    
    private static void iterateRecords(IteratorAction action) {
      Reader in;
      ExecutorService executor = Executors.newFixedThreadPool(5);
      try {
        in = new FileReader(config.getExportFileName());
        Iterable<CSVRecord> records = CSVFormat.EXCEL.withHeader().parse(in);
        logger.info(new Date());
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
      } catch (FileNotFoundException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
}
