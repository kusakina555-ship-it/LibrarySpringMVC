package org.example.controllers;
import org.example.model.Book;
import org.example.service.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    @GetMapping("/")
    public String getAllBooks(Model model) {
            List<Book> books = libraryService.getAllBooks();
            model.addAttribute("books", books);
            model.addAttribute("title", "Все книги");
            return "books/books"; // вернет books.html
        //    model.addAttribute("error", "Ошибка при загрузке книг: " + e.getMessage());

        }
    }

//    // Страница с доступными книгами
//    @GetMapping("/available")
//    public String getAvailableBooks(Model model) {
//        try {
//            List<Book> books = libraryService.getAvailableBooks();
//            model.addAttribute("books", books);
//            model.addAttribute("title", "Доступные книги");
//            return "books";
//        } catch (Exception e) {
//            model.addAttribute("error", "Ошибка при загрузке доступных книг: " + e.getMessage());
//            return "error";
//        }
//    }
//
//    // Страница конкретной книги
//    @GetMapping("/{id}")
//    public String getBookById(@PathVariable int id, Model model) {
//        try {
//            Book book = libraryService.getBookById(id);
//            model.addAttribute("book", book);
//            return "book-details";
//        } catch (Exception e) {
//            model.addAttribute("error", "Книга не найдена: " + e.getMessage());
//            return "error";
//        }
//    }
//
//    /* Форма добавления новой книги
//    @GetMapping("/add")
//    public String showAddBookForm(Model model) {
//        model.addAttribute("book", new Book());
//        return "add-book";
//    }
//*/
//    // Обработка добавления книги
//    @PostMapping
//    public String addBook(@ModelAttribute Book book, Model model) {
//        try {
//            Book savedBook = libraryService.addBook(book);
//            model.addAttribute("message", "Книга успешно добавлена!");
//            return "redirect:/books/" + savedBook.getId();
//        } catch (Exception e) {
//            model.addAttribute("error", "Ошибка при добавлении книги: " + e.getMessage());
//            model.addAttribute("book", book);
//            return "add-book";
//        }
//    }
//
//    // Форма редактирования книги
//    @GetMapping("/{id}/edit")
//    public String showEditBookForm(@PathVariable int id, Model model) {
//        try {
//            Book book = libraryService.getBookById(id);
//            model.addAttribute("book", book);
//            return "edit-book";
//        } catch (Exception e) {
//            model.addAttribute("error", "Книга не найдена: " + e.getMessage());
//            return "error";
//        }
//    }
//
//    // Обработка обновления книги
//    @PostMapping("/{id}")
//    public String updateBook(@PathVariable int id, @ModelAttribute Book book, Model model) {
//        try {
//            Book updatedBook = libraryService.updateBook(id, book);
//            model.addAttribute("message", "Книга успешно обновлена!");
//            return "redirect:/books/" + updatedBook.getId();
//        } catch (Exception e) {
//            model.addAttribute("error", "Ошибка при обновлении книги: " + e.getMessage());
//            model.addAttribute("book", book);
//            return "edit-book";
//        }
//    }
//
//    // Удаление книги
//    @PostMapping("/{id}/delete")
//    public String deleteBook(@PathVariable int id, Model model) {
//        try {
//            libraryService.deleteBook(id);
//            model.addAttribute("message", "Книга успешно удалена!");
//            return "redirect:/books";
//        } catch (Exception e) {
//            model.addAttribute("error", "Ошибка при удалении книги: " + e.getMessage());
//            return "error";
//        }
//    }

