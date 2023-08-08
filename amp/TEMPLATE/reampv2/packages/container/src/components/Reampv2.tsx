import React, {MutableRefObject, useEffect, useRef} from 'react';
import { mount } from 'reampv2App/Reampv2App';

const Reampv2 = () => {
    const ref: MutableRefObject<any> = useRef(null);

    const isFirstRunRef = useRef(true);
    const unmountRef = useRef(() => {});

    useEffect(() => {
        if (!isFirstRunRef.current) {
            return;
        }

        unmountRef.current = mount(ref.current);
        isFirstRunRef.current = false;
    }, []);

    useEffect(() => unmountRef.current, []);

    return (
        <div ref={ref}/>
    );
}
export default Reampv2
