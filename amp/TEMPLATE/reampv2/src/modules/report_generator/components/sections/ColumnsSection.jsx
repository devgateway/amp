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

class ColumnsSection extends Component {
  render() {
    const { visible, translations, columns } = this.props;
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
                <ColumnsSelector columns={columns} />
              </OptionsList>
            </Grid.Column>
            <Grid.Column>
              <OptionsList title={translations[`${TRN_PREFIX}selectedColumns`]} isRequired tooltip="tooltip 2" >
                <Image src="https://react.semantic-ui.com/images/wireframe/media-paragraph.png" />
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
  columns: state.uiReducer.columns.available
});

const mapDispatchToProps = dispatch => bindActionCreators({
}, dispatch);

export default connect(mapStateToProps, mapDispatchToProps)(ColumnsSection);

ColumnsSection.propTypes = {
  visible: PropTypes.bool.isRequired,
  translations: PropTypes.object.isRequired,
  columns: PropTypes.array
};

ColumnsSection.defaultProps = {
  columns: []
};

ColumnsSection.contextType = ReportGeneratorContext;
