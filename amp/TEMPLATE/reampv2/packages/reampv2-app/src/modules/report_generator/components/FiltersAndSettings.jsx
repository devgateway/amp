import React, { Component } from 'react';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import {
  GridRow, Segment, GridColumn, Grid
} from 'semantic-ui-react';
import PropTypes from 'prop-types';
import Filters from './Filters';
import Settings from './Settings';
import { translate } from '../utils/Utils';
import { ReportGeneratorContext } from './StartUp';
import { updateAppliedFilters } from '../actions/stateUIActions';
import { toggleIcon } from '../utils/appliedFiltersExtenalCode';
// eslint-disable-next-line no-unused-vars
import tree from './tree.css';

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

  handleApplySettings = () => {
    this.forceUpdate();
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
            <ul className="previsualization_tree">
              <li>
              <span className="prev_caret">
                {translate('calendar', profile, translations)}
              </span>
                <ul className="prev_nested">
                  <li>
                    {reportGlobalSettings.find(i => i.id === 'calendar-id')
                        .value.options.find(i => i.id === settings['calendar-id']).name}
                  </li>
                </ul>
              </li>
              <li>
              <span className="prev_caret">
                {translate('currency', profile, translations)}
              </span>
                <ul className="prev_nested">
                  <li>
                    {reportGlobalSettings.find(i => i.id === 'currency-code')
                        .value.options.find(i => i.id === settings['currency-code']).name}
                  </li>
                </ul>
              </li>
              <li>
              <span className="prev_caret">
                {translate('amountUnits', profile, translations)}
              </span>
                <ul className="prev_nested">
                  <li>
                    {reportGlobalSettings.find(i => i.id === 'number-divider')
                        .value.options.find(i => i.value === `${settings['amount-format']['number-divider']}`
                            || i.value === settings['number-divider']
                            || i.id === settings['number-divider'])
                        .name}
                  </li>
                </ul>
              </li>
              <li>
              <span className="prev_caret">
                {translate('yearRange', profile, translations)}
              </span>
                <ul className="prev_nested">
                  <li>
                    {`${translate('from', profile, translations)} ${settings['year-range'].from}`}
                    {' '}
                    {`${translate('until', profile, translations)} ${settings['year-range'].to}`}
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
                <Grid>
                  <GridRow className="filter-title-wrapper">
                    <GridColumn computer={8} mobile={16}>
                      <Filters
                          onApplyFilters={() => {}}
                          appliedSectionOpen={appliedFiltersOpen}
                          appliedSectionChange={this.handleFiltersChange} />
                    </GridColumn>
                    <GridColumn computer={8} mobile={16}>
                      <Settings
                          onApplySettings={this.handleApplySettings}
                          appliedSectionOpen={appliedSettingsOpen}
                          appliedSectionChange={this.handleSettingsChange} />
                    </GridColumn>
                  </GridRow>
                  {appliedFiltersOpen || appliedSettingsOpen ? (
                      <GridRow className="applied-filters-wrapper">
                        <GridColumn computer={8} mobile={16}>
                          <div className={!appliedFiltersOpen ? 'invisible-applied-filters' : 'applied-filters'}>
                            {this.generateAppliedFilters()}
                          </div>
                        </GridColumn>
                        <GridColumn computer={8} mobile={16}>
                          <div className={!appliedSettingsOpen ? 'invisible-applied-filters' : 'applied-filters'}>
                            {this.generateAppliedSettings()}
                          </div>
                        </GridColumn>
                      </GridRow>
                  ) : null}
                </Grid>
            ) : null}
          </Segment>
        </>
    );
  }

  // eslint-disable-next-line react/sort-comp,no-unused-vars
  componentDidUpdate(prevProps, prevState, snapshot) {
    // The js code that animates the applied filters tree has to be re-implemented and re-run.
    toggleIcon();
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
