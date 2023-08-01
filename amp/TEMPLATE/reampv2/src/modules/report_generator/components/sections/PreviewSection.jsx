import React, {Component} from 'react';
import PropTypes from 'prop-types';
import {bindActionCreators} from 'redux';
import {connect} from 'react-redux';
import {Form, Grid, GridColumn} from 'semantic-ui-react';
import {OverlayTrigger, Tooltip} from 'react-bootstrap';
import {ReportGeneratorContext} from '../StartUp';
import ErrorMessage from '../ErrorMessage';
import InfoIcon from '../../static/images/icon-information.svg';
import {translate} from '../../utils/Utils';

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
      lastReportId, previewLoading, previewLoaded, previewError, results, translations, profile
    } = this.props;
    const tooltipText = (
      <Tooltip id={Math.random()}>
        {translate('previewTooltip', profile, translations)}
      </Tooltip>
    );
    if (previewLoading) {
      return <Form loading />;
    } else if (previewError) {
      return <ErrorMessage visible message={translate('previewError', profile, translations)} />;
    } else if (previewLoaded && lastReportId !== -1) {
      return (
        <>
          <Grid>
            <GridColumn width={16}>
              <div className="option-list-title">
                <span>{translate('preview', profile, translations)}</span>
                <OverlayTrigger trigger={['hover', 'focus']} overlay={tooltipText}>
                  <img className="info-icon" src={InfoIcon} alt="info-icon" />
                </OverlayTrigger>
              </div>
              {this.renderReport(results)}
            </GridColumn>
          </Grid>
        </>
      );
    } else if (lastReportId === -1) {
      return null;
    } else {
      return null;
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
  profile: state.uiReducer.profile,
});

const mapDispatchToProps = dispatch => bindActionCreators({}, dispatch);

export default connect(mapStateToProps, mapDispatchToProps)(PreviewSection);

PreviewSection.propTypes = {
  translations: PropTypes.object.isRequired,
  lastReportId: PropTypes.number,
  previewLoading: PropTypes.bool,
  previewLoaded: PropTypes.bool,
  previewError: PropTypes.bool,
  results: PropTypes.string,
  profile: PropTypes.string,
};

PreviewSection.defaultProps = {
  lastReportId: -1,
  previewLoading: false,
  previewLoaded: false,
  previewError: false,
  results: undefined,
  profile: undefined
};

PreviewSection.contextType = ReportGeneratorContext;
