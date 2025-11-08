import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import './index.css'
import App from './App.jsx'
import { ClerkProvider } from '@clerk/clerk-react'

const clerkApiKey = import.meta.env.VITE_CLERK_PUBLISHABLE_KEY


createRoot(document.getElementById('root')).render(
    <ClerkProvider publishableKey={clerkApiKey}>
        <App />
    </ClerkProvider>
)
