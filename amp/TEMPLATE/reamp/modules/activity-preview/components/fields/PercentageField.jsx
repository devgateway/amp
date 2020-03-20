import React, { Component } from 'react';
import NumberUtils from '../../utils/NumberUtils';
require('../../styles/ActivityView.css');


export default class PercentageField extends Component {

  constructor(props) {
    super(props);
  }

  render() {
    const percentage = this.props.value !== undefined && this.props.value !== null
      ? `${NumberUtils.rawNumberToFormattedString(this.props.value, true, this.props.settings)}%` : null;
    return (<div>
      <span className={this.props.titleClass}>{this.props.title} </span>
      <span className={`${this.props.valueClass} ${'alignRight'}`}>{percentage}</span>
    </div>);
  }
}
