package com.library.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.library.Exception.LibraryException;
import com.library.enity.Book;
import com.library.enity.BorrowedBook;
import com.library.enity.User;
import com.library.service.UserService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {

    @Autowired
    private UserService userService;
    
	 private static final Logger log = LoggerFactory.getLogger(AdminController.class);


    // Register a new user
	   @PostMapping("/register")
	    public ResponseEntity<String> register(@RequestBody User user) {
	        // Automatically set userType to "user"
	        userService.register(user);
	        return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully.");
	    }
	 
	 
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User loginRequest) {
        try {
            User user = userService.login(loginRequest.getName(), loginRequest.getPassword());
            return ResponseEntity.status(HttpStatus.OK).body(user);  // Send user object with userType
        } catch (LibraryException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    @GetMapping("/borrowedBooks/{userId}")
    public ResponseEntity<?> getUserBorrowedBooks(@PathVariable Long userId) throws LibraryException {
        log.info("Fetching borrowed books for userId: {}", userId);
        User user = (User) userService.getUserBorrowedBooks(userId);
		return ResponseEntity.ok(user.getCheckedOutBooks());
    }

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }
    @GetMapping("/details/{userId}")
    public ResponseEntity<?> getUserDetails(@PathVariable Long userId) {
        try {
            User user = userService.getUserDetails(userId);
            return ResponseEntity.ok(user);
        } catch (LibraryException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    @GetMapping("/getUserBooksByName")
    public ResponseEntity<List<Book>> getUserBooksByName1(@RequestParam String name) {
    	System.out.println("Received user name: " + name);
        try {
            // Fetch the books associated with the user by name
            List<Book> books = userService.getUserBooksByName(name);
            return ResponseEntity.status(HttpStatus.OK).body(books); // Return books with 200 status
        } catch (LibraryException e) {
            // Return 400 Bad Request if user is not found or error occurs
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null); 
        }
    }
    // View all books
    @GetMapping("/viewAllBooks")
    public ResponseEntity<?> viewAllBooks() {
        return ResponseEntity.status(HttpStatus.OK).body(userService.viewAllBooks());
    }

    // View book details by ID
    @GetMapping("/viewBook/{bookId}")
    public ResponseEntity<?> viewBookById(@PathVariable Long bookId) throws LibraryException {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(userService.viewBookById(bookId));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // Search books by keyword
    @GetMapping("/searchBooks")
    public ResponseEntity<?> searchBooks(@RequestParam String keyword) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.searchBooks(keyword));
    }

    // View books by author
    @GetMapping("/viewBooksByAuthor")
    public ResponseEntity<?> viewBooksByAuthor(@RequestParam String author) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.viewBookByAuthor(author));
    }

    // View books by edition
    @GetMapping("/viewBooksByEdition")
    public ResponseEntity<?> viewBooksByEdition(@RequestParam String edition) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.viewBookByEdition(edition));
    }

    // Borrow a book
    @PostMapping("/checkoutBook/{userId}/{bookId}")
    public ResponseEntity<?> checkoutBook( @PathVariable Long userId,@PathVariable Long bookId) {
        log.info("Request to checkout book with ID: {} for user with ID: {}", bookId, userId);
        
        try {
            // Call the service method to process the checkout
            userService.checkoutBook(userId, bookId);
            
            // Return success response if checkout was successful
            log.info("Book with ID: {} successfully checked out by user with ID: {}", bookId, userId);
            return ResponseEntity.status(HttpStatus.OK).body("Book checked out successfully.");
        } catch (LibraryException e) {
            // Catch any LibraryException thrown by service layer
            log.error("Error during checkout process: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        } catch (Exception e) {
            // Catch any unexpected errors that might occur
            log.error("Unexpected error during checkout: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred. Please try again later.");
        }
    }



    // Return a book
	    @PostMapping("/returnBook")
	    public ResponseEntity<?> returnBook(@RequestParam Long userId, @RequestParam Long bookId) {
	        try {
	            userService.returnBook(userId, bookId);
	            return ResponseEntity.status(HttpStatus.OK).body("Book returned successfully.");
	        } catch (LibraryException e) {
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
	        }
	    }
}
