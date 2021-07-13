import React, {Component} from "react";
import Button from "react-bootstrap/Button";
import Modal from "react-bootstrap/Modal";
import {TranslationContext} from "../../../AppContext";
import {bindActionCreators} from "redux";
import {connect} from "react-redux";
import AlertError from "../AlertError";
import {Loading} from "../../../../../../utils/components/Loading";
import {cancelGeocoding} from "../../../../actions/geocodingAction";

class CancelGeocodingButton extends Component {

    constructor(props) {
        super(props);
        this.state = {show: false};

        this.handleClose = this.handleClose.bind(this);
        this.handleShow = this.handleShow.bind(this);
        this.onCancelGeocoding = this.onCancelGeocoding.bind(this);

    }

    handleClose() {
        this.setState({show: false});
    }

    handleShow() {
        this.setState({show: true});
    }

    onCancelGeocoding = (e) => {
        this.props.cancelGeocoding();
        this.handleClose();
    };

    render() {
        const {translations} = this.context;

        return (
            <>
                <Button variant="warning" onClick={this.handleShow}>{this.props.title}</Button>

                <Modal show={this.state.show} onHide={this.handleClose} animation={false}>
                    <Modal.Header closeButton>
                        <Modal.Title>{this.props.title}</Modal.Title>
                    </Modal.Header>
                    <Modal.Body>{translations['amp.geocoder:cancelGeocodingConfirmation']}</Modal.Body>
                    <Modal.Footer>
                        <Button variant="secondary" onClick={this.handleClose}>
                            {translations['amp.geocoder:cancel']}
                        </Button>
                        <Button variant="primary" className={'button-header'} onClick={this.onCancelGeocoding}>
                            {this.props.title}
                        </Button>
                        {this.props.geocoding.pending && <Loading/>}
                        {this.props.geocoding.error && <AlertError error={this.props.geocoding.error}/>}
                    </Modal.Footer>
                </Modal>
            </>
        );
    }
}

CancelGeocodingButton.contextType = TranslationContext;

const mapStateToProps = state => {
    return {
        geocoding: state.geocodingReducer,
    };
};

const mapDispatchToProps = dispatch => bindActionCreators({
    cancelGeocoding: cancelGeocoding,
}, dispatch);

export default connect(mapStateToProps, mapDispatchToProps)(CancelGeocodingButton);