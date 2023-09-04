import React, {useEffect} from 'react'
import {matchRoutes, useLocation, useNavigate} from "react-router-dom";
import { routes } from "../routing/routes";

const NavigationManager = ({ children }: { children: any }) => {
    const location = useLocation();
    const navigate = useNavigate();

    useEffect(() => {
        function containerNavigationHandler(event: CustomEvent) {
            const pathname = event.detail;
            if (location.pathname === pathname || !matchRoutes(routes, { pathname })) {
                return;
            }
            navigate(pathname);
        }

        //@ts-ignore
        window.addEventListener("[container] navigated", containerNavigationHandler);

        return () => {
            //@ts-ignore
            window.removeEventListener("[container] navigated", containerNavigationHandler);
        };
    }, [location]);

    useEffect(() => {
        const reampv2Event = new CustomEvent("[myapp] navigated", { detail: location.pathname })
        window.dispatchEvent(reampv2Event);

    }, [location]);

    return children;
}
export default NavigationManager;
