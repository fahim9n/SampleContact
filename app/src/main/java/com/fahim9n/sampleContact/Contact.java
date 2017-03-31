package com.fahim9n.sampleContact;

/**
 * Created by fahim9n on 3/31/2017.
 */
public class Contact {
    int id;
    String name;
    String phoneNumber;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Contact(){

    }

    public Contact(int i, String string, String string1) {
        setId(i);

        setName(string);
        setPhoneNumber(string1);
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
