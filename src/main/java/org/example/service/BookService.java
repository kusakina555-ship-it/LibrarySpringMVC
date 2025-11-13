package org.example.service;

import org.example.dao.BookDao;
import org.example.exception.BookNotFoundException;
import org.example.exception.DuplicateBookException;
import org.example.model.Book;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    private static final Logger log = LoggerFactory.getLogger(BookService.class);

    private final BookDao bookDao;

    @Autowired
    public BookService(BookDao bookDao) {
        this.bookDao = bookDao;
    }

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

    @Transactional
    public Book addBook(Book book) {
        try {
            // Проверка на дубликат перед сохранением
            Optional<Book> existingBook = bookDao.findByTitleAndAuthor(book.getTitle(), book.getAuthor());
            if (existingBook.isPresent()) {
                throw new DuplicateBookException(
                        "Книга с названием '" + book.getTitle() +
                                "' и автором '" + book.getAuthor() + "' уже существует"
                );
            }

            Book savedBook = bookDao.save(book);
            log.info("Добавлена новая книга в БД: {}", savedBook);
            return savedBook;
        } catch (DataIntegrityViolationException e) {
            // Резервная проверка на случай, если уникальность нарушена на уровне БД
            throw new DuplicateBookException("Книга с таким названием и автором уже существует", e);
        }
    }

    @Transactional
    public void deleteBook(int id) {
        Book book = getBookById(id); // метод для проверки существования книг
        bookDao.delete(id);
        log.info("Удалена книга из БД id: {}", id);
    }

    @Transactional
    public Book updateBook(int id, Book updatedBook) {
        Book existingBook = getBookById(id);
        // Проверяем дубликат только если изменились title или author
        if (!existingBook.getTitle().equalsIgnoreCase(updatedBook.getTitle()) ||
                !existingBook.getAuthor().equalsIgnoreCase(updatedBook.getAuthor())) {
            Optional<Book> duplicate = bookDao.findByTitleAndAuthor(
                    updatedBook.getTitle(), updatedBook.getAuthor());
            if (duplicate.isPresent() && !duplicate.get().getId().equals(id)) {
                throw new DuplicateBookException(
                        "Книга с названием '" + updatedBook.getTitle() +
                                "' и автором '" + updatedBook.getAuthor() + "' уже существует"
                );
            }
        }

        existingBook.setTitle(updatedBook.getTitle());
        existingBook.setAuthor(updatedBook.getAuthor());
        existingBook.setAvailable(updatedBook.getAvailable());

        Book updated = bookDao.update(existingBook);
        log.info("Обновлена книга в БД id: {}", id);
        return updated;
    }
}