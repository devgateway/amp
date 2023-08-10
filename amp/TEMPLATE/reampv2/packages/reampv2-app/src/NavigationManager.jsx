import React, {useEffect} from 'react'
import {matchRoutes, useLocation, useNavigate} from "react-router-dom";
import routes from "./routing/routes";

const NavigationManager = ({ children }) => {
    const location = useLocation();
    const navigate = useNavigate();

    console.log('NavigationManager', location);


    useEffect(() => {
        debugger;
        function containerNavigationHandler(event) {
            const pathname = event.detail;
            console.log('containerNavigationHandler========>', pathname);
            if (location.pathname !== pathname || !matchRoutes(routes, { pathname })) {
                return void 0;
            }
            navigate(pathname);
        }

        window.addEventListener('[container] navigated', containerNavigationHandler);


        return () => {
           window.removeEventListener('[container] navigated', containerNavigationHandler);
        }
        // eslint-disable-next-line
    }, []);

    // useEffect(() => {
    //     console.log('NavigationManager===============>', location);
    //     const reampv2Event = new CustomEvent("[reampv2] navigated", {detail: location.hash });
    //     window.dispatchEvent(reampv2Event);
    // }, [location.hash]);

    return children;
}
export default NavigationManager;
