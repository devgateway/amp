import React, {Component} from 'react';

import {faPencilAlt} from "@fortawesome/free-solid-svg-icons";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";


export default class GeocodingActionColumn extends Component {

    render() {
        return (<td>
            <FontAwesomeIcon className={'fa-icon fa-lg'} icon={faPencilAlt}/>
        </td>);
    }
}
