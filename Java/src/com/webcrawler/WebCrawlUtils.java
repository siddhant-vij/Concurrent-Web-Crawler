package com.webcrawler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.function.BiConsumer;

public class WebCrawlUtils {
  public static void fetchAndParse(String url, int depth, int maxDepth, BiConsumer<String, Integer> taskSubmitter) {
    try {
      Document doc = Jsoup.connect(url).get();
      // System.out.println("Fetched URL: " + url + " | Depth: " + depth + " | Title: " + doc.title());

      if (depth < maxDepth) {
        Elements links = doc.select("a[href]");
        links.forEach(link -> {
          String absUrl = link.attr("abs:href");
          try {
            if (taskSubmitter != null) {
              taskSubmitter.accept(absUrl, depth + 1);
            } else {
              fetchAndParse(absUrl, depth + 1, maxDepth, null);
            }
          } catch (Exception e) {
          }
        });
      }
    } catch (IOException e) {
    }
  }
}
