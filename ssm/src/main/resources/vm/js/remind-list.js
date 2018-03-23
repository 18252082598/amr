$(function () {
    $("#admin").text(account_name);
    $("#sum-to-do").text("(" + toDoNo + ")");
    $("#exit").click(logout);
    load_Sumtodo();
    reminderListPermission();
    load_communities();
    load_operators();
    load_users_remind();
    $("#search").click(load_users_remindBefore);
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
                load_users_remind();
            }
            return false;
        }
    });
    $("#markup").click(markup);
    $("#cancelMarkup").click(cancelMarkup);
    $("#import-remind").click(exportRemind);
})
var loadPage = function () {
    load_users_remind();
};
function load_users_remindBefore(){
    $(".input-page").val(1);
    load_users_remind();
}
function load_users_remind() {
    $("#t1 tbody tr").remove();
    var pageNo = $(".input-page").val().trim();
    pageNo = pageNo == "" ? 1 : pageNo;
    var pageSize = parseInt($(".pageSize option:selected").val().trim());
    var user_name = $("#user_name").val().trim();
    var community = $("#user_address_community option:selected").val().trim();
    var building = $("#user_address_building option:selected").val().trim();
    var unit = $("#user_address_unit option:selected").val().trim();
    var room = $("#user_address_room option:selected").val().trim();
    var operator_name = $("#operator_name option:selected").val().trim();
    var meter_no = $("#meter_no").val().trim();
    var startNo = (pageNo - 1) * pageSize;
    jQuery.ajax({
        url: "../user/loadUsersRemind.do",
        type: "post",
        data: {
            "user_name": user_name,
            "community": community,
            "building": building,
            "unit": unit,
            "room": room,
            "operator_name": operator_name,
            "meter_no": meter_no,
            "startNo": startNo,
            "pageSize": pageSize,
        },
        dataType: "json",
        success: function (result) {
            var map = result.data;
            var users = map["users"];
            var totalNo = map["totalNo"];
            $("#totalNo").text(totalNo);
            var page = parseInt(totalNo / pageSize);
            var pageMax = totalNo % pageSize == 0 ? page : page + 1;
            $(".glyphicon.glyphicon-step-forward").next().find("b").text(pageMax);
            for (var i = 0; i < users.length; i++) {
                var user = users[i];
                var user_name = user.user_name;
                var user_address_area = user.user_address_area;
                var user_address_community = user.user_address_community;
                var user_address_building = user.user_address_building;
                var user_address_unit = user.user_address_unit;
                var user_address_room = user.user_address_room;
                var contact_info = user.contact_info;
                var concentrator_name = user.concentrator_name;
                var meter_no = user.meter_no;
                var fee_type = user.fee_type;
                var balance = user.last_balance;
                balance = balance == parseInt(balance) ? balance : balance.toFixed(2);
                var pay_remind = user.pay_remind;
                var operator_account = user.operator_account;
                var last_read_time = user.last_read_time;
                last_read_time = format(last_read_time, 'yyyy.MM.dd HH:mm:ss');
                var str = "";
                str += '<tr><td><input type="checkbox"></td>';
                if (pay_remind == "1") {
                    str += '<td><span class="glyphicon glyphicon-flag" title="$reminder_list_008"></span></td>';
                } else {
                    str += '<td><span class="glyphicon glyphicon-info-sign" title="$reminder_list_009"></span></td>';
                }
                str += '<td>' + user_name + '</td>';
                str += '<td>' + user_address_community + '</td>';
                str += '<td>' + user_address_building + '</td>';
                str += '<td>' + user_address_unit + '</td>';
                str += '<td>' + user_address_room + '</td>';
                str += '<td>' + contact_info + '</td>';
                str += '<td>' + concentrator_name + '</td>';
                str += '<td>' + meter_no + '</td>';
                str += '<td>' + fee_type + '</td>';
                str += '<td>' + balance + '</td>';
                str += '<td>' + operator_account + '</td>';
                str += '<td>' + last_read_time + '</td></tr>';
                $("#t1 tbody").append($(str));
            }
        },
        error: function () {
            alert("error");
        }
    });
}

function markup() {
    if ($("#t1 tbody :checked").length == 0) {
        alert("$reminder_list_010");
    } else {
        $("#t1 tbody :checked").each(function () {
            var meter_no = $(this).parents("tr").find("td:eq(9)").text().trim();
            var user_name = $(this).parents("tr").find("td:eq(2)").text().trim();
            jQuery.ajax({
                url: "../user/markup.do",
                type: "post",
                data: {"meter_no": meter_no,
                    "user_name": user_name
                },
                dataType: "json",
                success: function (result) {
                    if (result.status == 1) {
                        $("#t1 tbody tr td:contains(" + meter_no + ")").each(function () {
                            $(this).parents("tr").find("td:eq(1)").replaceWith('<td><span class="glyphicon glyphicon-flag" title="$reminder_list_008"></span></td>');
                        });
                    }
                },
                error: function () {
                    alert("error");
                }
            });
        });
    }
}

function cancelMarkup() {
    if ($("#t1 tbody :checked").length == 0) {
        alert("$reminder_list_010");
    } else {
        $("#t1 tbody :checked").each(function () {
            var meter_no = $(this).parents("tr").find("td:eq(9)").text().trim();
            var user_name = $(this).parents("tr").find("td:eq(2)").text().trim();
            jQuery.ajax({
                url: "../user/delMarkup.do",
                type: "post",
                data: {"meter_no": meter_no,
                    "user_name": user_name
                },
                dataType: "json",
                success: function (result) {
                    if (result.status == 1) {
                        //alert(result.msg);
                        $("#t1 tbody tr td:contains(" + meter_no + ")").each(function () {
                            $(this).parents("tr").find("td:eq(1)").replaceWith('<td><span class="glyphicon glyphicon-info-sign" title="$reminder_list_009"></span></td>');
                        });
                    }
                },
                error: function () {
                    alert("error");
                }
            });
        });
    }
}

function exportRemind() {
    var user_name = $("#user_name").val().trim();
    var community = $("#user_address_community option:selected").val().trim();
    var building = $("#user_address_building option:selected").val().trim();
    var unit = $("#user_address_unit option:selected").val().trim();
    var operator_name = $("#operator_name option:selected").val().trim();
    var meter_no = $("#meter_no").val().trim();
    var language = getCookie("language");
    var totalNo = parseInt($("#totalNo").text().trim());
    var fileType = "EXCEL";
    var url = "../report/exportRemind.do?user_name=" + user_name + "&community="
            + community + "&building=" + building + "&unit=" + unit +
            "&operator_name=" + operator_name + "&meter_no=" + meter_no
            + "&language=" + language + "&totalNo=" + totalNo + "&fileType=" + fileType;
    window.location.href = url;
    var mask = $("<div class='window_mask'></div>");
    $("body").css("position", "relative");
    mask.appendTo("body");
    alert("$public_064");
    var f = setInterval(function () {
        jQuery.ajax({
            url: "../report/checkStatus.do",
            success: function (result) {
                if (result.status == 1) {
                    mask.remove();
                    clearInterval(f);
                }
            },
            error: function () {
                alert("error");
            }
        });
    }, 5000);
}

