/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.austin.crawle;

import cn.wanghaomiao.xpath.exception.XpathSyntaxErrorException;
import cn.wanghaomiao.xpath.model.JXDocument;
import cn.wanghaomiao.xpath.model.JXNode;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static java.util.stream.Collectors.toList;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author Berton
 */
public class Parser implements Runnable {

    BlockingQueue<String> messageQueue;

    public Parser() {
    }

    public Parser(BlockingQueue<String> messageQueue) {
        this.messageQueue = messageQueue;
    }

    public List<Book> parsing(String html) throws XpathSyntaxErrorException {

//        DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
//        domFactory.setNamespaceAware(true);
//        DocumentBuilder builder = domFactory.newDocumentBuilder();
//
//        InputStream ins = new ByteArrayInputStream(html.getBytes("UTF-8"));
//        Document doc = builder.parse(ins);
//
//        XPath xPath = XPathFactory.newInstance().newXPath();
//        NodeList nodeList = (NodeList) xPath.compile("//div[@id='content']/div/div[@class = 'article']/ul[class='sub-list']").evaluate(doc, XPathConstants.NODESET);
//        List<Book> books = new ArrayList<>();
//        String name = null;
//        String info = null;
//        float score = 0f;
//        int commentNum = 0;
//        String addition = null;
//        for (int i = 0; i < nodeList.getLength(); i++) {
//            Node node = nodeList.item(i);
//            name = (String) xPath.compile("div[@class='info']/h2/a/text()").evaluate(node, XPathConstants.STRING);
//            info = (String) xPath.compile("div[@class='info']/div[class='pub']/text()").evaluate(node, XPathConstants.STRING);
//            String scoreString = (String) xPath.compile("div[@class='info']/div[class='star clearfix']/span[class ='rating_nums']/text()").evaluate(node, XPathConstants.STRING);
//            score = Float.valueOf(scoreString);
//            String commentsNumString = (String) xPath.compile("div[@class='info']/div[class='star clearfix']/span[class ='pl']/text()").evaluate(node, XPathConstants.STRING);
//            commentNum = Integer.valueOf(commentsNumString);//Regex
//            addition = (String) xPath.compile("div[@class='info']/div[class='ft']/span[class ='buy-info']/a/text()").evaluate(node, XPathConstants.STRING);
//            books.add(new Book(name, info, score, commentNum, addition));
//        }
//        ins.close();
//        return books;
        JXDocument jxDocument = new JXDocument(html);
//        String xpath = "//div[@id='content']/div/div/ul[@class='subject-list']/li[@[class='subject-item']";
        String xpath = "//div[@id='content']/div/div/ul/li";
        List<JXNode> jxNodeList = jxDocument.selN(xpath);

        List<Book> books = new ArrayList<>();
        String name;
        String info;
        float score;
        int commentNum;
        String addition;

        for (JXNode node : jxNodeList) {
            name = StringUtils.join(node.sel("div[@class='info']/h2/a/text()"), "");
            info = StringUtils.join(node.sel("div[@class='info']/div[@class='pub']/text()"), "");
            String scoreString = (String) StringUtils.join(node.sel("div[@class='info']/div[@class='star clearfix']/span[@class ='rating_nums']/text()"), "");
            score = scoreString.trim().equals("") ? 0f : Float.valueOf(scoreString);
            String commentsNumString = (String) StringUtils.join(node.sel("div[@class='info']/div[@class='star clearfix']/span[@class ='pl']/text()"), "");
            Matcher matcher = Pattern.compile("\\d+").matcher(commentsNumString);
            String temp = null;
            while (matcher.find()) {
                temp = matcher.group();
            }
            commentNum = temp == null ? 0 : Integer.valueOf(temp);

            addition = StringUtils.join(node.sel("div[@class='info']/div[@class='ft']/span[class ='buy-info']/a/text()"), "");
            books.add(new Book(name, info, score, commentNum, addition));
        }
        return books;
    }

    public int getMaxPage(String html) {
        JXDocument jxDocument = new JXDocument(html);
        String xpath = "//div[@id='content']/div/div/div[@class='paginator']/a/text()";
        List<JXNode> jxNodeList;
        try {
            jxNodeList = jxDocument.selN(xpath);
            String ownText = jxNodeList.get(jxNodeList.size() - 1).getTextVal();
            return Integer.valueOf(ownText);
        } catch (XpathSyntaxErrorException ex) {
            Logger.getLogger(Parser.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    public List<Book> filterAndSort(List<Book> old, List<Book> newBooks) {
        old = new ArrayList<>(old);
        newBooks = new ArrayList<>(newBooks);
        old.addAll(newBooks);
        List<Book> temp = old.stream().filter(x -> x.getCommentNum() > 1000).sorted((Book b1, Book b2) -> {
            return b1.getScore() < b2.getScore() ? 1 : -1;
        }).collect(toList());

        Book[] books = new Book[100];
        for (int i = 0; i < books.length; i++) {
            if (i < temp.size()) {
                books[i] = temp.get(i);
            }
        }
        old = Arrays.asList(books);
        return old;
    }

    @Override
    public void run() {
        List<Book> reservedBooks = new ArrayList<>();
        while (true) {
            try {
                URL resource = Parser.class.getClassLoader().getResource("result.txt");
                try (FileWriter writer = new FileWriter(new File(resource.getPath()))) {
                    reservedBooks = reservedBooks.stream().filter(x -> x != null).collect(toList());
                    for (Book x : reservedBooks) {
                        writer.write(x.toString());
                    }
                    writer.flush();
                }
                List<Book> parsedBooks = parsing(messageQueue.take());
                reservedBooks = filterAndSort(reservedBooks, parsedBooks);
            } catch (Exception ex) {
                Logger.getLogger(Parser.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
