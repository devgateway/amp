import * as AMP from "amp/architecture";
import React from "react";
import Threshold from "./threshold.jsx";
import {Alert, Button} from "react-bootstrap";
import {postJson, delay} from "amp/tools";
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
  	let map = new Map();
    let idValue = nextProps.data.reduce(function(map, threshold) {
    	map.set(threshold.id, threshold.amountFrom)
    	return map;
    }, map);
    this.idValue = idValue;
    this.invalidIds = new Set([]);
  },
  render: function() {
    this.__ = key => this.props.translations[key];
  	let thresholdNodes = this.props.data.map(function(threshold) {
  	  return (
        <Threshold ref={threshold.id} id={threshold.id} colorName={threshold.name} color={threshold.color} from={threshold.amountFrom} 
        	onChange={this.onChildChanged} translations={this.props.translations}/>
      );
    }.bind(this));
    var infoAlert;
    if (this.state.alert !== ALERT_TYPE.NONE) {
    	infoAlert = this.getAlert();
	}
	this.data = this.props.data;
    
    return (
    	<table className="table table-striped">
        	<caption>
	          	<h2>
	            	{this.__('amp.heat-map:threshold-settings')}
	          	</h2>
        	</caption>
        	<thead>
		        <tr>
		          <th width="33%">{this.__('amp.heat-map:color')}</th>
		          <th width="33%">{this.__('amp.heat-map:from')} {this.__('amp.heat-map:threshold')}</th>
		          <th width="34%"/>
		        </tr>
		    </thead>
		    <tbody>
		    	{thresholdNodes}
		    </tbody>
		    <tfoot>
		    	<tr>
		    		<td colSpan="3">
		    		    <Button bsStyle="success" onClick={this.handleSubmit} disabled={!this.state.valid || this.state.submitting}>
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
      		<span className="alertMsg" >{isSuccess ? this.__('amp.heat-map:success') : this.state.alertMsg}</span>
		</Alert>);
  },
  hideAlert: function() {
  	this.setState({alert: ALERT_TYPE.NONE});
  },
  onChildChanged: function(elem) {
    let invalidCount = 0;
    invalidCount += this.validateChild(elem)
    this.invalidIds.forEach(id => invalidCount += this.validateChild(this.refs[id]));
    let valid = invalidCount === 0;
  	this.setState({valid : valid});
  	//console.log('onChildChanged -> ' + elem.props.id + ', with value = ' + elem.value)
  },
  validateChild: function(elem) {
    this.idValue.set(elem.props.id, elem.value);
    let sameValueElements = new Map([...this.idValue].filter(([key, value]) => value == elem.value)); 
    if (sameValueElements.size > 1) {
    	//console.log('Another element exists with the same value!');
    	for (let [key, value] of sameValueElements) {
    		this.refs[key].flagValidity(Threshold.STATUS.DUPLICATE);
    		this.invalidIds.add(key);
    	}
    	return sameValueElements.size;
    } else if (elem.value === null) {
    	this.refs[elem.props.id].flagValidity(Threshold.STATUS.INVALID);
    	return 1;
    } else {
    	this.invalidIds.delete(elem.props.id);
    	this.refs[elem.props.id].flagValidity(Threshold.STATUS.OK);
    	return 0;
    }
  },
  handleSubmit: function(e) {
    e.preventDefault();
    this.setState({submitting : true});
    for(let threshold of this.props.data) {
    	threshold.amountFrom = this.idValue.get(threshold.id);
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
  "amp.heat-map:save": "Save",
  "amp.heat-map:success": "Success",
  "amp.heat-map:threshold-settings": "Threshold Settings",
  "amp.heat-map:threshold": "Threshold",
  "amp.heat-map:from": "From",
  "amp.heat-map:color": "Color"
};

module.exports = ThresholdList;

