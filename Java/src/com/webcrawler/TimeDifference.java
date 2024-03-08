package com.webcrawler;

public class TimeDifference {
  public static void compare(String startUrl, int depth, int maxUrls) {
    long startTime, endTime, singleThreadedTime, multiThreadedTime;

    // Single-threaded
    startTime = System.currentTimeMillis();
    new SingleThreaded(startUrl, depth, maxUrls).startCrawling();
    endTime = System.currentTimeMillis();
    singleThreadedTime = endTime - startTime;
    System.out.println("Single-threaded Time: " + singleThreadedTime + "ms");

    // Multi-threaded
    startTime = System.currentTimeMillis();
    new MultiThreaded(startUrl, depth, maxUrls).startCrawling();
    endTime = System.currentTimeMillis();
    multiThreadedTime = endTime - startTime;

    System.out.println("Multi-threaded Time: " + multiThreadedTime + "ms");
  }
}
