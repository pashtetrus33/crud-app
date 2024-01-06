package ru.gb.simplecrudapp.repository;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.gb.simplecrudapp.entity.Book;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@DataJpaTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BookRepositoryTests {

    @Autowired
    BookRepository bookRepository;
    Book book;


    @BeforeEach
    void setUp() {
        book = Book.builder()
                .title("Преступление и наказание")
                .author("Федор Достоевский")
                .genre("роман")
                .build();

        bookRepository.save(book);
    }

    @AfterEach
    void tearDown() {

        book = null;
        bookRepository.deleteAll();
    }

    @Test
    @Order(1)
    //@Rollback(value = false)
    void testSaveBook() {
        assertThat(book.getId()).isGreaterThan(0);
    }

    @Test
    @Order(2)
    //@Rollback(value = false)
    void testFindBookById() {
        Book book = bookRepository.findById(2L).get();

        assertThat(book.getId()).isEqualTo(2L);
    }

    @Test
    @Order(3)
        //@Rollback(value = false)
    void testFindAllBooks() {
        List<Book> books = bookRepository.findAll();

        assertThat(books.size()).isGreaterThan(0);
    }

    @Test
    @Order(4)
        //@Rollback(value = false)
    void testUpdateBook_Found() {
        Book book = bookRepository.findById(4L).get();
        book.setAuthor("Федор Михайлович Достоевский");
        Book updatedBook = bookRepository.save(book);
        assertThat(updatedBook.getAuthor()).isEqualTo("Федор Михайлович Достоевский");
    }

    @Test
    @Order(5)
        //@Rollback(value = false)
    void testDeleteBook() {

        bookRepository.deleteById(1L);

        List<Book> list = bookRepository.findByAuthorEqualsIgnoreCase("Федор Михайлович Достоевский");

        assertThat(list.size()).isEqualTo(0);
    }

    @Test
    @Order(6)
        //@Rollback(value = false)
    void testFindBookByAuthor_Found() {
        List<Book> bookList = bookRepository.findByAuthorEqualsIgnoreCase("Федор Достоевский");

        assertThat(bookList.get(bookList.size() - 1).getId()).isEqualTo(book.getId());
        assertThat(bookList.get(bookList.size() - 1).getAuthor()).isEqualTo(book.getAuthor());
        assertThat(bookList.get(bookList.size() - 1).getGenre()).isEqualTo(book.getGenre());
    }

    @Test
    @Order(7)
        //Rollback(value = false)
    void testFindBookByAuthor_NotFound() {
        List<Book> bookList = bookRepository.findByAuthorEqualsIgnoreCase("Иван Тютчев");

        assertThat(bookList.isEmpty()).isTrue();
    }
}
