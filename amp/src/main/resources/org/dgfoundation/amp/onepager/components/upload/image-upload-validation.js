function setupImageUploadValidation(options) {
    var input = document.getElementById(options.inputId);
    var previewImg = document.getElementById(options.previewId);
    var noImage = document.getElementById(options.noImageId);
    var errorDiv = document.getElementById(options.errorId);
    var label = document.querySelector('label[for="' + options.inputId + '"]') ||
        document.getElementById('uploadLabel');

    if (!input) return;

    input.required = options.isRequired;
    if (label) {
        label.textContent = options.chooseImageText;
    }
    if (previewImg && !previewImg.src) {
        previewImg.style.display = 'none';
    }
    if (noImage && (!previewImg || !previewImg.src)) {
        noImage.textContent = options.noImageText;
        noImage.style.display = 'inline';
    }

    let currentFileData = null;

    const elementID = '#' + options.inputId;
    $(elementID).fileupload({
        url: options.uploadUrl,
        paramName: 'FILE-UPLOAD',
        singleFileUploads: true,
        iframe: true,
        dataType: 'json',
        minFileSize: 1,
        maxFileSize: options.maxSize || 5000,
        autoUpload: false,
        add: function (e, data) {
            currentFileData = data;
        },
        done: function (e, data) {
            console.log("Uploaded image");
            errorDiv.textContent = 'Uploaded image';
            var button = document.getElementById("submitButton");
            if (button) {
                console.log("Found submit button, triggering click");
                button.click();
            } else {
                console.error("Submit button not found with ID: submitButton");
            }
            resetInput(); // Reset the input after a successful upload
        },
        fail: function (e, data) {
            console.log("Failed to upload file: " + data.errorThrown);
            errorDiv.textContent = 'Failed to upload image';
            resetInput(); // Reset the input after a failed upload
        }
    });

    // Initial setup of the change listener
    setupChangeListener(input);

    function setupChangeListener(inputElement) {
        inputElement.addEventListener('change', function (event) {
            errorDiv.textContent = '';
            var file = event.target.files[0];
            if (!file) {
                resetDisplay();
                return;
            }

            var validTypes = options.validTypes || ['image/png', 'image/jpeg'];
            if (!validTypes.includes(file.type)) {
                errorDiv.textContent = options.invalidTypeMessage;
                resetInput(); // Reset on invalid type
                resetDisplay();
                return;
            }

            if (file.size > (options.maxSize || 50 * 1024)) {
                errorDiv.textContent = options.maxSizeMessage;
                resetInput(); // Reset on exceeding max size
                resetDisplay();
                return;
            }

            // Create modal elements if they don't exist
            let modal = document.getElementById('imagePreviewModal');
            if (!modal) {
                modal = document.createElement('div');
                modal.id = 'imagePreviewModal';
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

                const modalImg = document.getElementById('fullSizePreviewImage');
                modalImg.style.maxWidth = '90%';
                modalImg.style.maxHeight = '90%';
                modalImg.style.objectFit = 'contain';

                modal.appendChild(modalImg);
                document.body.appendChild(modal);

                modal.addEventListener('click', function(event) {
                    console.log("Clicked")
                    if (event.target === modal) {
                        modal.style.display = 'none';
                    }
                });
            }

            // Click handler for preview image
            if (previewImg) {
                previewImg.style.cursor = 'zoom-in';
                previewImg.onclick = function() {
                    if (previewImg.style.display !== 'none') {
                        document.getElementById('fullSizePreviewImage').src = previewImg.src;
                        modal.style.display = 'flex';
                    }
                };
            }


            var reader = new FileReader();
            reader.onload = function (e) {
                var img = new Image();
                console.log("loading image");
                img.onload = function () {
                    if (img.width > (options.maxWidth || 100) ||
                        img.height > (options.maxHeight || 100)) {
                        errorDiv.textContent = options.dimensionsMessage;
                        resetInput(); // Reset on invalid dimensions
                        resetDisplay();
                    } else {
                        console.log("Image size is okay");
                        if (previewImg) {
                            previewImg.src = e.target.result;
                            previewImg.style.display = 'inline';
                            previewImg.dataset.fullSizeSrc = e.target.result;
                        }
                        if (noImage) noImage.style.display = 'none';
                        if (label) label.textContent = file.name;
                        if (currentFileData) {
                            currentFileData.submit();
                            currentFileData = null;
                        }
                    }
                };
                img.src = e.target.result;
            };
            reader.readAsDataURL(file);
        });
    }

    function resetInput() {
        const oldInput = document.getElementById(options.inputId);
        if (oldInput) {
            const newInput = oldInput.cloneNode(true); // Create a new input element
            oldInput.parentNode.replaceChild(newInput, oldInput);
            setupChangeListener(newInput); // Re-attach the event listener to the new input
            input = newInput; // Update the 'input' variable
        }
    }

    function resetDisplay() {
        if (previewImg) {
            previewImg.src = '';
            previewImg.style.display = 'none';
        }
        if (noImage) noImage.style.display = 'inline';
        if (label) label.textContent = options.chooseImageText;
    }
}

function displayExistingImage(dataUrl, contentType, previewId, noImageId, inputId, fileName) {
    const previewImg = document.getElementById(previewId);
    const noImage = document.getElementById(noImageId);
    const label = document.querySelector('label[for="' + inputId + '"]') || document.getElementById('uploadLabel');

    if (previewImg) {
        previewImg.src = dataUrl;
        previewImg.dataset.fullSizeSrc = dataUrl;
        previewImg.style.display = 'inline';
        previewImg.style.cursor = 'zoom-in';
        previewImg.onclick = function() {
            const modal = document.getElementById('imagePreviewModal');
            if (modal) {
                const modalImg = document.getElementById('fullSizePreviewImage');
                if (modalImg) {
                    modalImg.src = this.dataset.fullSizeSrc;
                    modal.style.display = 'flex';
                }
            }
        };
        if (noImage) {
            noImage.style.display = 'none';
        }
    }


    if (label && fileName) {
        label.textContent = fileName;
    }
}
