package com.library.enity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@JsonIgnoreProperties({"borrowedBooks"}) 
@Table(name = "users")
public class User { 

	   @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    @Column(name = "user_id") // Explicitly specifying the column name
	    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String password;

    @Column(name = "email", nullable = false, unique = true)
    private String email;


    private String dob; // Date of Birth

    @Column(columnDefinition = "varchar(255) default 'User'")
    private String userType;

    // One user can borrow multiple books
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonBackReference
    private List<BorrowedBook> borrowedBooks = new ArrayList<>();

    // Constructors
    public User() {}

    public User(String name, String password, String emailId, String dob, String userType) {
        this.name = name;
        this.password = password;
        this.email = emailId;
        this.dob = dob;
        this.userType = userType;
    }

    // Getters and Setters
   

    public String getName() {
        return name;
    }

  

	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    
    public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public List<BorrowedBook> getBorrowedBooks() {
        return borrowedBooks;
    }

    public void setBorrowedBooks(List<BorrowedBook> borrowedBooks) {
        this.borrowedBooks = borrowedBooks;
    }

    public void addBorrowedBook(BorrowedBook borrowedBook) {
        this.borrowedBooks.add(borrowedBook);
    }

    public void removeBorrowedBook(BorrowedBook borrowedBook) {
        this.borrowedBooks.remove(borrowedBook);
    }
    public List<Map<String, Object>> getCheckedOutBooks() {
        // Create a list to hold the result
        List<Map<String, Object>> checkedOutBooksDetails = new ArrayList<>();

        // Iterate through borrowed books
        for (BorrowedBook borrowedBook : borrowedBooks) {
            if (!borrowedBook.isReturned()) {
                // Create a map to hold the details
                Map<String, Object> bookDetails = new HashMap<>();
                
                // Add the borrowedBook details
                
                bookDetails.put("borrowedDate", borrowedBook.getBorrowedDate());
                bookDetails.put("dueDate", borrowedBook.getDueDate());
                bookDetails.put("returned", borrowedBook.isReturned());
                
                // Add the book details (book name, author)
                Book book = borrowedBook.getBook(); // Assuming you have a Book object in BorrowedBook
                bookDetails.put("book_id",book.getId());
                bookDetails.put("book_edition",book.getEdition());
                bookDetails.put("bookName", book.getBookName());
                bookDetails.put("author", book.getAuthor());

                // Add the map to the result list
                checkedOutBooksDetails.add(bookDetails);
            }
        }

        // Return the final list
        return checkedOutBooksDetails;
    }

}

