import React, { Component } from 'react';
import { bindActionCreators } from 'redux';
import { connect } from 'react-redux';
import PropTypes from 'prop-types';
import md5 from 'md5';
import {
  Grid, GridColumn, GridRow
} from 'semantic-ui-react';
import MainMenu from './MainMenu';
import ColumnsSection from './sections/ColumnsSection';
import ReportingDetailSection from './sections/ReportingDetailsSection';
import MeasuresSection from './sections/MeasuresSection';
import NavigationButtons from './sections/NavigationButtons';
import PreviewSection from './sections/PreviewSection';
import { getPreview, updatePreviewId } from '../actions/previewActions';
import { ReportGeneratorContext } from './StartUp';

import {
  convertReportType, convertTotalGrouping, javaHashCode, areEnoughDataForPreview
} from '../utils/Utils';

class MainContent extends Component {
  constructor() {
    super();
    this.state = { visibleTab: 0 };
  }

  // eslint-disable-next-line no-unused-vars
  componentDidUpdate(prevProps, prevState, snapshot) {
    const {
      _updatePreviewId, _getPreview, columns, measures, hierarchies, reportDetails
    } = this.props;
    if (areEnoughDataForPreview(columns, measures, hierarchies, reportDetails)) {
      // Convert input data to a String then Number.
      const _reportDetails = { ...reportDetails };
      // Remove fields that would make the preview flicker.
      delete _reportDetails.description;
      delete _reportDetails.name;
      delete _reportDetails.selectedReportCategory;
      const merged = {
        ..._reportDetails,
        columns: columns.selected,
        measures: {
          selected: measures.selected,
          order: measures.order
        },
        hierarchies: {
          selected: hierarchies.selected,
          order: hierarchies.order
        }
      };
      const name = md5(JSON.stringify(merged));
      const newId = javaHashCode(name);
      // Avoid calling again the preview EP if not needed.
      if (newId !== prevProps.lastReportId) {
        _updatePreviewId(newId, name);
        const _columns = columns.selected.map(i => columns.available.find(j => j.id === i).name);
        const _measures = measures.order.map(i => measures.available.find(j => j.id === i).name);
        const _hierarchies = hierarchies.order.filter(i => hierarchies.selected.includes(i))
          .map(i => hierarchies.available.find(j => j.id === i).name);
        const year = (new Date()).getFullYear();
        const dateFilter = {
          date: {
            start: `${year - 1}-01-01`,
            end: `${year}-12-31`
          }
        };
        _getPreview({
          id: newId,
          add_columns: _columns,
          add_measures: _measures,
          page: 1,
          recordsPerPage: 10,
          add_hierarchies: _hierarchies,
          groupingOption: convertTotalGrouping(reportDetails.selectedTotalGrouping),
          reportType: convertReportType(reportDetails.selectedFundingGrouping),
          filters: dateFilter
        });
      }
    } else {
      // With current user input we cant generate a valid preview.
      _updatePreviewId(-1, undefined);
    }
  }

  handleMenuClick = (index) => {
    this.setState({ visibleTab: index });
  }

  handleNavButtonBack = () => {
    const { visibleTab } = this.state;
    this.setState({ visibleTab: visibleTab - 1 });
  }

  handleNavButtonNext = () => {
    const { visibleTab } = this.state;
    this.setState({ visibleTab: visibleTab + 1 });
  }

  render() {
    const { visibleTab } = this.state;
    const { saveNewReport, saveReport, runReport } = this.props;
    return (
      <>
        <Grid>
          <GridRow>
            <GridColumn width="4">
              <MainMenu
                onClick={this.handleMenuClick}
                tab={visibleTab}
                saveNewReport={saveNewReport}
                saveReport={saveReport}
                runReport={runReport} />
            </GridColumn>
            <GridColumn width="12">
              <ReportingDetailSection visible={visibleTab === 0} />
              <ColumnsSection visible={visibleTab === 1} />
              <MeasuresSection visible={visibleTab === 2} />
              <NavigationButtons
                tab={visibleTab}
                backClick={this.handleNavButtonBack}
                nextClick={this.handleNavButtonNext} />
              <PreviewSection />
            </GridColumn>
          </GridRow>
        </Grid>
      </>
    );
  }
}

const mapStateToProps = (state) => ({
  lastReportId: state.previewReducer.lastReportId,
  results: state.previewReducer.results,
  reportDetails: state.uiReducer.reportDetails,
  columns: state.uiReducer.columns,
  measures: state.uiReducer.measures,
  hierarchies: state.uiReducer.hierarchies,
});

const mapDispatchToProps = dispatch => bindActionCreators({
  _updatePreviewId: (id, name) => updatePreviewId(id, name),
  _getPreview: (body) => getPreview(body),
}, dispatch);

export default connect(mapStateToProps, mapDispatchToProps)(MainContent);

MainContent.propTypes = {
  lastReportId: PropTypes.number,
  reportDetails: PropTypes.object,
  columns: PropTypes.object,
  measures: PropTypes.object,
  hierarchies: PropTypes.object,
  _updatePreviewId: PropTypes.func.isRequired,
  _getPreview: PropTypes.func.isRequired,
  saveNewReport: PropTypes.func.isRequired,
  saveReport: PropTypes.func.isRequired,
  runReport: PropTypes.func.isRequired,
};

MainContent.defaultProps = {
  lastReportId: -1,
  reportDetails: undefined,
  columns: undefined,
  measures: undefined,
  hierarchies: undefined,
};

MainContent.contextType = ReportGeneratorContext;
