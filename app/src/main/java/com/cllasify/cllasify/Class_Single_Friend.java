package com.cllasify.cllasify;

public class Class_Single_Friend {

    String messageIdSender,messageIdReceiver,message, senderId;
    long timestamp;

    public Class_Single_Friend(String messageIdSender, String messageIdReceiver, String message, String senderId, long timestamp) {
        this.messageIdSender = messageIdSender;
        this.messageIdReceiver = messageIdReceiver;
        this.message = message;
        this.senderId = senderId;
        this.timestamp = timestamp;
    }

    public Class_Single_Friend(String message, String senderId, long timestamp) {
        this.message = message;
        this.senderId = senderId;
        this.timestamp = timestamp;
    }

    public Class_Single_Friend() {
    }


    public String getMessageIdSender() {
        return messageIdSender;
    }

    public void setMessageIdSender(String messageIdSender) {
        this.messageIdSender = messageIdSender;
    }

    public String getMessageIdReceiver() {
        return messageIdReceiver;
    }

    public void setMessageIdReceiver(String messageIdReceiver) {
        this.messageIdReceiver = messageIdReceiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
