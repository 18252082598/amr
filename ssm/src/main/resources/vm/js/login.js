$(function () {
    $("#login_button").click(login);
    document.onkeydown = function (event) {
        var e = event || window.event || arguments.callee.caller.arguments[0];
        if (e && e.keyCode == 13) { // Enter ��
            $("#login_button").click();
        }
    };
});

function show1() {
    var account_name = $("#formGroupInputLarge").val().trim();
    if (account_name == "") {
        $("#account_msg").css("color", "red");
        $("#account_msg").text("$login_005");
    }
}

function hide1() {
    $("#account_msg").text("");
}

function show2() {
    var password = $("#formGroupInputSmall").val().trim();
    if (password == "") {
        $("#pwd_msg").css("color", "red");
        $("#pwd_msg").text("$login_006");
    }
}

function hide2() {
    $("#pwd_msg").text("");
}

function login() {
    $("#login_button").text("$login_007");
    $("#account_msg").text("");
    $("#pwd_msg").text("");
    var account_name = $("#formGroupInputLarge").val().trim();
    var password = $("#formGroupInputSmall").val().trim();
    var check = true;
    if (account_name == "$login_008" || account_name == "") {
        $("#account_msg").css("color", "red");
        $("#account_msg").text("$login_005");
        check = false;
    }
    if (password == "$login_009" || password == "") {
        $("#pwd_msg").css("color", "red");
        $("#pwd_msg").text("$login_006");
        check = false;
    }
    if (check == false) {
        $("#login_button").text("$login_004");
    }
    if (check) {
        jQuery.ajax({
            url: "../operator/login.do",
            type: "post",
            data: {
                "admin_account": account_name,
                "admin_password": password
            },
            dataType: "json",
            success: function (result) {
                if (result.status == 1) {
                    var data = JSON.parse(result.data);
                    var account_status = data.accountStatus;
                    var permission = data.permission;
                    if (account_status === "0") {
                        alert("$login_013");
                    } else {
                        addCookie("account_name", account_name, 24);
                        addCookie("permission", permission, 24);
                        window.location.href = "$navigation_href_001";
                    }
                } else if (result.status == 2) {
                    $("button").text("$login_004");
                    $("#pwd_msg").text("$login_012");
                } else if (result.status == 3) {
                    $("button").text("$login_004");
                    alert("$public_036");
                }
            },
            error: function () {
                $("#login_button").text("$login_004");
                alert("error");
            }
        });
    }
}
