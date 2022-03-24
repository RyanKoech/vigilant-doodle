package com.example.vigilantdoodle.datamodels;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class PoliceReports extends RecursiveTreeObject<PoliceReports> {
    private final SimpleStringProperty obId;
    private final SimpleStringProperty reporterName;
    private final SimpleStringProperty offenderName;
    private final SimpleStringProperty location;
    private final SimpleStringProperty date;
    private final SimpleStringProperty time;
    private final SimpleStringProperty crime;

    public  PoliceReports(String obId, String reporterName, String offenderName, String location, String date, String time, String crime){
        this.obId = new SimpleStringProperty(obId);
        this.reporterName = new SimpleStringProperty(reporterName);
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

    public String getReporterName() {
        return reporterName.get();
    }

    public StringProperty reporterNameProperty() {
        return reporterName;
    }

    public void setReporterName(String reporterName) {
        this.reporterName.set(reporterName);
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
