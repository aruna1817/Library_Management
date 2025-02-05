package com.library.repository;



import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.library.enity.Book;
import com.library.enity.User;
    

	public interface BookRepository extends JpaRepository<Book, Long> {
	    
	    // Method to search books by name or author (case-insensitive search)
	    List<Book> findByBookNameContainingIgnoreCaseOrAuthorContainingIgnoreCase(String bookName, String author);

	    // Method to search books by author (case-insensitive)
	    List<Book> findByAuthorIgnoreCase(String author);

	    // Method to search books by edition (case-insensitive)
	    List<Book> findByEditionIgnoreCase(String edition);

	    // Method to find books borrowed by a user based on their username
	    List<Book> findByBorrowedBooks_User_name(String username);  // Note: Make sure you have a relationship between Book and BorrowedBook

	}


