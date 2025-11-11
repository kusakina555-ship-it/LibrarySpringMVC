package org.example.service;

import org.example.dao.BookDao;
import org.example.exception.BookNotFoundException;
import org.example.model.Book;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class LibraryService {

    private static final Logger log = LoggerFactory.getLogger(LibraryService.class);

    private final BookDao bookDao;

    @Autowired
    public LibraryService(BookDao bookDao) {
        this.bookDao = bookDao;
    }

    // Убираем всю логику инициализации
    @Transactional(readOnly = true)
    public List<Book> getAllBooks() {
        return bookDao.findAll();
    }

    @Transactional(readOnly = true)
    public List<Book> getAvailableBooks() {
        return bookDao.findAvailableBooks();
    }

    @Transactional(readOnly = true)
    public Book getBookById(int id) {
        return bookDao.findById(id)
                .orElseThrow(() -> new BookNotFoundException("Книга с id " + id + " не найдена в БД"));
    }

    public Book addBook(String title, String author) {
        Book book = new Book(title, author);
        Book savedBook = bookDao.save(book);
        log.info("Добавлена новая книга в БД: {}", savedBook);
        return savedBook;
    }

    public Book addBook(Book book) {
        if (book.getTitle() == null || book.getAuthor() == null) {
            throw new IllegalArgumentException("Невозможно добавить книгу с пустыми названием или автором");
        }

        if (book.getId() != null) {
            book.setId(null);
        }

        Book savedBook = bookDao.save(book);
        log.info("Добавлена новая книга в БД: {}", savedBook);
        return savedBook;
    }

    public void deleteBook(int id) {
        // Проверяем существование книги
        Book bookToDelete = getBookById(id);
        bookDao.delete(id);
        log.info("Удалена книга из БД id: {}", id);
    }

    public Book updateBook(int id, Book updatedBook) {
        Book existingBook = getBookById(id);
        existingBook.setTitle(updatedBook.getTitle());
        existingBook.setAuthor(updatedBook.getAuthor());
        existingBook.setAvailable(updatedBook.getAvailable());

        Book updated = bookDao.update(existingBook);
        log.info("Обновлена книга в БД id: {}", id);
        return updated;
    }

    @Transactional(readOnly = true)
    public Book getByTitleAndAuthor(String title, String author) {
        return bookDao.findByTitleAndAuthor(title, author)
                .orElseThrow(() -> new BookNotFoundException(
                        "Книга '" + title + "' автора '" + author + "' не найдена в БД"
                ));
    }
}