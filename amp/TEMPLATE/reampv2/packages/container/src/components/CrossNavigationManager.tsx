import React, {useEffect} from 'react';
import {EDIT_PROFILE_MODAL_EVENT_NAME, USER_MANAGER_APP_NAME} from "../utils/constants";
import {useLocation, useNavigate} from "react-router-dom";

const CrossNavigationManager = ({ children }: { children?: React.ReactNode }) => {
    const navigate = useNavigate();
    const location = useLocation();

    useEffect(() => {
        localStorage.setItem('currentPath', location.pathname);
        window.addEventListener(EDIT_PROFILE_MODAL_EVENT_NAME, (e: any) => {
            if (location.pathname === USER_MANAGER_APP_NAME){
                return;
            }

            navigate(`/${USER_MANAGER_APP_NAME}/edit-profile`);
        });

        return () => {
            window.removeEventListener(EDIT_PROFILE_MODAL_EVENT_NAME, () => {});
        }
    }, []);


    return (
        <div>
            {children}
        </div>
    )
}
export default CrossNavigationManager;

