/** @jsx h */
import * as AMP from "amp/architecture";
var {h} = AMP;
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

export function view(address, model: Model){
  var onDelete = e => address.send(new Delete());
  var onChange = e => address.send(new Change(e.target.value));
  var onToggleConstant = e => address.send(new ToggleConstant(e.target.checked));
  var inflationRate = model.inflationRate();
  return (
    <tr className={cn('inflation-rate-entry', {"danger has-error": !model.valid()})}>
      <td>
        <span className="form-control input-sm view">{model.get('year')}</span>
      </td>
      <td className="edit-on-hover inflation-rate">
        <input className="form-control input-sm edit" required
           onkeypress={allow(negative(point(number)))} value={inflationRate} onkeyup={onChange}
        />
        <span className="form-control input-sm view">{inflationRate}</span>
      </td>
      <td>
        <input type="checkbox" checked={model.constantCurrency()} onchange={onToggleConstant}/>
      </td>
      <td>
        {model.deletable() ?
          <i onclick={onDelete} className="glyphicon glyphicon-trash"></i>
        : null}
      </td>
    </tr>
  )
}

export function update(action: Action, model: Model){
  if(action instanceof Change){
    return model.inflationRate(action.inflationRate);
  }
  if(action instanceof ToggleConstant){
    return model.constantCurrency(action.constant);
  }
}