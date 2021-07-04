import React, { useState } from 'react';
import '../popups.css';
import { Alert } from 'react-bootstrap';

const CountryPopupOverlayTitle = ({ countriesMessage }) => {
  const [show, setShow] = useState(countriesMessage);
  const style = {
    background: '#313d4f',
    color: 'white',
    'margin-bottom': '50px'
  }
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
