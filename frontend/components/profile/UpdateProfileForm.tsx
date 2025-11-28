
'use client';

import * as React from 'react';
import { Box, TextField, Button, Typography } from '@mui/material';
import api from '@/services/api';

interface UserProfile {
    fullName: string;
    email: string;
    phone: string | null;
    profileImage: string | null;
}

interface UpdateProfileFormProps {
    profile: UserProfile;
    onProfileUpdate: () => void; // Function to refresh profile data on parent
}

export default function UpdateProfileForm({ profile, onProfileUpdate }: UpdateProfileFormProps) {
    const [fullName, setFullName] = React.useState(profile.fullName);
    const [phone, setPhone] = React.useState(profile.phone || '');
    const [error, setError] = React.useState('');
    const [success, setSuccess] = React.useState('');

    const handleSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
        event.preventDefault();
        setError('');
        setSuccess('');
        const payload = { fullName, phone, profileImage: profile.profileImage }; // profileImage is not editable in this form for simplicity
        try {
            await api.put('/user/profile', payload);
            setSuccess('Profile updated successfully!');
            onProfileUpdate(); // Trigger data refresh in parent component
        } catch (err) {
            console.error('Update failed:', err);
            setError('Profile update failed.');
        }
    };

    return (
        <Box component="form" onSubmit={handleSubmit} noValidate sx={{ mt: 1 }}>
            <TextField
                margin="normal"
                required
                fullWidth
                id="fullName"
                label="Full Name"
                name="fullName"
                value={fullName}
                onChange={(e) => setFullName(e.target.value)}
            />
            <TextField
                margin="normal"
                required
                fullWidth
                id="phone"
                label="Phone Number"
                name="phone"
                value={phone}
                onChange={(e) => setPhone(e.target.value)}
            />
            {error && <Typography color="error" sx={{ mt: 2 }}>{error}</Typography>}
            {success && <Typography color="primary" sx={{ mt: 2 }}>{success}</Typography>}
            <Button
                type="submit"
                fullWidth
                variant="contained"
                sx={{ mt: 3, mb: 2 }}
            >
                Update Profile
            </Button>
        </Box>
    );
}
