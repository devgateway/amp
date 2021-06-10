import React, { Component } from 'react';
import PropTypes from 'prop-types';
import {
  Button, Item, Menu,
} from 'semantic-ui-react';
import { bindActionCreators } from 'redux';
import { connect } from 'react-redux';
import { FM_IS_PUBLIC_REPORT_ENABLED, PROFILE_TAB, TRN_PREFIX } from '../utils/constants';
import './MainMenu.css';
import SaveModal from './SaveModal';
import { getLayout } from '../actions/layoutActions';
import { areEnoughDataForPreview } from '../utils/Utils';

const MENU = 'menu';

class MainMenu extends Component {
  constructor() {
    super();
    this.state = {
      modalSaveError: null, saveModalOpen: false, isNewReport: false
    };
  }

  componentDidMount() {
    const { _getLayout } = this.props;
    _getLayout();
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

  render() {
    const {
      modalSaveError, saveModalOpen, isNewReport
    } = this.state;
    const {
      translations, tab, saveNewReport, saveReport, loaded, results, runReport, id, options,
      columns, measures, hierarchies, reportDetails, profile
    } = this.props;
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
            content={translations[`${TRN_PREFIX}financialMeasures`]}
            active={tab === 2}
            onClick={this.handleItemClick}
              />
          {loaded && results.logged ? (
            <Item className="save_buttons_item">
              <Button color="green" onClick={() => this.setSaveModalOpen(true, !id)}>
                {translations[`${TRN_PREFIX}save`]}
              </Button>
              <Button color="orange" onClick={() => this.setSaveModalOpen(true, true)}>
                {translations[`${TRN_PREFIX}saveAs`]}
              </Button>
            </Item>
          ) : null }
        </Menu>
        {(loaded && !results.logged && !id
          && options && options.find(i => i.name === FM_IS_PUBLIC_REPORT_ENABLED).visible
          && profile !== PROFILE_TAB) ? (
            <Item>
              <Button
                size="huge"
                fluid
                color="green"
                onClick={runReport}
                disabled={!areEnoughDataForPreview(columns, measures, hierarchies, reportDetails)}>
                {translations[`${TRN_PREFIX}plusRunReport`]}
              </Button>
            </Item>
          ) : null }
        <SaveModal
          open={saveModalOpen}
          modalSaveError={modalSaveError}
          isNewReport={isNewReport}
          save={isNewReport ? saveNewReport : saveReport}
          close={() => this.setSaveModalOpen(false)} />
      </>
    );
  }
}

const mapStateToProps = state => ({
  translations: state.translationsReducer.translations,
  description: state.uiReducer.reportDetails.description,
  selectedReportCategory: state.uiReducer.reportDetails.selectedReportCategory,
  loaded: state.layoutReducer.loaded,
  results: state.layoutReducer.results,
  id: state.uiReducer.id,
  options: state.uiReducer.options,
  reportDetails: state.uiReducer.reportDetails,
  columns: state.uiReducer.columns,
  measures: state.uiReducer.measures,
  hierarchies: state.uiReducer.hierarchies,
  profile: state.uiReducer.profile,
});

const mapDispatchToProps = dispatch => bindActionCreators({
  _getLayout: () => getLayout()
}, dispatch);

export default connect(mapStateToProps, mapDispatchToProps)(MainMenu);

MainMenu.propTypes = {
  onClick: PropTypes.func.isRequired,
  translations: PropTypes.object.isRequired,
  tab: PropTypes.number.isRequired,
  saveNewReport: PropTypes.func.isRequired,
  saveReport: PropTypes.func.isRequired,
  loaded: PropTypes.bool,
  results: PropTypes.object,
  _getLayout: PropTypes.func.isRequired,
  runReport: PropTypes.func.isRequired,
  id: PropTypes.number,
  options: PropTypes.object,
  reportDetails: PropTypes.object,
  columns: PropTypes.object,
  measures: PropTypes.object,
  hierarchies: PropTypes.object,
  profile: PropTypes.string,
};

MainMenu.defaultProps = {
  loaded: false,
  results: undefined,
  id: undefined,
  options: undefined,
  reportDetails: undefined,
  columns: undefined,
  measures: undefined,
  hierarchies: undefined,
  profile: undefined
};
