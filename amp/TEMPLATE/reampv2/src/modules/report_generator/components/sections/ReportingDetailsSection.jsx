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
import {
  FUNDING_GROUPING_RADIO_OPTIONS, OPTIONS_CHECKBOX_OPTIONS,
  TOTAL_GROUPING_CHECKBOX_OPTIONS,
  TOTAL_GROUPING_RADIO_OPTIONS,
  TRN_PREFIX
} from '../../utils/constants';
import OptionsContent from './OptionsContent';
import {
  updateReportDetailsTotalGrouping, updateReportDetailsTotalsOnly,
  updateReportDetailsFundingGrouping, updateReportDetailsAllowEmptyFundingColumns,
  updateReportDetailsSplitByFunding,
  updateReportDetailsShowOriginalCurrencies,
  updateReportDetailsDescription
} from '../../actions/stateUIActions';

class ReportingDetailSection extends Component {
  // eslint-disable-next-line no-unused-vars
  componentDidUpdate(prevProps, prevState, snapshot) {
    const { selectedTotalGrouping, selectedFundingGrouping } = this.props;
    // In the old report generator some options are pre-filled to a default value.
    let options = this.getOptions(TOTAL_GROUPING_RADIO_OPTIONS);
    if (selectedTotalGrouping === null && options.length > 0) {
      const value = options[0] ? options[0].label : null;
      if (value) {
        this.selectTotalGrouping(null, { value });
      }
    }
    options = this.getOptions(FUNDING_GROUPING_RADIO_OPTIONS);
    if (selectedFundingGrouping === null && options.length > 0) {
      const value = options[0] ? options[0].label : null;
      if (value) {
        this.selectFundingGrouping(null, { value });
      }
    }
  }

  selectTotalGrouping = (e, { value }) => {
    const { _updateReportDetailsTotalGrouping } = this.props;
    _updateReportDetailsTotalGrouping(value);
  };

  selectSummaryReport = () => {
    const { _updateReportDetailsTotalsOnly, selectedSummaryReport } = this.props;
    _updateReportDetailsTotalsOnly(!selectedSummaryReport);
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

  getOptions = (group) => {
    const { options } = this.props;
    const radios = [];
    if (options) {
      group.forEach(i => {
        if (options.find(j => j.name === i).visible) {
          radios.push({
            name: i,
            label: options.find(j => j.name === i).label
          });
        }
      });
    }
    return radios;
  }

  render() {
    const {
      visible, translations, selectedTotalGrouping, selectedSummaryReport, selectedFundingGrouping,
      selectedAllowEmptyFundingColumns, selectedSplitByFunding, selectedShowOriginalCurrencies,
      description, loading
    } = this.props;

    return (
      <div className={!visible ? 'invisible-tab' : ''}>
        <Grid>
          <GridColumn computer="8" tablet="16">
            <OptionsList title={translations[`${TRN_PREFIX}totalGrouping`]} isRequired tooltip="tooltip 1" >
              <OptionsContent
                radioList={this.getOptions(TOTAL_GROUPING_RADIO_OPTIONS)}
                checkList={this.getOptions(TOTAL_GROUPING_CHECKBOX_OPTIONS)}
                selectedRadio={selectedTotalGrouping}
                selectedCheckboxes={[selectedSummaryReport]}
                changeCheckList={[this.selectSummaryReport]}
                changeRadioList={this.selectTotalGrouping}
                loading={loading} />
            </OptionsList>
          </GridColumn>
          <GridColumn computer="8" tablet="16">
            <OptionsList title={translations[`${TRN_PREFIX}reportDescription`]} isRequired tooltip="tooltip 2" >
              <Form>
                <TextArea value={description} onChange={this.changeDescription} />
              </Form>
            </OptionsList>
          </GridColumn>
          <GridColumn computer="8" tablet="16">
            <OptionsList title={translations[`${TRN_PREFIX}fundingGroup`]} tooltip="tooltip 3" isRequired >
              <OptionsContent
                radioList={this.getOptions(FUNDING_GROUPING_RADIO_OPTIONS)}
                changeRadioList={this.selectFundingGrouping}
                selectedRadio={selectedFundingGrouping}
                loading={loading}
                />
            </OptionsList>
          </GridColumn>
          <GridColumn computer="8" tablet="16">
            <OptionsList title={translations[`${TRN_PREFIX}options`]} tooltip="tooltip 4" >
              <OptionsContent
                checkList={this.getOptions(OPTIONS_CHECKBOX_OPTIONS)}
                selectedCheckboxes={[selectedAllowEmptyFundingColumns, selectedSplitByFunding,
                  selectedShowOriginalCurrencies]}
                changeCheckList={[this.selectAllowEmptyFundingColumns, this.selectSplitByFunding,
                  this.selectShowOriginalCurrencies]}
                loading={loading} />
            </OptionsList>
          </GridColumn>
        </Grid>
      </div>
    );
  }
}

const mapStateToProps = (state) => ({
  translations: state.translationsReducer.translations,
  selectedTotalGrouping: state.uiReducer.reportDetails.selectedTotalGrouping,
  selectedSummaryReport: state.uiReducer.reportDetails.selectedSummaryReport,
  reportDetails: state.uiReducer.reportDetails,
  selectedFundingGrouping: state.uiReducer.reportDetails.selectedFundingGrouping,
  selectedAllowEmptyFundingColumns: state.uiReducer.reportDetails.selectedAllowEmptyFundingColumns,
  selectedSplitByFunding: state.uiReducer.reportDetails.selectedSplitByFunding,
  selectedShowOriginalCurrencies: state.uiReducer.reportDetails.selectedShowOriginalCurrencies,
  description: state.uiReducer.reportDetails.description,
  options: state.uiReducer.options,
  loading: state.uiReducer.metaDataPending,
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
  selectedSummaryReport: PropTypes.bool,
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
  options: PropTypes.object,
  loading: PropTypes.bool,
};

ReportingDetailSection.defaultProps = {
  selectedTotalGrouping: undefined,
  selectedSummaryReport: false,
  selectedFundingGrouping: undefined,
  selectedAllowEmptyFundingColumns: false,
  selectedSplitByFunding: false,
  selectedShowOriginalCurrencies: false,
  description: undefined,
  options: undefined,
  loading: false,
};

ReportingDetailSection.contextType = ReportGeneratorContext;
