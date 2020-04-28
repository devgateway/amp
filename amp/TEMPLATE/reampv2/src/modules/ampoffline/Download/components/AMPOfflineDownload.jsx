import React, {Component} from 'react';

import fetchTranslations from '../actions/fetchTranslations';
import DownloadLinks from './DownloadLinks';
import monitor from '../images/monitor.png';
import {connect} from 'react-redux';
import {bindActionCreators} from 'redux';
import './css/style.css';

class AMPOfflineDownload extends Component {

    render() {
        const {translations} = this.props.translationsReducer;

        return (<div>
            <div className='col-md-5'>
                <img src={monitor}/>
            </div>
            <div className='col-md-7'>
                <div className='main_text'>
                    <h2>{translations['amp.offline:download-title']}</h2>
                    <span>{translations['amp.offline:text']}</span>*/}
                </div>
                <div>
                    <DownloadLinks/>
                </div>
            </div>
        </div>);
    }
}

/*
const mapStateToProps = state => ({...state});
*/
const mapStateToProps = (state) => {
    return {
        ...state
    };
};
const mapDispatchToProps = dispatch => bindActionCreators({}, dispatch);

export default connect(mapStateToProps, mapDispatchToProps)(AMPOfflineDownload);
