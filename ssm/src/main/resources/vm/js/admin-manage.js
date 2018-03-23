$(function () {
    var account_name = getCookie("account_name");
    if (account_name == null) {
        window.location.href = "login.html";
    }
    $("#apply-change-meter").click(function () {
        $("#change-meter-popup1").fadeIn();
    });
    $("#admin").text(account_name);
    $("#sum-to-do").text("(" + toDoNo + ")");
    $("#exit").click(logout);
    load_Sumtodo();
    administratorManagePermission();
    findAdmin();
    loadRoleOption();
    findSysLog();
    $(".span1").click(nextPage);
    $(".span2").click(prePage);
    $(".span3").click(firstPage);
    $(".span4").click(lastPage);
    $(".input-page").keypress(function (event) {
        var keycode = (event.keyCode ? event.keyCode : event.which);
        if (keycode == 13) {
            var pageIndex = $(this).val().trim();
            pageIndex = parseInt(pageIndex);
            var totalNo = parseInt($("#t1").data("admins.length"));
            var pageSize = parseInt($(".pageSize option:selected").val().trim());
            var pageNo = parseInt(totalNo / pageSize);
            var pageMax = totalNo % pageSize == 0 ? pageNo : pageNo + 1;
            if (pageIndex <= 0) {
                alert("$public_038");
            } else if (pageIndex > pageMax) {
                alert("$public_039");
            } else {
                $(".input-page")[0].value = pageIndex;
                $(".input-page")[1].value = pageIndex;
                findAdmin();
            }
            return false;
        }
    });
    $(".span5").click(nextLogPage);
    $(".span6").click(preLogPage);
    $(".span7").click(firstLogPage);
    $(".span8").click(lastLogPage);
    $("#pageLogIndex").keypress(function (event) {
        var keycode = (event.keyCode ? event.keyCode : event.which);
        if (keycode == 13) {
            var pageLogIndex = parseInt($(this).val().trim());
            var nextPageLogIndex = pageLogIndex;
            var totalLogNo = parseInt($("#t2").data("adminManageInfo.length"));
            var pageLogSize = parseInt($(".pageLogSize option:selected").val().trim());
            var pageLogNo = parseInt(totalLogNo / pageLogSize);
            var pageLogMax = totalLogNo % pageLogSize == 0 ? pageLogNo : pageLogNo + 1;
            if (pageLogIndex <= 0) {
                alert("$public_038");
            } else if
                    (nextPageLogIndex > pageLogMax) {
                alert("$public_039");
            } else {
                $("#pageLogIndex").value = nextPageLogIndex;
                loadAdminManageInfo();
            }
            return false;
        }
    });
    $("#t1 tbody").on("click", "tr", getAdminAccount);
    $("#t1 tbody").on("click", "tr", loadAdminManageInfoFirst);
    findSuppliersModify();
    $("#t1").on("click", ".glyphicon.glyphicon-pencil.list-button", modify);
    $("#t1").on("click", ".glyphicon.glyphicon-trash.list-button", del);
    $("#adminReset").click(admin_clear);
    $("#modifyAdminReset").click(modify_admin_clear);
    $("#regist_show").click(admin_clear);
});

var loadPage = function () {
    findAdmin();
};

function findAdmin() {
    $("#t1 tbody tr").remove();
    var pageNo = $(".input-page").val().trim();
    var pageSize = parseInt($(".pageSize option:selected").val().trim());
    jQuery.ajax({
        url: "../admin/findAdmin.do",
        type: "post",
        dataType: "json",
        success: function (result) {
            window.console.log(result);
            //alert(result);
            if (result.status == 1) {
                var admins = result.data;
                $("#totalNo").text(admins.length);
                var page = parseInt(admins.length / pageSize);
                var pageMax = admins.length % pageSize == 0 ? page : page + 1;
                $(".span4").next().text(pageMax);
                $("#t1").data("admins.length", admins.length);
                $("#t1").data("totalNo", totalNo);
                for (var i = (pageNo - 1) * pageSize; i < pageNo * pageSize && i < admins.length; i++) {
                    var admin = admins[i];
                    var admin_id = admin.admin_id;
                    var admin_account = admin.admin_account;
                    var admin_name = admin.admin_name;
                    var admin_tel = admin.admin_tel;
                    var admin_supplier_name = admin.admin_supplier_name;
                    if (admin_supplier_name === '') {
                        admin_supplier_name = '$read_list_004';
                    }
                    var admin_password = admin.admin_password;
                    var admin_power = admin.admin_power;
                    var last_load_time = admin.last_load_time;
                    var admin_add_time = admin.admin_add_time;
                    var account_status = admin.account_status;
                    var account_status_str = account_status == 1 ? "$administrator_manage_035" : "$administrator_manage_036";
                    var str = "";
                    str += '<tr><td>' + (i + 1) + '</td>';
                    str += '<td>' + admin_account + '</td>';
                    str += '<td>' + admin_name + '</td>';
                    str += '<td>' + admin_tel + '</td>';
                    str += '<td>' + admin_power + '</td>';
                    str += '<td>' + last_load_time + '</td>';
                    str += '<td>' + admin_add_time + '</td>';
                    str += '<td>' + account_status_str + '</td>';
                    str += '<td><a href="#"><span class="glyphicon glyphicon-pencil list-button"></span> </a><a href="#"><span class="glyphicon glyphicon-trash list-button"></span> </a></td></tr>';
                    $("#t1 tbody").append($(str));
                    $("#t1").data("admins" + (i + 1), admins);
                    $("#t1").data("admin_id" + (i + 1), admin_id);
                    $("#t1").data("admin_account" + (i + 1), admin_account);
                    $("#t1").data("admin_name" + (i + 1), admin_name);
                    $("#t1").data("admin_tel" + (i + 1), admin_tel);
                    $("#t1").data("admin_password" + (i + 1), admin_password);
                    $("#t1").data("admin_supplier_name" + (i + 1), admin_supplier_name);
                    $("#t1").data("admin_power" + (i + 1), admin_power);
                    $("#t1").data("account_status" + (i + 1), account_status);
                }
            }
        },
        error: function () {
            alert("error");
        }
    });

}
function loadRoleOption() {
    $("#admin-role2").empty();
    $("#admin-role3").empty();
    jQuery.ajax({
        url: "../admin/findRole.do",
        type: "post",
        async: false,
        success: function (result) {
            if (result.status == 1) {
                var roles = result.data;
                for (var i = 0; i < roles.length; i++) {
                    var str = "<option value='" + roles[i].role_name + "'>" + roles[i].role_name + "</option>";
                    $("#admin-role2").append($(str));
                    $("#admin-role3").append($(str));
                }
            } else {
                alert(result.msg);
            }
        },
        error: function () {
            alert("error");
        }
    });
}
function admin_clear() {
    $("#s1").text("");
    $("#s2").text("");
    $("#s3").text("");
    $("#s4").text("");
    $("#admin-account2")[0].value = "";
    $("#admin-name2")[0].value = "";
    $("#admin-tel2")[0].value = "";
    $("#admin-location-name2")[0].value = "$public_036";
    $("#admin-password2")[0].value = "";
    $("#confirm-admin-password2")[0].value = "";
    $("#admin-role2")[0].value = "$public_036";
    findSuppliers();
}

function modify_admin_clear() {
    $("#s1").text("");
    $("#s2").text("");
    $("#s3").text("");
    $("#s4").text("");
    $(".modifyAdmin")[1].value = "";
    $(".modifyAdmin")[2].value = "";
    $(".modifyAdmin")[3].value = "$public_036";
    $(".modifyAdmin")[4].value = "";
    $(".modifyAdmin")[5].value = "";
    // $(".modifyAdmin")[6].value = "$public_036";
    $('input:radio').removeAttr('checked');
    $(":checkbox[name='inlineRadioOptions3']").removeAttr('checked');
}

function findSuppliers() {
    $("#admin-location-name2").empty();
    $("#admin-location-name2").append($("<option value=''>$administrator_manage_056</option>"));
    jQuery.ajax({
        url: "../user/loadSuppliers.do",
        type: "post",
        async: false,
        data: {
            "province": "",
            "city": "",
            "district": "",
            "startNo": 0,
            "pageSize": 0,
        },
        dataType: "json",
        success: function (result) {
            if (result.status === 1) {
                var map = result.data;
                var suppliers = map["suppliers"];
                var str = "<option name='supplier' value='$read_list_004'>" + "$read_list_004" + "</option>";
                $("#admin-location-name2").append($(str));
                for (var i = 0; i < suppliers.length; i++) {
                    var supplier_name = suppliers[i].supplier_name;
                    str = "<option name='supplier' value='" + supplier_name + "'>" + supplier_name + "</option>";
                    $("#admin-location-name2").append($(str));
                }
            } else {
                alert(result.msg);
            }
        },
        error: function () {
            alert("error");
        }
    });
}
function findSuppliersModify() {
    $("#admin-location-name3").empty();
    jQuery.ajax({
        url: "../user/loadSuppliers.do",
        type: "post",
        async: false,
        data: {
            "province": "",
            "city": "",
            "district": "",
            "startNo": 0,
            "pageSize": 0,
        },
        dataType: "json",
        success: function (result) {
            if (result.status === 1) {
                var map = result.data;
                var suppliers = map["suppliers"];
                var str = "<option name='modify_supplier' value='$read_list_004'>" + "$read_list_004" + "</option>";
                $("#admin-location-name3").append($(str));
                for (var i = 0; i < suppliers.length; i++) {
                    var supplier_name = suppliers[i].supplier_name;
                    str = "<option name='modify_supplier' value='" + supplier_name + "'>" + supplier_name + "</option>";
                    $("#admin-location-name3").append($(str));
                }
            } else {
                alert("$public_036");
            }
        },
        error: function () {
            alert("error");
        }
    });
}
function regist_admin() {
    var admin_account = $("#admin-account2").val().trim();
    var admin_name = $("#admin-name2").val().trim();
    var admin_tel = $("#admin-tel2").val().trim();
    var admin_supplier_name = $("#admin-location-name2 option:selected").val().trim();
    if (admin_supplier_name === '$read_list_004') {
        admin_supplier_name = '';
    }
    var admin_password = $("#admin-password2").val().trim();
    var confirm_admin_password = $("#confirm-admin-password2").val().trim();
    var admin_power = $("#admin-role2 option:selected").val().trim();
    var account_status = $('input:radio[name="inlineRadioOptions2"]:checked').val().trim();
    var admin_add_time = getTime();
    var check = true;
    jQuery.ajax({
        url: "../admin/isExist.do",
        type: "post",
        dataType: "json",
        data: {
            "admin_account": admin_account
        },
        success: function (result) {
            if (result.status == 0) {
                $("#s1").css("color", "red");
                $("#s1").text("$administrator_manage_057");
                alert("$administrator_manage_057");
            } else if (result.status == 1) {

                if (check) {
                    jQuery.ajax({
                        url: "../admin/adminRegist.do",
                        type: "post",
                        data: {
                            "admin_account": admin_account,
                            "admin_name": admin_name,
                            "admin_tel": admin_tel,
                            "admin_supplier_name": admin_supplier_name,
                            "admin_password": admin_password,
                            "admin_power": admin_power,
                            "account_status": account_status,
                            "admin_add_time": admin_add_time,
                        },
                        traditional: true,
                        dataType: "json",
                        success: function (result) {
                            if (result.status == 1) {
                                alert("$public_037");
                                $('#administrator-add').modal('hide');
                                findAdmin();
                            }
                        },
                        error: function () {
                            alert("error");
                        },
                    });
                }
            }
        },
        error: function () {
            alert("error");
        },
    });
}

function modify() {
    var admin_index = $(this).parents("tr").find("td:eq(0)").text().trim();
    var admin_id = $("#t1").data("admin_id", admin_index);
    $("#t1").data("admin_index", admin_index);
    var admin_account = $(this).parents("tr").find("td:eq(1)").text().trim();
    var admin_name = $(this).parents("tr").find("td:eq(2)").text().trim();
    var admin_tel = $(this).parents("tr").find("td:eq(3)").text().trim();
    var account_status = $("#t1").data("account_status" + admin_index);
    var admin_supplier_name = $("#t1").data("admin_supplier_name" + admin_index);
    var admin_password = $("#t1").data("admin_password" + admin_index);
    var role = $(this).parents("tr").find("td:eq(4)").text().trim();
    document.getElementById("admin-account3").value = admin_account;
    document.getElementById("admin-name3").value = admin_name;
    document.getElementById("admin-tel3").value = admin_tel;
    findSuppliersModify();
    loadRoleOption();
    $("#admin-location-name3").find("option[value='" + admin_supplier_name + "']").attr("selected", true);
    $("#admin-role3").find("option[value='" + role + "']").attr("selected", true);
    $(":radio[value='" + account_status + "']").prop("checked", true);
}

function cofirm_modify_admin() {
    var admin_account = $("#admin-account3").val().trim();
    var admin_name = $("#admin-name3").val().trim();
    var admin_tel = $("#admin-tel3").val().trim();
    var admin_supplier_name = $("#admin-location-name3 option:selected").val().trim();
    if (admin_supplier_name === '$read_list_004') {
        admin_supplier_name = '';
    }
    var admin_password = $("#admin-password3").val().trim();
    var confirm_admin_password = $("#confirm-admin-password3").val().trim();
    var admin_power = $("#admin-role3 option:selected").val().trim();
    var account_status = $('input:radio[name="inlineRadioOptions3"]:checked').val().trim();
    var admin_index = $("#t1").data("admin_index");
    var admin_id = $("#t1").data("admin_id" + admin_index);
    var original_admin_name = $("#t1").data("admin_name" + admin_index);
    var original_admin_tel = $("#t1").data("admin_tel" + admin_index);
    var original_admin_supplier_name = $("#t1").data("admin_supplier_name" + admin_index);
    var original_admin_password = $("#t1").data("admin_password" + admin_index);
    var original_admin_power = $("#t1").data("admin_power" + admin_index);
    var original_account_status = $("#t1").data("account_status" + admin_index);
    var check = true;
    if (check) {
        jQuery.ajax({
            url: "../admin/modifyAdmin.do",
            type: "post",
            data: {
                "admin_id": admin_id,
                "admin_account": admin_account,
                "admin_name": admin_name,
                "admin_tel": admin_tel,
                "admin_supplier_name": admin_supplier_name,
                "admin_password": admin_password,
                "admin_power": admin_power,
                "account_status": account_status,
                "original_admin_name": original_admin_name,
                "original_admin_tel": original_admin_tel,
                "original_admin_supplier_name": original_admin_supplier_name,
                "original_admin_password": original_admin_password,
                "original_admin_power": original_admin_power,
                "original_account_status": original_account_status
            },
            traditional: true,
            dataType: "json",
            success: function (result) {
                if (result.status == 1) {
                    alert("$public_041");
                    $('#edit-userinfo').modal('hide');
                    findAdmin();
                    admin_clear();
                }
            },
            error: function () {
                alert("error");
            }
        });
    }
}

function del() {
    var admin_index = $(this).parents("tr").find("td:eq(0)").text().trim();
    var admin_id = $("#t1").data("admin_id" + admin_index);
    var admin_account = $(this).parents("tr").find("td:eq(1)").text().trim();
    if (confirm("$public_042")) {
        jQuery.ajax({
            url: "../admin/deleteAdmin.do",
            type: "post",
            data: {
                "admin_account": admin_account
            },
            dataType: "json",
            success: function (result) {
                if (result.status == 1) {
                    findAdmin();
                }
            },
            error: function () {
                alert("error");
            }
        });
    }
}

function getTime() {
    var d = new Date();
    var m = d.getMonth() + 1;
    var date = d.getDate();
    var h = d.getHours();
    var min = d.getMinutes();
    var sec = d.getSeconds();
    if ((m >= 1) && (m <= 9)) {
        m = "0" + m;
    }
    if ((date >= 1) && (date <= 9)) {
        date = "0" + date;
    }
    if ((h >= 1) && (h <= 9)) {
        h = "0" + h;
    }
    if ((min >= 1) && (min <= 9)) {
        min = "0" + min;
    }
    if ((sec >= 1) && (sec <= 9)) {
        sec = "0" + sec;
    }
    var str = d.getFullYear() + "." + m + "." + date + " " + h + ":" + min + ":" + sec;
    return  str;
}

