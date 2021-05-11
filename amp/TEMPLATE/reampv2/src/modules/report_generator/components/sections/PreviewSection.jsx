import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { bindActionCreators } from 'redux';
import { connect } from 'react-redux';
import {
  Form,
  Grid, GridColumn
} from 'semantic-ui-react';
import { TRN_PREFIX } from '../../utils/constants';
import { ReportGeneratorContext } from '../StartUp';
import ErrorMessage from '../ErrorMessage';

/**
 * This component will check (almost) all values selected by the user and fetch a new report preview if necessary.
 * Since the preview generation can take several seconds to complete and the user can make new modifications
 * to the UI thus triggering a new preview, we will keep track of the last preview requested and ignore the data
 * produced by older requests (notice that the latest request could return after an older one).
 */
class PreviewSection extends Component {
  renderReport = (results) => <div className="preview-container" dangerouslySetInnerHTML={{ __html: results }} />

  render() {
    const {
      lastReportId, previewLoading, previewLoaded, previewError, results, translations
    } = this.props;
    if (previewLoading) {
      return <Form loading />;
    } else if (previewError) {
      return <ErrorMessage visible message={translations[`${TRN_PREFIX}previewError`]} />;
    } else if (previewLoaded) {
      return (
        <Grid>
          <Grid.Row>
            <GridColumn width={16}>
              {this.renderReport(results)}
            </GridColumn>
          </Grid.Row>
        </Grid>
      );
    } else if (lastReportId <= 0) {
      return null;
    } else {
      return <span>{translations[`${TRN_PREFIX}no-data-short`]}</span>;
    }
  }
}

const mapStateToProps = (state) => ({
  translations: state.translationsReducer.translations,
  lastReportId: state.previewReducer.lastReportId,
  previewLoading: state.previewReducer.pending,
  previewLoaded: state.previewReducer.loaded,
  previewError: state.previewReducer.error,
  results: state.previewReducer.results,
});

const mapDispatchToProps = dispatch => bindActionCreators({}, dispatch);

export default connect(mapStateToProps, mapDispatchToProps)(PreviewSection);

PreviewSection.propTypes = {
  translations: PropTypes.object.isRequired,
  lastReportId: PropTypes.number,
  previewLoading: PropTypes.bool,
  previewLoaded: PropTypes.bool,
  previewError: PropTypes.bool,
  results: PropTypes.object,
};

PreviewSection.defaultProps = {
  lastReportId: -1,
  previewLoading: false,
  previewLoaded: false,
  previewError: false,
  results: undefined,
};

PreviewSection.contextType = ReportGeneratorContext;
