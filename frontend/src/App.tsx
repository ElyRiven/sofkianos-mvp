import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import { Navbar } from './components/Navbar';
import LandingPage from './pages/LandingPage';
import KudosPage from './pages/KudosPage';
import KudosPublicPage from './pages/KudosPublicPage';
import './index.css';

function App() {
  return (
    <Router>
      <div className="min-h-screen bg-zinc-950 text-zinc-100 selection:bg-[#FF5F00] selection:text-zinc-950">
        <Navbar />

        <main className="relative overflow-hidden">
          <Routes>
            <Route path="/" element={<LandingPage />} />
            <Route path="/kudos" element={<KudosPage />} />
            <Route path="/kudos-public" element={<KudosPublicPage />} />
          </Routes>
        </main>
      </div>
    </Router>
  );
}

export default App;
