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
  setInitialHierarchies
} from '../actions/stateUIActions';
import {
  convertTotalGrouping, getProfileFromReport, translate, hasFilters, convertReportType, revertReportType
} from '../utils/Utils';
import ErrorMessage from './ErrorMessage';
import {
  PROFILE_TAB, RUN_REPORT_NAME, SETTINGS_YEAR_RANGE, TYPE_PLEDGE
} from '../utils/constants';
import { setColumnsData, setHierarchiesData, setMeasuresData } from '../actions/mementoAction';
import { fetchLanguages } from '../actions/languagesActions';

class ReportGeneratorHome extends Component {
  constructor() {
    super();
    this.state = { showChildren: false, errors: null };
  }

  componentDidMount() {
    const {
      _getMetadata, _fetchReport, location, _updateProfile, _updateId, translations,
      _updateReportDetailsFundingGrouping, _setColumnsData, _setMeasuresData, _setHierarchiesData,
      _setInitialHierarchies, _fetchLanguages
    } = this.props;
    _fetchLanguages();
    // eslint-disable-next-line react/destructuring-assignment,react/prop-types
    const { id } = this.props.match.params;
    const typeFromURL = new URLSearchParams(location.search).get('type');
    const profileFromURL = new URLSearchParams(location.search).get('profile');

    // TODO: remove this parameter (and section) once we finish testing.
    const showOldReportGenerator = new URLSearchParams(location.search).get('showOldReportGenerator');
    if (showOldReportGenerator === 'true') {
      if (id) {
        if (typeFromURL === TYPE_PLEDGE) {
          window.open(`/reportWizard.do?editReportId=${id}&type=5`);
        } else {
          window.open(`/reportWizard.do?editReportId=${id}&type=1`);
        }
      } else {
        // eslint-disable-next-line no-lonely-if
        if (typeFromURL === TYPE_PLEDGE) {
          window.open('/reportWizard.do?tabs=false&reset=true&type=5');
        } else {
          window.open('/reportWizard.do?tabs=false&reset=true&type=1');
        }
      }
    }

    // If this is a saved report then ignore type and profile params from the URL.
    if (id) {
      _updateId(id);
      return _fetchReport(id).then((action) => {
        const profile = getProfileFromReport(action.payload);
        _updateProfile(profile);
        if (action.payload) {
          return _getMetadata(action.payload.type, profile).then((data) => {
            this.setState({ showChildren: true });

            _setColumnsData((Object.assign([], action.payload.columns)).map(i => i.id));
            _setMeasuresData(action.payload.measures.map(i => i.id));
            _setHierarchiesData(action.payload.hierarchies.map(i => i.id));

            // Load hierarchies into Redux's state.
            const _hierarchies = data.payload.columns
              .filter(i => action.payload.columns.find(j => j.id === i.id))
              .filter(i => i.hierarchy);
            let _hierarchiesOrder = action.payload.hierarchies;
            if (_hierarchiesOrder.length !== _hierarchies.length) {
              _hierarchiesOrder = [];
              action.payload.hierarchies.forEach(i => {
                _hierarchiesOrder.push(i.id);
              });
              _hierarchies.forEach(i => {
                if (!_hierarchiesOrder.find(j => j === i.id)) {
                  _hierarchiesOrder.push(i.id);
                }
              });
            }
            const selected = action.payload.hierarchies.map(i => i.id);
            _setInitialHierarchies(_hierarchies, selected, _hierarchiesOrder);
            return _updateReportDetailsFundingGrouping(revertReportType(action.payload.type));
          });
        } else {
          // eslint-disable-next-line max-len
          return this.setState({ errors: [<ErrorMessage visible message={translate('errorLoadingReport', profile, translations)} />] });
        }
      });
    } else {
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
};

ReportGeneratorHome.defaultProps = {
  profile: undefined,
  layoutLoaded: false,
  results: undefined,
};

ReportGeneratorHome.contextType = ReportGeneratorContext;

export default connect(mapStateToProps, mapDispatchToProps)(ReportGeneratorHome);
