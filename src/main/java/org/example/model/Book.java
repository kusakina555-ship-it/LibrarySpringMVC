package org.example.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

//@Data
//@NoArgsConstructor
public class Book {
    private static int idCounter = 0;

    private int id;
    private String title;
    private String author;

    @JsonProperty("isAvailable")
    private Boolean isAvailable = false;

    public Book(String title, String author) {
        this.id = ++idCounter;
        this.title = title;
        this.author = author;
        this.isAvailable = true;
    }

    public static void updateIdCounter(java.util.List<Book> books) {
        if (books.isEmpty()) {
            idCounter = 0;
        } else {
            idCounter = books.stream()
                    .mapToInt(Book::getId)
                    .max()
                    .orElse(0);
        }
    }

    public String availabilityMessage() {
        return isAvailable ? "да" : "нет";
    }

    @Override
    public String toString() {
        return "id=" + id +
                ", Название книги: " + title + ", Автор: " + author +
                ", Доступна: " + availabilityMessage();
    }
    // Геттеры и сеттеры
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public Boolean getAvailable() { return isAvailable; }
    public void setAvailable(Boolean available) { isAvailable = available; }
}