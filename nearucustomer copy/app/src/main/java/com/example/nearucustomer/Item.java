package com.example.nearucustomer;


public class Item {

    private String dataPrice;
    private String dataImage;
    private String dataLocation;

    public String getDataType() {
        return dataType;
    }

    private String dataType;
    private String dataNumber;
    private String dataCategory;
    private String dataDescription;


    public String getDataCategory() {return dataCategory;}

    public String getDataDescription() {return dataDescription;}

    public String getDataLocation() {return dataLocation;}

    public String getDataNumber() {return dataNumber;}



    public String getDataImage() {
        return dataImage;
    }

    public String getDataPrice() {
        return dataPrice;
    }

    public Item( String dataImage,String dataPrice , String dataLocation ,String dataNumber,String dataDescription,String dataCategory,String dataType) {
        this.dataPrice = dataPrice;
        this.dataImage = dataImage;
        this.dataLocation = dataLocation;
        this.dataNumber = dataNumber;
        this.dataDescription = dataDescription;
        this.dataCategory = dataCategory;
        this.dataType = dataType;
    }
    public Item(){

    }
}