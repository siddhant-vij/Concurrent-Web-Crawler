#include "hello_world.h"

int main()
{
  print_hello_world();
  return 0;
}

// https: // stackoverflow.com/questions/4278024/very-simple-c-web-crawler-spider

// C++ isn't the greatest language to write a webcrawler in. The raw-performance and low-level access you get in C++ is useless when writing a program like a webcrawler, which spends most of its time waiting for URLs to resolve and download.