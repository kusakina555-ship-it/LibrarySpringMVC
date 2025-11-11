package org.example.model;

import com.google.gson.annotations.Expose;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "title", nullable = false, length = 500)
    private String title;

    @Column(name = "author", nullable = false, length = 255)
    private String author;

    @Column(name = "is_available")
    private Boolean isAvailable = true;

    // автоматическое время создания/обновления
    @Column(name = "created_at", updatable = false)
    private java.time.LocalDateTime createdAt;

    @Column(name = "updated_at")
    private java.time.LocalDateTime updatedAt;

    //РУЧНЫЕ ГЕТТЕРЫ И СЕТТЕРЫ оставлены специально, потому что могут быть конфликты без них
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
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public Boolean getAvailable() {
        return isAvailable;
    }
    public void setAvailable(Boolean available) {
        isAvailable = available;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Book() {
    }

    public Book(String title, String author) {
        this.title = title;
        this.author = author;
        this.isAvailable = true;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
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
}
