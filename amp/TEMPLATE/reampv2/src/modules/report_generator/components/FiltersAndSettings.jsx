import React, { Component } from 'react';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import { Segment } from 'semantic-ui-react';
import PropTypes from 'prop-types';
import Filters from './Filters';
import Settings from './Settings';
import { translate } from '../utils/Utils';
import { ReportGeneratorContext } from './StartUp';
import { updateAppliedFilters } from '../actions/stateUIActions';

let filter = null; // This is the widget.

class FiltersAndSettings extends Component {
  constructor() {
    super();
    this.state = { appliedSettingsOpen: false, appliedFiltersOpen: false };
  }

  handleSettingsChange = () => {
    const { appliedSettingsOpen } = this.state;
    this.setState({ appliedSettingsOpen: !appliedSettingsOpen });
  }

  handleFiltersChange = (filter_) => {
    const { appliedFiltersOpen } = this.state;
    filter = filter_;
    this.setState({ appliedFiltersOpen: !appliedFiltersOpen });
  }

  generateAppliedSettings = () => {
    const {
      settings, profile, translations, reportGlobalSettings
    } = this.props;
    if (!reportGlobalSettings || !settings) {
      return null;
    }
    return (
      <div className="applied-filters">
        <div>
          <ul id="previsualization_tree">
            <li>
              <span className="prev_caret prev_caret-down" listener="true">
                {translate('calendar', profile, translations)}
              </span>
              <ul className="prev_nested active">
                <li>
                  {reportGlobalSettings.find(i => i.id === 'calendar-id')
                    .value.options.find(i => i.id === settings['calendar-id']).name}
                </li>
              </ul>
              <span className="prev_caret prev_caret-down" listener="true">
                {translate('currency', profile, translations)}
              </span>
              <ul className="prev_nested active">
                <li>
                  {reportGlobalSettings.find(i => i.id === 'currency-code')
                    .value.options.find(i => i.id === settings['currency-code']).name}
                </li>
              </ul>
              <span className="prev_caret prev_caret-down" listener="true">
                {translate('amountUnits', profile, translations)}
              </span>
              <ul className="prev_nested active">
                <li>
                  {reportGlobalSettings.find(i => i.id === 'number-divider')
                    .value.options.find(i => i.value === `${settings['amount-format']['number-divider']}`).name}
                </li>
              </ul>
              <span className="prev_caret prev_caret-down" listener="true">
                {translate('yearRange', profile, translations)}
              </span>
              <ul className="prev_nested active">
                <li>
                  {settings['year-range'].from}
                  {' '}
                  -
                  {settings['year-range'].to}
                </li>
              </ul>
            </li>
          </ul>
        </div>
      </div>
    );
  }

  generateAppliedFilters = () => {
    const {
      filters, html, _updateAppliedFilters
    } = this.props;
    // If this is a saved report we might need to create the html for the first time.
    if (html === null && filters && filter) {
      const html_ = filter.getAppliedFilters({ returnHTML: true });
      _updateAppliedFilters(filters, html_);
      return <div dangerouslySetInnerHTML={{ __html: html_ }} />;
    } else if (filters) {
      return <div dangerouslySetInnerHTML={{ __html: html }} />;
    }
    return null;
  }

  render() {
    const { loading } = this.props;
    const { appliedSettingsOpen, appliedFiltersOpen } = this.state;
    return (
      <>
        <Segment loading={loading} placeholder textAlign="left" className="filters_segment">
          {!loading ? (
            <>
              <Filters
                onApplyFilters={() => {}}
                appliedSectionOpen={appliedSettingsOpen}
                appliedSectionChange={this.handleFiltersChange} />
              <Settings
                onApplySettings={() => {}}
                appliedSectionOpen={appliedFiltersOpen}
                appliedSectionChange={this.handleSettingsChange} />
              <div className="applied-filters-wrapper">
                <div className={!appliedFiltersOpen ? 'invisible-applied-filters' : 'applied-filters'}>
                  {this.generateAppliedFilters()}
                </div>
                <div className={!appliedSettingsOpen ? 'invisible-applied-filters' : 'applied-filters'}>
                  {this.generateAppliedSettings()}
                </div>
              </div>
            </>
          ) : null}
        </Segment>
      </>
    );
  }
}

const mapStateToProps = state => ({
  translations: state.translationsReducer.translations,
  profile: state.uiReducer.profile,
  settings: state.uiReducer.settings,
  reportGlobalSettings: state.settingsReducer.reportGlobalSettings,
  filters: state.uiReducer.filters,
  html: state.uiReducer.appliedFilters,
});

const mapDispatchToProps = dispatch => bindActionCreators({
  _updateAppliedFilters: (data, html) => updateAppliedFilters(data, html),
}, dispatch);

FiltersAndSettings.propTypes = {
  loading: PropTypes.bool,
  settings: PropTypes.object,
  translations: PropTypes.object.isRequired,
  profile: PropTypes.string,
  reportGlobalSettings: PropTypes.object.isRequired,
  _updateAppliedFilters: PropTypes.func.isRequired,
  filters: PropTypes.object,
  html: PropTypes.string,
};

FiltersAndSettings.defaultProps = {
  loading: false,
  settings: undefined,
  profile: undefined,
  filters: null,
  html: null,
};

FiltersAndSettings.contextType = ReportGeneratorContext;

export default connect(mapStateToProps, mapDispatchToProps)(FiltersAndSettings);
