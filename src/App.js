import './App.css';
import LoginForm from './Components/Login';
import 'bootstrap/dist/css/bootstrap.min.css';
import RegistrationForm from './Components/RegistrationForm';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import AdminPage from './Components/Admin';
import UserPage from './Components/User';
import BookDetailsPage from './Components/BookDetailsPage';
import BorrowerDetailsPage from './Components/BorrowerDetailsPage';

function App() {
  return (
    <div>
      <Router>
        <Routes>
          <Route path="/" element={<LoginForm />} />
          <Route path="/register" element={<RegistrationForm />} />
          <Route path="/admin" element={<AdminPage />} />
          <Route path="/borrowerDetails/:bookId" element={<BorrowerDetailsPage />} />

          <Route path="/book/:id" element={<BookDetailsPage />} />
          <Route path="/user" element={<UserPage />} />
        </Routes>
      </Router>
    </div>
  );
}

export default App;
