package ru.gb.simplecrudapp.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.gb.simplecrudapp.entity.Book;
import ru.gb.simplecrudapp.exception.BookNotFoundException;
import ru.gb.simplecrudapp.repository.BookRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;


class BookServiceImplTest {
    @Mock
    private BookRepository bookRepository;
    private BookService bookService;
    AutoCloseable autoCloseable;
    Book book;


    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        bookService = new BookServiceImpl(bookRepository);
        book = Book.builder()
                .title("Преступление и наказание")
                .author("Федор Достоевский")
                .genre("роман")
                .build();
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void testSaveBook() {
        mock(Book.class);
        mock(BookRepository.class);
        when(bookRepository.save(book)).thenReturn(book);

        assertThat(bookService.saveBook(book)).isEqualTo(book);
        verify(bookRepository, times(1)).save(book);
    }

    @Test
    void testFetchBookList() {
        mock(Book.class);
        mock(BookRepository.class);

        when(bookRepository.findAll()).thenReturn(new ArrayList<>(Collections.singleton(book)));

        assertThat(bookService.fetchBookList().get(0)).isEqualTo(book);
        verify(bookRepository, times(1)).findAll();
    }

    @Test
    void testFindById() {
        mock(Book.class);
        mock(BookRepository.class);

        when(bookRepository.findById(1L)).thenReturn(Optional.ofNullable(book));

        assertThat(bookService.findById(1L).get()).isEqualTo(book);
        verify(bookRepository, times(1)).findById(1L);
    }

    @Test
    void testFindByAuthor_Found() {
        String author = "А.Пушкин";
        mock(Book.class);
        mock(BookRepository.class);

        when(bookRepository.findByAuthorEqualsIgnoreCase(author)).thenReturn(new ArrayList<>(Collections.singleton(book)));

        assertThat(bookService.findByAuthor(author).get(0)).isEqualTo(book);
        verify(bookRepository, times(1)).findByAuthorEqualsIgnoreCase(author);
    }

    @Test
    void testFindByAuthor_NotFound() {
        String author = "А.Пушкин";
        mock(Book.class);
        mock(BookRepository.class);

        when(bookRepository.findByAuthorEqualsIgnoreCase(author)).thenReturn(new ArrayList<>());

        assertThatThrownBy(() -> bookService.findByAuthor(author)).isInstanceOf(BookNotFoundException.class)
                .hasMessage("Автор " + author + " не найден");
    }

    @Test
    void testUpdateBook_Found() {
        mock(Book.class);
        mock(BookRepository.class);
        when(bookRepository.save(book)).thenReturn(book);
        when(bookRepository.findById(1L)).thenReturn(Optional.ofNullable(book));
        book.setId(1L);

        assertThat(bookService.updateBook(book, 1L)).isEqualTo(book);
        verify(bookRepository, times(1)).save(book);
    }

    @Test
    void testUpdateBook_NotFound() {
        mock(Book.class);
        mock(BookRepository.class);
        when(bookRepository.save(book)).thenReturn(book);

        assertThatThrownBy(() -> bookService.updateBook(book, 1L)).isInstanceOf(BookNotFoundException.class)
                .hasMessage("Книга с ID 1 не найдена");
    }

    @Test
    void testDeleteBookById_Found() {
        mock(Book.class);
        mock(BookRepository.class, CALLS_REAL_METHODS);
        when(bookRepository.findById(1L)).thenReturn(Optional.ofNullable(book));
        book.setId(1L);

        doAnswer(Answers.CALLS_REAL_METHODS).when(bookRepository).deleteById(1L);

        assertThat(bookService.deleteBookById(1L)).isEqualTo("Success");
        verify(bookRepository, times(1)).deleteById(1L);

    }

    @Test
    void testDeleteBookById_Not_Found() {
        mock(Book.class);
        mock(BookRepository.class, CALLS_REAL_METHODS);

        doAnswer(Answers.CALLS_REAL_METHODS).when(bookRepository).deleteById(1L);

        assertThatThrownBy(() -> bookService.deleteBookById(1L)).isInstanceOf(BookNotFoundException.class)
                .hasMessage("Книга с ID 1 не найдена");
    }
}