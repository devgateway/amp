const CaseInsensitiveFilterPlugin = function (system) {
    return {
        fn: {
            opsFilter: (taggedOps, phrase) => {
                return taggedOps.filter((tagObj, tag) => tag.toLowerCase().indexOf(phrase.toLowerCase()) !== -1);
            }
        }
    }
};