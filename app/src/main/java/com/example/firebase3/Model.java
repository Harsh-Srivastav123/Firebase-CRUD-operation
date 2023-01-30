package com.example.firebase3;

import android.net.Uri;

public class Model {

    String Name,Phone_no,Github,Description,status,imgUrl;

    public Model(String name, String phone_no, String github, String description, String status,String imgUrl)  {
        this.Name = name;
        this.Phone_no = phone_no;
        this.Github = github;
        this.Description = description;
        this.status=status;
        this.imgUrl=imgUrl;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Model(){

    }
    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPhone_no() {
        return Phone_no;
    }

    public void setPhone_no(String phone_no) {
        Phone_no = phone_no;
    }

    public String getGithub() {
        return Github;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setGithub(String github) {
        Github = github;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }
}
