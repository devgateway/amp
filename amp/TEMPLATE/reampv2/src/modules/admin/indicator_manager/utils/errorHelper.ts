export const errorHelper = <T>(error: T | any[]) => {
    if (error === null) {
        return 'No error';
    }

    if (typeof error === 'string') {
        return error;
    }

    const tempError = error as any;

    if (tempError) {
        const extractedError = tempError.error["0000"][0];

        if (!extractedError) {
            return "An error occurred.";
        }

        return extractedError;
    }

};
