package main

import (
	"fmt"
	"time"
)

func TimeDifference(url string) {
	startTime := time.Now()
	SequentialCrawl(url)
	endTime := time.Now()
	fmt.Println("Sequential time: ", endTime.Sub(startTime).Milliseconds(), "ms")

	startTime = time.Now()
	ConcurrentCrawl(url)
	endTime = time.Now()
	fmt.Println("Concurrent time: ", endTime.Sub(startTime).Milliseconds(), "ms")
}
