package org.oso.commentator.worker;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.Executor;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

public class PdfToTextWorker extends BaseWorker {

  private FileWriter fileWriter;
  
  public PdfToTextWorker(Executor executor, String documentId, FileWriter fileWriter) {
    super(executor, documentId);
    this.fileWriter = fileWriter;
  }

  @Override
  public void doAction() {
    try {
      String name = "c:\\users\\juan\\dev\\temp\\" + getDocumentId() + ".pdf";
      PDDocument doc = PDDocument.load(new File(name));
      PDFTextStripper stripper = new PDFTextStripper();
      StringBuilder sb = new StringBuilder("-----------------" + getDocumentId() + "-----------------\r\n");
      sb.append(stripper.getText(doc));
      fileWriter.append(sb.toString());
      doc.close();
    } catch (IOException e) {
      logger.error(getDocumentId() + ">" + e.getMessage());
    } 
  }

  @Override
  public void finish() {
   logger.info("Document " + getDocumentId() + " processed!");

  }

}
