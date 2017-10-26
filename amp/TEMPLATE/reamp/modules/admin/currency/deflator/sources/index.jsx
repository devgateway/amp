import React from "react";
import * as AMP from "amp/architecture";
import {maybeDownload} from "../tools";

export var view = AMP.view((model, actions) => {
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