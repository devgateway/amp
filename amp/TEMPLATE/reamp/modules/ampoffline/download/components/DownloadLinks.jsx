import React from "react";
import ReactDOM from "react-dom";

var DownloadLinks = React.createClass( {
    displayName: '',

    getInitialState: function() {
        return { data: [] };
    },

    render: function() {
        var __ = key => this.props.translations[key];
        return (
            <span>Lista de versiones para descargar</span>
        );
    }
} );

DownloadLinks.translations = {
    "amp.offline:test": "Test",
};

module.exports = DownloadLinks;