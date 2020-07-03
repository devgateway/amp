import React from 'react';
import './GenericTooltip.css';

class GenericToolTip extends React.Component {
    render() {
        const {titleLabel, color, values} = this.props;
        let headerStyle = {backgroundColor: color};
        return (
            <div className='generic-tooltip' >
                <div className="tooltip-header" style={headerStyle}>
                    {titleLabel}
                </div>
                <div className="inner">
                    {values.map(v => {
                        return (<div className={'row'}>
                            <div className={'col-md-12'}>{`${v.label}`}</div>
                        </div>)
                    })}
                </div>
            </div>
        )
    }
}

export default GenericToolTip;
