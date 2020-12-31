import React from 'react';
import './SimplePopup.css';

const SimplePopup = (props) => {
  const { message, onClose } = props;
  return (
    <div className="simple-popup">
      <a
        className="x-close"
        onClick={e => {
          onClose(e);
        }}>
        x
      </a>
      {message}
    </div>
  );
};
export default SimplePopup;
