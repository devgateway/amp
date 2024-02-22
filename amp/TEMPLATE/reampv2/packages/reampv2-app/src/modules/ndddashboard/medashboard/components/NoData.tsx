import React from "react";
import { Row } from "react-bootstrap";
import {DefaultTranslations} from "../types";


const NoData = ({ translations} : { translations: DefaultTranslations }) => {
    return (
        <Row  md={12} style={{
            display: 'flex',
            marginLeft: 0,
            alignItems: 'center',
            justifyContent: 'center',
            paddingTop: '10%',
            paddingBottom: '10%'

        }}>
        <h3 style={{
            textAlign: 'center',
            display: 'flex',
            justifyContent: 'center',
            alignItems: 'center',
        }}>
            {translations['amp.ndd.dashboard:me-no-data']}
        </h3>
        </Row>
    );
};

export default NoData;
