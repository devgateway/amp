const DIV_ROOT_ID = '_ampoffline_app_root';

import('./bootstrap').then(({ mount }) => {
    const appRoot = document.getElementById(DIV_ROOT_ID);

    if (appRoot) {
        mount({
            el: appRoot
        });
    }
});

export {};
