import React, {Component} from "react";
import Button from "react-bootstrap/Button";
import Modal from "react-bootstrap/Modal";
import Alert from "react-bootstrap/Alert";
import {TranslationContext} from "../../AppContext";
import {bindActionCreators} from "redux";
import {connect} from "react-redux";
import AlertError from "./AlertError";
import {Loading} from "../../../../../utils/components/Loading";

class RunSearchButton extends Component {

    constructor(props) {
        super(props);
        this.state = {show: false};

        this.handleClose = this.handleClose.bind(this);
        this.handleShow = this.handleShow.bind(this);
    }

    handleClose() {
        this.setState({show: false});
    }

    handleShow() {
        this.setState({show: true});
    }

    render() {
        const {translations} = this.context;

        return (
            <>
                <Button variant="success"
                        className={'pull-right button-header'}
                        disabled={this.props.selectedActivities.length < 1} onClick={this.handleShow}>{this.props.title}
                </Button>

                <Modal show={this.state.show} onHide={this.handleClose} animation={false}>
                    <Modal.Header closeButton>
                        <Modal.Title>{this.props.title}</Modal.Title>
                    </Modal.Header>
                    <Modal.Body>Activities to be geocoded:
                        {this.props.selectedActivities.map((value) => {
                            return <div>{value}</div>
                        })}
                        </Modal.Body>
                    <Modal.Footer>
                        <Button variant="secondary" onClick={this.handleClose}>
                            {translations['amp.geocoder:cancel']}
                        </Button>
                        <Button variant="success" className={'button-header'} onClick={this.props.onRunSearch}>
                            {this.props.title}
                        </Button>
                        {this.props.geocoding.run_search_pending && <Loading/>}
                        {this.props.geocoding.run_search_error && <AlertError error={this.props.geocoding.run_search_error}/>}
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
    };
};

const mapDispatchToProps = dispatch => bindActionCreators({}, dispatch);

export default connect(mapStateToProps, mapDispatchToProps)(RunSearchButton);