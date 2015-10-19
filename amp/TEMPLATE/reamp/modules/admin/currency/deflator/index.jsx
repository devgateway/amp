import * as AMP from "amp/architecture";
import * as Rate from "./rate";
import * as NewRate from "./new-rate";
import cn from "classnames";
import {MIN_YEAR, MAX_YEAR} from "amp/tools/validate";
import React from "react";
import {fetchJson, range} from "amp/tools";
import {INFLATABLE_CURRENCIES} from "amp/config/endpoints";
import Currency from "../model";
import style from "./style.less";

export var actions = AMP.actions({
  saveStarted: null,
  saveSucceeded: null,
  saveFailed: 'string',
  resetSaveStatus: null,
  rate: ['number', Rate.actions],
  newRate: NewRate.actions
});

var SaveStatus = {
  INITIAL: 0,
  SAVING: 1,
  SUCCESS: 2,
  FAIL: 3
};

var save = model => actions => {
  var currentCurrency = model.current().currentCurrency();
  var currencyCode = currentCurrency.code();
  fetch(`/rest/currencies/setInflationRate/${currencyCode}`, {
    method: 'post',
    credentials: 'same-origin',
    headers: {
      'Accept': 'application/json',
      'Content-Type': 'application/json'
    },
    body: JSON.stringify({
      rates: currentCurrency.inflationRates().map(entry => entry.year(parseInt).inflationRate(parseFloat).toJS())
    })
  }).then(response => {
    if (response.status >= 200 && response.status < 300) {
      actions.saveSucceeded();
    } else {
      actions.saveFailed(response.statusText);
    }
  });
};

var resetSaveStatus = actions => setTimeout(actions.resetSaveStatus, 3000);

var focusPrev = domNode => domNode.parentNode.parentNode.previousSibling.querySelector(".edit").focus();

var focusNext = domNode => domNode.parentNode.parentNode.nextSibling.querySelector(".edit").focus();

class InflationRates extends AMP.Model{
  constructor(mutationOrData){
    super(mutationOrData);
    var startYear = Math.min(...this.keys());
    var endYear = Math.max(...this.keys());
    this.startYear = () => startYear;
    this.endYear = () => endYear;
  }

  set(year, val){
    if(!this.empty()){
      if(year > this.endYear() + 1){
        return this.set(year - 1, Rate.model.year(year - 1)).set(year, val);
      }
      if(year < this.startYear() - 1){
        return this.set(year + 1, Rate.model.year(year + 1)).set(year, val);
      }
    }
    return super.set(year, val);
  }
}

class Currencies extends AMP.Model{}
class State extends AMP.Model{
  currentCurrency(){
    return this.currencies().get(this.currentCurrencyCode());
  }
}

class Model extends AMP.Model{
  saved: State;
  current: State;
  status: number;
  saveFailReason: string;
  currentInflationRates(){
    return this.current().currentCurrency().inflationRates();
  }
}
var model = new Model();

var ensureArray = maybeArray => Array.isArray(maybeArray) ? maybeArray : [];

//We need to talk to endpoints in order to figure out our model, so this function will return a promise that will resolve
//with the model once we have all the data we need
export var init = () =>
  Promise.all([
    fetchJson(INFLATABLE_CURRENCIES),
    fetchJson('/rest/currencies/getInflationRates')
  ]).then(([currencies, inflationRates]) => {
    var getInflationRatesFor = code =>
      ensureArray(inflationRates[code]).map(inflationRate =>
          new Rate.Model(inflationRate).set('deletable', false).set('valid', true))
      .reduce((inflationRates, inflationRate) =>
          inflationRates.set(inflationRate.year(), inflationRate), new InflationRates());
    var state = new State({
      newRate: NewRate.model,
      currentCurrencyCode: currencies[0]['code'],
      currencies: currencies
        .map(currency => new Currency(currency))
        .map(currency => currency.set('inflationRates', getInflationRatesFor(currency.code())))
        .reduce((currencies, currency) => currencies.set(currency.code(), currency), new Currencies())
    });
    return new Model({
      status: SaveStatus.INITIAL,
      saveFailReason: "",
      saved: state,
      current: state,
      translations: null
    })
  });

var haveChanges = model => !model.saved().equals(model.current());

var ratesAreValid = model =>
  model.current().currentCurrency().inflationRates()
    .every(inflationRate => {
      var inflationRate = inflationRate.inflationRate();
      return parseFloat(inflationRate) == inflationRate;
    });

var getValidationMessage = model => {
  var __ = key => model.getIn(['translations', key]);
  if(!ratesAreValid(model)){
    return (
      <div className="help-block">
        {__('amp.deflator:invalidRates')}
      </div>
    )
  }

  if(!haveChanges(model)){
    return (
      <div className="help-block">
        {__('amp.deflator:noChanges')}
      </div>
    )
  }
};

var getSaveStatus = model => {
  var __ = key => model.getIn(['translations', key]);
  switch(model.status()){
    case SaveStatus.SAVING:
      return (
        <div className="help-block">
          <div className="progress">
            <div className="progress-bar progress-bar-striped active" style={{width: '100%'}}/>
          </div>
        </div>
      )
    case SaveStatus.SUCCESS:
      return (
        <div className="help-block">
          <span className="label label-success">
            <i className="glyphicon glyphicon-ok"/>
            &nbsp;
            {__('amp.deflator:saved')}
          </span>
        </div>
      )
    case SaveStatus.FAIL:
      return (
        <div className="help-block">
          <span className="label label-danger">
            <i className="glyphicon glyphicon-remove"/>
            &nbsp;
            {__('amp.deflator:savingFailed') + " " + model.saveFailReason()}
          </span>
        </div>
      )
  }
};

var savingInProgress = model => model.status() == SaveStatus.SAVING;

var disableSaving = model => !haveChanges(model) || !ratesAreValid(model) || savingInProgress(model);

export var view = AMP.view((model: Model, actions) => {
  var translations = model.translations();
  var __ = key => translations.get(key);
  var state = model.current();
  var currentCurrency = state.currentCurrency();
  var currentInflationRates = currentCurrency.inflationRates();
  var repeatedYear = currentInflationRates.has(state.newRate().year());
  return (
    <div className="container">
      <div className="row">
        <div className="col-md-12">
          <table className="table table-striped">
            <caption>
              <h2>
                {__('amp.deflator:title')} {currentCurrency.name()}
                ({currentCurrency.code()})
              </h2>
            </caption>
            <thead>
            <tr>
              <th>{__('amp.deflator:year')}</th>
              <th>{__('amp.deflator:inflation')}</th>
              <th className="constant-currency">
                <span>
                  {__('amp.deflator:constantCurrency')}
                  <div className="tooltip bottom" role="tooltip">
                    <div className="tooltip-arrow"></div>
                    <div className="tooltip-inner">
                      {__('amp.deflator:constantCurrencyHelp')}
                    </div>
                  </div>
                </span>
              </th>
              <th>{__('amp.deflator:delete')}</th>
            </tr>
            </thead>
            <tbody>
            {currentInflationRates.sortKeys().map(rate => {
              var year = rate.year();
              var deletable = year == currentInflationRates.startYear() || year == currentInflationRates.endYear();
              var inflationRate = rate.inflationRate();
              var model = rate.deletable(deletable).valid(parseFloat(inflationRate) == inflationRate);
              return <Rate.view actions={actions.rate(year)} model={model} key={year}/>
            })}
            </tbody>
            <tfoot>
            <tr>
              <NewRate.view
                actions={actions.newRate()}
                model={state.newRate().repeatedYearWarning(repeatedYear).translations(translations)}
              />
              <td colSpan="3" className="text-right">
                <button
                  className={cn("btn btn-success", {disabled: disableSaving(model)})}
                  disabled={disableSaving(model)}
                  onClick={actions.saveStarted}
                >
                  {__('amp.deflator:save')}
                </button>
                {getValidationMessage(model)}
                {getSaveStatus(model)}
              </td>
            </tr>
            </tfoot>
          </table>
        </div>
      </div>
    </div>
  )
});

var getPathForYear = model => year =>
  ['current', 'currencies', model.current().currentCurrencyCode(), 'inflationRates', year];

var addEmptyRate = model => year => model.setIn(getPathForYear(model)(year), Rate.model.year(year));

var inc = number => number + 1;
var dec = number => number - 1;

var navigate = (model, except, target, year, nextYear, sideEffect) => domNode => {
  if(year == except) return model;
  return [year == target ? addEmptyRate(model)(nextYear) : model, sideEffect.bind(null, domNode)]
};

export var update = (action, model) => actions.match(action, {
  saveStarted: () => [model.status(SaveStatus.SAVING), save(model)],

  saveSucceeded: () => [model.saved(model.current()).status(SaveStatus.SUCCESS), resetSaveStatus],

  saveFailed: reason => [model.status(SaveStatus.FAIL).saveFailReason(reason), resetSaveStatus],

  resetSaveStatus: () => model.status(SaveStatus.INITIAL),

  rate: (year, rateAction) => Rate.actions.match(rateAction, {
    remove: () => model.unsetIn(getPathForYear(model)(year)),

    up: navigate(model, MIN_YEAR, model.currentInflationRates().startYear(), year, year - 1, focusPrev),

    down: navigate(model, MAX_YEAR, model.currentInflationRates().endYear(), year, year + 1, focusNext),

    _: () => AMP.updateSubmodel(getPathForYear(model)(year), Rate.update, rateAction, model)
  }),

  newRate: newRateAction => NewRate.actions.match(newRateAction, {
    yearSubmitted: year => AMP.updateSubmodel(['current', 'newRate'], NewRate.update, newRateAction,
            !model.hasIn(getPathForYear(model)(year)) ? addEmptyRate(model)(year) : model),

    _: () => AMP.updateSubmodel(['current', 'newRate'], NewRate.update, newRateAction, model)
  })
});

var newRateTranslations = NewRate.translations;

export var translations = {
  ...newRateTranslations,
  "amp.deflator:invalidRates": "One or more rates are invalid. Please verify the rates highlighted in red",
  "amp.deflator:noChanges": "No changes",
  "amp.deflator:saved": "Saved",
  "amp.deflator:save": "Save",
  "amp.deflator:savingFailed": "Saving failed",
  "amp.deflator:title": "Inflation rates for",
  "amp.deflator:year": "Year",
  "amp.deflator:inflation": "Inflation(%)",
  "amp.deflator:constantCurrency": "Constant currency",
  "amp.deflator:constantCurrencyHelp": "Select which deflated currencies you want to convert to real currencies",
  "amp.deflator:delete": "Delete"
};