package com.example.nearuseller;


public class DataClass {

    private String dataPrice;
    private String dataImage;
    private String dataLocation;

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    private String dataType;

    public String getDataTime() {
        return dataTime;
    }

    public void setDataTime(String dataTime) {
        this.dataTime = dataTime;
    }

    private String dataTime;
    private String dataNumber;

    public String getDataWhatsappNumber() {
        return dataWhatsappNumber;
    }

    public void setDataWhatsappNumber(String dataWhatsappNumber) {
        this.dataWhatsappNumber = dataWhatsappNumber;
    }

    private String dataWhatsappNumber;
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

    public DataClass( String dataImage,String dataPrice , String dataLocation ,String dataNumber,String dataWhatsappNumber,String dataDescription,String dataCategory,String dataTime,String dataType) {
        this.dataPrice = dataPrice;
        this.dataImage = dataImage;
        this.dataLocation = dataLocation;
        this.dataNumber = dataNumber;
        this.dataWhatsappNumber = dataWhatsappNumber;
        this.dataDescription = dataDescription;
        this.dataCategory = dataCategory;
        this.dataTime = dataTime;
        this.dataType = dataType;
    }
    public DataClass(){

    }
}