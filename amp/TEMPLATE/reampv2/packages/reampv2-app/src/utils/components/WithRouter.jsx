import React, {memo, useEffect} from 'react';
import {useLocation, useNavigate, useParams, useSearchParams} from 'react-router-dom';

export const withRouter = (Component) => {
    return memo((props) => {
        const navigate = useNavigate();
        const location = useLocation();
        const params = useParams();
        const [ searchParams, setSearchParams] = useSearchParams();

        const extractSearchParams = () => {
            let storedParams = localStorage.getItem('searchParams');

            location.search = storedParams;

            if (storedParams) {
                storedParams = storedParams.replace('?', '');
                const paramsArr  = storedParams.split('&');

                paramsArr.map((param) => {
                    const [key, value] = param.split('=');
                    setSearchParams(key, value);
                    return [key, value];

                });
            }
        }

        useEffect(() => {
            extractSearchParams();
        }, []);

        return (
            <>
                    <Component
                        navigate={navigate}
                        _location={location}
                        urlParams={params}
                        searchParams={searchParams}
                        storedSearchParams={localStorage.getItem('searchParams')}
                        {...props}
                    />
            </>

        );
    });
};
