import { IndicatorObjectType } from '../../../admin/indicator_manager/types';
import { errorHelper } from "../../../admin/indicator_manager/utils/errorHelper";
import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";
import { REST_INDICATORS } from '../../utils/constants';

const REDUCER_NAME = 'indicators';

type IndicatorInitialStateType = {
    indicators: IndicatorObjectType[];
    loading: boolean;
    error: any;
}

const initialState: IndicatorInitialStateType = {
    indicators: [],
    loading: false,
    error: null,
}

export const fetchIndicators = createAsyncThunk(
    `${REDUCER_NAME}/fetch`,
    async (_, { rejectWithValue }) => {
        const response = await fetch(REST_INDICATORS)
        const data: IndicatorObjectType [] = await response.json();

        if (response.status !== 200) {
            return rejectWithValue(data);
        }

        return data.sort((a, b) => a.name.localeCompare(b.name));
    }
);

const fetchIndicatorSlice = createSlice({
    name: REDUCER_NAME,
    initialState,
    reducers: {
        resetState : () => initialState,
    },
    extraReducers: (builder) => {
        builder.addCase(fetchIndicators.pending, (state) => {
            state.loading = true;
        });
        builder.addCase(fetchIndicators.fulfilled, (state, action) => {
            state.indicators = [];
            state.loading = false;
            state.indicators = action.payload;
        });
        builder.addCase(fetchIndicators.rejected, (state, action) => {
            state.loading = false;
            state.error = errorHelper(action.payload);

        });
    }
});

export const {
    resetState
} = fetchIndicatorSlice.actions;

export default fetchIndicatorSlice.reducer;
