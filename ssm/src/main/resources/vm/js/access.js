
var permission = getCookie("permission").split(",");
var recharge = false;
var rechargeList = false;
var readList = false;
var Cb = false;
var CbByCommuity = false
var CbByTiming = false
var CbByCycle = false
var reminderList = false;
var mbusManage = false;
var userManage = false;
var locationManage = false;
var addressManage = false;
var priceManage = false;
var dataAnalyze = false;
var rechargeAnalyze = false;
var administratorManage = false;
var authorityManagement = false;
var accountInformation = false;
var languageManage = false;
var timingCbAndintervalchb = false;
var selectedCb = false;
var ForcedOpeningVavle = false;
var ForcedClosingVavle = false;
var AutomaticControlVavle = false;
var readCard = false;
var openCard = false;
jQuery.each(permission, function () {
    if (this == "recharge") {
        recharge = true;
    } else if (this == "rechargeList") {
        rechargeList = true;
    } else if (this == "Cb") {
        Cb = true;
    } else if (this == "CbByCommuity") {
        CbByCommuity = true;
    } else if (this == "CbByTiming") {
        CbByTiming = true;
    } else if (this == "CbByCycle") {
        CbByCycle = true;
    } else if (this == "readList") {
        readList = true;
    } else if (this == "reminderList") {
        reminderList = true;
    } else if (this == "mbusManage") {
        mbusManage = true;
    } else if (this == "userManage") {
        userManage = true;
    } else if (this == "locationManage") {
        locationManage = true;
    } else if (this == "addressManage") {
        addressManage = true;
    } else if (this == "priceManage") {
        priceManage = true;
    } else if (this == "dataAnalyze") {
        dataAnalyze = true;
    } else if (this == "rechargeAnalyze") {
        rechargeAnalyze = true;
    } else if (this == "administratorManage") {
        administratorManage = true;
    } else if (this == "authorityManagement") {
        authorityManagement = true;
    } else if (this == "accountInformation") {
        accountInformation = true;
    } else if (this == "timingCbAndintervalchb") {
        timingCbAndintervalchb = true;
    } else if (this == "selectedCb") {
        selectedCb = true;
    } else if (this == "ForcedOpeningVavle") {
        ForcedOpeningVavle = true;
    } else if (this == "ForcedClosingVavle") {
        ForcedClosingVavle = true;
    } else if (this == "AutomaticControlVavle") {
        AutomaticControlVavle = true;
    } else if (this == "readCard") {
        readCard = true;
    } else if (this == "openCard") {
        openCard = true;
    }
});
function rechargePermission() {
    if (recharge == false) {
        $("#recharge-div1").replaceWith('<div class="access"><h5>$authority_management_047</h5><p>$authority_management_048</p></div>');
        $("#recharge-div2").remove();
    }
}
function rechargeListPermission() {
    if (rechargeList == false) {
        $("#recharge-list-div1").replaceWith('<div class="access"><h5>$authority_management_047</h5><p>$authority_management_048</p></div>');
    }
}
function CbPermission() {
    if (Cb === false) {
        $("#read-by-search1").replaceWith('<div class="access"><h5>$authority_management_047</h5><p>$authority_management_048</p></div>');
        return false;
    } else {
        return true;
    }
}
function CbByCommuityPermission() {
    if (CbByCommuity === false) {
        $("#read-by-xiaoqu1").replaceWith('<div class="access"><h5>$authority_management_047</h5><p>$authority_management_048</p></div>');
        return false;
    } else {
        return true;
    }
}
function CbByTimingPermission() {
    if (CbByTiming === false) {
        $("#autochb1").replaceWith('<div class="access"><h5>$authority_management_047</h5><p>$authority_management_048</p></div>');
        return false;
    } else {
        return true;
    }
}
function CbByCyclePermission() {
    if (CbByCycle === false) {
        $("#intervalchb1").replaceWith('<div class="access"><h5>$authority_management_047</h5><p>$authority_management_048</p></div>');
        return false;
    } else {
        return true;
    }
}
function readListPermission() {
    if (readList == false) {
        $("#read-list-div1").replaceWith('<div class="access"><h5>$authority_management_047</h5><p>$authority_management_048</p></div>');
    }
}
function reminderListPermission() {
    if (reminderList == false) {
        $("#reminder-list-div1").replaceWith('<div class="access"><h5>$authority_management_047</h5><p>$authority_management_048</p></div>');
    }
}
function mbusManagePermission() {
    if (mbusManage == false) {
        $("#mbus-manage-div1").replaceWith('<div class="access"><h5>$authority_management_047</h5><p>$authority_management_048</p></div>');
    }
}
function userManagePermission() {
    if (userManage == false) {
        $("#user-manage-div1").replaceWith('<div class="access"><h5>$authority_management_047</h5><p>$authority_management_048</p></div>');
        $("#user-manage-div2").remove();
    }
}
function locationManagePermission() {
    if (locationManage == false) {
        $("#location-manage-div1").replaceWith('<div class="access"><h5>$authority_management_047</h5><p>$authority_management_048</p></div>');
    }
}
function addressManagePermission() {
    if (addressManage == false) {
        $("#address-manage-div1").replaceWith('<div class="access"><h5>$authority_management_047</h5><p>$authority_management_048</p></div>');
    }
}
function priceManagePermission() {
    if (priceManage == false) {
        $("#price-manage-div1").replaceWith('<div class="access"><h5>$authority_management_047</h5><p>$authority_management_048</p></div>');
    }
}
function dataAnalyzePermission() {
    if (dataAnalyze == false) {
        $("#data-analyze-div1").replaceWith('<div class="access"><h5>$authority_management_047</h5><p>$authority_management_048</p></div>');
    }
}
function rechargeAnalyzePermission() {
    if (rechargeAnalyze == false) {
        $("#recharge-analyze-div1").replaceWith('<div class="access"><h5>$authority_management_047</h5><p>$authority_management_048</p></div>');
    }
}
function administratorManagePermission() {
    if (administratorManage == false) {
        $("#administrator-manage-div1").replaceWith('<div class="access"><h5>$authority_management_047</h5><p>$authority_management_048</p></div>');
        $("#administrator-manage-div2").remove();
    }
}
function authorityManagementPermission() {
    if (authorityManagement == false) {
        $("#authority-management-div1").replaceWith('<div class="access"><h5>$authority_management_047</h5><p>$authority_management_048</p></div>');
    }
}
function accountInformationPermission() {
    if (accountInformation == false) {
        $("#account-information-div1").replaceWith('<div class="access"><h5>$authority_management_047</h5><p>$authority_management_048</p></div>');
    }
}
function languageManagePermission() {
    if (languageManage == false) {
        $("#language-manage-div1").replaceWith('<div class="access"><h5>$authority_management_047</h5><p>$authority_management_048</p></div>');
    }
}

function cardManagePermission() {
    if (readCard == false) {
        $("#cardManage-div1").replaceWith('<div class="access"><h5>$authority_management_047</h5><p>$authority_management_048</p></div>');
    }
    return readCard;
}
function openCardPermission() {
    if (openCard == false) {
        $("#openCard-div1").replaceWith('<div class="access"><h5>$authority_management_047</h5><p>$authority_management_048</p></div>');
    }
    return openCard;
}

function selectedCbPermission() {
    return selectedCb;
}
function ForcedOpeningVavlePermission() {
    return ForcedOpeningVavle;
}
function ForcedClosingVavlePermission() {
    return ForcedClosingVavle;
}
function AutomaticControlVavlePermission() {
    return AutomaticControlVavle;
}
