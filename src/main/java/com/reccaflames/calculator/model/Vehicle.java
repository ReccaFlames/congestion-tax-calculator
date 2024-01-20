package com.reccaflames.calculator.model;

public interface Vehicle {

    static Vehicle of(String type) {
        return new Other(type);
    }

    String getVehicleType();
}
