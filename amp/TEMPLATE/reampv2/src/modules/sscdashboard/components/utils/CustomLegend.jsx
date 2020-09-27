import React, {Component} from 'react';
import './customLegend.css';
import {COLOR_MAP} from '../../utils/constants';

export default class CustomLegend extends Component {

    render() {
        const {data} = this.props;
        return (
            <div className="custom-legend">
                <ul className={data.length > 3 ? 'two-rows' : ''}>
                    {data.map(d => {
                        return (
                            <li key={COLOR_MAP.get(d.code)}>
                                <div className={"row"}>
                                    <div className="col-md-1 col-xs-1">
                                        <span className="symbol"
                                              style={{border: `2px solid ${COLOR_MAP.get(d.code)}`}}></span>
                                    </div>
                                    <div className="col-md-9 col-xs-9 label">{d.simpleLabel}
                                        <span
                                            className={"label percentage"}>{`${d.percentage}%`}</span>
                                    </div>
                                </div>
                            </li>
                        )
                    })}
                </ul>
            </div>
        )
    }
};

