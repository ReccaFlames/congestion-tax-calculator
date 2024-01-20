package com.reccaflames.calculator.service;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

import java.time.LocalTime;

@AllArgsConstructor
@EqualsAndHashCode
public class TimeRange implements Comparable<TimeRange> {
    private final LocalTime start;
    private final LocalTime end;

    public boolean contains(LocalTime time) {
        return !time.isBefore(start) && !time.isAfter(end);
    }

    @Override
    public int compareTo(TimeRange other) {
        return this.start.compareTo(other.start);
    }
}
