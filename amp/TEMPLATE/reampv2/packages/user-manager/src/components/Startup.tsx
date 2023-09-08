import React, { useEffect } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import { fetchUserProfile } from '../reducers/fetchUserProfileReducer';
import { useAppDispatch } from '../utils/hooks';
import EditProfile from './EditProfile';

const Startup = () => {
  const dispatch = useAppDispatch();
  const navigate = useNavigate();
  const location = useLocation();

  useEffect(() => {
    // eslint-disable-next-line no-unused-vars
    window.addEventListener('openUserModal', (_e) => {
      // if (location.pathname === '/edit-profile') {
      //   return;
      // }

      navigate('/edit-profile');
    });
  }, []);

  useEffect(() => {
    dispatch(fetchUserProfile());
  }, []);

  return (
    <div className="App">
      <EditProfile />
    </div>
  );
};

export default Startup;
