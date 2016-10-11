import React from "react";
import NumericInput from "react-numeric-input";
import {Alert} from "react-bootstrap";
require('./style.less');

function formatPercentage(value) {
	return value + " %";
}

var Threshold = React.createClass({displayName: 'Threshold',
  getInitialState: function() {
  	this.value = this.props.from;
  	this.to = this.props.to;
  	return {valid : true, inputStyle : {}, alertVisible : false};
  },
  handleChange: function(valueAsNumber, valueAsString) {
    this.value = valueAsNumber;
  	this.props.onChange(this);
  },
  flagValidity: function(status) {
    let valid = status === Threshold.STATUS.OK;
    let inputStyle = valid ? {} : {input : {color :'red'}};
  	this.setState({
  		valid : valid, 
  		inputStyle : inputStyle,
  		alertVisible : !valid,
  		alertMessage: status
	});
  },
  alertDismiss: function() {
  	this.setState({alertVisible : false});
  },
  render : function () {
    this.__ = key => this.props.translations[key]; 
    var bgStyle = {
      color: this.props.color
    };
    var alert;
    if (this.state.alertVisible) {
		alert = 
      		<Alert ref="notUnique" className="undo-popup inputAlert" bsStyle="danger" bsClass="alert" onDismiss={this.alertDismiss}>
      			<span className="alertMsg">{this.__(this.state.alertMessage)}</span>
		    </Alert>;
    }
    return (
      <tr key={this.props.id}>
        <td>{this.props.index}</td>
      	<td style={bgStyle}><strong>{this.props.colorName}</strong></td>
      	<td>
      		<NumericInput value={this.value} min={0} max={100} onChange={this.handleChange} 
      			 className="threshold" style={this.state.inputStyle}/> 
      	    <span className="threshold"> %</span>
      	</td>
      	<td><span className="threshold thresholdFixed">{this.to} %</span></td>
      	<td>{alert}</td>
      </tr>
    )
  }
});

Threshold.STATUS = {
	OK: "ok",
	DUPLICATE: "amp.heat-map:not-unique",
	INVALID: "amp.heat-map:invalid"
}

Threshold.translations = {
  "amp.heat-map:not-unique": "Not Unique!",
  "amp.heat-map:invalid": "Invalid"
}

module.exports = Threshold;
