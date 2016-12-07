/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.austin.crawle;

/**
 *
 * @author Berton
 */
public class PageGenerator {

    private int maxPage;
    private int currPage;

    public PageGenerator(int maxPage, int currPage) {
        this.maxPage = maxPage;
        this.currPage = currPage;
    }

    public int getMaxPage() {
        return maxPage;
    }

    public synchronized int getCurr() {
        int m = currPage;
        currPage++;
        return m;
    }
}
