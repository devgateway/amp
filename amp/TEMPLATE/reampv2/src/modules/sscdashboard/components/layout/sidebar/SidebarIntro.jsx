import React, { Component } from "react";
import { SSCTranslationContext } from '../../StartUp';

class SidebarIntro extends Component {

    render() {
        const {translations} = this.context;
        return (
            <div className="sidebar-intro">
                {this.props.text.map(t => {
                    return (<p key={t}>{translations[t]}</p>)
                })}
            </div>
        );
    }
}

export default SidebarIntro;

SidebarIntro.contextType = SSCTranslationContext;
