package com.example.awesoman.owo2_comic.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by Awesome on 2016/11/7.
 */

@DatabaseTable(tableName = "tb_comic")
public class ComicInfo implements Serializable {

    @DatabaseField(generatedId = true)
    int id;

    @DatabaseField(columnName = "comic_name")
    String comicName;

    @DatabaseField(columnName = "comic_path")
    String comicPath;

    @DatabaseField(columnName = "comic_type")
    int comicType;

    public ComicInfo() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public void setComicType(int comicType) {
        this.comicType = comicType;
    }

    public int getComicType() {
        return comicType;
    }
}
