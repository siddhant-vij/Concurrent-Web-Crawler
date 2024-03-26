package main

const (
	maxDepth = 2
	maxUrls  = 500
)

func main() {
	startUrls := []string{"https://www.wikipedia.org/"}
	for _, url := range startUrls {
		TimeDifference(url)
	}
}

// Sequential Crawling finished. Total URLs crawled: 500
// Sequential time:  138033 ms
// Concurrent Crawling finished. Total URLs crawled: 500
// Concurrent time:  2694 ms