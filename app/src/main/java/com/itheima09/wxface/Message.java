package com.itheima09.wxface;

/**
 * Created by sanpi on 2016/6/15.
 */

public class Message {
    //对话内容
    public String msg;
    //消息的来源
    public boolean isFromMe;
    //消息的类型
    public MessageType type = MessageType.Text;
    enum MessageType{
        Text,Gif,Movie
    }

    public Message(String msg, boolean isFromMe, MessageType type) {
        this.msg = msg;
        this.isFromMe = isFromMe;
        this.type = type;
    }
}
