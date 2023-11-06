import React, { useEffect, useRef} from 'react';
import {mount} from "reampv2App/Reampv2App";
import {useLocation, useNavigate} from "react-router-dom";
import {REAMPV2_APP_NAME} from "../utils/constants";

const reampv2Basename = `/${REAMPV2_APP_NAME}`;

const Reampv2 = () => {
    const ref = useRef<HTMLDivElement>(null);
    const location = useLocation();
    const navigate = useNavigate();


    // Listen to navigation events dispatched inside reampv2 mfe.
    useEffect(() => {
        const reampv2NavigationEventHandler = (event: Event) => {
            const pathname = (event as CustomEvent<string>).detail;
            const newPathname = `${reampv2Basename}${pathname}`;

            if (newPathname === location.pathname) {
                return;
            }
            // localStorage.setItem("searchParams", location.search);
            navigate({
                pathname: newPathname,
                search: location.search,
            });
        };
        window.addEventListener("[reampv2] navigated", reampv2NavigationEventHandler);

        return () => {
            window.removeEventListener(
                "[reampv2] navigated",
                reampv2NavigationEventHandler
            );
        };
    }, [location]);

    // Listen for container location changes and dispatch a notification.
    useEffect(() => {
        if (location.pathname.startsWith(reampv2Basename)) {
            localStorage.setItem('currentPath', location.pathname);
            const containerEvent = new CustomEvent("[container] navigated", {
                detail: location.pathname.replace(reampv2Basename, ""),
            });
            localStorage.setItem("searchParams", location.search);
            window.dispatchEvent(containerEvent);
        }
    }, [location]);

    const isFirstRunRef = useRef(true);
    const unmountRef = useRef(() => {});
    // Mount reampv2 MFE
    useEffect(
        () => {
            if (!isFirstRunRef.current) {
                return;
            }
            unmountRef.current = mount({
                mountPoint : ref.current!,
                initialPathName: location.pathname.replace(
                    reampv2Basename,
                    ''
                ),
                standalone: false
            });
            isFirstRunRef.current = false;
        },
        [location],
    );

    useEffect(() => unmountRef.current, []);

    return (
        <div ref={ref} id="reampv2-app-mfe" />
    );
}
export default Reampv2
