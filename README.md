# Student PDF Parser

## Описание
Этот проект на Java предназначен для парсинга PDF-файлов с данными студентов, фильтрации по коду специальности и записи информации в базу данных MySQL.

## Функционал
- Чтение и обработка PDF-документа с помощью библиотеки Apache PDFBox.
- Извлечение информации о студентах по заданному коду специальности.
- Сортировка студентов по коду ВУЗа.
- Запись извлечённых данных в MySQL базу данных.

## Используемые технологии
- Java
- Apache PDFBox 3.0.4
- MySQL Connector 9.2.0
- IntelliJ IDEA
- MySQL WorkBench

## Установка и настройка

### 1. Клонирование репозитория
```sh
git clone https://github.com/yourusername/your-repo.git
cd your-repo
```

### 2. Настройка базы данных
Создайте базу данных и таблицу, используя следующий SQL-код:
```sql
DROP DATABASE IF EXISTS student_db;
CREATE DATABASE student_db;
USE student_db;

CREATE TABLE students (
    id INT AUTO_INCREMENT PRIMARY KEY,
    number VARCHAR(10),
    ict_code VARCHAR(20),
    name TEXT,
    score INT,
    university_code VARCHAR(10),
    spec_code VARCHAR(10)
);
```

### 3. Настройка проекта
- Убедитесь, что у вас установлен JDK 17+.
- Добавьте библиотеки `pdfbox-3.0.4.jar` и `mysql-connector-java-9.2.0.jar` в зависимости проекта.

### 4. Конфигурация базы данных
Обновите параметры подключения в `Main.java`:
```java
private static final String DB_URL = "jdbc:mysql://localhost:3306/student_db";
private static final String DB_USER = "root";
private static final String DB_PASSWORD = "yourpassword";
```

## Запуск проекта
1. Скомпилируйте и запустите программу в IntelliJ IDEA.
2. Введите код специальности, чтобы получить список студентов.
3. Данные автоматически сохранятся в базе данных.

## Авторы
- **Ваше имя** (yourusername)

## Лицензия
Этот проект распространяется под лицензией MIT.

