package com.library.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.library.Exception.LibraryException;
import com.library.enity.Book;
import com.library.enity.BorrowedBook;
import com.library.enity.User;
import com.library.repository.BookRepository;
import com.library.repository.BorrowedBookRepository;
import com.library.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BorrowedBookRepository borrowedBookRepository;

    @Override
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @Override
    public Book getBookDetailsById(Long bookId) throws LibraryException {
        return bookRepository.findById(bookId)
                .orElseThrow(() -> new LibraryException("Book not found with ID: " + bookId));
    }

    @Override
    public List<Book> searchBooks(String bookName, String author) {
        return bookRepository.findByBookNameContainingIgnoreCaseOrAuthorContainingIgnoreCase(bookName, author);
    }

    @Override
    public List<Book> getBookByAuthor(String author) {
        return bookRepository.findByAuthorIgnoreCase(author);
    }

    @Override
    public List<Book> getBookByEdition(String edition) {
        return bookRepository.findByEditionIgnoreCase(edition);
    }

    @Override
    @Transactional
    public void borrowBook(Long userId, Long bookId) throws LibraryException {
        // Fetch the user by ID
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new LibraryException("User not found with ID: " + userId));

        // Fetch the book by ID
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new LibraryException("Book not found with ID: " + bookId));

        // Check if the book is available in stock
        if (book.getQuantity() <= 0) {
            throw new LibraryException("The book is out of stock.");
        }

        // Check if the user has already borrowed 3 books
        List<BorrowedBook> borrowedBooks = borrowedBookRepository.findByUserId(userId);
        if (borrowedBooks.size() >= 3) {
            throw new LibraryException("User has already borrowed 3 books.");
        }

        // Reduce the book's quantity by 1 as it is being borrowed
        book.setQuantity(book.getQuantity() - 1);
        bookRepository.save(book); // Save the updated book details

        // Record the borrowed book with borrowing and return dates
        BorrowedBook borrowedBook = new BorrowedBook(user, book, LocalDate.now(), LocalDate.now().plusWeeks(2));
        borrowedBookRepository.save(borrowedBook); // Save the borrowed book record
    }
    @Override
    @Transactional
    public void returnBook(Long userId, Long bookId) throws LibraryException {
        // Fetch the user by ID
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new LibraryException("User not found with ID: " + userId));

        // Fetch the book by ID
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new LibraryException("Book not found with ID: " + bookId));

        // Check if the user has borrowed this book
        BorrowedBook borrowedBook = borrowedBookRepository.findByIdAndBookId(userId, bookId)
                .orElseThrow(() -> new LibraryException("This book was not borrowed by the user."));

        // Delete the borrowed book record as it is being returned
        borrowedBookRepository.delete(borrowedBook);

        // Increase the book's quantity by 1 as it is being returned
        book.setQuantity(book.getQuantity() + 1);
        bookRepository.save(book); // Save the updated book details
    }

}
