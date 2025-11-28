
'use client';

import * as React from 'react';
import { Box, Typography, Button, CircularProgress } from '@mui/material';
import api from '@/services/api';
import AddressList from './AddressList';
import AddressForm from './AddressForm';

export interface Address {
    id: number;
    street: string;
    building: string;
    city: string;
    state: string;
    pincode: string;
    isDefault: boolean;
}

export default function AddressManagement() {
    const [addresses, setAddresses] = React.useState<Address[]>([]);
    const [loading, setLoading] = React.useState(true);
    const [error, setError] = React.useState('');
    const [editingAddress, setEditingAddress] = React.useState<Address | null>(null);
    const [showAddForm, setShowAddForm] = React.useState(false);

    const fetchAddresses = async () => {
        try {
            setLoading(true);
            const response = await api.get('/user/address');
            setAddresses(response.data);
        } catch (err) {
            console.error('Failed to fetch addresses:', err);
            setError('Could not load addresses.');
        } finally {
            setLoading(false);
        }
    };

    React.useEffect(() => {
        fetchAddresses();
    }, []);

    const handleEdit = (address: Address) => {
        setEditingAddress(address);
        setShowAddForm(false);
    };

    const handleDelete = async (addressId: number) => {
        if (window.confirm('Are you sure you want to delete this address?')) {
            try {
                await api.delete(`/user/address/${addressId}`);
                fetchAddresses(); // Refresh list
            } catch (err) {
                console.error('Failed to delete address:', err);
                setError('Could not delete address.');
            }
        }
    };

    const handleFormSuccess = () => {
        fetchAddresses();
        setEditingAddress(null);
        setShowAddForm(false);
    }

    if (loading) return <CircularProgress />;
    if (error) return <Typography color="error">{error}</Typography>;

    return (
        <Box>
            <AddressList addresses={addresses} onEdit={handleEdit} onDelete={handleDelete} />
            <Button variant="contained" onClick={() => { setShowAddForm(true); setEditingAddress(null); }} sx={{ mt: 2 }}>
                Add New Address
            </Button>

            {showAddForm && (
                <Box sx={{mt: 4}}>
                    <Typography variant="h6">Add New Address</Typography>
                    <AddressForm onSave={handleFormSuccess} />
                </Box>
            )}

            {editingAddress && (
                 <Box sx={{mt: 4}}>
                    <Typography variant="h6">Edit Address</Typography>
                    <AddressForm address={editingAddress} onSave={handleFormSuccess} />
                </Box>
            )}
        </Box>
    );
}
