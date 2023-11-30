package com.example.nearuseller;


public class SellerReadData {

    private String dataShopImage;
    private String dataName;

    public String getDataShopImage() {
        return dataShopImage;
    }

    public void setDataShopImage(String dataShopImage) {
        this.dataShopImage = dataShopImage;
    }

    public String getDataName() {
        return dataName;
    }

    public void setDataName(String dataName) {
        this.dataName = dataName;
    }

    public String getDataNumber() {
        return dataNumber;
    }

    public void setDataNumber(String dataNumber) {
        this.dataNumber = dataNumber;
    }

    public String getDataWhatsapp() {
        return dataWhatsapp;
    }

    public void setDataWhatsapp(String dataWhatsapp) {
        this.dataWhatsapp = dataWhatsapp;
    }

    public String getDataEmail() {
        return dataEmail;
    }

    public void setDataEmail(String dataEmail) {
        this.dataEmail = dataEmail;
    }

    public String getDataPassword() {
        return dataPassword;
    }

    public void setDataPassword(String dataPassword) {
        this.dataPassword = dataPassword;
    }

    public String getDataAddress() {
        return dataAddress;
    }

    public void setDataAddress(String dataAddress) {
        this.dataAddress = dataAddress;
    }

    public String getDataCity() {
        return dataCity;
    }

    public void setDataCity(String dataCity) {
        this.dataCity = dataCity;
    }

    private String dataNumber;
    private String dataWhatsapp;
    private String dataEmail;
    private String dataPassword;
    private String dataAddress;
    private String dataCity;




    public SellerReadData( String dataShopImage ,String dataName , String dataNumber , String dataWhatsapp, String dataEmail, String dataPassword, String dataAddres, String dataCity) {
        this.dataShopImage = dataShopImage;
        this.dataName = dataName;
        this.dataNumber = dataNumber;
        this.dataWhatsapp = dataWhatsapp;
        this.dataEmail = dataEmail;
        this.dataPassword = dataPassword;
        this.dataAddress = dataAddres;
        this.dataCity = dataCity;
    }
    public SellerReadData(){

    }
}