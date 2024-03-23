//eslint-disable
import { legacy_createStore as createStore, applyMiddleware, compose, Store } from '@reduxjs/toolkit';
import thunk from 'redux-thunk';
import rootReducer from './rootReducer';

const checkreduxDevTools = () => {
    // @ts-ignore
    if (typeof window !== 'undefined' && window.__REDUX_DEVTOOLS_EXTENSION_COMPOSE__) {
        // @ts-ignore
        return window.__REDUX_DEVTOOLS_EXTENSION_COMPOSE__;
    }
    return (...args: any[]) => {
        if (args.length === 0) {
            return undefined;
        }
        if (args.some(arg => typeof arg === 'object')) {
            return compose;
        }
        return compose(...args);
    };
};

const composeEnhancer = checkreduxDevTools();
// redux store
// eslint-disable-next-line
export const store: Store = createStore(rootReducer, composeEnhancer(applyMiddleware(thunk)));
