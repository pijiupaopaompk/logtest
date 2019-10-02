package com.soft.logtest;

import java.util.ArrayList;

public class MessageEvent {
    public ArrayList<String> list=new ArrayList<>();

    public MessageEvent(ArrayList<String> list) {
        this.list = list;
    }

    public ArrayList<String> getList() {
        return list;
    }

    public void setList(ArrayList<String> list) {
        this.list = list;
    }
}
