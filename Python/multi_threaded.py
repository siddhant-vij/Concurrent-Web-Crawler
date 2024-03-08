from typing import Final
from concurrent.futures import ThreadPoolExecutor, wait
import threading

from web_crawl_utils import fetch_and_parse


class MultiThreadedCrawler:
    def __init__(self, start_url: str, max_depth: int, max_urls: int) -> None:
        self.start_url: Final[str] = start_url
        self.max_depth: Final[int] = max_depth
        self.max_urls: Final[int] = max_urls
        self.visited: Final[set[str]] = set()
        self.lock: Final[threading.Lock] = threading.Lock()
        self.executor: Final[ThreadPoolExecutor] = ThreadPoolExecutor(max_workers=6)

    def start_crawling(self) -> None:
        future = self.executor.submit(
            fetch_and_parse,
            self.start_url,
            0,
            self.max_depth,
            self.max_urls,
            self.visited,
            self.lock,
            self.executor)
        wait([future])
        self.executor.shutdown()
        print(
            f"Total URLs crawled (MT): {len(self.visited)}")
