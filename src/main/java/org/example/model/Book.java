package org.example.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "books",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_books_title_author",
                columnNames = {"title", "author"}
        ))
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
        if (author == null || author.trim().isEmpty()) {
            throw new IllegalArgumentException("Автор не может быть пустым");
        }
        this.author = author.trim();
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Название книги не может быть пустым");
        }
        if (title.trim().length() > 500) {
            throw new IllegalArgumentException("Название книги не может превышать 500 символов");
        }
        this.title = title.trim();
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
        setTitle(title);
        setAuthor(author); // сеттеры для валидации
        this.isAvailable = true;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    @PrePersist
    protected void onCreate() {
        // Дополнительная валидация при сохранении
        validateFields();
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (updatedAt == null) {
            updatedAt = LocalDateTime.now();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        // Дополнительная валидация при обновлении
        validateFields();
        updatedAt = LocalDateTime.now();
    }

    // Валидация полей (аналог CHECK constraints из БД)
    private void validateFields() {
        // Проверка title (аналог chk_books_title_not_empty)
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Название книги не может быть пустым");
        }
        if (title.length() > 500) {
            throw new IllegalArgumentException("Название книги не может превышать 500 символов");
        }

        // Проверка author (аналог chk_books_author_not_empty)
        if (author == null || author.trim().isEmpty()) {
            throw new IllegalArgumentException("Автор не может быть пустым");
        }
        if (author.length() > 255) {
            throw new IllegalArgumentException("Имя автора не может превышать 255 символов");
        }

        // Проверка isAvailable (не может быть null)
        if (isAvailable == null) {
            isAvailable = true;
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
    // equals и hashCode для правильной работы unique constraint

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Book)) return false;
        Book book = (Book) o;
        if (!getTitle().equalsIgnoreCase(book.getTitle())) return false;
        return getAuthor().equalsIgnoreCase(book.getAuthor());
    }

    @Override
    public int hashCode() {
        int result = getTitle().toLowerCase().hashCode();
        result = 31 * result + getAuthor().toLowerCase().hashCode();
        return result;
    }
}
