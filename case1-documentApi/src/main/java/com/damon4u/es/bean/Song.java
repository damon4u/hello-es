package com.damon4u.es.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

/**
 * Description:
 *
 * @author damon4u
 * @version 2017-02-17 16:54
 */
public class Song {
    @JsonProperty("歌曲名称")
    private String name;
    @JsonProperty("歌手名称")
    private String singer;
    @JsonProperty("发行时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date releaseDate;
    @JsonProperty("评论数")
    private long commentCount;

    public Song() {
    }

    public Song(String name, String singer, Date releaseDate, long commentCount) {
        this.name = name;
        this.singer = singer;
        this.releaseDate = releaseDate;
        this.commentCount = commentCount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public long getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(long commentCount) {
        this.commentCount = commentCount;
    }

    @Override
    public String toString() {
        return "Song{" +
                "name='" + name + '\'' +
                ", singer='" + singer + '\'' +
                ", releaseDate=" + releaseDate +
                ", commentCount=" + commentCount +
                '}';
    }
}
