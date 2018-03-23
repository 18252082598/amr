$(function () {
    $("#admin").text(account_name);
    $("#sum-to-do").text("(" + toDoNo + ")");
    $("#exit").click(logout);
    load_Sumtodo();
    //权限验证֤
    accountInformationPermission();
    //加载当前管理员信息
    loadThisAdmin();
    $("#original-password").blur(function () {
        checkPassword();
    });
    $("#passwordChange").click(function () {
        $("#tab-account-xg  .required").trigger('blur');
        var numError = $('#tab-account-xg  .onError').length;
        if ((numError == 0) && check1 == true) {
            passwordChange();
        } else {
            alert('$account_information_035');
        }
    });
});
/** 
 * @returns {undefined}
 * 加载当前管理员信息
 */
var check1 = null;
function loadThisAdmin() {
    var admin_account = getCookie("account_name");
    jQuery.ajax({
        url: "../admin/findAdminByAccount.do",
        type: "post",
        data: {
            "admin_account": admin_account
        },
        dataType: "json",
        success: function (result) {
            var admin = result.data;
            var admin_id = admin.admin_id;
            var admin_power = admin.admin_power;
            var admin_name = admin.admin_name;
            var admin_tel = admin.admin_tel;
            var admin_password = admin.admin_password;
            var admin_supplier_name = admin.admin_supplier_name;
            var account_status = admin.account_status;
            var account_status_str = account_status == 1 ? "$administrator_manage_035" : "$administrator_manage_036";
            $("#t1").find("span:eq(1)").append(admin_power);
            $("#t1").find("span:eq(3)").append(admin_account);
            $("#t1").find("span:eq(5)").append(admin_name);
            $("#t1").find("span:eq(7)").append(admin_tel);
            $("#t1").find("span:eq(9)").append(admin_supplier_name);
            $("#t1").find("span:eq(11)").append(account_status_str);
            $("#t1").data("admin_id", admin_id);
            $("#t1").data("original_admin_tel", admin_tel);
            $("#t1").data("original_admin_name", admin_name);
        },
        error: function () {
            alert("error");
        }
    });
}
/**
 * 根据管理员id修改管理员联系方式
 * 
 * @returns {undefined}
 */
function modifyTelById() {
    var admin_tel = $("#modifyTel").val();
    var original_admin_tel = $("#t1").data("original_admin_tel");
    var admin_id = $("#t1").data("admin_id");
    jQuery.ajax({
        url: "../admin/modifyTelById.do",
        type: "post",
        data: {
            "admin_id": admin_id,
            "admin_tel": admin_tel,
            "original_admin_tel": original_admin_tel
        },
        dataType: "json",
        success: function (result) {
            alert("$public_041");
        },
        error: function () {
            alert("error");
        },
    });

}
/**
 * 根据管理员id修改管理员姓名
 * 
 * @returns {undefined}
 */
function modifyNameById() {
    var admin_name = $("#modifyName").val();
    var original_admin_name = $("#t1").data("original_admin_name");
    var admin_id = $("#t1").data("admin_id");
    jQuery.ajax({
        url: "../admin/modifyNameById.do",
        type: "post",
        data: {
            "admin_id": admin_id,
            "admin_name": admin_name,
            "original_admin_name": original_admin_name
        },
        dataType: "json",
        success: function (result) {
            alert("$public_041");
        },
        error: function () {
            alert("error");
        },
    });
}
/**
 * 验证密码是否正确
 * 
 * @returns {undefined}
 */
function checkPassword() {
    var original_password = $("#original-password").val();
    var admin_id = $("#t1").data("admin_id");
    jQuery.ajax({
        url: "../admin/checkPassword.do",
        type: "post",
        data: {
            "admin_id": admin_id,
            "original_password": original_password
        },
        dataType: "json",
        success: function (result) {
            if (result.status === 1) {
                $("#s2").text("");
                $('#s2').addClass('glyphicon  glyphicon-ok').css("color", '#666');
                check1 = true;
            } else if (result.status === 0) {
                $("#s2").removeClass("glyphicon  glyphicon-ok").css("color", "red");
                $("#s2").text("$account_information_032");
                check1 = false;
            }
        },
        error: function () {
            alert("error");
        }
    });
}
/**  
 * 修改密码提交
 * 
 * @returns {undefined}
 */
function passwordChange() {
    var admin_id = $("#t1").data("admin_id");
    var original_password_span = $("#s2").text().trim();
    var admin_password = $("#admin-password").val();
    var confirm_password = $("#confirm-password").val();
    jQuery.ajax({
        url: "../admin/modifyPasswordById.do",
        type: "post",
        data: {
            "admin_id": admin_id,
            "admin_password": admin_password
        },
        dataType: "json",
        success: function (result) {
            alert("$public_041");
            $('#tab-account-xg').modal('hide');
        },
        error: function () {
            alert("error");
        }
    });
}
