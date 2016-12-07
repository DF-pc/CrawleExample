/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.austin.crawle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Berton
 */
public class App {

    public static void main(String[] args) throws IOException, InterruptedException {

        BlockingQueue<String> messageQueue = new LinkedBlockingQueue<>(100);

        Parser parser = new Parser(messageQueue);

        PageGenerator pageGenerator = new PageGenerator(parser.getMaxPage(new Crawler().crawleTheContent(1)), 1);
        ExecutorService threadPool = Executors.newFixedThreadPool(5);
        for (int i = 0; i < 1; i++) {
            threadPool.execute(new Crawler(messageQueue, pageGenerator));
        }
//        try (InputStream in = App.class.getClassLoader().getResourceAsStream("test.html")) {
//            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
//
//            StringBuilder result = new StringBuilder();
//            String read = null;
//            while ((read = bufferedReader.readLine()) != null) {
//                result.append(read);
//            }
//            String page1 = result.toString();
//            messageQueue.put(page1);
//        }

        threadPool.execute(new Thread(parser));
//        threadPool.shutdown();

        Logger.getLogger(Crawler.class.getName()).log(Level.SEVERE, null, "main thread finished");
    }

}
