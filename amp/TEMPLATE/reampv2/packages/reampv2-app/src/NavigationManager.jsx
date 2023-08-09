import React, {useEffect} from 'react'
import {createHashHistory} from "history";

const NavigationManager = ({ children }) => {
    const history = createHashHistory();
    const location = history.location;

    useEffect(() => {
        function containerNavigationHandler(event) {
            const pathname = event.detail;
            console.log('containerNavigationHandler', event)
            if (location.hash === pathname) {
                return;
            }
            history.push(pathname);
        }

        window.addEventListener('[container] navigated', containerNavigationHandler);

        return () => {
            window.removeEventListener('[container] navigated', containerNavigationHandler);
        }
        // eslint-disable-next-line
    }, []);

    // useEffect(() => {
    //     window.dispatchEvent(
    //         new CustomEvent("[reampv2] navigated", {detail: location.hash})
    //     );
    // }, [location]);

    return children;
}
export default NavigationManager;
