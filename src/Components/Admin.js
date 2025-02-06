import React, { useState, useEffect } from 'react';
import { Table, Button, Modal, Form } from 'react-bootstrap';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

const AdminPage = () => {
  const [books, setBooks] = useState([]);
  const [borrowedBooks, setBorrowedBooks] = useState([]);
  const [showAddBookModal, setShowAddBookModal] = useState(false);
  const [newBook, setNewBook] = useState({
    bookName: '',
    author: '',
    edition: '',
    quantity: 1,
  });
  const navigate = useNavigate();

  // Fetch books and borrowed books when the page loads
  useEffect(() => {
    fetchBooks();
    fetchBorrowedBooks();
  }, []);

  // Fetch all books
  const fetchBooks = async () => {
    try {
      const response = await axios.get('http://localhost:8080/admin/getAllBooks');
      console.log('Books fetched:', response.data);

      // Check if the response data is an array
      if (Array.isArray(response.data)) {
        setBooks(response.data); // Update state with the books array
      } else {
        console.error('Unexpected response format for books:', response.data);
      }
    } catch (error) {
      console.error('Error fetching books:', error);
    }
  };

  // Fetch all borrowed books with user information
  const fetchBorrowedBooks = async () => {
    try {
      const response = await axios.get('http://localhost:8080/admin/getBorrowedBooks');
      setBorrowedBooks(response.data);
    } catch (error) {
      console.error('Error fetching borrowed books:', error);
    }
  };

  // Navigate to Borrowed Details Page
  const handleViewBorrowedDetails = (bookId) => {
    navigate(`/borrowerDetails/${bookId}`);
  };
  

  // Add Book Form Handlers
  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setNewBook({ ...newBook, [name]: value });
  };

  const handleAddBook = async (e) => {
    e.preventDefault();
    try {
      const response = await axios.post('http://localhost:8080/admin/addBook', newBook);
      setBooks((prevBooks) => [...prevBooks, response.data]);
      setShowAddBookModal(false);
      setNewBook({
        bookName: '',
        author: '',
        edition: '',
        quantity: 1,
      });
    } catch (error) {
      console.error('Error adding book:', error);
    }
  };

  // Handle Decrease Book Quantity
  const handleDecreaseQuantity = async (book_id) => {
    try {
      await axios.put(`http://localhost:8080/admin/decreaseBookQuantity/${book_id}`);
      setBooks((prevBooks) =>
        prevBooks.map((book) =>
          book.id === book_id ? { ...book, quantity: book.quantity - 1 } : book
        )
      );
    } catch (error) {
      console.error('Error decreasing book quantity:', error);
    }
  };

  // Handle Increase Book Quantity
  const handleIncreaseQuantity = async (book_id) => {
    try {
      await axios.put(`http://localhost:8080/admin/increaseBookQuantity/${book_id}`);
      setBooks((prevBooks) =>
        prevBooks.map((book) =>
          book.id === book_id ? { ...book, quantity: book.quantity + 1 } : book
        )
      );
    } catch (error) {
      console.error('Error increasing book quantity:', error);
    }
  };

  // Handle Delete Book
  const handleDeleteBook = async (book_id) => {
    try {
      await axios.delete(`http://localhost:8080/admin/deleteBook/${book_id}`);
      setBooks((prevBooks) => prevBooks.filter((book) => book.id !== book_id));
      alert('Book deleted successfully.');
    } catch (error) {
      console.error('Error deleting book:', error);
    }
  };

  // Handle Logout
  const handleLogout = () => {
    localStorage.removeItem('authToken');
    navigate('/');
  };

  return (
    <div className="container">
      <h2 className="text-center mb-4">Admin Page</h2>

      {/* Logout Button */}
      <Button variant="danger" onClick={handleLogout} className="mb-4">
        Logout
      </Button>

      <Button variant="success" onClick={() => setShowAddBookModal(true)} className="mb-4">
        Add Book
      </Button>

      {/* Books Table */}
      <h4>Books</h4>
      <Table striped bordered hover>
        <thead>
          <tr>
            <th>book_Id</th>
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
                <Button variant="secondary" onClick={() => handleDecreaseQuantity(book.id)}>
                  -
                </Button>
                <Button variant="secondary" onClick={() => handleIncreaseQuantity(book.id)}>
                  +
                </Button>
                <Button variant="danger" onClick={() => handleDeleteBook(book.id)}>
                  Delete
                </Button>
                <Button variant="info" onClick={() => handleViewBorrowedDetails(book.id)}>
                  Borrowed Details
                </Button>
              </td>
            </tr>
          ))}
        </tbody>
      </Table>

      {/* Add Book Modal */}
      <Modal show={showAddBookModal} onHide={() => setShowAddBookModal(false)}>
        <Modal.Header closeButton>
          <Modal.Title>Add New Book</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <Form onSubmit={handleAddBook}>
            <Form.Group controlId="formBookName">
              <Form.Label>Book Name</Form.Label>
              <Form.Control
                type="text"
                name="bookName"
                value={newBook.bookName}
                onChange={handleInputChange}
                required
              />
            </Form.Group>

            <Form.Group controlId="formAuthor">
              <Form.Label>Author</Form.Label>
              <Form.Control
                type="text"
                name="author"
                value={newBook.author}
                onChange={handleInputChange}
                required
              />
            </Form.Group>

            <Form.Group controlId="formEdition">
              <Form.Label>Edition</Form.Label>
              <Form.Control
                type="text"
                name="edition"
                value={newBook.edition}
                onChange={handleInputChange}
                required
              />
            </Form.Group>

            <Form.Group controlId="formQuantity">
              <Form.Label>Quantity</Form.Label>
              <Form.Control
                type="number"
                name="quantity"
                value={newBook.quantity}
                onChange={handleInputChange}
                required
              />
            </Form.Group>

            <Button variant="primary" type="submit">
              Add Book
            </Button>
          </Form>
        </Modal.Body>
      </Modal>
    </div>
  );
};

export default AdminPage;
