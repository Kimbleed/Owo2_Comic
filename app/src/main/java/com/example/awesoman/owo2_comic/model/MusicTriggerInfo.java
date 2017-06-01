package com.example.awesoman.owo2_comic.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Awesome on 2017/6/1.
 */
@DatabaseTable(tableName = "tb_music_trigger")
public class MusicTriggerInfo {

    @DatabaseField(generatedId = true)
    int id;

    @DatabaseField(columnName = "music_path")
    String musicPath;

    @DatabaseField(columnName = "comic_name")
    String comicName;

    @DatabaseField(columnName = "comic_chapter")
    String comicChapter;

    @DatabaseField(columnName = "comic_page")
    int page;

    public MusicTriggerInfo() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMusicPath() {
        return musicPath;
    }

    public void setMusicPath(String musicPath) {
        this.musicPath = musicPath;
    }

    public String getComicName() {
        return comicName;
    }

    public void setComicName(String comicName) {
        this.comicName = comicName;
    }

    public String getComicChapter() {
        return comicChapter;
    }

    public void setComicChapter(String comicChapter) {
        this.comicChapter = comicChapter;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }
}
