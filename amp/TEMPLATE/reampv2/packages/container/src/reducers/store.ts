import {
    Action, combineReducers, configureStore, ThunkAction,
} from '@reduxjs/toolkit';
import stateReducer from "./stateReducer";

// fetches the redux data from local storage
const loadState = () => {
    const serialState = localStorage.getItem('globalState');
    if (serialState === null || undefined) {
        return undefined;
    }
    return JSON.parse(serialState);
};

// this is the persisted state from local storage
const persistedState = loadState();

const staticReducers = {
    state: stateReducer,
};

const initialStore = {
    ...persistedState,
};

export const store = configureStore({
    reducer: combineReducers(staticReducers),
    preloadedState: initialStore,
});

export type AppDispatch = typeof store.dispatch;
export type RootState = ReturnType<typeof store.getState>;
export type AppThunk<ReturnType = void> = ThunkAction<
    ReturnType,
    RootState,
    unknown,
    Action<string>
>;
