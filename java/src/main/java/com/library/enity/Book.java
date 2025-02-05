package com.library.enity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@JsonIgnoreProperties({"borrowedBooks"})
@Table(name = "books")
public class Book {

	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    @Column(name = "book_id")  // Renamed the column to book_id
	    private Long Id;

    @Column(nullable = false)
    private String bookName;

    @Column(nullable = false)
    private String author;

    private String edition;

    @Column(nullable = false)
    private int quantity;

    // One book can have multiple borrowing records
    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<BorrowedBook> borrowedBooks = new ArrayList<>();

   
    // Constructors
    public Book() {}

    public Book(String bookName, String author, String edition, int quantity) {
        this.bookName = bookName;
        this.author = author;
        this.edition = edition;
        this.quantity = quantity;
    }


	public Long getId() {
		return Id;
	}

	public void setId(Long id) {
		Id = id;
	}

	public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getEdition() {
        return edition;
    }

    public void setEdition(String edition) {
        this.edition = edition;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

 

    public List<BorrowedBook> getBorrowedBooks() {
		return borrowedBooks;
	}

	public void setBorrowedBooks(List<BorrowedBook> borrowedBooks) {
		this.borrowedBooks = borrowedBooks;
	}

	// Utility methods
    public boolean isAvailable() {
        return this.quantity > 0;
    }

    public void borrowBook() {
        if (isAvailable()) {
            this.quantity -= 1;
        } else {
            throw new IllegalStateException("Book is not available");
        }
    }

    public void returnBook() {
        this.quantity += 1;
    }
}
