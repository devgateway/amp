import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { bindActionCreators } from 'redux';
import { connect } from 'react-redux';
import {
  Grid, GridColumn, GridRow, Input
} from 'semantic-ui-react';
import { TRN_PREFIX } from '../../utils/constants';
import OptionsList from './OptionsList';
import { ReportGeneratorContext } from '../StartUp';
import ColumnsSelector from './ColumnsSelector';
import {
  updateColumnsSelected,
  updateColumnsSorting,
  updateHierarchiesSelected,
  updateHierarchiesSorting,
  updateHierarchiesAvailable,
  resetColumnsSelected
} from '../../actions/stateUIActions';
import ColumnSorter from './ColumnsSorter';
import ErrorMessage from '../ErrorMessage';

class ColumnsSection extends Component {
  constructor() {
    super();
    this.state = { search: null };
  }

  handleColumnSelection = (id) => {
    const { selectedColumns, _updateColumnsSelected } = this.props;
    const index = selectedColumns.indexOf(id);
    if (index > -1) {
      selectedColumns.splice(index, 1);
    } else {
      selectedColumns.push(id);
    }
    _updateColumnsSelected(selectedColumns);
    this.handleAvailableHierarchiesChange(selectedColumns);
    this.forceUpdate();
  }

  handleColumnSort = (data) => {
    const { _updateColumnsSorting } = this.props;
    _updateColumnsSorting(data);
  }

  handleAvailableHierarchiesChange = (data) => {
    const {
      _updateHierarchiesAvailable, columns, hierarchies, hierarchiesOrder, selectedColumns
    } = this.props;
    const _hierarchies = hierarchies;
    if (data.length > _hierarchies.length) {
      // We selected a new column.
      let added = null;
      data.forEach(i => {
        if (!selectedColumns.find(j => j.id === i)) {
          added = i;
        }
      });
      if (added && columns.find(i => i.id === added)['is-hierarchy']) {
        _hierarchies.push(columns.find(i => i.id === added));
        _updateHierarchiesAvailable(_hierarchies);
        hierarchiesOrder.push(added);
        this.handleHierarchySort(hierarchiesOrder);
      }
    } else {
      // We removed a column.
      let deleted = null;
      hierarchies.forEach(i => {
        if (!data.includes(i.id)) {
          deleted = i.id;
        }
      });
      if (deleted) {
        let index = hierarchies.findIndex(i => i.id === deleted);
        hierarchies.splice(index, 1);
        index = hierarchiesOrder.findIndex(i => i === deleted);
        hierarchiesOrder.splice(index, 1);
        _updateHierarchiesAvailable(hierarchies);
        this.handleHierarchySort(hierarchiesOrder);
        this.handleHierarchySelection(deleted);
      }
    }
  }

  handleHierarchySelection = (id) => {
    const { _updateHierarchiesSelected, selectedHierarchies } = this.props;
    if (selectedHierarchies.includes(id)) {
      selectedHierarchies.splice(selectedHierarchies.findIndex(i => i === id), 1);
    } else {
      selectedHierarchies.push(id);
    }
    _updateHierarchiesSelected(selectedHierarchies);
    this.forceUpdate();
  }

  handleHierarchySort = (data) => {
    const { _updateHierarchiesSorting } = this.props;
    _updateHierarchiesSorting(data);
  }

  handleSearch = (event) => {
    this.setState({ search: event.target.value });
  }

  handleReset = () => {
    const { _resetColumnsSelected } = this.props;
    _resetColumnsSelected();
  }

  render() {
    const {
      visible, translations, columns, selectedColumns, hierarchies, hierarchiesOrder, selectedHierarchies,
      selectedSummaryReport
    } = this.props;
    const { search } = this.state;
    const _columns = search ? columns.filter(i => i.label.toLowerCase().indexOf(search.toLowerCase()) > -1) : columns;
    return (
      <div className={!visible ? 'invisible-tab' : ''}>
        <Grid columns={3}>
          <GridColumn computer="8" tablet="16">
            <Input icon="search" placeholder={translations[`${TRN_PREFIX}search`]} onChange={this.handleSearch} />
          </GridColumn>
          <GridColumn computer="8" textAlign="right" tablet="16">
            <span className="green_text bold pointer reset-text" onClick={this.handleReset}>
              {translations[`${TRN_PREFIX}resetColumns`]}
            </span>
          </GridColumn>
          <Grid.Column computer="6" tablet="16">
            <OptionsList
              title={translations[`${TRN_PREFIX}availableColumns`]}
              tooltip="tooltip 1"
              className="smallHeight" >
              <ColumnsSelector
                columns={_columns}
                selected={selectedColumns}
                showLoadingWhenEmpty
                onColumnSelectionChange={this.handleColumnSelection} />
            </OptionsList>
          </Grid.Column>
          <Grid.Column computer="5" tablet="16">
            <OptionsList
              title={translations[`${TRN_PREFIX}selectedColumns`]}
              isRequired
              tooltip="tooltip 2"
              className="smallHeight" >
              <ColumnSorter
                keyPrefix="columns"
                translations={translations}
                columns={columns.filter(i => selectedColumns.find(j => j === i.id))}
                order={selectedColumns}
                onColumnSortChange={this.handleColumnSort} />
            </OptionsList>
          </Grid.Column>
          <Grid.Column computer="5" tablet="16">
            <OptionsList
              title={translations[`${TRN_PREFIX}hierarchies`]}
              tooltip="tooltip 3"
              className="smallHeight" >
              <ColumnSorter
                keyPrefix="hierarchies"
                checkbox
                selected={selectedHierarchies}
                translations={translations}
                columns={hierarchies}
                order={hierarchiesOrder}
                onColumnSelectionChange={this.handleHierarchySelection}
                onColumnSortChange={this.handleHierarchySort} />
            </OptionsList>
          </Grid.Column>
          {selectedColumns.length === 0
            ? (
              <Grid.Column computer={16} className="narrowRow">
                <ErrorMessage visible message={translations[`${TRN_PREFIX}mustSelectOneColumn`]} />
              </Grid.Column>
            )
            : null }
          {selectedColumns.length > 0 && selectedColumns.length === selectedHierarchies.length && !selectedSummaryReport
            ? (
              <Grid.Column computer={16} className="narrowRow">
                <ErrorMessage visible message={translations[`${TRN_PREFIX}needMoreHierarchies`]} />
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
  columns: state.uiReducer.columns.available,
  selectedColumns: state.uiReducer.columns.selected,
  hierarchies: state.uiReducer.hierarchies.available,
  selectedHierarchies: state.uiReducer.hierarchies.selected,
  hierarchiesOrder: state.uiReducer.hierarchies.order,
  selectedSummaryReport: state.uiReducer.reportDetails.selectedSummaryReport,
});

const mapDispatchToProps = dispatch => bindActionCreators({
  _updateColumnsSelected: (data) => dispatch(updateColumnsSelected(data)),
  _updateColumnsSorting: (data) => dispatch(updateColumnsSorting(data)),
  _updateHierarchiesSelected: (data) => dispatch(updateHierarchiesSelected(data)),
  _updateHierarchiesSorting: (data) => dispatch(updateHierarchiesSorting(data)),
  _updateHierarchiesAvailable: (data) => dispatch(updateHierarchiesAvailable(data)),
  _resetColumnsSelected: () => dispatch(resetColumnsSelected()),
}, dispatch);

export default connect(mapStateToProps, mapDispatchToProps)(ColumnsSection);

ColumnsSection.propTypes = {
  visible: PropTypes.bool.isRequired,
  translations: PropTypes.object.isRequired,
  columns: PropTypes.array,
  selectedColumns: PropTypes.array,
  _updateColumnsSelected: PropTypes.func.isRequired,
  _updateColumnsSorting: PropTypes.func.isRequired,
  hierarchies: PropTypes.array,
  selectedHierarchies: PropTypes.array,
  _updateHierarchiesAvailable: PropTypes.func.isRequired,
  hierarchiesOrder: PropTypes.array,
  _updateHierarchiesSorting: PropTypes.func.isRequired,
  _updateHierarchiesSelected: PropTypes.func.isRequired,
  selectedSummaryReport: PropTypes.bool,
  _resetColumnsSelected: PropTypes.func.isRequired,
};

ColumnsSection.defaultProps = {
  columns: [],
  selectedColumns: [],
  hierarchies: [],
  selectedHierarchies: [],
  hierarchiesOrder: [],
  selectedSummaryReport: false,
};

ColumnsSection.contextType = ReportGeneratorContext;
