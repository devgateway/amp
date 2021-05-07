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

class MainMenu extends Component {
  constructor() {
    super();
    this.state = {
      activeItem: 'details', modalSaveError: null, saveModalOpen: false, isNewReport: false
    };
  }

  handleItemClick = (e, { name }) => {
    const { onClick } = this.props;
    this.setState({ activeItem: name });
    let index = -1;
    // eslint-disable-next-line default-case
    switch (name) {
      case 'details':
        index = 0;
        break;
      case 'columns':
        index = 1;
        break;
      case 'measures':
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
      activeItem, modalSaveError, saveModalOpen, isNewReport
    } = this.state;
    const { translations } = this.props;
    return (
      <>
        <Menu fluid vertical>
          <Menu.Item
            name="details"
            content={translations[`${TRN_PREFIX}reportingDetails`]}
            active={activeItem === 'details'}
            onClick={this.handleItemClick}
              />
          <Menu.Item
            name="columns"
            content={translations[`${TRN_PREFIX}columns`]}
            active={activeItem === 'columns'}
            onClick={this.handleItemClick}
              />
          <Menu.Item
            name="measures"
            content={translations[`${TRN_PREFIX}measures`]}
            active={activeItem === 'measures'}
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
};
