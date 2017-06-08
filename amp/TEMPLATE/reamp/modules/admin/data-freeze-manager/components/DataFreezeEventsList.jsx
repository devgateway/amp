import React, { Component, PropTypes } from 'react';
import {connect} from 'react-redux';
import {bindActionCreators} from 'redux';
import * as startUp from '../actions/StartUpAction';
import * as dataFreezeActions from '../actions/DataFreezeActions';
import * as Constants from '../common/Constants';
require('../styles/less/main.less');
export default class DataFreezeEventsList extends Component {    
    constructor(props, context) {      
        super(props, context);
        this.state = {};  
        this.showFreezeOption = this.showFreezeOption.bind(this);
    }
    
    componentWillMount() {  
        this.props.actions.loadDataFreezeEventsList({paging: this.props.paging, sorting: this.props.sorting});
    } 
    
    showFreezeOption(freezeOption) {
        let result = '';
        if (freezeOption === Constants.FREEZE_OPTION_ENTIRE_ACTIVITY) {             
            result = this.props.translations['amp.data-freezing:freeze-option-activity'];
        } else if (freezeOption === Constants.FREEZE_OPTION_FUNDING) {
            result = this.props.translations['amp.data-freezing:freeze-option-funding'];
        }
        
        return result;      
    }
    
    render() {       
        
        return (
                <div>
                <div className="container padded30">
                <div className="row">
                  <div className="col-md-12 bordered-content">
                    <table className="table table-bordered table-striped data-table">
                      <thead>
                        <tr>
                          <th className="col-md-2">{this.props.translations['amp.data-freezing:data-freeze-date']}</th>
                          <th>{this.props.translations['amp.data-freezing:grace-period']}</th>
                          <th>{this.props.translations['amp.data-freezing:open-period-start']}</th>
                          <th>{this.props.translations['amp.data-freezing:open-period-end']}</th>
                          <th>{this.props.translations['amp.data-freezing:number-of-activities']}</th>
                          <th>{this.props.translations['amp.data-freezing:freeze-options']}</th>
                          <th>{this.props.translations['amp.data-freezing:filters']}</th>
                          <th></th>
                          <th></th>
                        </tr>
                      </thead>
                      <tbody>
                        {this.props.dataFreezeEventsList.map(event => 
                              <tr >
                              <td>{event.freezingDate}</td>
                              <td>{event.gracePeriod}</td>
                              <td>{event.openPeriodStart}</td>
                              <td>{event.openPeriodEnd}</td>
                              <td></td>
                              <td>{this.showFreezeOption(event.freezeOption)}</td>
                              <td>
                                <span className="filter">Filter name 1, Filter name 2</span>
                              </td>
                              <td className="action">
                                  <img className="tab-content-icon" src="styles/images/edit.svg"/>
                              </td>
                              <td className="action">
                                <img className="tab-content-icon" src="styles/images/delete.svg"/>
                              </td>

                            </tr>    
                        )}                   
                      
                      </tbody>
                    </table>



                  </div>
                </div>
              </div>
               
                          <div className="container">
                          <div className="row">
                            <div className="col-md-8 pull-right pagination-wrapper">

                              <div className="col-md-4">
                                <ul className="pagination">
                                  <li className="active page-item page-item"><a href="#" className="page-link">1</a>
                                  </li>
                                  <li className="page-item"><a href="#" className="page-link">2</a>
                                  </li>
                                  <li className="page-item"><a href="#" className="page-link">3</a>
                                  </li>
                                  <li className="page-item"><a href="#" className="page-link">4</a>
                                  </li>
                                  <li className="next"><a href="#"><span aria-hidden="true">&rarr;</span></a>
                                  </li>
                                  <li className="page-item"><a href="#" className="page-link">&raquo;</a>
                                  </li>
                                </ul>
                              </div>

                              <div className="col-md-2">
                                <div className="input-group pull-right">
                                  <span className="input-group-addon" id="basic-addon1">
                                    <span className="glyphicon glyphicon-arrow-right"></span>
                                  </span>
                                  <input type="text" className="form-control" placeholder=""/>
                                </div>
                              </div>


                              <div className="col-md-2">
                                <div className="input-group pull-right">
                                  <span className="input-group-addon" id="basic-addon1">
                                    <span className="glyphicon glyphicon-th-list"></span>
                                  </span>
                                  <input type="text" className="form-control" placeholder=""/>
                                </div>
                              </div>


                              <div className="col-md-3 pull-right record-number">
                                <div>1-10 of 200 records</div>
                                <div>(p 1/20)</div>
                              </div>

                            </div>
                          </div>
                        </div>
                     </div>
        );
    }
}

function mapStateToProps(state, ownProps) { 
    return {        
        translations: state.startUp.translations,
        translate: state.startUp.translate,
        paging: state.dataFreeze.paging,
        sorting: state.dataFreeze.sorting,
        dataFreezeEventsList: state.dataFreeze.dataFreezeEventsList
    }
}

function mapDispatchToProps(dispatch) {
    return {actions: bindActionCreators(Object.assign({}, dataFreezeActions), dispatch)}
}

export default connect(mapStateToProps, mapDispatchToProps)(DataFreezeEventsList);
