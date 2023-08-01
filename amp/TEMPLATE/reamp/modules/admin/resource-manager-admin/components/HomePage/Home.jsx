import React, {Component} from 'react';
import TypeList from './TypeManager/TypeList.jsx';
import Settings from './Settings/Settings.jsx';
import {ALERT_TYPE} from '../../utils/constants.jsx';
import {delay} from 'amp/tools';
import {Alert} from 'react-bootstrap';

export default class HomePage extends Component {
    constructor() {
        super();
        console.log('constructor');
        this.state = {
            errorMessage: '',
            areListloaded: false,
            alert: ALERT_TYPE.NONE,
            alertMsg: '',
            selectedAllowedTypes: [],
            settingsList: {},
            settingsLoaded: false,
            settingsValuesSelected: {}
        };
        this.hideAlert = this.hideAlert.bind(this);
        this.getAlert = this.getAlert.bind(this);
        this.handleSelectedChanged = this.handleSelectedChanged.bind(this);
        this.handleSelectedChangeSettings = this.handleSelectedChangeSettings.bind(this);
    }

    componentDidMount() {
        this.props.loadAvailableTypes();
        this.props.loadAllowedTypes();
        this.props.loadSettings();
    }

    componentWillReceiveProps(nextProps) {
        if (nextProps.homePage.typesLoaded && nextProps.homePage.allowedLoaded && !this.state.areListloaded) {
            const areListloaded = true;
            const selectedAllowedTypes = nextProps.homePage.typesAllowed;
            this.setState({ areListloaded, selectedAllowedTypes });
        }

        if (nextProps.homePage.alert !== ALERT_TYPE.NONE) {
            const alert = nextProps.homePage.alert;
            const alertMsg = nextProps.homePage.alertMsg;
            this.setState({ alert, alertMsg });
        }

        if (nextProps.homePage.settingsLoaded && !this.state.settingsLoaded) {
            //settings have loaded
            const settingsList = nextProps.homePage.settingsList;
            const settingsLoaded = nextProps.homePage.settingsLoaded;
            this.setState({ settingsLoaded, settingsList });
        }
    }

    handleSelectedChanged(selectedAllowedTypes) {
        this.state.selectedAllowedTypes = selectedAllowedTypes;
    }

    handleSelectedChangeSettings(settingsValuesSelected) {
        this.state.settingsValuesSelected = settingsValuesSelected
    }

    getAlert() {
        if (this.state.alert !== ALERT_TYPE.NONE) {
            let isSuccess = this.state.alert === ALERT_TYPE.SUCCESS;
            if (isSuccess) {
                delay(2000).then(this.hideAlert);
            }
            return (
                <Alert ref="errorAlert" bsStyle={this.state.alert} className="resultAlert" bsClass="alert"
                       onDismiss={this.hideAlert}>
                    {isSuccess ? this.__('amp.resource-manager:success') : this.state.alertMsg}
                </Alert>);
        } else return '';
    }

    hideAlert() {
        this.setState({ alert: ALERT_TYPE.NONE });
    }

    render() {
        let infoAlert = this.getAlert();

        this.__ = key => this.props.startUp.translations[key];
        return (
            <div >
                <h1>{this.__('amp.resource-manager:resource-manager-title')}</h1>
                    <Settings translations={this.props.startUp.translations} settingsList={this.state.settingsList}
                              settingsLoaded={this.state.settingsLoaded}
                              handleSelectedChangeSettings={this.handleSelectedChangeSettings}
                    />
                    <TypeList typesAvailable={this.props.homePage.typesAvailable || []}
                              typesAllowed={this.props.homePage.typesAllowed || []}
                              handleSelectedChanged={this.handleSelectedChanged}
                              areListloaded={this.state.areListloaded}
                              translations={this.props.startUp.translations}
                    />
                <div className="acceptButton">
                    <button type="button" className="btn btn-primary"
                            onClick={() => {
                                this.props.saveSettings(this.state.selectedAllowedTypes,this.state.settingsValuesSelected);
                            }}>  {this.__('amp.resource-manager:button-accept')}
                    </button>
                    {infoAlert}
                </div>
            </div>
        );
    }

    static translations() {
        return {
            ...TypeList.translations(),
            ...Settings.translations(),
            'amp.resource-manager:resource-manager-title': 'Resource Manager',
            'amp.resource-manager:success': 'Success',
            "amp.resource-manager:button-accept": "Save changes"
        }
    }
}