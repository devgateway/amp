import React, {Component} from "react";
import Button from "react-bootstrap/Button";
import Modal from "react-bootstrap/Modal";
import Tooltip from "react-bootstrap/Tooltip";
import OverlayTrigger from "react-bootstrap/OverlayTrigger";
import {TranslationContext} from "../../../AppContext";
import {bindActionCreators} from "redux";
import {connect} from "react-redux";
import {saveAllEdits} from "../../../../actions/geocodingAction";

class SaveAllEditsButton extends Component {

    constructor(props) {
        super(props);
        this.state = {show: false};

        this.handleClose = this.handleClose.bind(this);
        this.handleShow = this.handleShow.bind(this);
        this.onSaveAllEdits = this.onSaveAllEdits.bind(this);
    }

    handleClose() {
        this.setState({show: false});
    }

    handleShow() {
        this.setState({show: true});
    }

    onSaveAllEdits = () => {
        const ampIds = this.props.geocoding.activities
            .filter(activity => activity.locations.length > 0)
            .filter(activity => activity.locations.filter(loc => loc.accepted !== false && loc.accepted !== true).length < 1)
            .map(act => act.amp_id);
        this.props.saveAllEdits(ampIds);
        this.handleClose();
    }

    hasActivityAcceptedOrRejectedAllLocations(activity) {
        if (activity.locations.length === 0) {
            return false;
        }

        return activity.locations.filter(loc => loc.accepted === null).length === 0;
    }

    render() {
        const {translations} = this.context;

        const isEnabled = this.props.geocoding.activities
            .filter(activity => this.hasActivityAcceptedOrRejectedAllLocations(activity)).length > 0;

        let renderTooltip = props => (
            <Tooltip {...props}>{this.props.tooltip}</Tooltip>
        );

        return (
            <>

                <OverlayTrigger placement="top" overlay={renderTooltip}>
                    <Button variant="success"
                            className={'pull-right button-header'}
                            disabled={!isEnabled} onClick={this.handleShow}>{this.props.title}
                    </Button>
                </OverlayTrigger>

                <Modal show={this.state.show} onHide={this.handleClose} animation={false}>
                    <Modal.Header closeButton>
                        <Modal.Title>{this.props.title}</Modal.Title>
                    </Modal.Header>
                    <Modal.Body>{translations['amp.geocoder:saveAllEditsConfirmation']}</Modal.Body>
                    <Modal.Footer>
                        <Button variant="secondary" onClick={this.handleClose}>
                            {translations['amp.geocoder:cancel']}
                        </Button>
                        <Button variant="success" className={'button-header'} onClick={this.onSaveAllEdits}>
                            {this.props.title}
                        </Button>
                    </Modal.Footer>
                </Modal>
            </>
        );
    }
}

SaveAllEditsButton.contextType = TranslationContext;

const mapStateToProps = state => {
    return {
        geocoding: state.geocodingReducer,
    };
};

const mapDispatchToProps = dispatch => bindActionCreators({
    saveAllEdits: saveAllEdits,
}, dispatch);

export default connect(mapStateToProps, mapDispatchToProps)(SaveAllEditsButton);