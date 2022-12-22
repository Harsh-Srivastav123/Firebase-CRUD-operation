package com.example.firebase3;

public class Model {

    String Name,Phone_no,Github,Description,status;

    public Model(String name, String phone_no, String github, String description, String status) {
        this.Name = name;
        this.Phone_no = phone_no;
        this.Github = github;
        this.Description = description;
        this.status=status;
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
