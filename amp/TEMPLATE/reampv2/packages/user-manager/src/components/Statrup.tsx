import React, { useEffect } from 'react';
import { fetchUserProfile } from '../reducers/fetchUserProfileReducer';
import { useAppDispatch } from '../utils/hooks';
import EditProfile from './EditProfile';

function Statrup() {
  const dispatch = useAppDispatch();

  window.addEventListener('openUserModal', (e) => {
    console.log(e);
    console.log('openUserModal inside user manager');
  });

  useEffect(() => {
    dispatch(fetchUserProfile());
  }, []);
  return (
    <div className="App">
      <EditProfile />
    </div>
  );
}
export default Statrup;
