export function callFunc(func){
  return obj => obj[func]();
}

export function obj2arr(obj){
  return Object.keys(obj).map(key => obj[key]);
}

export function fetchJson(url) {
  return fetch(url, {credentials: 'same-origin'}).then(callFunc('json'))
}

export function range(from, to){
  var arr = [];
  for(var counter = from; counter <= to; counter++){
    arr.push(counter);
  }
  return arr;
}