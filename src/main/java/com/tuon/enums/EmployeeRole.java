package com.tuon.enums;

public enum EmployeeRole {

    TRAINER("Trainer"),
    RECEPTIONIST("Receptionist"),
    MANAGER("Manager");

    private String label;

    EmployeeRole(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
