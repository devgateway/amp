import React from 'react';

export const Loading = () => (
  <div className="jumbotron">
    <div className="progress">
      <div
        className="progress-bar progress-bar-striped bg-info"
        role="progressbar"
        aria-valuenow="100"
        aria-valuemin="0"
        aria-valuemax="100"
        style={{ width: '100%' }}>
        Loading...
      </div>
    </div>
  </div>
);
