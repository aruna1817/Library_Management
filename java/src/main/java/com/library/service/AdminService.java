package com.library.service;

import java.util.List;
import java.util.Map;

import com.library.Exception.LibraryException;
import com.library.enity.Admin;
import com.library.enity.Book;

public interface AdminService {
	 Book addBook(Book book);
	 //Integer quantity);
	 void deleteBook(Long book_id) throws LibraryException;
	 Book updateBook(Book book)throws LibraryException;
	 List<Book>getAllBooks();
	  void increaseBookQuantity(Long bookId) throws LibraryException;
	  void decreaseBookQuantity(Long bookId) throws LibraryException;
	   Admin login(String name, String password) throws LibraryException;
	 //void logout(String adminName) throws LibraryException;
	List<Map<String, Object>> getBorrowerDetails(Long bookId);
		
 }
