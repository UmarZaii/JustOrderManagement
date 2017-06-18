package com.justorder.justordermanagement;

public class ProductClassListModel {

    private String className;
    private String classDisplay;
    private String classType;

    public ProductClassListModel() {

    }

    public ProductClassListModel(String className, String classDisplay, String classType) {
        this.className = className;
        this.classDisplay = classDisplay;
        this.classType = classType;
    }

    public String getClassName() {
        return className;
    }

    public String getClassDisplay() {
        return classDisplay;
    }

    public String getClassType() {
        return classType;
    }

}
