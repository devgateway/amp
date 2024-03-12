import {ensureArray} from "amp/tools";

var makeTuples = (a, b) => a.map((val, key) => [val, b[key]]);

var validateType = ([type, param]) => "string" == typeof type ? type == typeof param : param instanceof type;

var getOffendingParam = ([tuple, ...tuples], index = 1) =>
    validateType(tuple) ?
        getOffendingParam(tuples, index + 1) :
        [index, tuple[1], tuple[0]];

var validateParams = (paramTypes, params) => {
  if(!makeTuples(paramTypes, params).every(validateType)){
    var [index, paramValue, paramType] = getOffendingParam(makeTuples(paramTypes, params));
    console.error("Invalid value for parameter", index, "! Expected", paramValue, "to be", paramType, "!");
  }
};

var isForwardable = maybeForwardable => "function" == typeof maybeForwardable.forward

var isUnion = paramTypes => paramTypes.some(isForwardable);

export default function (definitions){
  return {
    forward(send){
      return Object.keys(definitions).reduce((obj, name) => {
        var definition = definitions[name];
        var paramTypes = null == definition ? [] : ensureArray(definition).slice(0);
        if(isUnion(paramTypes)){
          var union = paramTypes.pop();
          obj[name] = (...params) => {
            validateParams(paramTypes, params);
            return union.forward(action => send({
              name: name,
              params: params.concat([action])
            }))
          }
        } else {
          obj[name] = (...params) => {
            validateParams(paramTypes, params);
            send({
              name: name,
              params: params
            })
          }
        }
        return obj;
      }, {})
    },

    match({name, params}, rules){
      if("function" != typeof rules._){
        for(var definition in definitions){
          if("function" != typeof rules[definition]){
            console.error('No rule for action', definition, 'in', rules);
          }
        }
      }
      for(var rule in rules){
        if("_" != rule && "undefined" == typeof definitions[rule]){
          console.error("Rule", rule, "from", rules, "not found in", definitions);
        }
      }
      var cb = "function" == typeof rules[name] ? rules[name] : rules._;
      if("function" != typeof cb){
        console.error("Rule", name, "not found in", rules);
      }
      return cb(...params);
    }
  };
}