import React, {Component, useState} from 'react';
import {TranslationContext} from '../../AppContext';
import './geocoder.css'
import Button from 'react-bootstrap/Button';
import {bindActionCreators} from "redux";
import {connect, useSelector} from "react-redux";
import Modal from "react-bootstrap/Modal";

const RunSearchConfirmationModal = (props) =>  {
    let [show, setShow] = useState(false);

    let handleClose = () => setShow(false);
    let handleShow = () => setShow(true);

    return (
        <>
            <Button variant="success"
                    className={'pull-right button-header'}
                    disabled = {props.runSearchDisabled} onClick={handleShow}>{props.title}
            </Button>

            <Modal show={show} onHide={handleClose} animation={false}>
                <Modal.Header closeButton>
                    <Modal.Title>{props.title}</Modal.Title>
                </Modal.Header>
                <Modal.Body>Activities to be geocoded: {props.selectedActivities}</Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={handleClose}>
                        Cancel
                    </Button>
                    <Button variant="success" className={'button-header'} onClick={handleClose}>
                        {props.title}
                    </Button>
                </Modal.Footer>
            </Modal>
        </>
    );
}

class GeocoderHeader extends Component {

    render() {
        let {translations} = this.context;
        let isActivityTable = this.props.geocoding.status === 'COMPLETED';
        let runSearchDisabled = this.props.selectedActivities.length < 1;
        let activitiesList = this.props.selectedActivities.join(", ");

        return (
            <div className='panel-body custom-panel geocoder-header'>
                <div className={'col-md-2 text-header'}>{translations['amp.geocoder:selectProjects']}</div>
                <div className={'col-md-2 header-settings text-header'}><a href={'#geocoder'}>{translations['amp.geocoder:settings']}</a></div>
                {!isActivityTable && <div className={'col-md-2 text-header'}><a href={'#geocoder'}>{translations['amp.geocoder:resetAll']}</a></div>}
                {isActivityTable
                    ? <RunSearchConfirmationModal title={translations['amp.geocoder:runSearch']}
                                                  runSearchDisabled={runSearchDisabled}
                                                  selectedActivities={activitiesList}/>
                    : <Button variant="success" className={'pull-right button-header'}>{translations['amp.geocoder:saveAllEdits']}</Button>
                }
            </div>);
    }
}

GeocoderHeader.contextType = TranslationContext;

const mapStateToProps = state => {
    return {
        geocoding: state.geocodingReducer.geocoding,
        selectedActivities: state.activitiesReducer.selectedActivities
    };
};

const mapDispatchToProps = dispatch => bindActionCreators({}, dispatch);

export default connect(mapStateToProps, mapDispatchToProps)(GeocoderHeader);
