import React, { Component } from 'react';
import PropTypes from 'prop-types';
import {
  Button, Divider, Dropdown, Form, Header, Icon, Input, Label, Modal
} from 'semantic-ui-react';
import { bindActionCreators } from 'redux';
import { connect } from 'react-redux';
import { PROFILE_TAB, TRN_PREFIX } from '../utils/constants';
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
    const {
      save, close, name, columns, reportDetails, hierarchies, measures
    } = this.props;
    const msg = validateSaveModal(name, columns, reportDetails, hierarchies, measures);
    if (msg) {
      this.setState({ modalSaveError: msg });
    } else {
      save();
      close();
    }
  }

  handleCategoryChange = (e, { value }) => {
    const { _updateReportDetailsNameReportCategory } = this.props;
    _updateReportDetailsNameReportCategory(value);
  }

  generateSaveModal = () => {
    const {
      translations, open, isNewReport, close, reportPending, metaDataPending, isTab, profile
    } = this.props;
    const loading = reportPending || metaDataPending;
    const trnSufix = isTab || profile === PROFILE_TAB ? 'Tab' : 'Report';
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
            {isNewReport
              ? translations[`${TRN_PREFIX}saveNew${trnSufix}`]
              : translations[`${TRN_PREFIX}save${trnSufix}`]}
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
      translations, reportPending, name, metaDataPending, selectedReportCategory, reportCategories
    } = this.props;
    const { modalSaveError } = this.state;
    const loading = reportPending || metaDataPending;
    const options = reportCategories ? reportCategories.map(i => ({ key: i.id, text: i.label, value: i.id })) : [];
    return (
      <Form loading={loading}>
        {!loading ? (
          <>
            <Form.Field>
              <Label>
                <div className="red_text" style={{ float: 'left', paddingRight: '5px' }}>* </div>
                <div>{translations[`${TRN_PREFIX}enterReportTitle`]}</div>
              </Label>
              <Input defaultValue={name} focus onChange={(event) => this.handleChangeName(event.target.value)} />
            </Form.Field>
            {reportCategories ? (
              <Form.Field>
                <Label>{translations[`${TRN_PREFIX}category`]}</Label>
                <Dropdown
                  className="category-dropdown"
                  placeholder={translations[`${TRN_PREFIX}selectCategory`]}
                  selection
                  options={options}
                  defaultValue={selectedReportCategory}
                  onChange={this.handleCategoryChange}
            />
              </Form.Field>
            ) : null}
            {modalSaveError ? (
              <>
                <Divider />
                <ErrorMessage visible message={translations[TRN_PREFIX + modalSaveError]} />
              </>
            ) : null}
          </>
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
  columns: state.uiReducer.columns.selected,
  metaDataPending: state.uiReducer.metaDataPending,
  selectedReportCategory: state.uiReducer.reportDetails.selectedReportCategory,
  reportCategories: state.uiReducer.reportCategories,
  reportDetails: state.uiReducer.reportDetails,
  hierarchies: state.uiReducer.hierarchies,
  measures: state.uiReducer.measures,
  isTab: state.uiReducer.isTab,
  profile: state.uiReducer.profile,
});

const mapDispatchToProps = dispatch => bindActionCreators({
  _updateReportDetailsName: (data) => dispatch(updateReportDetailsName(data)),
  _updateReportDetailsNameReportCategory: (data) => dispatch(updateReportDetailsNameReportCategory(data)),
}, dispatch);

export default connect(mapStateToProps, mapDispatchToProps)(SaveModal);

SaveModal.propTypes = {
  translations: PropTypes.object.isRequired,
  open: PropTypes.bool.isRequired,
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
  columns: PropTypes.array,
  reportDetails: PropTypes.object,
  hierarchies: PropTypes.object,
  measures: PropTypes.object,
  isTab: PropTypes.bool,
  profile: PropTypes.string,
};

SaveModal.defaultProps = {
  reportPending: false,
  metaDataPending: false,
  name: undefined,
  selectedReportCategory: null,
  reportCategories: [],
  columns: [],
  reportDetails: undefined,
  hierarchies: undefined,
  measures: undefined,
  isTab: false,
  profile: undefined,
};
