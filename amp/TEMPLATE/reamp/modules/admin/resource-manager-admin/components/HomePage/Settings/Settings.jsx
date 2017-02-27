import React, { Component, PropTypes, Button } from 'react';
import { delay } from 'amp/tools';
import Select from 'react-select';
import 'react-select/dist/react-select.css';
import NumberInput from 'react-number-input';
import Checkbox from 'rc-checkbox';
require('../../Layout/App.less');
export default class Settings extends Component {

    constructor() {
        super();
        this.state = {
            resourceSortOption: {
                options: [],
                valueSelected: undefined,
                name: ''
            },
            maximumFileSize: {
                valueSelected: undefined,
                name: ''
            },
            limitFileToUpload: {
                valueSelected: 0,
                name: ''
            }
        }
        this.onResourceSortOptionChange = this.onResourceSortOptionChange.bind(this);
        this.onMaximunFileSizeChange = this.onMaximunFileSizeChange.bind(this);
        this.onChangeLimitFileToUpload = this.onChangeLimitFileToUpload.bind(this);
        this.onValuesChanges = this.onValuesChanges.bind(this);
    }

    onValuesChanges(nextState) {
        let settingsValuesSelected = {};
        settingsValuesSelected.resourceSortOption = nextState.resourceSortOption.valueSelected;
        settingsValuesSelected.maximumFileSize = nextState.maximumFileSize.valueSelected;
        settingsValuesSelected.limitFileToUpload = nextState.limitFileToUpload.valueSelected;
        this.props.handleSelectedChangeSettings(settingsValuesSelected);
        return (nextState);
    }

    onResourceSortOptionChange(val) {
        let nextState = this.state;
        const resourceSortOption = Object.assign({}, this.state.resourceSortOption, {
            valueSelected: val.id
        });
        nextState.resourceSortOption = resourceSortOption;
        this.setState(this.onValuesChanges(nextState));
    }

    onMaximunFileSizeChange(event) {
        let nextState = this.state;
        const maximumFileSize = Object.assign({}, this.state.maximumFileSize, {
            valueSelected: event.target.value
        });
        nextState.maximumFileSize = maximumFileSize;
        this.setState(this.onValuesChanges(nextState));

    }

    onChangeLimitFileToUpload(event) {
        let nextState = this.state;
        const limitFileToUpload = Object.assign({}, this.state.limitFileToUpload, {
            valueSelected: event.target.checked ? 1 : 0
        });

        nextState.limitFileToUpload = limitFileToUpload;
        this.setState(this.onValuesChanges(nextState));
    }

    componentWillReceiveProps(nextProps) {
        if (nextProps.settingsLoaded) {
            const resourceSortOption = {};
            resourceSortOption.options = nextProps.settingsList[2].value.options;
            resourceSortOption.valueSelected = nextProps.settingsList[2].value.defaultId;
            resourceSortOption.name = nextProps.settingsList[2].name;

            const maximumFileSize = {};
            maximumFileSize.valueSelected = nextProps.settingsList[0].value;
            maximumFileSize.name = nextProps.settingsList[0].name;

            const limitFileToUpload = {};
            limitFileToUpload.valueSelected = nextProps.settingsList[1].value == "true" ? 1 : 0;
            limitFileToUpload.name = nextProps.settingsList[1].name;
            let nextState = Object.assign({}, this.state, { resourceSortOption, maximumFileSize, limitFileToUpload })
            this.setState(this.onValuesChanges(nextState));


        }
    }

    render() {
        this.__ = key => this.props.translations[key];
        return <div>
            <div>
                <div>
                    <span>{this.state.limitFileToUpload.name}</span>
                </div>
                <div>
                    <Checkbox
                        checked={this.state.limitFileToUpload.valueSelected}
                        onChange={this.onChangeLimitFileToUpload}
                    />
                </div>
            </div>
            <div>
                <div>
                    <span>{this.state.maximumFileSize.name}</span>
                </div>
                <div>
                    <NumberInput
                        id="price"
                        type="number"
                        onChange={this.onMaximunFileSizeChange}
                        value={this.state.maximumFileSize.valueSelected}
                        placeholder={this.__('amp.resource-manager:please-enter-value')}
                        min={0}
                        max={100}
                        format="0"
                    /></div>
            </div>
            <div>
                <span>{this.state.resourceSortOption.name}</span><Select
                name="form-field-name"
                value={this.state.resourceSortOption.valueSelected}
                options={this.state.resourceSortOption.options}
                onChange={this.onResourceSortOptionChange}
                labelKey="name"
                valueKey="value"
                className="sortListSelect"
            />

            </div>
        </div>
    }

    static translations() {
        return {
            "amp.resource-manager:limit-file-to-upload": "Limit file to upload",
            "amp.resource-manager:please-enter-value": "Please enter value"
        }
    }
}
