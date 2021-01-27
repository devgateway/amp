import React, {Component} from "react";
import Button from "react-bootstrap/Button";
import Modal from "react-bootstrap/Modal";
import {TranslationContext} from "../../../AppContext";
import {bindActionCreators} from "redux";
import {connect} from "react-redux";
import {
    loadGeocoding,
    resetSaveResults
} from "../../../../actions/geocodingAction";

function ActivitySaveResults({activities}) {
    const results = [];
    const activitiesWithoutErrors = activities.filter(activity => !activity.error);
    debugger;
    const activitiesWithErrors = activities.filter(activity => activity.error);
    if (activitiesWithoutErrors.length > 0) {
        results.push(<div className={'status-header'}>Activities saved without errors:</div>)
        results.push(
            activitiesWithoutErrors.map((activity) =>
                <>
                    <div>Activity id: {activity.activity_id}</div>
                </>
            )
        );
    }
    if (activitiesWithErrors.length > 0) {
        results.push(<br/>);
        results.push(<div className={'status-header'}>Activities not saved due to errors:</div>)
        results.push(
            activitiesWithErrors.map((activity) =>
                <>
                    <div>Activity id: {activity.activity_id}</div>
                    <div className={'status-error'}>Error: {activity.error}</div>
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
                            <ActivitySaveResults activities={this.props.geocoding.save_activities_result}/>
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