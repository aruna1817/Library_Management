import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { Container, Form, Button, Card } from 'react-bootstrap';
import axios from 'axios';
import { Link } from 'react-router-dom';

const LoginForm = () => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const navigate = useNavigate();

  const handleLogin = async (e) => {
    e.preventDefault();
    setError('');

  
    try {
      let response; // Define response variable
    
      if (username.toLowerCase() === 'admin') {
        // Admin login
        response = await axios.post('http://localhost:8080/admin/login', {
          adminName: username,
          password: password,
        });
        console.log(response.data)
        // Check if response contains adminId
        if (response.data && response.data.admin_id) {
          console.log('Admin login success:', response.data);
          alert('Welcome Admin!');
          
          // Navigate to admin page after successful admin login
          navigate('/admin');
        } else {
          throw new Error('Invalid admin credentials');
        }
    
      } else {
        // User login
        console.log('Sending user login request:', { name: username, password });
    
        response = await axios.post('http://localhost:8080/user/login', {
          name: username,
          password: password,
        });
    
        // Check if response contains userId
        if (response && response.data && response.data.id) {
          const { id, name } = response.data;
          alert(`Welcome ${name}`);
          
          // Navigate to user page with userId and userName in the state
          navigate('/user', { state: { userId: id, userName: name } });
        } else {
          throw new Error('Invalid username or password');
        }
      }
    } catch (err) {
      console.error('Login error:', err.response?.data || err.message);
      setError(err.response?.data?.message || 'Login failed. Please check your credentials.');
    }
    
  
  
  };
    

  return (
    <Container>
      <div className="d-flex justify-content-center align-items-center" style={{ minHeight: '100vh' }}>
        <Card className="col-md-6 p-4">
          <h2 className="text-center">Login</h2>
          <Form onSubmit={handleLogin}>
            <Form.Group controlId="username">
              <Form.Label>Username</Form.Label>
              <Form.Control
                type="text"
                placeholder="Enter username"
                value={username}
                onChange={(e) => setUsername(e.target.value)}
                required
              />
            </Form.Group>

            <Form.Group controlId="password">
              <Form.Label>Password</Form.Label>
              <Form.Control
                type="password"
                placeholder="Enter password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                required
              />
            </Form.Group>

            <div className="text-center mt-3">
              <Button variant="primary" type="submit">
                Login
              </Button>
            </div>
          </Form>

          {error && <p className="text-danger mt-2 text-center">{error}</p>}

          <div className="mt-3 text-center">
            <Link to="/register">New user? Register here</Link>
          </div>
        </Card>
      </div>
    </Container>
  );
};

export default LoginForm;
