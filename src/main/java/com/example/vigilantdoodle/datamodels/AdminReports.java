package com.example.vigilantdoodle.datamodels;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class AdminReports extends RecursiveTreeObject<AdminReports> {
    private final SimpleStringProperty obId;
    private final SimpleStringProperty policeName;
    private final SimpleStringProperty offenderName;
    private final SimpleStringProperty location;
    private final SimpleStringProperty date;
    private final SimpleStringProperty time;
    private final SimpleStringProperty crime;

    public  AdminReports(String obId, String policeName, String offenderName, String location, String date, String time, String crime){
        this.obId = new SimpleStringProperty(obId);
        this.policeName = new SimpleStringProperty(policeName);
        this.offenderName = new SimpleStringProperty(offenderName);
        this.location = new SimpleStringProperty(location);
        this.date = new SimpleStringProperty(date);
        this.time = new SimpleStringProperty(time);
        this.crime = new SimpleStringProperty(crime);
    }

    public String getObId() {
        return obId.get();
    }

    public StringProperty obIdProperty() {
        return obId;
    }

    public void setObId(String obId) {
        this.obId.set(obId);
    }

    public String getPoliceName() {
        return policeName.get();
    }

    public StringProperty policeNameProperty() {
        return policeName;
    }

    public void setPoliceName(String policeName) {
        this.policeName.set(policeName);
    }

    public String getOffenderName() {
        return offenderName.get();
    }

    public StringProperty offenderNameProperty() {
        return offenderName;
    }

    public void setOffenderName(String offenderName) {
        this.offenderName.set(offenderName);
    }

    public String getLocation() {
        return location.get();
    }

    public StringProperty locationProperty() {
        return location;
    }

    public void setLocation(String location) {
        this.location.set(location);
    }

    public String getDate() {
        return date.get();
    }

    public StringProperty dateProperty() {
        return date;
    }

    public void setDate(String date) {
        this.date.set(date);
    }

    public String getTime() {
        return time.get();
    }

    public StringProperty timeProperty() {
        return time;
    }

    public void setTime(String time) {
        this.time.set(time);
    }

    public String getCrime() {
        return crime.get();
    }

    public StringProperty crimeProperty() {
        return crime;
    }

    public void setCrime(String crime) {
        this.crime.set(crime);
    }
}