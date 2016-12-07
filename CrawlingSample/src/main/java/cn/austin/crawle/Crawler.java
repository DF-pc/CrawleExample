/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.austin.crawle;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

/**
 *
 * @author Berton
 */
public class Crawler implements Runnable {

    BlockingQueue<String> messageQueue = null;
    private PageGenerator pageGenerator = null;

    public Crawler() {
    }

    public Crawler(BlockingQueue<String> messageQueue, PageGenerator pageGenerator) {
        this.messageQueue = messageQueue;
        this.pageGenerator = pageGenerator;
    }

    public String crawleTheContent(int currpage) throws IOException {
        String content = null;
        CloseableHttpResponse response = null;
        InputStream is = null;
        try {
            CloseableHttpClient httpclient = HttpClients.createDefault();
            int start = (currpage - 1) * 14 + 1;
            String url = "https://book.douban.com/subject_search?start=%s&search_text=%E7%BC%96%E7%A8%8B&cat=1001".replace("%s", String.valueOf(start));
            HttpGet httpGet = new HttpGet(url);

            httpGet.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
            httpGet.addHeader("Accept-Encoding", "gzip, deflate, sdch, br");
            httpGet.addHeader("Accept-Language", "zh-CN,zh;q=0.8");
            httpGet.addHeader("Connection", "keep-alive");
            httpGet.addHeader("Cookie", "gr_user_id=f2ae551f-d8be-472d-a17c-fb427123b958; viewed=\"1470240_11386364\"; ll=\"108296\"; bid=DT2w_1e4hnY; _vwo_uuid_v2=A0ADC053693BC0BAB806B37B77D3EE2E|58055d5c7afc6b50c4901fc93157dfdd; _pk_id.100001.3ac3=69b82fcb3d85b9cb.1481024949.4.1481087406.1481074949.; __utma=30149280.413198661.1441366971.1481074927.1481087106.26; __utmc=30149280; __utmz=30149280.1480767548.22.22.utmcsr=baidu|utmccn=(organic)|utmcmd=organic; __utma=81379588.563439035.1481024949.1481074927.1481087107.4; __utmc=81379588; __utmz=81379588.1481024949.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none)");
            httpGet.addHeader("Host", "book.douban.com");
            httpGet.addHeader("Referer", "https://book.douban.com/");
            httpGet.addHeader("Upgrade-Insecure-Requests", "1");
            httpGet.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");

            response = httpclient.execute(httpGet);
            HttpEntity entity = response.getEntity();

            is = entity.getContent();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            StringBuilder result = new StringBuilder();
            String read = null;
            while ((read = bufferedReader.readLine()) != null) {
                result.append(read);
            }
            content = result.toString();
        } finally {
            response.close();
            is.close();
        }
        return content;
    }

    @Override
    public void run() {

        while (true) {
            int currIndex = pageGenerator.getCurr();

            if (currIndex > pageGenerator.getMaxPage()) {
                break;
            }
            try {
                String content = crawleTheContent(currIndex);
                messageQueue.put(content);
            } catch (IOException | InterruptedException ex) {
                Logger.getLogger(Crawler.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

}
