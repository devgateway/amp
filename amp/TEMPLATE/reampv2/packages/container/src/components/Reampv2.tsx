import React, {MutableRefObject, useEffect, useRef} from 'react';
import {mount} from 'reampv2App/Reampv2App';
import {useLocation, useNavigate} from "react-router-dom";

const reampv2Basename = '/reampv2-app';

const Reampv2 = () => {
    const ref: MutableRefObject<any> = useRef(null);
    const navigate = useNavigate();
    const location = useLocation();

    console.log("location", location);

    useEffect(() => {
        const reampv2EventHandler = (event: any) => {
            const pathname = event.detail;
            const newPathname = `${reampv2Basename}${pathname}`;

            console.log("new pathname", newPathname);
            if (newPathname === location.hash) {
                return;
            }
            navigate(newPathname);
        };
        window.addEventListener("[reampv2] navigated", reampv2EventHandler);

        return () => {
            window.removeEventListener(
                "[reampv2] navigated",
                reampv2EventHandler
            );
        };

        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [location]);

    // Listen for shell location changes and dispatch a notification.
    useEffect(() => {
        if (location.pathname.startsWith(reampv2Basename)) {
            console.log("dispatching event");
            window.dispatchEvent(
                new CustomEvent("[container] navigated", {
                    detail: location.hash,
                })
            );
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
        });
        isFirstRunRef.current = false;
    }, [location]);

    useEffect(() => unmountRef.current, []);

    return (
        <div ref={ref}/>
    );
}
export default Reampv2
