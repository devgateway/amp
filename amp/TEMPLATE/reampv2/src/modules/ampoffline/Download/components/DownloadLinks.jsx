//import * as AMP from "amp/architecture";
import React, {Component} from "react";
import platform from "platform";
//import {loadTranslations} from "amp/modules/translate";
import {getFiles, getFilesError, getFilesPending} from '../reducers/startupReducer';
import fetchFilesAction from '../actions/fetchFiles';
import {
    AMP_OFFLINE_INSTALLERS, DEBIAN_LINUX, LINUX, MAC, MACINTOSH, OS_X, REDHAT_LINUX, WINDOWS
} from '../constants/Constants';
import {connect} from 'react-redux';
import {bindActionCreators} from 'redux';


class DownloadLinks extends Component {

    constructor(props) {
        super(props);
        this.shouldComponentRender = this.shouldComponentRender.bind(this);
        this.translations = {
            "amp.offline:download": "Download AMP Offline",
            "amp.offline:all-versions": "All installer versions",
            "amp.offline:best-version-message": "We have automatically detected which version of the application meets your operating system requirements. Other versions are available below.",
            "amp.offline:bits": "bits"
        }

    }


    componentDidMount() {
        const {fetchFiles} = this.props;

        fetchFiles();
    }

    shouldComponentRender() {
        return !this.props.pending;
    }


    _buildLinksTable() {
        const links = [];
        if (this.props.files) {
            this.props.files.sort((i, j) => i.os > j.os).map(i => {
                return links.push(<a href={`${AMP_OFFLINE_INSTALLERS}/${i.id}`}>{this._getInstallerName(i.os, i.arch)}</a>);
            });
            return (<ul>
                {links.map((l, j) => (<li key={j}>{l}</li>))}
            </ul>);
        } else {
            return null;
        }
    }

    _getInstallerName(os, arch) {
        let name = '';
        switch (os) {
            case WINDOWS:
                name = `Windows Vista/7/8/10 - ${arch} ${this.translations['amp.offline:bits']}`;
                break;
            case DEBIAN_LINUX:
                name = `Ubuntu Linux (.deb) - ${arch} ${this.translations['amp.offline:bits']}`;
                break;
            case REDHAT_LINUX:
                name = `RedHat Linux (.rpm) - ${arch} ${this.translations['amp.offline:bits']}`;
                break;
            case MAC:
                name = `Mac OS - ${arch} ${this.translations['amp.offline:bits']}`;
                break;
        }
        return name;
    }

    _detectBestInstaller() {

        const os = platform.os;
        const arch = os.architecture;
        const family = os.family.toLowerCase();
        let osNames = null;
        if (family.indexOf(WINDOWS) > -1) {
            osNames = [WINDOWS];
        } else if (family.indexOf(MACINTOSH) > -1 || family.indexOf(OS_X) > -1) {
            osNames = [MAC];
        } else if (family.indexOf(LINUX) > -1) {
            osNames = [DEBIAN_LINUX, REDHAT_LINUX];
        } else {
            return [];
        }
        debugger;
        const installer = this.props.files.filter(i => (osNames.filter(os => os === i.os).length > 0 && i.arch === arch.toString()));
        const links = installer.map(i => {
            const installerName = this._getInstallerName(i.os, i.arch);
            return (<div key={i.id} className="link"><a
                href={`${AMP_OFFLINE_INSTALLERS}/${i.id}`}>{this.translations['amp.offline:download']} {i.version} - {installerName}</a>
            </div>);
        });
        const message = this.translations['amp.offline:best-version-message'];
        return (
            <div className="alert alert-info" role="alert"><span className="info-text">{message}</span>{links}</div>);
    }

    render() {
        if (!this.shouldComponentRender()) {
            return <div>loading</div>
        } else {
            return (
                <div>
                    <div>
                        {this._detectBestInstaller()}
                    </div>
                    <h4>{this.translations['amp.offline:all-versions']}</h4>
                    <div>
                        {this._buildLinksTable()}
                    </div>
                </div>
            );
        }
    }
}

const mapStateToProps = state => ({
    error: getFilesError(state.startupReducer),
    files: getFiles(state.startupReducer),
    pending: getFilesPending(state.startupReducer)
});
const mapDispatchToProps = dispatch => bindActionCreators({fetchFiles: fetchFilesAction}, dispatch)
export default connect(mapStateToProps, mapDispatchToProps)(DownloadLinks);
/*);
DownloadLinks.translations = {
    "amp.offline:download": "Download AMP Offline",
    "amp.offline:all-versions": "All installer versions",
    "amp.offline:best-version-message": "We have automatically detected which version of the application meets your operating system requirements. Other versions are available below.",
    "amp.offline:bits": "bits"
};

module.exports = DownloadLinks;
*/

