import { errorHelper } from './../utils/errorHelper';
import { createAsyncThunk, createSlice, } from "@reduxjs/toolkit";
import { ProgramObjectType } from "../types";

type ProgramsInitialStateType = {
    programs: ProgramObjectType[];
    loading: boolean;
    error: any;
}

const initialState: ProgramsInitialStateType = {
    programs: [],
    loading: false,
    error: null,
}

export const getPrograms = createAsyncThunk(
    "programs/getPrograms",
    async (_, { rejectWithValue }) => {
        const response = await fetch("/rest/indicatorManager/programs");
        const data: ProgramObjectType [] = await response.json();

        if (response.status !== 200) {
            return rejectWithValue(data);
        }

        return data;
    }
);

const fetchProgramsSlice = createSlice({
    name: "programsData",
    initialState,
    reducers: {},
    extraReducers: (builder) => {
        builder.addCase(getPrograms.pending, (state) => {
            state.loading = true;
        });
        builder.addCase(getPrograms.fulfilled, (state, action) => {
            state.loading = false;
            state.programs = action.payload;
        });
        builder.addCase(getPrograms.rejected, (state, action) => {
            state.loading = false;
            state.error = errorHelper(action.payload);
    
        });
    }
});

export default fetchProgramsSlice.reducer;