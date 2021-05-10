import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { bindActionCreators } from 'redux';
import { connect } from 'react-redux';
import {
  Grid, GridColumn
} from 'semantic-ui-react';
import md5 from 'md5';
import { TRN_PREFIX } from '../../utils/constants';
import { ReportGeneratorContext } from '../StartUp';
import { javaHashCode } from '../../utils/Utils';
import { getPreview, updatePreviewId } from '../../actions/previewActions';

/**
 * This component will check (almost) all values selected by the user and fetch a new report preview if necessary.
 * Since the preview generation can take several seconds to complete and the user can make new modifications
 * to the UI thus triggering a new preview, we will keep track of the last preview requested and ignore the data
 * produced by older requests (notice that the latest request could return after an older one).
 */
class PreviewSection extends Component {
  // eslint-disable-next-line no-unused-vars
  shouldComponentUpdate(nextProps, nextState, nextContext) {
    const {
      lastReportId, reportDetails, columns, measures, hierarchies, _updatePreviewId,
    } = this.props;
    let ret = false;
    if (this.isEnoughData()) {
      const merged = {
        ...reportDetails,
        ...columns.selected,
        ...measures.selected,
        ...measures.order,
        ...hierarchies.selected,
        ...hierarchies.order
      };
      const name = md5(JSON.stringify(merged));
      const newId = javaHashCode(name);
      if (newId !== lastReportId) {
        _updatePreviewId(newId, name);
        ret = true;
      }
    } else if (lastReportId) {

    }
    return ret;
  }

  // eslint-disable-next-line no-unused-vars
  componentDidUpdate(prevProps, prevState, snapshot) {
    const {
      _getPreview, lastReportName, columns, selectedColumns
    } = this.props;
    const cols = columns.available.map(i => {
      if (selectedColumns.includes(i.id)) {
        return i.name;
      }
      return undefined;
    }).filter(i => i);
    _getPreview({ name: lastReportName, add_columns: cols });
  }

  isEnoughData = () => {
    const {
      selectedColumns, selectedMeasures, selectedHierarchies, reportDetails
    } = this.props;
    if (selectedColumns.length > 0 && selectedMeasures.length > 0) {
      const selectedSummaryReport = reportDetails && reportDetails.selectedSummaryReport;
      if (selectedColumns.length === selectedHierarchies.length && !selectedSummaryReport) {
        return false;
      }
      // TODO: check if this can be generalized when we implement the saving report functionality.
      return true;
    }
    return false;
  }

  render() {
    return (
      <Grid>
        <Grid.Row>
          <GridColumn>
            Preview
          </GridColumn>
        </Grid.Row>
      </Grid>
    );
  }
}

const mapStateToProps = (state) => ({
  translations: state.translationsReducer.translations,
  lastReportId: state.previewReducer.lastReportId,
  lastReportName: state.previewReducer.lastReportName,
  loading: state.previewReducer.pending,
  loaded: state.previewReducer.loaded,
  results: state.previewReducer.results,
  selectedColumns: state.uiReducer.columns.selected,
  selectedHierarchies: state.uiReducer.hierarchies.selected,
  hierarchiesOrder: state.uiReducer.hierarchies.order,
  selectedMeasures: state.uiReducer.measures.selected,
  measuresOrder: state.uiReducer.measures.order,
  reportDetails: state.uiReducer.reportDetails,
  columns: state.uiReducer.columns,
  measures: state.uiReducer.measures,
  hierarchies: state.uiReducer.hierarchies,
});

const mapDispatchToProps = dispatch => bindActionCreators({
  _updatePreviewId: (id, name) => dispatch(updatePreviewId(id, name)),
  _getPreview: (body) => getPreview(body),
}, dispatch);

export default connect(mapStateToProps, mapDispatchToProps)(PreviewSection);

PreviewSection.propTypes = {
  translations: PropTypes.object.isRequired,
  lastReportId: PropTypes.number,
  lastReportName: PropTypes.string,
  loading: PropTypes.bool,
  loaded: PropTypes.bool,
  results: PropTypes.object,
  selectedColumns: PropTypes.array,
  selectedHierarchies: PropTypes.array,
  hierarchiesOrder: PropTypes.array,
  selectedMeasures: PropTypes.array,
  measuresOrder: PropTypes.array,
  reportDetails: PropTypes.object,
  columns: PropTypes.object,
  measures: PropTypes.object,
  hierarchies: PropTypes.object,
  _updatePreviewId: PropTypes.func.isRequired,
  _getPreview: PropTypes.func.isRequired,
};

PreviewSection.defaultProps = {
  lastReportId: -1,
  lastReportName: undefined,
  loading: false,
  loaded: false,
  results: undefined,
  selectedColumns: [],
  selectedHierarchies: [],
  hierarchiesOrder: [],
  selectedMeasures: [],
  measuresOrder: [],
  reportDetails: undefined,
  columns: undefined,
  measures: undefined,
  hierarchies: undefined,
};

PreviewSection.contextType = ReportGeneratorContext;
