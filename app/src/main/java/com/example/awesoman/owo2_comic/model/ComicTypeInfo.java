package com.example.awesoman.owo2_comic.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Awesome on 2016/11/7.
 */

@DatabaseTable(tableName = "tb_comic_type")
public class ComicTypeInfo {

    @DatabaseField(generatedId = true)
    int id;

    @DatabaseField(columnName = "comic_type_name")
    String ComicTypeName;


    @DatabaseField(columnName ="comic_type_no" )
    int ComicTypeNo;

    public ComicTypeInfo() {
    }

    public String getComicTypeName() {
        return ComicTypeName;
    }

    public void setComicTypeName(String comicTypeName) {
        ComicTypeName = comicTypeName;
    }

    public int getComicTypeNo() {
        return ComicTypeNo;
    }

    public void setComicTypeNo(int comicTypeNo) {
        ComicTypeNo = comicTypeNo;
    }
}
