import React, {Component} from "react";
import Button from "react-bootstrap/Button";
import Modal from "react-bootstrap/Modal";
import Tooltip from "react-bootstrap/Tooltip";
import OverlayTrigger from "react-bootstrap/OverlayTrigger";
import {TranslationContext} from "../../../AppContext";
import {bindActionCreators} from "redux";
import {connect} from "react-redux";
import {removeProject} from "../../../../actions/geocodingAction";
import {faTimesCircle} from "@fortawesome/free-solid-svg-icons";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";

class RemoveProjectButton extends Component {

    constructor(props) {
        super(props);
        this.state = {show: false};

        this.handleClose = this.handleClose.bind(this);
        this.handleShow = this.handleShow.bind(this);
        this.onRemoveProject = this.onRemoveProject.bind(this);
    }

    handleClose(e) {
        if (e && e.stopPropagation) {
            e.stopPropagation();
        }
        this.setState({show: false});
    }

    handleShow(e) {
        if (e && e.stopPropagation) {
            e.stopPropagation();
        }
        this.setState({show: true});
    }

    onRemoveProject = (e) => {
        this.props._removeProject(this.props.ampId);
        this.handleClose(e);
    }

    render() {
        const {translations} = this.context;

        let renderTooltip = props => (
            <Tooltip {...props}>{translations['amp.geocoder:removeProjectTooltip']}</Tooltip>
        );

        return (
            <>

                <OverlayTrigger placement="top" overlay={renderTooltip}>
                    <FontAwesomeIcon className={'fa-icon fa-lg'} icon={faTimesCircle} onClick={this.handleShow}/>
                </OverlayTrigger>

                <Modal show={this.state.show} onHide={this.handleClose} animation={false}>
                    <Modal.Header closeButton>
                        <Modal.Title>{translations['amp.geocoder:removeProject']}</Modal.Title>
                    </Modal.Header>
                    <Modal.Body>
                        <p>
                            {translations['amp.geocoder:removeProjectConfirmation']}
                        </p>
                        <div className="modal-activities">
                            <div>{translations['amp.geocoder:ampId']}: {this.props.ampId}</div>
                        </div>
                    </Modal.Body>
                    <Modal.Footer>
                        <Button variant="secondary" onClick={this.handleClose}>
                            {translations['amp.geocoder:cancel']}
                        </Button>
                        <Button variant="success" className={'button-header'} onClick={this.onRemoveProject}>
                            {translations['amp.geocoder:removeProject']}
                        </Button>
                    </Modal.Footer>
                </Modal>
            </>
        );
    }
}

RemoveProjectButton.contextType = TranslationContext;

const mapStateToProps = () => ({});

const mapDispatchToProps = dispatch => bindActionCreators({
    _removeProject: removeProject,
}, dispatch);

export default connect(mapStateToProps, mapDispatchToProps)(RemoveProjectButton);