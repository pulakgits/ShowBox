package com.basetechz.showbox.G_models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class child_model {
   private  String movieImage,movieId,movieTxt,movieUrl;

   public child_model(){

   }

    public child_model(String movieImage, String movieId, String movieTxt,String movieUrl) {
        this.movieImage = movieImage;
        this.movieId = movieId;
        this.movieTxt=movieTxt;
        this.movieUrl=movieUrl;
    }



    public String getMovieImage() {
        return movieImage;
    }

    public void setMovieImage(String movieImage) {
        this.movieImage = movieImage;
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public String getMovieTxt() {
        return movieTxt;
    }

    public void setMovieTxt(String movieTxt) {
        this.movieTxt = movieTxt;
    }

    public String getMovieUrl() {
        return movieUrl;
    }

    public void setMovieUrl(String movieUrl) {
        this.movieUrl = movieUrl;
    }


}
