import React, { useEffect, useRef} from 'react';
import {mount} from "userManager/UserManagerApp";
import {useLocation, useNavigate} from "react-router-dom";
import {USER_MANAGER_APP_NAME} from "../utils/constants";

const userManagerBasename = `/${USER_MANAGER_APP_NAME}`;

const UserManager = () => {
    const ref = useRef<HTMLDivElement>(null);
    const location = useLocation();
    const navigate = useNavigate();

    // Listen to navigation events dispatched inside UserManager mfe.
    useEffect(() => {
        const reampv2NavigationEventHandler = (event: Event) => {
            const pathname = (event as CustomEvent<string>).detail;
            const newPathname = `${userManagerBasename}${pathname}`;

            if (newPathname === location.pathname) {
                return;
            }
            navigate(newPathname);
        };
        window.addEventListener("[UserManager] navigated", reampv2NavigationEventHandler);

        return () => {
            window.removeEventListener(
                "[UserManager] navigated",
                reampv2NavigationEventHandler
            );
        };
    }, [location]);

    // Listen for container location changes and dispatch a notification.
    useEffect(() => {
        if (location.pathname.startsWith(userManagerBasename)) {
            const containerEvent = new CustomEvent("[container] navigated", {
                detail: location.pathname.replace(userManagerBasename, ""),
            });

            window.dispatchEvent(containerEvent);
        }
    }, [location]);

    const isFirstRunRef = useRef(true);
    const unmountRef = useRef(() => {});
    // Mount UserManager MFE
    useEffect(
        () => {
            if (!isFirstRunRef.current) {
                return;
            }
            unmountRef.current = mount({
                mountPoint : ref.current!,
                initialPathName: location.pathname.replace(
                    userManagerBasename,
                    ''
                ),
                standalone: false,
            });
            isFirstRunRef.current = false;
        },
        [location],
    );

    useEffect(() => unmountRef.current, []);

    return (
        <div ref={ref} id="userManager-app-mfe" />
    );
}
export default UserManager;
