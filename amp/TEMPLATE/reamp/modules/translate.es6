import {obj2arr} from "amp/tools";

var translations = null;

export function loadTranslations(initial){
  translations = initial;
  return fetch("/rest/translations/label-translations", {
    method: 'post',
    credentials: 'same-origin',
    headers: {
      'Accept': 'application/json',
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(initial)
  }).then(response => response.json())
    .then(labels => {
      translations = labels;
      return this;
    });
}

export var t = key => translations[key];