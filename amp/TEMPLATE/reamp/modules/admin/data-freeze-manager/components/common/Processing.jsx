import React, {Component} from 'react';
import Modal from 'react-modal';

const customStyles = {
    content : {
        top                   : '50%',
        left                  : '50%',
        right                 : 'auto',
        bottom                : 'auto',
        marginRight           : '-50%'
    }
};
export default class Processing extends Component {

    constructor() {
        super();
    }

    render() {
        if (!this.props.show) {
            return null;
        }
        return (
            <div>
                <Modal
                    isOpen = {this.props.show}
                    contentLabel = {this.props.message}
                    style = {customStyles}
                >{this.props.message}</Modal>
            </div>
        );
    }
}
