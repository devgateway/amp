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
  getMetadata, fetchReport, updateProfile, updateId, saveNew, save
} from '../actions/stateUIActions';
import { convertTotalGrouping, getProfileFromReport, hasFilters } from '../utils/Utils';
import ErrorMessage from './ErrorMessage';
import { TRN_PREFIX } from '../utils/constants';

class ReportGeneratorHome extends Component {
  constructor() {
    super();
    this.state = { showChildren: false, errors: null };
  }

  componentDidMount() {
    const {
      _getMetadata, _fetchReport, location, _updateProfile, _updateId, translations
    } = this.props;
    // eslint-disable-next-line react/destructuring-assignment,react/prop-types
    const { id } = this.props.match.params;
    const typeFromURL = new URLSearchParams(location.search).get('type');
    const profileFromURL = new URLSearchParams(location.search).get('profile');
    // If this is a saved report then ignore type and profile params from the URL.
    if (id) {
      _updateId(id);
      return _fetchReport(id).then((action) => {
        const profile = getProfileFromReport(action.payload);
        _updateProfile(profile);
        if (action.payload) {
          return _getMetadata(action.payload.type, profile).then(() => this.setState({ showChildren: true }));
        } else {
          // eslint-disable-next-line max-len
          return this.setState({ errors: [<ErrorMessage visible message={translations[`${TRN_PREFIX}errorLoadingReport`]} />] });
        }
      });
    } else {
      _updateProfile(profileFromURL);
      _getMetadata(typeFromURL, profileFromURL);
      // eslint-disable-next-line react/no-did-mount-set-state
      this.setState({ showChildren: true });
    }
  }

  commonReport = (isNew) => {
    const { uiReducer } = this.props;
    const body = {
      id: isNew ? null : uiReducer.id,
      name: uiReducer.reportDetails.name,
      description: uiReducer.reportDetails.description,
      type: uiReducer.type,
      groupingOption: convertTotalGrouping(uiReducer.reportDetails.selectedTotalGrouping),
      summary: uiReducer.reportDetails.selectedSummaryReport,
      tab: uiReducer.reportDetails.isTab,
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
    }
    return body;
  }

  saveReport = () => {
    const { _save, uiReducer } = this.props;
    const body = this.commonReport(false);
    return _save(uiReducer.id, body).then(response => this.processAfterSave(response));
  }

  saveNewReport = () => {
    const { _saveNew } = this.props;
    const body = this.commonReport(true);
    return _saveNew(body).then(response => this.processAfterSave(response));
  }

  processAfterSave = (response) => {
    const { translations } = this.props;
    if (response.error) {
      const errors = [];
      Object.keys(response.error).forEach(i => {
        const trnLabel = translations[`${TRN_PREFIX}apiError${i}`];
        errors.push({ id: i, label: trnLabel || response.error[i] });
      });
      if (errors.length > 0) {
        this.setState({ errors: errors.map(i => <ErrorMessage key={i.id} visible message={i.label} />) });
        return null;
      }
    }
    // TODO: Maybe we need to save the url we are coming from.
    window.location.href = '/viewTeamReports.do?tabs=false&reset=true';
    return null;
  }

  render() {
    const { showChildren: canLoadChildren, errors } = this.state;
    return (
      <Container>
        <MainHeader />
        <FiltersAndSettings loading={!canLoadChildren} />
        {errors ? (
          <Segment>
            {errors}
          </Segment>
        ) : null}
        <MainContent saveNewReport={this.saveNewReport} saveReport={this.saveReport} />
      </Container>
    );
  }
}

const mapStateToProps = state => ({
  translations: state.translationsReducer.translations,
  uiReducer: state.uiReducer,
});

const mapDispatchToProps = dispatch => bindActionCreators({
  _getMetadata: (type, profile) => getMetadata(type, profile),
  _fetchReport: (id) => fetchReport(id),
  _updateProfile: (data) => updateProfile(data),
  _updateId: (data) => updateId(data),
  _saveNew: (data) => saveNew(data),
  _save: (id, data) => save(id, data),
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
};

ReportGeneratorHome.defaultProps = {};

ReportGeneratorHome.contextType = ReportGeneratorContext;

export default connect(mapStateToProps, mapDispatchToProps)(ReportGeneratorHome);
