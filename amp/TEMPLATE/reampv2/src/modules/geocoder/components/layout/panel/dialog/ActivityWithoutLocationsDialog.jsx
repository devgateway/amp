import React, {Component} from "react";
import Button from "react-bootstrap/Button";
import Modal from "react-bootstrap/Modal";
import {TranslationContext} from "../../../AppContext";
import {bindActionCreators} from "redux";
import {connect} from "react-redux";
import AlertError from "../AlertError";
import {Loading} from "../../../../../../utils/components/Loading";
import {cancelGeocoding} from "../../../../actions/geocodingAction";

class ActivityWithoutLocationsDialog extends Component {

    constructor(props) {
        super(props);
        this.state = {show: true};

        this.handleClose = this.handleClose.bind(this);
        this.onDiscardChanges = this.onDiscardChanges.bind(this);

    }

    handleClose() {
        this.setState({show: false});
    }

    onDiscardChanges = (e) => {
        this.props.cancelGeocoding();
        this.handleClose();
    };

    render() {
        const {translations} = this.context;

        return (
            <>
                <Modal show={this.state.show} onHide={this.handleClose} animation={false}>
                    <Modal.Header closeButton>
                        <Modal.Title>{this.props.title}</Modal.Title>
                    </Modal.Header>
                    <Modal.Body>{translations['amp.geocoder:discardGeocodingText']}</Modal.Body>
                    <Modal.Footer>
                        <Button variant="secondary" onClick={this.handleClose}>
                            {translations['amp.geocoder:cancel']}
                        </Button>
                        <Button variant="primary" className={'button-header'} onClick={this.onDiscardChanges}>
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

ActivityWithoutLocationsDialog.contextType = TranslationContext;

const mapStateToProps = state => {
    return {
        geocoding: state.geocodingReducer,
    };
};

const mapDispatchToProps = dispatch => bindActionCreators({
    cancelGeocoding: cancelGeocoding,
}, dispatch);

export default connect(mapStateToProps, mapDispatchToProps)(ActivityWithoutLocationsDialog);