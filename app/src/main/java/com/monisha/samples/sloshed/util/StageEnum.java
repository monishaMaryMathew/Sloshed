package com.monisha.samples.sloshed.util;

/**
 * Created by Monisha on 3/8/2018.
 */

public enum StageEnum {
    START_MY_NIGHT("Start my night", 0),
    MEAL_DETAILS("Last meal details", 1),
    METER_WITH_DRINK("Select a drink", 2),
    METER("Drink meter", 3);

    private String description;
    private int index;

    StageEnum(String s, int i) {
        description = s;
        index = i;
    }

    public StageEnum getPrevious() {
        StageEnum prev = null;
        switch (index) {
            case 0:
                prev = START_MY_NIGHT;
                break;
            case 1:
                prev = START_MY_NIGHT;
                break;
            case 2:
                prev = MEAL_DETAILS;
                break;
            case 3:
                prev = METER;
                break;
        }
        return prev;
    }

    public StageEnum getNext() {
        StageEnum next = null;
        switch (index) {
            case 0:
                next = MEAL_DETAILS;
                break;
            case 1:
                next = METER_WITH_DRINK;
                break;
            case 2:
                next = METER;
                break;
            case 3:
                next = METER;
                break;
        }
        return next;
    }

    @Override
    public String toString() {
        return description;
    }

    public int toInt() {
        return index;
    }
}
