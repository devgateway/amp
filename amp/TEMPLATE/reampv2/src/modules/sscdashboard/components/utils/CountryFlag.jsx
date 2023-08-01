import {useImage} from 'react-image';
import React, {Suspense} from 'react';
import PropTypes from 'prop-types';
import * as Utils from '../../utils/Utils';
import {FALLBACK_FLAG} from '../../utils/constants';

const Flag = ({ countryName }) => {
  const { src } = useImage({
    srcList: Utils.getCountryFlag(countryName)
  });
  return (<img src={src} alt={countryName} />);
};
const CountryFlag = ({ countryName }) => {
  return (
    <Suspense fallback={<img alt="loading" src={FALLBACK_FLAG} />}>
      <Flag countryName={countryName} />
    </Suspense>
  );
};
CountryFlag.propTypes = {
  countryName: PropTypes.string.isRequired
};

Flag.propTypes = {
  countryName: PropTypes.string.isRequired
};
export default CountryFlag;
