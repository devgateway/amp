import React, { Component } from 'react';
import PropTypes from 'prop-types';
import {
  Button, Divider, Form, Header, Icon, Input, Label, Modal
} from 'semantic-ui-react';
import { bindActionCreators } from 'redux';
import { connect } from 'react-redux';
import { TRN_PREFIX } from '../utils/constants';
import ErrorMessage from './ErrorMessage';
import { validateSaveModal } from '../utils/Utils';

class SaveModal extends Component {
  constructor(props) {
    super(props);
    this.state = { title: '', modalSaveError: null };
  }

  shouldComponentUpdate(nextProps, nextState, nextContext) {
    const { open } = this.props;
    if (open !== nextProps.open) {
      this.setState({ title: '', modalSaveError: null });
    }
    return (this.props !== nextProps || this.state !== nextState || this.context !== nextContext);
  }

  validateAndSave = () => {
    const { title } = this.state;
    const { save, close } = this.props;
    const msg = validateSaveModal(title);
    if (msg !== null) {
      this.setState({ modalSaveError: msg });
    } else {
      save(title);
      close();
    }
  }

  generateSaveModal = () => {
    const {
      translations, open, isNewReport, close
    } = this.props;
    return (
      <Modal
        className="save-modal"
        closeIcon
        open={open}
        onClose={() => close()}
      >
        <Header content={isNewReport ? translations[`${TRN_PREFIX}saveAs`] : translations[`${TRN_PREFIX}save`]} />
        <Modal.Content>
          {this.generateSaveModalContent()}
        </Modal.Content>
        <Modal.Actions>
          <Button color="green" onClick={this.validateAndSave}>
            <Icon name="save" />
            {translations[`${TRN_PREFIX}saveReport`]}
          </Button>
          <Button color="red" onClick={close}>
            <Icon name="cancel" />
            {translations[`${TRN_PREFIX}cancel`]}
          </Button>
        </Modal.Actions>
      </Modal>
    );
  }

  generateSaveModalContent = () => {
    const { translations } = this.props;
    const { modalSaveError } = this.state;
    return (
      <Form>
        <Form.Field>
          <Label>{translations[`${TRN_PREFIX}enterReportTitle`]}</Label>
          <Input focus onChange={(event) => this.setState({ title: event.target.value })} />
          {modalSaveError ? (
            <>
              <Divider />
              <ErrorMessage visible message={translations[TRN_PREFIX + modalSaveError]} />
            </>
          ) : null}
        </Form.Field>
      </Form>
    );
  }

  render() {
    return (
      <div className="invisible">
        {this.generateSaveModal()}
      </div>
    );
  }
}

const mapStateToProps = state => ({
  translations: state.translationsReducer.translations
});

const mapDispatchToProps = dispatch => bindActionCreators({}, dispatch);

export default connect(mapStateToProps, mapDispatchToProps)(SaveModal);

SaveModal.propTypes = {
  translations: PropTypes.object.isRequired,
  open: PropTypes.bool.isRequired,
  modalSaveError: PropTypes.string,
  isNewReport: PropTypes.bool.isRequired,
  close: PropTypes.func.isRequired,
  save: PropTypes.func.isRequired,
};

SaveModal.defaultProps = {
  modalSaveError: null,
};
