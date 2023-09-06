import { createSlice } from '@reduxjs/toolkit';

const REDUCER_NAME = 'state';

export type StateInitialStateType = {
    state: any;
}

const initialState: StateInitialStateType = {
    state: null,
}

const stateSlice = createSlice({
    name: REDUCER_NAME,
    initialState,
    reducers: {
        setState: (state, action) => {
            state.state = action.payload;
        },
        clearState: (state) => {
            state.state = null;
        }
    }
});

export const { setState, clearState } = stateSlice.actions;
export default stateSlice.reducer;
