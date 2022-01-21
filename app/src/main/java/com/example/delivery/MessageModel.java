package com.example.delivery;

public class MessageModel {

    String sender_id;
    String admin_id;
    String message;
    String message_time;
    String message_date;






    public MessageModel( String sender_id, String admin_id,String message, String message_time ) {
        this.message = message;
        this.sender_id = sender_id;
        this.message_time = message_time;
        this.admin_id = admin_id;
    }

    public String getAdmin_id() {
        return admin_id;
    }

    public void setAdmin_id(String admin_id) {
        this.admin_id = admin_id;
    }

    public MessageModel() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSender_id() {
        return sender_id;
    }

    public void setSender_id(String sender_id) {
        this.sender_id = sender_id;
    }

    public String getMessage_time() {
        return message_time;
    }

    public void setMessage_time(String message_time) {
        this.message_time = message_time;
    }


}
