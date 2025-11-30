'use client';

import { useEffect } from 'react';
import { useRouter } from 'next/navigation';
import { useAuth } from '@/contexts/AuthContext';

const withAdminAuth = <P extends object>(WrappedComponent: React.ComponentType<P>) => {
  const AdminAuthComponent = (props: P) => {
    const { isAuthenticated, getUserRole } = useAuth();
    const router = useRouter();

    useEffect(() => {
      const token = localStorage.getItem('token');
      if (!token) {
        // If no token, redirect to admin login page
        router.replace('/admin/login');
      } else {
        const userRole = getUserRole();
        if (!isAuthenticated || userRole !== 'ADMIN') {
          // If authenticated but not admin, or token invalid, redirect to admin login page
          console.warn("Attempted to access admin page without ADMIN role. Redirecting to admin login.");
          router.replace('/admin/login');
        }
      }
    }, [isAuthenticated, getUserRole, router]);

    // Show loading or nothing while authentication status and role are being verified
    if (!isAuthenticated || getUserRole() !== 'ADMIN') {
        return <p>Loading admin content...</p>; 
    }

    return <WrappedComponent {...props} />;
  };

  return AdminAuthComponent;
};

export default withAdminAuth;
