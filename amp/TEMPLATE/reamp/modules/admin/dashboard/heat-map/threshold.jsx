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
      <tr className="colorThreshold" key={this.props.id}>
      	<td className="title-and-desc" style={bgStyle}><strong>{this.props.colorName}</strong></td>
      	<td>
      		<NumericInput value={this.value} min={0} max={100} format={formatPercentage} onChange={this.handleChange} style = {this.state.inputStyle}/>
      	</td>
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
