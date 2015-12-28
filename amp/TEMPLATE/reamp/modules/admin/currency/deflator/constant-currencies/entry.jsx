import React from "react";
import * as AMP from "amp/architecture";

export class Model extends AMP.Model{}

export var init = (...promises) =>
    Promise.all(promises).then(([calendars, currencies, translations]) => new Model({
      calendar: null,
      currency: null,
      from: null,
      to: null,
      calendars: calendars,
      currencies: currencies,
      translations: translations
    }));

export var actions = AMP.actions({
  remove: null
});

export var view = AMP.view((model, actions) => {
  var currentCurrency = model.currencies().find(currency => currency.code() == model.currency());
  var __ = key => model.translations().get(key);
  return (
      <tr>
        <td>{currentCurrency.name()} ({currentCurrency.code()})</td>
        <td>{model.getIn(['calendars', model.calendar(), 'name'])}</td>
        <td>{model.from()}</td>
        <td>{model.to()}</td>
        <td>
          <button className="btn btn-default btn-xs" onClick={e => actions.remove()}>
            <i className="glyphicon glyphicon-trash"/> {__("amp.deflator:delete")}
          </button>
        </td>
      </tr>
  )
});

export var update = (action, model) => model;

export var translations = {
  "amp.deflator:delete": "Delete"
};