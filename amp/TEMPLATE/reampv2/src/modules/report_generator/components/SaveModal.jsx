import React, { Component } from 'react';
import PropTypes from 'prop-types';
import {
  Button, Checkbox, Divider, Dropdown, Form, Header, Icon, Input, Label, Modal
} from 'semantic-ui-react';
import { bindActionCreators } from 'redux';
import { connect } from 'react-redux';
import ErrorMessage from './ErrorMessage';
import { translate, validateSaveModal } from '../utils/Utils';
import { updateReportDetailsName, updateReportDetailsNameReportCategory } from '../actions/stateUIActions';
import { PROFILE_TAB } from '../utils/constants';
import MultiLingualInputText from './MultiLingualInputText';

class SaveModal extends Component {
  constructor(props) {
    super(props);
    this.state = { modalSaveError: null, openReportOnSave: false };
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
    const { openReportOnSave } = this.state;
    const msg = validateSaveModal(name, columns, reportDetails, hierarchies, measures);
    if (msg) {
      this.setState({ modalSaveError: msg });
    } else {
      save(openReportOnSave);
      close();
    }
  }

  handleCategoryChange = (e, { value }) => {
    const { _updateReportDetailsNameReportCategory } = this.props;
    _updateReportDetailsNameReportCategory(value);
  }

  generateSaveModal = () => {
    const {
      translations, open, isNewReport, close, reportPending, metaDataPending, profile
    } = this.props;
    const loading = reportPending || metaDataPending;
    return (
      <Modal
        className="save-modal"
        closeIcon
        open={open}
        onClose={() => close()}
      >
        <Header content={isNewReport
          ? translate('saveAs', profile, translations)
          : translate('save', profile, translations)} />
        <Modal.Content>
          {this.generateSaveModalContent()}
        </Modal.Content>
        <Modal.Actions>
          <Button color="green" onClick={this.validateAndSave} loading={loading} disabled={loading}>
            <Icon name="save" />
            {isNewReport
              ? translate('saveNew', profile, translations)
              : translate('save', profile, translations)}
          </Button>
          <Button color="red" onClick={close}>
            <Icon name="cancel" />
            {translate('cancel', profile, translations)}
          </Button>
        </Modal.Actions>
      </Modal>
    );
  }

  handleChangeAutoOpen = () => {
    const { openReportOnSave } = this.state;
    this.setState({ openReportOnSave: !openReportOnSave });
  }

  generateSaveModalContent = () => {
    const {
      translations, reportPending, name, metaDataPending, selectedReportCategory, reportCategories, profile
    } = this.props;
    const { modalSaveError, openReportOnSave } = this.state;
    const loading = reportPending || metaDataPending;
    const options = reportCategories ? reportCategories.map(i => ({ key: i.id, text: i.label, value: i.id })) : [];
    const languages = ['fr', 'en', 'sp']; // TODO: Get from settings.
    const names = [];
    if (name) {
      languages.forEach(l => {
        if (name[l]) {
          names.push(name[l]);
        } else {
          names.push('');
        }
      });
    }
    return (
      <Form loading={loading}>
        {!loading ? (
          <>
            <Form.Field>
              <Label>
                <div className="red_text" style={{ float: 'left', paddingRight: '5px' }}>* </div>
                <div>{translate('enterReportTitle', profile, translations)}</div>
              </Label>
              <MultiLingualInputText
                values={names}
                languages={languages}
                onChange={this.handleChangeName} />
            </Form.Field>
            {reportCategories ? (
              <Form.Field>
                <Label>{translate('category', profile, translations)}</Label>
                <Dropdown
                  className="category-dropdown"
                  placeholder={translate('selectCategory', profile, translations)}
                  selection
                  options={options}
                  defaultValue={selectedReportCategory}
                  onChange={this.handleCategoryChange}
            />
              </Form.Field>
            ) : null}
            {profile !== PROFILE_TAB ? (
              <Form.Field>
                <Checkbox
                  toggle
                  label={translate('autoOpen', profile, translations)}
                  checked={openReportOnSave}
                  onChange={this.handleChangeAutoOpen} />
              </Form.Field>
            ) : null}
            {modalSaveError ? (
              <>
                <Divider />
                <ErrorMessage visible message={translate(modalSaveError, profile, translations)} />
              </>
            ) : null}
          </>
        ) : null}
      </Form>
    );
  }

  handleChangeName = (lang, event) => {
    const { _updateReportDetailsName } = this.props;
    _updateReportDetailsName(event.target.value, lang);
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
  _updateReportDetailsName: (data, lang) => updateReportDetailsName(data, lang),
  _updateReportDetailsNameReportCategory: (data) => updateReportDetailsNameReportCategory(data),
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
  profile: undefined,
};
