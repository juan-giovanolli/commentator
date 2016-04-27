package org.oso.commentator.worker;

import java.util.concurrent.Executor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class BaseWorker implements Runnable {
  protected static final Logger logger = LogManager.getRootLogger();
  private Executor executor;
  private String documentId;

  public BaseWorker(Executor executor, String documentId) {
    this.executor = executor;
    this.documentId = documentId;
    executor.execute(this);
  }

  public void run() {
    doAction();
    finish();
  }

  public Executor getExecutor() {
    return executor;
  }

  public void setExecutor(Executor executor) {
    this.executor = executor;
  }

  public String getDocumentId() {
    return documentId;
  }

  public void setDocumentId(String documentId) {
    this.documentId = documentId;
  }

  public abstract void doAction();

  public abstract void finish();
}
