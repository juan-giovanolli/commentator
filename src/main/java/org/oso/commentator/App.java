package org.oso.commentator;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        
        Reader in;
        try {
          in = new FileReader("C:\\Users\\Juan\\Downloads\\DOCKET_EPA-HQ-OAR-2013-0602.csv");
          Iterable<CSVRecord> records = CSVFormat.EXCEL.withHeader().parse(in);
          System.out.println(new Date());
          int count = 0;
          ThreadGroup tg = new ThreadGroup("primero");
          
          for (CSVRecord record : records) {
              String documentId = record.get("Document Detail");
              Pattern pattern = Pattern.compile(".+D=(.+)");
              
              Matcher matcher = pattern.matcher(documentId);
              while(matcher.find()) {
                documentId = matcher.group(1);
                System.out.println(matcher.group(1));
              }
              
              SingleCommentProcessor sp = new SingleCommentProcessor(documentId);
              Thread t = new Thread(tg, sp);
              t.start();
              count++;
              if (count == 9) break;
          }
          
          System.out.println(new Date());
        } catch (FileNotFoundException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        } catch (IOException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
        
    }
}
