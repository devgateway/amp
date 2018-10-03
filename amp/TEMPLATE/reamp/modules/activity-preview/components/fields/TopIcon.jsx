import React, { Component } from 'react';
require('../../styles/ActivityView.css');

/**
 *    
 */
export default class TopIcon extends Component {

    render() {
        const {link, img, label, target} = this.props;

        return (
        <div className={'preview_icons'}>
            <a href={link} title={label} target={target}>
                <img src={img}/>{label}
            </a>
        </div>);
      }
}