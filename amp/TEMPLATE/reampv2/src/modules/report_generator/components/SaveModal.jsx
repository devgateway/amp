import React, { Component } from 'react';
import PropTypes from 'prop-types';
import {
  Button, Divider, Dropdown, Form, Header, Icon, Input, Label, Modal
} from 'semantic-ui-react';
import { bindActionCreators } from 'redux';
import { connect } from 'react-redux';
import { TRN_PREFIX } from '../utils/constants';
import ErrorMessage from './ErrorMessage';
import { validateSaveModal } from '../utils/Utils';
import { updateReportDetailsName, updateReportDetailsNameReportCategory } from '../actions/stateUIActions';

class SaveModal extends Component {
  constructor(props) {
    super(props);
    this.state = { modalSaveError: null };
  }

  shouldComponentUpdate(nextProps, nextState, nextContext) {
    const { open } = this.props;
    if (open !== nextProps.open) {
      this.setState({ modalSaveError: null });
    }
    return (this.props !== nextProps || this.state !== nextState || this.context !== nextContext);
  }

  validateAndSave = () => {
    const { save, close, name } = this.props;
    const msg = validateSaveModal(name);
    if (msg) {
      this.setState({ modalSaveError: msg });
    } else {
      save(name);
      close();
    }
  }

  generateSaveModal = () => {
    const {
      translations, open, isNewReport, close, reportPending, metaDataPending
    } = this.props;
    const loading = reportPending || metaDataPending;
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
          <Button color="green" onClick={this.validateAndSave} loading={loading} disabled={loading}>
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
    const {
      translations, reportPending, name, metaDataPending, selectedReportCategory, reportCategories,
      _updateReportDetailsNameReportCategory
    } = this.props;
    const { modalSaveError } = this.state;
    const loading = reportPending || metaDataPending;
    const options = reportCategories ? reportCategories.map(i => ({ key: i.id, text: i.label, value: i.id })) : [];
    return (
      <Form loading={loading}>
        {!loading ? (
          <Form.Field>
            <Label>{translations[`${TRN_PREFIX}enterReportTitle`]}</Label>
            <Input defaultValue={name} focus onChange={(event) => this.handleChangeName(event.target.value)} />
            {reportCategories ? (
              <Dropdown
                placeholder={translations[`${TRN_PREFIX}selectCategory`]}
                fluid
                selection
                options={options}
                defaultValue={selectedReportCategory}
                onChange={_updateReportDetailsNameReportCategory}
            />
            ) : null}
            {modalSaveError ? (
              <>
                <Divider />
                <ErrorMessage visible message={translations[TRN_PREFIX + modalSaveError]} />
              </>
            ) : null}
          </Form.Field>
        ) : null}
      </Form>
    );
  }

  handleChangeName = (val) => {
    const { _updateReportDetailsName } = this.props;
    _updateReportDetailsName(val);
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
  translations: state.translationsReducer.translations,
  reportPending: state.uiReducer.reportPending,
  name: state.uiReducer.reportDetails.name,
  metaDataPending: state.uiReducer.metaDataPending,
  selectedReportCategory: state.uiReducer.reportDetails.selectedReportCategory,
  reportCategories: state.uiReducer.reportCategories,
});

const mapDispatchToProps = dispatch => bindActionCreators({
  _updateReportDetailsName: (data) => dispatch(updateReportDetailsName(data)),
  _updateReportDetailsNameReportCategory: (data) => dispatch(updateReportDetailsNameReportCategory(data)),
}, dispatch);

export default connect(mapStateToProps, mapDispatchToProps)(SaveModal);

SaveModal.propTypes = {
  translations: PropTypes.object.isRequired,
  open: PropTypes.bool.isRequired,
  modalSaveError: PropTypes.string,
  isNewReport: PropTypes.bool.isRequired,
  close: PropTypes.func.isRequired,
  save: PropTypes.func.isRequired,
  reportPending: PropTypes.bool,
  metaDataPending: PropTypes.bool,
  name: PropTypes.string,
  _updateReportDetailsName: PropTypes.func.isRequired,
  _updateReportDetailsNameReportCategory: PropTypes.func.isRequired,
  selectedReportCategory: PropTypes.number,
  reportCategories: PropTypes.array,
};

SaveModal.defaultProps = {
  modalSaveError: null,
  reportPending: false,
  metaDataPending: false,
  name: undefined,
  selectedReportCategory: null,
  reportCategories: []
};
