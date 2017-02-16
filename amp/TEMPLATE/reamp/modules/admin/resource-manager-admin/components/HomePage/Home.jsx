import React, { Component, PropTypes } from 'react';
import TypeList from './TypeManager/TypeList.jsx';
import { ALERT_TYPE } from '../../utils/constants.jsx';

export default class HomePage extends Component {
    constructor() {
        super();
        console.log('constructor');
        this.state = {
            errorMessage: '',
            areListloaded: false,
            alert: ALERT_TYPE.NONE,
            alertMsg: ''
        };
    }

    componentDidMount() {
        this.props.loadAvailableTypes();
        this.props.loadAllowedTypes();
    }

    componentWillReceiveProps(nextProps) {
        if (nextProps.homePage.typesLoaded && nextProps.homePage.allowedLoaded) {
            const areListloaded = true;
            this.setState({ areListloaded });
        }

        if (nextProps.homePage.alert !== ALERT_TYPE.NONE) {
            const alert = nextProps.homePage.alert;
            const alertMsg = nextProps.homePage.alertMsg;
            this.setState({ alert, alertMsg });
        }
    }

    render() {
        this.__ = key => this.props.startUp.translations[key];
        return (
            <div >
                <h1>{this.__('amp.resource-manager:resource-manager-title')}</h1>
                <div>
                    <TypeList typesAvailable={this.props.homePage.typesAvailable || []}
                              typesAllowed={this.props.homePage.typesAllowed || []}
                              saveAllowedTypes={this.props.saveAllowedTypes}
                              areListloaded={this.state.areListloaded}
                              translations={this.props.startUp.translations}
                              alert={this.state.alert}
                              alertMsg={this.state.alertMsg}

                    />
                </div>
            </div>
        );
    }

    static translations() {
        return {
            ...TypeList.translations(),
            'amp.resource-manager:resource-manager-title': 'Resource manager Admin',
            'amp.resource-manager:sucess': 'Sucess'
        }
    }
}