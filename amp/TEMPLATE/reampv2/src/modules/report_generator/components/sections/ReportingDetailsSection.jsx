import React, { Component } from 'react';
import PropTypes from 'prop-types';
import {
  Form,
  Grid, GridColumn, GridRow, TextArea
} from 'semantic-ui-react';
import { bindActionCreators } from 'redux';
import { connect } from 'react-redux';
import OptionsList from './OptionsList';
import { ReportGeneratorContext } from '../StartUp';
import { TRN_PREFIX } from '../../utils/constants';
import OptionsContent from './OptionsContent';

class ReportingDetailSection extends Component {
  render() {
    const { visible, translations } = this.props;

    const fakeReportTypesRadio = ['Summary Report', 'Annual Report', 'Quarterly Report', 'Monthly Report'];
    const fakeReportTypesCheck = ['Totals Only'];
    const fakeFundingGrouping = ['Donor Report (Donor Funding)', 'Regional Report (Regional Funding)',
      'Component Report (Component Funding)'];
    const fakeOptions = ['Allow empty funding columns for year, quarter and month',
      'Split by funding',
      'Show Original reporting currencies'];

    return (
      <div className={!visible ? 'invisible-tab' : ''}>
        <Grid>
          <GridRow>
            <GridColumn width="8">
              <OptionsList title={translations[`${TRN_PREFIX}totalGrouping`]} isRequired tooltip="tooltip 1" >
                <OptionsContent radioList={fakeReportTypesRadio} checkList={fakeReportTypesCheck} selectedRadio={0} />
              </OptionsList>
            </GridColumn>
            <GridColumn width="8">
              <OptionsList title={translations[`${TRN_PREFIX}reportDescription`]} isRequired tooltip="tooltip 2" >
                <Form>
                  <TextArea />
                </Form>
              </OptionsList>
            </GridColumn>
          </GridRow>
          <GridRow>
            <GridColumn width="8">
              <OptionsList title={translations[`${TRN_PREFIX}fundingGroup`]} tooltip="tooltip 3" >
                <OptionsContent radioList={fakeFundingGrouping} />
              </OptionsList>
            </GridColumn>
            <GridColumn width="8">
              <OptionsList title={translations[`${TRN_PREFIX}options`]} tooltip="tooltip 4" >
                <OptionsContent checkList={fakeOptions} />
              </OptionsList>
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
