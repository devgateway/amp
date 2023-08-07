import React, { useContext } from 'react';
import DownloadLinks from './DownloadLinks';
import monitor from '../images/monitor.png';
import './css/style.css';
import { TranslationContext } from './Startup'

const AmpOfflineDownload = () => {
    const { translations } = useContext(TranslationContext);

    return (
        <div>
            <div className="col-md-5">
                <img src={monitor} alt={translations['amp.offline:download-title']} />
            </div>
            <div className="col-md-7">
                <div className="main_text">
                    <h2>{translations['amp.offline:download-title']}</h2>
                    <span>{translations['amp.offline:text']}</span>
                </div>
                <div>
                    <DownloadLinks />
                </div>
            </div>
        </div>
    );
}

export default AmpOfflineDownload;
