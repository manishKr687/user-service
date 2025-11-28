
'use client';

import * as React from 'react';
import { Container, Box, Typography, CircularProgress, Card, CardContent, Divider } from '@mui/material';
import withAuth from '@/hocs/withAuth';
import api from '@/services/api';
import UpdateProfileForm from '@/components/profile/UpdateProfileForm';
import AddressManagement from '@/components/profile/AddressManagement';

interface UserProfile {
  fullName: string;
  email: string;
  phone: string | null;
  profileImage: string | null;
}

function ProfilePage() {
  const [profile, setProfile] = React.useState<UserProfile | null>(null);
  const [loading, setLoading] = React.useState(true);
  const [error, setError] = React.useState('');

  const fetchProfile = async () => {
    try {
      setLoading(true);
      const response = await api.get('/user/profile');
      setProfile(response.data);
      setError('');
    } catch (err) {
      console.error('Failed to fetch profile:', err);
      setError('Failed to load profile data.');
    } finally {
      setLoading(false);
    }
  };

  React.useEffect(() => {
    fetchProfile();
  }, []);

  if (loading && !profile) {
    return (
      <Box sx={{ display: 'flex', justifyContent: 'center', mt: 4 }}>
        <CircularProgress />
      </Box>
    );
  }

  if (error) {
    return (
      <Typography color="error" align="center" sx={{ mt: 4 }}>
        {error}
      </Typography>
    );
  }

  return (
    <Container maxWidth="md">
      <Box sx={{ my: 4 }}>
        <Typography variant="h4" component="h1" gutterBottom>
          User Profile
        </Typography>
        {profile && (
          <Card sx={{ mb: 4 }}>
            <CardContent>
              <Typography variant="h6">Name: {profile.fullName}</Typography>
              <Typography color="text.secondary">Email: {profile.email}</Typography>
              <Typography color="text.secondary">Phone: {profile.phone || 'Not provided'}</Typography>
            </CardContent>
          </Card>
        )}
        
        {profile && (
          <Box sx={{ mb: 4 }}>
            <Typography variant="h5" component="h2" gutterBottom>
              Update Profile
            </Typography>
            <UpdateProfileForm profile={profile} onProfileUpdate={fetchProfile} />
          </Box>
        )}

        <Divider sx={{ my: 4 }} />

        <Box sx={{ my: 4 }}>
          <Typography variant="h5" component="h2" gutterBottom>
            Address Management
          </Typography>
          <AddressManagement />
        </Box>

      </Box>
    </Container>
  );
}

export default withAuth(ProfilePage);
