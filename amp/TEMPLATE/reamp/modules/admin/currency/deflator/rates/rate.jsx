import React from "react";
import * as AMP from "amp/architecture";
import cn from "classnames";
import {negative, number, point} from "amp/tools/validate";
import {keyCode} from "amp/tools";

const KEY_UP = 38;
const KEY_DOWN = 40;
require('react-date-picker/index.css');

export var actions = AMP.actions({
  remove: null,
  change: 'string',
  up: HTMLElement,
  down: HTMLElement
});


export class Model extends AMP.Model{}

export var model = new Model({
  value: null,
  period: null,
  valid: true,
  translations: null,
  deletedAt: null
});

export var humanReadablePeriod = period =>
    new Date(period).toLocaleDateString(undefined, {month: 'short', year: 'numeric', day: 'numeric'})

export var view = AMP.view((
    {valid, value, period, translations}//model
    , {remove, change, up, down}//actions
) => {
  var __ = key => translations().get(key);
  var onChange = e => {
    var value = e.target.value;
    var floatValue = +value;
    var inBounds = -100 < floatValue && floatValue < 100;
    if(0 == value.length || (negative(point(number))(value) && (isNaN(floatValue) || inBounds))){
      change(value);
    }
  };
  var handleArrowNavigation = e => {
    if(KEY_UP == keyCode(e)) up(e.target);
    if(KEY_DOWN == keyCode(e)) down(e.target);
  };
  return (
    <tr
      className={cn('inflation-rate-entry', {"danger has-error": !valid()})}
    >
      <td>
        <span className="form-control input-sm view">{humanReadablePeriod(period())}</span>
      </td>
      <td className="inflation-rate edit-on-hover">
        <input className="form-control input-sm edit" required
               value={value()} onChange={onChange}
        />
        <span className="form-control input-sm view">{value()}</span>
      </td>
      <td>
        <button className="btn btn-default btn-xs" onClick={remove}>
          <i className="glyphicon glyphicon-trash"/> {__('amp.deflator:delete')}
        </button>
      </td>
    </tr>
  )
}, "Rate");

export var update = (action, model) => actions.match(action, {
  change: model.value,
  _: () => model
});

export var translations = {
  "amp.deflator:delete": "Delete"
}