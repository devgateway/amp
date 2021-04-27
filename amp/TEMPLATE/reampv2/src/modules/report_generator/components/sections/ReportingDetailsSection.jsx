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
import {
  updateReportDetailsTotalGrouping, updateReportDetailsTotalsOnly,
  updateReportDetailsFundingGrouping, updateReportDetailsAllowEmptyFundingColumns,
  updateReportDetailsSplitByFunding,
  updateReportDetailsShowOriginalCurrencies,
  updateReportDetailsDescription
} from '../../actions/stateUIActions';

class ReportingDetailSection extends Component {
  selectTotalGrouping = (e, { value }) => {
    const { _updateReportDetailsTotalGrouping } = this.props;
    _updateReportDetailsTotalGrouping(value);
  };

  selectTotalsOnly = () => {
    const { _updateReportDetailsTotalsOnly, selectedTotalsOnly } = this.props;
    _updateReportDetailsTotalsOnly(!selectedTotalsOnly);
  }

  selectFundingGrouping = (e, { value }) => {
    const { _updateReportDetailsFundingGrouping } = this.props;
    _updateReportDetailsFundingGrouping(value);
  }

  selectAllowEmptyFundingColumns = () => {
    const { _updateReportDetailsAllowEmptyFundingColumns, selectedAllowEmptyFundingColumns } = this.props;
    _updateReportDetailsAllowEmptyFundingColumns(!selectedAllowEmptyFundingColumns);
  }

  selectSplitByFunding = () => {
    const { _updateReportDetailsSplitByFunding, selectedSplitByFunding } = this.props;
    _updateReportDetailsSplitByFunding(!selectedSplitByFunding);
  }

  selectShowOriginalCurrencies = () => {
    const { _updateReportDetailsShowOriginalCurrencies, selectedShowOriginalCurrencies } = this.props;
    _updateReportDetailsShowOriginalCurrencies(!selectedShowOriginalCurrencies);
  }

  changeDescription = (e, { value }) => {
    const { _updateReportDetailsDescription } = this.props;
    _updateReportDetailsDescription(value);
  }

  render() {
    const {
      visible, translations, selectedTotalGrouping, selectedTotalsOnly, selectedFundingGrouping,
      selectedAllowEmptyFundingColumns, selectedSplitByFunding, selectedShowOriginalCurrencies,
      description
    } = this.props;

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
                <OptionsContent
                  radioList={fakeReportTypesRadio}
                  checkList={fakeReportTypesCheck}
                  selectedRadio={selectedTotalGrouping}
                  selectedCheckboxes={{ selectedTotalsOnly }}
                  changeCheckList={[this.selectTotalsOnly]}
                  changeRadioList={this.selectTotalGrouping} />
              </OptionsList>
            </GridColumn>
            <GridColumn width="8">
              <OptionsList title={translations[`${TRN_PREFIX}reportDescription`]} isRequired tooltip="tooltip 2" >
                <Form>
                  <TextArea value={description} onChange={this.changeDescription} />
                </Form>
              </OptionsList>
            </GridColumn>
          </GridRow>
          <GridRow />
          <GridRow>
            <GridColumn width="8">
              <OptionsList title={translations[`${TRN_PREFIX}fundingGroup`]} tooltip="tooltip 3" >
                <OptionsContent
                  radioList={fakeFundingGrouping}
                  changeRadioList={this.selectFundingGrouping}
                  selectedRadio={selectedFundingGrouping}
                />
              </OptionsList>
            </GridColumn>
            <GridColumn width="8">
              <OptionsList title={translations[`${TRN_PREFIX}options`]} tooltip="tooltip 4" >
                <OptionsContent
                  checkList={fakeOptions}
                  selectedCheckboxes={[selectedAllowEmptyFundingColumns, selectedSplitByFunding,
                    selectedShowOriginalCurrencies]}
                  changeCheckList={[this.selectAllowEmptyFundingColumns, this.selectSplitByFunding,
                    this.selectShowOriginalCurrencies]} />
              </OptionsList>
            </GridColumn>
          </GridRow>
        </Grid>
      </div>
    );
  }
}

const mapStateToProps = (state) => ({
  translations: state.translationsReducer.translations,
  selectedTotalGrouping: state.uiReducer.reportDetails.selectedTotalGrouping,
  selectedTotalsOnly: state.uiReducer.reportDetails.selectedTotalsOnly,
  reportDetails: state.uiReducer.reportDetails,
  selectedFundingGrouping: state.uiReducer.reportDetails.selectedFundingGrouping,
  selectedAllowEmptyFundingColumns: state.uiReducer.reportDetails.selectedAllowEmptyFundingColumns,
  selectedSplitByFunding: state.uiReducer.reportDetails.selectedSplitByFunding,
  selectedShowOriginalCurrencies: state.uiReducer.reportDetails.selectedShowOriginalCurrencies,
  description: state.uiReducer.reportDetails.description,
});

const mapDispatchToProps = dispatch => bindActionCreators({
  _updateReportDetailsTotalGrouping: (data) => dispatch(updateReportDetailsTotalGrouping(data)),
  _updateReportDetailsTotalsOnly: (data) => dispatch(updateReportDetailsTotalsOnly(data)),
  _updateReportDetailsFundingGrouping: (data) => dispatch(updateReportDetailsFundingGrouping(data)),
  _updateReportDetailsAllowEmptyFundingColumns: (data) => dispatch(updateReportDetailsAllowEmptyFundingColumns(data)),
  _updateReportDetailsSplitByFunding: (data) => dispatch(updateReportDetailsSplitByFunding(data)),
  _updateReportDetailsShowOriginalCurrencies: (data) => dispatch(updateReportDetailsShowOriginalCurrencies(data)),
  _updateReportDetailsDescription: (data) => dispatch(updateReportDetailsDescription(data)),
}, dispatch);

export default connect(mapStateToProps, mapDispatchToProps)(ReportingDetailSection);

ReportingDetailSection.propTypes = {
  translations: PropTypes.object.isRequired,
  visible: PropTypes.bool.isRequired,
  selectedTotalGrouping: PropTypes.string,
  _updateReportDetailsTotalGrouping: PropTypes.func.isRequired,
  selectedTotalsOnly: PropTypes.bool,
  _updateReportDetailsTotalsOnly: PropTypes.func.isRequired,
  _updateReportDetailsFundingGrouping: PropTypes.func.isRequired,
  selectedFundingGrouping: PropTypes.string,
  _updateReportDetailsAllowEmptyFundingColumns: PropTypes.func.isRequired,
  _updateReportDetailsSplitByFunding: PropTypes.func.isRequired,
  _updateReportDetailsShowOriginalCurrencies: PropTypes.func.isRequired,
  selectedAllowEmptyFundingColumns: PropTypes.bool,
  selectedSplitByFunding: PropTypes.bool,
  selectedShowOriginalCurrencies: PropTypes.bool,
  description: PropTypes.string,
  _updateReportDetailsDescription: PropTypes.func.isRequired,
};

ReportingDetailSection.defaultProps = {
  selectedTotalGrouping: undefined,
  selectedTotalsOnly: false,
  selectedFundingGrouping: undefined,
  selectedAllowEmptyFundingColumns: false,
  selectedSplitByFunding: false,
  selectedShowOriginalCurrencies: false,
  description: undefined,
};

ReportingDetailSection.contextType = ReportGeneratorContext;
