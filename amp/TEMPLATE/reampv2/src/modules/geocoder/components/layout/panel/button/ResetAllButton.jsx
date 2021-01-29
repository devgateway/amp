import React, {Component} from "react";
import Button from "react-bootstrap/Button";
import Modal from "react-bootstrap/Modal";
import {TranslationContext} from "../../../AppContext";
import {bindActionCreators} from "redux";
import {connect} from "react-redux";
import {resetAllLocationStatuses} from "../../../../actions/geocodingAction";

class ResetAllButton extends Component {

    constructor(props) {
        super(props);
        this.state = {show: false};

        this.handleClose = this.handleClose.bind(this);
        this.handleShow = this.handleShow.bind(this);
        this.onResetAll = this.onResetAll.bind(this);

    }

    handleClose() {
        this.setState({show: false});
    }

    handleShow() {
        this.setState({show: true});
    }

    onResetAll = (e) => {
        this.props.resetAllActivities();
        this.handleClose();
    };

    hasActivityAcceptedOrRejectedLocations(activity) {
        if (activity.locations.length == 0) {
            return false;
        }

        return activity.locations.filter(loc => loc.accepted != null).length > 0;
    }

    render() {
        const {translations} = this.context;

        const isEnabled = this.props.geocoding.activities
            .filter(activity => this.hasActivityAcceptedOrRejectedLocations(activity)).length > 0;

        return (
            <>
                <Button variant="primary" onClick={this.handleShow} disabled={!isEnabled}>
                    {this.props.title}
                </Button>

                <Modal show={this.state.show} onHide={this.handleClose} animation={false}>
                    <Modal.Header closeButton>
                        <Modal.Title>{this.props.title}</Modal.Title>
                    </Modal.Header>
                    <Modal.Body>All the geocoded locations would be reset.</Modal.Body>
                    <Modal.Footer>
                        <Button variant="secondary" onClick={this.handleClose}>
                            {translations['amp.geocoder:cancel']}
                        </Button>
                        <Button variant="primary" className={'button-header'} onClick={this.onResetAll}>
                            {this.props.title}
                        </Button>
                    </Modal.Footer>
                </Modal>
            </>
        );
    }
}

ResetAllButton.contextType = TranslationContext;

const mapStateToProps = state => {
    return {
        geocoding: state.geocodingReducer,
    };
};

const mapDispatchToProps = dispatch => bindActionCreators({
    resetAllActivities: resetAllLocationStatuses,
}, dispatch);

export default connect(mapStateToProps, mapDispatchToProps)(ResetAllButton);