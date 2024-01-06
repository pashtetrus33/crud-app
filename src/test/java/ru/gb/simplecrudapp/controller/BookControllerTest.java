package ru.gb.simplecrudapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.gb.simplecrudapp.entity.Book;
import ru.gb.simplecrudapp.service.BookService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(BookController.class)
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private BookService bookService;
    Book bookOne;
    Book bookTwo;
    List<Book> bookList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        bookOne = Book.builder()
                .title("Преступление и наказание")
                .author("Федор Достоевский")
                .genre("роман")
                .build();

        bookTwo = Book.builder()
                .title("Отцы и дети")
                .author("Иван Тургенев")
                .genre("роман")
                .build();

        bookList.add(bookOne);
        bookList.add(bookTwo);
    }

    @AfterEach
    void tearDown() {
        bookList.clear();
    }

    @Test
    void testSaveBook() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(bookOne);

        when(bookService.saveBook(bookOne)).thenReturn(bookOne);
        this.mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print()).andExpect(status().isOk());
    }

    @Test
    void testFetchBookList() throws Exception {
        when(bookService.fetchBookList()).thenReturn(bookList);
        this.mockMvc.perform(get("/api/books"))
                .andDo(print()).andExpect(status().isOk());
        verify(bookService, times(1)).fetchBookList();
    }

    @Test
    void testFindById() throws Exception {
        when(bookService.findById(1L)).thenReturn(Optional.ofNullable(bookOne));
        this.mockMvc.perform(get("/api/books/" + "1")).andDo(print()).andExpect(status().isOk());
        verify(bookService, times(1)).findById(1L);
    }

    @Test
    void testFindByAuthor() throws Exception {
        String author = "А.Пушкин";
        when(bookService.findByAuthor(author)).thenReturn(new ArrayList<>(Collections.singleton(bookOne)));
        this.mockMvc.perform(get("/api/books/author/" + author)).andDo(print()).andExpect(status().isOk());
        verify(bookService, times(1)).findByAuthor(author);
    }

    @Test
    void testUpdateBook() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(bookOne);
        when(bookService.updateBook(bookOne, 1L))
                .thenReturn(bookOne);
        this.mockMvc.perform(put("/api/books/" + "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print()).andExpect(status().isOk());
    }

    @Test
    void testDeleteBookById() throws Exception {
        when(bookService.deleteBookById(1L))
                .thenReturn("Book Deleted Successfully");
        this.mockMvc.perform(delete("/api/books/" + "1"))
                .andDo(print()).andExpect(status().isOk());
        verify(bookService, times(1)).deleteBookById(1L);
    }
}