import React, { Component } from 'react';
import {Button, Modal, Table} from 'react-bootstrap';
import * as AC from '../../utils/ActivityConstants';
import Checkbox from 'rc-checkbox';
require('../../styles/ActivityView.css');

export default class SimpleModal extends Component {

  constructor(props) {
    super(props); 
    this.handleHide = this.handleHide.bind(this);
    this.handleChangeCheckbox = this.handleChangeCheckbox.bind(this);
    this.handleCompare = this.handleCompare.bind(this);
    this.handleHistory = this.handleHistory.bind(this);
    this.handleUpdateVersion = this.handleUpdateVersion.bind(this);
    
    this.state = {
      show: false,
      activityId: props.activityInfo[AC.INFO_ACTIVITY_ID],
      currentValues: []
    };
  }

  handleChangeCheckbox(value) {
    this.setState({
        currentValues: this._getToggledOptions(this.state.currentValues, value)
    });

  }

  handleHide() {
    this.setState({ show: false });
  }

  handleCompare(event) {
    event.preventDefault();
    const values = this.state.currentValues;
    const data = {
        action: 'compare',
        activityOneId: values[0],
        activityTwoId: values[1],
        showMergeColumn: false,
        method: 'compare',
        activityId: this.state.activityId
    };

    this.submitPostForm('/aim/compareActivityVersions.do', data, '_blank');
  }

  handleHistory(event) {
    event.preventDefault();
    const data = {
        actionMethod: 'changesSummary',
        activityId: this.state.activityId
    };

    this.submitPostForm('/aim/viewActivityHistory.do', data, '_blank');

  }

  handleUpdateVersion(event, newVersionId){
    event.preventDefault();
    const data = {
        action: 'setVersion',
        method: 'compare',
        activityCurrentVersion : newVersionId
    };

    this.submitPostForm('/aim//compareActivityVersions.do', data, '_self');

  }

  submitPostForm(url, data, target) {
    var form = document.createElement("form");
    form.target = target;
    form.method = "POST";
    form.action = url;
    form.style.display = "none";

    for (var key in data) {
        var input = document.createElement("input");
        input.type = "hidden";
        input.name = key;
        input.value = data[key];
        form.appendChild(input);
    }

    document.body.appendChild(form);
    form.submit();
    document.body.removeChild(form);
  }

  _getToggledOptions(currentValues, value) {
    const id = '' + value.target.name;
    if (!currentValues) {
        currentValues = [id];
    } else {
        if (currentValues.indexOf(id) >= 0) {
            currentValues.splice(currentValues.indexOf(id));
        } else {
            currentValues.push(id);
        }
    }
    return currentValues;
  }

  _compareToggle(activityId){
      const values = this.state.currentValues;
      return values && values.length > 1 && (activityId === undefined || values.indexOf('' + activityId) < 0);
  }

  _getVersionTable(activityInfo, translations){
    let row = [];
    for (var id in activityInfo[AC.VERSION_HISTORY]) {
        const activityId = activityInfo[AC.VERSION_HISTORY][id][AC.INFO_ACTIVITY_ID];
        let actionLabel = ('');
        if (activityId === activityInfo[AC.INFO_LAST_VERSION]) {
            actionLabel = (translations['current_version']);
        } else if (activityInfo[AC.UPDATE_CURRENT_VERSION]) {
            actionLabel = (
                <Button className={'link_version'} bsStyle="link" bsSize="xsmall" onClick={(evt) => this.handleUpdateVersion(evt, activityId)}>
                    {translations['make_current_version']}
                </Button>);
        }
        row.push(<tr key={'Row_' + activityId}>
            <td>
                <Checkbox
                    name={'' + activityId}
                    onChange={this.handleChangeCheckbox}
                    disabled={this._compareToggle(activityId)}
                />
            </td>
            <td>{activityInfo[AC.VERSION_HISTORY][id][AC.MODIFIED_BY_INFO]}</td>
            <td>{activityInfo[AC.VERSION_HISTORY][id][AC.MODIFIED_DATE]}</td>
            <td className='preview_container'>{actionLabel}</td>
        </tr>);
    }
    return (
    <Table striped bordered condensed hover>
        <thead>
            <tr>
            <th>#</th>
            <th>{translations['last_modification']}</th>
            <th>{translations['date']}</th>
            <th>{translations['action']}</th>
            </tr>
        </thead>
        <tbody>
            {row}            
        </tbody>
    </Table>
    );
  }

  render () {
    const { activityInfo, translations, settings } = this.props;
    const label = translations['version_history'];
    let showChangeSummary = settings[AC.TEAM_ID] || settings[AC.PUBLIC_CHANGE_SUMMARY];
    return (
        <div className={'preview_icons'}>
            <a title={label} onClick={() => this.setState({ show: true })}>
                <img src={'/TEMPLATE/reamp/modules/activity-preview/styles/images/AMP_history.svg'}/>
                {label}
            </a>

            <Modal show={this.state.show} onHide={this.handleHide} bsSize="large"
                container={this} aria-labelledby="contained-modal-title">
                <Modal.Header closeButton>
                    <Modal.Title id="contained-modal-title">{label}</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    {this._getVersionTable(activityInfo, translations)}
                    <Button onClick={this.handleCompare} disabled={!this._compareToggle()}>
                        {translations['compare_versions']}
                    </Button>
                    {showChangeSummary &&
                        <Button onClick={this.handleHistory}>{translations['show_change_summary']}</Button>
                    }                    
                </Modal.Body>
                <Modal.Footer>
                <Button onClick={this.handleHide}>{translations['close']}</Button>
                </Modal.Footer>
            </Modal>
      </div>
    );
  }
}

