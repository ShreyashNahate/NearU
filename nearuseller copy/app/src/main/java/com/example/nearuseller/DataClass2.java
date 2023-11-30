package com.example.nearuseller;


public class DataClass2 {

    private String dataTitle;
    private String dataImage;
    private String key;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }



    public String getDataTitle() {
        return dataTitle;
    }

    public String getDataImage() {
        return dataImage;
    }

    public DataClass2(String dataTitle,   String dataImage) {
        this.dataTitle = dataTitle;
        this.dataImage = dataImage;
    }
    public DataClass2(){

    }
}