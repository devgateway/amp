import React from "react";
import * as AMP from "amp/architecture";
import cn from "classnames";

export class Model extends AMP.Model{
  constructor(whatever){
    super(whatever);
    this.currentCurrency = () => this.currencies().find(({code}) => code() == this.currency());
    this.currentCalendarName = () => this.getIn(['calendars', this.calendar(), 'name'])
  }

  isIntersectedOrTouched(calendar, currency, from, to){
    return calendar == this.calendar() &&
           currency == this.currency() &&
           !(
              to < this.from() - 1 ||
              from > this.to() + 1
           );
  }

  checkMerging({open, calendar, currency, from, ensuredTo}){
    return this.highlight(open() && this.isIntersectedOrTouched(calendar(), currency(), from(), ensuredTo()));
  }
}

export var init = (...promises) =>
    Promise.all(promises).then(([calendars, currencies, translations]) => new Model({
      calendar: null,
      currency: null,
      from: null,
      to: null,
      calendars: calendars,
      currencies: currencies,
      translations: translations,
      highlight: false,
      deletedAt: null
    }));

export var actions = AMP.actions({
  remove: null
});

export var view = AMP.view(({__, currentCurrency, highlight, calendar, from, to, currentCalendarName}, actions) => (
    <tr className={cn({info: highlight()})}>
      <td>{currentCurrency().name()} ({currentCurrency().code()})</td>
      <td>{currentCalendarName()}</td>
      <td>{from()}</td>
      <td>{to()}</td>
      <td>
        <button className="btn btn-default btn-xs" onClick={e => actions.remove()}>
          <i className="glyphicon glyphicon-trash"/> {__("amp.deflator:delete")}
        </button>
      </td>
    </tr>
));

export var update = (action, model) => model;

export var translations = {
  "amp.deflator:delete": "Delete"
};