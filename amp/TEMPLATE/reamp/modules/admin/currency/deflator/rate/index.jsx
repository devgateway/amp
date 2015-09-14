import React from "react";
import * as AMP from "amp/architecture";
import style from "./style.less";
import __ from "amp/modules/translate";
import cn from "classnames";
import {allow, negative, point, number} from "amp/tools/validate";

export class Model extends AMP.Model{
  inflationRate: string;
  deletable: boolean;
  valid: boolean;
}

export var model = new Model({
  constantCurrency: false,
  inflationRate: "",
  year: "",
  deletable: false,
  valid: false
});

export class Action extends AMP.Action{}
export class Delete extends Action{}
export class Change extends Action{
  constructor(newInflationRate: string){
    super();
    this.inflationRate = newInflationRate;
  }
}
export class ToggleConstant extends Action{
  constructor(constant: boolean){
    super();
    this.constant = constant;
  }
}

class Rate extends AMP.View{
  render(){
    var {address, model} = this.props;
    var onDelete = e => address.send(new Delete());
    var onChange = e => {
      var value = e.target.value;
      if(0 == value.length || negative(point(number))(value)){
        address.send(new Change(value));
      }
    };
    var onToggleConstant = e => address.send(new ToggleConstant(e.target.checked));
    var inflationRate = model.inflationRate();
    return (
      <tr className={cn('inflation-rate-entry', {"danger has-error": !model.valid()})}>
        <td>
          <span className="form-control input-sm view">{model.get('year')}</span>
        </td>
        <td className="edit-on-hover inflation-rate">
          <input className="form-control input-sm edit" required
            value={inflationRate} onChange={onChange}
          />
          <span className="form-control input-sm view">{inflationRate}</span>
        </td>
        <td>
          <input type="checkbox" checked={model.constantCurrency()} onChange={onToggleConstant}/>
        </td>
        <td>
          {model.deletable() ?
            <i onClick={onDelete} className="glyphicon glyphicon-trash"></i>
            : null}
        </td>
      </tr>
    )
  }
}

Rate.propTypes = Rate.propTypes || {};
Rate.propTypes.model = React.PropTypes.instanceOf(Model);

export {Rate as view}

export function update(action: Action, model: Model){
  if(action instanceof Change){
    return model.inflationRate(action.inflationRate);
  }
  if(action instanceof ToggleConstant){
    return model.constantCurrency(action.constant);
  }
}