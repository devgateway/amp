import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";
import { ProgramConfig, InitialState } from '../types';
import { REST_PROGRAM_CONFIGURATION } from "../../utils/constants";
import { errorHelper } from "../../../admin/indicator_manager/utils/errorHelper";

const REDUCER_NAME = 'programConfiguration';

export const fetchProgramConfiguration = createAsyncThunk(
    `${REDUCER_NAME}/fetch`,
    async (_ , { rejectWithValue }) => {
        const response = await fetch(REST_PROGRAM_CONFIGURATION);

        const data: ProgramConfig [] = await response.json();

        if (response.status !== 200) {
            return rejectWithValue(data);
        }

        return data.sort((a, b) => a.name.localeCompare(b.name));
    }
);

interface ProgramConfigurationState extends InitialState {
    data: ProgramConfig [] | null;
}

const initialState: ProgramConfigurationState = {
    data: null,
    loading: false,
    error: null
};

const programConfigurationSlice = createSlice({
    name: REDUCER_NAME,
    initialState,
    reducers: {
        resetProgramConfiguration: () => initialState
    },
    extraReducers: (builder) => {
        builder.addCase(fetchProgramConfiguration.pending, (state) => {
            state.loading = true;
        });
        builder.addCase(fetchProgramConfiguration.fulfilled, (state, { payload }) => {
            state.loading = false;
            state.data = payload;
        });
        builder.addCase(fetchProgramConfiguration.rejected, (state, { payload }) => {
            state.loading = false;
            state.error = errorHelper(payload);
        });
    }
});

export const { resetProgramConfiguration } = programConfigurationSlice.actions;
export default programConfigurationSlice.reducer;
