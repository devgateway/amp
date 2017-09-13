import React, { Component } from 'react';
import { Tooltip, OverlayTrigger } from 'react-bootstrap';
import { IMG_VALUE } from '../common/Constants';


export default class HeaderToolTip extends Component {

     constructor() {
          super();
     }

     getColumnTooltip() {
          const header = this.props.headers.filter((element) =>
              element.originalColumnName === String(this.props.column)
          );
          return header[0].description;
     }

     render() {
          const tooltip = <Tooltip id={this.props.column + '-icon-tooltip'}>{this.getColumnTooltip()}</Tooltip>;
          let imgSrc = 'images/icon-information.svg';
          if (this.props.imgType && this.props.imgType === IMG_VALUE) {
               imgSrc = 'images/icon-value.svg';
          }
          return (
              <OverlayTrigger trigger={['hover', 'focus']} placement="right" overlay={tooltip}>
                   <img className="table-icon" src={imgSrc}/>
              </OverlayTrigger>
          );
     }
}
