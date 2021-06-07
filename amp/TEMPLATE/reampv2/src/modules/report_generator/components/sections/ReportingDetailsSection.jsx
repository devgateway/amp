import React, { Component } from 'react';
import PropTypes from 'prop-types';
import {
  Form,
  Grid, GridColumn, TextArea
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
  updateReportDetailsDescription,
  updateReportDetailsAlsoShowPledges, updateReportDetailsUseAboveFilters
} from '../../actions/stateUIActions';
import { hasFilters } from '../../utils/Utils';

class ReportingDetailSection extends Component {
  // eslint-disable-next-line no-unused-vars
  componentDidUpdate(prevProps, prevState, snapshot) {
    const { selectedTotalGrouping, selectedFundingGrouping } = this.props;
    // In the old report generator some options are pre-filled to a default value.
    let options = this.getOptions(TOTAL_GROUPING_RADIO_OPTIONS);
    if (selectedTotalGrouping === null && options.length > 0) {
      const value = options[0] ? options[0].name : null;
      if (value) {
        this.selectTotalGrouping(null, { value });
      }
    }
    options = this.getOptions(FUNDING_GROUPING_RADIO_OPTIONS);
    if (selectedFundingGrouping === null && options.length > 0) {
      const value = options[0] ? options[0].name : null;
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

  selectAlsoShowPledges = () => {
    const { _updateReportDetailsAlsoShowPledges, selectedAlsoShowPledges } = this.props;
    _updateReportDetailsAlsoShowPledges(!selectedAlsoShowPledges);
  }

  selectUseAboveFilters = () => {
    const { _updateReportDetailsUseAboveFilters, selectedUseAboveFilters, filters } = this.props;
    if (hasFilters(filters)) {
      _updateReportDetailsUseAboveFilters(!selectedUseAboveFilters);
    } else {
      _updateReportDetailsUseAboveFilters(false);
    }
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
        const option = options.find(j => j.name === i);
        if (option && option.visible) {
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
      description, loading, selectedAlsoShowPledges, selectedUseAboveFilters
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
            <OptionsList title={translations[`${TRN_PREFIX}reportDescription`]} tooltip="tooltip 2" >
              <Form className="description">
                <TextArea value={description} onChange={this.changeDescription} />
              </Form>
            </OptionsList>
          </GridColumn>
          {this.getOptions(FUNDING_GROUPING_RADIO_OPTIONS).length !== 1 ? (
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
          ) : null}
          <GridColumn computer="8" tablet="16">
            <OptionsList title={translations[`${TRN_PREFIX}options`]} tooltip="tooltip 4" >
              <OptionsContent
                checkList={this.getOptions(OPTIONS_CHECKBOX_OPTIONS)}
                selectedCheckboxes={[selectedAllowEmptyFundingColumns, selectedSplitByFunding,
                  selectedShowOriginalCurrencies, selectedAlsoShowPledges, selectedUseAboveFilters]}
                changeCheckList={[this.selectAllowEmptyFundingColumns, this.selectSplitByFunding,
                  this.selectShowOriginalCurrencies, this.selectAlsoShowPledges, this.selectUseAboveFilters]}
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
  selectedAlsoShowPledges: state.uiReducer.reportDetails.selectedAlsoShowPledges,
  selectedUseAboveFilters: state.uiReducer.reportDetails.selectedUseAboveFilters,
  description: state.uiReducer.reportDetails.description,
  options: state.uiReducer.options,
  loading: state.uiReducer.metaDataPending,
  filters: state.uiReducer.filters,
});

const mapDispatchToProps = dispatch => bindActionCreators({
  _updateReportDetailsTotalGrouping: (data) => dispatch(updateReportDetailsTotalGrouping(data)),
  _updateReportDetailsTotalsOnly: (data) => dispatch(updateReportDetailsTotalsOnly(data)),
  _updateReportDetailsFundingGrouping: (data) => dispatch(updateReportDetailsFundingGrouping(data)),
  _updateReportDetailsAllowEmptyFundingColumns: (data) => dispatch(updateReportDetailsAllowEmptyFundingColumns(data)),
  _updateReportDetailsSplitByFunding: (data) => dispatch(updateReportDetailsSplitByFunding(data)),
  _updateReportDetailsShowOriginalCurrencies: (data) => dispatch(updateReportDetailsShowOriginalCurrencies(data)),
  _updateReportDetailsDescription: (data) => dispatch(updateReportDetailsDescription(data)),
  _updateReportDetailsAlsoShowPledges: (data) => dispatch(updateReportDetailsAlsoShowPledges(data)),
  _updateReportDetailsUseAboveFilters: (data) => dispatch(updateReportDetailsUseAboveFilters(data)),
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
  selectedAlsoShowPledges: PropTypes.bool,
  selectedUseAboveFilters: PropTypes.bool,
  description: PropTypes.string,
  _updateReportDetailsDescription: PropTypes.func.isRequired,
  options: PropTypes.array,
  loading: PropTypes.bool,
  _updateReportDetailsAlsoShowPledges: PropTypes.func.isRequired,
  _updateReportDetailsUseAboveFilters: PropTypes.func.isRequired,
  filters: PropTypes.object,
};

ReportingDetailSection.defaultProps = {
  selectedTotalGrouping: undefined,
  selectedSummaryReport: false,
  selectedFundingGrouping: undefined,
  selectedAllowEmptyFundingColumns: false,
  selectedSplitByFunding: false,
  selectedShowOriginalCurrencies: false,
  selectedAlsoShowPledges: false,
  selectedUseAboveFilters: false,
  description: undefined,
  options: undefined,
  loading: false,
  filters: null,
};

ReportingDetailSection.contextType = ReportGeneratorContext;
