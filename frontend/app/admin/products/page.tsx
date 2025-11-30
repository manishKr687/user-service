'use client';

import React, { useEffect, useState } from 'react';
import productApi from '../../../services/productApi';
import { useAuth } from '@/contexts/AuthContext';
import { useRouter } from 'next/navigation';
import withAdminAuth from '@/hocs/withAdminAuth'; // Import the HOC

interface Product {
  id: string;
  name: string;
  description: string;
  price: number;
  brand: string;
  category: string;
  active: boolean;
}

interface NewProduct {
  name: string;
  description: string;
  price: number;
  brand: string;
  category: string;
  active: boolean;
}

const AdminProductsPage = () => {
  // const { isAuthenticated, getUserRole } = useAuth(); // No longer needed directly here due to HOC
  // const router = useRouter(); // No longer needed directly here due to HOC

  const [products, setProducts] = useState<Product[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [newProduct, setNewProduct] = useState<NewProduct>({
    name: '',
    description: '',
    price: 0,
    brand: '',
    category: '',
    active: true,
  });
  // const [isAuthorizing, setIsAuthorizing] = useState(true); // HOC handles authorization state

  useEffect(() => {
    // The HOC handles authentication and role checks.
    // We only need to fetch products once the component is rendered (meaning authorized).
    fetchProducts();
  }, []); // Dependencies removed as HOC handles auth state

  const fetchProducts = async () => {
    try {
      const response = await productApi.get('/api/products');
      setProducts(response.data.content);
    } catch (err) {
      setError('Failed to fetch products');
    } finally {
      setLoading(false);
    }
  };

  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
    const { name, value, type, checked } = e.target as HTMLInputElement;
    setNewProduct((prev) => ({
      ...prev,
      [name]: type === 'checkbox' ? checked : value,
    }));
  };

  const handleAddProduct = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      // Assuming a specific admin endpoint for adding products
      await productApi.post('/admin/products', newProduct); 
      setNewProduct({
        name: '',
        description: '',
        price: 0,
        brand: '',
        category: '',
        active: true,
      });
      fetchProducts(); // Refresh product list
    } catch (err) {
      setError('Failed to add product');
    }
  };

  if (loading) { // isAuthorizing check is handled by HOC now
    return <div>Loading admin panel...</div>;
  }

  if (error) {
    return <div>{error}</div>;
  }

  return (
    <div style={{ padding: '20px' }}>
      <h1>Admin Products Management</h1>

      <h2>Add New Product</h2>
      <form onSubmit={handleAddProduct} style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '10px', maxWidth: '600px' }}>
        <label>
          Name:
          <input type="text" name="name" value={newProduct.name} onChange={handleInputChange} required />
        </label>
        <label>
          Description:
          <textarea name="description" value={newProduct.description} onChange={handleInputChange}></textarea>
        </label>
        <label>
          Price:
          <input type="number" name="price" value={newProduct.price} onChange={handleInputChange} required min="0" step="0.01" />
        </label>
        <label>
          Brand:
          <input type="text" name="brand" value={newProduct.brand} onChange={handleInputChange} required />
        </label>
        <label>
          Category:
          <input type="text" name="category" value={newProduct.category} onChange={handleInputChange} required />
        </label>
        <label>
          Active:
          <input type="checkbox" name="active" checked={newProduct.active} onChange={handleInputChange} />
        </label>
        <button type="submit" style={{ gridColumn: 'span 2' }}>Add Product</button>
      </form>

      <h2 style={{ marginTop: '30px' }}>Existing Products</h2>
      <ul style={{ listStyleType: 'none', padding: 0 }}>
        {products.map((product) => (
          <li key={product.id} style={{ border: '1px solid #ccc', margin: '10px 0', padding: '10px' }}>
            <h3>{product.name} ({product.brand} - {product.category})</h3>
            <p>{product.description}</p>
            <p>Price: ${product.price}</p>
            <p>Status: {product.active ? 'Active' : 'Inactive'}</p>
            {/* Add Update and Delete buttons here later */}
          </li>
        ))}
      </ul>
    </div>
  );
};

export default withAdminAuth(AdminProductsPage); // Wrap with HOC
