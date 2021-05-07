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
    alert('to be implemented');
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
            {/* eslint-disable-next-line max-len */}
            <Segment basic>Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.</Segment>
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
