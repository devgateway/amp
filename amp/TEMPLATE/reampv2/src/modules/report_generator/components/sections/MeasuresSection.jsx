import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { Grid, GridRow } from 'semantic-ui-react';
import { bindActionCreators } from 'redux';
import { connect } from 'react-redux';
import { TRN_PREFIX } from '../../utils/constants';
import {
  updateColumnsSelected,
  updateColumnsSorting, updateHierarchiesAvailable,
  updateHierarchiesSelected,
  updateHierarchiesSorting
} from '../../actions/stateUIActions';
import { ReportGeneratorContext } from '../StartUp';
import OptionsList from './OptionsList';
import ColumnsSelector from './ColumnsSelector';

class MeasuresSection extends Component {
  render() {
    const {
      visible, translations, measures, selectedMeasures
    } = this.props;
    return (
      <div className={!visible ? 'invisible-tab' : ''}>
        <Grid divided>
          <GridRow>
            <Grid.Column width={8}>
              <OptionsList title={translations[`${TRN_PREFIX}availableFinancialMeasures`]} tooltip="tooltip 1" >
                <ColumnsSelector
                  columns={measures}
                  selected={selectedMeasures}
                  showLoadingWhenEmpty
                  noCategories
                  onColumnSelectionChange={this.handleColumnSelection} />
              </OptionsList>
            </Grid.Column>
            <Grid.Column width={8}>
              <OptionsList title={translations[`${TRN_PREFIX}orderSelectedFinancialMeasures`]} tooltip="tooltip 1" >
                content 2
              </OptionsList>
            </Grid.Column>
          </GridRow>
        </Grid>
      </div>
    );
  }
}

const mapStateToProps = (state) => ({
  translations: state.translationsReducer.translations,
  measures: state.uiReducer.measures.available,
  selectedMeasures: state.uiReducer.measures.selected,
  measuresOrder: state.uiReducer.measures.order
});

const mapDispatchToProps = dispatch => bindActionCreators({
}, dispatch);

export default connect(mapStateToProps, mapDispatchToProps)(MeasuresSection);

MeasuresSection.propTypes = {
  visible: PropTypes.bool.isRequired,
  translations: PropTypes.object.isRequired,
  measures: PropTypes.array,
  selectedMeasures: PropTypes.array,
  measuresOrder: PropTypes.array,
};

MeasuresSection.defaultProps = {
  measures: [],
  selectedMeasures: [],
  measuresOrder: [],
};

MeasuresSection.contextType = ReportGeneratorContext;
