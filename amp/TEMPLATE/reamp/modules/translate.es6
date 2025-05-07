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

export function loadTranslationsDev(initial){
  return fetch("http://localhost:8080/rest/translations/label-translations", {
    method: 'post',
    credentials: 'no-cors',
    headers: {
      'Accept': 'application/json',
      'Content-Type': 'application/json',
      'Access-Control-Request-Method': 'POST',
      'Access-Control-Request-Headers': 'X-Custom-Header'
    },
    body: JSON.stringify(initial)
  }).then(callFunc('json'))
}