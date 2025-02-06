package com.library.controller;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.library.Exception.LibraryException;
import com.library.enity.Admin;
import com.library.enity.Book;
import com.library.service.AdminService;

@RestController
@RequestMapping("/admin")
@CrossOrigin(origins = "http://localhost:3000")
public class AdminController {


 
	 private static final Logger log = LoggerFactory.getLogger(AdminController.class);

	    @Autowired
	    private AdminService adminService;
	    // Admin login endpoint
	    
	    @PostMapping("/login")
	    public ResponseEntity<?> login(@RequestBody Admin loginRequest) {
	        try {
	            // Call the login method from the service layer
	            Admin admin = adminService.login(loginRequest.getAdminName(), loginRequest.getPassword());
	            return ResponseEntity.status(HttpStatus.OK).body(admin);  // Return admin details (adminName and userType)
	        } catch (LibraryException e) {
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
	        }
	    }
//	 

	    @GetMapping("/getBorrowerDetails/{bookId}")
	    public ResponseEntity<List<Map<String, Object>>> getBorrowerDetails(@PathVariable Long bookId) {
	        List<Map<String, Object>> borrowerDetails = adminService.getBorrowerDetails(bookId);
	        return ResponseEntity.ok(borrowerDetails);
	    }
	    // Add a new book
	    @PostMapping("/addBook")
	    public ResponseEntity<Book> addBook(@RequestBody Book book) {
	        // Save the book through the service
	        Book savedBook = adminService.addBook(book);

	        // Return the saved book (including book_id) as the response
	        return ResponseEntity.status(HttpStatus.CREATED).body(savedBook);
	    }

	    // Delete a book by ID
	    @DeleteMapping("/deleteBook/{bookId}")
	    public ResponseEntity<String> deleteBook(@PathVariable Long bookId) throws LibraryException {
	        adminService.deleteBook(bookId);
	        return ResponseEntity.status(HttpStatus.OK).body("Book deleted successfully");
	    }

	    // Increase book quantity
	    @PutMapping("/increaseBookQuantity/{bookId}")
	    public ResponseEntity<String> increaseBookQuantity(@PathVariable Long bookId) throws LibraryException {
	        adminService.increaseBookQuantity(bookId);
	        return ResponseEntity.status(HttpStatus.OK).body("Book quantity updated successfully");
	    }

	    // Decrease book quantity
	    @CrossOrigin(origins = "http://localhost:3000")
	    @PutMapping("/decreaseBookQuantity/{bookId}")
	    public ResponseEntity<String> decreaseBookQuantity(@PathVariable Long bookId) throws LibraryException {
	    	  log.info("Received request to decrease quantity for book ID: {}", bookId);
	        adminService.decreaseBookQuantity(bookId);
	        return ResponseEntity.status(HttpStatus.OK).body("Book quantity updated successfully");
	    }

	    // Get all books
	    @GetMapping("/getAllBooks")
	    public ResponseEntity<List<Book>> getAllBooks() {
	        List<Book> books = adminService.getAllBooks();
	        log.info("Number of books fetched: {}", books.size()); 
	        return ResponseEntity.status(HttpStatus.OK).body(books);
	    }
	    @PutMapping("/updateBook")
	    public ResponseEntity<Book> updateBook(@RequestBody Book book) throws LibraryException {
	        try {
	            // Call the updateBook service method
	            Book updatedBook = adminService.updateBook(book);
	            return ResponseEntity.status(HttpStatus.OK).body(updatedBook);  // Return updated book details
	        } catch (LibraryException e) {
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);  // Handle exception if the book isn't found
	        }
	}
}