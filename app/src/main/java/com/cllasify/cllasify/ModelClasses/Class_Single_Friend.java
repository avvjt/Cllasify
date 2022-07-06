package com.cllasify.cllasify.ModelClasses;

public class Class_Single_Friend {

    String messageIdSender, messageIdReceiver, message, senderId, msgCategory, pdfUrl;
    long timestamp;


    public Class_Single_Friend(String messageIdSender, String messageIdReceiver, String message, String senderId, long timestamp, String msgCategory, String pdfUrl) {
        this.messageIdSender = messageIdSender;
        this.messageIdReceiver = messageIdReceiver;
        this.message = message;
        this.senderId = senderId;
        this.timestamp = timestamp;
        this.msgCategory = msgCategory;
        this.pdfUrl = pdfUrl;
    }

    public Class_Single_Friend(String message, String senderId, long timestamp) {
        this.message = message;
        this.senderId = senderId;
        this.timestamp = timestamp;
    }

    public Class_Single_Friend() {
    }

    public String getPdfUrl() {
        return pdfUrl;
    }

    public void setPdfUrl(String pdfUrl) {
        this.pdfUrl = pdfUrl;
    }

    public String getMsgCategory() {
        return msgCategory;
    }

    public void setMsgCategory(String msgCategory) {
        this.msgCategory = msgCategory;
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
