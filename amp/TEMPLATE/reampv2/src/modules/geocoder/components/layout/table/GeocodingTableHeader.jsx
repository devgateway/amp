import React, {Component} from 'react';
import {TranslationContext} from '../../AppContext';
import '../panel/geocoder.css'
import {bindActionCreators} from "redux";
import {connect} from "react-redux";
import ResetAllButton from "../panel/button/ResetAllButton";
import CancelGeocodingButton from "../panel/button/CancelGeocodingButton";
import SaveAllEditsButton from "../panel/button/SaveAllEditsButton";

class GeocodingTableHeader extends Component {

    render() {
        const {translations} = this.context;

        if (this.props.geocoding.pending || this.props.geocoding.geocodeShouldRun) {
            return <></>
        }

        return (
            <div className='panel-body custom-panel geocoder-header'>
                <CancelGeocodingButton title={translations['amp.geocoder:cancelGeocoding']} />
                <SaveAllEditsButton className={'pull-right button-header'} title={translations['amp.geocoder:saveAllEdits']}
                                    tooltip={translations['amp.geocoder:saveAllEditsTooltip']}/>
                <ResetAllButton title={translations['amp.geocoder:resetAll']} tooltip={translations['amp.geocoder:resetAllTooltip']}/>
            </div>);
    }
}

GeocodingTableHeader.contextType = TranslationContext;

const mapStateToProps = state => {
    return {
        geocoding: state.geocodingReducer,
    };
};

const mapDispatchToProps = dispatch => bindActionCreators({}, dispatch);

export default connect(mapStateToProps, mapDispatchToProps)(GeocodingTableHeader);
