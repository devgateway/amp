import React from 'react';
import {ClipLoader} from "react-spinners";


export const Loading = (props) => (
    <>
    <div className="modal-backdrop show"></div>
    <div className="loading">
        <div className="loading-dialog">
            <div className="loading-content">
                <div className="loading-title">{props.title}</div>
                <div className="loading-spinner">
                    <ClipLoader
                        size={30}
                        loading={true}
                     />
                </div>
                <div className="loading-text">{props.text}</div>
            </div>
        </div>
    </div>
    </>
);
