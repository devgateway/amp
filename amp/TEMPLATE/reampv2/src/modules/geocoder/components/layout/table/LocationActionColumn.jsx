import React, {Component} from 'react';

import {faCheckCircle, faTimesCircle} from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";

export default class LocationActionColumn extends Component {

    render() {

        const {
            handleAcceptLocation,
            handleRejectLocation
        } = this.props.handleLocation;

        return (
            <div className={'location-action'}>
                {this.props.location.accepted === true ? 'ACCEPTED' : this.props.location.accepted === false ? 'REJECTED'
                        :
                    <ul>
                        <li><a href="#" title="accept" onClick={e => handleAcceptLocation(e, this.props.location.id)}>
                                <FontAwesomeIcon className={'fa-icon fa-2x'} icon={faCheckCircle}/>
                                <span>accept</span>
                            </a>
                        </li>

                        <li><a href="#" title="reject" onClick={e => handleRejectLocation(e, this.props.location.id)}>
                                <FontAwesomeIcon className={'fa-icon fa-2x'} icon={faTimesCircle}/>
                                <span>reject</span>
                            </a>
                        </li>
                    </ul>}
            </div>
        );
    }
}