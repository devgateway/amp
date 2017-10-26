import React from "react";
import * as AMP from "amp/architecture";
import * as Rate from "./rate";
import style from "./style.less";
import {showSave, maybeDownload} from "../tools";
import {Glyphicon, ProgressBar, Alert} from "react-bootstrap";

var sortByPeriod = (a, b) => a.period().localeCompare(b.period());

export var maybeUndoPopup = (__, maybeRate, {cleanTrash, undelete}) => maybeRate ? (
    <Alert className="undo-popup" bsStyle="info" onDismiss={e => cleanTrash(maybeRate.deletedAt())}>
      {Rate.humanReadablePeriod(maybeRate.period())}&nbsp;
      {__('amp.deflator:rateDeleted')}&nbsp;
      <a href="javascript:void(0);" className="alert-link" onClick={e => undelete(maybeRate)}>
        {__('amp.deflator:undo')}
      </a>
    </Alert>
) : null;

export var view = AMP.view((model: Model, actions) => {
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