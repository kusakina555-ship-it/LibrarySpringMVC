package org.example.dao;

import org.example.model.Book;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class BookDao {

    private final SessionFactory sessionFactory;

    @Autowired
    public BookDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    public Book save(Book book) {
        getCurrentSession().persist(book);
        return book;
    }

    public Optional<Book> findById(Integer id) {
        return Optional.ofNullable(getCurrentSession().get(Book.class, id));
    }

    public List<Book> findAll() {
        return getCurrentSession()
                .createQuery("FROM Book ORDER BY id", Book.class)
                .getResultList();
    }

    public List<Book> findAvailableBooks() {
        return getCurrentSession()
                .createQuery("FROM Book WHERE isAvailable = true ORDER BY title", Book.class)
                .getResultList();
    }

    public Book update(Book book) {
        return (Book) getCurrentSession().merge(book);
    }

    public void delete(Integer id) {
        Book book = getCurrentSession().get(Book.class, id);
        if (book != null) {
            getCurrentSession().remove(book);
        }
    }

    public Optional<Book> findByTitleAndAuthor(String title, String author) {
        Query<Book> query = getCurrentSession()
                .createQuery("FROM Book WHERE LOWER(title) = LOWER(:title) AND LOWER(author) = LOWER(:author)", Book.class);
        query.setParameter("title", title);
        query.setParameter("author", author);
        return query.uniqueResultOptional();
    }
}