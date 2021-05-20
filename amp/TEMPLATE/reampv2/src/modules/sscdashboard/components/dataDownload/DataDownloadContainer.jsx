import React, { Component } from 'react';
import { Tab, Tabs } from 'react-bootstrap';
import { bindActionCreators } from 'redux';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import { SSCTranslationContext } from '../StartUp';
import './dataDownloadContainer.css';
import MultiSelectionDropDown from '../layout/filters/MultiSelectionDropDown';
import { generateYearsFilters } from '../../utils/Utils';
import { downloadData, dataDownloaded } from '../../actions/downloadData';
import { exportToXLS, populateFilters } from '../../utils/exportUtils';

class DataDownloadContainer extends Component {
  constructor(props) {
    super(props);
    this.state = {
      key: 'Country',
      selectedFilters: {
        selectedYears: [],
        selectedCountries: [],
        selectedSectors: [],
        selectedModalities: []
      }
    };
  }

  componentDidMount() {
    this.resetFilters();
  }

  componentDidUpdate() {
    const {
      dataDownloadLoaded, dataDownload, _dataDownloaded, filters
    } = this.props;
    if (dataDownloadLoaded) {
      const { sscDashboardXlsResultRows } = dataDownload;
      const { selectedFilters } = this.state;
      const { translations } = this.context;
      const sscXlsData = {};
      sscXlsData.columns = [];
      sscXlsData.source = translations['amp.ssc.dashboard:page-title'];
      sscXlsData.title = translations['amp.ssc.dashboard:download-title'];
      sscXlsData.filters = [];
      sscXlsData.columns.push({
        headerTitle: translations['amp.ssc.dashboard:sectors-project'],
        key: 'activity',
        width: 100
      });
      sscXlsData.columns.push({
        headerTitle: translations['amp.ssc.dashboard:Sector'],
        key: 'sector',
        width: 50
      });
      sscXlsData.columns.push({
        headerTitle: translations['amp.ssc.dashboard:Modality'],
        key: 'modality',
        width: 50
      });
      sscXlsData.columns.push({
        headerTitle: translations['amp.ssc.dashboard:Country'],
        key: 'country',
        width: 50
      });
      sscXlsData.columns.push({
        headerTitle: translations['amp.ssc.dashboard:Year'],
        key: 'year',
        width: 30
      });
      sscXlsData.rows = sscDashboardXlsResultRows;
      sscXlsData.filters = populateFilters(translations, filters, selectedFilters);
      exportToXLS(sscXlsData);
      _dataDownloaded();
    }
  }

  handleSelectedCountryDataDownloadChanged(pSelectedCountries) {
    this.updateFilterState('selectedCountries', pSelectedCountries);
  }

  handleSelectedSectorDataDownloadChanged(pSelectedSectors) {
    this.updateFilterState('selectedSectors', pSelectedSectors);
  }

  handleSelectedModalityDataDownloadChanged(pSelectedModalities) {
    this.updateFilterState('selectedModalities', pSelectedModalities);
  }

  handleSelectedYearDataDownloadChanged(pSelectedYears) {
    this.updateFilterState('selectedYears', pSelectedYears);
  }

  setKey(key) {
    this.setState({ key });
  }

  resetFilters() {
    const { selectedFilters } = this.props;
    if (selectedFilters) {
      // eslint-disable-next-line react/no-did-mount-set-state
      this.setState({ selectedFilters });
    }
  }

  updateFilterState(filterSelector, updatedSelectedFilters) {
    this.setState((currentState) => {
      const selectedFilters = { ...currentState.selectedFilters };
      selectedFilters[filterSelector] = updatedSelectedFilters;
      return { selectedFilters };
    });
  }

  render() {
    const {
      filters, filtersRestrictions, settings, toggleDataDownload, _downloadData, error
    } = this.props;
    const { translations } = this.context;
    const { key, selectedFilters } = this.state;
    const years = [];
    const {
      selectedYears = [], selectedCountries = [], selectedSectors = [], selectedModalities = []
    } = selectedFilters;
    const { sectors } = filters.sectors;
    let { countries } = filters.countries;
    const { modalities } = filters.modalities;
    if (countries.length > 0 && filtersRestrictions.countriesWithData.length > 0) {
      countries = countries.filter(c => filtersRestrictions.countriesWithData.includes(c.id));
    }
    generateYearsFilters(years, settings);
    return (
      <div className="data-download-wrapper">

        <div className="export-wrapper">
          <ul>
            <li className="return-link" onClick={() => toggleDataDownload()}>X</li>
          </ul>
        </div>
        <div className="data-download">
          <Tabs
            id="filters-tab"
            activeKey={key}
            transition={false}
            onSelect={(k) => this.setKey(k)}
          >
            <Tab
              eventKey="Country"
              title={`${translations['amp.ssc.dashboard:Country']} (${selectedCountries.length}/${countries.length})`}>
              <div className="download-content">
                <MultiSelectionDropDown
                  options={countries}
                  filterName="amp.ssc.dashboard:Country-download"
                  filterId="ddCountryDownload"
                  selectedOptions={selectedCountries}
                  label="amp.ssc.dashboard:Sector"
                  onChange={this.handleSelectedCountryDataDownloadChanged.bind(this)}
                />
              </div>
            </Tab>
            <Tab
              eventKey="sector"
              title={`${translations['amp.ssc.dashboard:Sector']} (${selectedSectors.length}/${sectors.length})`}>
              <div className="download-content">
                <MultiSelectionDropDown
                  options={sectors}
                  filterName="amp.ssc.dashboard:Sector-download"
                  filterId="ddSectorDownload"
                  selectedOptions={selectedSectors}
                  label="amp.ssc.dashboard:Sector"
                  onChange={this.handleSelectedSectorDataDownloadChanged.bind(this)}
                />
              </div>
            </Tab>
            <Tab
              eventKey="modality"
              title={`${translations['amp.ssc.dashboard:Modality']} (${selectedModalities.length}/${modalities.length})`}>
              <div className="download-content">
                <MultiSelectionDropDown
                  key="2222"
                  options={modalities}
                  filterName="amp.ssc.dashboard:Modality-download"
                  filterId="ddModalityDownload"
                  selectedOptions={selectedModalities}
                  label="amp.ssc.dashboard:Modality"
                  onChange={this.handleSelectedModalityDataDownloadChanged.bind(this)}
                />
              </div>
            </Tab>
            <Tab
              eventKey="year"
              title={`${translations['amp.ssc.dashboard:Year']} (${selectedYears.length}/${years.length})`}>
              <div className="download-content">
                <MultiSelectionDropDown
                  options={years}
                  filterName="amp.ssc.dashboard:Year-download"
                  filterId="ddYearDownload"
                  selectedOptions={selectedYears}
                  label="amp.ssc.dashboard:Year"
                  onChange={this.handleSelectedYearDataDownloadChanged.bind(this)}
                />
              </div>
            </Tab>
          </Tabs>
          {error && <div>{error}</div>}
          <div className="row">
            <div className="col-md-6 reset-link data-link">
              <span
                onClick={() => this.resetFilters()}>
                {translations['amp.ssc.dashboard:reset']}
              </span>
            </div>
            <div className="col-md-6 download-link data-link">
              {' '}
              <span
                onClick={() => _downloadData(selectedFilters)}>
                {translations['amp.ssc.dashboard:Download']}
              </span>
            </div>
          </div>
        </div>
      </div>
    );
  }
}

const mapStateToProps = state => ({
  dataDownloadPending: state.dataDownloadReducer.dataDownloadPending,
  dataDownloadLoaded: state.dataDownloadReducer.dataDownloadLoaded,
  dataDownload: state.dataDownloadReducer.dataDownload,
  error: state.dataDownloadReducer.error,
});

const mapDispatchToProps = dispatch => bindActionCreators({
  _downloadData: downloadData,
  _dataDownloaded: dataDownloaded,
}, dispatch);

export default connect(mapStateToProps, mapDispatchToProps)(DataDownloadContainer);

DataDownloadContainer.contextType = SSCTranslationContext;
DataDownloadContainer.propTypes = {
  filters: PropTypes.shape({
    sectors: PropTypes.shape({
      sectors: PropTypes.array
    }),
    countries: PropTypes.shape({ countries: PropTypes.array }),
    modalities:
      PropTypes.shape({ modalities: PropTypes.array }),
  })
    .isRequired,
  settings: PropTypes.object.isRequired,
  filtersRestrictions:
  PropTypes.object.isRequired,
  selectedFilters:
  PropTypes.object.isRequired,
  toggleDataDownload: PropTypes.func.isRequired,
  _downloadData: PropTypes.func.isRequired,
  _dataDownloaded: PropTypes.func.isRequired,
  error: PropTypes.string,
  dataDownloadLoaded: PropTypes.bool,
  dataDownload: PropTypes.array
};
DataDownloadContainer.defaultProps = {
  error: null,
  dataDownload: null,
  dataDownloadLoaded: false
};
