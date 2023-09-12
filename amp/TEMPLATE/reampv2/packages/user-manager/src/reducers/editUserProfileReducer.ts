import { createAsyncThunk, createSlice } from '@reduxjs/toolkit';
import { toast } from 'react-toastify';
import { EditUserProfile, UserProfile } from '../types';
import { REST_EDIT_USER_PROFILE } from '../utils/constants';
import { errorHelper } from '../utils/errorHelper';

const REDUCER_NAME = 'editUserProfile';

export type EditUserProfileInitialStateType = {
    user: EditUserProfile | null;
    loading: boolean;
    error: any;
};

const initialState: EditUserProfileInitialStateType = {
  user: null,
  loading: false,
  error: null,
};

export const editUserProfile = createAsyncThunk(
  `${REDUCER_NAME}/editUserProfile`,
  async (user: EditUserProfile, { rejectWithValue }) => {
    const response = await fetch(`${REST_EDIT_USER_PROFILE}/${user.id}`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(user),
    });
    const data: UserProfile | any = await response.json();

    if (response.status !== 200) {
      toast.error('Error updating user profile');
      return rejectWithValue(data);
    }

    toast.success('User profile updated');
    return data;
  },
);

const editUserProfileSlice = createSlice({
  name: REDUCER_NAME,
  initialState,
  reducers: {},
  extraReducers: (builder) => {
    builder.addCase(editUserProfile.pending, (state) => {
      state.loading = true;
    });
    builder.addCase(editUserProfile.fulfilled, (state, action) => {
      state.loading = false;
      state.user = action.payload;
    });
    builder.addCase(editUserProfile.rejected, (state, action) => {
      state.loading = false;
      state.error = errorHelper(action.payload);
    });
  },
});

export default editUserProfileSlice.reducer;
