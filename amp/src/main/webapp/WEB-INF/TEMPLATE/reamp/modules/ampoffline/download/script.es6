import AMPOfflineDownload from "./index.jsx";
import ReactDOM from "react-dom";
import React from "react";
import boilerplate from "../../../../ampTemplate/node_modules/amp-boilerplate/dist/amp-boilerplate.js";

ReactDOM.render(
  <AMPOfflineDownload />,
  document.getElementById('ampoffline-client-download')
);

new boilerplate.layout({});
