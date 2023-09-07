import React, {memo} from 'react';
import {useLocation, useNavigate, useParams, useSearchParams} from 'react-router-dom';

export const withRouter = (Component) => {
    return memo((props) => {
        const navigate = useNavigate();
        const location = useLocation();
        const params = useParams();
        const [ searchParams] = useSearchParams();

        return (
            <Component
                navigate={navigate}
                location={location}
                params={params}
                searchParams={searchParams}
                {...props}
            />
        );
    });
};
