import React, { useEffect, useRef } from 'react';

const useDidMountEffect = (func: React.EffectCallback, deps?: React.DependencyList | undefined) => {
    const didMount = useRef(false);

    useEffect(() => {
        if (didMount.current) func();
        else didMount.current = true;
    // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [...(deps || [])]);
}

export default useDidMountEffect;