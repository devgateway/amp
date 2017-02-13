import React, { Component, PropTypes, Button } from "react";
import FilteredMultiSelect from "react-filtered-multiselect";

/*
 export default class TypeList extends Component {

 constructor() {
 super();
 console.log('constructor');

 this.handleChange = this.handleChange.bind(this);
 }

 handleChange(e) {
 console.log(e);
 }

 componentDidMount() {
 debugger;
 console.log(this.props);
 }

 handleChange(value) {
 console.log(value);
 //this.state.({value});
 }


 render() {
 const options = [
 { value: 1, text: 'Item One' },
 { value: 2, text: 'Item Two' }
 ]
 debugger;
 return (
 <div className="row">
 <div className="col-md-5">
 <FilteredMultiSelect
 classNames={BOOTSTRAP_CLASSES}
 onChange={this.handleChange}
 options={options}
 />
 </div>
 </div>
 );
 }
 }*/
export default class TypeList extends Component {

    constructor() {
        super();
        this.state = {
            selectedOptions: undefined
        }
        ;
        console.log('constructor');
        this.handleDeselect = this.handleDeselect.bind(this);
        this.handleSelect = this.handleSelect.bind(this);
    }

    handleDeselect(deselectedOptions) {
        var selectedOptions = this.state.selectedOptions.slice()
        deselectedOptions.forEach(option => {
            selectedOptions.splice(selectedOptions.indexOf(option), 1)
        })
        this.setState({ selectedOptions });
    }

    componentDidMount() {
        debugger;
    }

    handleSelect(selectedOptions) {
        selectedOptions.sort((a, b) => a.id - b.id);
        this.setState({ selectedOptions });
    }

    handleName() {
        return "name";
    }

    render() {
        debugger;
        let { mimeTypesAvailable } = this.props;
        const { saveAllowedTypes } = this.props;
        this.state.selectedOptions = this.state.selectedOptions || this.props.mimeTypesAllowed;

        return <div className="row">
            <div className="col-md-5">
                <FilteredMultiSelect
                    buttonText="Add"
                    classNames={{
                    filter: 'form-control',
                    select: 'form-control',
                    button: 'btn btn btn-block btn-default',
                    buttonActive: 'btn btn btn-block btn-primary'
                    }}
                    onChange={this.handleSelect}
                    options={mimeTypesAvailable}
                    selectedOptions={this.state.selectedOptions}
                    textProp={handleName}
                    valueProp="name"
                    size="20"
                />
            </div>
            <div className="col-md-5">
                <FilteredMultiSelect
                    buttonText="Remove"
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
                    size="20"
                />
            </div>
            <div className="col-md-5">
                <button type="button"
                        onClick={() => {saveAllowedTypes(selectedOptions);}}>Aceptar
                </button>
            </div>
            <div className="col-md-5">Cancelar</div>
        </div>
    }

}