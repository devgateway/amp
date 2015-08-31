/** @jsx h */

import * as AMP from "amp/architecture";
var {h} = AMP;
import __ from "amp/modules/translate";
import * as Rate from "./rate";
import * as NewRate from "./new-rate";
import cn from "classnames";

export class Action extends AMP.Action{}
class Save extends Action{}

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
      })
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
      saved: state,
      current: state
    })
  });

export function view(address, model: Model){
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
                (
                  {currentCurrency.code()}
                )
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
                return Rate.view(address.usePackage(RateAction, rate.get('year')), model)
              })}
            </tbody>
            <tfoot>
              <tr>
                {NewRate.view(address, state.newRate())}
                <td colSpan="3" className="text-right">
                  <button
                    className={cn("btn btn-success", {disabled: !haveChanges})}
                    disabled={!haveChanges}
                    onclick={e => address.send(new Save())}
                  >
                    {__('Save')}
                  </button>
                </td>
              </tr>
            </tfoot>
          </table>
        </div>
      </div>
    </div>
  )
}

export function update(action: AMP.Action, model:Model){
  if(action instanceof Save){
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
    if(action instanceof NewRate.YearSubmitted){
      let path = ['current', 'currencies', model.current().currentCurrencyCode(), 'inflationRates', action.year()];
      return model.hasIn(path) ? model : model.setIn(path, Rate.model.year(action.year()))
    }
    return AMP.updateSubmodel(['current', 'newRate'], NewRate.update, action, model);
  }
}