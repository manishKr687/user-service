
'use client';
import { Container, Typography, Box } from '@mui/material';

export default function Home() {
  return (
    <Container>
      <Box
        sx={{
          marginTop: 8,
          display: 'flex',
          flexDirection: 'column',
          alignItems: 'center',
        }}
      >
        <Typography variant="h4" component="h1" gutterBottom>
          Welcome to the E-commerce Site
        </Typography>
        <Typography variant="body1">
          Please use the links in the header to login or register.
        </Typography>
      </Box>
    </Container>
  );
}
