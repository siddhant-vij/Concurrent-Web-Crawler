from typing import Final

from time_performance import compare


def run() -> None:
    start_url: Final[str] = "https://www.wikipedia.org/"
    max_depth: Final[int] = 2
    max_urls: Final[int] = 250

    compare(start_url, max_depth, max_urls)


if __name__ == "__main__":
    run()


# Total URLs crawled (ST): 250
# Single-threaded time: 250.774 seconds
# Total URLs crawled (MT): 250
# Multi-threaded time: 50.144 seconds
