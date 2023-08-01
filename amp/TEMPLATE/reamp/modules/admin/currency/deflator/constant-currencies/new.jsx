import * as AMP from "amp/architecture";
import React from "react";
import {callFunc, identity, range} from "amp/tools";
import {MAX_YEAR, MIN_YEAR} from "amp/tools/validate";
//import {SETTINGS} from "amp/config/endpoints";

var within = arr => maybeEl => arr.indexOf(maybeEl) !== -1;

var ensureInBounds = bounds => maybeEl => within(bounds)(maybeEl) ? maybeEl : bounds[0];

export class Model extends AMP.Model{
  constructor(...args){
    super(...args);

    var fromYears = range(MIN_YEAR, MAX_YEAR);
    this.fromYears = () => fromYears;

    var toYears = fromYears.filter(year => year >= this.from());
    this.toYears = () => toYears;

    var ensuredTo = ensureInBounds(toYears)(this.to());
    this.ensuredTo = () => ensuredTo;

    //let's find all ranges for currently selected currency and currently selected calendar
    var currentConstantCurrencies = this.constantCurrencies().filterEntries(
        ({currency, calendar}) => currency() == this.currency() && calendar() == this.calendar()
    );

    this.intersectionsOrTouches = () =>
        currentConstantCurrencies.filter(constantCurrency =>
            constantCurrency.isIntersectedOrTouched(this.calendar(), this.currency(), this.from(), ensuredTo));

    this.intersectsOrTouches = () => !this.intersectionsOrTouches().empty();

    this.mergedFrom = () => Math.min(...this.intersectionsOrTouches().mapEntries(callFunc('from')), this.from());
    this.mergedTo = () => Math.max(...this.intersectionsOrTouches().mapEntries(callFunc('to')), ensuredTo);
  }
}

export var init = (...args) =>
  Promise.all(args).then(([constantCurrencies, currencies, calendars, translations]) => {
        var defaultCurrency = currencies.head().code();
        var currentYear = new Date().getFullYear();
        return new Model({
          constantCurrencies: constantCurrencies,
          calendar: calendars.head().id(),
          currency: defaultCurrency,
          from: currentYear,
          to: currentYear,
          calendars: calendars,
          currencies: currencies,
          translations: translations,
          open: false
        })
      }
    );

export var actions = AMP.actions({
  changeCalendar: "number",
  changeCurrency: "string",
  changeFrom: "number",
  changeTo: "number",
  add: ["number", "string", "number", "number"],
  toggle: "boolean",
  maybeClose: null
});

var select = (value, updateCb, children) => //create a select.input-sm.form-control
    <select value={value} className="input-sm form-control" onChange={e => updateCb(e.target.value)}>{children}</select>

var option = (value, label) => //create an <option> given its value and onChange
    <option value={value} key={value}>{label}</option>

var dropdown = (
    value,//current value of the dropdown
    entries,//all possible options
    updateCb,//function to call when the <select> changes
    blurCb,//function to call onBlur
    valueCb = identity,//function that returns the value
    labelCb = identity//function that returns the label
) => {
  var mapCb = entry => option(valueCb(entry), labelCb(entry));
  var mappedEntries = "function" == typeof entries.mapEntries ?
      entries.mapEntries(mapCb) :
      entries.map(mapCb);
  return <div className="form-group" onBlur={blurCb}>{select(value, updateCb, mappedEntries)}</div>
};

var getCurrencyName = currency => currency.name() + "(" + currency.code() + ")";

var ensureInt = cb => value => cb(parseInt(value));

var form = (
    {calendar, currency, from, ensuredTo, fromYears, toYears, calendars, currencies, intersectsOrTouches, mergedFrom,
        mergedTo, __},//model
    {changeCalendar, changeCurrency, changeFrom, changeTo, add, maybeClose}//actions
  ) => (
    <form className="form-inline add-new-cc" action="javascript:void(0)">
      {dropdown(currency(), currencies(), changeCurrency, maybeClose, callFunc("code"), getCurrencyName)}
      {dropdown(calendar(), calendars(), ensureInt(changeCalendar), maybeClose, callFunc("id"), callFunc("name"))}
      {dropdown(from(), fromYears(), ensureInt(changeFrom), maybeClose)}
      {dropdown(ensuredTo(), toYears(), ensureInt(changeTo), maybeClose)}
      <button className="btn btn-default btn-sm" onClick={e => add(calendar(), currency(), mergedFrom(), mergedTo())}>
        {intersectsOrTouches() ?
            __("amp.deflator:merge") :
            __("amp.deflator:add")}
      </button>
      {intersectsOrTouches() ?
        <p className="help-block">
          {__("amp.deflator:mergeHint")}
        </p>
      : null}
    </form>
);

var button = (__, onMouseEnter) => (
    <button className="btn btn-primary" onMouseEnter={onMouseEnter}>
      {__('amp.deflator:add')}
    </button>
);

export var view = AMP.view((model, actions) =>
    <td colSpan="4">
      {model.open() ? form(model, actions) : button(model.__, actions.toggle.bind(null, true))}
    </td>
);

export var translations = {
  "amp.deflator:add": "Add",
  "amp.deflator:merge": "Merge",
  "amp.deflator:mergeHint": `This new range and the highlighted ranges will be merged,
    because they either intersect or are neighbouring.`
};

var maybeClose = ({toggle}) => toggle(!!document.querySelector(".add-new-cc:hover, .add-new-cc select:focus"));

export var update = (action, model) => actions.match(action, {
  changeCalendar: model.calendar,
  changeCurrency: model.currency,
  changeFrom: model.from,
  changeTo: model.to,
  add: () => model.open(false),
  toggle: model.open,
  maybeClose: () => [model, maybeClose]
});