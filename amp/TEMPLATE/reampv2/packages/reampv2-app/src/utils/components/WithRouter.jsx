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

        console.log("withRouter searchParams", searchParams);

        return (
            <>
                    <Component
                        navigate={navigate}
                        location={location}
                        urlParams={params}
                        searchParams={searchParams}
                        useSearchParams={useSearchParams}
                        {...props}
                    />
            </>

        );
    });
};
