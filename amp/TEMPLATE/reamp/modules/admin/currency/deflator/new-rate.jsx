/** @flow */
/** @jsx h */
import * as AMP from "amp/architecture";
var {h} = AMP;
import __ from "amp/modules/translate";
import {allow, year} from "amp/tools/validate";

export class Action extends AMP.Action{}

export class YearChanged extends Action {
  constructor(year: integer){
    super();
    this.year = year;
  }
}

export class YearSubmitted extends Action{
  constructor(year: number){
    super();
    this.year = () => year;
  }
}

export class Model extends AMP.Model{}

export var model = new Model().set('year', '');

function getValidationError(model){
  var year = parseInt(model.year());
  if(!year) return;
  if (!(year >= MIN_YEAR && year <= MAX_YEAR)) {
    return (
      <span className="help-block">{__("The year must be between #$ and #$", MIN_YEAR, MAX_YEAR)}</span>
    )
  }

  if (!(year < model.getIn(['except', 'from']) || year > model.getIn(['except', 'to']))) {
    return (
      <span className="help-block">{__("There is already an entry for #$", year)}</span>
    )
  }

  return null;
}

export function view(address, model: Model){
  var submitYear = e => {
    e.preventDefault();
    address.send(new YearSubmitted(parseInt(e.target.querySelector('input').value)));
  };
  return (
    <td className="edit-on-hover add-new-rate">
      <form onsubmit={submitYear} action="#">
        <input
          className="form-control edit"
          placeholder={__('Enter the year and press Enter')}
          onkeypress={allow(year)}
          value={model.year()}
        />
        <button className="btn btn-primary view">{__('Add inflation rate')}</button>
      </form>
    </td>
  )
}

export function update(action: Action, model: Model){
  if(action instanceof YearChanged){
    return model.year(action.year);
  }
}