import React, { useState, useEffect } from 'react';
import { Table, Button, Spinner } from 'react-bootstrap';
import axios from 'axios';
import { useNavigate, useLocation } from 'react-router-dom';

const UserPage = () => {
  const location = useLocation();
  const { userId, userName } = location.state || { userId: null, userName: null };

  const [userDetails, setUserDetails] = useState(null);
  const [books, setBooks] = useState([]);
  const [userBooks, setUserBooks] = useState([]);
  const [loadingUserDetails, setLoadingUserDetails] = useState(true);
  const [loadingBooks, setLoadingBooks] = useState(true);

  const navigate = useNavigate();

  useEffect(() => {
    if (userId) {
      axios
        .get(`http://localhost:8080/user/details/${userId}`)
        .then((response) => {
          setUserDetails(response.data);
          setLoadingUserDetails(false);
        })
        .catch(() => {
          setUserDetails(null);
          setLoadingUserDetails(false);
        });
    }
  }, [userId]);

  useEffect(() => {
    if (loadingUserDetails) return;

    setLoadingBooks(true);

    axios
      .get('http://localhost:8080/user/viewAllBooks')
      .then((response) => {
        setBooks(Array.isArray(response.data) ? response.data : []);
      })
      .catch(() => setBooks([]));

    if (userId) {
      axios
        .get(`http://localhost:8080/user/borrowedBooks/${userId}`)
        .then((response) => {
          setUserBooks(Array.isArray(response.data) ? response.data : []);
        })
        .catch(() => setUserBooks([]));
    }
  }, [userDetails, loadingUserDetails, userId]);

  useEffect(() => {
    if (books.length > 0 && userBooks.length >= 0) {
      setLoadingBooks(false);
    }
  }, [books, userBooks]);

  const handleCheckout = (id) => {
    // Check if user has reached the borrow limit (3 different types of books)
    const uniqueBookTypes = new Set(userBooks.map((book) => book.type));
    if (uniqueBookTypes.size >= 3) {
      alert('You can only check out up to 3 books of different types.');
      return;
    }
  
    // Check if the user has already checked out this book
    const isBookAlreadyCheckedOut = userBooks.some((book) => book.id === id);
    if (isBookAlreadyCheckedOut) {
      alert('You can only check out one copy of each book.');
      return;
    }
  
    // Proceed with the checkout process
    axios
      .post(`http://localhost:8080/user/checkoutBook/${userId}/${id}`)
      .then((response) => {
        alert('Book checked out successfully!');
        window.location.reload();
       // Find the selected book from available books
        const selectedBook = books.find((book) => book.id === id);
        if (selectedBook) {
          const borrowedDate = new Date().toISOString().split('T')[0];
          const dueDate = new Date();
          dueDate.setDate(dueDate.getDate() + 14); // 14-day loan period
          const formattedDueDate = dueDate.toISOString().split('T')[0];
  
          // Add the borrowed book to userBooks state
          const bookWithDates = { ...selectedBook, borrowedDate, dueDate: formattedDueDate };
  
          setUserBooks((prev) => [...prev, bookWithDates]);
  
          // Update book quantity (decrease by 1)
          setBooks((prev) =>
            prev.map((book) =>
              book.id === id ? { ...book, quantity: book.quantity - 1 } : book
            )
          );
        }
      })
      .catch((error) => {
        alert('Error: ' + (error.response?.data || 'Failed to checkout book'));
      });
  };
  

  const handleReturnBook = async (id) => {
    try {
      const response = await axios.post(
        `http://localhost:8080/user/returnBook?userId=${userId}&bookId=${id}`
      );
  
      console.log("Return response:", response.data); // Debugging log
  
      alert('Book returned successfully!');

      window.location.reload();
  
      // Update userBooks state: remove the returned book
      setUserBooks((prev) => prev.filter((book) => book.id !== id));
  
      // Update books quantity: increment by 1 for the returned book
      setBooks((prev) =>
        prev.map((book) =>
          book.id === id ? { ...book, quantity: book.quantity + 1 } : book
        )
      );
  
      // Check borrow limit after returning a book
      const uniqueBookTypes = new Set(userBooks.map((book) => book.type));
      if (uniqueBookTypes.size < 3) {
        // User can now borrow more books, enable "Checkout" button again
        alert('You can now borrow more books!');
      }
  
    } catch (error) {
      console.error("Error returning book:", error.response?.data || error.message);
      alert('Error: ' + (error.response?.data || 'Failed to return book'));
    }
  };
  
  


  const handleLogout = () => {
    localStorage.removeItem('authToken');
    navigate('/');
  };

  return (
    <div className="container">
      <div className="user-details mb-4">
        <h5>User Details</h5>
        {loadingUserDetails ? (
          <p>Loading user details...</p>
        ) : userDetails ? (
          <>
            <p><strong>User ID:</strong> {userDetails.id}</p>
            <p><strong>Name:</strong> {userDetails.name}</p>
            <p><strong>Email:</strong> {userDetails.email}</p>
          </>
        ) : (
          <p>Failed to load user details.</p>
        )}
      </div>

      <h2 className="text-center mb-4">User Page</h2>
      <Button variant="danger" onClick={handleLogout} className="mb-4">
        Logout
      </Button>

      {loadingBooks ? (
        <div className="d-flex justify-content-center">
          <Spinner animation="border" variant="primary" />
        </div>
      ) : (
        <>
          <h4>Available Books</h4>
          <Table striped bordered hover>
            <thead>
              <tr>
                <th>ID</th>
                <th>Name</th>
                <th>Author</th>
                <th>Edition</th>
                <th>Quantity</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              {books.map((book) => (
                <tr key={book.id}>
                  <td>{book.id}</td>
                  <td>{book.bookName}</td>
                  <td>{book.author}</td>
                  <td>{book.edition}</td>
                  <td>{book.quantity}</td>
                  <td>
                    <Button
                      variant="primary"
                      onClick={() => handleCheckout(book.id)}
                      disabled={book.quantity <= 0}
                    >
                      Checkout
                    </Button>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>

          <h4>Your Borrowed Books</h4>
          <Table striped bordered hover>
            <thead>
              <tr>
                <th>ID</th>
                <th>Name</th>
                <th>Author</th>
                <th>Edition</th>
                <th>Borrowed Date</th>
                <th>Due Date</th> {/* New Column */}
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              {userBooks.map((book) => (
                <tr key={book.id}>
                  <td>{book.book_id}</td>
                  <td>{book.bookName}</td>
                  <td>{book.author}</td>
                  <td>{book.book_edition}</td>
                  <td>{book.borrowedDate}</td>
                  <td>{book.dueDate || 'N/A'}</td> {/* Display due date */}
                  <td>
                    <Button variant="danger" onClick={() => handleReturnBook(book.book_id)}>
                      Return
                    </Button>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        </>
      )}
    </div>
  );
};

export default UserPage;
