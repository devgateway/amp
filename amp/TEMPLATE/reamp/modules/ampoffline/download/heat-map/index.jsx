import React from "react";
import ReactDOM from "react-dom";
import ThresholdList from "./threshold-list.jsx";
import {fetchJson} from "amp/tools";

var HeatMapSettings = React.createClass({displayName: 'Heat Map Settings',
  getInitialState: function() {
    return {data: []};
  },
  componentDidMount: function() {
    fetchJson(this.props.url).then(settings => this.setState({data: settings.amountColors}));
  },
  render : function () {
  	var __ = key => this.props.translations[key];
    return (
      <div className="heatMapSettings">
        <h1>{__('amp.heat-map:heat-map-settings')}</h1>
        <ThresholdList translations = {this.props.translations} data={this.state.data} />
      </div>
    );
  }
});

HeatMapSettings.translations = {
  ...ThresholdList.translations,
  "amp.heat-map:heat-map-settings": "Fragmentation Chart Settings",
};

module.exports = HeatMapSettings;