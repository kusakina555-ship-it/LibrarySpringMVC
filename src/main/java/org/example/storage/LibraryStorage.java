package org.example.storage;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.example.model.Book;
import org.springframework.stereotype.Component;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@Component
public class LibraryStorage {
    private static final String FILE_NAME = "library_data.json";
    private static final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .excludeFieldsWithoutExposeAnnotation()
            .create();

    public static void saveBooks(List<Book> books) {
        try (FileWriter writer = new FileWriter(FILE_NAME)) {
            gson.toJson(books, writer);
            System.out.println("✓ Данные успешно сохранены. Книг: " + books.size());
            // Отладочная информация
            for (Book book : books) {
                System.out.println("  Сохранена книга: " + book);
            }
        } catch (IOException e) {
            System.out.println("✗ Ошибка при сохранении данных: " + e.getMessage());
        }
    }

    public static List<Book> loadBooks() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            System.out.println("Файл данных не найден. Начните с пустой библиотеки.");
            return new ArrayList<>();
        }

        try (FileReader reader = new FileReader(FILE_NAME)) {
            Type bookListType = new TypeToken<ArrayList<Book>>(){}.getType();
            List<Book> books = gson.fromJson(reader, bookListType);

            if (books != null) {
                System.out.println("✓ Данные успешно загружены. Загружено книг: " + books.size());
                return books;
            } else {
                System.out.println("Файл пуст или поврежден");
                return new ArrayList<>();
            }
        } catch (FileNotFoundException e) {
            System.out.println("Файл не найден, создается новая библиотека");
            return new ArrayList<>();
        } catch (IOException e) {
            System.out.println("Ошибка при чтении файла: " + e.getMessage());
            return new ArrayList<>();
        } catch (Exception e) {
            System.out.println("Ошибка при загрузке данных: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}