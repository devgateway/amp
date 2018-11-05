import React, { Component } from 'react';
require('../../styles/ActivityView.css');

/**
 *    
 */
export default class TopIcon extends Component {

    render() {
        const {link, img, label, target, tooltip} = this.props;

        return (
        <div className={'preview_icons'}>
            <a href={link} title={label || tooltip} target={target}>
                <img src={img}/>{label}
            </a>
        </div>);
      }
}