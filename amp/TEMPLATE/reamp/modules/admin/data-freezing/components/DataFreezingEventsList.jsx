import React, { Component, PropTypes } from 'react';
import {connect} from 'react-redux';
import {bindActionCreators} from 'redux';
import * as startUp from '../actions/StartUpAction.jsx';
export default class DataFreezingEventsList extends Component {    
    constructor(props, context) {      
        super(props, context);
        this.state = {};        
    }
    
    componentWillMount() {        
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
                          <th className="col-md-2">Data Freeze Date</th>
                          <th>Grace Period</th>
                          <th>Open Period Start</th>
                          <th>Open Period End</th>
                          <th># of Activities</th>
                          <th>Freeze Options</th>
                          <th>Filters</th>
                          <th></th>
                          <th></th>
                        </tr>
                      </thead>
                      <tbody>
                        <tr>
                          <td>2017-3-28</td>
                          <td>15</td>
                          <td>2017-3-25</td>
                          <td>45</td>
                          <td>2017-4-5</td>
                          <td>Entire Activity</td>
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
                        <tr>
                          <td>2017-2-28</td>
                          <td>6</td>
                          <td>2017-2-25</td>
                          <td>5</td>
                          <td>2017-2-4</td>
                          <td>Funding</td>
                          <td><span className="filter">First Name, other, Last Name</span></td>
                          <td className="action">
                              <img className="tab-content-icon" src="styles/images/edit.svg"/>
                          </td>
                          <td className="action">
                            <img className="tab-content-icon" src="styles/images/delete.svg"/>
                          </td>

                        </tr>
                        <tr>
                          <td>2017-3-25</td>
                          <td>7</td>
                          <td>2017-3-13</td>
                          <td>16</td>
                          <td>2017-1-1</td>
                          <td>Funding</td>
                          <td><span className="filter">Filter name 1, Filter name 2</span></td>
                          <td>
                              <img className="tab-content-icon" src="styles/images/edit.svg"/>
                          </td>
                          <td>
                            <img className="tab-content-icon" src="styles/images/delete.svg"/>
                          </td>
                        </tr>
                        <tr>
                          <td>2017-2-05</td>
                          <td>21</td>
                          <td>2017-3-15</td>
                          <td>23</td>
                          <td>2017-2-13</td>
                          <td>Entire Activity</td>
                          <td><span className="filter">Filter name, Name 1, Name 2</span></td>
                          <td>
                              <img className="tab-content-icon" src="styles/images/edit.svg"/>
                          </td>
                          <td>
                            <img className="tab-content-icon" src="styles/images/delete.svg"/>
                          </td>
                        </tr>
                        <tr>
                          <td>2017-2-02</td>
                          <td>9</td>
                          <td>2017-3-13</td>
                          <td>88</td>
                          <td>2017-3-13</td>
                          <td>Entire Activity</td>
                          <td><span className="filter">First Name, Last Name, other filter</span></td>
                          <td>
                              <img className="tab-content-icon" src="styles/images/edit.svg"/>
                          </td>
                          <td>
                              <img className="tab-content-icon" src="styles/images/delete.svg"/>
                          </td>
                        </tr>
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
        translate: state.startUp.translate
    }
}

function mapDispatchToProps(dispatch) {
    return {actions: bindActionCreators({}, dispatch)}
}

export default connect(mapStateToProps, mapDispatchToProps)(DataFreezingEventsList);
