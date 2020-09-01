import React, { useContext } from "react";
import { Link } from "react-router-dom";

import { ROUTES_HOME } from '../../../utils/constants';
import { SSCTranslationContext } from '../../StartUp';

export const HomeButton = (props) => {
    const {translations} = useContext(SSCTranslationContext)
    return (
        <div>
            <Link to={ROUTES_HOME}>
                <button className="btn btn-home" type="button">
                    {translations['amp.ssc.dashboard:Home-Page']}
                </button>
            </Link>
        </div>
    );
};
