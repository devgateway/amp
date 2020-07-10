import React, { Component } from 'react';
import './customLegend.css';
import { SECTORS_DECIMAL_POINTS_CHART } from '../../utils/constants';

export default class CustomLegend extends Component {

    render() {
        const {colors, data} = this.props;
        return (
            <div className="custom-legend">
                <ul className={data.length > 3 ? 'two-rows' : ''}>
                    {data.map(d => {
                        return (
                            <li key={colors[d.id]}>
                                <div className={"row"}>
                                    <div className="col-md-1">
                                        <span className="symbol" style={{border: `2px solid ${colors[d.id]}`}}></span>
                                    </div>
                                    <div className="col-md-9 label">{d.simpleLabel}
                                        <span
                                            className={"label percentage"}>{`${d.percentage.toFixed(SECTORS_DECIMAL_POINTS_CHART)}%`}</span>
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

