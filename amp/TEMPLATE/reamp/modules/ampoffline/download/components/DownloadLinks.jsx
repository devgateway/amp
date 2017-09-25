import React from "react";
import Link from "react-router";
import ReactDOM from "react-dom";
import platform from "platform";
import { fetchJson } from "amp/tools";

const WINDOWS = 'windows';
const MAC = 'mac';
const LINUX = 'debian';

var DownloadLinks = React.createClass( {

    displayName: '',

    getInitialState: function() {
        return { data: [] };
    },

    componentDidMount: function() {
        fetchJson( this.props.url ).then( installers => this.setState( { data: installers } ) );
    },

    _buildLinksTable() {
        const links = [];
        if ( this.state.data ) {
            this.state.data.sort(( i, j ) => i.os > j.os ).map( i => {
                links.push( <a href={`${this.props.url}/${i.id}`} >{this._getInstallerName( i.os, i.arch )}</a> );
            } );
            return ( <ul>
                {links.map( l => ( <li>{l}</li> ) )}
            </ul> );
        } else {
            return null;
        }
    },

    _getInstallerName( os, arch ) {
        let name = '';
        switch ( os ) {
            case WINDOWS:
                name = `Windows Vista/7/8/10 - ${arch} bits`;
                break;
            case LINUX:
                name = `Ubuntu Linux (.deb) - ${arch} bits`;
                break;
            case MAC:
                name = `Mac OS - ${arch} bits`;
                break;
        }
        return name;
    },

    _detectBestInstaller() {
        const os = platform.os;
        const arch = os.architecture;
        const family = os.family.toLowerCase();
        let osName = '';
        if ( family.indexOf( WINDOWS ) > -1 ) {
            osName = WINDOWS;
        } else if ( family.indexOf( 'macintosh' ) > -1 ) {
            osName = MAC;
        } else if ( family.indexOf( 'linux' ) > -1 ) {
            osName = LINUX;
        } else {
            return null;
        }
        const installer = this.state.data.filter( i => ( i.os === osName && i.arch === arch.toString() ) );
        if ( installer.length > 0 ) {
            const message = 'We have automatically detected which version of the application meets your operating system requirements. Other versions are available below.';
            const installerName = this._getInstallerName( installer[0].os, installer[0].arch );
            const link = <div><a href='' >Download AMP Offline {installer[0].version} for {installerName}</a></div>;
            return ( <div className="alert alert-info" role="alert"><span>{message}</span>{link}</div> );
        }
    },

    render: function() {
        var __ = key => this.props.translations[key];
        return (
            <div>
                <div>
                    {this._detectBestInstaller()}
                </div>
                <h4>All installer versions</h4>
                <div>
                    {this._buildLinksTable()}
                </div>
            </div>
        );
    }
} );

DownloadLinks.translations = {
    "amp.offline:test": "Test",
};

module.exports = DownloadLinks;