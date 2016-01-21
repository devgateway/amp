import * as AMP from "amp/architecture";
import React from "react";
import {CURRENCIES_LIST, INFLATION_SOURCES, INFLATION_RATES} from "amp/config/endpoints";
import {fetchJson, postJson, callFunc} from "amp/tools";
import {Nav, NavItem, NavDropdown, MenuItem} from "react-bootstrap";
import cn from "classnames";
import {Glyphicon, ProgressBar, Alert} from "react-bootstrap";
import * as Rate from "./rate";
import * as ConstantCurrencies from "./constant-currencies";
import * as NewConstantCurrency from "./constant-currencies/new";
import {RequestStatus, showSave} from "./tools";
require('./style.less');

const TABS = {
  INFLATION_RATES: "inflation-rates",
  CONSTANT_CURRENCIES: "constant-currencies",
  INFLATION_SOURCES: "inflation-sources"
};

export var actions = AMP.actions({
  changeTab: "string",
  changeCurrentCurrency: "string",
  inflationRatesDownloadStarted: "number",
  inflationRatesDownloadSuccess: ["number", "object"],
  inflationRatesDownloadFail: "number",
  resetInflationDownloadStatus: "number",
  save: null,
  saveSuccess: null,
  saveFail: null,
  resetSaveStatus: null,
  rate: ["string", "string", Rate.actions],
  constantCurrencies: ConstantCurrencies.actions,
  undelete: Rate.Model,
  cleanTrash: "number"
});


class InflationSource extends AMP.Model{}

class Model extends AMP.Model{
  getCurrentCurrency(){
    var currentCurrencyCode = this.currentCurrencyCode();
    return this.currencies().find(currency => currency.code() == currentCurrencyCode);
  }
}

var parseRates = translations => rates => new AMP.Model(rates).map((value, period) => Rate.model
    .value(value)
    .period(period)
    .translations(translations)
);


var parseInflationSource = (parsed, data) => parsed.set(data.id, new InflationSource({
  id: data.id,
  name: data.name,
  desc: data.desc,
  currency: data.settings["currency-code"],
  frequency: data.settings.frequency,
  downloadStatus: RequestStatus.INITIAL
}));

var parseCurrency = inflationSources => (code, name) => new AMP.Model({
  code: code,
  name: name,
  hasSource: inflationSources.some(inflationSource => inflationSource.currency() == code)
});

var parseObject = parser => obj => new AMP.Model(Object.keys(obj).map( key => parser(key, obj[key]) ) );

var sortByHasSourceThenByCode = (a, b) => {
  if(a.hasSource() && !b.hasSource()) return -1;
  if(!a.hasSource() && b.hasSource()) return 1;
  return a.code().localeCompare(b.code())
};

var sortByPeriod = (a, b) => a.period().localeCompare(b.period());

export function init(translationsPromise){
  var parsedTranslationsPromise = translationsPromise.then(translations => new AMP.Model(translations));
  var inflationSourcesPromise = fetchJson(INFLATION_SOURCES)
      .then(
          all => all.reduce(parseInflationSource, new AMP.Model())
      );
  //currencies depend on inflation sources
  var currenciesPromise = Promise.all([inflationSourcesPromise, fetchJson(CURRENCIES_LIST)])
      .then(
          ([inflationSources, currencies]) =>
              parseObject(parseCurrency(inflationSources))(currencies).sort(sortByHasSourceThenByCode)
      )
  //inflationRates depend on translations
  var inflationRatesPromise = Promise.all([parsedTranslationsPromise, fetchJson(INFLATION_RATES)])
    .then(
        ([translations, inflationRates]) => new AMP.Model(inflationRates).map(parseRates(translations))
    )
  //new constant currencies depends on inflation rates, currencies and translations
  var constantCurrenciesPromise =
      ConstantCurrencies.init(currenciesPromise, parsedTranslationsPromise);
  return Promise.all([
      currenciesPromise,
      inflationSourcesPromise,
      inflationRatesPromise,
      parsedTranslationsPromise,
      constantCurrenciesPromise
  ]).then(([currencies, inflationSources, inflationRates, translations, constantCurrencies]) =>
      new Model({
        currentTab: TABS.INFLATION_RATES,
        currentCurrencyCode: currencies.getIn([0, 'code']),
        currencies: currencies,
        inflationSources: inflationSources,
        inflationRates: inflationRates,
        saveStatus: RequestStatus.INITIAL,
        translations: translations,
        newRate: null,
        constantCurrencies: constantCurrencies,
        trash: false
      })
  );
}

var maybeDownload = __ => ({inflationRatesDownloadStarted}) => inflationSources => {
  var wrap = dom => <small className="pull-right">
    {dom}
  </small>;

  if(inflationSources.some(inflationSource => inflationSource.downloadStatus() == RequestStatus.RUNNING)){
    return wrap(
      <ProgressBar active now={100} style={{width: 100}}/>
    )
  }

  if(inflationSources.some(inflationSource => inflationSource.downloadStatus() == RequestStatus.SUCCESS)){
    return wrap(
        <span className="label label-success">
          <i className="glyphicon glyphicon-ok"/>
          &nbsp;
          {__('amp.deflator:success')}
        </span>
    )
  }

  if(inflationSources.some(inflationSource => inflationSource.downloadStatus() == RequestStatus.FAIL)){
    return wrap(
        <span className="label label-danger">
          <i className="glyphicon glyphicon-remove"/>
          &nbsp;
          {__('amp.deflator:failed')}
        </span>
    )
  }

  if(inflationSources.size() < 1) return null;

  if(inflationSources.size() == 1){
    var inflationSource = inflationSources.pop();
    return wrap(
      <button className="btn btn-default" onClick={inflationRatesDownloadStarted.bind(null, inflationSource.id())}>
        <Glyphicon glyph="download-alt"/> {__('amp.deflator:downloadFrom')} {inflationSource.name()}
      </button>
    )
  }

  return wrap(
    <Dropdown id="download-dropdown">
      <Dropdown.Toggle>
        <Glyphicon glyph="download-alt"/> {__('amp.deflator:download')}
      </Dropdown.Toggle>
      <Dropdown.Menu>
        <MenuItem eventKey="1">Dropdown link</MenuItem>
        <MenuItem eventKey="2">Dropdown link</MenuItem>
      </Dropdown.Menu>
    </Dropdown>
  );
};

var maybeUndoPopup = (__, maybeRate, {cleanTrash, undelete}) => maybeRate ? (
    <Alert className="undo-popup" bsStyle="info" onDismiss={e => cleanTrash(maybeRate.deletedAt())}>
      {Rate.humanReadablePeriod(maybeRate.period())}&nbsp;
      {__('amp.deflator:rateDeleted')}&nbsp;
      <a href="javascript:void(0);" className="alert-link" onClick={e => undelete(maybeRate)}>
        {__('amp.deflator:undo')}
      </a>
    </Alert>
) : null;

export var InflationRates = AMP.view((model: Model, actions) => {
  var translations = model.translations();
  var __ = key => translations.get(key);
  var currency = model.getCurrentCurrency();
  var code = model.currentCurrencyCode();
  var inflationSources = model.inflationSources().filter(inflationSource => inflationSource.currency() == code);
  var inflationRates = model.inflationRates().get(code);
  return (
    <table className="table table-striped inflation-rates">
      <caption>
        <h2>
          {__('amp.deflator:inflationRatesFor')} {currency.name()} ({currency.code()})&nbsp;
          {maybeDownload(__)(actions)(inflationSources)}
        </h2>
      </caption>
      <thead>
      <tr>
        <th>{__('amp.deflator:timePeriod')}</th>
        <th>{__('amp.deflator:inflation')}</th>
        <th>{__('amp.deflator:actions')}</th>
      </tr>
      </thead>
      <tbody>
        {inflationRates ? inflationRates.sort(sortByPeriod).mapEntries(inflationRate => (
          <Rate.view
              key={inflationRate.period()}
              model={inflationRate.set('translations', translations)}
              actions={actions.rate(code, inflationRate.period())}
          />
        )):null}
      </tbody>
      <tfoot>
        <tr>
          <td colSpan="3" className="text-right">
            {maybeUndoPopup(__, model.trash(), actions)}
            {showSave(__)(actions.save)(model.saveStatus())}
          </td>
        </tr>
      </tfoot>
    </table>
  )
}, 'InflationRates');

var InflationSources = AMP.view((model, actions) => {
  var __ = key => model.translations().get(key);
  return (
      <table className="table table-striped">
        <caption>
          <h2>
            {__('amp.deflator:inflationSources')}
          </h2>
        </caption>
        <thead>
          <tr>
            <th>{__('amp.deflator:nameAndDescription')}</th>
            <th>{__('amp.deflator:currency')}</th>
            <th>{__('amp.deflator:frequency')}</th>
            <th>{__('amp.deflator:actions')}</th>
          </tr>
        </thead>
        <tbody>
        {model.inflationSources().mapEntries(source => (
          <tr key={source.id()}>
            <td className="title-and-desc">
              <strong>{source.name()}</strong>
              <p>{source.desc()}</p>
            </td>
            <td>{source.currency()}</td>
            <td>
              {function(frequency){
                switch(frequency){
                  case "Q": return __('amp.deflator:quarterly')
                  default: return "NOT IMPLEMENTED"
                }
              }(source.frequency())}
            </td>
            <td>
              {maybeDownload(__)(actions)(new AMP.Model([source]))}
            </td>
          </tr>
        ))}
        </tbody>
        <tfoot>
        </tfoot>
      </table>
  )});

export var view = AMP.view((model, actions) => {
  var {changeTab, changeCurrentCurrency} = actions;
  var translations = model.translations();
  var __ = key => translations.get(key);
  var currencies = model.currencies();
  var pipeOnlyStringsTo = cb => maybeString => "string" == typeof maybeString ? cb(maybeString) : null;
  return (
      <div className="container">
        <div className="row">
          <div className="col-md-12">
            <h1>{__('amp.deflator:title')}</h1>
          </div>
          <div className="col-md-12">
            <Nav bsStyle="tabs" activeKey={model.currentTab()} onSelect={pipeOnlyStringsTo(changeTab)}>
              <NavDropdown
                  className={cn({active: TABS.INFLATION_RATES == model.currentTab()})}
                  eventKey={TABS.INFLATION_RATES}
                  activeKey={model.currentCurrencyCode}
                  title={__('amp.deflator:inflationRates') + ": " + model.currentCurrencyCode()}
                  id="main-navigation"
                  onSelect={(_, currency) => changeCurrentCurrency(currency)}
              >
                {currencies.mapEntries(currency => (
                  <MenuItem
                    eventKey={currency.code()}
                    key={currency.code()}
                    href="javascript:void(0)"
                    active={model.currentCurrencyCode() == currency.code()}
                  >
                    {currency.code()}, {currency.name()}
                    {" "}
                    {currency.hasSource() ? <i className="glyphicon glyphicon-download-alt"/> : null}
                  </MenuItem>
                ))}
              </NavDropdown>
              <NavItem eventKey={TABS.CONSTANT_CURRENCIES} href="javascript:void(0);">{__('amp.deflator:constantCurrencies')}</NavItem>
              <NavItem eventKey={TABS.INFLATION_SOURCES} href="javascript:void(0);">{__('amp.deflator:inflationSources')}</NavItem>
            </Nav>
          </div>
          <div className="col-md-12">
            {function(currentTab){
                switch(currentTab){
                    case TABS.INFLATION_SOURCES: return <InflationSources model={model} actions={actions}/>;
                    case TABS.CONSTANT_CURRENCIES: return (
                      <ConstantCurrencies.view
                          model={model.constantCurrencies()}
                          actions={actions.constantCurrencies()}
                      />
                    )
                    default: return <InflationRates model={model} actions={actions}/>
                }
            }(model.currentTab())}
          </div>
        </div>
      </div>
  )
}, 'Deflator');

var downloadInflationRates = id => currency => actions => fetchJson(INFLATION_RATES + `/${id}`)
    .then(rates => actions.inflationRatesDownloadSuccess(id, rates[currency]))
    .catch(reason => actions.inflationRatesDownloadFail(id, reason));

var resetInflationDownloadStatus = id => actions => setTimeout(actions.resetInflationDownloadStatus.bind(null, id), 3000);

var save = inflationRates => actions =>
    postJson(INFLATION_RATES, inflationRates.map(currency => currency.map(callFunc('value'))).toJS())
        .then(response => (response.status >= 200 && response.status < 300) ?
            actions.saveSuccess() :
            actions.saveFail()
        ).catch(() => actions.saveFail());

var resetSaveStatus = actions => setTimeout(actions.resetSaveStatus, 3000);


var getInflationRateCurrency = model => sourceId => model.getIn(['inflationSources', sourceId, 'currency']);

var setInflationSourceDownloadStatus = model => id => status =>
    model.setIn(['inflationSources', id, 'downloadStatus'], status);

var cleanTrashEffect = timestamp => ({cleanTrash}) => setTimeout(
    () => document.querySelector(".undo-popup:hover") ? null : cleanTrash(timestamp)
    , 5000
);

export var update = (action, model) => actions.match(action, {
  changeTab: newTab => model.currentTab(newTab),

  changeCurrentCurrency: newCurrency => model.currentTab(TABS.INFLATION_RATES).currentCurrencyCode(newCurrency),

  inflationRatesDownloadStarted: sourceId => [
    setInflationSourceDownloadStatus(model)(sourceId)(RequestStatus.RUNNING),
    downloadInflationRates(sourceId)(getInflationRateCurrency(model)(sourceId))
  ],

  inflationRatesDownloadSuccess: (sourceId, rates) => [
    setInflationSourceDownloadStatus(model)(sourceId)(RequestStatus.SUCCESS)
      .setIn(['inflationRates', getInflationRateCurrency(model)(sourceId)], parseRates(model.translations())(rates)),
    resetInflationDownloadStatus(sourceId)
  ],

  inflationRatesDownloadFail: (sourceId, reason) =>  [
    setInflationSourceDownloadStatus(model)(sourceId)(RequestStatus.FAIL),
    resetInflationDownloadStatus(sourceId)
  ],

  resetInflationDownloadStatus: sourceId => setInflationSourceDownloadStatus(model)(sourceId)(RequestStatus.INITIAL),

  save: () => [
    model.saveStatus(RequestStatus.RUNNING),
    save(model.inflationRates())
  ],

  saveSuccess: () => [
    model.saveStatus(RequestStatus.SUCCESS),
    resetSaveStatus
  ],

  saveFail: () => [
    model.saveStatus(RequestStatus.FAIL),
    resetSaveStatus
  ],

  resetSaveStatus: () => model.saveStatus(RequestStatus.INITIAL),

  cleanTrash: timestamp => false !== model.trash() && timestamp == model.trash().deletedAt() ?
      model.trash(false) :
      model,

  undelete: target => model.setIn(
        ['inflationRates', target.currency(), target.period()]
        , target.deletedAt(null).unset('currency')
    ).trash(false),

  rate: (currency, period, inflationRateAction) => Rate.actions.match(inflationRateAction, {
    remove: () => {
      if(!model.getIn(['inflationRates', currency, period])) debugger;
      var timestampedRate = model.getIn(['inflationRates', currency, period])
          .deletedAt(new Date().getTime())
          .set('currency', currency);
      return [
        model.trash(timestampedRate).unsetIn(['inflationRates', currency, period]),
        cleanTrashEffect(timestampedRate.deletedAt())
      ]
    },
    _: () =>  AMP.updateSubmodel(['inflationRates', currency, period], Rate.update, inflationRateAction, model)
  }),

  constantCurrencies: constantCurrenciesAction => {
    var result = AMP.updateSubmodel(['constantCurrencies'], ConstantCurrencies.update, constantCurrenciesAction, model);
    var [submodel, sideEffect] = result;
    var redirectSideEffect = () => [
      submodel,
      sideEffect ? actions => sideEffect(actions.constantCurrencies()) : sideEffect
    ];
    return ConstantCurrencies.actions.match(constantCurrenciesAction, {
      save: redirectSideEffect,
      saveSuccess: redirectSideEffect,
      saveFail: redirectSideEffect,
      newConstantCurrency: redirectSideEffect,
      entry: redirectSideEffect,
      _: () => result
    })
  }
});

export var translations = {
  ...Rate.translations,
  ...ConstantCurrencies.translations,
  "amp.deflator:title": "Currency deflator",
  "amp.deflator:inflationRates": "Inflation rates",
  "amp.deflator:constantCurrencies": "Constant currencies",
  "amp.deflator:inflationSources": "Inflation sources",
  "amp.deflator:currencies": "Currencies",
  "amp.deflator:inflationRatesFor": "Inflation rates for",
  "amp.deflator:timePeriod": "Time period",
  "amp.deflator:inflation": "Inflation(%)",
  "amp.deflator:downloadFrom": "Download from",
  "amp.deflator:download": "Download",
  "amp.deflator:save": "Save",
  "amp.deflator:actions": "Actions",
  "amp.deflator:success": "Success",
  "amp.deflator:failed": "Failed",
  "amp.deflator:nameAndDescription": "Name and description",
  "amp.deflator:currency": "Currency",
  "amp.deflator:downloadAll": "Download all",
  "amp.deflator:frequency": "Frequency",
  "amp.deflator:quarterly": "Quarterly",
  "amp.deflator:rateDeleted": "rate deleted."
};