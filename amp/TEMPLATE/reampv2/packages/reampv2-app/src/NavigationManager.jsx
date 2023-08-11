import React, {useEffect} from 'react'
import {matchRoutes, useLocation, useNavigate} from "react-router-dom";
import routes from "./routing/routes";

const NavigationManager = ({ children }) => {
    const location = useLocation();
    const navigate = useNavigate();

    useEffect(() => {
        window.addEventListener('[container] navigated', (e) => {
            console.log('navigated', e);
            navigate(e.detail);
        });

    }, [location]);

    return children;
}
export default NavigationManager;
