import React, {MutableRefObject, useEffect, useRef} from 'react';
import { mount } from 'ampoffline/AmpOfflineApp';

const AmpOffline = () => {
    const ref: MutableRefObject<any> = useRef(null);

    const isFirstRunRef = useRef(true);
    const unmountRef = useRef(() => {});

    useEffect(() => {
        if (!isFirstRunRef.current) {
            return;
        }

        unmountRef.current = mount({
            el: ref.current,
            standalone: false
        });
        isFirstRunRef.current = false;
    }, []);

    useEffect(() => unmountRef.current, []);

    return (
        <div ref={ref}/>
    )
};

export default AmpOffline;
