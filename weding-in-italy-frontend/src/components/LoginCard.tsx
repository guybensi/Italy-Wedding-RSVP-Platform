'use client';
import { useState } from 'react';
import { login } from '@/lib/api';

const MANAGER_NAME = 'גיא בן שמחון';

export default function LoginCard() {
  const [name, setName] = useState('');
  const [password, setPassword] = useState<string | undefined>(undefined);
  const [showPwd, setShowPwd] = useState(false);
  const [loading, setLoading] = useState(false);
  const [err, setErr] = useState<string | null>(null);

  function handleName(v: string) {
    setName(v);
    const isManager = v.trim() === MANAGER_NAME;
    setShowPwd(isManager);
    if (!isManager) {
      setPassword(undefined);
    }
  }

  async function doLogin(e: React.FormEvent) {
    e.preventDefault();
    setErr(null);
    setLoading(true);
    try {
      const res = await login(name.trim(), password);
      localStorage.setItem('token', res.token);
      localStorage.setItem('role', res.role);
      localStorage.setItem('inviterId', res.inviterId);
      window.location.href = res.role === 'MANAGER' ? '/admin' : '/me';
    } catch (e: any) {
      setErr(e?.message || 'Login failed');
    } finally {
      setLoading(false);
    }
  }

  return (
    <form onSubmit={doLogin}
      style={{
        width: 380,
        maxWidth: '90vw',
        background: '#fff',
        padding: 24,
        borderRadius: 16,
        boxShadow:
          '0 1px 2px rgba(0,0,0,.06), 0 8px 24px rgba(0,0,0,.08)'
      }}>
      <h1 style={{ margin: 0, fontSize: 28, fontWeight: 700 }}>Wedding RSVP</h1>
      <p style={{ color: '#667085', marginTop: 6, marginBottom: 18 }}>
        היכנס עם השם המלא שלך. סיסמה נדרשת רק למנהל.
      </p>

      <label style={{ fontSize: 13, color: '#475467' }}>שם מלא</label>
      <input
        value={name}
        onChange={(e) => handleName(e.target.value)}
        placeholder="הקלד/י שם מלא"
        required
        style={{
          width: '100%',
          padding: '10px 12px',
          border: '1px solid #e5e7eb',
          borderRadius: 10,
          outline: 'none',
          marginTop: 6,
          marginBottom: 10
        }}
      />

      {showPwd && (
        <>
          <label style={{ fontSize: 13, color: '#475467' }}>סיסמת מנהל</label>
          <input
            value={password ?? ''}
            onChange={(e) => setPassword(e.target.value)}
            placeholder="5810"
            type="password"
            required
            style={{
              width: '100%',
              padding: '10px 12px',
              border: '1px solid #e5e7eb',
              borderRadius: 10,
              outline: 'none',
              marginTop: 6,
              marginBottom: 10
            }}
          />
        </>
      )}

      {err && (
        <div style={{
          background: '#fef2f2',
          color: '#b91c1c',
          padding: '8px 10px',
          borderRadius: 8,
          marginBottom: 10,
          fontSize: 13
        }}>
          {err}
        </div>
      )}

      <button disabled={loading}
        style={{
          width: '100%',
          padding: '10px 12px',
          borderRadius: 10,
          background: loading ? '#94a3b8' : 'linear-gradient(90deg,#2563eb,#7c3aed)',
          color: '#fff',
          fontWeight: 600,
          border: 'none',
          cursor: 'pointer'
        }}>
        {loading ? 'נכנס...' : 'כניסה'}
      </button>

      <div style={{ fontSize: 12, color: '#667085', marginTop: 10 }}>
        * אם השם לא קיים – יוּצר משתמש חדש אוטומטית.
      </div>
    </form>
  );
}
