import React, { Component, PropTypes } from 'react';
import {connect} from 'react-redux';
import {bindActionCreators} from 'redux';
import * as startUp from '../actions/StartUpAction.jsx';
export default class DataFreezingEventForm extends Component {    
    constructor(props, context) {      
        super(props, context);
        this.state = {};        
    }
    
    componentWillMount() {        
    }  
    
    render() {       
        return (
                <form>
                <table className="container-fluid data-selection-fields">
                <tbody>
                  <tr>
                    <td className="col-md-6">
                      <img className="tab-content-icon" src="styles/images/icon-information.svg"/>Sed maximus eros eget risus vehicula handrerit.

                    </td>
                    <td className="col-md-6">

                    </td>
                  </tr>

                  <tr>
                    <td className="col-md-6">
                      <img className="tab-content-icon" src="styles/images/icon-information.svg"/>
                      <span className="required">*</span> Data Freezing Date
                      <div className="input-group date pull-right col-md-7" data-provide="datepicker">
                        <input type="text" className="form-control"/>
                        <div className="input-group-addon">
                          <span className="glyphicon glyphicon-calendar"></span>
                        </div>
                      </div>
                    </td>
                    <td className="col-md-6">
                      <img className="tab-content-icon" src="styles/images/icon-information.svg"/>
                      <div className="input-group pull-right col-md-7">
                        <input type="text" className="form-control" aria-describedby="basic-addon2"/>
                        <span className="input-group-addon" id="basic-addon2">Days</span>
                      </div>
                      Grace Period
                    </td>
                  </tr>


                  <tr>
                    <td className="col-md-6">
                      <img className="tab-content-icon" src="styles/images/icon-information.svg"/>
                      <span className="required">*</span> Data Open Period Start
                      <div className="input-group date pull-right col-md-7" data-provide="datepicker">
                        <input type="text" className="form-control"/>
                        <div className="input-group-addon">
                          <span className="glyphicon glyphicon-calendar"></span>
                        </div>
                      </div>
                    </td>
                    <td className="col-md-6">
                      <img className="tab-content-icon" src="styles/images/icon-information.svg"/>
                      <div className="input-group pull-right col-md-7">
                        <div className="radio pull-left col-md-3">
                          <label>
                            <input type="radio" name="optradio"/>Yes</label>
                        </div>
                        <div className="radio pull-left col-md-3">
                          <label>
                            <input type="radio" name="optradio"/>No</label>
                        </div>
                      </div>
                      Notification Email
                    </td>
                  </tr>


                  <tr>
                    <td className="col-md-6">
                      <img className="tab-content-icon" src="styles/images/icon-information.svg"/>
                      <span className="required">*</span> Data Open Period End
                      <div className="input-group date pull-right col-md-7" data-provide="datepicker">
                        <input type="text" className="form-control"/>
                        <div className="input-group-addon">
                          <span className="glyphicon glyphicon-calendar"></span>
                        </div>
                      </div>
                    </td>
                    <td className="col-md-6">
                      <div className="input-group pull-right col-md-7">
                        <div className="radio pull-left col-md-6">
                          <label>
                            <input type="radio" name="optradio"/>Entire Activity</label>
                        </div>
                        <div className="radio pull-left col-md-3">
                          <label>
                            <input type="radio" name="optradio"/>Funding</label>
                        </div>
                      </div>
                      <div>
                        <img className="tab-content-icon" src="styles/images/icon-information.svg"/>
                        Freeze Options</div>

                    </td>
                  </tr>
                  <tr>
                    <td className="col-md-6" colSpan="2">
                      <img className="tab-content-icon" src="styles/images/icon-information.svg"/>
                      <span className="required">*</span> Filters
                      <button className="btn btn-default filter-add">
                        <span className="glyphicon glyphicon-plus-sign"></span>
                      </button>
                    </td>                        
                  </tr>
                  </tbody>
                </table>                          
                <button className="btn btn-success pull-right">Save Event</button>
              </form>
        );
    }
}

function mapStateToProps(state, ownProps) { 
    return {        
        translations: state.startUp.translations,
        translate: state.startUp.translate
    }
}

function mapDispatchToProps(dispatch) {
    return {actions: bindActionCreators({}, dispatch)}
}

export default connect(mapStateToProps, mapDispatchToProps)(DataFreezingEventForm);
