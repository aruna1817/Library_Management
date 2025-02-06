import React, { useEffect, useState } from "react";
import { Table, Button } from "react-bootstrap";
import { useParams, useNavigate } from "react-router-dom";
import axios from "axios";

const BorrowerDetailsPage = () => {
  const { bookId } = useParams();
  const [borrowers, setBorrowers] = useState([]);
  const navigate = useNavigate();

  useEffect(() => {
    fetchBorrowerDetails();
  }, []);

  const fetchBorrowerDetails = async () => {
    try {
      const response = await axios.get(`http://localhost:8080/admin/getBorrowerDetails/${bookId}`);
      setBorrowers(response.data);
    } catch (error) {
      console.error("Error fetching borrower details:", error);
    }
  };

  return (
    <div className="container">
      <h2 className="text-center mb-4">Borrower Details</h2>
      <Button variant="secondary" onClick={() => navigate(-1)}>
        Back
      </Button>

      <Table striped bordered hover className="mt-3">
        <thead>
          <tr>
            <th>User ID</th>
            <th>Name</th>
            <th>Email</th>
            <th>Returned Status</th>
          </tr>
        </thead>
        <tbody>
          {borrowers.map((borrower) => (
            <tr key={borrower.userId}>
              <td>{borrower.userId}</td>
              <td>{borrower.userName}</td>
              <td>{borrower.email}</td>
              <td>{borrower.returned ? "Returned" : "Not Returned"}</td>
            </tr>
          ))}
        </tbody>
      </Table>
    </div>
  );
};

export default BorrowerDetailsPage;
