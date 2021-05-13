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

  saveReport = (title) => {
    alert(title);
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
          save={this.saveReport}
          close={() => this.setSaveModalOpen(false)} />
      </>
    );
  } /* TODO: dispatch the title and send as props. */
}

const mapStateToProps = state => ({
  translations: state.translationsReducer.translations
});

const mapDispatchToProps = dispatch => bindActionCreators({}, dispatch);

export default connect(mapStateToProps, mapDispatchToProps)(MainMenu);

MainMenu.propTypes = {
  onClick: PropTypes.func.isRequired,
  translations: PropTypes.object.isRequired,
  tab: PropTypes.number.isRequired,
};
