import './App.css';
//components import
import SideNavigationMenu from './components/SideNavigationMenu';
import { useState, useEffect } from 'react';
import TopNavigationMenu from './components/TopNavigationMenu';
import { Route, Routes, useLocation, Navigate, useNavigate } from 'react-router-dom';
import Dashboard from './components/Dashboard';
import Login from './components/Login';

function App() {
  const [open, setOpen] = useState(false);
  const location = useLocation();
  const [authenticated, setAuthenticated] = useState(false);
  const [loading, setLoading] = useState(true); // Track initial loading

  const navigate = useNavigate();

  // Check if there is any localStorage entry on initial render and page refresh
  useEffect(() => {
    let isAuthenticated = false;
    const data = window.localStorage.getItem('Data');
    if (data) {
      isAuthenticated = true;
      setAuthenticated(isAuthenticated);
    }
    setLoading(false); // Set loading to false after initial render

    // If not authenticated and not on the login page, redirect to login
    if (!isAuthenticated && location.pathname !== '/login') {
      
      navigate("/login");

    }
  },[authenticated]);

  // // If loading, show loading indicator
  // if (loading) {
  //   return <div>Loading...</div>;
  // }

  return (
    <div className="bg-cyan-100 p-5 h-screen">
      {/* <div className={`${location.pathname !== '/login' ? "ml-5 flex h-full" : "h-full"} `}>
        {(location.pathname !== '/login' && location.pathname !== '/setPassword' && location.pathname !== '/forgotPasswordMail' && location.pathname !== '/doctorOnboarding' && location.pathname !== '/doctorStatus') && (
          <SideNavigationMenu
            open={open}
            setOpen={setOpen}
          />
        )}
        <div className={` ${location.pathname !== '/login' ? "ml-5 flex flex-col w-full h-full" : "h-full"} `}>
          {(location.pathname !== '/login' && location.pathname !== '/setPassword' && location.pathname !== '/forgotPasswordMail') && (
            <TopNavigationMenu
              open={open}
              setOpen={setOpen}
            />
          )} */}
          <Routes>
            <Route path='/main' element={<Dashboard />} />
            <Route path='login' element={<Login setAuthenticated = {setAuthenticated}/>} />
          </Routes>
        {/* </div>
      </div> */}
  </div>

  );
}

export default App;
