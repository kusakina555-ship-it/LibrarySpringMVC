package org.example.controllers;
import org.example.model.Book;
import org.example.service.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
@RequestMapping("/books")
public class BookController {
    private final LibraryService libraryService;

    @Autowired
    public BookController(LibraryService libraryService) {
        this.libraryService = libraryService;
    }

    // Главная страница со списком всех книг
    @GetMapping({"", "/"})
    public String getAllBooks(Model model) {
        try {
            List<Book> books = libraryService.getAllBooks();
            model.addAttribute("books", books);
            model.addAttribute("title", "Все книги");
            return "books/books"; // вернет books.html
        } catch (Exception e) {
            model.addAttribute("error", "Ошибка при загрузке книг: " + e.getMessage());
            return "books/error";
        }
    }

    // Страница с доступными книгами
    @GetMapping("/available")
    public String getAvailableBooks(Model model) {
        try {
            List<Book> books = libraryService.getAvailableBooks();
            model.addAttribute("books", books);
            model.addAttribute("title", "Доступные книги");
            return "books/books";
        } catch (Exception e) {
            model.addAttribute("error", "Ошибка при загрузке доступных книг: " + e.getMessage());
            return "books/error";
        }
    }

    // Страница конкретной книги
    @GetMapping("/{id}")
    public String getBookById(@PathVariable("id") int bookId, Model model) {
        try {
            Book book = libraryService.getBookById(bookId);
            model.addAttribute("book", book);
            return "books/book-details";
        } catch (Exception e) {
            model.addAttribute("error", "Книга не найдена: " + e.getMessage());
            return "books/error";
        }
    }

    //Форма добавления новой книги
    @GetMapping("/add")
    public String showAddBookForm(Model model) {
        model.addAttribute("book", new Book());
        return "books/add-book";
    }

    // Обработка добавления книги
    @PostMapping
    public String addBook(@ModelAttribute Book book, Model model) {
        try {
            Book savedBook = libraryService.addBook(book);
            model.addAttribute("message", "Книга успешно добавлена!");
            return "redirect:/books/" + savedBook.getId();
        } catch (Exception e) {
            model.addAttribute("error", "Ошибка при добавлении книги: " + e.getMessage());
            model.addAttribute("book", book);
            return "books/add-book";
        }
    }

    // Форма редактирования книги
    @GetMapping("/{id}/edit")
    public String showEditBookForm(@PathVariable("id") int bookId, Model model) {
        try {
            Book book = libraryService.getBookById(bookId);
            model.addAttribute("book", book);
            return "books/edit-book";
        } catch (Exception e) {
            model.addAttribute("error", "Книга не найдена: " + e.getMessage());
            return "books/error";
        }
    }

    // Обработка обновления книги
    @PostMapping("/{id}")
    public String updateBook(@PathVariable("id") int bookId, @ModelAttribute Book book, Model model) {
        try {
            Book updatedBook = libraryService.updateBook(bookId, book);
            model.addAttribute("message", "Книга успешно обновлена!");
            return "redirect:/books/" + updatedBook.getId();
        } catch (Exception e) {
            model.addAttribute("error", "Ошибка при обновлении книги: " + e.getMessage());
            model.addAttribute("book", book);
            return "books/edit-book";
        }
    }

    // Удаление книги
    @PostMapping("/{id}/delete")
    public String deleteBook(@PathVariable("id") int id, Model model) {
        try {
            libraryService.deleteBook(id);
            model.addAttribute("message", "Книга успешно удалена!");
            return "redirect:/books";
        } catch (Exception e) {
            model.addAttribute("error", "Ошибка при удалении книги: " + e.getMessage());
            return "books/error";
        }
    }
}
