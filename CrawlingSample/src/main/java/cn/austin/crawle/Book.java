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
public class Book {

    private String name;
    private String info;
    private float score;
    private int commentNum;
    private String addition;

    public Book(String name, String Info, float score, int commentNum, String addition) {
        this.name = name;
        this.info = Info;
        this.score = score;
        this.commentNum = commentNum;
        this.addition = addition;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String Info) {
        this.info = Info;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public int getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(int commentNum) {
        this.commentNum = commentNum;
    }

    public String getAddition() {
        return addition;
    }

    public void setAddition(String addition) {
        this.addition = addition;
    }

    @Override
    public String toString() {
        return name + "," + info + "," + score + "," + commentNum + "," + addition + "\n";
    }
}
