import React, { Component, PropTypes } from 'react';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import * as dataFreezeActions from '../actions/DataFreezeActions';
class UnfreezeAll extends Component {
     constructor(props, context) {
          super(props, context);
          this.state = {};
          this.unfreezeAll = this.unfreezeAll.bind(this);
     }

     componentWillMount() {
     }

     unfreezeAll() {
          if (confirm(this.props.translations['amp.data-freeze-event:unfreeze-all-confirm'])) {
               this.props.actions.unfreezeAll();
          }
     }

     render() {
          return (
              <div className="container-fluid">
                   <div className="text-center">
                        <img src="styles/images/alert.svg" className="system-icon"/>
                        <p className="padded">{this.props.translations['amp.data-freezing:unfreeze-all-desc']}</p>
                        <br/>
                        <button type="button" className="btn btn-danger"
                                onClick={this.unfreezeAll}>{this.props.translations['amp.data-freezing:unfreeze-all']}</button>
                        <div>
                             <div id="filter-popup"></div>
                             <div>
                                  <div className="row"><br/>
                                       <table className="table table-bordered table-striped data-table">
                                            <thead>
                                            <tr>
                                                 <th className="col-md-2">Data Freeze Date</th>
                                                 <th># of Activities</th>
                                            </tr>
                                            </thead>
                                            <tbody>
                                            <tr>
                                                 <td className="date-column">{this.props.frozenActivities.freezingDate}</td>
                                                 <td className="text-left">{this.props.frozenActivities.count}</td>
                                            </tr>
                                            </tbody>
                                       </table></div>
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
          errors: state.dataFreeze.errors || [],
          infoMessages: state.dataFreeze.infoMessages || [],
          frozenActivities: state.dataFreeze.frozenActivities
     }
}

function mapDispatchToProps(dispatch) {
     return {
          actions: bindActionCreators(Object.assign({}, dataFreezeActions), dispatch)
     }
}

export default connect(mapStateToProps, mapDispatchToProps)(UnfreezeAll);
