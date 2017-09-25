import React from "react";
import Link from "react-router";
import ReactDOM from "react-dom";
import { fetchJson } from "amp/tools";

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
            case 'windows':
                name = `Windows Vista/7/8/10 - ${arch} bits`;
                break;
            case 'debian':
                name = `Ubuntu Linux (.deb) - ${arch} bits`;
                break;
            case 'mac':
                name = `MacOS - ${arch} bits`;
                break;
        }
        return name;
    },

    render: function() {
        var __ = key => this.props.translations[key];
        return (
            <div>
                <h4>Available installers</h4>
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