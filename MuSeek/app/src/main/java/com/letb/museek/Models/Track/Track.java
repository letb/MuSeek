package com.letb.museek.Models.Track;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;


public class Track implements Serializable {

    @SerializedName("url")
    private String url;

    @SerializedName("success")
    private String success;

    @SerializedName("data")
    private Data data;

    @SerializedName("pic")
    private String pic;

    private List<String> imagesSizesAsc;

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public void setImagesSizesAsc(List<String> imagesSizesAsc) {
        this.imagesSizesAsc = imagesSizesAsc;
        this.pic = imagesSizesAsc.get(2);
    }

    public Track() {
        data = new Data();
    }
//
//    track_id (string) — идентификатор трека
//    position (int) — позиция в топе
//      artist (string) — исполнитель
//                                                 track (string) — название
//                                                                  length (int) — продолжительность воспроизведения в секундах
//    bitrate (string) — толщина потока в человекочитаемом формате
//                                                         size (int) — размер файла в байтах

    public Track(String id, String artist, String track, int length, String bitrate) {
        this.data = new Data(id, artist, track, length, bitrate);
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return data.getArtist() + " – " + data.getTrack();
    }

    public void setTitle(String artist, String track) {
        data.setArtist(artist);
        data.setTrack(track);
    }
}
