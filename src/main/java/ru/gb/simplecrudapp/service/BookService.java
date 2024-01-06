package ru.gb.simplecrudapp.service;

import ru.gb.simplecrudapp.entity.Book;

import java.util.List;
import java.util.Optional;

public interface BookService {

    Book saveBook(Book book);

    List<Book> fetchBookList();

    Book updateBook(Book book, Long bookId);

    String deleteBookById(Long bookId);

    Optional<Book> findById(long id);

    List<Book> findByAuthor(String author);
}

