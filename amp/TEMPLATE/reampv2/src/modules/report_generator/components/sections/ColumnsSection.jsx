import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { bindActionCreators } from 'redux';
import { connect } from 'react-redux';
import {
  Grid, GridColumn, GridRow, Image, Input
} from 'semantic-ui-react';
import { TRN_PREFIX } from '../../utils/constants';
import OptionsList from './OptionsList';
import { ReportGeneratorContext } from '../StartUp';
import ColumnsSelector from './ColumnsSelector';
import { updateColumnsSelected, updateColumnsSorting } from '../../actions/stateUIActions';
import ColumnSorter from './ColumnsSorter';

class ColumnsSection extends Component {
  handleColumnSelection = (id) => {
    const { selectedColumns, _updateColumnsSelected } = this.props;
    const index = selectedColumns.indexOf(id);
    if (index > -1) {
      selectedColumns.splice(index, 1);
    } else {
      selectedColumns.push(id);
    }
    _updateColumnsSelected(selectedColumns);
    this.forceUpdate();
  }

  handleColumnSort = (data) => {
    const { _updateColumnsSorting } = this.props;
    _updateColumnsSorting(data);
  }

  render() {
    const {
      visible, translations, columns, selectedColumns
    } = this.props;
    return (
      <div className={!visible ? 'invisible-tab' : ''}>
        <Grid columns={3} divided>
          <GridRow>
            <GridColumn width="16">
              <Input icon="search" placeholder={translations[`${TRN_PREFIX}search`]} />
            </GridColumn>
          </GridRow>
          <GridRow>
            <Grid.Column>
              <OptionsList title={translations[`${TRN_PREFIX}availableColumns`]} tooltip="tooltip 1" >
                <ColumnsSelector
                  columns={columns}
                  selected={selectedColumns}
                  showLoadingWhenEmpty
                  onColumnSelectionChange={this.handleColumnSelection} />
              </OptionsList>
            </Grid.Column>
            <Grid.Column>
              <OptionsList title={translations[`${TRN_PREFIX}selectedColumns`]} isRequired tooltip="tooltip 2" >
                <ColumnSorter columns={columns} selected={selectedColumns} onColumnSortChange={this.handleColumnSort} />
              </OptionsList>
            </Grid.Column>
            <Grid.Column>
              <OptionsList title={translations[`${TRN_PREFIX}hierarchies`]} tooltip="tooltip 3" >
                <Image src="https://react.semantic-ui.com/images/wireframe/media-paragraph.png" />
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
  columns: state.uiReducer.columns.available,
  selectedColumns: state.uiReducer.columns.selected
});

const mapDispatchToProps = dispatch => bindActionCreators({
  _updateColumnsSelected: (data) => dispatch(updateColumnsSelected(data)),
  _updateColumnsSorting: (data) => dispatch(updateColumnsSorting(data)),
}, dispatch);

export default connect(mapStateToProps, mapDispatchToProps)(ColumnsSection);

ColumnsSection.propTypes = {
  visible: PropTypes.bool.isRequired,
  translations: PropTypes.object.isRequired,
  columns: PropTypes.array,
  selectedColumns: PropTypes.array,
  _updateColumnsSelected: PropTypes.func.isRequired,
  _updateColumnsSorting: PropTypes.func.isRequired,
};

ColumnsSection.defaultProps = {
  columns: [],
  selectedColumns: []
};

ColumnsSection.contextType = ReportGeneratorContext;
