import debug from "debug";

export default function(slug){
  var res = {
    log: debug(slug),
    err: debug(slug),
    warn: debug(slug),
    onDebug: debug(slug)
  };
  res.err.log = console.error.bind(console);
  res.warn.log = console.warn.bind(console);
  res.onDebug.log = function(...args){
    args.pop()();
  };
  return res;
}