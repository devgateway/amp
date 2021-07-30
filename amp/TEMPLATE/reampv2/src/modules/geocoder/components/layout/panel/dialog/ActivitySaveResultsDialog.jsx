import React, {Component} from "react";
import Button from "react-bootstrap/Button";
import Modal from "react-bootstrap/Modal";
import {TranslationContext} from "../../../AppContext";
import {bindActionCreators} from "redux";
import {connect} from "react-redux";
import {loadGeocoding, resetSaveResults} from "../../../../actions/geocodingAction";

function ActivitySaveResults(props) {
    const results = [];
    const activitiesWithoutErrors = props.activities.filter(activity => !activity.error);
    const activitiesWithErrors = props.activities.filter(activity => activity.error);

    if (activitiesWithoutErrors.length > 0) {
        results.push(<div className={'status-header'}>{props.translations['amp.geocoder:saveResultsText']}:</div>)
        results.push(
            activitiesWithoutErrors.map((activity) =>
                <>
                    <div>{props.translations['amp.geocoder:ampId']}: {activity.amp_id}</div>
                </>
            )
        );
    }

    function deepFlatten(array) {
        return array.reduce(function (r, e) {
            return Array.isArray(e) ? r.push(...deepFlatten(e)) : r.push(e), r
        }, [])
    };

    function translateErrorMessage(message) {
        if (message === "Different implementation levels selected") {
            return props.translations['amp.geocoder:error.differentImplementationLevels'];
        }
        return message;
    };

    function getUserFriendlyErrorMessage(error) {
        let messages = Object.values(JSON.parse(error))
            .map(x => Object.values(x))
            .flat();

        return deepFlatten(messages)
            .map(translateErrorMessage)
            .join("; ");
    }

    if (activitiesWithErrors.length > 0) {
        results.push(<br/>);
        results.push(<div className={'status-header'}>{props.translations['amp.geocoder:saveResultsErrorText']}:</div>)
        results.push(
            activitiesWithErrors.map((activity) =>
                <>
                    <div>{props.translations['amp.geocoder:ampId']}: {activity.amp_id}</div>
                    <div className={'status-error'}>{getUserFriendlyErrorMessage(activity.error)}</div>
                    <br/>
                </>
            )
        );
    }

    return results;
}

class ActivitySaveResultsDialog extends Component {

    constructor(props) {
        super(props);
        this.state = {show: true};

        this.handleClose = this.handleClose.bind(this);

    }

    handleClose() {
        this.props.resetSaveResults();
        this.setState({show: false});
    }

    render() {
        const {translations} = this.context;

        return (
            <>
                <Modal show={this.state.show} onHide={this.handleClose} animation={false}>
                    <Modal.Header closeButton>
                        <Modal.Title>{this.props.title}</Modal.Title>
                    </Modal.Header>
                    <Modal.Body>
                            <ActivitySaveResults translations={translations}
                                                 activities={this.props.geocoding.save_activities_result} />
                    </Modal.Body>
                    <Modal.Footer>
                        <Button variant="primary" onClick={this.handleClose}>
                            {translations['amp.geocoder:ok']}
                        </Button>
                    </Modal.Footer>
                </Modal>
            </>
        );
    }
}

ActivitySaveResultsDialog.contextType = TranslationContext;

const mapStateToProps = state => {
    return {
        geocoding: state.geocodingReducer,
    };
};

const mapDispatchToProps = dispatch => bindActionCreators({
    loadGeocoding: loadGeocoding,
    resetSaveResults: resetSaveResults,
}, dispatch);

export default connect(mapStateToProps, mapDispatchToProps)(ActivitySaveResultsDialog);