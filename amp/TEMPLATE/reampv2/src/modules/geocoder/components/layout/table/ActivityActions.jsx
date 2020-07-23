import React, {Component} from 'react';

import { faTimesCircle} from "@fortawesome/free-solid-svg-icons";
import { faPencilAlt} from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";



export default class TableCell extends Component {

    render() {
        return (<td>
            <FontAwesomeIcon className={'fa-icon'} icon={faPencilAlt}/>
            <FontAwesomeIcon className={'fa-icon'} icon={faTimesCircle}/>
        </td>);
    }
}
