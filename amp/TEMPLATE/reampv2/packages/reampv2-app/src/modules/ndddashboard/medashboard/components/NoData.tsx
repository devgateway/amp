import React from "react";
import {DefaultTranslations} from "../types";


const NoData = ({ translations} : { translations: DefaultTranslations }) => {
    return (
        <h3 style={{
            textAlign: 'center',
            display: 'flex',
            justifyContent: 'center',
            alignItems: 'center',
            marginTop: '30%'
        }}>
            {translations['amp.ndd.dashboard:me-no-data']}
        </h3>
    );
};

export default NoData;
