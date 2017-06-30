import React, { Component } from 'react';
import { Tooltip, OverlayTrigger } from 'react-bootstrap';


export default class HeaderToolTip extends Component {

     constructor() {
          super();
     }
     getColumnTooltip() {
          const header = this.props.headers.filter((element) =>
              element.originalColumnName === this.props.column
          );
          return header[0].description;
     }

     render() {
          const tooltip = <Tooltip id={this.props.column + '-icon-tooltip'}>{this.getColumnTooltip()}</Tooltip>;
          return (
              <OverlayTrigger trigger={['hover', 'focus']} placement="right" overlay={tooltip}>
                   <img className="table-icon" src="images/icon-information.svg"/>
              </OverlayTrigger>
          );
     }
}
