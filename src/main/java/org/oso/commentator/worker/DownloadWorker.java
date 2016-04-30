package org.oso.commentator.worker;

import java.util.concurrent.Executor;

import org.oso.commentator.config.Config;
import org.oso.commentator.util.DownloadHandler;

public class DownloadWorker extends BaseWorker {

  private String url;
  private static final String URL_MODEL = "https://www.regulations.gov/contentStreamer?documentId=DOCUMENT_ID&attachmentNumber=1&disposition=attachment&contentType=pdf";
  private static final String DOCUMENT_ID_VARIABLE = "DOCUMENT_ID";
  private Config config = Config.getInstance();

  public DownloadWorker(Executor executor, String documentId) {
    super(executor, documentId);
    this.url = new String(URL_MODEL).replace(DOCUMENT_ID_VARIABLE, documentId);
  }

  @Override
  public void finish() {
    logger.info("Document " + getDocumentId() + " downloaded!");
  }

  @Override
  public void doAction() {
    String name = new StringBuilder()
        .append(System.getProperty("user.home"))
        .append(config.getExportCommentsPath())
        .append(getDocumentId())
        .append(".pdf")
        .toString();
    new DownloadHandler().download(url, name);
  }

}
