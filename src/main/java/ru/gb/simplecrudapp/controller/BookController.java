package ru.gb.simplecrudapp.controller;


import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import ru.gb.simplecrudapp.entity.Book;
import ru.gb.simplecrudapp.exception.BookNotFoundException;
import ru.gb.simplecrudapp.service.BookService;


@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @Operation(summary = "Сохранение книги")
    @PostMapping()
    public Book saveBook(@Valid @RequestBody Book book) {
        return bookService.saveBook(book);
    }


    @Operation(summary = "Поиск всех книг")
    @GetMapping()
    public List<Book> fetchBookList() {
        return bookService.fetchBookList();
    }

    @Operation(summary = "Поиск книги по id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Книга найдена",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Book.class))}),
            @ApiResponse(responseCode = "400", description = "Указан неверный id",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Книга не найдена",
                    content = @Content)})
    @GetMapping("/{id}")
    public Book findById(@Parameter(description = "id книги для поиска") @PathVariable long id) {
        return bookService.findById(id)
                .orElseThrow(() -> new BookNotFoundException("КНИГА_НЕ_НАЙДЕНА", "Книга с ID " + id + " не найдена"));
    }

    @Operation(summary = "Поиск книг по автору")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Книга(и) найдена(ы)",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Book.class))}),
            @ApiResponse(responseCode = "400", description = "Автор указан неверно",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Книги не найдены",
                    content = @Content)})
    @GetMapping("/author/{author}")
    public List<Book> findByAuthor(@Parameter(description = "автор книги для поиска") @PathVariable String author) {
        return bookService.findByAuthor(author);
    }

    @Operation(summary = "Обновление книги по id")
    @PutMapping("/{id}")
    public Book updateBook(@RequestBody Book book, @Parameter(description = "id книги для обновления") @PathVariable("id") Long id) {
        return bookService.updateBook(book, id);
    }


    @Operation(summary = "Удаление книги по id")
    @DeleteMapping("/{id}")
    public String deleteBookById(@Parameter(description = "id книги для удаления") @PathVariable("id") Long id) {
        return bookService.deleteBookById(id);
    }
}

