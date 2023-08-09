const DIV_ROOT_ID = 'reamp-v2-app-root';

import('./bootstrap').then(({ mount }) => {
    const appRoot = document.getElementById(DIV_ROOT_ID);

    if (appRoot) {
        console.log('Mounting app')
        mount({
            el: appRoot,
        });
    }
});

export {};
