'use client';

import { useState } from 'react';
import { login } from '../lib/api';

export default function LoginForm() {
  const [name, setName] = useState('');
  const [password, setPassword] = useState('');
  const [message, setMessage] = useState('');

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    const res = await login(name, password);
    setMessage(res.message || 'Logged in!');
  };

  return (
    <form onSubmit={handleSubmit}>
      <h2>Login</h2>
      <input value={name} onChange={(e) => setName(e.target.value)} placeholder="Full Name" />
      <input type="password" value={password} onChange={(e) => setPassword(e.target.value)} placeholder="Password" />
      <button type="submit">Login</button>
      <p>{message}</p>
    </form>
  );
}