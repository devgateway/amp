import React, {Component} from "react";
import Button from "react-bootstrap/Button";
import Tooltip from "react-bootstrap/Tooltip";
import OverlayTrigger from "react-bootstrap/OverlayTrigger";
import Modal from "react-bootstrap/Modal";
import {TranslationContext} from "../../../AppContext";
import {bindActionCreators} from "redux";
import {connect} from "react-redux";
import {runSearch} from "../../../../actions/geocodingAction";

class RunSearchButton extends Component {

    constructor(props) {
        super(props);
        this.state = {show: false};

        this.handleClose = this.handleClose.bind(this);
        this.handleShow = this.handleShow.bind(this);
        this.onRunSearch = this.onRunSearch.bind(this);
    }

    handleClose() {
        this.setState({show: false});
    }

    handleShow() {
        this.setState({show: true});
    }

    onRunSearch = () => {
        this.props.runSearch(this.props.selectedActivities);
        this.handleClose();
    }

    render() {
        const {translations} = this.context;

        let renderTooltip = props => (
            <Tooltip {...props}>{this.props.tooltip}</Tooltip>
        );

        return (
            <>
                <OverlayTrigger placement="top" overlay={renderTooltip}>
                    <Button variant="success"
                            className={'pull-right button-header'}
                            disabled={this.props.selectedActivities.length < 1 || this.props.activitiesPending} onClick={this.handleShow}>{this.props.title}
                    </Button>
                </OverlayTrigger>

                <Modal show={this.state.show} onHide={this.handleClose} animation={false}>
                    <Modal.Header closeButton>
                        <Modal.Title>{this.props.title}</Modal.Title>
                    </Modal.Header>
                    <Modal.Body>
                        {translations['amp.geocoder:runSearchText']}:
                        <div className="modal-activities">
                            {this.props.activities
                                .filter(act => this.props.selectedActivities.includes(act.id))
                                .map((value) => {
                                return <div key={value.col2}>{value.col2}</div>
                            })}
                        </div>
                        </Modal.Body>
                    <Modal.Footer>
                        <Button variant="secondary" onClick={this.handleClose}>
                            {translations['amp.geocoder:cancel']}
                        </Button>
                        <Button variant="success" className={'button-header'} onClick={this.onRunSearch}>
                            {this.props.title}
                        </Button>
                    </Modal.Footer>
                </Modal>
            </>
        );
    }
}

RunSearchButton.contextType = TranslationContext;

const mapStateToProps = state => {
    return {
        geocoding: state.geocodingReducer,
        activitiesPending: state.activitiesReducer.pending,
        activities: state.activitiesReducer.activities
    };
};

const mapDispatchToProps = dispatch => bindActionCreators({
    runSearch: runSearch,
}, dispatch);

export default connect(mapStateToProps, mapDispatchToProps)(RunSearchButton);