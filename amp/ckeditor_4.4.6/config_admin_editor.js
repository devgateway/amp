CKEDITOR.editorConfig = function (config) {
    config.toolbar = 'MyToolbar';
    config.disableNativeSpellChecker = false;
    config.browserContextMenuOnCtrl = true;
    config.scayt_autoStartup = false;
    config.toolbar_MyToolbar =
        [
            { name:'document', items:[ 'Source', 'DocProps', 'Preview', 'Print' ] },
            { name:'clipboard', items:[ 'Cut', 'Copy', 'Paste', 'PasteText', 'PasteFromWord', '-', 'Undo', 'Redo' ] },
            { name:'editing', items:[ 'Find', 'Replace', '-', 'SelectAll'/*, '-', 'SpellChecker', 'Scayt'*/ ] },
            { name:'basicstyles', items:[ 'Bold', 'Italic', 'Underline', 'Strike', 'Subscript', 'Superscript', '-', 'RemoveFormat' ] },
            { name:'links', items:[ 'Link', 'Unlink'/*,'Anchor'*/ ] },
            { name:'insert', items:[ 'Image', 'Table', 'HorizontalRule'/*,'Smiley'*/, 'SpecialChar', 'PageBreak' ] },
            { name:'styles', items:[ /*'Styles',*/'Format', 'Font', 'FontSize' ] },
            { name:'colors', items:[ 'TextColor', 'BGColor' ] },
            { name:'paragraph', items:[ 'NumberedList', 'BulletedList', '-', 'Outdent', 'Indent', '-', 'Blockquote',
                '-', 'JustifyLeft', 'JustifyCenter', 'JustifyRight', 'JustifyBlock', '-'/*,'BidiLtr','BidiRtl'*/ ] }
        ];
    //config.enterMode = CKEDITOR.ENTER_DIV;
    config.allowedContent = true; //This is key to allow styling.
};
