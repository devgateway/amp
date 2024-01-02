import React, {useEffect} from 'react'
import { matchRoutes, useLocation, useNavigate, useSearchParams } from "react-router-dom";
import routes from "./routing/routes";

const NavigationManager = ({ children }) => {
    const location = useLocation();
    const [ searchParams] = useSearchParams();
    const navigate = useNavigate();

    useEffect(() => {
        function containerNavigationHandler(event) {
            const pathname = event.detail.pathname;
            if (location.pathname === pathname || !matchRoutes(routes, { pathname })) {
                return;
            }

            const searchParamsString = searchParams.toString();
            navigate({
                pathname,
                search: searchParamsString ? `?${searchParamsString}` : "",
            });
        }

        window.addEventListener("[container] navigated", containerNavigationHandler);

        return () => {
            window.removeEventListener("[container] navigated", containerNavigationHandler);
        };
    }, [location]);

    useEffect(() => {
        const reampv2Event = new CustomEvent("[reampv2] navigated", { detail: location.pathname })
        window.dispatchEvent(reampv2Event);

    }, [location]);

    return children;
}
export default NavigationManager;
