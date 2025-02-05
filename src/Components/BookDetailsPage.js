import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import { Table, Button, Container } from 'react-bootstrap';
import axios from 'axios';

const BookDetailsPage = () => {
  const { id } = useParams(); // Get the book ID from the URL
  const [bookDetails, setBookDetails] = useState(null);
  const [borrowers, setBorrowers] = useState([]);

  useEffect(() => {
    // Fetch book details and borrower information
    axios
      .get(`http://localhost:8080/admin/getBookDetails/${id}`)
      .then((response) => {
        setBookDetails(response.data.book); // Book details
        setBorrowers(response.data.borrowers); // Borrower data
      })
      .catch((error) => {
        console.error('There was an error fetching the book details!', error);
      });
  }, [id]);

  // Checkout book (decrease quantity by 1)
  const checkoutBook = () => {
    axios
      .put(`http://localhost:8080/user/checkoutBook/${id}`)
      .then((response) => {
        setBookDetails({ ...bookDetails, quantity: bookDetails.quantity - 1 });
      })
      .catch((error) => {
        console.error('Error during checkout', error);
      });
  };

  // Return book (increase quantity by 1)
  const returnBook = () => {
    axios
      .put(`http://localhost:8080/user/returnBook/${id}`)
      .then((response) => {
        setBookDetails({ ...bookDetails, quantity: bookDetails.quantity + 1 });
      })
      .catch((error) => {
        console.error('Error during return', error);
      });
  };

  return (
    <Container>
      {bookDetails ? (
        <>
          <h2>Book Details</h2>
          <h4>{bookDetails.bookName} - {bookDetails.author}</h4>
          <p><strong>Edition:</strong> {bookDetails.edition}</p>
          <p><strong>Quantity:</strong> {bookDetails.quantity}</p>
          
          <h4>Borrowers:</h4>
          <Table striped bordered hover>
            <thead>
              <tr>
                <th>User Name</th>
                <th>Email</th>
                <th>Borrow Date</th>
                <th>Return Date</th>
              </tr>
            </thead>
            <tbody>
              {borrowers.length > 0 ? (
                borrowers.map((borrower, index) => (
                  <tr key={index}>
                    <td>{borrower.name}</td>
                    <td>{borrower.emailId}</td>
                    <td>{borrower.borrowDate}</td>
                    <td>{borrower.returnDate}</td>
                  </tr>
                ))
              ) : (
                <tr>
                  <td colSpan="4">No users have borrowed this book.</td>
                </tr>
              )}
            </tbody>
          </Table>

          <Button variant="primary" onClick={checkoutBook} disabled={bookDetails.quantity <= 0}>
            Checkout Book
          </Button>
          <Button variant="secondary" onClick={returnBook}>
            Return Book
          </Button>
        </>
      ) : (
        <p>Loading book details...</p>
      )}
    </Container>
  );
};

export default BookDetailsPage;
