(function(config) {
    var input = document.getElementById(config.inputId);
    var preview = document.getElementById(config.previewId);
    var noImage = document.getElementById(config.noImageId);
    var error = document.getElementById(config.errorId);

    if (!input) return;

    input.addEventListener('change', function (e) {
        error.textContent = '';
        var file = e.target.files[0];

        if (!file) {
            preview.style.display = 'none';
            noImage.style.display = 'inline';
            return;
        }

        var allowedTypes = ['image/png', 'image/jpeg'];
        if (!allowedTypes.includes(file.type)) {
            error.textContent = config.invalidTypeMsg;
            input.value = '';
            return;
        }

        if (file.size > config.maxSize) {
            error.textContent = config.fileSizeMsg;
            input.value = '';
            return;
        }

        var reader = new FileReader();
        reader.onload = function (event) {
            var img = new Image();
            img.onload = function () {
                if (img.width > config.maxWidth || img.height > config.maxHeight) {
                    error.textContent = config.dimensionsMsg;
                    input.value = '';
                    preview.style.display = 'none';
                    noImage.style.display = 'inline';
                } else {
                    preview.src = event.target.result;
                    preview.style.display = 'inline';
                    noImage.style.display = 'none';
                }
            };
            img.src = event.target.result;
        };
        reader.readAsDataURL(file);
    });
})(window.imageUploadConfig);
