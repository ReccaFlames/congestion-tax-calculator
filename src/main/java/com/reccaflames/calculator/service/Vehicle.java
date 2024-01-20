package com.reccaflames.calculator.service;

public interface Vehicle {

    static Vehicle of(String type) {
        return new Other(type);
    }

    String getVehicleType();
}
