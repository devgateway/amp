import * as AMP from "amp/architecture";
import {t} from "amp/modules/translate";
import * as Rate from "./rate";
import * as NewRate from "./new-rate";
import cn from "classnames";
import {MIN_YEAR, MAX_YEAR} from "amp/tools/validate";
import React from "react";
import {fetchJson, range} from "amp/tools";
import {INFLATABLE_CURRENCIES} from "amp/config/endpoints";
import Currency from "../model";
import style from "./style.less";

export class Action extends AMP.Action{}
class Save extends Action{}
class SaveSuccess extends Save{}
class SaveFailed extends Save{
  constructor(reason){
    super();
    this.reason = () => reason;
  }
}
class ResetSaveStatus extends Save{}
var SaveStatus = {
  INITIAL: 0,
  SAVING: 1,
  SUCCESS: 2,
  FAIL: 3
};

class RateAction extends AMP.Package{}

class SaveSideEffect extends AMP.effects.SideEffect{
  constructor(model){
    super(model, address => {
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
          address.send(new SaveSuccess());
        } else {
          address.send(new SaveFailed(response.statusText))
        }
      });
    });
  }
}

class ResetSaveStatusSideEffect extends AMP.effects.TimeoutSideEffect{
  constructor(model){
    super(model, 3000, address => address.send(new ResetSaveStatus));
  }
}

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
      ensureArray(inflationRates[code]).map(inflationRate => new Rate.Model(inflationRate))
      .reduce((inflationRates, inflationRate) => inflationRates.set(inflationRate.year(), inflationRate), new InflationRates());
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
      current: state
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
  if(!ratesAreValid(model)){
    return (
      <div className="help-block">
        {t('amp.deflator:invalidRates')}
      </div>
    )
  }

  if(!haveChanges(model)){
    return (
      <div className="help-block">
        {t('amp.deflator:noChanges')}
      </div>
    )
  }
};

var getSaveStatus = model => {
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
            {t('amp.deflator:saved')}
          </span>
        </div>
      )
    case SaveStatus.FAIL:
      return (
        <div className="help-block">
          <span className="label label-danger">
            <i className="glyphicon glyphicon-remove"/>
            &nbsp;
            {t('amp.deflator:savingFailed') + " " + model.saveFailReason()}
          </span>
        </div>
      )
  }
}

var savingInProgress = model => model.status() == SaveStatus.SAVING;

var disableSaving = model => !haveChanges(model) || !ratesAreValid(model) || savingInProgress(model);

class Deflator extends AMP.View {
  render() {
    var {address, model} = this.props;
    var state = model.current();
    var currentCurrency = state.currentCurrency();
    var currentInflationRates = currentCurrency.inflationRates();
    return (
      <div className="container">
        <div className="row">
          <div className="col-md-12">
            <table className="table table-striped">
              <caption>
                <h2>
                  {t('amp.deflator:title')} {currentCurrency.name()}
                  ({currentCurrency.code()})
                </h2>
              </caption>
              <thead>
              <tr>
                <th>{t('amp.deflator:year')}</th>
                <th>{t('amp.deflator:inflation')}</th>
                <th className="constant-currency">
                  <span>
                    {t('amp.deflator:constantCurrency')}
                    <div className="tooltip bottom" role="tooltip">
                      <div className="tooltip-arrow"></div>
                      <div className="tooltip-inner">
                        {t('amp.deflator:constantCurrencyHelp')}
                      </div>
                    </div>
                  </span>
                </th>
                <th>{t('amp.deflator:delete')}</th>
              </tr>
              </thead>
              <tbody>
              {currentInflationRates.map(rate => {
                var deletable = rate.year() == currentInflationRates.startYear() || rate.year() == currentInflationRates.endYear();
                var inflationRate = rate.inflationRate();
                var isValid = parseFloat(inflationRate) == inflationRate;
                var model = rate.set('deletable', false).deletable(deletable)
                  .set('valid', isValid);
                return <Rate.view address={address.usePackage(RateAction, rate.get('year'))} model={model}/>
              })}
              </tbody>
              <tfoot>
              <tr>
                <NewRate.view address={address} model={state.newRate()}/>
                <td colSpan="3" className="text-right">
                  <button
                    className={cn("btn btn-success", {disabled: disableSaving(model)})}
                    disabled={disableSaving(model)}
                    onClick={e => address.send(new Save())}
                  >
                    {t('amp.deflator:save')}
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
  }
}

Deflator.propTypes = Deflator.propTypes || {};
Deflator.propTypes.model = React.PropTypes.instanceOf(Model);
export {Deflator as view};

export function update(action: AMP.Action, model:Model){
  if(action instanceof Save){
    if(action instanceof SaveSuccess){
      return new ResetSaveStatusSideEffect(
        model.saved(model.current()).status(SaveStatus.SUCCESS)
      )
    }
    if(action instanceof SaveFailed){
      return new ResetSaveStatusSideEffect(
        model.status(SaveStatus.FAIL).saveFailReason(action.reason())
      )
    }
    if(action instanceof ResetSaveStatus){
      return model.status(SaveStatus.INITIAL);
    }
    return new SaveSideEffect(model.status(SaveStatus.SAVING))
  }
  if(action instanceof RateAction){
    let path = ['current', 'currencies', model.current().currentCurrencyCode(), 'inflationRates', action.getTag()];
    var originalAction = action.unpack();
    if(originalAction instanceof Rate.Delete){
      return model.unsetIn(path);
    }
    return AMP.updateSubmodel(path, Rate.update, originalAction, model);
  }
  if(action instanceof NewRate.Action){
    var submodel = AMP.updateSubmodel(['current', 'newRate'], NewRate.update, action, model);
    if(action instanceof NewRate.YearSubmitted){
      let path = ['current', 'currencies', model.current().currentCurrencyCode(), 'inflationRates', action.year()];
      return !model.hasIn(path) && MIN_YEAR <= action.year() && action.year() <= MAX_YEAR ?
        submodel.setIn(path, Rate.model.year(action.year())) :
        model;
    }
    return submodel;
  }
}