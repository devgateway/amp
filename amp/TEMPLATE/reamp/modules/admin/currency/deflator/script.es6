import {init, view, update} from "./index.jsx";
import {run} from "amp/architecture";
import boilerplate from "../../../../../ampTemplate/node_modules/amp-boilerplate/dist/amp-boilerplate.js";
import {loadTranslations} from "amp/modules/translate";
import labels from "./translations.json";

Promise.all([
  init(),
  loadTranslations(labels)
]).then(([model]) => run({
  model: model,
  view: view,
  update: update,
  element: document.getElementById('main-container')
}));

new boilerplate.layout({});