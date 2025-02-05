package com.library.service;

import java.util.List;

import com.library.Exception.LibraryException;
import com.library.enity.Book;
import com.library.enity.BorrowedBook;
import com.library.enity.User;

public interface UserService {
	
	void register(User user);
    User getUserDetails(Long userId) throws LibraryException;
	List<Book>viewAllBooks();
	Book viewBookById(Long bookId) throws LibraryException;
	List<Book>searchBooks(String keyword);
	List<Book> viewBookByAuthor(String authoNamer);
	List<Book>viewBookByEdition(String edition);
	 User login(String name, String password) throws LibraryException;
	 List<User> getAllUsers();
	void returnBook(Long userId,Long bookId)throws LibraryException;
	void checkoutBook(Long userId, Long bookId) throws LibraryException;
	
	List<Book> getUserBooksByName(String name) throws LibraryException;
	User getUserBorrowedBooks(Long userId) throws LibraryException;
	

	
}

