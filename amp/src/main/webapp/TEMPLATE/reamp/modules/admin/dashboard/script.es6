import DashboardSettings from "./index.jsx";
import ReactDOM from "react-dom";
import React from "react";
import boilerplate from "../../../../ampTemplate/node_modules/amp-boilerplate/dist/amp-boilerplate.js";

ReactDOM.render(
  <DashboardSettings />,
  document.getElementById('heat-map')
);

new boilerplate.layout({showLogin: false});
