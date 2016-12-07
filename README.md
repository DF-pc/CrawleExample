# CrawleExample

This repository is a simple demonstration of how to grasp book information from book.douban.com in JAVA platform. 

Since the project is aimed at crawling from the specified website, you need to make some changes when using directly. Proxied IPs could be be used in your project if anti-reptiles exists in the website.

Basic Requirements:
  (1) Apache project HttpComponents, a http client
  (2) JsoupXpath, a third-party tooos that supports XPath Language
  
QuickStart:
    # a MQ
    BlockingQueue<String> messageQueue = new LinkedBlockingQueue<>(100);
    # supports multi-producer-one-consumer model, 
    Parser parser = new Parser(messageQueue);
    PageGenerator pageGenerator = new PageGenerator(parser.getMaxPage(new Crawler().crawleTheContent(1)), 1);
    ExecutorService threadPool = Executors.newFixedThreadPool(5);
    for (int i = 0; i < 5; i++) {
        threadPool.execute(new Crawler(messageQueue, pageGenerator));
    }
    threadPool.execute(new Thread(parser));

Result:
    The result is stored in the file of result.txt, which has some kinds of format. you can open it using excel to open and save it, and the separator is commas.
    
