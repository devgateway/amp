import React, {memo, useEffect} from 'react';
import {useLocation, useNavigate, useParams, useSearchParams} from 'react-router-dom';

export const withRouter = (Component) => {
    return memo((props) => {
        const navigate = useNavigate();
        const location = useLocation();
        const params = useParams();
        const [ searchParams, setSearchParams] = useSearchParams();

        useEffect(() => {
            setSearchParams(localStorage.getItem("searchParams") || "");
        }, []);

        console.log('my searchParams', searchParams.get('profile'))

        return (
            <>
                {searchParams.get('profile') ? (
                    <Component
                        navigate={navigate}
                        location={location}
                        params={params}
                        searchParams={searchParams}
                        {...props}
                    />
                ) : (
                    <div>loading...</div>
                )}
            </>

        );
    });
};
