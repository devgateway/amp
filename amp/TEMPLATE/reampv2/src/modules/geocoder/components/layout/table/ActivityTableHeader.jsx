import React, {Component} from 'react';
import {TranslationContext} from '../../AppContext';
import '../panel/geocoder.css'
import Button from "react-bootstrap/Button";
import Tooltip from "react-bootstrap/Tooltip";
import OverlayTrigger from "react-bootstrap/OverlayTrigger";
import {bindActionCreators} from "redux";
import {connect} from "react-redux";
import RunSearchButton from "../panel/button/RunSearchButton";
import {loadActivities, switchNationalProjects} from "../../../actions/activitiesAction";

const SelectedActivitiesMessage = ({title, selectedSize, size}) =>  {
    return (
        <>
            <div className={'col-md-2 text-header'}>
                <span>{title}</span>
                <span className="activities-message"><b>({selectedSize}/{size})</b></span>
            </div>
        </>
    );
}

class ActivityTableHeader extends Component {

    constructor(props) {
        super(props);

        this.onToggleNationalProjects = this.onToggleNationalProjects.bind(this);
    }

    onToggleNationalProjects = () => {
        this.props._switchNationalProjects();
        this.props._loadActivities(!this.props._includeNationalProjects);
    }


    render() {
        const {translations} = this.context;

        if (this.props.activitiesPending) {
            return <></>
        }


        let toggleButtonTooltip = this.props._includeNationalProjects ? translations['amp.geocoder:removeNationalLevelProjectsTooltip']
            : translations['amp.geocoder:addNationalLevelProjectsTooltip'];

        let toggleButtonText = this.props._includeNationalProjects ? translations['amp.geocoder:removeNationalLevelProjects']
            : translations['amp.geocoder:addNationalLevelProjects'];

        let renderTooltip = props => (
            <Tooltip {...props}>{toggleButtonTooltip}</Tooltip>
        );

        return (
            <div className='panel-body custom-panel geocoder-header'>
                <SelectedActivitiesMessage title={translations['amp.geocoder:selectProjects']}
                                            selectedSize={this.props.selectedActivities.length}
                                            size={this.props.activities.length} />
                <RunSearchButton title={translations['amp.geocoder:runSearch']} tooltip={translations['amp.geocoder:runSearchTooltip']}
                         selectedActivities={this.props.selectedActivities}
                         onRunSearch={this.onRunSearch} />
                <OverlayTrigger placement="top" overlay={renderTooltip}>
                    <Button variant="primary"
                            className={'pull-right button-header'}
                            onClick={this.onToggleNationalProjects}>{toggleButtonText}
                    </Button>
                </OverlayTrigger>
            </div>);
    }
}

ActivityTableHeader.contextType = TranslationContext;

const mapStateToProps = state => {
    return {
        activities: state.activitiesReducer.activities,
        activitiesPending: state.activitiesReducer.pending,
        _includeNationalProjects: state.activitiesReducer.includeNationalProjects
    };
};

const mapDispatchToProps = dispatch => bindActionCreators({
    _switchNationalProjects: switchNationalProjects,
    _loadActivities: loadActivities,
}, dispatch);

export default connect(mapStateToProps, mapDispatchToProps)(ActivityTableHeader);
