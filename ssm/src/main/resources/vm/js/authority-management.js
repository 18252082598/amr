var loadPage = function () {
    findRole();
};
function findRole() {
    $("#t1 tbody tr").remove();
    var pageNo = $(".input-page").val().trim();
    pageNo = pageNo == "" ? 1 : pageNo;
    var pageSize = parseInt($(".pageSize option:selected").val().trim());
    jQuery.ajax({
        url: "../admin/findRole.do",
        type: "post",
        dataType: "json",
        success: function (result) {
            if (result.status == 1) {
                var roles = result.data;
                $("#totalNo").text(roles.length);
                var page = parseInt(roles.length / pageSize);
                var pageMax = roles.length % pageSize == 0 ? page
                        : page + 1;
                $(".span4").next().text(pageMax);
                $("#t1").data("roles.length", roles.length);
                $("#t1").data("totalNo", totalNo);
                for (var i = (pageNo - 1) * pageSize; i < pageNo
                        * pageSize
                        && i < roles.length; i++) {
                    var role = roles[i];
                    var id = role.id;
                    var role_name = role.role_name;
                    var permission = role.permission;
                    var role_add_time = role.role_add_time;
                    var str = "";
                    str += '<tr><td>' + (i + 1) + '</td>';
                    str += '<td>' + role_name + '</td>';
                    str += '<td>' + getPermission(permission) + '</td>';
                    str += '<td>' + role_add_time + '</td>';
                    str += '<td><a href="#"><span class="glyphicon glyphicon-pencil list-button"></span> </a><a href="#"><span class="glyphicon glyphicon-trash list-button"></span> </a></td></tr>';
                    $("#t1 tbody").append($(str));
                    $("#t1").data("roles" + (i + 1), roles);
                    $("#t1").data("id" + (i + 1), id);
                    $("#t1").data("role_name" + (i + 1), role_name);
                    $("#t1").data("permission" + (i + 1), permission);
                }
            }
        },
        error: function () {
            alert("error");
        }
    });
}

/*
 * 获取当前语言版本的权限名称
 */
function getPermission(permission) {
    var permission_container = [];
    var permission_separate = permission.split(",");
    jQuery.each(permission_separate, function () {
        if (this == "recharge") {
            permission_container.push("$authority_management_022");
        } else if (this == "rechargeList") {
            permission_container.push("$authority_management_023");
        } else if (this == "Cb") {
            permission_container.push("$authority_management_053");
        } else if (this == "CbByCommuity") {
            permission_container.push("$authority_management_054");
        } else if (this == "CbByTiming") {
            permission_container.push("$authority_management_055");
        } else if (this == "CbByCycle") {
            permission_container.push("$authority_management_056");
        } else if (this == "readList") {
            permission_container.push("$authority_management_026");
        } else if (this == "reminderList") {
            permission_container.push("$authority_management_027");
        } else if (this == "mbusManage") {
            permission_container.push("$authority_management_028");
        } else if (this == "userManage") {
            permission_container.push("$authority_management_030");
        } else if (this == "locationManage") {
            permission_container.push("$authority_management_031");
        } else if (this == "addressManage") {
            permission_container.push("$authority_management_032");
        } else if (this == "priceManage") {
            permission_container.push("$authority_management_034");
        } else if (this == "dataAnalyze") {
            permission_container.push("$authority_management_036");
        } else if (this == "rechargeAnalyze") {
            permission_container.push("$authority_management_037");
        } else if (this == "administratorManage") {
            permission_container.push("$authority_management_039");
        } else if (this == "authorityManagement") {
            permission_container.push("$authority_management_040");
        } else if (this == "accountInformation") {
            permission_container.push("$authority_management_041");
        } else if (this == "selectedCb") {
            permission_container.push("$authority_management_057");
        } else if (this == "ForcedOpeningVavle") {
            permission_container.push("$authority_management_058");
        } else if (this == "ForcedClosingVavle") {
            permission_container.push("$authority_management_059");
        } else if (this == "AutomaticControlVavle") {
            permission_container.push("$authority_management_060");
        } else if (this == "readCard") {
            permission_container.push("$cardManage_010");
        } else if (this == "openCard") {
            permission_container.push("$cardManage_003");
        }
    });
    return permission_container;
}

function clearRole() {
    $("#role_name3")[0].value = "";
    $('input:checkbox').removeAttr('checked');
}

function addRole() {
    var role_name = $("#roleName1").val().trim();
    var permission = [];
    if (permission != null) {
        $('input:checkbox[name="access"]:checked').each(function () {
            permission.push($(this).val());
        });
    } else {
        permission = " ";
    }
    var role_add_time = format(new Date().getTime(), 'yyyy.MM.dd HH:mm:ss')
    if (role_name == null || role_name == "") {
        alert("$authority_management_049");
    } else if (permission == null || permission == "") {
        alert("$authority_management_050");
    } else {
        jQuery.ajax({
            url: "../admin/addRole.do",
            type: "post",
            dataType: "json",
            data: {
                "role_name": role_name,
                "permission": permission,
                "role_add_time": role_add_time
            },
            traditional: true,
            success: function (result) {
                if (result.status == 1) {
                    alert("$public_051");
                    $('#tab-authority-add').modal('hide');
                    findRole();
                } else if (result.status == 2) {
                    alert("$authority_management_052");
                } else {
                    alert("$public_052");
                }
            },
            error: function () {
                alert("error");
            }
        });
    }
}
function modifyRole() {
    $(":checkbox").prop("checked", false);
    var role_index = $(this).parents("tr").find("td:eq(0)").text().trim();
    $("#t1").data("role_index", role_index);
    var role_name = $(this).parents("tr").find("td:eq(1)").text();
    var permission = $("#t1").data("permission" + role_index);
    document.getElementById("role-name3").value = role_name;
    var str = permission.split(",");
    jQuery.each(str, function () {
        $(":checkbox[value=" + "'" + this + "'" + "]").prop("checked", true);
    });
}

function cofirmModifyRole() {
    var role_index = $("#t1").data("role_index");
    var role_name = $("#role-name3").val();
    var permission = [];
    $('input:checkbox[name="access1"]:checked').each(function () {
        permission.push($(this).val());
    });
    var id = $("#t1").data("id" + role_index);
    var original_role_name = $("#t1").data("role_name" + role_index);
    var original_permission = $("#t1").data("permission" + role_index);
    jQuery.ajax({
        url: "../admin/modifyRole.do",
        type: "post",
        data: {
            "id": id,
            "role_name": role_name,
            "permission": permission,
            "original_role_name": original_role_name,
            "original_permission": original_permission
        },
        traditional: true,
        dataType: "json",
        success: function (result) {
            if (result.status == 1) {
                alert("$public_041");
                $('#authority-xg').modal('hide');
                findRole();
            }
        },
        error: function () {
            alert("error");
        }
    });
}

function delRole() {
    var role_index = $(this).parents("tr").find("td:eq(0)").text().trim();
    var role_name = $(this).parents("tr").find("td:eq(1)").text().trim();
    var id = $("#t1").data("id" + role_index);
    jQuery.ajax({
        url: "../admin/checkRoleExist.do",
        type: "post",
        data: {
            "id": id
        },
        dataType: "json",
        success: function (result) {
            if (result.status == 1) {

                if (confirm("$public_042")) {
                    jQuery.ajax({
                        url: "../admin/delRole.do",
                        type: "post",
                        data: {
                            "id": id,
                            "role_name": role_name
                        },
                        dataType: "json",
                        success: function (result) {
                            if (result.status == 1) {
                                alert("$public_050");
                                findRole();
                            }
                        },
                        error: function () {
                            alert("error");
                        }
                    });
                }
            } else {
                alert("$authority_management_051");
            }
        },
        error: function () {
            alert("error");
        }
    });
}

$(function () {
    $("#admin").text(account_name);
    $("#sum-to-do").text("(" + toDoNo + ")");
    $("#exit").click(logout);
    load_Sumtodo();
    authorityManagementPermission();
    findRole();
    $("#addRole").click(addRole);
    $("#t1").on("click", ".glyphicon.glyphicon-pencil.list-button", modifyRole);
    $("#roleModify").click(cofirmModifyRole);
    $("#t1").on("click", ".glyphicon.glyphicon-trash.list-button", delRole);
    $(".span1").click(nextPage);
    $(".span2").click(prePage);
    $(".span3").click(firstPage);
    $(".span4").click(lastPage);
    $(".input-page").keypress(function (event) {
        var keycode = (event.keyCode ? event.keyCode : event.which);
        if (keycode == 13) {
            var pageIndex = $(this).val().trim();
            pageIndex = parseInt(pageIndex);
            var totalNo = parseInt($("#t1").data("roles.length"));
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
                findRole();
            }
            return false;
        }
    });
});