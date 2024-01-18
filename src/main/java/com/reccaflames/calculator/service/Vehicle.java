package com.reccaflames.calculator.service;

import java.util.*;
import java.text.*;

public interface Vehicle {

    static Vehicle of(String type) {
        return new Other(type);
    }
    String getVehicleType();
}
