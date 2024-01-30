package com.example.knygnesys.utils;

public class Book {
    private int id;
    private String title;
    private String author;
    private int is_available;
    private int fk_Branch;
    private int fk_Order;

    public Book(int id, String title, String author, int is_available,int fk_Branch, int fk_Order) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.is_available = is_available;
        this.fk_Branch = fk_Branch;
        this.fk_Order = fk_Order;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }
    public int getIs_available() {
        return is_available;
    }
    public int getFk_Branch() {
        return fk_Branch;
    }
    public int getFk_Order() {
        return fk_Order;
    }
}
