package com.example.knygnesys.utils;

public class User {
    private int id;
    private String name;
    private String surname;
    private String email;
    private String username;
    private String password;
    private int readers_id;
    private String address;
    private int points;
    private int fk_Role;

    public User(int id, String name, String surname, String email, String username, String password,
                int readers_id, String address, int points, int fk_Role) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.username = username;
        this.password = password;
        this.readers_id = readers_id;
        this.address = address;
        this.points = points;
        this.fk_Role = fk_Role;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
public void setName(String name){
        this.name = name;
}
    public String getSurname() {
        return surname;
    }
public void setSurname(String surname){
        this.surname = surname;
}
    public String getEmail() {
        return email;
    }
public void setEmail(String email){
        this.email = email;
}
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public int getReadersId() {
        return readers_id;
    }

    public String getAddress() {
        return address;
    }
public void setAddress(String address){
        this.address = address;
}
    public int getPoints() {
        return points;
    }

    public int getFkRole() {
        return fk_Role;
    }
public void setFkRole(int fk_Role){
        this.fk_Role = fk_Role;
}


}


