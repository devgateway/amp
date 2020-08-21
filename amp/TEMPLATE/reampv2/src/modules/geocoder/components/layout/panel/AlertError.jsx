import React, {Component} from "react";
import Alert from "react-bootstrap/Alert";

export default class AlertError extends Component {
    render() {
        let {error} = this.props;
        return (
            <Alert variant="danger" show={true} transition={false}>
                <Alert.Heading>Sorry, there was a problem</Alert.Heading>
                <p>{error}</p>
            </Alert>
        );
    }
}