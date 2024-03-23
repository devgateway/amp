import React, { Component } from 'react';
import {
  Grid, GridColumn, GridRow, Header
} from 'semantic-ui-react';
import { bindActionCreators } from 'redux';
import { connect } from 'react-redux';
import PropTypes from 'prop-types';
import { OverlayTrigger, Tooltip } from 'react-bootstrap';
import { ReportGeneratorContext } from './StartUp';
import InfoIcon from '../static/images/icon-information.svg';
import { translate } from '../utils/Utils';
import { PROFILE_TAB } from '../utils/constants';

class MainHeader extends Component {
  handleResetAll = () => {
    const { translations, profile } = this.props;
    // eslint-disable-next-line no-restricted-globals
    if (confirm(translate('confirmResetAll', profile, translations))) {
      // eslint-disable-next-line no-restricted-globals
      location.reload();
    }
  }

  handleCancel = () => {
    const { translations, profile } = this.props;
    // eslint-disable-next-line no-restricted-globals
    if (confirm(translate('confirmCancel', profile, translations))) {
      if (profile !== PROFILE_TAB) {
        window.location.href = '/viewTeamReports.do?tabs=false&reset=true';
      } else {
        window.location.href = '/viewTeamReports.do?tabs=true&reset=true';
      }
    }
  }

  render() {
    const { translations, profile } = this.props;
    const tooltipText = (
      <Tooltip id="reset-all-tooltip">
        {translate('resetAllTooltip', profile, translations)}
      </Tooltip>
    );
    return (
      <>
        <Grid>
          <GridRow>
            <GridColumn width="6" textAlign="left">
              <Header textAlign="left" size="medium">
                {translate('reportGenerator', profile, translations)}
              </Header>
            </GridColumn>
            <GridColumn width="4" textAlign="left" />
            <GridColumn width="6" textAlign="right">
              <span className="green_text bold pointer" onClick={this.handleResetAll}>
                {translate('resetAll', profile, translations)}
                <OverlayTrigger trigger={['hover', 'focus']} overlay={tooltipText}>
                  <img className="info-icon" src={InfoIcon} alt="info-icon" />
                </OverlayTrigger>
              </span>
&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;
              <span className="red_text bold pointer" onClick={this.handleCancel}>
                {translate('cancel', profile, translations)}
              </span>
            </GridColumn>
          </GridRow>
          <GridRow>
            <GridColumn>{translate('appDescription', profile, translations)}</GridColumn>
          </GridRow>
        </Grid>
      </>
    );
  }
}

const mapStateToProps = state => ({
  translations: state.translationsReducer.translations,
  profile: state.uiReducer.profile,
});

const mapDispatchToProps = dispatch => bindActionCreators({}, dispatch);

export default connect(mapStateToProps, mapDispatchToProps)(MainHeader);

MainHeader.propTypes = {
  translations: PropTypes.object.isRequired,
  profile: PropTypes.string,
};

MainHeader.defaultProps = {
  profile: undefined,
};

MainHeader.contextType = ReportGeneratorContext;
