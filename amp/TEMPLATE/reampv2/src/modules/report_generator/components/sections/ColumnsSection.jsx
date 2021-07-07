import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { bindActionCreators } from 'redux';
import { connect } from 'react-redux';
import {
  Grid, GridColumn, Input
} from 'semantic-ui-react';
import { PROFILE_TAB } from '../../utils/constants';
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
import { translate } from '../../utils/Utils';
import InputWrapper from './InputWrapper';

class ColumnsSection extends Component {
  constructor() {
    super();
    this.state = { search: null, applySearch: false };
  }

  handleColumnSelection = (id) => {
    const { selectedColumns, _updateColumnsSelected, columns } = this.props;
    const index = selectedColumns.indexOf(id);
    if (index > -1) {
      selectedColumns.splice(index, 1);
    } else {
      selectedColumns.push(id);
    }
    _updateColumnsSelected(selectedColumns);
    // Update the hierarchies only with the selected columns that are a hierarchy.
    this.handleAvailableHierarchiesChange(selectedColumns.filter(i => columns.find(j => j.hierarchy && j.id === i)));
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
      if (added) {
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
        this.handleHierarchySelection(deleted, true);
      }
    }
  }

  handleHierarchySelection = (id, deleted) => {
    const { _updateHierarchiesSelected, selectedHierarchies } = this.props;
    const index = selectedHierarchies.findIndex(i => i === id);
    if (deleted) {
      if (index > -1) {
        selectedHierarchies.splice(index, 1);
      }
    } else if (selectedHierarchies.includes(id)) {
      selectedHierarchies.splice(index, 1);
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
    this.setState({ search: event.target.value, applySearch: false });
  }

  handleReset = () => {
    const {
      _resetColumnsSelected, _updateColumnsSelected, id, initialColumns
    } = this.props;
    if (id) {
      _updateColumnsSelected(Object.assign([], initialColumns));
    } else {
      _resetColumnsSelected();
    }
  }

  // eslint-disable-next-line no-unused-vars
  highlightColumns = (event) => {
    const { search } = this.state;
    if (search) {
      this.setState({ applySearch: true });
    }
  }

  render() {
    const {
      visible, translations, columns, selectedColumns, hierarchies, hierarchiesOrder, selectedHierarchies,
      selectedSummaryReport, profile
    } = this.props;
    const { search, applySearch } = this.state;
    const _columns = search ? columns.filter(i => i.label.toLowerCase().indexOf(search.toLowerCase()) > -1) : columns;
    return (
      <div className={!visible ? 'invisible-tab' : ''}>
        <Grid columns={3}>
          <GridColumn computer="8" tablet="16">
            <InputWrapper keyPress="Enter" keyEvent={this.highlightColumns}>
              <Input
                icon="search"
                placeholder={translate('search', profile, translations)}
                onChange={this.handleSearch} />
            </InputWrapper>
          </GridColumn>
          <GridColumn computer="8" textAlign="right" tablet="16">
            <span className="green_text bold pointer reset-text" onClick={this.handleReset}>
              {translate('resetColumns', profile, translations)}
            </span>
          </GridColumn>
          <Grid.Column computer="6" tablet="16">
            <OptionsList
              title={translate('availableColumns', profile, translations)}
              tooltip="tooltip 1"
              className="smallHeight" >
              <ColumnsSelector
                openSections={applySearch}
                columns={_columns}
                selected={selectedColumns}
                showLoadingWhenEmpty={!search}
                onColumnSelectionChange={this.handleColumnSelection} />
            </OptionsList>
          </Grid.Column>
          <Grid.Column computer="5" tablet="16">
            <OptionsList
              title={translate('selectedColumns', profile, translations)}
              isRequired
              tooltip="tooltip 2"
              className="smallHeight" >
              <ColumnSorter
                keyPrefix="columns"
                translations={translations}
                columns={columns.filter(i => selectedColumns.find(j => j === i.id))}
                order={selectedColumns}
                onColumnSortChange={this.handleColumnSort}
                profile={profile} />
            </OptionsList>
          </Grid.Column>
          <Grid.Column computer="5" tablet="16">
            <OptionsList
              title={translate('hierarchies', profile, translations)}
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
                onColumnSortChange={this.handleHierarchySort}
                profile={profile} />
            </OptionsList>
          </Grid.Column>
          {selectedColumns.length === 0
            ? (
              <Grid.Column computer={16} className="narrowRow">
                <ErrorMessage visible message={translate('mustSelectOneColumn', profile, translations)} />
              </Grid.Column>
            )
            : null }
          {selectedColumns.length > 0 && selectedColumns.length === selectedHierarchies.length && !selectedSummaryReport
            ? (
              <Grid.Column computer={16} className="narrowRow">
                <ErrorMessage visible message={translate('needMoreHierarchies', profile, translations)} />
              </Grid.Column>
            )
            : null }
          {selectedHierarchies.length > 3
            ? (
              <Grid.Column computer={16} className="narrowRow">
                <ErrorMessage visible message={translate('moreThan3Hierarchies', profile, translations)} warning />
              </Grid.Column>
            )
            : null}
          {selectedColumns.length > 3 && profile === PROFILE_TAB
            ? (
              <Grid.Column computer={16} className="narrowRow">
                <ErrorMessage visible message={translate('noMoreThan3ColumnsInTabs', profile, translations)} />
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
  profile: state.uiReducer.profile,
  id: state.uiReducer.id,
  initialColumns: state.mementoReducer.initialColumns,
});

const mapDispatchToProps = dispatch => bindActionCreators({
  _updateColumnsSelected: (data) => updateColumnsSelected(data),
  _updateColumnsSorting: (data) => updateColumnsSorting(data),
  _updateHierarchiesSelected: (data) => updateHierarchiesSelected(data),
  _updateHierarchiesSorting: (data) => updateHierarchiesSorting(data),
  _updateHierarchiesAvailable: (data) => updateHierarchiesAvailable(data),
  _resetColumnsSelected: () => resetColumnsSelected(),
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
  profile: PropTypes.string,
  id: PropTypes.number,
  initialColumns: PropTypes.array,
};

ColumnsSection.defaultProps = {
  columns: [],
  selectedColumns: [],
  hierarchies: [],
  selectedHierarchies: [],
  hierarchiesOrder: [],
  selectedSummaryReport: false,
  profile: undefined,
  id: undefined,
  initialColumns: [],
};

ColumnsSection.contextType = ReportGeneratorContext;
