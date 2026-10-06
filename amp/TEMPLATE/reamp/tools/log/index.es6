import debug from "debug";

export default function(slug){
  var res = {
    log: debug(slug),
    err: debug(slug),
    warn: debug(slug),
    onDebug: debug(slug)
  };
  res.err.log = Function.prototype.bind.call(console.error, console);
  res.warn.log = Function.prototype.bind.call(console.warn, console);
  res.onDebug.log = function(...args){
    for(var index in args){
      if("function" == typeof args[index]){
        args[index]();
        break;
      }
    }
  };
  return res;
}