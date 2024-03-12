/** @module tools */

/**
 * Returns a function that will invoke `func` property on its argument
 * @param {Function} func
 * @returns {Function}
 */
export var callFunc = funcName => obj => obj[funcName]();

/**
 * Converts an object to an array
 * @param {Object} obj
 * @returns {Array}
 */
export function obj2arr(obj){
  return Object.keys(obj).map(key => obj[key]);
}

export var identity = whatever => whatever

export function fetchJson(url) {
  return fetch(url, {credentials: 'same-origin', cache: 'no-store', headers: {'Content-Type': 'application/json'}}).then(callFunc('json'))
}

export function postJson(endpoint, payload){
  return fetch(endpoint, {
    method: 'post',
    credentials: 'same-origin',
    headers: {
      'Accept': 'application/json',
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(payload)
  })
}

export function deleteJson(endpoint, payload){
  return fetch(endpoint, {
    method: 'delete',
    credentials: 'same-origin',
    headers: {
      'Accept': 'application/json',
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(payload)
  })
}

export function putJson(endpoint, payload){
  return fetch(endpoint, {
    method: 'put',
    credentials: 'same-origin',
    headers: {
      'Accept': 'application/json',
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(payload)
  })
}


export function fetchJsonDev(url) {
  url = 'http://localhost:8080' + url; 
  return fetch(url, {
  	credentials: 'no-cors',
  	cache: 'no-store',
  	headers: {
      'Accept': 'application/json',
      'Content-Type': 'application/json',
      'Access-Control-Request-Method': 'POST',
      'Access-Control-Request-Headers': 'X-Custom-Header'
    }}
  ).then(callFunc('json'))
}

export function postJsonDev(endpoint, payload){
  endpoint = 'http://localhost:8080' + endpoint;	
  return fetch(endpoint, {
    method: 'post',
    credentials: 'no-cors',
    headers: {
      'Accept': 'application/json',
      'Content-Type': 'application/json',
      'Access-Control-Request-Method': 'POST',
      'Access-Control-Request-Headers': 'X-Custom-Header'
    },
    body: JSON.stringify(payload)
  })
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

/**
 * If the argument is an array, returns it, otherwise return an array whose sole member is the argument
 * @param maybeArray
 * @returns {Array}
 */
export function ensureArray(maybeArray){
  return Array.isArray(maybeArray) ? maybeArray : [maybeArray]
}

export var spy = cb => (...args) => {
  console.log(...args);
  return cb(...args);
};

/**
 * Composes functions. compose(a, b, c)(1) is the same as a(b(c(1)))
 * @param cbs An infinite number of functions
 */
export var compose = (...cbs) => initial => cbs.reduce((accum, cb) => cb(accum), initial);

export function delay(interval) {
    return new Promise(function(resolve) {
        setTimeout(resolve, interval);
    });
}
