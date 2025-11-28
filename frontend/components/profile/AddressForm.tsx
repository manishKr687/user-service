
'use client';

import * as React from 'react';
import { Box, TextField, Button, FormControlLabel, Checkbox } from '@mui/material';
import api from '@/services/api';
import { Address } from './AddressManagement';

interface AddressFormProps {
    address?: Address | null;
    onSave: () => void;
}

export default function AddressForm({ address, onSave }: AddressFormProps) {
    const [street, setStreet] = React.useState(address?.street || '');
    const [building, setBuilding] = React.useState(address?.building || '');
    const [city, setCity] = React.useState(address?.city || '');
    const [state, setState] = React.useState(address?.state || '');
    const [pincode, setPincode] = React.useState(address?.pincode || '');
    const [isDefault, setIsDefault] = React.useState(address?.isDefault || false);
    const [error, setError] = React.useState('');

    const handleSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
        event.preventDefault();
        setError('');
        const payload = { street, building, city, state, pincode, isDefault };

        try {
            if (address) {
                // Update existing address
                await api.put(`/user/address/${address.id}`, payload);
            } else {
                // Create new address
                await api.post('/user/address', payload);
            }
            onSave(); // Notify parent to refresh and close form
        } catch (err) {
            console.error('Failed to save address:', err);
            setError('Could not save address.');
        }
    };

    return (
        <Box component="form" onSubmit={handleSubmit} noValidate>
            <TextField margin="normal" required fullWidth label="Street" value={street} onChange={(e) => setStreet(e.target.value)} />
            <TextField margin="normal" required fullWidth label="Building / Apartment" value={building} onChange={(e) => setBuilding(e.target.value)} />
            <TextField margin="normal" required fullWidth label="City" value={city} onChange={(e) => setCity(e.target.value)} />
            <TextField margin="normal" required fullWidth label="State" value={state} onChange={(e) => setState(e.target.value)} />
            <TextField margin="normal" required fullWidth label="Pincode" value={pincode} onChange={(e) => setPincode(e.target.value)} />
            <FormControlLabel
                control={<Checkbox checked={isDefault} onChange={(e) => setIsDefault(e.target.checked)} />}
                label="Set as default address"
            />
            {error && <Typography color="error" sx={{ mt: 2 }}>{error}</Typography>}
            <Button type="submit" variant="contained" sx={{ mt: 3 }}>
                Save Address
            </Button>
        </Box>
    );
}
