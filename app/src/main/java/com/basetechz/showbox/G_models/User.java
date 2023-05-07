package com.basetechz.showbox.G_models;

public class User {
    String name,Bio,Dob,gender,email,password,image;

    public User(){

    }

    public  User(String names , String bios ,String Dob, String images){
        this.name = names;
        this.Bio = bios;
        this.Dob = Dob;
        this.image = images;
    }
    public User(String name, String bio,String dob,String gender,String email, String password, String image){
      this.name = name;
      this.Bio=bio;
      this.Dob = dob;
      this.gender = gender;
      this.email = email;
      this.password = password;
      this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBio() {
        return Bio;
    }

    public void setBio(String bio) {
        Bio = bio;
    }


    public String getDob() {
        return Dob;
    }

    public void setDob(String dob) {
        Dob = dob;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
