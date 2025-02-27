import React from 'react';

const NotFound = () => {
    return (
        <div style={{
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'center',
            height: '65vh',
        }}>
            <div className="text-center">
                <h1 className="display-1 fw-bold">404</h1>
                <p className="fs-3"> <span className="text-danger">Opps!</span> Page not found.</p>
                <p className="lead">
                    The page you’re looking for doesn’t exist.
                </p>
                <a href="index.html" className="btn btn-primary">Go Back</a>
            </div>
        </div>
    )
}
export default NotFound;
