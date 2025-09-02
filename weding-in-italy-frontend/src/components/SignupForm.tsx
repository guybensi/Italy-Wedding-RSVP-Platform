'use client';

import { useState } from 'react';

export default function SignupForm() {
  const [name, setName] = useState('');

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    alert(`Created guest: ${name}`);
  };

  return (
    <form onSubmit={handleSubmit}>
      <h2>Sign Up</h2>
      <input value={name} onChange={(e) => setName(e.target.value)} placeholder="Full Name" />
      <button type="submit">Create</button>
    </form>
  );
}