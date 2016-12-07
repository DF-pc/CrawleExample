/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.austin.crawler;

import cn.austin.crawle.Crawler;
import java.io.IOException;
import junit.framework.Assert;
import org.junit.Test;

/**
 *
 * @author Berton
 */
public class CrawlerTest {

    @Test
    public void testCrawleTheContent() throws IOException {
        Crawler crawler = new Crawler();
        String result = crawler.crawleTheContent(1);
        Assert.assertNotNull(result);
    }
}
