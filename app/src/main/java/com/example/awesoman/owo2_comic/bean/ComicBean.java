package com.example.awesoman.owo2_comic.bean;

import java.io.Serializable;

/**
 * Created by Awesome on 2016/11/7.
 */

public class ComicBean implements Serializable{
    String comicName;
    String comicPath;

    public String getComicName() {
        return comicName;
    }

    public void setComicName(String comicName) {
        this.comicName = comicName;
    }

    public String getComicPath() {
        return comicPath;
    }

    public void setComicPath(String comicPath) {
        this.comicPath = comicPath;
    }
}
