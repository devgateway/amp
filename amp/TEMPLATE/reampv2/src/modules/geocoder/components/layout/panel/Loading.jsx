import React from 'react';
import {ClipLoader} from "react-spinners";


export const Loading = () => (
    <>
    <div className="modal-backdrop show"></div>
    <div className="loading">
        <div className="loading-dialog">
            <div className="loading-content">
                <div className="loading-title">Geocoder Running</div>
                <div className="loading-spinner">
                    <ClipLoader
                        size={30}
                        loading={true}
                     />
                </div>
                <div className="loading-text">The GeoCoding Tool is currently running based on selections chosen to be GeoCoded</div>
            </div>
        </div>
    </div>
    </>
);
