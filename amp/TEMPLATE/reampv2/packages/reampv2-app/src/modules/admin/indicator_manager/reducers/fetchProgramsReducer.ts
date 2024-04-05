import { errorHelper } from './../utils/errorHelper';
import { createAsyncThunk, createSlice, } from "@reduxjs/toolkit";
import { ProgramObjectType, ProgramSchemeType } from "../types";
import { extractChildrenFromProgramScheme } from '../utils/helpers';

type ProgramsInitialStateType = {
    programSchemes: ProgramSchemeType[];
    programs: ProgramObjectType[];
    loading: boolean;
    error: any;
}

const initialState: ProgramsInitialStateType = {
    programSchemes: [],
    programs: [],
    loading: false,
    error: null,
}

export const getPrograms = createAsyncThunk(
    "programs/getPrograms",
    async (_, { rejectWithValue }) => {
        const response = await fetch("/rest/indicatorManager/programs");
        const data: ProgramSchemeType[] = await response.json();

        const children = extractChildrenFromProgramScheme(data);

        if (response.status !== 200) {
            return rejectWithValue(data);
        }

        return {
            programSchemes: data,
            programs: children.sort((a, b) => a.name.localeCompare(b.name))
        }
    }
);

const fetchProgramsSlice = createSlice({
    name: "programsData",
    initialState,
    reducers: {
        setProgramSchemes: (state, action) => {
            state.programSchemes = action.payload;
        },
        clearProgramSchemes: (state) => {
            state.programSchemes = [];
        },
        setPrograms: (state, action) => {
            state.programs = action.payload;
        },
        clearPrograms: (state) => {
            state.programs = [];
        }
    },
    extraReducers: (builder) => {
        builder.addCase(getPrograms.pending, (state) => {
            state.loading = true;
        });
        builder.addCase(getPrograms.fulfilled, (state, action) => {
            state.loading = false;
            state.programSchemes = action.payload.programSchemes;
            state.programs = action.payload.programs;
        });
        builder.addCase(getPrograms.rejected, (state, action) => {
            state.loading = false;
            state.error = errorHelper(action.payload);

        });
    }
});

export const { setProgramSchemes, clearProgramSchemes, setPrograms, clearPrograms } = fetchProgramsSlice.actions;
export default fetchProgramsSlice.reducer;
