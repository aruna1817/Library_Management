import { Link } from 'react-router-dom';
import 'bootstrap/dist/css/bootstrap.min.css';
import React, { useState } from 'react';
import { Container, Form, Button, Card, Modal } from 'react-bootstrap';
import axios from 'axios';

const RegistrationForm = () => {
  const [showModal, setShowModal] = useState(false);
  const [error, setError] = useState('');
  const [formData, setFormData] = useState({
    name: '',
    email: '',
    dob: '', // Collecting date of birth
    password: '',
    borrow_books: '', // This field seems unnecessary unless you plan to initialize it.
  });

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      const response = await axios.post('http://localhost:8080/user/register', formData);
      console.log('Registration successful', response.data);
      setShowModal(true);
    } catch (error) {
      console.error('There was an error registering the user!', error);
      setError('Registration failed. Please try again.');
    }
  };

  const handleCloseModal = () => {
    setShowModal(false);
  };

  return (
    <Container>
      <div className="d-flex justify-content-center align-items-center" style={{ minHeight: '100vh' }}>
        <Card className="col-md-6 p-4">
          <h2 className="text-center">Registration Page</h2>
          <Form onSubmit={handleSubmit}>
            <Form.Group controlId="formName">
              <Form.Label>Name</Form.Label>
              <Form.Control
                type="text"
                placeholder="Enter the name"
                name="name"
                value={formData.name}
                onChange={handleChange}
              />
            </Form.Group>
            <Form.Group controlId="formEmail">
              <Form.Label>Email address</Form.Label>
              <Form.Control
                type="email"
                name="email"
                placeholder="Enter the email"
                value={formData.email}
                onChange={handleChange}
              />
            </Form.Group>
            <Form.Group controlId="formDOB">
              <Form.Label>Date of Birth</Form.Label>
              <Form.Control type="date" name="dob" value={formData.dob} onChange={handleChange} />
            </Form.Group>
            <Form.Group controlId="formPassword">
              <Form.Label>Password</Form.Label>
              <Form.Control
                type="password"
                placeholder="Password"
                name="password"
                value={formData.password}
                onChange={handleChange}
              />
            </Form.Group>
            <Button variant="primary" type="submit">
              Register
            </Button>
            {error && <p className="text-danger mt-2">{error}</p>}
          </Form>
          <div className="text-center mt-3">
            <Link to="/">Already have an account? Login here.</Link>
          </div>
          <Modal show={showModal} onHide={handleCloseModal}>
            <Modal.Header closeButton>
              <Modal.Title>Registration Completed</Modal.Title>
            </Modal.Header>
            <Modal.Body>Your registration is completed successfully.</Modal.Body>
            <Modal.Footer>
              <Button variant="secondary" onClick={handleCloseModal}>
                Close
              </Button>
            </Modal.Footer>
          </Modal>
        </Card>
      </div>
    </Container>
  );
};

export default RegistrationForm;
