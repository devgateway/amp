import React, { Component } from 'react';
import PropTypes from 'prop-types';
import {
  Button, Item, Menu,
} from 'semantic-ui-react';
import { bindActionCreators } from 'redux';
import { connect } from 'react-redux';
import { TRN_PREFIX } from '../utils/constants';
import './MainMenu.css';
import SaveModal from './SaveModal';
import { saveNew } from '../actions/stateUIActions';
import { convertTotalGrouping } from '../utils/Utils';

const MENU = 'menu';

class MainMenu extends Component {
  constructor() {
    super();
    this.state = {
      modalSaveError: null, saveModalOpen: false, isNewReport: false
    };
  }

  handleItemClick = (e, { name }) => {
    const { onClick } = this.props;
    let index = -1;
    // eslint-disable-next-line default-case
    switch (name) {
      case MENU + 0:
        index = 0;
        break;
      case MENU + 1:
        index = 1;
        break;
      case MENU + 2:
        index = 2;
        break;
    }
    onClick(index);
  }

  setSaveModalOpen = (open, isNew) => {
    this.setState({ saveModalOpen: open, isNewReport: isNew });
  }

  saveReport = () => {
  }

  saveNewReport = () => {
    const { uiReducer, _saveNew } = this.props;
    const body = {
      id: null,
      name: uiReducer.reportDetails.name,
      description: uiReducer.reportDetails.description,
      type: uiReducer.type,
      groupingOption: convertTotalGrouping(uiReducer.reportDetails.selectedTotalGrouping),
      summary: uiReducer.reportDetails.selectedSummaryReport,
      tab: uiReducer.reportDetails.isTab,
      publicView: uiReducer.reportDetails.publicView,
      workspaceLinked: uiReducer.reportDetails.workspaceLinked,
      alsoShowPledges: uiReducer.reportDetails.selectedAlsoShowPledges,
      showOriginalCurrency: uiReducer.reportDetails.selectedShowOriginalCurrencies,
      allowEmptyFundingColumns: uiReducer.reportDetails.selectedAllowEmptyFundingColumns,
      splitByFunding: uiReducer.reportDetails.selectedSplitByFunding,
      reportCategory: uiReducer.reportDetails.selectedReportCategory,
      ownerId: uiReducer.reportDetails.ownerId,
      includeLocationChildren: uiReducer.reportDetails.includeLocationChildren,
      columns: uiReducer.columns.selected,
      hierarchies: uiReducer.hierarchies.order.filter(i => uiReducer.hierarchies.selected.find(j => j === i)),
      measures: uiReducer.measures.order.filter(i => uiReducer.measures.selected.find(j => j === i)),
      filters: uiReducer.filters,
      settings: uiReducer.settings,
    };
    _saveNew(body);
  }

  render() {
    const {
      modalSaveError, saveModalOpen, isNewReport
    } = this.state;
    const { translations, tab } = this.props;
    return (
      <>
        <Menu fluid vertical>
          <Menu.Item
            name={MENU + 0}
            content={translations[`${TRN_PREFIX}reportingDetails`]}
            active={tab === 0}
            onClick={this.handleItemClick}
              />
          <Menu.Item
            name={MENU + 1}
            content={translations[`${TRN_PREFIX}columns`]}
            active={tab === 1}
            onClick={this.handleItemClick}
              />
          <Menu.Item
            name={MENU + 2}
            content={translations[`${TRN_PREFIX}measures`]}
            active={tab === 2}
            onClick={this.handleItemClick}
              />
          <Item className="save_buttons_item">
            <Button color="green" onClick={() => this.setSaveModalOpen(true, false)}>
              {translations[`${TRN_PREFIX}save`]}
            </Button>
            <Button color="orange" onClick={() => this.setSaveModalOpen(true, true)}>
              {translations[`${TRN_PREFIX}saveAs`]}
            </Button>
          </Item>
        </Menu>
        <Item>
          <Button disabled size="huge" fluid color="grey">{translations[`${TRN_PREFIX}plusRunReport`]}</Button>
        </Item>
        <SaveModal
          open={saveModalOpen}
          modalSaveError={modalSaveError}
          isNewReport={isNewReport}
          save={isNewReport ? this.saveNewReport : this.saveReport}
          close={() => this.setSaveModalOpen(false)} />
      </>
    );
  } /* TODO: dispatch the title and send as props. */
}

const mapStateToProps = state => ({
  translations: state.translationsReducer.translations,
  uiReducer: state.uiReducer,
  description: state.uiReducer.reportDetails.description,
  selectedReportCategory: state.uiReducer.reportDetails.selectedReportCategory,
});

const mapDispatchToProps = dispatch => bindActionCreators({
  _saveNew: (data) => saveNew(data),
}, dispatch);

export default connect(mapStateToProps, mapDispatchToProps)(MainMenu);

MainMenu.propTypes = {
  onClick: PropTypes.func.isRequired,
  translations: PropTypes.object.isRequired,
  tab: PropTypes.number.isRequired,
  _saveNew: PropTypes.func.isRequired,
  uiReducer: PropTypes.object.isRequired,
};

MainMenu.defaultProps = {
};
