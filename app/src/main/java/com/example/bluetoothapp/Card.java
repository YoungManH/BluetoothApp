package com.example.bluetoothapp;

public class Card {
    public  String mDeviceName;
    public String mTemp;
    public String mHumi;
    public String mMac;

    public Card(String deviceName, String tmp, String hum, String mac) {
        this.mDeviceName = deviceName;
        this.mTemp = tmp;
        this.mHumi = hum;
        this.mMac = mac;
    }

    public void setmDeviceName(String deviceName) {
        mDeviceName = deviceName;
    }

    public void setmTemp(String temp) {
        mTemp = temp;
    }

    public void setmHumi(String humi) {
        mHumi = humi;
    }
}
