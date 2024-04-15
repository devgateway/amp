import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";;
import { REST_INDICATORS_BY_SECTOR } from '../../utils/constants';
import {errorHelper} from "../../../admin/indicator_manager/utils/errorHelper";

const reducerName = 'fetchIndicatorsBySector';

type IndicatorsBySectorInitialStateType = {
    data: any;
    loading: boolean;
    error: any;
}


const initialState: IndicatorsBySectorInitialStateType = {
    data: null,
    loading: false,
    error: null
}

export const fetchIndicatorsBySector = createAsyncThunk(
    `${reducerName}/fetch`,
    async ( id: any, { rejectWithValue }) => {
        const response = await fetch(`${REST_INDICATORS_BY_SECTOR}/${id}`);
        const data = await response.json();

        if (response.status !== 200) {
            return rejectWithValue(data);
        }
        return data.sort((a: any, b: any) => a.name.localeCompare(b.name));
    }
);

const indicatorsBySectorSlice = createSlice({
    name: reducerName,
    initialState,
    reducers: {
        resetState : (state) => {
            state.data = null;
            state.loading = false;
            state.error = null;
        }
    },
    extraReducers: (builder) => {
        builder.addCase(fetchIndicatorsBySector.pending, (state) => {
            state.loading = true;
        });
        builder.addCase(fetchIndicatorsBySector.fulfilled, (state, action) => {
            state.error = null;
            state.loading = false;
            state.data = action.payload;
        });
        builder.addCase(fetchIndicatorsBySector.rejected, (state, action) => {
            state.loading = false;
            state.data = null;
            state.error = errorHelper(action.payload)
        });
    }
});

export const { resetState } = indicatorsBySectorSlice.actions;
export default indicatorsBySectorSlice.reducer;
