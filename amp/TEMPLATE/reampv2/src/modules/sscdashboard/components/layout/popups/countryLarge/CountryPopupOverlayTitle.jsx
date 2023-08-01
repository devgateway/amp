import React, {useContext, useEffect, useState} from 'react';
import '../popups.css';
import PropTypes from 'prop-types';
import {SSCTranslationContext} from '../../../StartUp';

const CountryPopupOverlayTitle = ({ countriesMessage, updateCountriesMessage }) => {
  const translationsContext = useContext(SSCTranslationContext);
  const [show, setShow] = useState(countriesMessage);
  const style = {
    background: '#FFFFFF',
    color: '#000000',
    'font-weight': 'bold',
    'margin-bottom': '50px'
  };
  useEffect(() => {
    const timer = setTimeout(() => {
      if (show) {
        if (updateCountriesMessage) {
          updateCountriesMessage(false);
        }
        setShow(false);
      }
    }, 10000);
    return () => clearTimeout(timer);
  }, [show, updateCountriesMessage]);
  return (
    <div>
      {!show && (<h2>&nbsp;</h2>)}
      {show && (
        <div className="alert alert-primary" role="alert" style={style}>
          <p>{translationsContext.translations['amp.ssc.dashboard:six']}</p>
        </div>
      )}
    </div>
  );
};

export default CountryPopupOverlayTitle;
CountryPopupOverlayTitle.propTypes = {
  countriesMessage: PropTypes.bool,
  updateCountriesMessage: PropTypes.func.isRequired
};
CountryPopupOverlayTitle.defaultProps = {
  countriesMessage: false
};