from typing import Final, Set

from web_crawl_utils import fetch_and_parse


class SingleThreadedCrawler:
    def __init__(self, start_url: str, max_depth: int, max_urls: int) -> None:
        self.start_url: Final[str] = start_url
        self.max_depth: Final[int] = max_depth
        self.max_urls: Final[int] = max_urls
        self.visited: Final[Set[str]] = set()

    def start_crawling(self) -> None:
        fetch_and_parse(self.start_url, 0, self.max_depth,
                        self.max_urls, self.visited, None, None)
        print(f"Total URLs crawled (ST): {len(self.visited)}")
