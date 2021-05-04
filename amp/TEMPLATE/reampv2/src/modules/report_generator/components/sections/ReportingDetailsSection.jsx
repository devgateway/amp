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
      visible, translations, selectedTotalGrouping, selectedTotalsOnly, selectedFundingGrouping,
      selectedAllowEmptyFundingColumns, selectedSplitByFunding, selectedShowOriginalCurrencies,
      description
    } = this.props;

    return (
      <div className={!visible ? 'invisible-tab' : ''}>
        <Grid>
          <GridRow>
            <GridColumn width="8">
              <OptionsList title={translations[`${TRN_PREFIX}totalGrouping`]} isRequired tooltip="tooltip 1" >
                <OptionsContent
                  radioList={this.getOptions(TOTAL_GROUPING_RADIO_OPTIONS)}
                  checkList={this.getOptions(TOTAL_GROUPING_CHECKBOX_OPTIONS)}
                  selectedRadio={selectedTotalGrouping}
                  selectedCheckboxes={[selectedTotalsOnly]}
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
                  radioList={this.getOptions(FUNDING_GROUPING_RADIO_OPTIONS)}
                  changeRadioList={this.selectFundingGrouping}
                  selectedRadio={selectedFundingGrouping}
                />
              </OptionsList>
            </GridColumn>
            <GridColumn width="8">
              <OptionsList title={translations[`${TRN_PREFIX}options`]} tooltip="tooltip 4" >
                <OptionsContent
                  checkList={this.getOptions(OPTIONS_CHECKBOX_OPTIONS)}
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
  options: state.uiReducer.options,
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
  options: PropTypes.object,
};

ReportingDetailSection.defaultProps = {
  selectedTotalGrouping: undefined,
  selectedTotalsOnly: false,
  selectedFundingGrouping: undefined,
  selectedAllowEmptyFundingColumns: false,
  selectedSplitByFunding: false,
  selectedShowOriginalCurrencies: false,
  description: undefined,
  options: undefined,
};

ReportingDetailSection.contextType = ReportGeneratorContext;
