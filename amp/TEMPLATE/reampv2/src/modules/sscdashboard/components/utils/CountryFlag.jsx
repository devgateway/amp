import { useImage } from 'react-image';
import * as Utils from '../../utils/Utils';
import React, { Suspense } from 'react';
import { FALLBACK_FLAG } from '../../utils/constants';

const Flag = ({countryName}) => {
    const {src} = useImage({
        srcList: Utils.getCountryFlag(countryName)
    });
    return (<img src={src} alt={countryName}/>);
};
const CountryFlag = ({countryName}) => {
    return (<Suspense fallback={<img alt="loading" src={FALLBACK_FLAG}/>}>
        <Flag countryName={countryName}/>
    </Suspense>);
}

export default CountryFlag;
