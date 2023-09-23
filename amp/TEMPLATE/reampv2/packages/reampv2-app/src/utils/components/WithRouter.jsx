import React, {memo} from 'react';
import {useLocation, useNavigate, useParams} from 'react-router-dom';

export const withRouter = (Component) => {
    return memo((props) => {
        const navigate = useNavigate();
        const location = useLocation();
        const params = useParams();

        return (
            <Component
                navigate={navigate}
                location={location}
                params={params}
                {...props}
            />
        );
    });
};
