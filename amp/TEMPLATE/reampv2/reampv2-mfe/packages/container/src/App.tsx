import React, {Suspense} from 'react';

const AmpOfflineLazy = React.lazy(() => import('./components/AmpOffline'));

function App() {
    return (
        <div className="App">
            <Suspense fallback={<div>Loading...</div>}>
                <AmpOfflineLazy />
            </Suspense>
        </div>
    );
}

export default App;
