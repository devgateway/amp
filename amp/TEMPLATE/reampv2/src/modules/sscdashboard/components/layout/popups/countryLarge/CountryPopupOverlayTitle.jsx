import React, { useEffect, useState } from 'react';
import '../popups.css';
import PropTypes from 'prop-types';

const CountryPopupOverlayTitle = ({ countriesMessage, updateCountriesMessage }) => {
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
  }, []);
  return (
    <div>
      {!show && (<h2>&nbsp;</h2>)}
      {show && (
        <div className="alert alert-primary" role="alert" style={style}>
          <p>
            Only the first six countries were carried over. If something different is needed it
            will need to be modified with the flags/filters.
          </p>
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
}
