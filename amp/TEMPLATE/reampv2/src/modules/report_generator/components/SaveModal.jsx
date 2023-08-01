import React, {Component} from 'react';
import PropTypes from 'prop-types';
import {Button, Checkbox, Divider, Dropdown, Form, Header, Icon, Label, Modal} from 'semantic-ui-react';
import {bindActionCreators} from 'redux';
import {connect} from 'react-redux';
import ErrorMessage from './ErrorMessage';
import {translate, validateSaveModal} from '../utils/Utils';
import {
    revertReportDetailsName,
    updateReportDetailsName,
    updateReportDetailsNameMultiLang,
    updateReportDetailsNameReportCategory
} from '../actions/stateUIActions';
import {PROFILE_TAB} from '../utils/constants';
import MultiLingualInputText from './MultiLingualInputText';

class SaveModal extends Component {
  constructor(props) {
    super(props);
    this.state = { modalSaveError: null, openReportOnSave: false, originalState: undefined };
  }

  shouldComponentUpdate(nextProps, nextState, nextContext) {
    const { open } = this.props;
    if (open !== nextProps.open) {
      this.setState({ modalSaveError: null });
    }
    return (this.props !== nextProps || this.state !== nextState || this.context !== nextContext);
  }

  // eslint-disable-next-line no-unused-vars
  componentDidUpdate(prevProps, prevState, snapshot) {
    const { originalState } = this.state;
    const { name } = this.props;
    if (!originalState && !this.isLoading()) {
      // eslint-disable-next-line react/no-did-update-set-state
      this.setState({
        originalState: {
          name
        }
      });
    }
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
      this.setState({
        originalState: {
          name
        }
      });
      close();
    }
  }

  handleCategoryChange = (e, { value }) => {
    const { _updateReportDetailsNameReportCategory } = this.props;
    _updateReportDetailsNameReportCategory(value);
  }

  handleCancel = () => {
    const { close, _revertReportDetailsName } = this.props;
    const { originalState } = this.state;
    _revertReportDetailsName(originalState.name);
    this.setState({ originalState: undefined });
    close();
  }

  generateSaveModal = () => {
    const {
      translations, open, isNewReport, reportPending, metaDataPending, profile
    } = this.props;
    const loading = reportPending || metaDataPending;
    return (
      <Modal
        className="save-modal"
        closeIcon={false}
        open={open}
        onClose={() => this.handleCancel()}
        centered
        size="small"
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
          <Button color="red" onClick={this.handleCancel}>
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

  isLoading = () => {
    const { reportPending, metaDataPending } = this.props;
    return reportPending || metaDataPending;
  }

  generateSaveModalContent = () => {
    const {
      translations, name, selectedReportCategory, reportCategories, profile, languages,
      globalSettings
    } = this.props;
    const { modalSaveError, openReportOnSave } = this.state;
    if (!globalSettings) {
      return null;
    }
    const loading = this.isLoading();
    const options = reportCategories ? reportCategories.map(i => ({ key: i.id, text: i.label, value: i.id })) : [];
    const names = [];
    if (name) {
      if (globalSettings.multilingual) {
        languages.forEach(l => {
          if (name[l.id]) {
            names.push(name[l.id]);
          } else {
            names.push('');
          }
        });
      } else {
        names.push(name);
      }
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
                isMultiLanguage={globalSettings.multilingual}
                mandatory={[globalSettings['default-language']]}
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

  handleChangeName = (lang, text) => {
    const { _updateReportDetailsName, _updateReportDetailsNameMultiLang } = this.props;
    if (lang) {
      _updateReportDetailsNameMultiLang(text, lang);
    } else {
      _updateReportDetailsName(text);
    }
  }

  render() {
    const { languages } = this.props;
    if (!languages) {
      return null;
    }
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
  languages: state.languagesReducer.data,
  globalSettings: state.settingsReducer.globalSettings,
});

const mapDispatchToProps = dispatch => bindActionCreators({
  _updateReportDetailsNameMultiLang: (data, lang) => updateReportDetailsNameMultiLang(data, lang),
  _updateReportDetailsName: (data) => updateReportDetailsName(data),
  _updateReportDetailsNameReportCategory: (data) => updateReportDetailsNameReportCategory(data),
  _revertReportDetailsName: (data) => revertReportDetailsName(data),
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
  _updateReportDetailsNameMultiLang: PropTypes.func.isRequired,
  _updateReportDetailsNameReportCategory: PropTypes.func.isRequired,
  _revertReportDetailsName: PropTypes.func.isRequired,
  selectedReportCategory: PropTypes.number,
  reportCategories: PropTypes.array,
  columns: PropTypes.array,
  reportDetails: PropTypes.object,
  hierarchies: PropTypes.object,
  measures: PropTypes.object,
  profile: PropTypes.string,
  languages: PropTypes.object,
  globalSettings: PropTypes.object,
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
  languages: undefined,
  globalSettings: undefined
};
