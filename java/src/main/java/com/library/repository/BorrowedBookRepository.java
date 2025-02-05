package com.library.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.library.enity.Book;
import com.library.enity.BorrowedBook;
import com.library.enity.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface BorrowedBookRepository extends JpaRepository<BorrowedBook, Long> {
    List<BorrowedBook> findByUserId(Long userId);
    List<BorrowedBook> findByBookId(Long bookId);
    Optional<BorrowedBook> findByIdAndBookId(Long userId, Long bookId);
    List<BorrowedBook> findByUser(User user);
    
    // Additional custom queries
    List<BorrowedBook> findByUserAndReturnedFalse(User user);
    List<BorrowedBook> findByBookAndReturnedFalse(Book book);
}
