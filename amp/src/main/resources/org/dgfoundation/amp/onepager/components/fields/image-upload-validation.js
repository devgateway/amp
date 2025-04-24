function setupImageUploadValidation(options) {
    console.log("Options",options);
    var input = document.getElementById(options.inputId);
    var previewImg = document.getElementById(options.previewId);
    var noImage = document.getElementById(options.noImageId);
    var errorDiv = document.getElementById(options.errorId);
    var label = document.querySelector('label[for="' + options.inputId + '"]') ||
        document.getElementById('uploadLabel');

    if (!input) return;

    // Initialize labels
    if (label) {
        label.textContent = options.chooseImageText;
    }
    if (noImage) {
        noImage.textContent = options.noImageText;
    }

    input.addEventListener('change', function (event) {
        errorDiv.textContent = '';
        var file = event.target.files[0];

        if (!file) {
            if (previewImg) previewImg.style.display = 'none';
            if (noImage) noImage.style.display = 'inline';
            if (label) label.textContent = options.chooseImageText;
            return;
        }

        var validTypes = options.validTypes || ['image/png', 'image/jpeg'];
        if (!validTypes.includes(file.type)) {
            errorDiv.textContent = options.invalidTypeMessage;
            input.value = '';
            if (previewImg) {
                previewImg.src = '';
                previewImg.style.display = 'none';
            }
            if (noImage) noImage.style.display = 'inline';
            if (label) label.textContent = options.chooseImageText;
            return;
        }

        if (file.size > (options.maxSize || 50 * 1024)) {
            errorDiv.textContent = options.maxSizeMessage;
            input.value = '';
            if (previewImg) {
                previewImg.src = '';
                previewImg.style.display = 'none';
            }
            if (noImage) noImage.style.display = 'inline';
            if (label) label.textContent = options.chooseImageText;
            return;
        }

        // Create modal elements
        const modal = document.createElement('div');
        modal.style.display = 'none';
        modal.style.position = 'fixed';
        modal.style.zIndex = '1000';
        modal.style.left = '0';
        modal.style.top = '0';
        modal.style.width = '100%';
        modal.style.height = '100%';
        modal.style.backgroundColor = 'rgba(0,0,0,0.8)';
        modal.style.justifyContent = 'center';
        modal.style.alignItems = 'center';
        modal.style.cursor = 'zoom-out';

        const modalImg = document.createElement('img');
        modalImg.style.maxWidth = '90%';
        modalImg.style.maxHeight = '90%';
        modalImg.style.objectFit = 'contain';

        modal.appendChild(modalImg);
        document.body.appendChild(modal);

        // Click handler for preview image
        if (previewImg) {
            previewImg.style.cursor = 'zoom-in';
            previewImg.addEventListener('click', function() {
                if (previewImg.style.display !== 'none') {
                    modalImg.src = previewImg.src;
                    modal.style.display = 'flex';
                }
            });
        }

        // Close modal when clicked
        modal.addEventListener('click', function() {
            modal.style.display = 'none';
        });


        var reader = new FileReader();
        reader.onload = function (e) {
            var img = new Image();
            img.onload = function () {
                if (img.width > (options.maxWidth || 100) ||
                    img.height > (options.maxHeight || 100)) {
                    errorDiv.textContent = options.dimensionsMessage;
                    input.value = '';
                    if (previewImg) {
                        previewImg.src = '';
                        previewImg.style.display = 'none';
                    }
                    if (noImage) noImage.style.display = 'inline';
                    if (label) label.textContent = options.chooseImageText;
                } else {
                    if (previewImg) {
                        previewImg.src = e.target.result;
                        previewImg.style.display = 'inline';
                        previewImg.dataset.fullSizeSrc = e.target.result;

                    }
                    if (noImage) noImage.style.display = 'none';
                    if (label) label.textContent = file.name;
                }
            };
            img.src = e.target.result;
        };
        reader.readAsDataURL(file);
    });
}
