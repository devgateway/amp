import { TypedUseSelectorHook, useDispatch, useSelector } from 'react-redux';
import React, { useEffect, useRef } from 'react';
import type { RootState, AppDispatch } from '../reducers/store';

// Use throughout your app instead of plain `useDispatch` and `useSelector`
export const useAppDispatch = () => useDispatch<AppDispatch>();
export const useAppSelector: TypedUseSelectorHook<RootState> = useSelector;

/**
 * @param func - Function to be executed after the first render
 * @param deps - Dependencies for the effect
 * @description - This hook is used to execute a function after the first render
 */
export const useDidMountEffect = (func: React.EffectCallback, deps?: React.DependencyList | undefined) => {
  const didMount = useRef(false);

  useEffect(() => {
    if (didMount.current) func();
    else didMount.current = true;
    // eslint-disable-next-line
  }, [...(deps || [])]);
};
