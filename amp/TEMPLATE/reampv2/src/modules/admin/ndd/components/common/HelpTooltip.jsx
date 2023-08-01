import React, {Component} from 'react';
import {OverlayTrigger, Tooltip} from 'react-bootstrap';
import PropTypes from 'prop-types';
import {bindActionCreators} from 'redux';
import {connect} from 'react-redux';
import {NDDContext} from '../Startup';

// eslint-disable-next-line no-unused-vars

class HelpTooltip extends Component {
  render() {
    const { translations, labelKey } = this.props;
    const tooltip = (
      <Tooltip id={`${labelKey}-icon-tooltip`}>
        {translations[labelKey]}
      </Tooltip>
    );
    return (
      <OverlayTrigger transition={false} trigger={['hover', 'focus']} placement="right" overlay={tooltip}>
        {/* eslint-disable-next-line jsx-a11y/alt-text */}
        <img
          className="info-icon"
          src="/TEMPLATE/reamp/modules/admin/data-freeze-manager/styles/images/icon-information.svg" />
      </OverlayTrigger>
    );
  }
}

HelpTooltip.contextType = NDDContext;

HelpTooltip.propTypes = {
  translations: PropTypes.object.isRequired,
  labelKey: PropTypes.string.isRequired
};

const mapStateToProps = state => ({
  translations: state.translationsReducer.translations
});
const mapDispatchToProps = dispatch => bindActionCreators({}, dispatch);
export default connect(mapStateToProps, mapDispatchToProps)(HelpTooltip);
