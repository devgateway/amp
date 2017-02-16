import React, { Component, PropTypes } from 'react';

export default class App extends Component {

    constructor(props, context) {
      
        super(props, context);
    }
   
    render() {        
        return (
            <div >
                <div>

                <ul className="nav nav-tabs" role="tablist">
                  <li role="presentation" className="active"><a href="#indicator1" aria-controls="indicator1" role="tab" data-toggle="tab">Indicator 1</a></li>
                  <li role="presentation"><a href="#indicator6" aria-controls="indicator6" role="tab" data-toggle="tab">Indicator 6</a></li>
                  
                </ul>

                <div className="tab-content panel">
                 <div role="tabpanel" className="tab-pane active" id="indicator1">
                  Indicator 1 Data
                     <table className="table table-striped">
                                    <thead>
                                      <tr>
                                        <th>Date</th>
                                        <th>Donor Agency</th>
                                        <th>Amount</th>
                                        <th>Currency</th>
                                        <th>Action</th>
                                      </tr>
                                    </thead>
                                    <tbody>
                                      <tr>
                                        <th scope="row">21/01/2017</th>
                                        <td>USAID</td>
                                        <td>$20,000,000</td>
                                        <td>USD</td>
                                        <td>EDI</td>                      
                                      </tr>
                                       <tr>
                                        <th scope="row">21/01/2017</th>
                                        <td>Australia</td>
                                        <td>$30,000,000</td>
                                        <td>USD</td>
                                        <td>EDI</td>                      
                                      </tr>
                                     
                                    </tbody>
                                  </table>
                  </div>
                      
                   <div role="tabpanel" className="tab-pane" id="indicator6">Indicator 6 Data</div>                  
                </div>

              </div>
                 
            </div>
        );
    }
}
