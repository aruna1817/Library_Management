package com.library.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.library.Exception.LibraryException;
import com.library.controller.AdminController;
import com.library.enity.Admin;
import com.library.enity.Book;
import com.library.enity.BorrowedBook;
import com.library.repository.AdminRepository;
import com.library.repository.BookRepository;
import com.library.repository.BorrowedBookRepository;


@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private BookRepository bookRepository;

  
    @Autowired
    private BorrowedBookRepository borrowedBookRepository;

	 private static final Logger log = LoggerFactory.getLogger(AdminController.class);
    @Override
    public Admin login(String adminName, String password) throws LibraryException {
        // Retrieve the admin by adminName
        Admin admin = adminRepository.findByAdminName(adminName)
                .orElseThrow(() -> new LibraryException("Invalid admin username or password."));

        // Compare the provided password with the stored password
        if (!admin.getPassword().equals(password)) {
        	log.warn("Failed login attempt for admin: {}", adminName);
            throw new LibraryException("Invalid admin username or password.");
        }
        log.info("Admin {} logged in successfully.", adminName);
        return admin;
    }
   @Override
    public List<Map<String, Object>> getBorrowerDetails(Long bookId) {
        List<BorrowedBook> borrowedBooks = borrowedBookRepository.findByBookId(bookId);

        return borrowedBooks.stream().map(bb -> {
            Map<String, Object> borrowerInfo = new HashMap<>();
            borrowerInfo.put("userId", bb.getUser().getId());
            borrowerInfo.put("userName", bb.getUser().getName());
            borrowerInfo.put("email", bb.getUser().getEmail());
            borrowerInfo.put("returned", bb.isReturned());
            return borrowerInfo;
        }).collect(Collectors.toList());
    }

    @Override
    public Book addBook(Book book)  {
        // Save the book in the database
        return bookRepository.save(book);
    }

    @Override
    public void deleteBook(Long bookId) throws LibraryException {
        if (!bookRepository.existsById(bookId)) {
            throw new LibraryException("Book not found.");
        }
        bookRepository.deleteById(bookId);
    }


    @Override
    public Book updateBook(Book book) throws LibraryException {
        if (!bookRepository.existsById(book.getId())) {
            throw new LibraryException("Book not found.");
        }
        return bookRepository.save(book);
    }

@Override
    public List<Book> getAllBooks() {
        List<Book> books = bookRepository.findAll();
        log.info("Fetched books: {}", books); // Log the fetched books
        return books;
    }


    @Override
    public void increaseBookQuantity(Long bookId) throws LibraryException {
        Optional<Book> optionalBook = bookRepository.findById(bookId);
        if (optionalBook.isPresent()) {
            Book book = optionalBook.get();
            book.setQuantity(book.getQuantity() + 1);
            bookRepository.save(book);
        } else {
            throw new LibraryException("Book not found.");
        }
    }

    @Override
    public void decreaseBookQuantity(Long bookId) throws LibraryException {
        Optional<Book> optionalBook = bookRepository.findById(bookId);
        if (optionalBook.isPresent()) {
            Book book = optionalBook.get();
            if (book.getQuantity() > 0) {
                book.setQuantity(book.getQuantity() - 1);
                bookRepository.save(book);
            } else {
                throw new LibraryException("Book quantity is already zero.");
            }
        } else {
            throw new LibraryException("Book not found.");
        }
    }
}
