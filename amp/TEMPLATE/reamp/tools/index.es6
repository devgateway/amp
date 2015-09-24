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

export function keyCode(e){
  return "undefined" != typeof e.which ? e.which : e.keyCode;
}

export function shallowDiff (a,b){
  if(a && b && "object" == typeof a && "object" == typeof b){
    return Object.keys(a).some(key => a[key] != b[key]);
  } else {
    return a != b;
  }
}