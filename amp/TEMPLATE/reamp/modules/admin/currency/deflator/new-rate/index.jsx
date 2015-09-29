import * as AMP from "amp/architecture";
import React from "react";
import {t} from "amp/modules/translate";
import * as validate from "amp/tools/validate";
import style from "./style.less";
import cn from "classnames";

export var actions = AMP.actions({
  yearChanged: 'string',
  yearSubmitted: 'number'
});

export class Model extends AMP.Model{}

export var model = new Model({
  year: '',
  repeatedYearWarning: false
});

var changeYear = yearChanged => e => {
  var value = e.target.value;
  if(0 == value.length || validate.year(value)){
    yearChanged(value);
  }
};

var submitYear = yearSubmitted => e => {
  e.preventDefault();
  yearSubmitted(parseInt(e.target.querySelector('input').value));
};

export var view = AMP.view(({repeatedYearWarning, year}, {yearChanged, yearSubmitted}) => (
    <td className={cn("edit-on-hover add-new-rate", {"has-error": repeatedYearWarning()})}>
      <form onSubmit={submitYear(yearSubmitted)} action="#">
        <input
          className="form-control edit"
          placeholder={t('amp.deflator:newRateHint')}
          onChange ={changeYear(yearChanged)}
          value={year()}
        />
        <button className="btn btn-primary view">{t('amp.deflator:add')}</button>
        <div className="help-block">
          {repeatedYearWarning() ?
          t('amp.deflator:repeatedYear') + " " + year() :
              t('amp.deflator:yearConstraints')
          }
        </div>
      </form>
    </td>
));

export var update = (action, model: Model) => actions.match(action, {
  yearChanged: model.year,
  yearSubmitted: () => model.year("")
});