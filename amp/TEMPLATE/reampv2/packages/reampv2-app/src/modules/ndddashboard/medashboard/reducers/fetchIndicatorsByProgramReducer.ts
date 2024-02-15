import { errorHelper } from '../../../admin/indicator_manager/utils/errorHelper';
import { IndicatorObjectType } from '../../../admin/indicator_manager/types';
import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";
import { REST_INDICATORS_BY_PROGRAM } from '../../utils/constants';

const REDUCER_NAME = 'indicatorsByProgram';
type IndicatorInitialStateType = {
    data: IndicatorObjectType[];
    loading: boolean;
    error: any;
}

const initialState: IndicatorInitialStateType = {
    data: [],
    loading: false,
    error: null
}

export const fetchIndicatorsByProgram = createAsyncThunk(
    `${REDUCER_NAME}/fetch`,
    async (id: number, { rejectWithValue }) => {
        const response = await fetch(`${REST_INDICATORS_BY_PROGRAM}/${id}`);
        const data: IndicatorObjectType [] = await response.json();

        if (response.status !== 200) {
            return rejectWithValue(data);
        }

        return data.sort((a, b) => a.name.localeCompare(b.name));
    }
);

const indicatorsByProgramSlice = createSlice({
    name: REDUCER_NAME,
    initialState,
    reducers: {
        resetState : (state) => {
            state.data = [];
            state.loading = false;
            state.error = null;
        }
    },
    extraReducers: (builder) => {
        builder.addCase(fetchIndicatorsByProgram.pending, (state) => {
            state.loading = true;
        });
        builder.addCase(fetchIndicatorsByProgram.fulfilled, (state, action) => {
            state.data = [];
            state.loading = false;
            state.data = action.payload;
        });
        builder.addCase(fetchIndicatorsByProgram.rejected, (state, action) => {
            state.loading = false;
            state.error = errorHelper(action.payload);

        });
    }
});

export const {
    resetState
} = indicatorsByProgramSlice.actions;

export default indicatorsByProgramSlice.reducer;
