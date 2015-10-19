import * as AMP from "amp/architecture";
import React from "react";
import * as validate from "amp/tools/validate";
import style from "./style.less";
import cn from "classnames";
import {keyCode} from "amp/tools";
const KEY_UP = 38;
const KEY_DOWN = 40;

export var actions = AMP.actions({
  yearChanged: 'string',
  yearSubmitted: 'number',
  increment: null,
  decrement: null
});

export class Model extends AMP.Model{}

export var model = new Model({
  year: '',
  repeatedYearWarning: false,
  translations: null
});

var changeYear = yearChanged => e => {
  var value = e.target.value;
  if(0 == value.length || validate.year(value)){
    yearChanged(value);
  }
};

var submitYear = yearSubmitted => e => {
  e.preventDefault();
  yearSubmitted(parseInt(e.target.querySelector('input').value));
};

var maybeNavigate = inc => dec => e => {
  if(KEY_UP == keyCode(e)) inc();
  if(KEY_DOWN == keyCode(e)) dec();
};

export var view = AMP.view(({repeatedYearWarning, year, translations},
  {yearChanged, yearSubmitted, increment, decrement}) => {
  var __ = key => translations().get(key);
  return (
    <td className={cn("edit-on-hover add-new-rate", {"has-error": repeatedYearWarning()})}>
      <form onSubmit={submitYear(yearSubmitted)} action="#">
        <input
            className="form-control edit"
            placeholder={__('amp.deflator:newRateHint')}
            onChange={changeYear(yearChanged)}
            onKeyDown={maybeNavigate(increment)(decrement)}
            value={year()}
            />
        <button className="btn btn-primary view">{__('amp.deflator:add')}</button>
        <div className="help-block">
          {repeatedYearWarning() ?
            __('amp.deflator:repeatedYear') + " " + year() :
            __('amp.deflator:yearConstraints')
          }
        </div>
      </form>
    </td>
  )
});

var withInt = cb => n => cb(parseInt(n));
var yearStep = step =>
  withInt(year => validate.year(String(year + step)) ? year + step : year);

export var update = (action, model: Model) => actions.match(action, {
  yearChanged: model.year,

  yearSubmitted: () => model.year(""),

  increment: () => model.year(yearStep(1)),

  decrement: () => model.year(yearStep(-1))
});

export var translations = {
  "amp.deflator:newRateHint": "Enter the year and press Enter",
  "amp.deflator:add": "Add inflation rate",
  "amp.deflator:yearConstraints": "Year must be between 1970 and 2050",
  "amp.deflator:repeatedYear": "There is already an entry for"
};