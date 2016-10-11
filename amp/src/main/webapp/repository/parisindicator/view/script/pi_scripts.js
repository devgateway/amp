function toggleSettings(){
    var currentDisplaySettings = document.getElementById('currentDisplaySettings');
    var displaySettingsButton = document.getElementById('displaySettingsButton');
    if(currentDisplaySettings.style.display == "block"){
        currentDisplaySettings.style.display = "none";
        displaySettingsButton.innerHTML = '<digi:trn jsFriendly="true" key="rep:showCurrSettings">Show current settings</digi:trn>';
    }
    else
    {
        currentDisplaySettings.style.display = "block";
        displaySettingsButton.innerHTML = '<digi:trn jsFriendly="true" key="rep:hideCurrSettings">Hide current settings</digi:trn>';
    }
}