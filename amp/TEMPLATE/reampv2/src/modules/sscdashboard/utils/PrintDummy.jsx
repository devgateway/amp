import React from 'react';
import PropTypes from 'prop-types';

const PrintDummy = ({ friendly }) => (
  <>
    <div id={`print${friendly ? '-friendly' : ''}-dummy`}>
      <div id={`print${friendly ? '-friendly' : ''}-dummy-container`} />
    </div>
    <div id={`print${friendly ? '-friendly' : ''}-simple-dummy`}>
      <div id={`print${friendly ? '-friendly' : ''}-simple-dummy-container`} />
    </div>
  </>
);
PrintDummy.propTypes = {
  friendly: PropTypes.bool.isRequired
};
export default PrintDummy;
