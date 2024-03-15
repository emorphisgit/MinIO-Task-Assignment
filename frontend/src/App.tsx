import React from 'react';
import './App.css';
import Home from './Pages/Home';
import { Box } from '@mui/material';
import AlertPopUp from './Utils/AlertPopUp';


function App() {
  return (
    <>
     <AlertPopUp />
      <Box p={2}>
        <Home />
      </Box>
    </>
  );
}

export default App;
