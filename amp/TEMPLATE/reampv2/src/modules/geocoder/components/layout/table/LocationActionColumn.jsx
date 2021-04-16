import React, {Component} from 'react';

import {faCheckCircle, faTimesCircle} from "@fortawesome/free-solid-svg-icons";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import Tooltip from "react-bootstrap/Tooltip";
import OverlayTrigger from "react-bootstrap/OverlayTrigger";
import {TranslationContext} from "../../AppContext";
import {bindActionCreators} from "redux";
import {connect} from "react-redux";

class LocationActionColumn extends Component {

    render() {

        const {
            handleAcceptLocation,
            handleRejectLocation
        } = this.props.handleLocation;

        const {translations} = this.context;

        let renderAcceptTooltip = props => (
            <Tooltip {...props}>{translations['amp.geocoder:locationAcceptTooltip']}</Tooltip>
        );

        let renderRejectTooltip = props => (
            <Tooltip {...props}>{translations['amp.geocoder:locationRejectTooltip']}</Tooltip>
        );

        return (
            <div className={'location-action'}>
                {this.props.location.accepted === true ? 'ACCEPTED' : this.props.location.accepted === false ? 'REJECTED'
                        :
                    <ul>
                        <li>
                            <OverlayTrigger placement="top" overlay={renderAcceptTooltip}>
                                <a href="#" title="accept" onClick={e => handleAcceptLocation(e, this.props.location.id)}>
                                    <FontAwesomeIcon className={'fa-icon fa-2x'} icon={faCheckCircle}/>
                                    <span>{translations['amp.geocoder:locationAccept']}</span>
                                </a>
                            </OverlayTrigger>
                        </li>

                        <li>
                            <OverlayTrigger placement="top" overlay={renderRejectTooltip}>
                                <a href="#" title="reject" onClick={e => handleRejectLocation(e, this.props.location.id)}>
                                    <FontAwesomeIcon className={'fa-icon fa-2x'} icon={faTimesCircle}/>
                                    <span>{translations['amp.geocoder:locationReject']}</span>
                                </a>
                            </OverlayTrigger>
                        </li>
                    </ul>}
            </div>
        );
    }
}


LocationActionColumn.contextType = TranslationContext;

function mapStateToProps(state, ownProps) {
    return {}
}

const mapDispatchToProps = dispatch => bindActionCreators({}, dispatch);

export default connect(mapStateToProps, mapDispatchToProps)(LocationActionColumn);