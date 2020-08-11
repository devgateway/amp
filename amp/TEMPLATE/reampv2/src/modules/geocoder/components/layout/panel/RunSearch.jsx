import React, {Component} from "react";
import Button from "react-bootstrap/Button";
import Modal from "react-bootstrap/Modal";
import {TranslationContext} from "../../AppContext";

export default class RunSearch extends Component  {

    constructor(props) {
        super(props);
        this.state = {show: false};

        this.handleClose = this.handleClose.bind(this);
        this.handleShow = this.handleShow.bind(this);
    }

    handleClose() {
        this.setState(prevState => {
            return {show: false}
        });
    }

    handleShow () {
        this.setState(prevState => {
            return {show: true}
        });
    }

    render() {
        let {translations} = this.context;

        return (
                <>
                    <Button variant="success"
                            className={'pull-right button-header'}
                            disabled = {this.props.selectedActivities.length < 1} onClick={this.handleShow}>{this.props.title}
                    </Button>

                    <Modal show={this.state.show} onHide={this.handleClose} animation={false}>
                        <Modal.Header closeButton>
                            <Modal.Title>{this.props.title}</Modal.Title>
                        </Modal.Header>
                        <Modal.Body>Activities to be geocoded: {this.props.selectedActivities}</Modal.Body>
                        <Modal.Footer>
                            <Button variant="secondary" onClick={this.handleClose}>
                                Cancel
                            </Button>
                            <Button variant="success" className={'button-header'} onClick={this.handleClose}>
                                {this.props.title}
                            </Button>
                        </Modal.Footer>
                    </Modal>
                </>
            );
    }
}

RunSearch.contextType = TranslationContext;