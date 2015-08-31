import {init, view, update} from "./index.jsx";
import {run} from "amp/architecture";
import boilerplate from "../../../../../ampTemplate/node_modules/amp-boilerplate/dist/amp-boilerplate.js";

new boilerplate.layout({});

init().then(model => run({
  model: model,
  view: view,
  update: update,
  element: document.getElementById('main-container')
}));