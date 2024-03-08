package com.webcrawler;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class MultiThreaded {
  private final ExecutorService executor;
  private final Phaser phaser;
  private final String startUrl;
  private final int maxDepth;
  private final int maxUrls;
  private final Set<String> visited = ConcurrentHashMap.newKeySet();
  private final AtomicInteger urlCount = new AtomicInteger(0);

  public MultiThreaded(String startUrl, int maxDepth, int maxUrls) {
    this.startUrl = startUrl;
    this.maxDepth = maxDepth;
    this.maxUrls = maxUrls;
    this.executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    this.phaser = new Phaser(1);
  }

  public void startCrawling() {
    submitTask(startUrl, 0);
    phaser.arriveAndAwaitAdvance();
    executor.shutdown();
    try {
      if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
        executor.shutdownNow();
      }
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    } finally {
      System.out.println("MT Crawling finished. Total URLs crawled: " + urlCount.get());
    }
  }

  private void submitTask(String url, int depth) {
    final int currentCount = urlCount.get();
    if (currentCount < this.maxUrls && urlCount.compareAndSet(currentCount, currentCount + 1)) {
      phaser.register();
      executor.submit(() -> {
        try {
          if (depth < maxDepth && visited.add(url)) {
            WebCrawlUtils.fetchAndParse(url, depth, maxDepth, this::submitTask);
          }
        } finally {
          phaser.arriveAndDeregister();
        }
      });
    }
  }

}
