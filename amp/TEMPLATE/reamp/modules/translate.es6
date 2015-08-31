import obj2arr from "amp/tools/obj2arr";

export default function __(_text){
  var text = _text;
  obj2arr(arguments).slice(1).forEach(param => text = text.replace('#$', param));
  return text;
}