package com.example.awesoman.owo2_comic.model;

import java.util.List;

/**
 * Created by Awesome on 2017/6/2.
 */

public class ComicContentResult {
    int total ;
    int limit;
    List<HttpImageInfo> imageList ;

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

    public List<HttpImageInfo> getImageList() {
        return imageList;
    }

    public void setImageList(List<HttpImageInfo> imageList) {
        this.imageList = imageList;
    }
}
