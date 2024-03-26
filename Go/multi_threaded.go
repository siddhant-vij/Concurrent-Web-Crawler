package main

import (
	"context"
	"fmt"
	"runtime"
	"sync"
)

type crawlTask struct {
	url   string
	depth int
}

type crawlerState struct {
	mu          sync.Mutex
	urlCount    int
	urlsVisited map[string]bool
	maxDepth    int
	maxUrls     int
	ctx         context.Context
	cancel      context.CancelFunc
}

func ConcurrentCrawl(startURL string) {
	ctx, cancel := context.WithCancel(context.Background())
	state := &crawlerState{
		urlsVisited: make(map[string]bool),
		maxDepth:    maxDepth,
		maxUrls:     maxUrls,
		ctx:         ctx,
		cancel:      cancel,
	}
	taskChan := make(chan crawlTask, maxUrls)
	var wg sync.WaitGroup

	wg.Add(1)
	go func() {
		defer wg.Done()
		taskChan <- crawlTask{url: startURL, depth: 0}
	}()

	for i := 0; i < runtime.NumCPU(); i++ {
		wg.Add(1)
		go worker(&wg, state, taskChan)
	}

	wg.Wait()
	fmt.Printf("Concurrent Crawling finished. Total URLs crawled: %d\n", state.urlCount)
}

func worker(wg *sync.WaitGroup, state *crawlerState, taskChan chan crawlTask) {
	defer wg.Done()
	for {
		select {
		case task := <-taskChan:
			if state.ctx.Err() != nil {
				return
			}
			processTask(wg, task, state, taskChan)
		case <-state.ctx.Done():
			return
		}
	}
}

func processTask(wg *sync.WaitGroup, task crawlTask, state *crawlerState, taskChan chan crawlTask) {
	state.mu.Lock()
	if state.urlCount >= state.maxUrls || task.depth > state.maxDepth || state.urlsVisited[task.url] {
		if state.urlCount >= state.maxUrls {
			state.cancel()
		}
		state.mu.Unlock()
		return
	}
	state.urlsVisited[task.url] = true
	state.urlCount++
	// fmt.Printf("Visiting URL #%d: %s at depth: %d\n", state.urlCount, task.url, task.depth)
	state.mu.Unlock()


	if state.urlCount < state.maxUrls && task.depth+1 <= state.maxDepth {
		wg.Add(1)
		go func() {
			defer wg.Done()
			FetchAndParseURL(state.ctx, task.url, task.depth, state.maxDepth, func(url string, depth int) {
				select {
				case taskChan <- crawlTask{url: url, depth: depth}:
				case <-state.ctx.Done():
				}
			})
		}()
	}
}
