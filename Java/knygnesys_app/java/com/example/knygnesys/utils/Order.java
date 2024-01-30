package com.example.knygnesys.utils;

public class Order {
    private int id;
    private int book_amount;
    private String created;
    private String return_date;
    private String get_date;
    private String get_time;
    private String address;
    private int courier_id;
    private int fk_State;
    private int fk_User;
    private int fk_Branch;

    public Order(int id, int book_amount, String created, String return_date, String get_date, String get_time, String address, int courier_id, int fk_State, int fk_User, int fk_Branch) {
        this.id = id;
        this.book_amount = book_amount;
        this.created = created;
        this.return_date = return_date;
        this.get_date = get_date;
        this.get_time = get_time;
        this.address = address;
        this.courier_id = courier_id;
        this.fk_State = fk_State;
        this.fk_User = fk_User;
        this.fk_Branch = fk_Branch;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBook_amount() {
        return book_amount;
    }

    public void setBook_amount(int book_amount) {
        this.book_amount = book_amount;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getReturn_date() {
        return return_date;
    }

    public void setReturn_date(String return_date) {
        this.return_date = return_date;
    }

    public String getGet_date() {
        return get_date;
    }

    public void setGet_date(String get_date) {
        this.get_date = get_date;
    }

    public String getGet_time() {
        return get_time;
    }

    public void setGet_time(String ge_time) {
        this.get_time = ge_time;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getCourier_id() {
        return courier_id;
    }

    public void setCourier_id(int courier_id) {
        this.courier_id = courier_id;
    }

    public int getFk_State() {
        return fk_State;
    }

    public void setFk_State(int fk_State) {
        this.fk_State = fk_State;
    }

    public int getFk_User() {
        return fk_User;
    }

    public void setFk_User(int fk_User) {
        this.fk_User = fk_User;
    }

    public int getFk_Branch() {
        return fk_Branch;
    }

    public void setFk_Branch(int fk_Branch) {
        this.fk_Branch = fk_Branch;
    }
}
