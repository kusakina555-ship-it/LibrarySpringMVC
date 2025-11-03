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
        books = LibraryStorage.loadBooks();
        Book.updateIdCounter(books);

        // Добавляем тестовые данные, если библиотека пуста
        if (books.isEmpty()) {
            addBook(new Book("Война и мир", "Лев Толстой"));
            addBook(new Book("Преступление и наказание", "Федор Достоевский"));
            addBook(new Book("Мастер и Маргарита", "Михаил Булгаков"));
            log.info("Добавлены тестовые книги. Всего книг: {}", books.size());
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

    public Book addBook(Book book) {
        // Если книга без ID, генерируем новый
        if (book.getId() == 0) {
            book = new Book(book.getTitle(), book.getAuthor());
        }
        books.add(book);
        LibraryStorage.saveBooks(books);
        log.info("Добавлена новая книга id: {}", book.getId());
        return book;
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
