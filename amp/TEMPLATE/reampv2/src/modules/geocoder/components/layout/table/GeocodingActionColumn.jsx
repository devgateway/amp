import React, {Component} from 'react';

import {faPencilAlt} from "@fortawesome/free-solid-svg-icons";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";

export default class GeocodingActionColumn extends Component {

    render() {
        if (this.props.enabled) {
            return (
                <FontAwesomeIcon className={'fa-icon fa-lg'} icon={faPencilAlt}/>
            );
        }

        return (<div className={'no-locations'}>{this.props.message}</div>)
    }
}
