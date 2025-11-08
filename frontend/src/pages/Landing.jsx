import React, { use, useEffect } from 'react'
import HeroSection from '../components/landing/HeroSection'
import FeaturesSection from '../components/landing/FeaturesSection'
import PricingSection from '../components/landing/PricingSection'
import TestimonialSection from '../components/landing/TestimonialSection'
import CTASection from '../components/landing/CTASection'
import FooterSection from '../components/landing/FooterSection'
import { features, testimonials } from '../assets/data'
import { pricingPlans } from '../assets/data'
import { useClerk, useUser } from '@clerk/clerk-react'
import { useNavigate } from 'react-router-dom'

const Landing = () => {

  const { openSignIn, openSignUp } = useClerk();
  const { isSignedIn } = useUser();
  const navigate = useNavigate();

  useEffect(() => {
    if (isSignedIn) {
      navigate('/dashboard');
    }
  }, [isSignedIn, navigate]);

  return (
    <div className='landing-page bg-gradient-to-b from-gray-50 to-gray-100' >

    {/* Hero Section */}

    <HeroSection openSignIn={openSignIn} openSignUp={openSignUp} />

    {/* Features Section */}

    <FeaturesSection features={features} />

    {/* Pricing Section */}

    <PricingSection pricingPlans={pricingPlans} openSignIn={openSignIn} openSignUp={openSignUp} />

    {/* Testimonials Section */}

    <TestimonialSection testimonials={testimonials} />

    {/* Call to Action Section */}

    <CTASection openSignUp={openSignUp} />

    {/* Footer Section */}

    <FooterSection />

    </div>
  )
}

export default Landing