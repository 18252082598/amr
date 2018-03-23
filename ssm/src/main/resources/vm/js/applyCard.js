$(function () {
    $("#admin").text(account_name);
    $("#sum-to-do").text("(" + toDoNo + ")");
    $("#exit").click(logout);
    var openCard = openCardPermission();
    if (openCard) {
        findUser();
    }
    $("#findUser").click(findUserBefore);
    //$("#applyCard").click(applyCard);
    $(".span1").click(nextPage);
    $(".span2").click(prePage);
    $(".span3").click(firstPage);
    $(".span4").click(lastPage);
    $(".input-page").keypress(function (event) {
        var keycode = (event.keyCode ? event.keyCode : event.which);
        if (keycode == 13) {
            var pageIndex = $(this).val().trim();
            pageIndex = parseInt(pageIndex);
            var totalNo = parseInt($("#totalNo").text().trim());
            var pageSize = $(".pageSize option:selected").text().trim();
            var pageNo = parseInt(totalNo / pageSize);
            var pageMax = totalNo % pageSize == 0 ? pageNo : pageNo + 1;
            if (pageIndex <= 0) {
                alert("$public_038");
            } else if (pageIndex > pageMax) {
                alert("$public_039");
            } else {
                $(".input-page")[0].value = pageIndex;
                $(".input-page")[1].value = pageIndex;
                findUser();
            }
            return false;
        }
    });
    $("#recharge-success-panel").on("hidden.bs.modal", function () {
        window.location.href = "applyCard.html";
    });
    $(".s1").click(nextPage2);
    $(".s2").click(prePage2);
    $(".s3").click(firstPage2);
    $(".s4").click(lastPage2);
    $("#input-page").keypress(function (event) {
        var keycode = (event.keyCode ? event.keyCode : event.which);
        if (keycode == 13) {
            var pageIndex = $(this).val().trim();
            pageIndex = parseInt(pageIndex);
            var totalNo = parseInt($("#totalNo2").text().trim());
            var pageSize = $(".pageSize2 option:selected").text().trim();
            var pageNo = parseInt(totalNo / pageSize);
            var pageMax = totalNo % pageSize == 0 ? pageNo : pageNo + 1;
            if (pageIndex <= 0) {
                alert("$public_038");
            } else if (pageIndex > pageMax) {
                alert("$public_039");
            } else {
                $("#input-page")[0].value = pageIndex;
            }
            return false;
        }
    });
});
var loadPage = function () {
    findUser();
};
function findUserBefore() {
    $(".input-page").val(1);
    findUser();
}
function findUser() {
    $("#t1 tbody tr").remove();
    $("#t7 tbody tr").remove();
    var pageNo = $(".input-page").val().trim();
    pageNo = pageNo == "" ? 1 : pageNo;
    var pageSize = $(".pageSize option:selected").val();
    var user_name = '';
    var community = '';
    var building = '';
    var unit = '';
    var room = $("#roomNo").val().trim();
    var operator_name = '';
    var meter_no = '';
    var startNo = (pageNo - 1) * pageSize;
    jQuery.ajax({
        url: "../user/findUser.do",
        type: "post",
        data: {
            "user_name": user_name,
            "user_address_community": community,
            "user_address_building": building,
            "user_address_unit": unit,
            "user_address_room": room,
            "operator_name": operator_name,
            "contact_info": "",
            "meter_no": meter_no,
            "status": "",
            "startNo": startNo,
            "pageSize": pageSize,
        },
        dataType: "json",
        success: function (result) {
            if (result.status == 1) {
                var map = result.data;
                var users = map["users"];
                var totalNo = map["totalNo"];
                $("#totalNo").text(totalNo);
                var page = parseInt(totalNo / pageSize);
                var pageMax = totalNo % pageSize == 0 ? page : page + 1;
                $(".glyphicon.glyphicon-step-forward.span4").next().text(pageMax);
                $("#t1").data("totalNo", totalNo);
                for (var i = 0; i < users.length; i++) {
                    var user = users[i];
                    var user_id = user.user_id;
                    var meter_no = user.meter_no;
                    var user_name = user.user_name;
                    var meter_model = user.meter_model;
                    $("#t1").data("meter_model" + meter_no, meter_model);
                    var fee_type = user.fee_type;
                    $("#t1").data("fee_type" + meter_no, fee_type);
                    var meter_type = user.meter_type;
                    if (meter_type == 10) {
                        meter_type = "$user_add_017";
                    } else if (meter_type == 20) {
                        meter_type = "$user_add_018";
                    } else if (meter_type == 30) {
                        meter_type = "$remote_read_047_1";
                    } else if (meter_type == 40) {
                        meter_type = "$remote_read_047_2";
                    }
                    var province = user.province;
                    var city = user.city;
                    var district = user.district;
                    $("#t1").data("meter_model" + meter_no, meter_model);
                    $("#t1").data("fee_type" + meter_no, fee_type);
                    $("#t1").data("meter_type" + meter_no, meter_type);
                    $("#t1").data("province" + meter_no, province);
                    $("#t1").data("city" + meter_no, city);
                    $("#t1").data("district" + meter_no, district);
                    var user_address_area = user.user_address_area;
                    $("#t1").data("user_address_area" + meter_no, user_address_area);
                    var user_address_community = user.user_address_community;
                    $("#t1").data("user_address_community" + meter_no, user_address_community);
                    var user_address_building = user.user_address_building;
                    $("#t1").data("user_address_building" + meter_no, user_address_building);
                    var user_address_unit = user.user_address_unit;
                    $("#t1").data("user_address_unit" + meter_no, user_address_unit);
                    var user_address_room = user.user_address_room;
                    $("#t1").data("user_address_room" + meter_no, user_address_room);
                    var contact_info = user.contact_info;
                    var id_card_no = user.id_card_no;
                    var house_area = user.house_area;
                    var house_coefficient = user.house_coefficient;
                    var concentrator_name = user.concentrator_name;
                    var meter_status = user.meter_status;
                    $("#t1").data("meter_status" + meter_no, meter_status);
                    var user_status = user.user_status;
                    $("#t1").data("user_status" + meter_no, user_status);
                    var user_status_str = user_status == 0 ? "$user_manage_101" : "$user_manage_102";
                    var operator_account = user.operator_account;
                    var last_balance = user.last_balance;
                    if (last_balance !== parseInt(last_balance)) {
                        last_balance = last_balance.toFixed(2);
                    }
                    $("#t1").data("last_balance" + meter_no, last_balance);
                    var last_read_time = user.last_read_time;
                    $("#t1").data("last_read_time" + meter_no, last_read_time);
                    var supplier_name = user.supplier_name;
                    $("#t1").data("supplier_name" + meter_no, supplier_name);
                    var str = "";
                    str += '<tr><td>' + (startNo + (i + 1)) + '</td>';
                    str += '<td>' + user_name + '</td>';
                    str += '<td>' + user_address_community + '</td>';
                    str += '<td>' + user_address_building + '</td>';
                    str += '<td>' + user_address_unit + '</td>';
                    str += '<td>' + user_address_room + '</td>';
                    str += '<td>' + contact_info + '</td>';
                    str += '<td>' + concentrator_name + '</td>';
                    str += '<td>' + meter_no + '</td>';
                    str += '<td>' + fee_type + '</td>';
                    str += '<td>' + last_balance + '</td>';
                    str += '<td>' + user_status_str + '</td>';
                    if (user_status == "1" && meter_status == "1") {
                        str += '<td ></td></tr>';
                    } else {
                        str += '<td ></td></tr>';
                    }
                    $("#t1 tbody").append($(str));
                }
                $("#t1 tbody tr:eq(0) td:eq(0)").click();
            } else {
                alert("$public_036");
            }
        },
        error: function () {
            alert("error");
        }
    });
}

function nextPage2() {
    var pageIndex = $("#input-page").val().trim();
    pageIndex = parseInt(pageIndex) + 1;
    var totalNo = parseInt($("#totalNo2").text().trim());
    var pageSize = $(".pageSize2 option:selected").val().trim();
    var pageNo = parseInt(totalNo / pageSize);
    var pageMax = totalNo % pageSize == 0 ? pageNo : pageNo + 1;
    if (pageIndex <= 0) {
        alert("$public_038");
    } else if (pageIndex > pageMax) {
        alert("$public_039");
    } else {
        $("#input-page")[0].value = pageIndex;
    }
}

function prePage2() {
    var pageIndex = $("#input-page").val().trim();
    pageIndex = parseInt(pageIndex) - 1;
    if (pageIndex <= 0) {
        alert("$public_040");
    } else {
        $("#input-page")[0].value = pageIndex;
    }
}

function firstPage2() {
    $("#input-page")[0].value = 1;
}

function lastPage2() {
    var totalNo = parseInt($("#totalNo2").text().trim());
    var pageSize = $(".pageSize2 option:selected").val().trim();
    var pageNo = parseInt(totalNo / pageSize);
    var pageEnd = totalNo % pageSize == 0 ? pageNo : pageNo + 1;
    $("#input-page")[0].value = pageEnd;
}

function applyCard() {
    var pageNo = $(".input-page").val().trim();
    pageNo = pageNo == "" ? 1 : pageNo;
    var pageSize = $(".pageSize option:selected").val();
    var user_name = '';
    var community = '';
    var building = '';
    var unit = '';
    var room = $("#roomNo").val().trim();
    if (room === 'undefined' || room === '') {
        alert("$cardManage_009");
        return;
    }
    var operator_name = '';
    var meter_no = '';
    var startNo = (pageNo - 1) * pageSize;
    jQuery.ajax({
        url: "../user/findUser.do",
        type: "post",
        data: {
            "user_name": user_name,
            "user_address_community": community,
            "user_address_building": building,
            "user_address_unit": unit,
            "user_address_room": room,
            "operator_name": operator_name,
            "contact_info": "",
            "meter_no": meter_no,
            "status": "",
            "startNo": startNo,
            "pageSize": pageSize,
        },
        dataType: "json",
        success: function (result) {
            if (result.status === 1) {
                var map = result.data;
                var users = map["users"];
                if (users.length === 0) {
                    alert('$cardManage_008');
                    return;
                }
                testWebSocket();
            } else {
                alert("$public_036");
            }
        },
        error: function () {
            alert("error");
        }
    });
}
