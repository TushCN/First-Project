package com.example.cryptx.networking;

/**
 * Created by CryptX on 01-10-2017.
 */

public class Courses {

    int Id;
    String email;
    String name;
    String body;
    public Courses(int id, String name, String email,String body) {
        this.Id = id;
        this.email = email;
        this.name = name;
        this.body = body;
    }
}
