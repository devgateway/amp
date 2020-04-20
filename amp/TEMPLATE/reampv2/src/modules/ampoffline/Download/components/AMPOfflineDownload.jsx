import React, {Component} from "react";
//import {loadTranslations} from "amp/modules/translate";
import DownloadLinks from './DownloadLinks';
import monitor from '../images/monitor.png';
import {connect} from 'react-redux';
import {bindActionCreators} from 'redux';
import './css/style.css';
class AMPOfflineDownload extends Component {

    render() {
        return (<div>
            <div className='col-md-5'>
                <img src={monitor}/>
            </div>
            <div className='col-md-7'>
                <div className='main_text'>
                    {/*<h2>{this.state.translations['amp.offline:download-title']}</h2>
                    <span>{this.state.translations['amp.offline:text']}</span>*/}
                    <h2>Download Offline Client 23</h2>
                    <span>The AMP Offline application allows you to edit and add activity information to the AMP without having an active internet connection. In order to use the application, you must download and install the compatible version of AMP Offline application from the list of the latest AMP Offline installers.
                        When you run the application for the first time, you must have an active internet connection in order to sync your user data, activity data, and other critical data to the application. After that, you may work offline and sync your data periodically.</span>
                </div>
                <div>
                    <DownloadLinks/>
                </div>
            </div>
        </div>);
    }
}

const mapStateToProps = state => ({...state});

const mapDispatchToProps = dispatch => bindActionCreators({}, dispatch);

export default connect(mapStateToProps, mapDispatchToProps)(AMPOfflineDownload);
