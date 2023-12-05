import * as AMP from "amp/architecture";
import React from "react";
import {HEAT_MAP_ADMIN} from "amp/config/endpoints";
import HeatMapSettings from "./heat-map/index.jsx";
import {loadTranslations} from "amp/modules/translate";

const TABS = {
  HEAT_MAP: "heat-map"
};

var DashboardSettings = React.createClass({displayName: 'Heat Map Settings',
  getInitialState: function() {
    return {translations: []};
  },
  componentDidMount: function() {
  	let toTranslate = new AMP.Model(translations).toJS();
    loadTranslations(translations).then(trns => this.updateTranslations(trns));
    console.log(this.state.translations);
  },
  render : function () {
    return (
    	<HeatMapSettings translations = {this.state.translations} url = {HEAT_MAP_ADMIN}/>
    );
  },
  updateTranslations: function(translations) {
  	this.setState({translations: translations})
  }
});

var translations = {
	...HeatMapSettings.translations
}


module.exports = DashboardSettings;
