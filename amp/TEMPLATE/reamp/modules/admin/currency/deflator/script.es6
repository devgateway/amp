import {translations, init, view, update, actions} from "./index.jsx";
import {run, Model} from "amp/architecture";
import boilerplate from "../../../../../ampTemplate/node_modules/amp-boilerplate/dist/amp-boilerplate.js";
import {loadTranslations} from "amp/modules/translate";

var translationsPromise = loadTranslations(translations);

init(translationsPromise).then(model => run({
  model: model,
  view: view,
  actions: actions,
  update: update,
  element: document.getElementById('main-container')
}));

new boilerplate.layout({});