from typing import Final

from single_threaded import SingleThreadedCrawler
from multi_threaded import MultiThreadedCrawler
import time


def compare(start_url: str,
            max_depth: int,
            max_urls: int) -> None:
    start_time = time.time()
    single_threaded_crawler = SingleThreadedCrawler(
        start_url, max_depth, max_urls)
    single_threaded_crawler.start_crawling()
    single_elapsed: float = time.time() - start_time
    print(f"Single-threaded time: {single_elapsed:.3f} seconds")

    start_time = time.time()
    multi_threaded_crawler = MultiThreadedCrawler(
        start_url, max_depth, max_urls)
    multi_threaded_crawler.start_crawling()
    multi_elapsed = time.time() - start_time

    print(f"Multi-threaded time: {multi_elapsed:.3f} seconds")
