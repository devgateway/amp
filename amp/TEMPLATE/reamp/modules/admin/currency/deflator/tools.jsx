import React from "react";
import {ProgressBar, Glyphicon} from "react-bootstrap";

//Describes an XHR request status
export const RequestStatus = {
  INITIAL: 0,//no XHR running atm
  RUNNING: 1,//request running
  SUCCESS: 2,//request resulted in success
  FAIL: 3//request resulted in failure
};


//used to display "statuses", a bootstrap label with a glyphicon and message text
var status = (labelClass, glyph, text) => (
    <span className={`label label-${labelClass}`}>
      <Glyphicon glyph={glyph}/>
      &nbsp;
      {text}
    </span>
);

//maps RequestStatus to DOM
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

//show a display
export var maybeDownload = __ => ({inflationRatesDownloadStarted}) => inflationSources => {
  var wrap = dom => <small className="pull-right">
    {dom}
  </small>;

  if(inflationSources.some(inflationSource => inflationSource.downloadStatus() == RequestStatus.RUNNING)){
    return wrap(
        <ProgressBar active now={100} style={{width: 100}}/>
    )
  }

  if(inflationSources.some(inflationSource => inflationSource.downloadStatus() == RequestStatus.SUCCESS)){
    return wrap(status("success", "ok", __('amp.deflator:success')))
  }

  if(inflationSources.some(inflationSource => inflationSource.downloadStatus() == RequestStatus.FAIL)){
    return wrap(status("danger", "remove", __('amp.deflator:failed')))
  }

  if(inflationSources.size() < 1) return null;

  if(inflationSources.size() == 1){
    var inflationSource = inflationSources.pop();
    return wrap(
        <button className="btn btn-default" onClick={inflationRatesDownloadStarted.bind(null, inflationSource.id())}>
          <Glyphicon glyph="download-alt"/> {__('amp.deflator:downloadFrom')} {inflationSource.name()}
        </button>
    )
  }

  return wrap(
      <Dropdown id="download-dropdown">
        <Dropdown.Toggle>
          <Glyphicon glyph="download-alt"/> {__('amp.deflator:download')}
        </Dropdown.Toggle>
        <Dropdown.Menu>
          <MenuItem eventKey="1">Dropdown link</MenuItem>
          <MenuItem eventKey="2">Dropdown link</MenuItem>
        </Dropdown.Menu>
      </Dropdown>
  );
};