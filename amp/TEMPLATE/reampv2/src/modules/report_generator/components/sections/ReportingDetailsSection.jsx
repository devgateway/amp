import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { Grid, GridColumn, GridRow } from 'semantic-ui-react';
import { bindActionCreators } from 'redux';
import { connect } from 'react-redux';
import OptionsList from './OptionsList';
import { ReportGeneratorContext } from '../StartUp';
import { TRN_PREFIX } from '../../utils/constants';

class ReportingDetailSection extends Component {
  render() {
    const { visible, translations } = this.props;
    return (
      <div className={!visible ? 'invisible-tab' : ''}>
        <Grid>
          <GridRow>
            <GridColumn width="8">
              <OptionsList title={translations[`${TRN_PREFIX}totalGrouping`]} isRequired tooltip="tooltip 1" />
            </GridColumn>
            <GridColumn width="8">
              <OptionsList title={translations[`${TRN_PREFIX}options`]} isRequired tooltip="tooltip 2" />
            </GridColumn>
          </GridRow>
          <GridRow>
            <GridColumn width="8">
              <OptionsList title={translations[`${TRN_PREFIX}fundingGroup`]} tooltip="tooltip 3" />
            </GridColumn>
          </GridRow>
        </Grid>
      </div>
    );
  }
}

const mapStateToProps = state => ({
  translations: state.translationsReducer.translations
});

const mapDispatchToProps = dispatch => bindActionCreators({}, dispatch);

export default connect(mapStateToProps, mapDispatchToProps)(ReportingDetailSection);

ReportingDetailSection.propTypes = {
  translations: PropTypes.object.isRequired,
  visible: PropTypes.bool.isRequired,
};

ReportingDetailSection.contextType = ReportGeneratorContext;
