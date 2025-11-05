package org.example.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//@Data
//@NoArgsConstructor
public class Book {
    private static int idCounter = 0;

    // Геттеры и сеттеры
    @Getter
    @Setter
    private int id;
    @Setter
    @Getter
    private String title;
    @Setter
    @Getter
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

    public Boolean getAvailable() { return isAvailable; }
    public void setAvailable(Boolean available) { isAvailable = available; }
}