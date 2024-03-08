package com.webcrawler;

public class SingleThreaded {
  private final String startUrl;
  private final int maxDepth;
  private final int maxUrls;
  private int urlCount = 0;

  public SingleThreaded(String startUrl, int maxDepth, int maxUrls) {
    this.startUrl = startUrl;
    this.maxDepth = maxDepth;
    this.maxUrls = maxUrls;
  }

  public void startCrawling() {
    fetchAndParse(startUrl, 0);
    System.out.println("ST Crawling finished. Total URLs crawled: " + urlCount);
  }

  private void fetchAndParse(String url, int depth) {
    if (urlCount >= this.maxUrls) {
      return;
    }
    urlCount++;
    WebCrawlUtils.fetchAndParse(url, depth, maxDepth, this::fetchAndParse);
  }
}
