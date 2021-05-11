import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { bindActionCreators } from 'redux';
import { connect } from 'react-redux';
import {
  Grid, GridColumn
} from 'semantic-ui-react';
import { TRN_PREFIX } from '../../utils/constants';
import { ReportGeneratorContext } from '../StartUp';

/**
 * This component will check (almost) all values selected by the user and fetch a new report preview if necessary.
 * Since the preview generation can take several seconds to complete and the user can make new modifications
 * to the UI thus triggering a new preview, we will keep track of the last preview requested and ignore the data
 * produced by older requests (notice that the latest request could return after an older one).
 */
class PreviewSection extends Component {
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
});

const mapDispatchToProps = dispatch => bindActionCreators({}, dispatch);

export default connect(mapStateToProps, mapDispatchToProps)(PreviewSection);

PreviewSection.propTypes = {
  translations: PropTypes.object.isRequired,
};

PreviewSection.defaultProps = {
};

PreviewSection.contextType = ReportGeneratorContext;
