package com.example.hocapp.models;

public class ChatModel {

    private String sender;
    private String receiver;
    private String message;

    public ChatModel(String sender, String receiver, String message){
        this.sender= sender;
        this.receiver=receiver;
        this.message=message;


    }

    public ChatModel(){

    }

    public String getSender(){ return sender; }
    public  void  setSender(String sender){ this.sender=sender;}


    public String getReceiver(){ return receiver; }
    public  void  setReceiver(String receiver){ this.receiver=receiver;}


    public String getMessage(){ return message; }
    public  void  setMessage(String message){ this.message=message;}

}
