import React, { Component } from 'react';
import { Tooltip, OverlayTrigger } from 'react-bootstrap';


export default class Label extends Component {

  constructor(props) {
    super(props);
  }

  getContent() {
    const { label }  = this.props;
    const labelClass = (this.props.labelClass ? this.props.labelClass : '');
    return (<div className={labelClass} ><span>{label}</span>{this.props.separator ? <hr /> : ''}</div>);
  }

  tooltip() {
    const { tooltip } = this.props;
    return <Tooltip id="tooltip" >{tooltip}</Tooltip>;
  }

  render() {
    if (this.props.tooltip) {
      return (<OverlayTrigger placement="right" overlay={this.tooltip()} >
        {this.getContent()}
      </OverlayTrigger>);
    } else {
      return this.getContent();
    }
  }
}
