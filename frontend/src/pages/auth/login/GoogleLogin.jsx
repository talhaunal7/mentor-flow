import React from 'react';
import { GoogleLogin } from '@react-oauth/google';

const GoogleSignIn = ({ onGoogleSignIn }) => {
    return (
        <GoogleLogin
            onSuccess={(credentialResponse) => {
                onGoogleSignIn(credentialResponse);
            }}
            onError={() => {
                console.log('Login Failed');
            }}
            useOneTap
        />
    );
};

export default GoogleSignIn;

