import {ProgressBar, Glyphicon} from "react-bootstrap";
import React from "react";

export const RequestStatus = {
  INITIAL: 0,
  RUNNING: 1,
  SUCCESS: 2,
  FAIL: 3
};

var status = (labelClass, glyph, text) => (
    <span className={`label label-${labelClass}`}>
      <Glyphicon glyph={glyph}/>
      &nbsp;
      {text}
    </span>
);

export var showSave = __ => saveAction => saveStatus => {
  switch(saveStatus){
    case RequestStatus.RUNNING: return <ProgressBar className="pull-right" active now={100} style={{width: 100}}/>;
    case RequestStatus.SUCCESS: return status("success", "ok", __('amp.deflator:success'));
    case RequestStatus.FAIL: return status("danger", "remove", __('amp.deflator:failed'));
    default: return (
        <button className="btn btn-success" onClick={saveAction}>
          {__('amp.deflator:save')}
        </button>
    )
  }
};