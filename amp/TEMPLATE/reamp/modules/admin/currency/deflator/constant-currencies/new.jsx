import * as AMP from "amp/architecture";
import React from "react";
import {callFunc, identity, fetchJson} from "amp/tools";
import {SETTINGS} from "amp/config/endpoints";

export class Model extends AMP.Model{}

export var init = (inflationRatesPromise, currenciesPromise, calendarsPromise, translationsPromise) =>
  Promise.all([inflationRatesPromise, currenciesPromise, calendarsPromise, translationsPromise])
    .then(
      ([inflationRates, currencies, calendars, translations]) => {
        var defaultCurrency = currencies.head().code();
        var defaultYear = inflationRates.has(defaultCurrency) ?
            inflationRates.get(defaultCurrency).years().head() :
            null;
        return new Model({
          calendar: calendars.head().id(),
          currency: defaultCurrency,
          from: defaultYear,
          to: defaultYear,
          inflationRates: inflationRates,
          calendars: calendars,
          currencies: currencies,
          translations: translations
        })
      }
    );

export var actions = AMP.actions({
  changeCalendar: "string",
  changeCurrency: "string",
  changeFrom: "string",
  changeTo: "string",
  add: ["string", "string", "number", "number"],
  addRates: "string"
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
) => <td>{select(value, updateCb, entries.mapEntries(entry => option(valueCb(entry), labelCb(entry))))}</td>

var getCurrencyName = currency => currency.name() + "(" + currency.code() + ")";

var ensureYearInBounds = bounds => year => bounds.has(year) ? year : bounds.head();

function maybeSave(
  __,//translator function
  inflationRates,//all inflation rates
  currency,//currently selected currency in the add new constant currency diaolog
  fromValue,//current value of "from year" select
  toValue,//current value fo "to year" select
  changeFrom,//action to be called when from year changes
  changeTo,//action to be called when to year changes
  addRates,//action to be called when user wants to add missing rates
  add//action to be called when adding a rate
){
  var fromYears = inflationRates.has(currency) ? inflationRates.get(currency).years() : null;
  if(null === fromYears) return (
      <td colSpan="3">
        {__("amp.deflator:cannotAddConstantCurrency")}
        &nbsp;
        <a href="javascript:void(0)" onClick={addRates}>{__("amp.deflator:add")}?</a>
      </td>
  );
  var toYears = fromYears.filter(year => year >= fromValue);
  var ensuredYear = ensureYearInBounds(toYears)(toValue);
  return [
    dropdown(fromValue, fromYears, changeFrom),
    dropdown(ensuredYear, toYears, changeTo),
    <td>
      <button className="btn btn-default btn-sm" onClick={e => add(ensuredYear)}>
        {__("amp.deflator:add")}
      </button>
    </td>
  ]
}


export var view = AMP.view((model, actions) => {
  var {calendar, currency, from, to, calendars, currencies, inflationRates, translations} = model;
  var {changeCalendar, changeCurrency, changeFrom, changeTo, addRates, add} = actions;
  var __ = key => translations().get(key);
  var onAddRates = addRates.bind(null, currency());
  var onAdd = add.bind(null, calendar(), currency(), from());
  return (
    <tr>
      {dropdown(calendar(), calendars(), changeCalendar, callFunc("id"), callFunc("name"))}
      {dropdown(currency(), currencies(), changeCurrency, callFunc("code"), getCurrencyName)}
      {maybeSave(__, inflationRates(), currency(), from(), to(), changeFrom, changeTo, onAddRates, onAdd)}
    </tr>
  )
});

export var translations = {
  "amp.deflator:cannotAddConstantCurrency": "Selected currency has no inflation rates.",
  "amp.deflator:add": "Add"
};

export var update = (action, model) => actions.match(action, {
  changeCalendar: model.calendar,
  changeCurrency: model.currency,
  changeFrom: model.from,
  changeTo: model.to,
  add: () => model,
  addRates: () => model
});