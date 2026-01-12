-- Добавляем тестовые книги, если их еще нет
INSERT INTO books (title, author, is_available, created_at, updated_at)
SELECT 'Война и мир', 'Лев Толстой', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
    WHERE NOT EXISTS (SELECT 1 FROM books WHERE title = 'Война и мир' AND author = 'Лев Толстой');

INSERT INTO books (title, author, is_available, created_at, updated_at)
SELECT 'Преступление и наказание', 'Федор Достоевский', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
    WHERE NOT EXISTS (SELECT 1 FROM books WHERE title = 'Преступление и наказание' AND author = 'Федор Достоевский');

INSERT INTO books (title, author, is_available, created_at, updated_at)
SELECT 'Мастер и Маргарита', 'Михаил Булгаков', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
    WHERE NOT EXISTS (SELECT 1 FROM books WHERE title = 'Мастер и Маргарита' AND author = 'Михаил Булгаков');

INSERT INTO books (title, author, is_available, created_at, updated_at)
SELECT 'Анна Каренина', 'Лев Толстой', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
    WHERE NOT EXISTS (SELECT 1 FROM books WHERE title = 'Анна Каренина' AND author = 'Лев Толстой');

INSERT INTO books (title, author, is_available, created_at, updated_at)
SELECT 'Идиот', 'Федор Достоевский', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
    WHERE NOT EXISTS (SELECT 1 FROM books WHERE title = 'Идиот' AND author = 'Федор Достоевский');