package com.example.nearuseller;

public class userReadData {
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    userReadData(){

    }
    public  String  name , number , email, pass,city;;
    public userReadData(String name, String number , String email, String pass, String city ){
        this.name = name;
        this.number = number;
        this.email = email;
        this.pass = pass;
        this.city=city;
    }
}
