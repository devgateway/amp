import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { bindActionCreators } from 'redux';
import { connect } from 'react-redux';
import { Container, Segment } from 'semantic-ui-react';
import { ReportGeneratorContext } from './StartUp';
import MainHeader from './MainHeader';
import MainContent from './MainContent';
import FiltersAndSettings from './FiltersAndSettings';
import {
  getMetadata,
  fetchReport,
  updateProfile,
  updateId,
  saveNew,
  save,
  runReport,
  updateReportDetailsFundingGrouping,
  setInitialHierarchies,
  markExistingReportSanitized,
  updateColumnsSelected,
  updateMeasuresSelected,
  updateMeasuresSorting,
} from '../actions/stateUIActions';
import {
  convertTotalGrouping, getProfileFromReport, translate, hasFilters, convertReportType, revertReportType
} from '../utils/Utils';
import ErrorMessage from './ErrorMessage';
import {
  PROFILE_TAB, RUN_REPORT_NAME, SETTINGS_YEAR_RANGE
} from '../utils/constants';
import { setColumnsData, setHierarchiesData, setMeasuresData } from '../actions/mementoAction';
import { fetchLanguages } from '../actions/languagesActions';
import {withRouter} from "../../../utils/components/WithRouter";

class ReportGeneratorHome extends Component {
  constructor() {
    super();
    this.state = { showChildren: false, errors: null };
  }

  componentDidMount() {
    const {
      _getMetadata, _fetchReport, location, _updateProfile, _updateId, translations,
      _updateReportDetailsFundingGrouping, _setColumnsData, _setMeasuresData, _setHierarchiesData,
      _setInitialHierarchies, _fetchLanguages, _markExistingReportSanitized, _updateColumnsSelected,
      _updateMeasuresSelected, _updateMeasuresSorting,
    } = this.props;
    _fetchLanguages();
    // eslint-disable-next-line react/destructuring-assignment,react/prop-types
    const { id } = this.props.params;
    const searchParams = this.props.searchParams;
    const typeFromURL = new URLSearchParams(location.search).get('type');
    const profileFromURL = new URLSearchParams(location.search).get('profile');

    // If this is a saved report then ignore type and profile params from the URL.
    if (id) {
      _updateId(id);
      return _fetchReport(id).then((reportMetadata) => {
        const profile = getProfileFromReport(reportMetadata.payload);
        _updateProfile(profile);
        if (reportMetadata.payload) {
          return _getMetadata(reportMetadata.payload.type, profile).then((apiMetaData) => {
            this.setState({ showChildren: true });

            // AMP-30290: We need to remove columns that are not available in the API anymore.
            const sanitizedSelectedColumns = reportMetadata.payload.columns
                .filter(i => apiMetaData.payload.columns.find(j => j.id === i.id));
            _updateColumnsSelected(sanitizedSelectedColumns.map(i => i.id));
            const sanitizedSelectedMeasures = reportMetadata.payload.measures
                .filter(i => apiMetaData.payload.measures.find(j => i.id === j.id));
            _updateMeasuresSelected(sanitizedSelectedMeasures.map(i => i.id));
            _updateMeasuresSorting(sanitizedSelectedMeasures.map(i => i.id));
            const sanitizedHierarchies = reportMetadata.payload.hierarchies
                .filter(i => sanitizedSelectedColumns.find(j => j.id === i.id));

            // Save original columns and measures for "reset".
            _setColumnsData((Object.assign([], sanitizedSelectedColumns)).map(i => i.id));
            _setMeasuresData(([...sanitizedSelectedMeasures]));

            // Load hierarchies into Redux's state.
            const _hierarchies = apiMetaData.payload.columns
                .filter(i => sanitizedSelectedColumns.find(j => j.id === i.id))
                .filter(i => i.hierarchy);
            const _hierarchiesOrder = [];
            sanitizedHierarchies.forEach(i => {
              _hierarchiesOrder.push(i.id);
            });
            _hierarchies.forEach(i => {
              if (!_hierarchiesOrder.find(j => j === i.id)) {
                _hierarchiesOrder.push(i.id);
              }
            });
            const selected = sanitizedHierarchies.map(i => i.id);
            _setInitialHierarchies(_hierarchies, selected, _hierarchiesOrder);

            // Save original hierarchies for "reset".
            _setHierarchiesData({
              available: [..._hierarchies],
              selected: [...selected],
              order: [..._hierarchiesOrder]
            });
            _markExistingReportSanitized();
            return _updateReportDetailsFundingGrouping(revertReportType(reportMetadata.payload.type));
          });
        } else {
          // eslint-disable-next-line max-len
          return this.setState({ errors: [<ErrorMessage visible message={translate('errorLoadingReport', profile, translations)} />] });
        }
      });
    } else {
      _markExistingReportSanitized();
      _updateProfile(profileFromURL);
      _getMetadata(typeFromURL, profileFromURL);
      _updateReportDetailsFundingGrouping(revertReportType(typeFromURL));
      // eslint-disable-next-line react/no-did-mount-set-state
      this.setState({ showChildren: true });
    }
  }

  commonReport = (isNew, isDynamic) => {
    const { uiReducer } = this.props;
    const body = {
      id: isNew ? null : uiReducer.id,
      name: !isDynamic ? uiReducer.reportDetails.name : RUN_REPORT_NAME,
      description: uiReducer.reportDetails.description,
      type: convertReportType(uiReducer.reportDetails.selectedFundingGrouping),
      groupingOption: convertTotalGrouping(uiReducer.reportDetails.selectedTotalGrouping),
      summary: uiReducer.reportDetails.selectedSummaryReport,
      tab: (uiReducer.profile === PROFILE_TAB),
      publicView: uiReducer.reportDetails.publicView,
      workspaceLinked: uiReducer.reportDetails.workspaceLinked,
      alsoShowPledges: uiReducer.reportDetails.selectedAlsoShowPledges,
      showOriginalCurrency: uiReducer.reportDetails.selectedShowOriginalCurrencies,
      allowEmptyFundingColumns: uiReducer.reportDetails.selectedAllowEmptyFundingColumns,
      splitByFunding: uiReducer.reportDetails.selectedSplitByFunding,
      reportCategory: uiReducer.reportDetails.selectedReportCategory,
      ownerId: uiReducer.reportDetails.ownerId,
      includeLocationChildren: uiReducer.reportDetails.includeLocationChildren,
      columns: uiReducer.columns.selected,
      hierarchies: uiReducer.hierarchies.order.filter(i => uiReducer.hierarchies.selected.find(j => j === i)),
      measures: uiReducer.measures.order.filter(i => uiReducer.measures.selected.find(j => j === i)),
      settings: uiReducer.settings,
    };
    // Cleanup filters.
    if (hasFilters(uiReducer.filters)) {
      body.filters = uiReducer.filters;
      Object.keys(uiReducer.filters).forEach(key => {
        if (uiReducer.filters[key] === null) {
          delete uiReducer.filters[key];
        }
      });
    } else {
      delete body.includeLocationChildren;
    }

    // Cleanup settings.
    if (body.settings && body.settings[SETTINGS_YEAR_RANGE]) {
      Object.keys(body.settings[SETTINGS_YEAR_RANGE]).forEach(i => {
        body.settings[SETTINGS_YEAR_RANGE][i] = `${body.settings[SETTINGS_YEAR_RANGE][i]}`;
      });
    }

    return body;
  }

  saveReport = (open) => {
    const { _save, uiReducer } = this.props;
    const body = this.commonReport(false, false);
    return _save(uiReducer.id, body).then(response => this.processAfterSave(response, open));
  }

  saveNewReport = (open) => {
    const { _saveNew } = this.props;
    const body = this.commonReport(true, false);
    return _saveNew(body).then(response => this.processAfterSave(response, open));
  }

  runReport = () => {
    const { _runReport } = this.props;
    const body = this.commonReport(true, true);
    return _runReport(body).then(response => this.processAfterSave(response, true));
  }

  processAfterSave = (response, open) => {
    const { translations, uiReducer, profile } = this.props;
    if (response.error) {
      const errors = [];
      Object.keys(response.error).forEach(i => {
        const trnLabel = translate(`apiError${i}`, profile, translations);
        errors.push({ id: i, label: trnLabel || response.error[i] });
      });
      if (errors.length > 0) {
        this.setState({ errors: errors.map(i => <ErrorMessage key={i.id} visible message={i.label} />) });
      }
      return null;
    }
    if (response.payload.id < 0) {
      window.open(`/TEMPLATE/ampTemplate/saikuui_reports/index_reports.html#report/run/${response.payload.id}`);
    } else if (uiReducer.profile === PROFILE_TAB) {
      window.location.href = '/viewTeamReports.do?tabs=true&reset=true';
    } else {
      if (open) {
        window.open(`/TEMPLATE/ampTemplate/saikuui_reports/index_reports.html#report/open/${response.payload.id}`);
      }
      window.location.href = '/viewTeamReports.do?tabs=false&reset=true';
    }
    return null;
  }

  render() {
    const { showChildren: canLoadChildren, errors } = this.state;
    const { layoutLoaded, results, profile } = this.props;

    // Only logged users can use the tab generator.
    if (layoutLoaded && results.logged !== true && profile === PROFILE_TAB) {
      window.location.href = '/';
    }

    return (
        <Container>
          <MainHeader />
          <FiltersAndSettings loading={!canLoadChildren} />
          {errors ? (
              <Segment className="errors_segment">
                {errors}
              </Segment>
          ) : null}
          <MainContent saveNewReport={this.saveNewReport} saveReport={this.saveReport} runReport={this.runReport} />
        </Container>
    );
  }
}

const mapStateToProps = state => ({
  translations: state.translationsReducer.translations,
  uiReducer: state.uiReducer,
  profile: state.uiReducer.profile,
  layoutLoaded: state.layoutReducer.loaded,
  results: state.layoutReducer.results,
});

const mapDispatchToProps = dispatch => bindActionCreators({
  _getMetadata: (type, profile) => getMetadata(type, profile),
  _fetchReport: (id) => fetchReport(id),
  _updateProfile: (data) => updateProfile(data),
  _updateId: (data) => updateId(data),
  _saveNew: (data) => saveNew(data),
  _save: (id, data) => save(id, data),
  _runReport: (data) => runReport(data),
  _updateReportDetailsFundingGrouping: (data) => updateReportDetailsFundingGrouping(data),
  _setColumnsData: (data) => setColumnsData(data),
  _setHierarchiesData: (data) => setHierarchiesData(data),
  _setMeasuresData: (data) => setMeasuresData(data),
  _setInitialHierarchies: (available, selected, order) => setInitialHierarchies(available, selected, order),
  _fetchLanguages: () => fetchLanguages(),
  _markExistingReportSanitized: () => markExistingReportSanitized(),
  _updateColumnsSelected: (data) => updateColumnsSelected(data),
  _updateMeasuresSelected: (data) => updateMeasuresSelected(data),
  _updateMeasuresSorting: (data) => updateMeasuresSorting(data),
}, dispatch);

ReportGeneratorHome.propTypes = {
  _getMetadata: PropTypes.func.isRequired,
  _fetchReport: PropTypes.func.isRequired,
  _updateProfile: PropTypes.func.isRequired,
  _updateId: PropTypes.func.isRequired,
  location: PropTypes.object.isRequired,
  translations: PropTypes.object.isRequired,
  _saveNew: PropTypes.func.isRequired,
  _save: PropTypes.func.isRequired,
  uiReducer: PropTypes.object.isRequired,
  _runReport: PropTypes.func.isRequired,
  _updateReportDetailsFundingGrouping: PropTypes.func.isRequired,
  profile: PropTypes.string,
  layoutLoaded: PropTypes.bool,
  results: PropTypes.object,
  _setColumnsData: PropTypes.func.isRequired,
  _setMeasuresData: PropTypes.func.isRequired,
  _setHierarchiesData: PropTypes.func.isRequired,
  _setInitialHierarchies: PropTypes.func.isRequired,
  _fetchLanguages: PropTypes.func.isRequired,
  _markExistingReportSanitized: PropTypes.func.isRequired,
  _updateColumnsSelected: PropTypes.func.isRequired,
  _updateMeasuresSelected: PropTypes.func.isRequired,
  _updateMeasuresSorting: PropTypes.func.isRequired,
  searchParams: PropTypes.object.isRequired,
};

ReportGeneratorHome.defaultProps = {
  profile: undefined,
  layoutLoaded: false,
  results: undefined,
};

ReportGeneratorHome.contextType = ReportGeneratorContext;
export default connect(mapStateToProps, mapDispatchToProps)(withRouter(ReportGeneratorHome));
