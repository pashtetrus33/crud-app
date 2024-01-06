package ru.gb.simplecrudapp.service;

import org.springframework.stereotype.Service;
import ru.gb.simplecrudapp.entity.Book;
import ru.gb.simplecrudapp.exception.BookNotFoundException;
import ru.gb.simplecrudapp.repository.BookRepository;

import java.util.List;
import java.util.Optional;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public Book saveBook(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public List<Book> fetchBookList() {
        return bookRepository.findAll();
    }

    @Override
    public Optional<Book> findById(long id) {
        return bookRepository.findById(id);
    }

    @Override
    public List<Book> findByAuthor(String author) {
        List<Book> result = bookRepository.findByAuthorEqualsIgnoreCase(author);
        if (result.size() > 0){
            return result;
        } else {
            throw new BookNotFoundException("КНИГА_НЕ_НАЙДЕНА", "Автор " + author + " не найден");
        }
    }

    @Override
    public Book updateBook(Book book, Long id) {

        Optional<Book> optionalBook = bookRepository.findById(id);

        if (optionalBook.isPresent()) {
            Book bookDB = optionalBook.get();
            bookDB.setTitle(book.getTitle());
            bookDB.setAuthor(book.getAuthor());
            bookDB.setGenre(book.getGenre());

            return bookRepository.save(bookDB);
        } else {
            throw new BookNotFoundException("КНИГА_НЕ_НАЙДЕНА", "Книга с ID " + id + " не найдена");
        }
    }

    @Override
    public String deleteBookById(Long id) {
        if (bookRepository.findById(id).isPresent()) {
            bookRepository.deleteById(id);
            return "Success";
        } else {
            throw new BookNotFoundException("КНИГА_НЕ_НАЙДЕНА", "Книга с ID " + id + " не найдена");
        }
    }
}