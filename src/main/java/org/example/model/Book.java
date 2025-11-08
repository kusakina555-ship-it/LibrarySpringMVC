package org.example.model;

import com.google.gson.annotations.Expose;
import lombok.Data;

@Data
public class Book {
    private static int idCounter = 0;
    @Expose
    private int id;
    @Expose
    private String title;

    public String getAuthor() {
        return author;
    }
    public void setAuthor(String author) {
        this.author = author;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    private void setId(int id) {
        this.id = id;
        // Обновляем счетчик при загрузке существующих книг
        if (id > idCounter) {
            idCounter = id;
        }
    }
    public int getId() {
        return id;
    }
    @Expose
    private String author;
    @Expose
    private Boolean isAvailable = false;

public Book(){
}

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
            int maxId = books.stream()
                    .mapToInt(Book::getId)
                    .max()
                    .orElse(0);
            idCounter = maxId;
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

    // Геттер для Thymeleaf
    public Boolean getAvailable() {
        return isAvailable;
    }

    // Сеттер для изменения статуса доступности
    public void setAvailable(Boolean available) {
        isAvailable = available;
    }
}