import {DEVELOPMENT} from "./Constants";

export function getRootUrl() {
  if (process.env.NODE_ENV === DEVELOPMENT) {
    return '/#';
  } else {
    return `${process.env.PUBLIC_URL}/index.html#`;
  }
}

export function formatText(text) {
    var args = arguments;
    return text.replace(/{(\d+)}/g, function (match, number) {
        let argIdx = parseInt(number) + 1;
        return typeof args[argIdx] != 'undefined' ? args[argIdx] : match;
    });
};