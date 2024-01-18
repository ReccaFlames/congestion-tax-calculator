package com.reccaflames.calculator.service;

public class Other implements Vehicle {

    private final String type;

    public Other(String type) {
        this.type = type;
    }

    @Override
    public String getVehicleType() {
        return type;
    }
}
