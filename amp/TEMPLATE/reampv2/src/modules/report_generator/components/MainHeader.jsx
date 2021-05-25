import React, { Component } from 'react';
import {
  Grid, GridColumn, GridRow, Header, Segment
} from 'semantic-ui-react';
import { bindActionCreators } from 'redux';
import { connect } from 'react-redux';
import PropTypes from 'prop-types';
import { ReportGeneratorContext } from './StartUp';
import { TRN_PREFIX } from '../utils/constants';

class MainHeader extends Component {
  handleResetAll = () => {
    const { translations } = this.props;
    // eslint-disable-next-line no-restricted-globals
    if (confirm(translations[`${TRN_PREFIX}confirmResetAll`])) {
      // eslint-disable-next-line no-restricted-globals
      location.reload();
    }
  }

  handleCancel = () => {
    const { translations } = this.props;
    // eslint-disable-next-line no-restricted-globals
    if (confirm(translations[`${TRN_PREFIX}confirmCancel`])) {
      window.location.href = '/viewTeamReports.do?tabs=false&reset=true';
    }
  }

  render() {
    const { translations } = this.props;
    return (
      <>
        <Grid>
          <GridRow>
            <GridColumn width="6" textAlign="left">
              <Header textAlign="left" size="medium">
                {translations[`${TRN_PREFIX}reportGenerator`]}
              </Header>
            </GridColumn>
            <GridColumn width="4" textAlign="left" />
            <GridColumn width="6" textAlign="right">
              <span className="green_text bold pointer" onClick={this.handleResetAll}>
                {translations[`${TRN_PREFIX}resetAll`]}
              </span>
              {/* eslint-disable-next-line jsx-a11y/alt-text */}
              <img
                className="info-icon"
                src="/TEMPLATE/reamp/modules/admin/data-freeze-manager/styles/images/icon-information.svg" />
&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;
              <span className="red_text bold pointer" onClick={this.handleCancel}>
                {translations[`${TRN_PREFIX}cancel`]}
              </span>
            </GridColumn>
          </GridRow>
          <GridRow>
            <GridColumn>{translations[`${TRN_PREFIX}appDescription`]}</GridColumn>
          </GridRow>
        </Grid>
      </>
    );
  }
}

const mapStateToProps = state => ({
  translations: state.translationsReducer.translations
});

const mapDispatchToProps = dispatch => bindActionCreators({}, dispatch);

export default connect(mapStateToProps, mapDispatchToProps)(MainHeader);

MainHeader.propTypes = {
  translations: PropTypes.object.isRequired,
};

MainHeader.contextType = ReportGeneratorContext;
