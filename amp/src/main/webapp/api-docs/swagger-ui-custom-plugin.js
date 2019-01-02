const CaseInsensitiveFilterPlugin = function (system) {
    return {
        fn: {
            opsFilter: (taggedOps, phrase) => {
                return taggedOps.filter((tagObj, tag) => tag.toLowerCase().indexOf(phrase.toLowerCase()) !== -1);
            }
        }
    }
};

document.addEventListener('animationstart', (e) => {
  const { srcElement } = e;
  if (srcElement && srcElement.classList.contains('invalid')) {
    // we could jump to 'srcElement' directly, but when there are more than one invalid, user will see the last one, that's why searching the the first
    const firstParamWithError = srcElement.closest('tbody').querySelector('.invalid');
    firstParamWithError.scrollIntoView();
  }
});
