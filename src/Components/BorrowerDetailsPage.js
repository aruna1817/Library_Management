import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import { Table } from 'react-bootstrap';
import axios from 'axios';

const BorrowerDetailsPage = () => {
  const { bookId } = useParams(); // Get the bookId from the URL
  const [borrowers, setBorrowers] = useState([]);

  useEffect(() => {
    // Fetch the borrowers for this book
    axios
      .get(`http://localhost:8080/admin/getBookBorrowers/${bookId}`)
      .then((response) => {
        setBorrowers(response.data);
      })
      .catch((error) => {
        console.error('There was an error fetching the borrowers!', error);
      });
  }, [bookId]);

  return (
    <div className="container">
      <h2 className="text-center mb-4">Borrowers for Book {bookId}</h2>
      <Table striped bordered hover>
        <thead>
          <tr>
            <th>Name</th>
            <th>Email</th>
            <th>Borrowed Date</th>
            <th>End Date</th>
          </tr>
        </thead>
        <tbody>
          {borrowers.map((borrower) => (
            <tr key={borrower.id}>
              <td>{borrower.name}</td>
              <td>{borrower.email}</td>
              <td>{borrower.borrowedDate}</td>
              <td>{borrower.endDate}</td>
            </tr>
          ))}
        </tbody>
      </Table>
    </div>
  );
};

export default BorrowerDetailsPage;
