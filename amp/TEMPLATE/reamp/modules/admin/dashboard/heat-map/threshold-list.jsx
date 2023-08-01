import * as AMP from "amp/architecture";
import React from "react";
import ThresholdState from "./threshold-state.jsx";
import Threshold from "./threshold.jsx";
import {Alert, Button, Glyphicon} from "react-bootstrap";
import {delay, postJson} from "amp/tools";
import {HEAT_MAP_ADMIN} from "amp/config/endpoints";

const ALERT_TYPE = {
  NONE: "none",
  SUCCESS: "success",
  ERROR: "danger"
};

var ThresholdList = React.createClass({displayName: 'Threshold List',
  getInitialState: function() {
    return {data : {}, valid: true, submitting: false, alert: ALERT_TYPE.NONE};
  },
  componentWillReceiveProps: function(nextProps) {
  	this.thresholdState = new ThresholdState(nextProps, this.refs);
  },
  _getIndexSuffix: function(current) {
  	switch(current) {
  	  case 0: return " (" + this.__('amp.heat-map:lowest') + ")";
  	  case this.props.data.length - 1: return " (" + this.__('amp.heat-map:highest')  + ")";
  	  default: return "";
  	}
  },
  render: function() {
    this.__ = key => this.props.translations[key];
    let thresholdNodes = this.props.data.map(function(threshold, index) {
      index = (index + 1) + this._getIndexSuffix(index);
      return (
        <Threshold ref={threshold.id} id={threshold.id} index={index}
        	colorName={threshold.name} color={threshold.color}
        	from={threshold.amountFrom} to={threshold.to} 
        	onChange={this.onChildChanged} translations={this.props.translations}/>
      );
    }.bind(this));
    var infoAlert;
    if (this.state.alert !== ALERT_TYPE.NONE) {
    	infoAlert = this.getAlert();
	}
	
    return (
    	<table className="table table-striped">
        	<caption>
	          	<h2>{this.__('amp.heat-map:threshold-settings')}</h2>
	            <Alert><Glyphicon glyph="info-sign"/> {this.__('amp.heat-map:howTo')}</Alert>
        	</caption>
        	<thead>
		        <tr>
		          <th width="20%">{this.__('amp.heat-map:threshold-range')}</th>	
		          <th width="20%">{this.__('amp.heat-map:color')}</th>
		          <th width="20%">{this.__('amp.heat-map:from')}</th>
		          <th width="20%">{this.__('amp.heat-map:to')}</th>
		          <th width="20%"/>
		        </tr>
		    </thead>
		    <tbody>
		    	{thresholdNodes}
		    </tbody>
		    <tfoot>
		    	<tr>
		    		<td colSpan="5">
		    		    <Button bsStyle="primary" onClick={this.handleSubmit} disabled={!this.state.valid || this.state.submitting}>
		    		    	{this.__('amp.heat-map:save')}
		    		    </Button>
			    		<p/>
	        			{infoAlert}
	        		</td>
	        	</tr>
		    </tfoot>
		</table>
    );
  },
  getAlert: function() {
  	let isSuccess = this.state.alert === ALERT_TYPE.SUCCESS; 
  	if (isSuccess) {
  		delay(2000).then(this.hideAlert);
  	}
  	return (
  		<Alert ref="errorAlert" bsStyle={this.state.alert} className="resultAlert" bsClass="alert" onDismiss={this.hideAlert}>
  			{isSuccess ? this.__('amp.heat-map:success') : this.state.alertMsg}
		</Alert>);
  },
  hideAlert: function() {
  	this.setState({alert: ALERT_TYPE.NONE});
  },
  onChildChanged: function(elem) {
  	let valid = this.thresholdState.validateThresholdChange(elem, this.refs);
  	this.updateRangeAlert();
  	this.setState({valid : valid});
  },
  updateRangeAlert: function() {
    if (this.thresholdState.getInvalidRanges().size === 0) {
      this.hideAlert();
  	} else {
  	  let rangeErrors = new Array();
  	  // 0 based index, expecting any except the last
  	  for(let index of this.thresholdState.getInvalidRanges()) {
  	    let errorMsg = <div>
  	      {this.__('amp.heat-map:range-must-be').replace("{0}", (index + 1)) + " " +
  	      (index == 0 ? "" : this.__('amp.heat-map:higher') + " #" + index + " " + this.__('amp.heat-map:and') + " ") +
  	      this.__('amp.heat-map:lower') + " #" + (index + 2) + "."}
  	      </div>;
  	    rangeErrors.push(errorMsg);
  	  }
  	  let alertMsg = <div>
  	  	{this.__('amp.heat-map:invalid-ranges')}
  	  	{rangeErrors}
  	  	</div>;
  	  this.setState({
  	  	alert: ALERT_TYPE.ERROR,
  	  	alertMsg: alertMsg 
  	  });
  	}
  },
  handleSubmit: function(e) {
    e.preventDefault();
    this.setState({submitting : true});
    for(let threshold of this.props.data) {
    	threshold.amountFrom = this.thresholdState.getIdValue().get(threshold.id);
    }
    let newSettings = new AMP.Model({'amountColors' : this.props.data});
    postJson(HEAT_MAP_ADMIN, newSettings.toJS()).then(result => this.processResult(result));
    console.log('submit new data');
  },
  processResult: function(result) {
  	//console.log('process submit result');
  	this.setState({submitting : false});
  	if (result.status < 200 || result.status >= 300) {
  		result['json']().then(r => this.processFailureResult(r));
    } else {
    	this.setState({alert: ALERT_TYPE.SUCCESS});
    }
  },
  processFailureResult: function(reason) {
  	this.setState({submitting : false});
  	let data = new AMP.Model(reason);
  	let errorMsg = "";
  	for (let value of data.entries()) {
  		errorMsg += Object.values(value).join(". ");
  	}
  	this.setState({alertMsg: errorMsg, alert: ALERT_TYPE.ERROR});
  	console.log('reason = ' + errorMsg);
  }
  
});

ThresholdList.translations = {
  ...Threshold.translations,
  "amp.heat-map:howTo": "Listed colors will be used to highlight the amounts within specified ordered ranges",
  "amp.heat-map:invalid-ranges": "Threshold ranges are incorrectly ordered:",
  "amp.heat-map:range-must-be": "Range #{0} must be",
  "amp.heat-map:higher": "higher than",
  "amp.heat-map:and": "and",
  "amp.heat-map:lower": "lower than",
  "amp.heat-map:lowest": "lowest",
  "amp.heat-map:highest": "highest",
  "amp.heat-map:save": "Save",
  "amp.heat-map:success": "Success",
  "amp.heat-map:threshold-settings": "Threshold Settings",
  "amp.heat-map:threshold-range": "Threshold range",
  "amp.heat-map:from": "From",
  "amp.heat-map:to": "To",
  "amp.heat-map:color": "Color"
};

module.exports = ThresholdList;