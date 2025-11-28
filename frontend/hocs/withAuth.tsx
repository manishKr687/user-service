
'use client';

import { useEffect } from 'react';
import { useRouter } from 'next/navigation';
import { useAuth } from '@/contexts/AuthContext';

const withAuth = <P extends object>(WrappedComponent: React.ComponentType<P>) => {
  const AuthComponent = (props: P) => {
    const { isAuthenticated } = useAuth();
    const router = useRouter();

    useEffect(() => {
      // This check runs on the client side after the component mounts.
      // The initial isAuthenticated state might be false until the effect in AuthContext runs.
      // A more robust solution might involve a loading state in the AuthContext.
      const token = localStorage.getItem('token');
      if (!token) {
        router.replace('/login');
      }
    }, [isAuthenticated, router]);

    // You can add a loading spinner here while checking for authentication
    if (!isAuthenticated) {
        // This will be shown briefly on initial load or if not authenticated.
        return <p>Loading...</p>; 
    }

    return <WrappedComponent {...props} />;
  };

  return AuthComponent;
};

export default withAuth;
