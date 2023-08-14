import React, {MutableRefObject, useEffect, useRef} from 'react';
import {mount} from 'reampv2App/Reampv2App';
import {useLocation, useNavigate} from "react-router-dom";

const reampv2Basename = '/reampv2-app';

const Reampv2 = () => {
    const ref: MutableRefObject<any> = useRef(null);
    const location = useLocation();
    const navigate = useNavigate();

    // Listen to navigation events dispatched inside reampv2 mfe.
    useEffect(() => {
        const reampv2NavigationEventHandler = (event: any) => {
            const pathname = event.detail;
            const newPathname = `${reampv2Basename}${pathname}`;

            if (newPathname === location.pathname) {
                return;
            }
            navigate(pathname);
        };
        window.addEventListener("[reampv2] navigated", reampv2NavigationEventHandler);

        return () => {
            window.removeEventListener(
                "[reampv2] navigated",
                reampv2NavigationEventHandler
            );
        };
    }, [location]);

    // Listen for shell location changes and dispatch a notification.
    useEffect(() => {
            if (location.pathname === reampv2Basename) {
                const reampv2NavigationEvent = new CustomEvent("[container] navigated", {
                    detail: location.hash.replace("#/", ""),
                });
                window.dispatchEvent(reampv2NavigationEvent);
            }
            }, [location]);

    const isFirstRunRef = useRef(true);
    const unmountRef = useRef(() => {
    });

    useEffect(() => {
        if (!isFirstRunRef.current) {
            return;
        }

        unmountRef.current = mount({
            el: ref.current,
            standalone: false
        });
        isFirstRunRef.current = false;
    }, [location]);

    useEffect(() => unmountRef.current, []);

    return (
        <div ref={ref}/>
    );
}
export default Reampv2
