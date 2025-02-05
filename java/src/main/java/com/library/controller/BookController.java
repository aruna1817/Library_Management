package com.library.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.library.Exception.LibraryException;
import com.library.enity.Book;
import com.library.service.BookService;

@RestController
@RequestMapping("/books")
@CrossOrigin(origins = "http://localhost:3000")
public class BookController {

    @Autowired
    private BookService bookService;

    // Get all books
    @GetMapping("/getAll")
    public ResponseEntity<?> getAllBooks() {
        List<Book> books = bookService.getAllBooks();
        if (books.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No books available.");
        }
        return ResponseEntity.status(HttpStatus.OK).body(books);
    }

    // Get book details by ID
    @GetMapping("/admin/getBookDetails/{bookId}")
    public ResponseEntity<?> getBookDetails(@PathVariable Long bookId) {
        try {
            Book book = bookService.getBookDetailsById(bookId);
            return ResponseEntity.status(HttpStatus.OK).body(book);
        } catch (LibraryException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // Search books by keyword (book name or author)
    @GetMapping("/search")
    public ResponseEntity<?> searchBooks(@RequestParam String keyword) throws LibraryException {
        List<Book> books = bookService.searchBooks(keyword, keyword);
		if (books.isEmpty()) {
		    return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No books found for the given keyword.");
		}
		return ResponseEntity.status(HttpStatus.OK).body(books);
    }

    // Get books by author
    @GetMapping("/byAuthor")
    public ResponseEntity<?> getBooksByAuthor(@RequestParam String author) {
        List<Book> books = bookService.getBookByAuthor(author);
        if (books.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No books found for the author: " + author);
        }
        return ResponseEntity.status(HttpStatus.OK).body(books);
    }

    // Get books by edition
    @GetMapping("/byEdition")
    public ResponseEntity<?> getBooksByEdition(@RequestParam String edition) {
        List<Book> books = bookService.getBookByEdition(edition);
        if (books.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No books found for the edition: " + edition);
        }
        return ResponseEntity.status(HttpStatus.OK).body(books);
    }

    // Borrow a book
    @PostMapping("/{userId}/borrow/{bookId}")
    public ResponseEntity<?> borrowBook(@PathVariable Long userId, @PathVariable Long bookId) {
        try {
            bookService.borrowBook(userId, bookId);
            return ResponseEntity.status(HttpStatus.OK).body("Book borrowed successfully.");
        } catch (LibraryException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // Return a book
    @PostMapping("/{userId}/return/{bookId}")
    public ResponseEntity<?> returnBook(@PathVariable Long userId, @PathVariable Long bookId) {
        try {
            bookService.returnBook(userId, bookId);
            return ResponseEntity.status(HttpStatus.OK).body("Book returned successfully.");
        } catch (LibraryException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
