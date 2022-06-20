package com.suicuntong.sct.entity;

import java.util.Objects;

public class User {
    private String name;
    private String password;
    private String phone;
    private String date;
    private String question;
    private String answer;

    public User(){

    }

    public User(String name, String phone, String password, String date,String question,String answer){
        this.name = name;
        this.password = password;
        this.phone = phone;
        this.date = date;
        this.question = question;
        this.answer = answer;
    }

    public User(String phone, String password){
        this.password=password;
        this.phone=phone;
    }

    public User(String phone){
        this.phone=phone;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", phone='" + phone + '\'' +
                ", date='" + date + '\'' +
                ", question='" + question + '\'' +
                ", answer='" + answer + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(name, user.name) &&
                Objects.equals(password, user.password) &&
                Objects.equals(phone, user.phone) &&
                Objects.equals(date, user.date) &&
                Objects.equals(question, user.question) &&
                Objects.equals(answer, user.answer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, password, phone, date, question, answer);
    }
}
