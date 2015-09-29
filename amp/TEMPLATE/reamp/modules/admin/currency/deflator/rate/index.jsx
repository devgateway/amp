import React from "react";
import * as AMP from "amp/architecture";
import style from "./style.less";
import cn from "classnames";
import {allow, negative, point, number} from "amp/tools/validate";
const KEY_UP = 38;
const KEY_DOWN = 40;
import {keyCode} from "amp/tools";

export var actions = AMP.actions({
  remove: null,
  change: 'string',
  toggleConstant: 'boolean',
  up: HTMLElement,
  down: HTMLElement
});


export class Model extends AMP.Model{
  inflationRate: string;
  deletable: boolean;
  valid: boolean;
}

export var model = new Model({
  constantCurrency: false,
  inflationRate: "",
  year: "",
  deletable: false,
  valid: false
});

export var view = AMP.view(({inflationRate, valid, year, constantCurrency, deletable}
    , {remove, change, toggleConstant, up, down}) => {
  var onChange = e => {
    var value = e.target.value;
    if(0 == value.length || negative(point(number))(value)){
      change(value);
    }
  };
  var onToggleConstant = e => toggleConstant(e.target.checked);
  var handleArrowNavigation = e => {
    if(KEY_UP == keyCode(e)) up(e.target);
    if(KEY_DOWN == keyCode(e)) down(e.target);
  };
  return (
    <tr
      className={cn('inflation-rate-entry', {"danger has-error": !valid()})}
      onKeyUp={handleArrowNavigation}
    >
      <td>
        <span className="form-control input-sm view">{year()}</span>
      </td>
      <td className="edit-on-hover inflation-rate">
        <input className="form-control input-sm edit" required
          value={inflationRate()} onChange={onChange}
        />
        <span className="form-control input-sm view">{inflationRate()}</span>
      </td>
      <td>
        <input type="checkbox" checked={constantCurrency()} onChange={onToggleConstant}/>
      </td>
      <td>
        {deletable() ?
          <i onClick={remove} className="glyphicon glyphicon-trash"></i>
          : null}
      </td>
    </tr>
  )
});

function pass(){}

export var update = (action, model) => actions.match(action, {
  change: model.inflationRate,
  toggleConstant: model.constantCurrency,
  _: pass
});