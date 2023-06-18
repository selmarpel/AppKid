package com.ftec.kidapp.Activity;

public class HistoricoItem {
    private String buttonName;
    private String dateTime;

    public HistoricoItem(String buttonName, String dateTime) {
        this.buttonName = buttonName;
        this.dateTime = dateTime;
    }

    public String getButtonName() {
        return buttonName;
    }

    public String getDateTime() {
        return dateTime;
    }
}
