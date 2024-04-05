import React, { useEffect, useRef } from 'react';

/**
 * @param func - Function to be executed after the first render
 * @param deps - Dependencies for the effect
 * @description - This hook is used to execute a function after the first render
 */
const useDidMountEffect = (func: React.EffectCallback, deps?: React.DependencyList | undefined) => {
    const didMount = useRef(false);

    useEffect(() => {
        if (didMount.current) func();
        else didMount.current = true;
    // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [...(deps || [])]);
}

export default useDidMountEffect;