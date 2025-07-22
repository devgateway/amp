"use strict";
require('babel-polyfill');
import Model from "./model";
import React from "react";
import ReactDOM from 'react-dom';
import createAction from "./actions";
import {ensureArray} from "amp/tools";

export {createAction as actions}

export function view(cb, name = "Unnamed"){
  var view = class extends View{
    render(){
      var {model, actions} = this.props;
      return cb(model, actions);
    }
  }
  view.displayName = name;
  return view;
}

class View extends React.Component {
  shouldComponentUpdate (nextProps){
    return this.props.model != nextProps.model;
  }
}

View.propTypes = {
  model: React.PropTypes.instanceOf(Model)
};

export function run({model, view, update, element, actions}){
  var View = view, state = model, reactElement, attachedActions;
  function processAction(action){
    var [newState, sideEffect] = ensureArray(update(action, state));
    function doPostUpdate(){
      state = newState;
      if("function" == typeof sideEffect){
        sideEffect(attachedActions);
      }
    }
    var props = {
      actions: attachedActions,
      model: newState
    };
    if(reactElement.shouldComponentUpdate(props)){
      reactElement = ReactDOM.render(<View {...props}/>, element, doPostUpdate);
    } else {
      doPostUpdate();
    }
  }
  attachedActions = actions.forward(processAction);
  reactElement = ReactDOM.render(<View actions={attachedActions} model={model}/>, element);
}

export function updateSubmodel(path:Array<string>, update, action, model){
  var [newModel, sideEffect] = ensureArray(update(action, model.getIn(path)));
  return [model.setIn(path, newModel), sideEffect]
}

export {Model as Model}
