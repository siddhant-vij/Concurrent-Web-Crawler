package main

import (
	"context"
	"fmt"
)

var urlCount int = 0

func SequentialCrawl(url string) {
	fetchAndParse(url, 0)
	fmt.Printf("Sequential Crawling finished. Total URLs crawled: %d\n", urlCount)
}

func fetchAndParse(url string, depth int) {
	if depth > maxDepth || urlCount >= maxUrls {
		return
	}
	urlCount++
	// fmt.Printf("Visiting URL #%d: %s at depth: %d\n", urlCount, url, depth)
	if urlCount >= maxUrls {
		return
	}
	FetchAndParseURL(context.TODO(), url, depth, maxDepth, fetchAndParse)
}
