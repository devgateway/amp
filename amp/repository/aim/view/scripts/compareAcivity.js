function toggleFilterSettings(){
    var currentFilterSettings = $('#currentFilterSettings');
    var displayFilterButton = $('#displayFilterButton');
    if(currentFilterSettings.css('display') == "inline-flex"){
        currentFilterSettings.hide();
        $('#exportScorecard').hide();
        displayFilterButton.html('<digi:trn jsFriendly="true" key="aim:Showfilteroptions">Show Filter options</digi:trn>'+ ' &gt;&gt;');
    }
    else
    {
        currentFilterSettings.css('display', 'inline-flex');
        $('#exportScorecard').css('display','inline-flex');
        displayFilterButton.html('<digi:trn jsFriendly="true" key="aim:Hidefilteroptions">Hide Filter options</digi:trn>'+ ' &lt;&lt;');
    }
}

function viewDifferences(activityOneId) {
    document.aimCompareActivityVersionsForm.method.value = "viewDifferences";
    document.aimCompareActivityVersionsForm.activityOneId.value = activityOneId;
    document.aimCompareActivityVersionsForm.submit();
}

function compareAll(){
    document.getElementById("compPrevForm").target = "_blank";
    var user = document.getElementById("userId").value;
    var team = document.getElementById("teamId").value;
    document.aimCompareActivityVersionsForm.selectedUser.value = user;
    document.aimCompareActivityVersionsForm.selectedTeam.value = team;
    document.aimCompareActivityVersionsForm.selectedDateFrom.value = document.getElementById("selectedDateFromText").value;
    document.aimCompareActivityVersionsForm.selectedDateTo.value = document.getElementById("selectedDateToText").value;
    document.aimCompareActivityVersionsForm.method.value = "compareAll";
    document.aimCompareActivityVersionsForm.submit();
}