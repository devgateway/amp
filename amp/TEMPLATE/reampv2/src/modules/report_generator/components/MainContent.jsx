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

import { javaHashCode } from '../utils/Utils';

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
    if (this.areEnoughDataForPreview()) {
      // Convert input data to a String then Number.
      const _reportDetails = reportDetails;
      delete _reportDetails.description;
      const merged = {
        ..._reportDetails,
        ...columns.selected,
        ...columns.order,
        ...measures.selected,
        ...measures.order,
        ...hierarchies.selected,
        ...hierarchies.order
      };
      const name = md5(JSON.stringify(merged));
      const newId = javaHashCode(name);
      // Avoid calling again the preview EP if not needed.
      if (newId !== prevProps.lastReportId) {
        _updatePreviewId(newId, name);
        const cols = columns.available.map(i => {
          if (columns.selected.includes(i.id)) {
            return i.name;
          }
          return undefined;
        }).filter(i => i);
        const _measures = measures.available.map(i => {
          if (measures.selected.includes(i.id)) {
            return i.name;
          }
          return undefined;
        }).filter(i => i);
        const _hierarchies = hierarchies.available.map(i => {
          if (hierarchies.selected.includes(i.id)) {
            return i.name;
          }
          return undefined;
        }).filter(i => i);
        const year = (new Date()).getFullYear();
        const dateFilter = {
          date: {
            start: `${year - 2}-01-01`,
            end: `${year}-12-31`
          }
        };
        _getPreview({
          add_columns: cols,
          add_measures: _measures,
          page: 1,
          recordsPerPage: 10,
          add_hierarchies: _hierarchies,
          groupingOption: 'A', /* TODO: get actual value. */
          filters: dateFilter
        });
      }
    } else {
      // With current user input we cant generate a valid preview.
      _updatePreviewId(-1, undefined);
    }
  }

  areEnoughDataForPreview = () => {
    const {
      columns, measures, hierarchies, reportDetails
    } = this.props;
    if (columns && measures && hierarchies && reportDetails) {
      if (columns.selected.length > 0 && measures.selected.length > 0) {
        const selectedSummaryReport = reportDetails && reportDetails.selectedSummaryReport;
        if (columns.selected.length === hierarchies.selected.length && !selectedSummaryReport) {
          return false;
        }
        // TODO: check if this can be generalized when we implement the saving report functionality.
        return true;
      }
    }
    return false;
  }

  handleMenuClick = (index) => {
    this.setState({ visibleTab: index });
  }

  render() {
    const { visibleTab } = this.state;
    return (
      <>
        <Grid>
          <GridRow>
            <GridColumn width="4">
              <MainMenu onClick={this.handleMenuClick} />
            </GridColumn>
            <GridColumn width="12">
              <ReportingDetailSection visible={visibleTab === 0} />
              <ColumnsSection visible={visibleTab === 1} />
              <MeasuresSection visible={visibleTab === 2} />
              <NavigationButtons />
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
  _updatePreviewId: (id, name) => dispatch(updatePreviewId(id, name)),
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
};

MainContent.defaultProps = {
  lastReportId: -1,
  reportDetails: undefined,
  columns: undefined,
  measures: undefined,
  hierarchies: undefined,
};

MainContent.contextType = ReportGeneratorContext;
