"use strict";
require('babel-core/polyfill');
import Model from "./model";
import * as effects from "./effects";
import React from "react";
import {shallowDiff} from "amp/tools";

export class View extends React.Component {
  shouldComponentUpdate (nextProps){
    return shallowDiff(this.props, nextProps);
  }
}

View.propTypes = {
  model: React.PropTypes.instanceOf(Model)
};

export class Action{}
export class Package extends Action{
  /**
   *
   * @param tag {any}
   * @param originalAction {Action}
   */
  constructor(tag, originalAction: Action){
    super();
    this.getTag = () => tag;
    this.unpack = () => originalAction;
  }
}

export function run({model, view, update, element}){
  var View = view;
  function makeAddress(packages: Array<Package>){
    return {
      send(_action){
        var action = packages.reduceRight((prev, curr) => {
          var [PackageClass, tag] = curr;
          return new PackageClass(tag, prev);
        }, _action);
        var response = update(action, state);
        var newState = response instanceof effects.SideEffect ? response.model : response;
        if(state != newState){
          React.render(<View address={address} model={newState}/>, element);
          state = newState;
        }
        if(response instanceof effects.SideEffect){
          response.unleash(address);
        }
      },
      /**
       * @param PackageClass {Package}
       * @param tag {any}
       */
      usePackage(PackageClass, tag){
        return makeAddress(packages.concat([[PackageClass, tag]]));
      }
    }
  }
  var state = model;
  var address = makeAddress([]);
  React.render(<View address={address} model={model}/>, element);
  return address;
}

export function updateSubmodel(path:Array<string>, update, action, model){
  var response = update(action, model.getIn(path));
  if(response instanceof effects.SideEffect){
    response.model = model.setIn(path, response.model);
    return response;
  } else if(response instanceof Model){
    return model.setIn(path, response);
  }
}

export {Model as Model}
export {effects as effects}