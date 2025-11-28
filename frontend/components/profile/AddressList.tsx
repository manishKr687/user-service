
'use client';

import * as React from 'react';
import { List, ListItem, ListItemText, IconButton, Typography } from '@mui/material';
import EditIcon from '@mui/icons-material/Edit';
import DeleteIcon from '@mui/icons-material/Delete';
import { Address } from './AddressManagement';

interface AddressListProps {
    addresses: Address[];
    onEdit: (address: Address) => void;
    onDelete: (addressId: number) => void;
}

export default function AddressList({ addresses, onEdit, onDelete }: AddressListProps) {
    if (addresses.length === 0) {
        return <Typography>No addresses found.</Typography>;
    }

    return (
        <List>
            {addresses.map((address) => (
                <ListItem
                    key={address.id}
                    secondaryAction={
                        <>
                            <IconButton edge="end" aria-label="edit" onClick={() => onEdit(address)}>
                                <EditIcon />
                            </IconButton>
                            <IconButton edge="end" aria-label="delete" onClick={() => onDelete(address.id)}>
                                <DeleteIcon />
                            </IconButton>
                        </>
                    }
                >
                    <ListItemText
                        primary={`${address.street}, ${address.building}`}
                        secondary={`${address.city}, ${address.state} - ${address.pincode} ${address.isDefault ? '(Default)' : ''}`}
                    />
                </ListItem>
            ))}
        </List>
    );
}
