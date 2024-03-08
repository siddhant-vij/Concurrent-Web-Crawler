from typing import Set, Optional
from concurrent.futures import ThreadPoolExecutor
import threading
from urllib.parse import urljoin
import requests
import bs4


def fetch_and_parse(
        url: str,
        depth: int,
        max_depth: int,
        max_urls: int,
        visited: Set[str],
        lock: Optional[threading.Lock],
        executor: ThreadPoolExecutor) -> None:
    if depth > max_depth:
        return
    if lock:
        with lock:
            if url in visited or len(visited) >= max_urls:
                return
            visited.add(url)
    else:
        if url in visited or len(visited) >= max_urls:
            return
        visited.add(url)

    try:
        response = requests.get(url)
        if response.status_code != 200:
            return
    except requests.exceptions.RequestException:
        return

    soup = bs4.BeautifulSoup(response.text, 'html.parser')

    # if soup.title and soup.title.string:
    #     print(f"Depth: {depth}, Title: {soup.title.string}, URL: {url}")
    # else:
    #     print(f"Depth: {depth}, Title: None, URL: {url}")

    if depth == max_depth:
        return

    links = soup.find_all('a', href=True)
    for link in links:
        href = link.get('href')
        abs_url = urljoin(response.url, href)
        if lock:
            with lock:
                if len(visited) < max_urls and abs_url not in visited:
                    executor.submit(fetch_and_parse, abs_url, depth + 1, max_depth, max_urls, visited, lock, executor)
        else:
            if len(visited) < max_urls:
                fetch_and_parse(abs_url, depth + 1, max_depth,
                            max_urls, visited, None, None)
