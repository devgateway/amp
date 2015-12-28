import * as AMP from "amp/architecture";
import React from "react";
import {callFunc, identity, fetchJson, range} from "amp/tools";
import {MIN_YEAR, MAX_YEAR} from "amp/tools/validate";
import {SETTINGS} from "amp/config/endpoints";

var eq = (field, val) => model => model.get(field) == val;

var within = arr => maybeEl => arr.indexOf(maybeEl) !== -1;

var not = cb => (...args) => !cb(...args);

var ensureInBounds = bounds => maybeEl => within(bounds)(maybeEl) ? maybeEl : bounds[0];

export class Model extends AMP.Model{
  constructor(...args){
    super(...args);
    //let's find all ranges for currently selected currency
    var currentConstantCurrencies = this.constantCurrencies().filterEntries(eq('currency', this.currency()));
    var existingYears = currentConstantCurrencies.size() ? //did we find something?
        currentConstantCurrencies//sweet!
            .map(({from, to}) => range(from(), to()))//convert the internal data structures to plain arrays
            .reduce((a, b) => a.concat(b), [])//then concat them all into one array
        : [];//we found nothing? the return an empty array(without this we would get an empty Model

    var fromYears = range(MIN_YEAR, MAX_YEAR).filter(not(within(existingYears)));
    this.fromYears = () => fromYears;

    var ensuredFrom = ensureInBounds(fromYears)(this.from());
    this.ensuredFrom = () => ensuredFrom;

    var upperBound = Math.min.apply(Math,
        currentConstantCurrencies
            .filter(({from}) => from() > ensuredFrom)
            .mapEntries(({from}) => from() - 1)
            .concat([MAX_YEAR])
    );

    var toYears = range(ensuredFrom, upperBound);
    this.toYears = () => toYears;

    var ensuredTo = ensureInBounds(toYears)(this.to());
    this.ensuredTo = () => ensuredTo;


  }
}

export var init = (...args) =>
  Promise.all(args).then(([constantCurrencies, currencies, calendars, translations]) => {
        var defaultCurrency = currencies.head().code();
        return new Model({
          constantCurrencies: constantCurrencies,
          calendar: calendars.head().id(),
          currency: defaultCurrency,
          from: MIN_YEAR,
          to: new Date().getFullYear(),
          calendars: calendars,
          currencies: currencies,
          translations: translations
        })
      }
    );

export var actions = AMP.actions({
  changeCalendar: "number",
  changeCurrency: "string",
  changeFrom: "number",
  changeTo: "number",
  add: ["number", "string", "number", "number"]
});

var select = (value, updateCb, children) => //create a select.input-sm.form-control
    <select value={value} className="input-sm form-control" onChange={e => updateCb(e.target.value)}>{children}</select>

var option = (value, label) => //create an <option> given its value and onChange
    <option value={value} key={value}>{label}</option>

var dropdown = (
    value,//current value of the dropdown
    entries,//all possible options
    updateCb,//function to call when the <select> changes
    valueCb = identity,//function that returns the value
    labelCb = identity//function that returns the label
) => {
  var mapCb = entry => option(valueCb(entry), labelCb(entry));
  var entries = "function" == typeof entries.mapEntries ?
      entries.mapEntries(mapCb) :
      entries.map(mapCb);
  return <td>{select(value, updateCb, entries)}</td>
}

var getCurrencyName = currency => currency.name() + "(" + currency.code() + ")";

var ensureInt = cb => value => cb(parseInt(value));

export var view = AMP.view((model, actions) => {
  var {calendar, currency, ensuredFrom, ensuredTo, fromYears, toYears, calendars, currencies, translations} = model;
  var {changeCalendar, changeCurrency, changeFrom, changeTo, add} = actions;
  var __ = key => translations().get(key);
  var onAdd = add.bind(null, calendar(), currency(), ensuredFrom(), ensuredTo());
  return (
    <tr>
      {dropdown(calendar(), calendars(), ensureInt(changeCalendar), callFunc("id"), callFunc("name"))}
      {dropdown(currency(), currencies(), changeCurrency, callFunc("code"), getCurrencyName)}
      {dropdown(ensuredFrom(), fromYears(), ensureInt(changeFrom))}
      {dropdown(ensuredTo(), toYears(), ensureInt(changeTo))}
      <td>
        <button className="btn btn-default btn-sm" onClick={onAdd}>
          {__("amp.deflator:add")}
        </button>
      </td>
    </tr>
  )
});

export var translations = {
  "amp.deflator:add": "Add"
};

export var update = (action, model) => actions.match(action, {
  changeCalendar: model.calendar,
  changeCurrency: model.currency,
  changeFrom: model.from,
  changeTo: model.to,
  add: () => model
});