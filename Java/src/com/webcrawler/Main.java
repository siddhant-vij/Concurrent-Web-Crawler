package com.webcrawler;

import java.util.List;

public class Main {
  public static void main(String[] args) {
    List<String> startUrls = List.of(
        "https://www.wikipedia.org/");
    int depth = 2;
    // Add more start URLs & change the depth if needed
    final int MAX_URLS = 250;

    startUrls.forEach(url -> TimeDifference.compare(url, depth, MAX_URLS));
  }
}

// For MAX_URLS = 250
// ST Crawling finished. Total URLs crawled: 250
// Single-threaded Time: 135787ms
// MT Crawling finished. Total URLs crawled: 250
// Multi-threaded Time: 21376ms

// For MAX_URLS = 250
// ST Crawling finished. Total URLs crawled:250
// Single-threaded Time: 88173ms
// MT Crawling finished. Total URLs crawled:250
// Multi-threaded Time: 19340ms