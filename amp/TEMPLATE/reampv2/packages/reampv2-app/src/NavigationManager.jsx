import React, {useEffect} from 'react'
import {matchRoutes, useLocation, useNavigate} from "react-router-dom";
import routes from "./routing/routes";

const NavigationManager = ({ children }) => {
    const location = useLocation();
    const navigate = useNavigate();

    console.log('reampv2========', location)

    useEffect(() => {
        function containerNavigationHandler(event) {
            const pathname = event.detail;
            console.log('my pathname gotcha', pathname)
            if (location.pathname === pathname || !matchRoutes(routes, { pathname })) {
                return;
            }
            navigate(pathname);
        }

        window.addEventListener("[container] navigated", containerNavigationHandler);

        return () => {
            window.removeEventListener("[container] navigated", containerNavigationHandler);
        };
    }, [location]);

    useEffect(() => {
        console.log('debug current location before dispatch', location)
        const reampv2Event = new CustomEvent("[reampv2] navigated", { detail: location.pathname })
        window.dispatchEvent(reampv2Event);

    }, [location]);

    return children;
}
export default NavigationManager;
