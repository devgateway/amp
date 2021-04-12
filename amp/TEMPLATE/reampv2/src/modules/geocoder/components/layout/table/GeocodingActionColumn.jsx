import React, {Component} from 'react';

import {faPencilAlt} from "@fortawesome/free-solid-svg-icons";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import Tooltip from "react-bootstrap/Tooltip";
import OverlayTrigger from "react-bootstrap/OverlayTrigger";

export default class GeocodingActionColumn extends Component {

    render() {
        let renderTooltip = props => (
            <Tooltip {...props}>{this.props.tooltip}</Tooltip>
        );

        if (this.props.enabled) {
            return (
                <OverlayTrigger placement="top" overlay={renderTooltip}>
                    <FontAwesomeIcon className={'fa-icon fa-lg'} icon={faPencilAlt}/>
                </OverlayTrigger>
            );
        }

        return (<div className={'no-locations'}>{this.props.message}</div>)
    }
}
