import {callFunc} from "amp/tools";

export function loadTranslations(initial){
  return fetch("/rest/translations/label-translations", {
    method: 'post',
    credentials: 'same-origin',
    headers: {
      'Accept': 'application/json',
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(initial)
  }).then(callFunc('json'))
}