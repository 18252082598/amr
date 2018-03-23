$(function () {
    $("#admin").text(account_name);
    $("#sum-to-do").text("(" + toDoNo + ")");
    $("#exit").click(logout);
    load_Sumtodo();
    var remote_read = $("#remote_read");
    var Cb = CbPermission();
    if (Cb) {
        remote_read.append("<li class='active'><a href='#read-by-search'  data-toggle='tab'>" + "$remote_read_002" + "</a></li>");
        load_communities();
        findUsersToRead();
        var rocv = $("#rocv");
        var selectedCb = selectedCbPermission();
        if (selectedCb) {
            rocv.append("<button id='read_meter' class='button blue' type='button' data-toggle='modal'>" + "$remote_read_011" + "</button>");
        }
        var ForcedOpeningVavle = ForcedOpeningVavlePermission();
        if (ForcedOpeningVavle) {
            rocv.append("<button id='open_valve' class='button white' type='button'>" + "$remote_read_012" + "</button>");
        }
        var ForcedClosingVavle = ForcedClosingVavlePermission();
        if (ForcedClosingVavle) {
            rocv.append("<button id='close_valve' class='button white' type='button'>" + "$remote_read_013" + "</button>");
        }
        var AutomaticControlVavle = AutomaticControlVavlePermission();
        if (AutomaticControlVavle) {
            rocv.append("<button id='valve_selfcontrol' class='button white' type='button'>" + "$remote_read_01301" + "</button>");
        }
    }
    var CbByCommuity = CbByCommuityPermission();
    if (CbByCommuity) {
        remote_read.append("<li class=''><a href='#read-by-xiaoqu'  data-toggle='tab'>" + "$remote_read_007" + "</a></li>");
        loadCommunityInfo();
    }
    var CbByTiming = CbByTimingPermission();
    if (CbByTiming) {
        remote_read.append("<li class=''><a href='#autochb'  data-toggle='tab'>" + "$remote_read_008" + "</a></li>");
        showReadParameterByTiming();
    }
    var CbByCycle = CbByCyclePermission();
    if (CbByCycle) {
        remote_read.append("<li class=''><a href='#intervalchb'  data-toggle='tab'>" + "$remote_read_00802" + "</a></li>");
        showReadParameterByCycle();
    }

    $("#findUser").click(findUsersToReadBefore);
    $(".span1").click(nextPage);
    $(".span2").click(prePage);
    $(".span3").click(firstPage);
    $(".span4").click(lastPage);
    $(".input-page").keypress(enterKeyAction);
    $("#read_meter").click(readMeter);
    //$("#send_parameters2 button").click(sendUserParameters);
    $("#open_valve").click(openOrCloseValve);
    $("#close_valve").click(openOrCloseValve);
    $("#valve_selfcontrol").click(openOrCloseValve);
    $("#searchCommunity").click(searchCommunityBefore);
    $(".s1").click(nextPage2);
    $(".s2").click(prePage2);
    $(".s3").click(firstPage2);
    $(".s4").click(lastPage2);
    $(".input-page.pageIndex").keypress(function (event) {
        var keycode = (event.keyCode ? event.keyCode : event.which);
        if (keycode == 13) {
            var pageIndex = $(this).val().trim();
            pageIndex = parseInt(pageIndex);
            var totalNo = parseInt($("#t2").data("communities.length"));
            var pageSize = $(".pageSize2 option:selected").val().trim();
            var pageNo = parseInt(totalNo / pageSize);
            var pageMax = totalNo % pageSize == 0 ? pageNo : pageNo + 1;
            if (pageIndex <= 0) {
                alert("$public_038");
            } else if (pageIndex > pageMax) {
                alert("$public_039");
            } else {
                $(".input-page.pageIndex")[0].value = pageIndex;
                $(".input-page.pageIndex")[1].value = pageIndex;
                loadCommunityInfo();
            }
            return false;
        }
    });
    $("#t2 tfoot button").click(readAll);
    $("#send_parameters button").click(sendCommunityParameters);
    $("#saveParameter").click(saveReadParameter);
    $("#saveParameter2").click(saveReadParameter2);
    var status = getQueryString("class");
    if (status == "active") {
        $("[href='#autochb']").click();
    }
})

/*
 * 此变量的存在的意义在于每次抄表或者开关阀时将其赋值给定时器，抄表或者开关阀完成时将其消除，如果在抄表或者开关阀过程中点击了搜索按钮或者其他按钮
 * 此时定时器会在后台不停运行，而前台页面可能已经改变，导致系统性能低下，所以在点击其他按钮时应该消除定时器，也就是消除check即可
 */
var check;
var loadPage = function () {
    findUsersToRead();
};
function findUsersToReadBefore() {
    if (check) {
        clearInterval(check);
    }
    $("#open_valve").attr("disabled", false);
    $("#close_valve").attr("disabled", false);
    $(".input-page").val(1);
    findUsersToRead();
}
function findUsersToRead() {
    $("#t1 tbody tr").remove();
    var pageNo = $(".input-page").val().trim();
    var pageSize = $(".pageSize option:selected").val().trim();
    var user_name = $("#user_name").val().trim();
    var community = $("#user_address_community option:selected").val().trim();
    var building = $("#user_address_building option:selected").val().trim();
    var unit = $("#user_address_unit option:selected").val().trim();
    var room = $("#user_address_room option:selected").val().trim();
    var meter_no = $("#meter_no").val().trim();
    var startNo = (pageNo - 1) * pageSize;
    jQuery.ajax({
        url: "../user/findUsersToRead.do",
        type: "post",
        data: {
            "user_name": user_name,
            "user_address_community": community,
            "user_address_building": building,
            "user_address_unit": unit,
            "user_address_room": room,
            "operator_name": "",
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
                $("#t1").data("totalNo", totalNo);
                var page = parseInt(totalNo / pageSize);
                var pageMax = totalNo % pageSize == 0 ? page : page + 1;
                $(".glyphicon.glyphicon-step-forward.span4").next().text(pageMax);
                for (var i = 0; i < users.length; i++) {
                    var user = users[i];
                    var user_id = user.user_id;
                    var meter_no = user.meter_no;
                    var fee_type = user.fee_type;
                    var user_name = user.user_name;
                    var meter_type = user.meter_type;
                    var user_address_community = user.user_address_community;
                    var user_address_building = user.user_address_building;
                    var user_address_unit = user.user_address_unit;
                    var user_address_room = user.user_address_room;
                    var contact_info = user.contact_info;
                    var concentrator_name = user.concentrator_name;
                    var user_status = user.user_status;
                    user_status = user_status = "1" ? "$user_manage_102" : "$user_manage_101";
                    var valve_status = user.valve_status === "1" ? "$remote_read_069" : "$remote_read_070"
                    var meter_status = user.meter_status;
                    switch (meter_status) {
                        case "0":
                            meter_status = "$user_manage_102_01";
                            break;
                        case "1":
                            meter_status = "$user_manage_102";
                            break;
                        case "2":
                            meter_status = "$user_manage_103";
                            break;
                        case "3":
                            meter_status = "$user_manage_054";
                            break;
                        case "4":
                            meter_status = "$user_manage_108";
                            break;
                        case "5":
                            meter_status = "$user_manage_116";
                            break;
                        case "6":
                            meter_status = "$user_manage_113";
                            break;
                    }
                    var protocol_type = user.protocol_type;
                    var valve_protocol = user.valve_protocol;
                    var user_index = i + 1 + startNo;
                    var str = "";
                    str += '<tr><td><form  class="lef"><input type="checkbox"></form></td>';
                    str += '<td>' + user_index + '</td>';
                    str += '<td>' + user_name + '</td>';
                    str += '<td>' + user_address_community + '</td>';
                    str += '<td>' + user_address_building + '</td>';
                    str += '<td>' + user_address_unit + '</td>';
                    str += '<td>' + user_address_room + '</td>';
                    str += '<td>' + contact_info + '</td>';
                    str += '<td>' + concentrator_name + '</td>';
                    str += '<td>' + fee_type + '</td>';
                    str += '<td>' + meter_no + '</td>';
                    str += '<td>' + user_status + '</td>';
                    str += '<td>' + meter_status + '</td>';
                    str += '<td>' + valve_status + '</td>';
                    str += '<td></td></tr>';
                    $("#t1 tbody").append($(str));
                    $("#t1").data("meter_type" + user_index, meter_type);
                    $("#t1").data("protocol_type" + user_index, protocol_type);
                    $("#t1").data("valve_protocol" + user_index, valve_protocol);
                }
            }
        },
        error: function () {
            alert("error");
        }
    });
}

//抄表确认
function readMeter() {
    var users = $("#t1 tbody").find(":checked");
    if (users.length == 0) {
        alert("$remote_read_044");
    } else {
        //$('#chb2').modal('show');
        sendUserParameters();
    }
}

//发送抄表请求(按用户抄表)
function sendUserParameters() {
    //var auto = $(this).text().trim();
    //$('#chb2').modal('hide');
    //var isAutoClear = auto == "$remote_read_039" ? 0 : 1;//是：0 
    var isAutoClear = 0;
    var accountName = getCookie("account_name");
    var arr = new Array();
    var meter_noFalseList = new Array();
    var meter_noSucList = new Array();
    $("#t1 tbody :checked").each(function () {
        $(this).parents("tr").find("td:eq(14)").replaceWith('<td><img src="../images/all/loading_icon_u478.gif" title="$remote_read_045"></td>');
        var user_index = $(this).parents("tr").find("td:eq(1)").text().trim();
        var concentrator_name = $(this).parents("tr").find("td:eq(8)").text().trim();
        var meter_no = $(this).parents("tr").find("td:eq(10)").text().trim();
        var meter_type = $("#t1").data("meter_type" + user_index);
        var protocol_type = $("#t1").data("protocol_type" + user_index);
        var ary = new Array(concentrator_name, meter_no, meter_type, protocol_type);//集中器名称，表号，表类型，协议类型
        arr.push(ary);
    });
    arr.push(new Array("", "", "", ""));
    // 从服务器返回一个时间点，查询这个时间点之后的抄表数据，以便更新该表抄表状态
    var sendTime = format(new Date(), "yyyy-MM-dd HH:mm:ss");
    jQuery.ajax({
        url: "../user/sendUserParameters.do",
        type: "post",
        data: {
            "list": arr,
            "isAutoClear": isAutoClear,
            "accountName": accountName,
        },
        dataType: "json",
        traditional: true,
        success: function (result) {
            if (result.status === 1) {
                var map = result.data;
                meter_noFalseList = map.meter_noFalseList;
                meter_noSucList = map.meter_noSucList;
                sendTime = map.sendTime;
                if (check) {
                    clearInterval(check);
                }
                check = setInterval(function () {
                    jQuery.ajax({
                        url: "../user/findReadResultByOperator.do",
                        type: "post",
                        data: {
                            "sendTime": sendTime,
                            "accountName": accountName,
                        },
                        dataType: "json",
                        success: function (result) {
                            if (result.status === 1) {
                                var count1 = 0;
                                var count2 = 0;
                                var count3 = 0;
                                var readInfo = result.data;
                                for (var i = 0; i < meter_noFalseList.length; i++) {
                                    var meter_noFalse = meter_noFalseList[i];
                                    $("#t1 tbody td:nth-child(11)").each(function () {
                                        if ($(this).text() === meter_noFalse) {
                                            var td = $(this).parent().find("td:last");
                                            td.replaceWith('<td><span class="glyphicon glyphicon-warning-sign" title="$read_list_063"></span></td>');
                                        }
                                    });
                                    count2++;
                                }
                                for (var i = 0; i < readInfo.length; i++) {
                                    var meter_noSuc = readInfo[i].meter_no;
                                    var exception = readInfo[i].exception;
                                    var batteryStatus = readInfo[i].data9;
                                    $("#t1 tbody td:nth-child(11)").each(function () {
                                        if ($(this).text() === meter_noSuc) {
                                            var td = $(this).parent().find("td:last");
                                            if (exception === "1") {
                                                count1++;
                                                td.replaceWith('<td><span class="glyphicon glyphicon-ok" title="$remote_read_063"></span></td>');
                                            } else {
                                                count2++;
                                                td.replaceWith('<td><span class="glyphicon glyphicon-remove" title="$remote_read_064"></span></td>');
                                            }
                                            if (batteryStatus !== "") {
                                                count3++;
                                            }
                                        }
                                    });
                                }
                                if (meter_noSucList.length === readInfo.length) {
                                    clearInterval(check);
                                    $("#alertM p span:eq(0)").text(count1);
                                    $("#alertM p span:eq(1)").text(count2);
                                    $("#alertM p span:eq(2)").text(count3);
                                    $("#alertM").show(3000);
                                }
                            } else {
                                clearInterval(check);
                                $("#t1 tbody :checked").each(function () {
                                    $(this).parents("tr").find("td:eq(14)").replaceWith('<td><span class="glyphicon glyphicon-remove" title="$remote_read_050"></span></td>');
                                });
                                alert("$public_066");
                            }
                        },
                        error: function () {
                            $("#t1 tbody :checked").each(function () {
                                $(this).parents("tr").find("td:eq(14)").replaceWith('<td><span class="glyphicon glyphicon-remove" title="$remote_read_050"></span></td>');
                            });
                            alert("$remote_read_071");
                        }
                    });
                }, 3000);
            } else {
                $("#t1 tbody :checked").each(function () {
                    $(this).parents("tr").find("td:eq(14)").replaceWith('<td><span class="glyphicon glyphicon-remove" title="$remote_read_050"></span></td>');
                });
                alert("$public_066");
            }
        },
        error: function () {
            $("#t1 tbody :checked").each(function () {
                $(this).parents("tr").find("td:eq(14)").replaceWith('<td><span class="glyphicon glyphicon-remove" title="$remote_read_050"></span></td>');
            });
            alert("$remote_read_051");
        }
    });

}

// 发送开阀或关阀请求
function openOrCloseValve() {
    var users = $("#t1 tbody").find(":checked");
    if (users.length == 0) {
        alert("$remote_read_044");
        return;
    }
    $("#open_valve").attr("disabled", true);
    $("#close_valve").attr("disabled", true);
    var checkedDOMs;
    var checkedLen;
    var operateType = $(this).text().trim();
    var accountName = getCookie("account_name");
    var arr = new Array();
    $("#t1 tbody :checked").each(function () {
        var user_index = $(this).parents("tr").find("td:eq(1)").text().trim();
        var valve_protocol = $("#t1").data("valve_protocol" + user_index);
        if (!valve_protocol) {
            return true;
        }
        var meter_no = $(this).parents("tr").find("td:eq(10)").text().trim();
        $(this).parents("tr").find("td:eq(14)").html('<img src="../images/all/loading_icon_u478.gif" title="$remote_read_045">');
        var concentrator_name = $(this).parents("tr").find("td:eq(8)").text().trim();
        var meter_no = $(this).parents("tr").find("td:eq(10)").text().trim();
        var meter_type = $("#t1").data("meter_type" + user_index);
        var valve_status = $(this).parents("tr").find("td:eq(13)");
        var valve_status_flag = false;
        var protocol_type = "";

        if (operateType === "$remote_read_012") {//强制开阀

//            if (valve_status.text() === "$remote_read_069") {
//                valve_status_flag = true;
//            }
            //protocol_type = "com.joymeter.dtu.data.other.ParseElecOpenValveData_001";
            protocol_type = valve_protocol + '_Open';
        } else if (operateType === "$remote_read_013") {//强制关阀
//            if (valve_status.text() === "$remote_read_070") {
//                valve_status_flag = true;
//            }
            //protocol_type = "com.joymeter.dtu.data.other.ParseElecCloseValveData_001";
            protocol_type = valve_protocol + '_Close';
        } else if (operateType === "$remote_read_01301") {//阀门自控
            protocol_type = valve_protocol + '_Free';
        }
        if (valve_status_flag === false) {
            var ary = new Array(concentrator_name, meter_no, meter_type, protocol_type);
            arr.push(ary);
        } else {
            $(this).parents("tr").find("td:eq(14)").html('<span class="glyphicon glyphicon-ok"></span>');
            $(this).parents("tr").find("td:eq(0)").find("input").attr("checked", false);
            $("#open_valve").attr("disabled", false);
            $("#close_valve").attr("disabled", false);
        }
    });
    arr.push(new Array("", "", "", ""));
    checkedDOMs = $("#t1 tbody :checked");
    checkedLen = checkedDOMs.length;
    //从服务器返回一个时间点，查询这个时间点之后的阀门操作数据
    var sendTime = format(new Date(), "yyyy-MM-dd HH:mm:ss");
    if (arr.length > 1) {
        jQuery.ajax({
            url: "../user/openOrCloseValve.do",
            type: "post",
            data: {
                "list": arr,
                "accountName": accountName,
            },
            dataType: "json",
            async: false,
            traditional: true,
            success: function (result) {
                if (result.status === 1) {
                    sendTime = result.data.sendTime;
                    if (check) {
                        clearInterval(check);
                    }
                    check = setInterval(function () {
                        jQuery.ajax({
                            url: "../user/findValveStatus.do",
                            type: "post",
                            data: {"sendTime": sendTime},
                            dataType: "json",
                            success: function (result) {
                                if (result.status == 1) {
                                    var readInfo = result.data;
                                    var returnCount = 0;
                                    for (var i = 0; i < readInfo.length; i++) {
                                        var meter_no = readInfo[i].meter_no;
                                        var exception = readInfo[i].exception;
                                        var valve_state = readInfo[i].data9;
                                        $("#t1 tbody td:nth-child(11)").each(function () {
                                            if ($(this).text() === meter_no) {
                                                returnCount++;
                                                if (exception === "1") {
                                                    $(this).parent().find("td:eq(14)").html('<span class="glyphicon glyphicon-ok"></span>');
                                                    //var valve_state = $(this).parent().find("td:eq(13)").html();
                                                    //if (valve_state === "$remote_read_069") {
                                                    if (valve_state === "0") {
                                                        $(this).parent().find("td:eq(13)").html("$remote_read_070");
                                                    }
                                                    //if (valve_state === "$remote_read_070") {
                                                    if (valve_state === "1") {
                                                        $(this).parent().find("td:eq(13)").html("$remote_read_069");
                                                    }

                                                } else {
                                                    $(this).parent().find("td:eq(14)").html('<span class="glyphicon glyphicon-remove"></span>');
                                                }
                                            }
                                        });
                                    }
                                    if (returnCount === checkedLen) {
                                        clearInterval(check);
                                        $("#open_valve").attr("disabled", false);
                                        $("#close_valve").attr("disabled", false);
                                    }
                                }
                            },
                            error: function () {
                                alert("error");
                                clearInterval(check);
                            }
                        });
                    }, 3000);
                }
            },
            error: function () {
                $("#t1 tbody :checked").each(function () {
                    $(this).parents("tr").find("td:eq(13)").html('<span class="glyphicon glyphicon-remove"></span>');
                });
                //alert("$remote_read_051");
            }
        });
    }

}

function loadCommunityInfo() {
    var community_address = $("#name").val().trim();
    $("#t2 tbody tr").remove();
    var pageNo = $(".input-page.pageIndex").val().trim();
    pageNo = pageNo == "" ? 1 : pageNo;
    var pageSize = $(".pageSize2 option:selected").val();
    jQuery.ajax({
        url: "../user/findCommunityInfo.do",
        type: "post",
        dataType: "json",
        data: {
            "community_address": community_address
        },
        success: function (result) {
            if (result.status == 1) {
                var communities = result.data;
                $("#t2").data("communities.length", communities.length);
                var page = parseInt(communities.length / pageSize);
                var pageMax = communities.length % pageSize == 0 ? page : page + 1;
                $(".glyphicon.glyphicon-step-forward.s4").next().text(pageMax);
                for (var i = pageSize * (pageNo - 1); i < pageNo * pageSize && i < communities.length; i++) {
                    var community = communities[i];
                    var str = "";
                    str += "<tr><td><input type='checkbox'><td>" + community[0] + "</td></td>";
                    str += "<td>" + community[1] + "</td><td>" + community[2] + "</td>";
                    str += "<td style='color:orangered'>0</td><td>0</td><td>$remote_read_055</td></tr>";
                    $("#t2 tbody").append($(str));
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
function searchCommunityBefore() {
    $(".input-page.pageIndex").val(1);
    searchCommunity();
}
function searchCommunity() {
    var community_address = $("#name").val().trim();
    $("#t2 tbody tr").remove();
    var pageNo = $(".input-page.pageIndex").val().trim();
    pageNo = pageNo == "" ? 1 : pageNo;
    var pageSize = $(".pageSize2 option:selected").val().trim();
    jQuery.ajax({
        url: "../user/findCommunityInfo.do",
        type: "post",
        data: {
            "community_address": community_address
        },
        dataType: "json",
        success: function (result) {
            if (result.status == 1) {
                var communities = result.data;
                $("#t2").data("communities.length", communities.length);
                var page = parseInt(communities.length / pageSize);
                var pageMax = communities.length % pageSize == 0 ? page : page + 1;
                $(".glyphicon.glyphicon-step-forward.s4").next().text(pageMax);
                for (var i = pageSize * (pageNo - 1); i < pageNo * pageSize && i < communities.length; i++) {
                    var community = communities[i];
                    if (name == "" || name == community[0] || name == community[1]) {
                        var str = "";
                        str += "<tr><td><input type='checkbox'><td>" + community[0] + "</td></td>";
                        str += "<td>" + community[1] + "</td><td>" + community[2] + "</td>";
                        str += "<td style='color:orangered'>0</td><td>0</td><td>$remote_read_055</td></tr>";
                        $("#t2 tbody").append($(str));
                    }
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
function change2(pageSize) {
    $(".pageSize2").find("option[value='" + pageSize + "']").attr("selected", true);
    loadCommunityInfo();
}

function nextPage2() {
    var pageIndex = $(".input-page.pageIndex").val().trim();
    pageIndex = parseInt(pageIndex) + 1;
    var totalNo = parseInt($("#t2").data("communities.length"));
    var pageSize = $(".pageSize2 option:selected").val().trim();
    var pageNo = parseInt(totalNo / pageSize);
    var pageMax = totalNo % pageSize == 0 ? pageNo : pageNo + 1;
    if (pageIndex <= 0) {
        alert("$public_038");
    } else if (pageIndex > pageMax) {
        alert("$public_039");
    } else {
        $(".input-page.pageIndex")[0].value = pageIndex;
        $(".input-page.pageIndex")[1].value = pageIndex;
        loadCommunityInfo();
    }
}

function prePage2() {
    var pageIndex = $(".input-page.pageIndex").val().trim();
    pageIndex = parseInt(pageIndex) - 1;
    if (pageIndex <= 0) {
        alert("$public_040");
    } else {
        $(".input-page.pageIndex")[0].value = pageIndex;
        $(".input-page.pageIndex")[1].value = pageIndex;
        loadCommunityInfo();
    }
}

function firstPage2() {
    $(".input-page.pageIndex")[0].value = 1;
    $(".input-page.pageIndex")[1].value = 1;
    loadCommunityInfo();
}

function lastPage2() {
    var totalNo = parseInt($("#t2").data("communities.length"));
    var pageSize = $(".pageSize2 option:selected").val().trim();
    var pageNo = parseInt(totalNo / pageSize);
    var pageEnd = totalNo % pageSize == 0 ? pageNo : pageNo + 1;
    $(".input-page.pageIndex")[0].value = pageEnd;
    $(".input-page.pageIndex")[1].value = pageEnd;
    loadCommunityInfo();
}

function readByCommunity() {}
function sendByCommunity() {}

function readAll() {
    var communities = $("#t2 tbody").find(":checked");
    if (communities.length == 0) {
        alert("$remote_read_056");
    } else {
//        $('#chb').modal('show');
        sendCommunityParameters();
    }
}

//一键抄(按小区抄表)
function sendCommunityParameters() {
//    var auto = $(this).text().trim();
//    $("#close").click();
//    var isAutoClear = auto == "$remote_read_039" ? 0 : 1;
    var isAutoClear = 0;
    var accountName = getCookie("account_name");
    var index = 0;
    var sendTime = 0;
    var communityMap = new Map();
    $("#t2 tbody :checked").each(function () {
        $(this).parents("tr").find("td:eq(4)").text(0);
        $(this).parents("tr").find("td:eq(5)").text(0);
        $(this).parents("tr").find("td:eq(6)").text("$remote_read_041");
        var community_name = $(this).parents("tr").find("td:eq(1)").text().trim();
        var userNo = $(this).parents("tr").find("td:eq(3)").text().trim();
        userNo = parseInt(userNo);
        communityMap[community_name] = {
            "userNo": userNo //小区总户数
        };
        jQuery.ajax({
            url: "../user/findByCommunity.do",
            type: "post",
            data: {
                "community_name": community_name,
                "isAutoClear": isAutoClear,
                "accountName": accountName
            },
            dataType: "json",
            success: function (result) {
                if (result.status === 1) {
                    if (index === 0) {
                        sendTime = result.data;
                        index++;
                        if (check) {
                            clearInterval(check);
                        }
                        check = setInterval(updateCommunityReadResult, 3000);
                    }
                }
            },
            error: function () {}
        });
    });
    function updateCommunityReadResult() {
        jQuery.ajax({
            url: "../user/updateCommunityReadResult.do",
            data: {
                "sendTime": sendTime,
                "accountName": accountName
            },
            type: "post",
            dataType: "json",
            success: function (result) {
                if (result.status === 1) {
                    var readStatusMap = result.data;
                    var totalUserCount = 0;//抄表用户总数量
                    var totalSuccessCount = 0;//抄表成功的总数量
                    var totalFailCount = 0;//抄表失败的总数量
                    var totalBatteryLowCount = 0;//电池欠压的总数量
                    for (var key in readStatusMap) {
                        var communityName = key;
                        var dataMap = readStatusMap[key];
                        var userCount = communityMap[communityName].userNo;//用户数量
                        var count1 = dataMap["count1"];//抄表成功的数量
                        var count2 = dataMap["count2"];//抄表失败的数量
                        var count3 = dataMap["count3"];//电池欠压的数量
                        var count4 = dataMap["count4"];//抄表读数的总量
                        totalUserCount += userCount;
                        totalSuccessCount += count1;
                        totalFailCount += count2;
                        totalBatteryLowCount += count3;
                        var $tr = $("#t2 tbody td:contains(" + communityName + ")").parent();
                        var str = "$remote_read_057" + (count1 + count2) + ";$remote_read_058" + count1 + ";$remote_read_059" + count2;
                        count4 = count4 === 0 ? 0 : count4.toFixed(2);
                        $tr.find("td:eq(4)").text(str);
                        $tr.find("td:eq(5)").text(count4);
                        if ((count1 + count2) === userCount) {
                            var readStatus = "$remote_read_060";
                            $tr.find("td:eq(6)").text(readStatus);
                        }
                    }
                    if (totalUserCount && (totalSuccessCount + totalFailCount) === totalUserCount) {
                        clearInterval(check);
                        $("#alertM p span:eq(0)").text(totalSuccessCount);
                        $("#alertM p span:eq(1)").text(totalFailCount);
                        $("#alertM p span:eq(2)").text(totalBatteryLowCount);
                        $("#alertM").slideDown();
                    }
                }
            },
            error: function () {}
        });
    }
}

// 保存定时抄表参数
function saveReadParameter() {
    //获取修改后的值
    var day = $("#day").text().trim();
    //获取修改前的值
    var original_day = $("#autochb").data("day");
    var hour = $("#hour").text().trim();
    var original_hour = $("#autochb").data("hour");
    var minute = $("#minute").text().trim();
    var original_minute = $("#autochb").data("minute");
    var second = "00";
    day = day.substring(0, day.indexOf("$remote_read_021"));
    hour = hour.substring(0, hour.indexOf("$remote_read_022"));
    minute = minute.substring(0, minute.indexOf("$remote_read_023"));
    //second = second.substring(0, second.indexOf("$remote_read_024"));
    var balance_warn = $("#balance_warn").val().trim();
    var original_balance_warn = $("#autochb").data("balance_warn");
    var valve_close = $("#valve_close").val().trim();
    var original_valve_close = $("#autochb").data("valve_close");
    var isAutoRead = $("#isAutoRead input:checked").val().trim();
    var original_isAutoRead = $("#autochb").data("isAutoRead");
    var isAutoInform = $("#isAutoInform input:checked").val().trim();
    var original_isAutoInform = $("#autochb").data("isAutoInform");
    jQuery.ajax({
        url: "../user/saveReadParameter.do",
        type: "post",
        data: {
            "parameter_id": "1",
            "day": day,
            "hour": hour,
            "minute": minute,
            "second": second,
            "balance_warn": balance_warn,
            "valve_close": valve_close,
            "isAutoRead": isAutoRead,
            "isAutoInform": isAutoInform,
            "original_day": original_day,
            "original_hour": original_hour,
            "original_minute": original_minute,
            "original_balance_warn": original_balance_warn,
            "original_valve_close": original_valve_close,
            "original_isAutoRead": original_isAutoRead,
            "original_isAutoInform": original_isAutoInform
        },
        dataType: "json",
        success: function (result) {
            if (result.status == 1) {
                alert("$remote_read_067");
            }
        },
        error: function () {
            alert("error");
        }
    });
}

//保存周期抄表参数
function saveReadParameter2() {
    //获取修改后的周期抄表参数
    var day = $("#day2").text().trim();
    //获取修改前的周期抄表参数
    var original_day = $("#intervalchb").data("day2");
    var hour = $("#hour2").text().trim();
    var original_hour = $("#intervalchb").data("hour2");
    var minute = $("#minute2").text().trim();
    var original_minute = $("#intervalchb").data("minute2");
    var second = "00";
    day = day.substring(0, day.indexOf("$remote_read_021"));
    hour = hour.substring(0, hour.indexOf("$remote_read_022"));
    minute = minute.substring(0, minute.indexOf("$remote_read_023"));
    //second = second.substring(0, second.indexOf("$remote_read_024"));
    var balance_warn = $("#balance_warn2").val().trim();
    var original_balance_warn = $("#intervalchb").data("balance_warn2");
    var valve_close = $("#valve_close2").val().trim();
    var original_valve_close = $("#intervalchb").data("valve_close2");
    var isAutoRead = $("#isAutoRead2 input:checked").val().trim();
    var original_isAutoRead = $("#intervalchb").data("isAutoRead2");
    var isAutoInform = $("#isAutoInform2 input:checked").val().trim();
    var original_isAutoInform = $("#intervalchb").data("isAutoInform2");
    jQuery.ajax({
        url: "../user/saveReadParameter.do",
        type: "post",
        data: {
            "parameter_id": "2",
            "day": day,
            "hour": hour,
            "minute": minute,
            "second": second,
            "balance_warn": balance_warn,
            "valve_close": valve_close,
            "isAutoRead": isAutoRead,
            "isAutoInform": isAutoInform,
            "original_day": original_day,
            "original_hour": original_hour,
            "original_minute": original_minute,
            "original_balance_warn": original_balance_warn,
            "original_valve_close": original_valve_close,
            "original_isAutoRead": original_isAutoRead,
            "original_isAutoInform": original_isAutoInform
        },
        dataType: "json",
        success: function (result) {
            if (result.status == 1) {
                alert("$remote_read_067");
            }
        },
        error: function () {
            alert("error");
        }
    });
}

function showReadParameterByTiming() {
    jQuery.ajax({
        url: "../user/showReadParameter.do",
        type: "post",
        dataType: "json",
        success: function (result) {
            if (result.status == 1) {
                var readParameters = result.data;
                if (readParameters == null) {
                    return;
                }
                for (var i = 0; i < readParameters.length; i++) {
                    var readParameter = readParameters[i];
                    var parameter_id = readParameter.parameter_id;
                    if (parameter_id === "1") {
                        //定时抄表设置
                        $("#day").text(readParameter.day + "$remote_read_021");
                        //将页面显示的定时抄表值绑定在autochb上，在向后端发送修改保存设置的请求时
                        //将该值一并带上，便于日志进行输出修改前后的值对比
                        $("#autochb").data("day", readParameter.day);
                        $("#hour").text(readParameter.hour + "$remote_read_022");
                        $("#autochb").data("hour", readParameter.hour);
                        $("#minute").text(readParameter.minute + "$remote_read_023");
                        $("#autochb").data("minute", readParameter.minute);
                        $("#balance_warn")[0].value = readParameter.balance_warn;
                        $("#autochb").data("balance_warn", readParameter.balance_warn);
                        $("#valve_close")[0].value = readParameter.valve_close;
                        $("#autochb").data("valve_close", readParameter.valve_close);
                        $("#autochb").data("isAutoRead", readParameter.isAutoRead);
                        $("#autochb").data("isAutoInform", readParameter.isAutoInform);
                        if (readParameter.isAutoRead === "0") {
                            $("#isAutoRead input[value='0']").attr("checked", true);
                        }
                        if (readParameter.isAutoInform === "0") {
                            $("#isAutoInform input[value='0']").attr("checked", true);
                        }
                    }
                }
            }
        },
        error: function () {
            alert("error");
        }
    });
}
function showReadParameterByCycle() {
    jQuery.ajax({
        url: "../user/showReadParameter.do",
        type: "post",
        dataType: "json",
        success: function (result) {
            if (result.status == 1) {
                var readParameters = result.data;
                if (readParameters == null) {
                    return;
                }
                for (var i = 0; i < readParameters.length; i++) {
                    var readParameter = readParameters[i];
                    var parameter_id = readParameter.parameter_id;
                    if (parameter_id === "2") {
                        //周期抄表设置
                        $("#day2").text(readParameter.day + "$remote_read_021");
                        $("#intervalchb").data("day2", readParameter.day);
                        $("#hour2").text(readParameter.hour + "$remote_read_022");
                        $("#intervalchb").data("hour2", readParameter.hour);
                        $("#minute2").text(readParameter.minute + "$remote_read_023");
                        $("#intervalchb").data("minute2", readParameter.minute);
                        $("#balance_warn2")[0].value = readParameter.balance_warn;
                        $("#intervalchb").data("balance_warn2", readParameter.balance_warn);
                        $("#valve_close2")[0].value = readParameter.valve_close;
                        $("#intervalchb").data("valve_close2", readParameter.valve_close);
                        $("#intervalchb").data("isAutoRead2", readParameter.isAutoRead);
                        $("#intervalchb").data("isAutoInform2", readParameter.isAutoInform);
                        if (readParameter.isAutoRead === "0") {
                            $("#isAutoRead2 input[value='0']").attr("checked", true);
                        }
                        if (readParameter.isAutoInform === "0") {
                            $("#isAutoInform2 input[value='0']").attr("checked", true);
                        }
                    }
                }
            }
        },
        error: function () {
            alert("error");
        }
    });
}
