import React from "react";
import * as AMP from "amp/architecture";

export var view = AMP.view((model, actions) => (
    <tr>
      <td>
        <input className="form-control input-sm" required/>
      </td>
      <td>
        <input className="form-control input-sm" required/>
      </td>
      <td>
        <button className="btn btn-default btn-sm">
          Add
        </button>
      </td>
    </tr>
))