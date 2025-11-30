
'use client';

import React, { createContext, useContext, useState, useEffect, ReactNode, useCallback } from 'react';
import api from '@/services/api';
import { jwtDecode } from 'jwt-decode'; // Import jwtDecode

interface DecodedToken {
  sub: string; // Subject (username/email)
  role: string; // User role
  exp: number; // Expiration time
  iat: number; // Issued at time
  // Add other claims you expect
}

interface AuthContextType {
  isAuthenticated: boolean;
  user: DecodedToken | null;
  login: (token: string, refreshToken: string) => void;
  logout: () => void;
  getUserRole: () => string | null;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export function useAuth() {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
}

export function AuthProvider({ children }: { children: ReactNode }) {
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [user, setUser] = useState<DecodedToken | null>(null);

  const decodeAndSetUser = useCallback((token: string | null) => {
    if (token) {
      try {
        const decoded: DecodedToken = jwtDecode(token);
        // Check if token is expired
        if (decoded.exp * 1000 < Date.now()) {
          console.warn("Token expired.");
          logout(); // Log out if token is expired
          return null;
        }
        setIsAuthenticated(true);
        setUser(decoded);
        return decoded;
      } catch (error) {
        console.error("Failed to decode token:", error);
        logout();
        return null;
      }
    }
    setIsAuthenticated(false);
    setUser(null);
    return null;
  }, []); // Added decodeAndSetUser to dependencies

  useEffect(() => {
    const token = localStorage.getItem('token');
    decodeAndSetUser(token);
  }, [decodeAndSetUser]); // Added decodeAndSetUser to dependencies

  const login = (token: string, refreshToken: string) => {
    localStorage.setItem('token', token);
    localStorage.setItem('refreshToken', refreshToken);
    decodeAndSetUser(token);
  };

  const logout = useCallback(() => { // Memoize logout
    localStorage.removeItem('token');
    localStorage.removeItem('refreshToken');
    setIsAuthenticated(false);
    setUser(null);
    // You might want to also call a backend logout endpoint
  }, []); // No dependencies for logout

  const getUserRole = useCallback(() => {
    return user?.role || null;
  }, [user]);

  const value = {
    isAuthenticated,
    user,
    login,
    logout,
    getUserRole,
  };

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}
