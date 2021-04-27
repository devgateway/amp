import React, { Component } from 'react';
import { Button, ButtonGroup } from 'semantic-ui-react';
import PropTypes from 'prop-types';
import { bindActionCreators } from 'redux';
import { connect } from 'react-redux';
import { TRN_PREFIX } from '../../utils/constants';

class NavigationButtons extends Component {
  constructor() {
    super();
    this.state = { nextEnabled: false, backEnabled: false };
  }

  setEnabledOrDisabledButtons = () => {
    // TODO: Logic to enable/disable each button.
  }

  handleBackClick = () => {
    const { backClick } = this.props;
    backClick();
  }

  handleNextClick=() => {
    const { nextClick } = this.props;
    nextClick();
  }

  render() {
    const { nextEnabled, backEnabled } = this.state;
    const { translations } = this.props;
    return (
      <ButtonGroup className="navigation-buttons">
        <Button positive={backEnabled} disabled={!backEnabled}>{translations[`${TRN_PREFIX}back`]}</Button>
        <Button positive={nextEnabled} disabled={!nextEnabled}>{translations[`${TRN_PREFIX}next`]}</Button>
      </ButtonGroup>
    );
  }
}

const mapStateToProps = state => ({
  translations: state.translationsReducer.translations
});

const mapDispatchToProps = dispatch => bindActionCreators({}, dispatch);

export default connect(mapStateToProps, mapDispatchToProps)(NavigationButtons);

NavigationButtons.propTypes = {
  backClick: PropTypes.func.isRequired,
  nextClick: PropTypes.func.isRequired,
  translations: PropTypes.object.isRequired,
};
