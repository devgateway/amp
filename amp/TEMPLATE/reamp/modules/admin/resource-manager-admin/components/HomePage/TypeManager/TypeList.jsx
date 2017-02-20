import FilteredMultiSelect from 'react-filtered-multiselect';
import React, { Component, PropTypes, Button } from 'react';
import { Alert } from 'react-bootstrap';
import { delay } from 'amp/tools';
require('../../Layout/App.less');
const ALERT_TYPE = {
    NONE: "none",
    SUCCESS: "success",
    ERROR: "danger"
};

export default class TypeList extends Component {

    constructor() {

        super();
        this.state = {
            selectedOptions: [],
            availableOptions: [],
            initialized: false,
            alert: ALERT_TYPE.NONE,
            alertMsg: ''
        };
        this.handleDeselect = this.handleDeselect.bind(this);
        this.handleSelect = this.handleSelect.bind(this);
        this.hideAlert = this.hideAlert.bind(this);
        this.getAlert = this.getAlert.bind(this);
    }

    handleDeselect(deselectedOptions) {
        let selectedOptions = this.state.selectedOptions.slice();
        deselectedOptions.forEach(option => {
            selectedOptions.splice(selectedOptions.indexOf(option), 1)
        });
        this.setState({ selectedOptions });
    }

    sortOptions(a, b) {
        if (a.description > b.description) {
            return 1;
        }
        if (a.description < b.description) {
            return -1;
        }
        return 0;

    }

    componentWillReceiveProps(nextProps) {
        if (!this.state.initialized && nextProps.areListloaded) {
            const selectedOptions = nextProps.typesAllowed;
            const availableOptions = nextProps.typesAvailable;
            availableOptions.sort((a, b) => {
                return this.sortOptions(a, b);
            });
            const initialized = true;
            this.setState({ initialized, selectedOptions, availableOptions });
        }

        if (nextProps.alert !== ALERT_TYPE.NONE) {
            const { alert, alertMsg } = nextProps;
            this.setState({ alert, alertMsg });
        }
    }

    handleSelect(selectedOptions) {
        selectedOptions.sort((a, b) => {
            return this.sortOptions(a, b);
        });
        this.setState({ selectedOptions });
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
                    {isSuccess ? this.__('amp.resource-manager:sucess') : this.state.alertMsg}
                </Alert>);
        } else return '';
    }

    hideAlert() {
        this.setState({ alert: ALERT_TYPE.NONE });
    }

    render() {
        let infoAlert = this.getAlert();
        this.__ = key => this.props.translations[key];
        const { saveAllowedTypes } = this.props;
        const typeToFilter = this.__('amp.resource-manager:type-to-filter');
        const buttonAdd = this.__('amp.resource-manager:button-add');
        return (
            <div>
                <h2 className="subTitle">{this.__('amp.resource-manager:type-list-title')}</h2>
                <div className="row">

                    <div className="col-md-5">
                        <FilteredMultiSelect
                            buttonText={this.__('amp.resource-manager:button-add')}
                            placeholder={typeToFilter}
                            classNames={{
                    filter: 'form-control',
                    select: 'form-control',
                    button: 'btn btn btn-block btn-default',
                    buttonActive: 'btn btn btn-block btn-primary'
                    }}
                            onChange={this.handleSelect}
                            options={this.state.availableOptions}
                            selectedOptions={this.state.selectedOptions}
                            textProp="description"
                            valueProp="name"
                            size="16"
                        />
                    </div>
                    <div className="col-md-5">
                        <FilteredMultiSelect
                            buttonText={this.__('amp.resource-manager:button-remove')}
                            placeholder={typeToFilter}
                            classNames={{
                    filter: 'form-control',
                    select: 'form-control',
                    button: 'btn btn btn-block btn-default',
                    buttonActive: 'btn btn btn-block btn-danger'
                    }}
                            onChange={this.handleDeselect}
                            options={this.state.selectedOptions}
                            textProp="description"
                            valueProp="name"
                            size="16"
                        />
                    </div>
                </div>
                <div className="acceptButton">
                    <button type="button" className="btn btn-primary"
                            onClick={() => {
                                saveAllowedTypes(this.state.selectedOptions);
                            }}>Aceptar
                    </button>
                    {infoAlert}
                </div>
            </div>
        );

    }

    static translations() {
        return {
            "amp.resource-manager:type-list-title": "Type list manager",
            "amp.resource-manager:type-to-filter": "Start typing to filter",
            "amp.resource-manager:button-add": "Add",
            "amp.resource-manager:button-remove": "Remove"
        }

    }
}