import * as AMP from "amp/architecture";
import React from "react";
import __ from "amp/modules/translate";
import * as validate from "amp/tools/validate";

export class Action extends AMP.Action{}

export class YearChanged extends Action {
  constructor(year: string){
    super();
    this.year = () => year;
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
};

class NewRate extends AMP.View{
  render(){
    var {address, model} = this.props;
    var changeYear = e => {
      var value = e.target.value;
      if(0 == value.length || validate.year(value)){
        address.send(new YearChanged(value))
      }
    }
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
            onChange ={changeYear}
            value={model.year()}
          />
          <button className="btn btn-primary view">{__('Add inflation rate')}</button>
        </form>
      </td>
    )
  }
}

NewRate.propTypes.model = React.PropTypes.instanceOf(Model);
export {NewRate as view};

export function update(action: Action, model: Model){
  if(action instanceof YearChanged){
    return model.year(action.year());
  }
}