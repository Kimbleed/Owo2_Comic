package com.example.awesoman.owo2_comic.model;

import java.util.List;

/**
 * Created by Awesome on 2017/6/1.
 */

public class ComicBookResult {
    int total ;
    int limit;
    List<HttpComicInfo> bookList ;

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

    public List<HttpComicInfo> getBookList() {
        return bookList;
    }

    public void setBookList(List<HttpComicInfo> bookList) {
        this.bookList = bookList;
    }
}
