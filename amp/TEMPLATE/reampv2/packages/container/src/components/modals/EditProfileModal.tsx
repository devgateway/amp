import React, {Suspense} from 'react';
import {Provider} from "react-redux";
import {store} from "../../reducers/store";
const EditProfileModalLazy = React.lazy(() => import("userManager/EditProfileModal"));

interface EditProfileProps {
    show: boolean,
    setShow: React.Dispatch<React.SetStateAction<boolean>>
}
const EditProfileModal = ({ show , setShow }: EditProfileProps) => {
    return (
        <Suspense fallback={<div className="loading"/>}>
            <Provider store={store}>
                <EditProfileModalLazy store={store} show={show} setShow={setShow}/>
            </Provider>
        </Suspense>
    )
}
export default EditProfileModal;

