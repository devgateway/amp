import React, {Component} from 'react';
import {OverlayTrigger, Tooltip} from 'react-bootstrap';
import {IMG_VALUE} from '../common/Constants';


class HeaderToolTip extends Component {

     constructor() {
          super();
     }

     getColumnTooltip() {
         if (this.props.tooltip) {
             return this.props.tooltip;
         }
         const header = this.props.headers.filter((element) =>
              element.originalColumnName === String(this.props.column)
          );
          return header[0].description;
     }

     getImageSrc() {
         if (this.props.imgSrc) {
             return this.props.imgSrc;
         }
         let imgSrc = 'images/icon-information.svg';
         if (this.props.imgType && this.props.imgType === IMG_VALUE) {
              imgSrc = 'images/icon-value.svg';
         }
         return imgSrc;
     }

     render() {
          const tooltip = <Tooltip id={this.props.column + '-icon-tooltip'}>{this.getColumnTooltip()}</Tooltip>;
          return (
              <OverlayTrigger trigger={['hover', 'focus']} placement="right" overlay={tooltip}>
                   <img className="table-icon" src={this.getImageSrc()}/>
              </OverlayTrigger>
          );
     }
}
