import * as AMP from "amp/architecture";
import React from "react";
import {t} from "amp/modules/translate";
import * as validate from "amp/tools/validate";
import style from "./style.less";
import cn from "classnames";

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

export var model = new Model({
  year: '',
  repeatedYearWarning: false
})

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
      <td className={cn("edit-on-hover add-new-rate", {"has-error": model.repeatedYearWarning()})}>
        <form onSubmit={submitYear} action="#">
          <input
            className="form-control edit"
            placeholder={t('amp.deflator:newRateHint')}
            onChange ={changeYear}
            value={model.year()}
          />
          <button className="btn btn-primary view">{t('amp.deflator:add')}</button>
          <div className="help-block">
            {model.repeatedYearWarning() ?
              t('amp.deflator:repeatedYear') + " " + model.year() :
              t('amp.deflator:yearConstraints')
            }
          </div>
        </form>
      </td>
    )
  }
}

NewRate.propTypes = NewRate.propTypes || {};
NewRate.propTypes.model = React.PropTypes.instanceOf(Model);
export {NewRate as view};

export function update(action: Action, model: Model){
  if(action instanceof YearChanged){
    return model.year(action.year());
  }
  if(action instanceof YearSubmitted){
    return model.year("");
  }
}