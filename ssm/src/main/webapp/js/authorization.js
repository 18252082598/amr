$(function () {
    $("#login").click(login);
    document.onkeydown = function (event) {
        var e = event || window.event || arguments.callee.caller.arguments[0];
        if (e && e.keyCode === 13) { // Enter ��
            $("#login").click();
        }
    };
    $("#reset").click(reset);
});

function showAccountErrorMess() {
    var account_name = $("#formGroupInputLarge").val().trim();
    if (account_name === "") {
        $("#account_msg").css("color", "red");
        $("#account_msg").text("账号不能为空，请输入账号");
    }
}

function hideAccountErrorMess() {
    $("#account_msg").text("");
}

function showPasswordErrorMess() {
    var password = $("#formGroupInputSmall").val().trim();
    if (password === "") {
        $("#pwd_msg").css("color", "red");
        $("#pwd_msg").text("密码不能为空，请输入密码");
    }
}

function hidePasswordErrorMess() {
    $("#pwd_msg").text("");
}

function reset() {
    $("#formGroupInputLarge").val("");
    $("#formGroupInputSmall").val("");
    $("#pwd_msg").text("");
    $("#account_msg").text("");

}
function login() {
    $("#login").text("授权中...");
    $("#login").addClass("disabled");
    $("#reset").addClass("disabled");
    $("#account_msg").text("");
    $("#pwd_msg").text("");
    var account_name = $("#formGroupInputLarge").val().trim();
    var password = $("#formGroupInputSmall").val().trim();
    var check = true;
    if (account_name === "请输入您的账号" || account_name === "") {
        $("#account_msg").css("color", "red");
        $("#account_msg").text("账号不能为空，请输入账号");
        check = false;
    }
    if (password === "请输入您的密码" || password === "") {
        $("#pwd_msg").css("color", "red");
        $("#pwd_msg").text("密码不能为空，请输入密码");
        check = false;
    }
    if (check === false) {
        $("#login").text("确定");
    }
    if (check) {
        $("#login").text("确定");
        jQuery.ajax({
            url: "../admin/authorization.do",
            type: "post",
            data: {
                "admin_account": account_name,
                "admin_password": password
            },
            dataType: "json",
            success: function (result) {
                if (result.status === 1) {
                    alert("授权成功");
                } else if (result.status === 2) {
                    $("#login").removeClass("disabled");
                    $("#reset").removeClass("disabled");
                    $("#pwd_msg").text("账号或密码错误");
                } else if (result.status === 3) {
                    $("#login").removeClass("disabled");
                    $("#reset").removeClass("disabled");
                    alert("系统异常，请稍后再试");
                }
            },
            error: function () {
                alert("error");
            }
        });
    }
}
