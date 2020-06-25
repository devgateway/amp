import React, { Component } from "react";

class SidebarIntro extends Component {
    render() {
        return (
            <div className="sidebar-intro">
                {this.props.text.map(t => {
                    return (<p>{t}</p>)
                })}
            </div>
        );
    }
}

export default SidebarIntro;
