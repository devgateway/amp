import React, { Component } from 'react';
import PropTypes from 'prop-types';
import {
  Grid, GridColumn, Input
} from 'semantic-ui-react';
import { bindActionCreators } from 'redux';
import { connect } from 'react-redux';
import { IS_MEASURELESS_REPORT, PROFILE_TAB, TRN_PREFIX } from '../../utils/constants';
import {
  updateMeasuresSelected,
  updateMeasuresSorting,
  resetMeasuresSelected,
} from '../../actions/stateUIActions';
import { ReportGeneratorContext } from '../StartUp';
import OptionsList from './OptionsList';
import ColumnsSelector from './ColumnsSelector';
import ErrorMessage from '../ErrorMessage';
import ColumnSorter from './ColumnsSorter';

class MeasuresSection extends Component {
  constructor() {
    super();
    this.state = { search: null };
  }

  handleMeasureSelection = (id) => {
    const { _updateMeasuresSelected, selectedMeasures } = this.props;
    const index = selectedMeasures.indexOf(id);
    if (index > -1) {
      selectedMeasures.splice(index, 1);
    } else {
      selectedMeasures.push(id);
    }
    _updateMeasuresSelected(selectedMeasures);
    this.handleAvailableMeasuresChange(selectedMeasures);
    this.forceUpdate();
  }

  handleMeasureSort = (data) => {
    const { _updateMeasuresSorting } = this.props;
    _updateMeasuresSorting(data);
  }

  handleSearch = (event) => {
    this.setState({ search: event.target.value });
  }

  handleAvailableMeasuresChange(data) {
    const { selectedMeasures, measuresOrder } = this.props;
    if (data.length > measuresOrder.length) {
      // We selected a new column.
      let added = null;
      data.forEach(i => {
        if (!selectedMeasures.find(j => j.id === i)) {
          added = i;
        }
      });
      if (added) {
        measuresOrder.push(added);
        this.handleMeasureSort(measuresOrder);
      }
    } else {
      // We removed a column.
      let deleted = null;
      measuresOrder.forEach(i => {
        if (!data.includes(i)) {
          deleted = i;
        }
      });
      if (deleted) {
        const index = measuresOrder.findIndex(i => i === deleted);
        measuresOrder.splice(index, 1);
        this.handleMeasureSort(measuresOrder);
      }
    }
  }

  handleReset = () => {
    const { _resetMeasuresSelected } = this.props;
    _resetMeasuresSelected();
  }

  render() {
    const {
      visible, translations, measures, selectedMeasures, measuresOrder, profile
    } = this.props;
    const { search } = this.state;
    const _measures = search ? measures.filter(i => i.label.toLowerCase().indexOf(search.toLowerCase()) > -1)
      : measures;
    return (
      <div className={!visible ? 'invisible-tab' : ''}>
        <Grid>
          <GridColumn computer="8">
            <Input icon="search" placeholder={translations[`${TRN_PREFIX}search`]} onChange={this.handleSearch} />
          </GridColumn>
          <GridColumn computer="8" textAlign="right">
            <span className="green_text bold pointer reset-text" onClick={this.handleReset}>
              {translations[`${TRN_PREFIX}resetMeasures`]}
            </span>
          </GridColumn>
          <Grid.Column computer="8" tablet="16">
            <OptionsList
              title={translations[`${TRN_PREFIX}availableFinancialMeasures`]}
              tooltip="tooltip 1"
              isRequired
              className="smallHeight">
              <ColumnsSelector
                columns={_measures}
                selected={selectedMeasures}
                showLoadingWhenEmpty
                noCategories
                onColumnSelectionChange={this.handleMeasureSelection} />
            </OptionsList>
          </Grid.Column>
          <Grid.Column computer="8" tablet="16">
            <OptionsList
              className="smallHeight"
              title={translations[`${TRN_PREFIX}orderSelectedFinancialMeasures`]}
              tooltip="tooltip 1" >
              <ColumnSorter
                keyPrefix="measures"
                translations={translations}
                columns={measures.filter(i => selectedMeasures.find(j => j === i.id))}
                order={measuresOrder}
                onColumnSelectionChange={this.handleHierarchySelection}
                onColumnSortChange={this.handleMeasureSort} />
            </OptionsList>
          </Grid.Column>
          {!IS_MEASURELESS_REPORT && selectedMeasures.length === 0
            ? (
              <Grid.Column computer={16} className="narrowRow">
                <ErrorMessage visible message={translations[`${TRN_PREFIX}mustSelectOneMeasure`]} />
              </Grid.Column>
            )
            : null }
          {selectedMeasures.length > 2 && profile === PROFILE_TAB
            ? (
              <Grid.Column computer={16} className="narrowRow">
                <ErrorMessage visible message={translations[`${TRN_PREFIX}noMoreThan2MeasuresInTabs`]} />
              </Grid.Column>
            )
            : null }
          {selectedMeasures.length === 0 && profile === PROFILE_TAB
            ? (
              <Grid.Column computer={16} className="narrowRow">
                <ErrorMessage visible message={translations[`${TRN_PREFIX}mustHave1MeasuresInTabs`]} />
              </Grid.Column>
            )
            : null }
        </Grid>
      </div>
    );
  }
}

const mapStateToProps = (state) => ({
  translations: state.translationsReducer.translations,
  measures: state.uiReducer.measures.available,
  selectedMeasures: state.uiReducer.measures.selected,
  measuresOrder: state.uiReducer.measures.order,
  profile: state.uiReducer.profile,
});

const mapDispatchToProps = dispatch => bindActionCreators({
  _updateMeasuresSelected: (data) => dispatch(updateMeasuresSelected(data)),
  _updateMeasuresSorting: (data) => dispatch(updateMeasuresSorting(data)),
  _resetMeasuresSelected: () => dispatch(resetMeasuresSelected),
}, dispatch);

export default connect(mapStateToProps, mapDispatchToProps)(MeasuresSection);

MeasuresSection.propTypes = {
  visible: PropTypes.bool.isRequired,
  translations: PropTypes.object.isRequired,
  measures: PropTypes.array,
  selectedMeasures: PropTypes.array,
  measuresOrder: PropTypes.array,
  _updateMeasuresSelected: PropTypes.func.isRequired,
  _updateMeasuresSorting: PropTypes.func.isRequired,
  _resetMeasuresSelected: PropTypes.func.isRequired,
  profile: PropTypes.string,
};

MeasuresSection.defaultProps = {
  measures: [],
  selectedMeasures: [],
  measuresOrder: [],
  profile: undefined
};

MeasuresSection.contextType = ReportGeneratorContext;
