// @flow
import React, {Component} from 'react';


export default class App extends Component {

    constructor(props, context) {
        super(props, context);
    }
    /*

        static propTypes = {
        children: PropTypes.element.isRequired
    };

    static contextTypes = {
        store: React.PropTypes.object.isRequired
    };
*/
    render() {
        console.log('render');
        return (
            <div >
                {this.props.children}
            </div>
        );
    }
}
