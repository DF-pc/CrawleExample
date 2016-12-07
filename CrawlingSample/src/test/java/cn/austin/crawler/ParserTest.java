/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.austin.crawler;

import cn.austin.crawle.Book;
import cn.austin.crawle.Parser;
import cn.wanghaomiao.xpath.exception.XpathSyntaxErrorException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

/**
 *
 * @author Berton
 */
public class ParserTest {

    Parser parser;
    List<Book> old;
    List<Book> newBooks;
    String htmlPage;

    @Before
    public void inti() throws IOException {
        parser = new Parser();
        old = Arrays.asList(new Book("python", "good book by austin", 3.0f, 1200, "paper version:23"),
                new Book("java", "good book by austin", 23.0f, 2200, "paper version:23"),
                new Book("js", "good book by austin", 123.0f, 200, "paper version:23"),
                new Book("c#", "good book by austin", 223.0f, 200, "paper version:23"),
                new Book("nodejs", "good book by austin", 323.0f, 500, "paper version:23"));
        newBooks = Arrays.asList(new Book("c", "good book by austin", 25.0f, 800, "paper version:23"),
                new Book("c++", "good book by austin", 221.0f, 3200, "paper version:23"));
        try (InputStream in = ParserTest.class.getClassLoader().getResourceAsStream("test.html")) {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in, "UTF-8"));

            StringBuilder result = new StringBuilder();
            String read = null;
            while ((read = bufferedReader.readLine()) != null) {
                result.append(read);
            }
            htmlPage = result.toString();
        }
    }

//    @Test
    public void testFilterAndSort() {

        List<Book> filterAndSort = parser.filterAndSort(old, newBooks);
        Assert.assertEquals("c++", filterAndSort.get(2).getName());
    }

    @Test
    public void testParsing() throws FileNotFoundException, IOException, ParserConfigurationException, SAXException, XPathExpressionException, XpathSyntaxErrorException {

        List<Book> books = parser.parsing(htmlPage);
        Assert.assertEquals(14, books.size());

    }

    @Test
    public void testgetMaxPage() throws XpathSyntaxErrorException {
        int maxPage = parser.getMaxPage(htmlPage);
        Assert.assertEquals(747, maxPage);
    }

    @Test
    public void testoperateFile() throws IOException {
        URL resource = ParserTest.class.getClassLoader().getResource("result.txt");
        try (FileWriter writer = new FileWriter(new File(resource.getPath()))) {
            writer.write("abcdeffffffffffffffffffffffffffffffffffffffffffff");
            writer.flush();
        }
    }

}
