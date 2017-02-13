import Home from "./components/index.jsx";
import ReactDOM from "react-dom";
import React from "react";
import boilerplate from "../../../../ampTemplate/node_modules/amp-boilerplate/dist/amp-boilerplate.js";

ReactDOM.render(
  <Home />,
  document.getElementById('gpi-data')
);

new boilerplate.layout({showLogin: false});
