import FilteredMultiSelect from 'react-filtered-multiselect';
import React, { Component, PropTypes, Button } from 'react';
import { Alert, Glyphicon } from 'react-bootstrap';
import { delay } from 'amp/tools';
require('../../Layout/App.less');


export default class TypeList extends Component {

    constructor() {

        super();
        this.state = {
            selectedOptions: [],
            availableOptions: [],
            initialized: false,
        };
        this.handleDeselect = this.handleDeselect.bind(this);
        this.handleSelect = this.handleSelect.bind(this);

    }

    handleDeselect(deselectedOptions) {
        let selectedOptions = this.state.selectedOptions.slice();
        deselectedOptions.forEach(option => {
            selectedOptions.splice(selectedOptions.indexOf(option), 1)
        });
        this.props.handleSelectedChanged(selectedOptions);
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

    }

    handleSelect(selectedOptions) {
        selectedOptions.sort((a, b) => {
            return this.sortOptions(a, b);
        });
        this.props.handleSelectedChanged(selectedOptions);
        this.setState({ selectedOptions });
    }


    render() {
        this.__ = key => this.props.translations[key];
        const { saveAllowedTypes } = this.props;
        const typeToFilter = this.__('amp.resource-manager:type-to-filter');
        const buttonAdd = this.__('amp.resource-manager:button-add');
        return (
            <div>
                <h2 className="subTitle">{this.__('amp.resource-manager:type-list-title')}</h2>
                <div className="row">
                    <div className="col-md-5"><h4>{this.__('amp.resource-manager:available-file-type')}</h4>
                    </div>
                    <div className="col-md-5"><h4>{this.__('amp.resource-manager:allowed-file-type')}</h4></div>
                </div>
                <div className="row">
                    <div className="col-md-5">
                        <Alert><Glyphicon
                            glyph="info-sign"/><span
                            className="infoText">{this.__('amp.resource-manager:explanation-left')}</span>
                        </Alert>
                    </div>
                    <div className="col-md-5"><Alert><Glyphicon glyph="info-sign"/><span className="infoText">
                        {this.__('amp.resource-manager:explanation-right')}</span></Alert>
                    </div>
                </div>
                < div className="row">

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
                    < div
                        className="col-md-5">
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
                    < / div >
                < / div >
            < / div >
        )
            ;

    }

    static translations() {
        return {
            "amp.resource-manager:available-file-type": "Available file types",
            "amp.resource-manager:allowed-file-type": "Allowed file types",
            "amp.resource-manager:type-list-title": "File type upload manager",
            "amp.resource-manager:type-to-filter": "Start typing to filter",
            "amp.resource-manager:button-add": "Add",
            "amp.resource-manager:button-remove": "Remove",
            "amp.resource-manager:explanation-left": "Select from the list which file types you would like to be allowed " +
            "in AMP uploads. Select and click \"Add\" to select the allowable file types.",
            "amp.resource-manager:explanation-right": "Selected file types that are allowed to be uploaded in AMP. " +
            "To remove any options from the allowed file types, select and click \"Remove\".",
        }

    }
}