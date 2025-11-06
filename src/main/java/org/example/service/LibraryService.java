package org.example.service;

import org.example.exception.BookNotFoundException;
import org.example.model.Book;
import org.example.storage.LibraryStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import jakarta.annotation.PostConstruct;

@Service
public class LibraryService {

    private static final Logger log = LoggerFactory.getLogger(LibraryService.class);

    private List<Book> books;

    @PostConstruct
    public void init() {
        // Загружаем книги из файла
        books = LibraryStorage.loadBooks();

        // ОБЯЗАТЕЛЬНО обновляем счетчик ДО добавления тестовых книг
        Book.updateIdCounter(books);
        log.info("Загружено книг: {}", books.size());

        // Добавляем тестовые данные ТОЛЬКО если библиотека пуста
        if (books.isEmpty()) {
            log.info("Библиотека пуста, добавляем тестовые книги...");
            addBook("Война и мир", "Лев Толстой");
            addBook("Преступление и наказание", "Федор Достоевский");
            addBook("Мастер и Маргарита", "Михаил Булгаков");
            log.info("Добавлены тестовые книги. Всего книг: {}", books.size());
        } else {
            log.info("Библиотека загружена из файла. Всего книг: {}", books.size());
            // Логируем загруженные книги для отладки
            for (Book book : books) {
                log.info("Загружена книга: {}", book);
            }
        }
    }

    // Новый метод для добавления книг по названию и автору
    public Book addBook(String title, String author) {
        Book book = new Book(title, author);
        books.add(book);
        LibraryStorage.saveBooks(books);
        log.info("Добавлена новая книга: {}", book);
        return book;
    }

    public Book addBook(Book book) {
        // Если книга не имеет ID (новая книга), создаем ее заново
        if (book.getId() == 0) {
            if (book.getTitle() == null || book.getAuthor() == null) {
                throw new IllegalArgumentException("Невозможно добавить книгу с пустыми названием или автором");
            }
            return addBook(book.getTitle(), book.getAuthor());
        } else {
            // Если книга уже имеет ID (например, из JSON), просто добавляем ее
            books.add(book);
            LibraryStorage.saveBooks(books);
            log.info("Добавлена существующая книга: {}", book);
            return book;
        }
    }

    public List<Book> getAllBooks() {
        return new ArrayList<>(books);
    }

    public List<Book> getAvailableBooks() {
        return books.stream()
                .filter(Book::getAvailable)
                .collect(java.util.stream.Collectors.toList());
    }

    public Book getBookById(int id) {
        return books.stream()
                .filter(book -> book.getId() == id)
                .findFirst()
                .orElseThrow(() -> new BookNotFoundException("Книга с id " + id + " не найдена"));
    }

    public void deleteBook(int id) {
        Book bookToRemove = getBookById(id);
        books.remove(bookToRemove);
        LibraryStorage.saveBooks(books);
        log.info("Удалена книга id: {}", id);
    }

    public Book updateBook(int id, Book updatedBook) {
        Book existingBook = getBookById(id);
        existingBook.setTitle(updatedBook.getTitle());
        existingBook.setAuthor(updatedBook.getAuthor());
        existingBook.setAvailable(updatedBook.getAvailable());
        LibraryStorage.saveBooks(books);
        log.info("Обновлена книга id: {}", id);
        return existingBook;
    }

    public Book getByTitleAndAuthor(String title, String author) {
        return books.stream()
                .filter(book -> book.getTitle().equalsIgnoreCase(title) &&
                        book.getAuthor().equalsIgnoreCase(author))
                .findFirst()
                .orElseThrow(() -> new BookNotFoundException(
                        "Книга '" + title + "' автора '" + author + "' не найдена"
                ));
    }
}
