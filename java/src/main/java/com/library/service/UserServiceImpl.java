package com.library.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

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
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BorrowedBookRepository borrowedBookRepository;

    @Override
    public void register(User user) {
        if (user.getUserType() == null || user.getUserType().isEmpty()) {
            user.setUserType("user");
        }
        userRepository.save(user);
    }
    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    
@Override
public User getUserBorrowedBooks(Long userId) throws LibraryException {
    User user = userRepository.findById(userId).orElseThrow(() -> new LibraryException("User not found"));
    return user; // borrowedBooks will be included due to the relationship
}


    @Override
    public User getUserDetails(Long userId) throws LibraryException {
        return userRepository.findById(userId)
                .orElseThrow(() -> new LibraryException("User not found with ID: " + userId));
    }

    @Override
    public User login(String name, String password) throws LibraryException {
        User user = userRepository.findByName(name)
                .orElseThrow(() -> new LibraryException("Invalid username or password."));

        if (!user.getPassword().equals(password)) {
            throw new LibraryException("Invalid username or password.");
        }

        return user;
    }

    @Override
    public List<Book> viewAllBooks() {
        return bookRepository.findAll();
    }

    @Override
    public Book viewBookById(Long bookId) throws LibraryException {
        return bookRepository.findById(bookId)
                .orElseThrow(() -> new LibraryException("Book not found with ID: " + bookId));
    }

    @Override
    public List<Book> searchBooks(String keyword) {
        return bookRepository.findByBookNameContainingIgnoreCaseOrAuthorContainingIgnoreCase(keyword, keyword);
    }

    @Override
    public List<Book> viewBookByAuthor(String author) {
        return bookRepository.findByAuthorIgnoreCase(author);
    }

    @Override
    public List<Book> viewBookByEdition(String edition) {
        return bookRepository.findByEditionIgnoreCase(edition);
    }

    /**
     * Allows a user to checkout a book if they have not exceeded the borrow limit.
     */
    @Override
    @Transactional
    public void checkoutBook(Long userId, Long bookId) throws LibraryException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new LibraryException("User not found."));

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new LibraryException("Book not found."));

        // Ensure the book is available for checkout
        if (book.getQuantity() <= 0) {
            throw new LibraryException("This book is currently out of stock.");
        }

        // Fetch the books currently borrowed by the user
        List<BorrowedBook> borrowedBooks = borrowedBookRepository.findByUserId(userId);
        
        for (BorrowedBook borrowedBook : borrowedBooks) {
            if (borrowedBook.getBook().getId().equals(bookId) && !borrowedBook.isReturned()) {
                throw new LibraryException("This book has already been checked out and not returned yet.");
            }
        }

        // Prevent a user from checking out more than 3 books
        if (borrowedBooks.size() >= 3) {
            throw new LibraryException("User can only check out up to 3 books at a time.");
        }

        // Reduce the book's available quantity by 1 (as it is being borrowed)
        book.setQuantity(book.getQuantity() - 1);
        bookRepository.save(book);

        // Create a new BorrowedBook record for tracking
        BorrowedBook borrowedBook = new BorrowedBook(user, book, LocalDate.now(), LocalDate.now().plusWeeks(2));
        borrowedBook.setReturned(false);
        user.getBorrowedBooks().add(borrowedBook);
        userRepository.save(user);
        //borrowedBookRepository.save(borrowedBook);
    }


    /**
     * Allows a user to return a book that they borrowed.
     */
    @Override
    @Transactional
    public void returnBook(Long userId, Long bookId) throws LibraryException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new LibraryException("User not found."));

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new LibraryException("Book not found."));

        // Find the borrowed book record
        BorrowedBook borrowedBook = borrowedBookRepository.findByUserId(userId).stream()
                .filter(bb -> bb.getBook().getId().equals(bookId))
                .findFirst()
                .orElseThrow(() -> new LibraryException("This book was not borrowed by the user."));

        // Remove the borrowed book record from the database
        borrowedBookRepository.delete(borrowedBook);

        // Increase book quantity
        book.setQuantity(book.getQuantity() + 1);
        bookRepository.save(book);
    }


    /**
     * Fetches the list of books currently borrowed by a user.
     */
    @Override
    public List<Book> getUserBooksByName(String name) throws LibraryException {
        User user = userRepository.findByName(name)
                .orElseThrow(() -> new LibraryException("User not found with name: " + name));

        // Fetch all borrowed books for the user that are not returned
        List<BorrowedBook> borrowedBooks = borrowedBookRepository.findByUserId(user.getId());

        return borrowedBooks.stream()
                .filter(bb -> !bb.isReturned()) // Only return currently borrowed books
                .map(BorrowedBook::getBook)
                .collect(Collectors.toList());
    }

}
