import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";;
import { REST_INDICATORS_BY_CLASSIFICATION } from '../../utils/constants';

const reducerName = 'fetchIndicatorsByClassification';

type IndicatorsByClassificationInitialStateType = {
    data: any;
    loading: boolean;
    error: any;
}

const initialState: IndicatorsByClassificationInitialStateType = {
    data: null,
    loading: false,
    error: null
}


export const fetchIndicatorsByClassification = createAsyncThunk(
    `${reducerName}/fetch`,
    async ( id: any, { rejectWithValue }) => {
        const response = await fetch(`${REST_INDICATORS_BY_CLASSIFICATION}/${id}`);
        const data = await response.json();

        if (response.status !== 200) {
            return rejectWithValue(data);
        }
        return data.sort((a: any, b: any) => a.name.localeCompare(b.name));
    }
);

export const indicatorsByClassificationSlice = createSlice({
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
        builder.addCase(fetchIndicatorsByClassification.pending, (state) => {
            state.loading = true;
        });
        builder.addCase(fetchIndicatorsByClassification.fulfilled, (state, action) => {
            state.data = null;
            state.loading = false;
            state.data = action.payload;
        });
        builder.addCase(fetchIndicatorsByClassification.rejected, (state, action) => {
            state.loading = false;
            state.error = action.payload;
        });
    }
});


export const { resetState } = indicatorsByClassificationSlice.actions;

export default indicatorsByClassificationSlice.reducer;
