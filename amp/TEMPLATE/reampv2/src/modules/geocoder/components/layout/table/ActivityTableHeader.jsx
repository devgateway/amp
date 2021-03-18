import React, {Component} from 'react';
import {TranslationContext} from '../../AppContext';
import '../panel/geocoder.css'
import {bindActionCreators} from "redux";
import {connect} from "react-redux";
import RunSearchButton from "../panel/button/RunSearchButton";

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
    }

    render() {
        const {translations} = this.context;
        return (
            <div className='panel-body custom-panel geocoder-header'>
                <SelectedActivitiesMessage title={translations['amp.geocoder:selectProjects']}
                                            selectedSize={this.props.selectedActivities.length}
                                            size={this.props.activities.length} />
                <RunSearchButton title={translations['amp.geocoder:runSearch']}
                         selectedActivities={this.props.selectedActivities}
                         onRunSearch={this.onRunSearch} />
            </div>);
    }
}

ActivityTableHeader.contextType = TranslationContext;

const mapStateToProps = state => {
    return {
        activities: state.activitiesReducer.activities,
    };
};

const mapDispatchToProps = dispatch => bindActionCreators({}, dispatch);

export default connect(mapStateToProps, mapDispatchToProps)(ActivityTableHeader);
