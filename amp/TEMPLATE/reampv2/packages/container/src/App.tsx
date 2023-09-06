import React, { useEffect, useState } from 'react';
import Router from "./routing";
import EditProfileModal from "./components/modals/EditProfileModal";
import {EDIT_PROFILE_MODAL_EVENT_NAME} from "./utils/constants";

const App = () => {
    const [showEditProfileModal, setShowEditProfileModal] = useState(false);

    // window.addEventListener(EDIT_PROFILE_MODAL_EVENT_NAME, () => {
    //     setShowEditProfileModal(true);
    //     console.log('showing user modal in the container')
    // })
    return (
        <div>
            <EditProfileModal show={showEditProfileModal} setShow={setShowEditProfileModal}/>
            <Router />
            <Router />
        </div>
    );
}

export default App;
