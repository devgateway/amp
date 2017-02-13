import React, { Component, PropTypes } from "react";
import TypeList from "./TypeManager/TypeList.jsx";

export default class HomePage extends Component {

    // This seems to be a way to validate this component receives some props.
    /*    static propTypes = {
     // This React component receives the login function to be dispatched as a prop,
     // so it doesnt have to know about the implementation.
     loginAction: PropTypes.func.isRequired
     };
     */
    constructor() {
        super();
        console.log('constructor');
        this.state = {
            errorMessage: '',
            isLoadingList: false
        };
    }

    componentDidMount() {
        this.props.loadAvailableTypes();
        this.props.loadAllowedTypes();
    }

    render() {
        this.__ = key => this.props.startUp.translations[key];
        return (
            <div >
                <div>
                    {this.__('amp.resource-manager:resource-manager-title')}
                </div>
                <div>
                    <TypeList mimeTypesAvailable={this.props.homePage.mimeTypesAvailable}
                              mimeTypesAllowed={this.props.homePage.mimeTypesAllowed}
                              saveAllowedTypes={this.props.saveAllowedTypes}/>
                </div>
            </div>
        );
    }
}