package com.example.awesoman.owo2_comic.model;

import java.util.List;

/**
 * Created by Awesome on 2017/6/1.
 */

public class ComicChapterResult {
    int total ;
    int limit;
    List<HttpComicChapterInfo> chapterList ;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public List<HttpComicChapterInfo> getBookList() {
        return chapterList;
    }

    public void setBookList(List<HttpComicChapterInfo> chapterList) {
        this.chapterList = chapterList;
    }
}
