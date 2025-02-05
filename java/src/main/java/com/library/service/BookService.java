package com.library.service;

import java.util.List;

import com.library.Exception.LibraryException;
import com.library.enity.Book;

 public interface BookService {
	     
	     // Fetch all books in the library
	     List<Book> getAllBooks();
	     
	     // Get book details by book ID
	     Book getBookDetailsById(Long bookId) throws LibraryException;
	     
	     // Search books by name or author
	     List<Book> searchBooks(String bookName, String author);
	     
	     // Get books by author
	     List<Book> getBookByAuthor(String author);
	     
	     // Get books by edition
	     List<Book> getBookByEdition(String edition);
	     
	     // Borrow a book (user and book IDs passed as arguments)
	     void borrowBook(Long userId, Long bookId) throws LibraryException;
	     
	     // Return a borrowed book (user and book IDs passed as arguments)
	     void returnBook(Long userId, Long bookId) throws LibraryException;
	 }



