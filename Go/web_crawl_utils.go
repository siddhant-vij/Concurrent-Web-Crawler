package main

import (
	"context"
	"net/http"

	"golang.org/x/net/html"
)

func FetchAndParseURL(ctx context.Context, url string, depth, maxDepth int, taskSubmitter func(url string, depth int)) {
	if ctx.Err() != nil {
		return
	}

	resp, err := http.Get(url)
	if err != nil {
		return
	}
	defer resp.Body.Close()

	doc, err := html.Parse(resp.Body)
	if err != nil {
		return
	}

	visitNode := func(n *html.Node) {
		if n.Type == html.ElementNode && n.Data == "a" {
			for _, a := range n.Attr {
				if a.Key == "href" {
					absUrl, err := resp.Request.URL.Parse(a.Val)
					if err == nil {
						taskSubmitter(absUrl.String(), depth+1)
					}
				}
			}
		}
	}

	forEachNode(doc, visitNode, nil)
}

func forEachNode(n *html.Node, pre, post func(n *html.Node)) {
	if pre != nil {
		pre(n)
	}
	for c := n.FirstChild; c != nil; c = c.NextSibling {
		forEachNode(c, pre, post)
	}
	if post != nil {
		post(n)
	}
}
