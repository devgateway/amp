import * as AMP from "amp/architecture";
import __ from "amp/modules/translate";
import * as Rate from "./rate";
import * as NewRate from "./new-rate";
import cn from "classnames";
import {MIN_YEAR, MAX_YEAR} from "amp/tools/validate";
import React from "react";

export class Action extends AMP.Action{}
class Save extends Action{}
class SaveSuccess extends Save{}
class SaveFailed extends Save{
  constructor(reason){
    super();
    this.reason = () => reason;
  }
}

class RateAction extends AMP.Package{}

class SaveSideEffect extends AMP.SideEffect{
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

class InflationRates extends AMP.Model{
    constructor(mutationOrData){
    super(mutationOrData);
    var startYear = Math.min(...super.keys());
    var endYear = Math.max(...super.keys());
    this.startYear = () => startYear;
    this.endYear = () => endYear;
  }

  get(key){
    return !super.get(key) && this.startYear()< key && key < this.endYear() ? Rate.model.set('year', key) : super.get(key);
  }

  keys(){
    var arr = [];
    for(var year = this.startYear(); year <= this.endYear(); year++){
      arr.push(year);
    }
    return arr;
  }
}

class Currency extends AMP.Model{}
class Currencies extends AMP.Model{}
class State extends AMP.Model{
  currentCurrency(){
    return this.currencies().get(this.currentCurrencyCode());
  }
}

class Model extends AMP.Model{
  saved: State;
  current: State;
}
var model = new Model();

var callFunc = func => obj => obj[func]();

var fetchJson = url => fetch(url, {credentials: 'same-origin'}).then(callFunc('json'));

var ensureArray = maybeArray => Array.isArray(maybeArray) ? maybeArray : [];

//We need to talk to endpoints in order to figure out our model, so this function will return a promise that will resolve
//with the model once we have all the data we need
export var init = () =>
  Promise.all([
    fetchJson('/rest/currencies/inflatableCurrencies'),
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
      lastSaveStatus: "",
      saved: state,
      current: state
    })
  });

class Deflator extends AMP.View {
  render() {
    var {address, model} = this.props;
    var state = model.current();
    var haveChanges = !model.saved().equals(state);
    var currentCurrency = state.currentCurrency();
    var currentInflationRates = currentCurrency.inflationRates();
    return (
      <div className="container">
        <div className="row">
          <div className="col-md-12">
            <table className="table table-striped">
              <caption>
                <h2>
                  {__('Inflation rates for')} {currentCurrency.name()}
                  ({currentCurrency.code()})
                </h2>
              </caption>
              <thead>
              <tr>
                <th>{__('Year')}</th>
                <th>{__('Inflation(%)')}</th>
                <th>{__('Constant currency')}</th>
                <th>{__('Delete')}</th>
              </tr>
              </thead>
              <tbody>
              {currentInflationRates.map(rate => {
                var isFirstOrLast = rate.year() == currentInflationRates.startYear()
                  || rate.year() == currentInflationRates.endYear();
                var inflationRate = rate.inflationRate();
                var isValid = parseFloat(inflationRate) == inflationRate;
                var model = rate.set('deletable', false).deletable(isFirstOrLast)
                  .set('valid', isValid);
                return <Rate.view address={address.usePackage(RateAction, rate.get('year'))} model={model}/>
              })}
              </tbody>
              <tfoot>
              <tr>
                <NewRate.view address={address} model={state.newRate()}/>
                <td colSpan="3" className="text-right">

                  &nbsp;
                  <button
                    className={cn("btn btn-success", {disabled: !haveChanges})}
                    disabled={!haveChanges}
                    onClick={e => address.send(new Save())}
                  >
                    {__('Save')}
                  </button>
                  <div className="help-block">
                    {model.lastSaveStatus() || (!haveChanges && __("No changes"))}
                  </div>
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

Deflator.propTypes.model = React.PropTypes.instanceOf(Model);
export {Deflator as view};

export function update(action: AMP.Action, model:Model){
  if(action instanceof Save){
    if(action instanceof SaveSuccess){
      return model
        .saved(model.current())
        .set('lastSaveStatus', __('Save successful'));
    }
    if(action instanceof SaveFailed){
      return model.set('lastSaveStatus', __('Save failed: #$', action.reason()));
    }
    return new SaveSideEffect(model);
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