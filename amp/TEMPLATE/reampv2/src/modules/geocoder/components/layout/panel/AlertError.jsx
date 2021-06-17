import React, {Component} from "react";
import Alert from "react-bootstrap/Alert";
import {TranslationContext} from "../../AppContext";
import {formatText} from "../../../../../utils/Utils";

export default class AlertError extends Component {
    render() {
        const {translations} = this.context;

        let errorMessage = getErrorMessage(translations, this.props);

        return (
            <Alert variant="danger" show={true} transition={false}>
                <Alert.Heading>{translations['amp.geocoder:errorAlertWarning']}</Alert.Heading>
                <p>{errorMessage}</p>
            </Alert>
        );
    }
}

function getErrorMessage(translations, {errorCode, error}) {
    let errorMessageCode = 'amp.geocoder:error.' + errorCode;

    if (translations.hasOwnProperty(errorMessageCode)) {
        if (errorCode === '1704' || errorCode === '1705') {
            let errorObj = JSON.parse(error);
            return formatText(translations[errorMessageCode], errorObj[0][Object.keys(errorObj[0])[0]][0]);
        } else {
            return translations[errorMessageCode];
        }
    }

    return error;
}

AlertError.contextType = TranslationContext;