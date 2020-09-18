import React from 'react';

const PrintDummy = (props) => {
    const {friendly} = props;
    return (<>
        <div id={`print${friendly ? '-friendly' : ''}-dummy`}>
            <div id={`print${friendly ? '-friendly' : ''}-dummy-container`}/>
        </div>
        <div id={`print${friendly ? '-friendly' : ''}-simple-dummy`}>
            <div id={`print${friendly ? '-friendly' : ''}-simple-dummy-container`}/>
        </div>
    </>)
};
export default PrintDummy;
