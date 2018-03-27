package com.monisha.samples.sloshed.models;

import android.support.annotation.NonNull;

/**
 * Created by Monisha on 3/27/2018.
 */

public class Contact implements Comparable<Contact>{
    String phoneNumber;
    String name;

    public Contact(String phoneNumber, String name) {
        this.phoneNumber = phoneNumber;
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        return this.name.equals(((Contact) obj).getName()) && this.phoneNumber.equals(((Contact) obj).getPhoneNumber());
    }

    @Override
    public int compareTo(@NonNull Contact o) {
        return this.getName().compareTo(o.getName());
    }
}
