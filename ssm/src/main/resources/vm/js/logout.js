
function logout() {
    delCookie("account_name");
    delCookie("permission");
    jQuery.ajax({
        url: "../operator/logout.do",
        type: "post",
        success: function (result) {
            if (result.status == 1) {
                window.location.href = "$navigation_href_000";
            } else {
                alert("$public_036");
            }
            ;
        },
        error: function () {
            $("button").text("$login_004");
            alert("error");
        }
    });
}

var account_name = getCookie("account_name");
var toDoNo = getCookie("toDoNo");
if (account_name == null) {
    window.location.href = "$navigation_href_000";
}