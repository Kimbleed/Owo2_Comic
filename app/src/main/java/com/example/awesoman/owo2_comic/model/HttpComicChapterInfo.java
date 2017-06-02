package com.example.awesoman.owo2_comic.model;

import java.io.Serializable;

/**
 * Created by Awesome on 2017/6/1.
 */

public class HttpComicChapterInfo implements Serializable{
    String name;
    String id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
