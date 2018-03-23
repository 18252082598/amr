$(function () {
    $("#admin").text(account_name);
    $("#sum-to-do").text("(" + toDoNo + ")");
    $("#exit").click(logout);
    load_Sumtodo();
    userManagePermission();
    load_communities();
    findUser();
    $("#t1 tbody").on("click", "tr", loadUserManageInfo);
    loadUpDownForm();
    $("#regist_show").click(clear);
    $("#regist").click(regist);
    $("#findUser").click(findUserBefore);
    $(".span1").click(nextPage);
    $(".span2").click(prePage);
    $(".span3").click(firstPage);
    $(".span4").click(lastPage);
    $(".input-page").keypress(function (event) {
        var keycode = (event.keyCode ? event.keyCode : event.which);
        if (keycode == 13) {
            var pageIndex = $(this).val().trim();
            pageIndex = parseInt(pageIndex);
            var totalNo = parseInt($("#t1").data("totalNo"));
            var pageSize = $(".pageSize option:selected").val().trim();
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
    $("#all_select").click(all_select);
    //换表按钮
    $("#change_meter").click(change_meter);
    $("#change_footer").on("click", "#apply-change", apply_change);
    $("#change_footer").on("click", "#remove-meter2", remove_meter2);
    $("#change_footer").on("click", "#switch-meter", switch_meter);
    $("#change-meter").on("hidden.bs.modal", function () {
        window.location.href = "user-manage.html";
    });
    //更换费率按钮
    $("#change_fee_type").click(change_fee_type);
    $("#change_feeType_confirm").click(change_feeType_confirm);
    //销户按钮
    $("#delete_user").click(delete_user);
    $("#remove_footer").on("click", "#apply-remove", apply_remove);
    $("#remove_footer").on("click", "#remove-meter", remove_meter);
    $("#remove_footer").on("click", "#off-meter", off_meter);
    $("#t4").on("click", "button:contains('$user_manage_120')", saveAccount);
    $("#cancel-user").on("hidden.bs.modal", function () {
        window.location.href = "user-manage.html";
    });
    //修改按钮
    $("#modify_user").click(modify_user);
    $("#modify_user_confirm").click(modify_user_confirm);
    //删除按钮
    $("#remove_user").click(remove_user);
    $("#remove_user_confirm").click(remove_user_confirm);
    $("#downloadModel").click(download);
    $("#exportRoomInfo").click(exportRoomInfo);
});

var loadPage = function () {
    findUser();
};
function findUserBefore() {
    $(".input-page").val(1);
    findUser();
}
//加载用户
function findUser() {
    //按照表号查询用户
    var meter_no = ($("#meter_no").val().trim());
    if (isNaN(meter_no)) {
        alert("meter no format is wrong,please input correctly！");
        return;
    }
    $("#t1 tbody tr").remove();
    var pageNo = $(".input-page").val().trim();
    var pageSize = parseInt($(".pageSize option:selected").val().trim());
    var user_name = $("#user_name").val().trim();
    var community = $("#user_address_community option:selected").val().trim();
    var building = $("#user_address_building option:selected").val().trim();
    var unit = $("#user_address_unit option:selected").val().trim();
    var room = $("#user_address_room option:selected").val().trim();
    var contact_info = $("#contact_info").val().trim();
    var meter_no = $("#meter_no").val().trim();
    // 获取地址栏中的参数：“”或“换表”或“销户”
    var status = getQueryString("status");
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
            "operator_name": "",
            "contact_info": contact_info,
            "meter_no": meter_no,
            "status": status,
            "startNo": startNo,
            "pageSize": pageSize
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
                $(".span4").next().text(pageMax);
                $("#t1").data("totalNo", totalNo);
                for (var i = 0; i < users.length; i++) {
                    var index = startNo + (i + 1);
                    var user = users[i];
                    var user_id = user.user_id;
                    var user_name = user.user_name;
                    var province = user.province;
                    var city = user.city;
                    var district = user.district;
                    var user_address_area = user.user_address_area;
                    var user_address_community = user.user_address_community;
                    var user_address_building = user.user_address_building;
                    var user_address_unit = user.user_address_unit;
                    var user_address_room = user.user_address_room;
                    var contact_info = user.contact_info;
                    var supplier_name = user.supplier_name;
                    var id_card_no = user.id_card_no;
                    var house_area = user.house_area;
                    var coefficient_name = user.coefficient_name;
                    var concentrator_name = user.concentrator_name;
                    var meter_no = user.meter_no;
                    var meter_type = user.meter_type;
                    var initing_data = user.initing_data;
                    initing_data = initing_data === null ? 0 : initing_data;
                    var meter_status = user.meter_status;
                    var user_status = user.user_status;
                    var operator_account = user.operator_account;
                    var add_time = user.add_time;
                    add_time = format(add_time, 'yyyy.MM.dd HH:mm:ss');
                    var meter_type = user.meter_type;
                    if (meter_type === "10") {
                        meter_type = "$user_manage_061";
                    } else if (meter_type === "11") {
                        meter_type = "$user_manage_061_2";
                    } else if (meter_type === "20") {
                        meter_type = "$user_manage_062";
                    } else if (meter_type === "30") {
                        meter_type = "$user_manage_062_1";
                    } else if (meter_type === "40") {
                        meter_type = "$remote_read_047_2";
                    }
                    var meter_model = user.meter_model;
                    var protocol_type = user.protocol_type;
                    var valve_protocol = user.valve_protocol;
                    var submeter_no = user.submeter_no;
                    var valve_no = user.valve_no;
                    var last_balance = user.last_balance;
                    var last_read_time = user.last_read_time;
                    if (last_read_time == null) {
                        last_read_time = "";
                    }
                    var fee_type = user.fee_type;
                    $("#t1").data("meter_status" + index, meter_status);
                    $("#t1").data("user_status" + index, user_status);
                    var user_status_str = "";
                    if (user_status == "1") {
                        user_status_str = "$user_manage_102";
                    } else if (user_status == "0") {
                        user_status_str = "$user_manage_101";
                    }
                    var meter_status_str = "";
                    if (meter_status == "0") {
                        meter_status_str = "$user_manage_102_01";
                    } else if (meter_status == "1") {
                        meter_status_str = "$user_manage_102";
                    } else if (meter_status == "2") {
                        meter_status_str = "$user_manage_103";
                    } else if (meter_status == "3") {
                        meter_status_str = "$user_manage_054";
                    } else if (meter_status == "4") {
                        meter_status_str = "$user_manage_108";
                    } else if (meter_status == "5") {
                        meter_status_str = "$user_manage_116";
                    } else if (meter_status == "6") {
                        meter_status_str = "$user_manage_113";
                    }
                    var pay_type = user.pay_type;
                    var str = "";
                    str += '<tr><td><input type="checkbox" value="' + user_id + '"></td>';
                    str += '<td>' + index + '</td>';
                    str += '<td>' + user_name + '</td>';
                    str += '<td>' + user_address_community + '</td>';
                    str += '<td>' + user_address_building + '</td>';
                    str += '<td>' + user_address_unit + '</td>';
                    str += '<td>' + user_address_room + '</td>';
                    str += '<td>' + contact_info + '</td>';
                    str += '<td>' + id_card_no + '</td>';
                    str += '<td>' + concentrator_name + '</td>';
                    str += '<td>' + meter_no + '</td>';
                    str += '<td>' + meter_type + '</td>';
                    str += '<td>' + initing_data + '</td>';
                    str += '<td>' + operator_account + '</td>';
                    str += '<td>' + user_status_str + '</td>';
                    str += '<td>' + meter_status_str + '</td>';
                    str += '<td>' + add_time + '</td></tr>';
                    $("#t1 tbody").append($(str));
                    $("#t1").data("meter_no" + index, meter_no);
                    $("#t1").data("user_id" + index, user_id);
                    $("#t1").data("user_name" + index, user_name);
                    $("#t1").data("province" + index, province);
                    $("#t1").data("city" + index, city);
                    $("#t1").data("district" + index, district);
                    $("#t1").data("user_address_area" + index, user_address_area);
                    $("#t1").data("user_address_community" + index, user_address_community);
                    $("#t1").data("user_address_building" + index, user_address_building);
                    $("#t1").data("user_address_unit" + index, user_address_unit);
                    $("#t1").data("user_address_room" + index, user_address_room);
                    $("#t1").data("id_card_no" + index, id_card_no);
                    $("#t1").data("contact_info" + index, contact_info);
                    $("#t1").data("supplier_name" + index, supplier_name);
                    $("#t1").data("house_area" + index, house_area);
                    $("#t1").data("coefficient_name" + index, coefficient_name);
                    $("#t1").data("concentrator_name" + index, concentrator_name);
                    $("#t1").data("meter_type" + index, meter_type);
                    $("#t1").data("initing_data" + index, initing_data);
                    $("#t1").data("meter_model" + index, meter_model);
                    $("#t1").data("submeter_no" + index, submeter_no);
                    $("#t1").data("valve_no" + index, valve_no);
                    $("#t1").data("protocol_type" + index, protocol_type);
                    $("#t1").data("valve_protocol" + index, valve_protocol);
                    $("#t1").data("fee_type" + index, fee_type);
                    $("#t1").data("operator_account" + index, operator_account);
                    $("#t1").data("last_balance" + index, last_balance);
                    $("#t1").data("last_read_time" + index, last_read_time);
                    $("#t1").data("pay_type" + index, pay_type);
                }
                if ($("#t1 tbody tr").length == 0) {
                    $("#t2 tbody tr").remove();
                } else {
                    $("#t1 tbody tr:eq(0)").click();
                }
            }
        },
        error: function () {
            alert("error");
        }
    });
}

//加载操作记录
function loadUserManageInfo() {
    $("#t2 tbody tr").remove();
    // var meter_no=$(this).find("td:eq(12)").text().trim();
    var user_name = $(this).find("td:eq(2)").text().trim();
    jQuery.ajax({
        url: "../user/findUserManageInfo.do",
        type: "post",
        data: {
            "user_name": user_name
        },
        dataType: "json",
        success: function (result) {
            if (result.status == 1) {
                var userManageInfo = result.data;
                for (var i = 0; i < userManageInfo.length; i++) {
                    var info = userManageInfo[i];
                    var oparate_time = info.operateDate;
                    var str = "";
                    str += "<tr><td>" + oparate_time + "</td>";
                    str += "<td>" + info.loginName + "</td>";
                    str += "<td>" + info.operatingcontent + "</td></tr>";
                    $("#t2 tbody").append(str);
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

//全选或全不选
function all_select() {
    $('#t1 tbody :checkbox').prop("checked", $(this).prop("checked"));
}

//清空上一次注册信息
function clear() {
    $("#s1").text("");
    $("#s2").text("");
    $("#s3").text("");
    $("#s4").text("");
    $("#s5").text("");
    $("#s6").text("");
    $("#s7").text("");
    $("#s8").text("");
    $("#user-name2")[0].value = "";
    $("#user-room2")[0].value = "";
    $("#user-mobile2")[0].value = "";
    $("#user-cardID2")[0].value = "";
    $("#meter-cardID2")[0].value = "";
    $("#meter-subID2")[0].value = "0";
    $("#valve-cardID2")[0].value = "0";
    findFeeTypes();
}

//注册页面：加载小区选项列表
function findCommunities() {
    $("#community").empty();
    $("#community").append($("<option value=''>$user_manage_009</option>"));
    $("#building").empty();
    $("#building").append($("<option value=''>$user_manage_010</option>"));
    $("#unit").empty();
    $("#unit").append($("<option value=''>$user_manage_011</option>"));
    $("#mbus-name2").empty();
    $("#mbus-name2").append($("<option value=''>$user_manage_058</option>"));
    var province = $("#add1 option:selected").val().trim();
    var city = $("#add2 option:selected").val().trim();
    var district = $("#add3 option:selected").val().trim();
    jQuery.ajax({
        url: "../user/findAllCommunities.do",
        type: "post",
        data: {
            "province": province,
            "city": city,
            "district": district
        },
        dataType: "json",
        success: function (result) {
            if (result.status == 1) {
                var communities = result.data;
                for (var i = 0; i < communities.length; i++) {
                    var community_name = communities[i].community_name;
                    var str = "<option value='" + community_name + "'>" + community_name + "</option>";
                    $("#community").append($(str));
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

//注册页面：根据小区名称查找楼栋信息
function findBuildings() {
    var province = $("#add1 option:selected").val().trim();
    var city = $("#add2 option:selected").val().trim();
    var district = $("#add3 option:selected").val().trim();
    var community_name = $("#community option:selected").val().trim();
    if (community_name != "$user_manage_009") {
        jQuery.ajax({
            url: "../user/loadBuilding.do",
            type: "post",
            data: {
                "province": province,
                "city": city,
                "district": district,
                "community_name": community_name
            },
            dataType: "json",
            success: function (result) {
                if (result.status == 1) {
                    $("#building").empty();
                    $("#building").append($("<option value=''>$user_manage_010</option>"));
                    $("#unit").empty();
                    $("#unit").append($("<option value=''>$user_manage_011</option>"));
                    $("#mbus-name2").empty();
                    $("#mbus-name2").append($("<option value=''>$user_manage_058</option>"));
                    var buildings = result.data;
                    for (var i = 0; i < buildings.length; i++) {
                        var building_name = buildings[i].building_name;
                        var str = "<option value='" + building_name + "'>" + building_name + "</option>";
                        $("#building").append($(str));
                    }
                } else {
                    alert("$public_036");
                }
            },
            error: function () {
                alert("error");
            }
        });
    } else {
        $("#building").empty();
        $("#building").append($("<option value=''>$user_manage_010</option>"));
        $("#unit").empty();
        $("#unit").append($("<option value=''>$user_manage_011</option>"));
        $("#mbus-name2").empty();
        $("#mbus-name2").append($("<option value=''>$user_manage_058</option>"));
    }
}

//注册页面：根据小区和楼栋名称查找单元信息
function findUnits() {
    var province = $("#add1 option:selected").val().trim();
    var city = $("#add2 option:selected").val().trim();
    var district = $("#add3 option:selected").val().trim();
    var community_name = $("#community option:selected").val().trim();
    var building_name = $("#building option:selected").val().trim();
    if (building_name != "$user_manage_010") {
        jQuery.ajax({
            url: "../user/loadUnit.do",
            type: "post",
            data: {
                "province": province,
                "city": city,
                "district": district,
                "community_name": community_name,
                "building_name": building_name
            },
            dataType: "json",
            success: function (result) {
                if (result.status == 1) {
                    $("#unit").empty();
                    $("#unit").append($("<option value=''>$user_manage_011</option>"));
                    $("#mbus-name2").empty();
                    $("#mbus-name2").append($("<option value=''>$user_manage_058</option>"));
                    var units = result.data;
                    for (var i = 0; i < units.length; i++) {
                        var unit_name = units[i].unit_name;
                        var str = "<option value='" + unit_name + "'>" + unit_name + "</option>";
                        $("#unit").append($(str));
                    }
                } else {
                    alert("$public_036");
                }
            },
            error: function () {
                alert("error");
            }
        });
    } else {
        $("#unit").empty();
        $("#unit").append($("<option value=''>$public_011</option>"));
        $("#mbus-name2").empty();
        $("#mbus-name2").append($("<option value=''>$public_058</option>"));
    }
}

//注册页面：根据小区、楼栋和单元名称查找集中器名称
function findConcentrators() {
    var community_name = $("#community option:selected").val().trim();
    var building_name = $("#building option:selected").val().trim();
    var unit_name = $("#unit option:selected").val().trim();
    if (community_name !== "" && building_name !== "" && unit_name !== "") {
        jQuery.ajax({
            url: "../user/loadConcentrators.do",
            type: "post",
            data: {
                "community_name": community_name,
                "building_name": building_name,
                "unit_name": unit_name,
                "startNo": 0,
                "pageSize": 0
            },
            dataType: "json",
            success: function (result) {
                $("#mbus-name2").empty();
                $("#mbus-name2").append($("<option value=''>$user_manage_058</option>"));
                var map = result.data;
                var concentrators = map["concentrators"];
                for (var i = 0; i < concentrators.length; i++) {
                    var concentrator_name = concentrators[i].concentrator_name;
                    var str = "<option value='" + concentrator_name + "'>" + concentrator_name + "</option>";
                    $("#mbus-name2").append($(str));
                }
            },
            error: function () {
                alert("error");
            }
        });
    } else {
        $("#mbus-name2").empty();
        $("#mbus-name2").append($("<option value=''>$public_058</option>"));
    }
}

//注册页面：加载网点选项
function findSuppliers() {
    var province = $("#add1 option:selected").val().trim();
    var city = $("#add2 option:selected").val().trim();
    var district = $("#add3 option:selected").val().trim();
    $("#location-name2").empty();
    $("#location-name2").append($("<option value=''>$user_manage_060</option>"));
    jQuery.ajax({
        url: "../user/loadSuppliers.do",
        type: "post",
        data: {
            "province": province,
            "city": city,
            "district": district,
            "startNo": 0,
            "pageSize": 0
        },
        dataType: "json",
        success: function (result) {
            if (result.status == 1) {
                var map = result.data;
                var suppliers = map["suppliers"];
                for (var i = 0; i < suppliers.length; i++) {
                    var supplier_name = suppliers[i].supplier_name;
                    var str = "<option value='" + supplier_name + "'>" + supplier_name + "</option>";
                    $("#location-name2").append($(str));
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

//加载费率类型选项
function findFeeTypes() {
    $("#price-type2").empty();
    $("#price-type2").append($("<option value=''>$user_manage_06701</option>"));
    jQuery.ajax({
        url: "../user/loadFeeTypes.do",
        type: "post",
        dataType: "json",
        success: function (result) {
            if (result.status == 1) {
                var feeTypes = result.data;
                for (var i = 0; i < feeTypes.length; i++) {
                    var fee_type = feeTypes[i].feeTypeName;
                    var str = "<option value='" + fee_type + "'>" + fee_type + "</option>";
                    $("#price-type2").append($(str));
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

//加载户型系数选项
function findHouseCoefficients() {
    $("#house-coefficient2").empty();
    $("#house-coefficient2").append($("<option value=''>$user_manage_096</option>"));
    jQuery.ajax({
        url: "../user/load_house_coefficient.do",
        type: "post",
        dataType: "json",
        success: function (result) {
            if (result.status == 1) {
                var coes = result.data;
                for (var i = 0; i < coes.length; i++) {
                    var coefficient_name = coes[i].coefficient_name;
                    var str = "<option value='" + coefficient_name + "'>" + coefficient_name + "</option>";
                    $("#house-coefficient2").append($(str));
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

function loadInfo() {
    findCommunities();
    findSuppliers();
}

//注册用户
function regist() {
    var user_name = $("#user-name2").val().trim();
    var province = $("#d2 select:eq(0) option:selected").val().trim();
    var city = $("#d2 select:eq(1) option:selected").val().trim();
    var district = $("#d2 select:eq(2) option:selected").val().trim();
    var user_address_area = province + city + district;
    var user_address_community = $("#d2 select:eq(3) option:selected").val().trim();
    var user_address_building = $("#d2 select:eq(4) option:selected").val().trim();
    var user_address_unit = $("#d2 select:eq(5) option:selected").val().trim();
    var user_address_room = $("#user-room2").val().trim();
    var user_address_original_room = $("#user-room2").val().trim();
    var concentrator_name = $("#mbus-name2 option:selected").val().trim();
    var supplier_name = $("#location-name2 option:selected").val().trim();
    var contact_info = $("#user-mobile2").val().trim();
    var id_card_no = $("#user-cardID2").val().trim();
    var meter_type = $("#meter-type2 option:selected").val().trim();
    var meter_model = $("#meter-no2 option:selected").val().trim();
    var protocol_type = $("#meter-protocol2 option:selected").val().trim();
    var valve_protocol = $("#valve-protocol2 option:selected").val().trim();
    var fee_type = $("#price-type2 option:selected").val().trim();
    var meter_no = $("#meter-cardID2").val().trim();
    var submeter_no = $("#meter-subID2").val().trim();
    var valve_no = $("#valve-cardID2").val().trim();
    var pay_type = $("#pay_type2 option:selected").val().trim();
    var operator_account = getCookie("account_name");
    // 非空检查、格式检查
    var check = true;
    var numError = $('#tab-register form .onError').length;
    if (numError > 0
            || user_name === ""
            || user_address_community === ""
            || user_address_building === ""
            || user_address_unit === ""
            || user_address_room === ""
            || concentrator_name === ""
            || meter_type === ""
            || meter_model === ""
            || protocol_type === ""
            ) {
        alert("$user_add_024");
        check = false;
        return;
    }
    if (check) {
        jQuery.ajax({
            url: "../user/regist.do",
            type: "post",
            data: {
                "user_name": user_name,
                "province": province,
                "city": city,
                "district": district,
                "user_address_area": user_address_area,
                "user_address_community": user_address_community,
                "user_address_building": user_address_building,
                "user_address_unit": user_address_unit,
                "user_address_room": user_address_room,
                "user_address_original_room": user_address_original_room,
                "concentrator_name": concentrator_name,
                "supplier_name": supplier_name,
                "contact_info": contact_info,
                "id_card_no": id_card_no,
                "house_area": 0,
                "coefficient_name": "",
                "meter_type": meter_type,
                "meter_model": meter_model,
                "protocol_type": protocol_type,
                "valve_protocol": valve_protocol,
                "fee_type": fee_type,
                "meter_no": meter_no,
                "submeter_no": submeter_no,
                "valve_no": valve_no,
                "auto_deduction": "1",
                "meter_status": "1",
                "user_status": "1",
                "operator_account": operator_account,
                "pay_type": pay_type
            },
            dataType: "json",
            success: function (result) {
                if (result.status == 1) {
                    alert("$public_037");
                    $('#tab-register').modal('hide');
                    window.location.href = "user-manage.html";
                } else if (result.status === 1062) {
                    alert("$user_manage_141");
                } else {
                    alert("$public_036");
                }
            },
            error: function () {
                alert("error");
            }
        });
    }
}

//用户管理页面：换表按钮
function change_meter() {
    var arr = new Array();
    var user_status = "";
    var meter_status = "";
    var meter_no;
    $("#t1 tbody :checked").each(function () {
        var index = $(this).parents("tr").find("td:eq(1)").text().trim();
        $("#t3").data("index", index);
        var user_name = $(this).parents("tr").find("td:eq(2)").text().trim();
        meter_no = $(this).parents("tr").find("td:eq(10)").text().trim();
        var meter_type = $("#t1").data("meter_type" + index);
        if (meter_type === "10") {
            meter_type = "$user_manage_061";
        } else if (meter_type === "20") {
            meter_type = "$user_manage_062";
        } else if (meter_type === "30") {
            meter_type = "$user_manage_062_1";
        }
        var meter_model = $("#t1").data("meter_model" + index);
        var user_address_area = $("#t1").data("user_address_area" + index);
        var user_address_community = $(this).parents("tr").find("td:eq(3)").text().trim();
        var user_address_building = $(this).parents("tr").find("td:eq(4)").text().trim();
        var user_address_unit = $(this).parents("tr").find("td:eq(5)").text().trim();
        var user_address_room = $(this).parents("tr").find("td:eq(6)").text().trim();
        var user_address = user_address_area
                + user_address_community
                + user_address_building + user_address_unit
                + user_address_room;
        var contact_info = $(this).parents("tr").find("td:eq(7)").text().trim();
        user_status = $("#t1").data("user_status" + index);
        meter_status = $("#t1").data("meter_status" + index);
        var last_balance = $("#t1").data("last_balance" + index);
        var last_read_time = $("#t1").data("last_read_time" + index);
        if (last_read_time !== "") {
            last_read_time = format(last_read_time, 'yyyy.MM.dd HH:mm:ss');
        }
        $("#t3 tr:eq(0) td:eq(1)").text(user_name);
        $("#t3 tr:eq(1) td:eq(1)").text(meter_type);
        $("#t3 tr:eq(2) td:eq(1)").text(meter_no);
        $("#t3 tr:eq(3) td:eq(1)").text(meter_model);
        $("#t3 tr:eq(4) td:eq(1)").text(user_address);
        $("#t3 tr:eq(5) td:eq(1)").text(contact_info);
        $("#t3 tr:eq(6) td:eq(1)").text(last_balance + "$user_manage_097");
        $("#t3 tr:eq(7) td:eq(1)").text(last_read_time);
        arr.push(user_name);
    });

    if (arr.length == 0) {
        alert("$user_manage_098");
    } else if (arr.length > 1) {
        alert("$user_manage_099");
    } else if (arr.length == 1 && user_status == "0") {
        alert("$user_manage_100");
    } else if (arr.length == 1 && user_status == "1") {
        $("#change-meter").modal('show');
        // 检查用户的换表状态，根据不同的换表状态，改变按钮的样式
        var apply_time;
        var remove_time;
        var change_time;
        var pre_meter_no;
        if (meter_status != "1") {
            jQuery.ajax({
                url: "../user/findOperateTime.do",
                type: "post",
                async: false,
                data: {
                    "meter_no": meter_no
                },
                dataType: "json",
                success: function (result) {
                    if (result.status == 1) {
                        var info = result.data;
                        for (var i = 0; i < info.length; i++) {
                            var msg = info[i].info_content;
                            if (msg == "1") {
                                apply_time = info[i].operate_time;
                            } else if (msg == "2") {
                                remove_time = info[i].operate_time;
                            } else if (msg == "3") {
                                change_time = info[i].operate_time;
                                pre_meter_no = info[i].meter_no;
                            }
                        }
                    } else {
                        alert("$public_36");
                    }
                },
                error: function () {
                    alert("error");
                }
            });
        }

        if (meter_status == "1") {
            $("#change_footer").empty();
            var str = "";
            str += "<button id='apply-change' type='button' class='button blue'>$user_manage_053</button>";
            str += "<button type='button' class='button grey' style='cursor:auto'>$user_manage_054</button>";
            str += "<button type='button' class='button grey' style='cursor:auto'>$user_manage_055</button>";
            $("#change_footer").append($(str));
        } else if (meter_status == "2") {
            apply_time = format(apply_time, 'yyyy.MM.dd HH:mm:ss');
            $("#t3 tbody").append("<tr><td>$user_manage_107</td><td>" + apply_time + "</td></tr>");
            $("#change_footer").empty();
            var str = "";
            str += "<span class='glyphicon glyphicon-ok'>$user_manage_053</span>";
            str += "<button id='remove-meter2' type='button' class='button blue'>$user_manage_054</button>";
            str += "<button type='button' class='button grey' style='cursor:auto'>$user_manage_055</button>";
            $("#change_footer").append($(str));
        } else if (meter_status == "3") {
            apply_time = format(apply_time, 'yyyy.MM.dd HH:mm:ss');
            remove_time = format(remove_time, 'yyyy.MM.dd HH:mm:ss');
            $("#t3 tbody").append(
                    "<tr><td>$user_manage_105</td><td>" + apply_time + "</td></tr>");
            $("#t3 tbody").append(
                    "<tr><td>$user_manage_106</td><td>" + remove_time + "</td></tr>");
            $("#t3 tbody").append(
                    "<tr><td>$user_manage_107</td><td><input value=''></td></tr>");
            $("#t3 tbody input").focus();
            $("#change_footer").empty();
            var str = "";
            str += "<span class='glyphicon glyphicon-ok'>$user_manage_053</span>";
            str += "<span class='glyphicon glyphicon-ok'>$user_manage_054</span>";
            str += "<button id='switch-meter' type='button' class='button blue'>$user_manage_055</button>";
            $("#change_footer").append($(str));
        } else if (meter_status == "4") {
            apply_time = format(apply_time, 'yyyy.MM.dd HH:mm:ss');
            remove_time = format(remove_time, 'yyyy.MM.dd HH:mm:ss');
            change_time = format(change_time, 'yyyy.MM.dd HH:mm:ss');
            $("#t3 tbody").append("<tr><td>$user_manage_105</td><td>" + apply_time + "</td></tr>");
            $("#t3 tbody").append("<tr><td>$user_manage_106</td><td>" + remove_time + "</td></tr>");
            $("#t3 tbody").append("<tr><td>$user_manage_107</td><td><input value=''></td></tr>");
            $("#t3 tbody").append("<tr><td>$user_manage_109</td><td>" + change_time + "</td></tr>");
            $("#change_footer").empty();
            var str = "";
            str += "<span class='glyphicon glyphicon-ok'>$user_manage_053</span>";
            str += "<span class='glyphicon glyphicon-ok'>$user_manage_054</span>";
            str += "<span class='glyphicon glyphicon-ok'>$user_manage_108</span>";
            $("#change_footer").append($(str));
        }
    }
}

//添加换表申请记录
function apply_change() {
    var index = $("#t3").data("index");
    var user_name = $("#t3 tbody").find("tr:eq(0) td:eq(1)").text().trim();
    var meter_no = $("#t3 tbody").find("tr:eq(2) td:eq(1)").text().trim();
    var user_address = $("#t3 tbody").find("tr:eq(4) td:eq(1)").text().trim();
    var contact_info = $("#t3 tbody").find("tr:eq(5) td:eq(1)").text().trim();
    var operator_account = getCookie("account_name");
    var province = $("#t1").data("province" + index);
    var city = $("#t1").data("city" + index);
    var district = $("#t1").data("district" + index);
    jQuery.ajax({
        url: "../user/applyChangeMeter.do",
        type: "post",
        data: {
            "user_name": user_name,
            "meter_no": meter_no,
            "province": province,
            "city": city,
            "district": district,
            "user_address": user_address,
            "info_content": "1",
            "contact_info": contact_info,
            "operator_account": operator_account
        },
        dataType: "json",
        success: function (result) {
            if (result.status == 1) {
                $("#hint-3").fadeIn();
                $("#hint-3").fadeOut(3000);
                setTimeout(function () {
                    $("#apply-change").replaceWith("<span class='glyphicon glyphicon-ok'>$user_manage_053</span>");
                    $("#change_footer button:eq(0)").replaceWith("<button id='remove-meter2' type='button' class='button blue'>$user_manage_054</button>");
                    $("#t1 tbody :checked").parents("tr").find("td:eq(15)").text("$user_manage_103");
                    $("#t3 tbody").append("<tr><td>$user_manage_105</td><td>" + getTime() + "</td></tr>");
                }, 3000);
            } else {
                alert("$public_036");
            }
        },
        error: function () {
            alert("error");
        }
    });
}

//添加拆表申请记录
function remove_meter2() {
    var index = $("#t3").data("index");
    var user_name = $("#t3 tbody").find("tr:eq(0) td:eq(1)").text().trim();
    var meter_no = $("#t3 tbody").find("tr:eq(2) td:eq(1)").text().trim();
    var user_address = $("#t3 tbody").find("tr:eq(4) td:eq(1)").text().trim();
    var contact_info = $("#t3 tbody").find("tr:eq(5) td:eq(1)").text().trim();
    var operator_account = getCookie("account_name");
    var province = $("#t1").data("province" + index);
    var city = $("#t1").data("city" + index);
    var district = $("#t1").data("district" + index);
    jQuery.ajax({
        url: "../user/removeMeter.do",
        type: "post",
        data: {
            "user_name": user_name,
            "meter_no": meter_no,
            "province": province,
            "city": city,
            "district": district,
            "user_address": user_address,
            "info_content": "2",
            "contact_info": contact_info,
            "operator_account": operator_account
        },
        dataType: "json",
        success: function (result) {
            if (result.status == 1) {
                $("#hint-4").fadeIn();
                $("#hint-4").fadeOut(3000);
                setTimeout(function () {
                    $("#remove-meter2").replaceWith("<span class='glyphicon glyphicon-ok'>$user_manage_054</span>");
                    $("#change_footer button:eq(0)").replaceWith("<button id='switch-meter' type='button' class='button blue'>$user_manage_055</button>");
                    $("#t1 tbody :checked").parents("tr").find("td:eq(15)").text("$user_manage_054");
                    $("#t3 tbody").append("<tr><td>$user_manage_106</td><td>" + getTime() + "</td></tr>");
                    $("#t3 tbody").append("<tr><td>$user_manage_107</td><td><input value=''></td></tr>");
                    $('#t3 tbody input').focus();
                }, 3000);
            } else {
                alert("$public_036");
            }
        },
        error: function () {
            alert("error");
        }
    });
}

//换表
function switch_meter() {
    var index = $("#t3").data("index");
    var user_name = $("#t3 tbody").find("tr:eq(0) td:eq(1)").text().trim();
    var meter_no = $("#t3 tbody").find("tr:eq(2) td:eq(1)").text().trim();
    var province = $("#t1").data("province" + index);
    var city = $("#t1").data("city" + index);
    var district = $("#t1").data("district" + index);
    var user_address = $("#t3 tbody").find("tr:eq(4) td:eq(1)").text().trim();
    var contact_info = $("#t3 tbody").find("tr:eq(5) td:eq(1)").text().trim();
    var last_balance = $("#t3 tbody").find("tr:eq(6) td:eq(1)").text().trim();
    var new_meter_no = $("#t3 tbody").find("tr:eq(-1) td:eq(1) input").val().trim();
    var operator_account = getCookie("account_name");
    var check = false;
    if (new_meter_no == "" || isNaN(new_meter_no)) {
        alert("$user_manage_110");
    } else {
        check = true;
    }
    if (check && confirm("$user_manage_111")) {
        jQuery.ajax({
            url: "../user/changeMeter.do",
            type: "post",
            data: {
                "user_name": user_name,
                "meter_no": meter_no,
                "province": province,
                "city": city,
                "district": district,
                "user_address": user_address,
                "info_content": "3",
                "contact_info": contact_info,
                "new_meter_no": new_meter_no,
                "operator_account": operator_account,
                "original_meter_no": meter_no
            },
            dataType: "json",
            success: function (result) {
                if (result.status === 1) {
                    $("#switch-meter").replaceWith("<span class='glyphicon glyphicon-ok'>$user_manage_108</span>");
                    $("#t1 tbody :checked").parents("tr").find("td:eq(15)").text("$user_manage_108");
                    $("#t1 tbody :checked").parents("tr").find("td:eq(12)").text(new_meter_no);
                    $("#t3 tbody tr:eq(-1) td:eq(1)").html(new_meter_no);
                    $("#t3 tbody").append("<tr><td>$user_manage_109</td><td>" + getTime() + "</td></tr>");
                } else if (result.status === 1062) {
                    alert("$user_manage_141");
                } else {
                    alert("$public_036");
                }
            },
            error: function () {
                alert("error");
            }
        });
    }
}

//更换费率类型按钮
function change_fee_type() {
    var userNo = $("#t1 tbody :checked").length;
    var check = false;
    if (userNo == 0) {
        alert("$user_manage_098");
    } else if (userNo > 1) {
        alert("$user_manage_099");
    } else if (userNo == 1) {
        $("#change-price").modal('show');
        check = true;
    }
    if (check) {
        //var meter_no = $("#t1 tbody :checked").parents("tr").find("td:eq(10)").text().trim();
        var index = $("#t1 tbody :checked").parents("tr").find("td:eq(1)").text().trim();
        var feeType = $("#t1").data("fee_type" + index);
        jQuery.ajax({
            url: "../user/loadFeeTypes.do",
            type: "post",
            dataType: "json",
            success: function (result) {
                if (result.status == 1) {
                    $("#price-type3").empty();
                    var feeTypes = result.data;
                    for (var i = 0; i < feeTypes.length; i++) {
                        var fee_type = feeTypes[i].feeTypeName;
                        $("#price-type3").append("<option value='" + fee_type + "'>" + fee_type + "</option>");
                    }
                    $("#price-type3").find("option[value='" + feeType + "']").attr("selected", true);
                } else {
                    alert("$public_036");
                }
            },
            error: function () {
                alert("error");
            }
        });
    }
}

//更换费率类型确认
function change_feeType_confirm() {
    var index = $("#t1 tbody :checked").parents("tr").find("td:eq(1)").text().trim();
    //var meter_no = $("#t1 tbody :checked").parents("tr").find("td:eq(10)").text().trim();
    var user_name = $("#t1 tbody :checked").parents("tr").find("td:eq(2)").text().trim();
    var id_card_no = $("#t1 tbody :checked").parents("tr").find("td:eq(8)").text().trim();
    var original_fee_type = $("#t1").data("fee_type" + index);
    var fee_type = $("#price-type3 option:selected").val().trim();
    jQuery.ajax({
        url: "../user/changeFeeType.do",
        type: "post",
        data: {
            "original_fee_type": original_fee_type,
            "user_name": user_name,
            "id_card_no": id_card_no,
            "fee_type": fee_type
        },
        dataType: "json",
        success: function (result) {
            if (result.status == 1) {
                $("#close_changeFeeType").click();
                alert("$user_manage_137");
                findUser();
            } else {
                alert("$public_036");
            }
        },
        error: function () {
            alert("error");
        }
    });
}

//销户按钮
function delete_user() {
    var arr = new Array();
    var user_status = "";
    var meter_status = "";
    var meter_no;
    $("#t1 tbody :checked").each(function () {
        var index = $(this).parents("tr").find("td:eq(1)").text().trim();
        $("#t4").data("index", index);
        var user_name = $(this).parents("tr").find("td:eq(2)").text().trim();
        meter_no = $(this).parents("tr").find("td:eq(10)").text().trim();
        var meter_type = $("#t1").data("meter_type" + index);
        if (meter_type === "10") {
            meter_type = "$user_manage_061";
        } else if (meter_type === "20") {
            meter_type = "$user_manage_062";
        } else if (meter_type === "30") {
            meter_type = "$user_manage_062_1";
        }
        var meter_model = $("#t1").data("meter_model" + index);
        var user_address_area = $("#t1").data("user_address_area" + index);
        var user_address_community = $(this).parents("tr").find("td:eq(3)").text().trim();
        var user_address_building = $(this).parents("tr").find("td:eq(4)").text().trim();
        var user_address_unit = $(this).parents("tr").find("td:eq(5)").text().trim();
        var user_address_room = $(this).parents("tr").find("td:eq(6)").text().trim();
        var user_address = user_address_area
                + user_address_community
                + user_address_building + user_address_unit
                + user_address_room;
        var contact_info = $(this).parents("tr").find("td:eq(7)").text().trim();
        user_status = $("#t1").data("user_status" + index);
        meter_status = $("#t1").data("meter_status" + index);
        var last_balance = $("#t1").data("last_balance" + index);
        var last_read_time = $("#t1").data("last_read_time" + index);
        if (last_read_time !== "") {
            last_read_time = format(last_read_time, 'yyyy.MM.dd HH:mm:ss');
        }
        arr.push(user_name);
        $("#t4 tr:eq(0) td:eq(1)").text(user_name);
        $("#t4 tr:eq(1) td:eq(1)").text(meter_type);
        $("#t4 tr:eq(2) td:eq(1)").text(meter_no);
        $("#t4 tr:eq(3) td:eq(1)").text(meter_model);
        $("#t4 tr:eq(4) td:eq(1)").text(user_address);
        $("#t4 tr:eq(5) td:eq(1)").text(contact_info);
        $("#t4 tr:eq(6) td:eq(1)").text(last_balance.toFixed(2) + "$user_manage_097");
        $("#t4 tr:eq(7) td:eq(1)").text(last_read_time);
    });
    var check = true;
    if (arr.length == 1 && user_status == "1"
            && (meter_status == "2" || meter_status == "3" || meter_status == "4")) {
        check = false;
        alert("$user_manage_112");
    }
    if (arr.length == 0) {
        alert("$user_manage_098");
    } else if (arr.length > 1) {
        alert("$user_manage_099");
    } else if (arr.length == 1 && check == true) {
        $("#cancel-user").modal('show');
        var apply_time;
        var remove_time;
        var closing_time;
        var del_time;
        var info_content = "";
        if (meter_status != "1") {
            jQuery.ajax({
                url: "../user/findOperateTime.do",
                type: "post",
                async: false,
                data: {
                    "meter_no": meter_no
                },
                dataType: "json",
                success: function (result) {
                    if (result.status == 1) {
                        var info = result.data;
                        for (var i = 0; i < info.length; i++) {
                            var msg = info[i].info_content;
                            if (msg == "4") {
                                apply_time = info[i].operate_time;
                            } else if (msg == "5") {
                                remove_time = info[i].operate_time;
                            } else if (msg == "7") {
                                info_content = "7";
                                closing_time = info[i].operate_time;
                            } else if (msg == "6") {
                                del_time = info[i].operate_time;
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
        if (meter_status == "1") {
            $("#remove_footer").empty();
            var str = "";
            str += "<button id='apply-remove' type='button' class='button blue'>$user_manage_084</button>";
            str += "<button type='button' class='button grey' style='cursor:auto'>$user_manage_089</button>";
            str += "<button type='button' class='button grey' style='cursor:auto'>$user_manage_076</button>";
            $("#remove_footer").append($(str));
        } else if (meter_status == "6") {
            apply_time = format(apply_time, 'yyyy.MM.dd HH:mm:ss');
            $("#t4 tbody").append("<tr><td>$user_manage_105</td><td>" + apply_time + "</td></tr>");
            $("#remove_footer").empty();
            var str = "";
            str += "<span class='glyphicon glyphicon-ok'>$user_manage_088</span>";
            str += "<button id='remove-meter' type='button' class='button blue'>$user_manage_089</button>";
            str += "<button type='button' class='button grey' style='cursor:auto'>$user_manage_076</button>";
            $("#remove_footer").append($(str));
        } else if (meter_status == "3") {
            apply_time = format(apply_time, 'yyyy.MM.dd HH:mm:ss');
            remove_time = format(remove_time, 'yyyy.MM.dd HH:mm:ss');
            $("#t4 tbody").append("<tr><td>$user_manage_105</td><td>" + apply_time + "</td></tr>");
            $("#t4 tbody").append("<tr><td>$user_manage_106</td><td>" + remove_time + "</td></tr>");
            if (info_content == "") {
                var last_balance = $("#t4 tr:eq(6) td:eq(1)").text().trim();
                var hint_msg = parseFloat(last_balance) >= 0 ? "$user_manage_117" : "$user_manage_118";
                $('#t4').append(
                        '<tr><td>$user_manage_119</td><td><input placeholder="' + hint_msg
                        + '">&nbsp;<button>$user_manage_120</button></td></tr>');
                $('#t4 input').focus();
            } else if (info_content == "7") {
                closing_time = format(closing_time, 'yyyy.MM.dd HH:mm:ss');
                $("#t4 tbody").append("<tr><td>$user_manage_121</td><td>" + closing_time + "</td></tr>");
            }
            $("#remove_footer").empty();
            var str = "";
            str += "<span class='glyphicon glyphicon-ok'>$user_manage_084</span>";
            str += "<span class='glyphicon glyphicon-ok'>$user_manage_085</span>";
            str += "<button id='off-meter' type='button' class='button blue'>$user_manage_101</button>";
            $("#remove_footer").append($(str));
        } else if (meter_status == "5") {
            apply_time = format(apply_time, 'yyyy.MM.dd HH:mm:ss');
            remove_time = format(remove_time, 'yyyy.MM.dd HH:mm:ss');
            closing_time = format(closing_time, 'yyyy.MM.dd HH:mm:ss');
            del_time = format(del_time, 'yyyy.MM.dd HH:mm:ss');
            $("#t4 tbody").append("<tr><td>$user_manage_105</td><td>" + apply_time + "</td></tr>");
            $("#t4 tbody").append("<tr><td>$user_manage_106</td><td>" + remove_time + "</td></tr>");
            $("#t4 tbody").append("<tr><td>$user_manage_121</td><td>" + closing_time + "</td></tr>");
            $("#t4 tbody").append("<tr><td>$user_manage_122</td><td>" + del_time + "</td></tr>");
            $("#remove_footer").empty();
            var str = "";
            str += "<span class='glyphicon glyphicon-ok'>$user_manage_088</span>";
            str += "<span class='glyphicon glyphicon-ok'>$user_manage_085</span>";
            str += "<span class='glyphicon glyphicon-ok'>$user_manage_116</span>";
            $("#remove_footer").append($(str));
        }
    }
}

//申请拆表
function apply_remove() {
    var user_name = $("#t4 tbody").find("tr:eq(0) td:eq(1)").text().trim();
    var meter_no = $("#t4 tbody").find("tr:eq(2) td:eq(1)").text().trim();
    var index = $("#t4").data("index");
    var province = $("#t1").data("province" + index);
    var city = $("#t1").data("city" + index);
    var district = $("#t1").data("district" + index);
    var user_address = $("#t4 tbody").find("tr:eq(4) td:eq(1)").text().trim();
    var contact_info = $("#t4 tbody").find("tr:eq(5) td:eq(1)").text().trim();
    var operator_account = getCookie("account_name");
    jQuery.ajax({
        url: "../user/applyRemoveMeter.do",
        type: "post",
        data: {
            "user_name": user_name,
            "meter_no": meter_no,
            "province": province,
            "city": city,
            "district": district,
            "user_address": user_address,
            "info_content": "4",
            "contact_info": contact_info,
            "operator_account": operator_account
        },
        dataType: "json",
        success: function (result) {
            if (result.status == 1) {
                $("#hint-1").fadeIn();
                $("#hint-1").fadeOut(3000);
                setTimeout(function () {
                    $("#apply-remove").replaceWith("<span class='glyphicon glyphicon-ok'>$user_manage_088</span>");
                    $("#remove_footer button:eq(0)").replaceWith("<button id='remove-meter' type='button' class='button blue'>$user_manage_089</button>");
                    $("#t1 tbody :checked").parents("tr").find("td:eq(14)").text("$user_manage_101");
                    $("#t1 tbody :checked").parents("tr").find("td:eq(15)").text("$user_manage_113");
                    $('#t4').append('<tr><td>$user_manage_105</td><td>' + getTime() + '</td></tr>');
                }, 3000);
            } else {
                alert("$public_036");
            }
        },
        error: function () {
            alert("error");
        }
    });
}

//拆表
function remove_meter() {
    var user_name = $("#t4 tbody").find("tr:eq(0) td:eq(1)").text().trim();
    var meter_no = $("#t4 tbody").find("tr:eq(2) td:eq(1)").text().trim();
    var index = $("#t4").data("index");
    var province = $("#t1").data("province" + index);
    var city = $("#t1").data("city" + index);
    var district = $("#t1").data("district" + index);
    var user_address = $("#t4 tbody").find("tr:eq(4) td:eq(1)").text().trim();
    var contact_info = $("#t4 tbody").find("tr:eq(5) td:eq(1)").text().trim();
    var last_balance = $("#t4 tbody").find("tr:eq(6) td:eq(1)").text().trim();
    var hint_msg = parseFloat(last_balance) >= 0 ? "$user_manage_117" : "$user_manage_118";
    var operator_account = getCookie("account_name");
    jQuery.ajax({
        url: "../user/delMeter.do",
        type: "post",
        data: {
            "user_name": user_name,
            "meter_no": meter_no,
            "province": province,
            "city": city,
            "district": district,
            "user_address": user_address,
            "info_content": "5",
            "contact_info": contact_info,
            "operator_account": operator_account
        },
        dataType: "json",
        success: function (result) {
            if (result.status == 1) {
                $("#hint-2").fadeIn();
                $("#hint-2").fadeOut(3000);
                setTimeout(function () {
                    $("#remove-meter").replaceWith("<span class='glyphicon glyphicon-ok'>$user_manage_089</span>");
                    $("#remove_footer button:eq(0)").replaceWith("<button id='off-meter' type='button' class='button blue'>$user_manage_101</button>");
                    $("#t1 tbody :checked").parents("tr").find("td:eq(13)").text("$user_manage_089");
                    $('#t4').append('<tr><td>$user_manage_123</td><td>' + getTime() + '</td></tr>');
                    $('#t4').append(
                            '<tr><td>$user_manage_119</td><td><input placeholder="' + hint_msg
                            + '">&nbsp;<button>$user_manage_120</button></td></tr>');
                    $('#t4 input').focus();
                }, 3000);
            } else {
                alert("$public_036");
            }
        },
        error: function () {
            alert("error");
        }
    });
}

//拆表结算
function saveAccount() {
    var user_name = $("#t4 tbody").find("tr:eq(0) td:eq(1)").text().trim();
    var meter_no = $("#t4 tbody").find("tr:eq(2) td:eq(1)").text().trim();
    var index = $("#t4").data("index");
    var province = $("#t1").data("province" + index);
    var city = $("#t1").data("city" + index);
    var district = $("#t1").data("district" + index);
    var user_address = $("#t4 tbody").find("tr:eq(4) td:eq(1)").text().trim();
    var contact_info = $("#t4 tbody").find("tr:eq(5) td:eq(1)").text().trim();
    var last_balance = $("#t4 tbody").find("tr:eq(6) td:eq(1)").text().trim();
    last_balance = parseFloat(last_balance.substring(0, last_balance.indexOf("$user_manage_097")));
    var closingCost = parseFloat($(this).prev().val().trim());
    var operator_account = getCookie("account_name");
    var operate_time = getTime();
    var check = true;
    if (closingCost == "" || isNaN(closingCost)
            || (last_balance >= 0 && last_balance - closingCost < 0)
            || (last_balance < 0 && last_balance + closingCost < 0)) {
        check = false;
        alert("$user_manage_124");
    }
    if (check && confirm("$user_manage_125")) {
        jQuery.ajax({
            url: "../user/saveAccount.do",
            type: "post",
            data: {
                "user_name": user_name,
                "province": province,
                "city": city,
                "district": district,
                "user_address": user_address,
                "contact_info": contact_info,
                "meter_no": meter_no,
                "info_content": "7",
                "closingCost": closingCost,
                "operator_account": operator_account
            },
            dataType: "json",
            success: function (result) {
                if (result.status == 1) {
                    var new_account = last_balance >= 0 ? last_balance
                            - closingCost : last_balance + closingCost;
                    $("#t4 tbody tr:eq(6) td:eq(1)").text(new_account + "$user_manage_097");
                    $("#t4 tr:last").replaceWith(
                            '<tr><td>$user_manage_121</td><td>' + operate_time
                            + '</td></tr>');
                } else {
                    alert("$public_036");
                }
            },
            error: function () {
                alert("error");
            }
        });
    }
}

//已拆表
function off_meter() {
    if ($("#t4 tr:last td:eq(0)").text().trim() == "$user_manage_119") {
        alert("$user_manage_126");
        return;
    }
    var user_name = $("#t4 tbody").find("tr:eq(0) td:eq(1)").text().trim();
    var meter_no = $("#t4 tbody").find("tr:eq(2) td:eq(1)").text().trim();
    var index = $("#t4").data("index");
    var province = $("#t1").data("province" + index);
    var city = $("#t1").data("city" + index);
    var district = $("#t1").data("district" + index);
    var user_address = $("#t4 tbody").find("tr:eq(4) td:eq(1)").text().trim();
    var contact_info = $("#t4 tbody").find("tr:eq(5) td:eq(1)").text().trim();
    var operator_account = getCookie("account_name");
    if (confirm("$user_manage_090")) {
        if ($("#t4 tr:last td:eq(0)").text().trim() == "$user_manage_121") {
            jQuery.ajax({
                url: "../user/delUser.do",
                type: "post",
                data: {
                    "user_name": user_name,
                    "meter_no": meter_no,
                    "province": province,
                    "city": city,
                    "district": district,
                    "user_address": user_address,
                    "info_content": "6",
                    "contact_info": contact_info,
                    "operator_account": operator_account
                },
                dataType: "json",
                success: function (result) {
                    if (result.status == 1) {
                        alert("$user_manage_127");
                        $("#off-meter").replaceWith("<span class='glyphicon glyphicon-ok'>$user_manage_116</span>");
                        $("#t1 tbody :checked").parents("tr").find("td:eq(13)").text("$user_manage_116");
                        $('#t4').append('<tr><td>$user_manage_122</td><td>' + getTime() + '</td></tr>');
                    } else {
                        alert("$public_036");
                    }
                },
                error: function () {
                    alert("error");
                }
            });
        } else {
            alert("$user_manage_091");
        }
    }
}

function loadCommunities() {
    var province = $("#xadd1 option:selected").val().trim();
    var city = $("#xadd2 option:selected").val().trim();
    var district = $("#xadd3 option:selected").val().trim();
    $("#address_community").empty();
    $("#address_community").append($("<option value=''>$user_manage_009</option>"));
    $("#address_building").empty();
    $("#address_building").append($("<option value=''>$user_manage_010</option>"));
    $("#address_unit").empty();
    $("#address_unit").append($("<option value=''>$user_manage_011</option>"));
    $("#mbus-name4").empty();
    $("#mbus-name4").append($("<option value=''>$user_manage_058</option>"));
    jQuery.ajax({
        url: "../user/findAllCommunities.do",
        type: "post",
        async: false,
        data: {
            "province": province,
            "city": city,
            "district": district
        },
        dataType: "json",
        success: function (result) {
            if (result.status == 1) {
                var communities = result.data;
                for (var i = 0; i < communities.length; i++) {
                    var community_name = communities[i].community_name;
                    var str = "<option value='" + community_name + "'>" + community_name + "</option>";
                    $("#address_community").append($(str));
                }
            } else {
                alert("$public_036");
            }
        },
        error: function () {
            alert("error");
        }
    });
    loadSuppliers();
}

function loadBuildings() {
    var province = $("#xadd1 option:selected").val().trim();
    var city = $("#xadd2 option:selected").val().trim();
    var district = $("#xadd3 option:selected").val().trim();
    var community_name = $("#address_community option:selected").val().trim();
    if (community_name != "$user_manage_009") {
        jQuery.ajax({
            url: "../user/loadBuilding.do",
            type: "post",
            async: false,
            data: {
                "province": province,
                "city": city,
                "district": district,
                "community_name": community_name
            },
            dataType: "json",
            success: function (result) {
                if (result.status == 1) {
                    $("#address_building").empty();
                    $("#address_building").append($("<option value=''>$user_manage_010</option>"));
                    $("#address_unit").empty();
                    $("#address_unit").append($("<option value=''>$user_manage_011</option>"));
                    $("#mbus-name4").empty();
                    $("#mbus-name4").append($("<option value=''>$user_manage_058</option>"));
                    var buildings = result.data;
                    for (var i = 0; i < buildings.length; i++) {
                        var building_name = buildings[i].building_name;
                        var str = "<option value='" + building_name + "'>" + building_name + "</option>";
                        $("#address_building").append($(str));
                    }
                } else {
                    alert("$public_036");
                }
            },
            error: function () {
                alert("error");
            }
        });
    } else {
        $("#address_building").empty();
        $("#address_building").append($("<option value=''>$user_manage_010</option>"));
        $("#address_unit").empty();
        $("#address_unit").append($("<option value=''>$user_manage_011</option>"));
        $("#mbus-name4").empty();
        $("#mbus-name4").append($("<option value=''>$user_manage_058</option>"));
    }
}

function loadUnits() {
    var province = $("#xadd1 option:selected").val().trim();
    var city = $("#xadd2 option:selected").val().trim();
    var district = $("#xadd3 option:selected").val().trim();
    var community_name = $("#address_community option:selected").val().trim();
    var building_name = $("#address_building option:selected").val().trim();
    if (building_name != "$user_manage_010") {
        jQuery.ajax({
            url: "../user/loadUnit.do",
            type: "post",
            async: false,
            data: {
                "province": province,
                "city": city,
                "district": district,
                "community_name": community_name,
                "building_name": building_name
            },
            dataType: "json",
            success: function (result) {
                if (result.status == 1) {
                    $("#address_unit").empty();
                    $("#address_unit").append($("<option value=''>$user_manage_011</option>"));
                    $("#mbus-name4").empty();
                    $("#mbus-name4").append($("<option value=''>$user_manage_058</option>"));
                    var units = result.data;
                    for (var i = 0; i < units.length; i++) {
                        var unit_name = units[i].unit_name;
                        var str = "<option value='" + unit_name + "'>" + unit_name + "</option>";
                        $("#address_unit").append($(str));
                    }
                } else {
                    alert("$public_036");
                }
            },
            error: function () {
                alert("error");
            }
        });
    } else {
        $("#address_unit").empty();
        $("#address_unit").append($("<option value=''>$user_manage_011</option>"));
        $("#mbus-name4").empty();
        $("#mbus-name4").append($("<option value=''>$user_manage_058</option>"));
    }
}
function loadConcentrators() {
    var community_name = $("#address_community option:selected").val().trim();
    var building_name = $("#address_building option:selected").val().trim();
    var unit_name = $("#address_unit option:selected").val().trim();
    if (unit_name != "$user_manage_011") {
        jQuery.ajax({
            url: "../user/loadConcentrators.do",
            type: "post",
            async: false,
            data: {
                "community_name": community_name,
                "building_name": building_name,
                "unit_name": unit_name,
                "startNo": 0,
                "pageSize": 0
            },
            dataType: "json",
            success: function (result) {
                $("#mbus-name4").empty();
                $("#mbus-name4").append($("<option value=''>$user_manage_058</option>"));
                var map = result.data;
                var concentrators = map["concentrators"];
                for (var i = 0; i < concentrators.length; i++) {
                    var concentrator_name = concentrators[i].concentrator_name;
                    var str = "<option value='" + concentrator_name + "'>" + concentrator_name + "</option>";
                    $("#mbus-name4").append($(str));
                }
            },
            error: function () {
                alert("error");
            }
        });
    } else {
        $("#mbus-name4").empty();
        $("#mbus-name4").append($("<option value=''>$user_manage_058</option>"));
    }
}
function loadSuppliers() {
    var province = $("#xadd1 option:selected").val().trim();
    var city = $("#xadd2 option:selected").val().trim();
    var district = $("#xadd3 option:selected").val().trim();
    $("#location-name4").empty();
    $("#location-name4").append($("<option value=''>$user_manage_060</option>"));
    jQuery.ajax({
        url: "../user/loadSuppliers.do",
        type: "post",
        async: false,
        data: {
            "province": province,
            "city": city,
            "district": district,
            "startNo": 0,
            "pageSize": 0
        },
        dataType: "json",
        success: function (result) {
            if (result.status == 1) {
                var map = result.data;
                var suppliers = map["suppliers"];
                for (var i = 0; i < suppliers.length; i++) {
                    var supplier_name = suppliers[i].supplier_name;
                    var str = "<option value='" + supplier_name + "'>" + supplier_name + "</option>";
                    $("#location-name4").append($(str));
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

function loadFeeTypes() {
    $("#price-type4").empty();
    $("#price-type4").append($("<option value=''>$user_manage_06701</option>"));
    jQuery.ajax({
        url: "../user/loadFeeTypes.do",
        type: "post",
        async: false,
        dataType: "json",
        success: function (result) {
            if (result.status == 1) {
                var feeTypes = result.data;
                for (var i = 0; i < feeTypes.length; i++) {
                    var fee_type = feeTypes[i].feeTypeName;
                    var str = "<option value='" + fee_type + "'>" + fee_type + "</option>";
                    $("#price-type4").append($(str));
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

function loadHouseCoefficients() {
    $("#house-coefficient4").empty();
    $("#house-coefficient4").append($("<option value=''>$user_manage_096</option>"));
    jQuery.ajax({
        url: "../user/load_house_coefficient.do",
        type: "post",
        dataType: "json",
        success: function (result) {
            if (result.status == 1) {
                var coes = result.data;
                for (var i = 0; i < coes.length; i++) {
                    var coefficient = coes[i].coefficient;
                    var str = "<option value='" + coefficient + "'>" + coefficient + "</option>";
                    $("#house-coefficient4").append($(str));
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

//用户信息修改按钮
function modify_user() {
    var userNo = $("#t1 tbody :checked").length;
    if (userNo == 0) {
        alert("$user_manage_098");
    } else if (userNo > 1) {
        alert("$user_manage_099");
    } else if (userNo == 1) {
        $("#edit-userinfo").modal('show');
    }
    $("#t1 tbody :checked").each(
            function () {
                var index = $(this).parents("tr").find("td:eq(1)").text().trim();
                var user_name = $(this).parents("tr").find("td:eq(2)").text().trim();
                var mbus_name = $(this).parents("tr").find("td:eq(9)").text().trim();
                var meter_no = $(this).parents("tr").find("td:eq(10)").text().trim();
                $("#t1").data("meter_no", meter_no);
                var fee_type = $("#t1").data("fee_type" + index);
                var meter_type = $("#t1").data("meter_type" + index);
                if (meter_type === "$user_manage_061") {
                    meter_type = 10;
                }
                if (meter_type === "$user_manage_061_2") {
                    meter_type = 11;
                }
                if (meter_type === "$user_manage_062") {
                    meter_type = 20;
                }
                if (meter_type === "$read_list_052") {
                    meter_type = 30;
                }
                if (meter_type === "$remote_read_047_2") {
                    meter_type = 40;
                }
                var meter_model = $("#t1").data("meter_model" + index);
                var submeter_no = $("#t1").data("submeter_no" + index);
                var valve_no = $("#t1").data("valve_no" + index);
                var supplier_name = $("#t1").data("supplier_name" + index);
                var province = $("#t1").data("province" + index);
                var city = $("#t1").data("city" + index);
                var district = $("#t1").data("district" + index);
                var pay_type = $("#t1").data("pay_type" + index);
                //根据用户的省市区地址信息，进行动态显示
                addressInit('xadd1', 'xadd2', 'xadd3', province, city, district);
                // 加载小区选项列表
                loadCommunities();
                loadSuppliers();
                var user_address_community = $(this).parents("tr").find("td:eq(3)").text().trim();
                var user_address_building = $(this).parents("tr").find("td:eq(4)").text().trim();
                var user_address_unit = $(this).parents("tr").find("td:eq(5)").text().trim();
                var user_address_room = $(this).parents("tr").find("td:eq(6)").text().trim();
                var contact_info = $(this).parents("tr").find("td:eq(7)").text().trim();
                var id_card_no = $(this).parents("tr").find("td:eq(8)").text().trim();
                var protocol_type = $("#t1").data("protocol_type" + index);
                var valve_protocol = $("#t1").data("valve_protocol" + index);
                var initing_data = $("#t1").data("initing_data" + index);
                document.getElementById("user-name4").value = user_name;
                document.getElementById("user-room4").value = user_address_room;
                $("#address_community").find("option[value='" + user_address_community + "']").attr("selected", true);
                loadBuildings();
                $("#address_building").find("option[value='" + user_address_building + "']").attr("selected", true);
                loadUnits();
                $("#address_unit").find("option[value='" + user_address_unit + "']").attr("selected", true);
                loadConcentrators();
                $("#mbus-name4").find("option[value='" + mbus_name + "']").attr("selected", true);
                $("#location-name4").find("option[value='" + supplier_name + "']").attr("selected", true);
                loadFeeTypes();
                $("#price-type4").find("option[value='" + fee_type + "']").attr("selected", true);
                var meter_typeed = document.getElementById("meter-type4");
                for (var i = 0; i < meter_typeed.options.length; i++) {
                    if (parseInt(meter_typeed.options[i].value) === meter_type) {
                        meter_typeed.options[i].selected = true;
                    }
                }
                var meter_modeled = document.getElementById("meter-model4");
                for (var i = 0; i < meter_modeled.options.length; i++) {
                    if (meter_modeled.options[i].value === meter_model) {
                        meter_modeled.options[i].selected = true;
                    }
                }
                var meter_protocoled = document.getElementById("meter-protocol4");
                for (var i = 0; i < meter_protocoled.options.length; i++) {
                    if (meter_protocoled.options[i].value === protocol_type) {
                        meter_protocoled.options[i].selected = true;
                    }
                }
                var valve_protocoled = document.getElementById("valve-protocol4");
                for (var i = 0; i < valve_protocoled.options.length; i++) {
                    if (valve_protocoled.options[i].value === valve_protocol) {
                        valve_protocoled.options[i].selected = true;
                    }
                }
                var pay_typeed = document.getElementById("pay_type4");
                for (var i = 0; i < pay_typeed.options.length; i++) {
                    if (parseInt(pay_typeed.options[i].value) === pay_type) {
                        pay_typeed.options[i].selected = true;
                    }
                }
                document.getElementById("meter-no4").value = meter_no;
                document.getElementById("user-mobile4").value = contact_info;
                document.getElementById("user-cardID4").value = id_card_no;
                document.getElementById("meter-subID4").value = submeter_no;
                document.getElementById("valve-cardID4").value = valve_no;
                document.getElementById("initing-data4").value = initing_data;
            });
}

//用户信息修改确认
function modify_user_confirm() {
    var index = $("#t1 tbody :checked").parents("tr").find("td:eq(1)").text().trim();
    var meter_no = $("#t1").data("meter_no");
    var new_meter_no = $("#meter-no4").val().trim();
    var user_name = $("#user-name4").val().trim();
    var province = $("#xadd1 option:selected").val().trim();
    var city = $("#xadd2 option:selected").val().trim();
    var district = $("#xadd3 option:selected").val().trim();
    var user_address_area = province + city + district;
    var user_address_community = $("#address_community option:selected").val().trim();
    var user_address_building = $("#address_building option:selected").val().trim();
    var user_address_unit = $("#address_unit option:selected").val().trim();
    var user_address_room = $("#user-room4").val().trim().trim();
    var user_address_original_room = $("#user-room4").val().trim().trim();
    var concentrator_name = $("#mbus-name4 option:selected").val().trim();
    var location_name = $("#location-name4 option:selected").val().trim();
    var contact_info = $("#user-mobile4").val().trim();
    var id_card_no = $("#user-cardID4").val().trim();
    var meter_type = $("#meter-type4 option:selected").val().trim();
    var meter_model = $("#meter-model4 option:selected").val().trim();
    var protocol_type = $("#meter-protocol4 option:selected").val().trim();
    //var protocol_type_str = $("#meter-protocol4 option:selected").val().trim();
    var valve_protocol = $("#valve-protocol4 option:selected").val().trim();
    var submeter_no = $("#meter-subID4").val().trim();
    var valve_no = $("#valve-cardID4").val().trim();
    var operator_account = getCookie("account_name");
    var pay_type = $("#pay_type4 option:selected").val().trim();
    var user_id = $("#t1").data("user_id" + index);
    var initing_data = $("#initing-data4").val().trim();
    var original_initing_data = $("#t1").data("initing_data" + index);
    var original_user_name = $("#t1").data("user_name" + index);
    var original_user_address_area = $("#t1").data("user_address_area" + index);
    var original_user_address_community = $("#t1").data("user_address_community" + index);
    var original_user_address_building = $("#t1").data("user_address_building" + index);
    var original_user_address_unit = $("#t1").data("user_address_unit" + index);
    var original_user_address_room = $("#t1").data("user_address_room" + index);
    var original_concentrator_name = $("#t1").data("concentrator_name" + index);
    var original_supplier_name = $("#t1").data("supplier_name" + index);
    var original_contact_info = $("#t1").data("contact_info" + index);
    var original_id_card_no = $("#t1").data("id_card_no" + index);
    var original_meter_type = $("#t1").data("meter_type" + index);
    var original_meter_model = $("#t1").data("meter_model" + index);
    var original_protocol_type = $("#t1").data("protocol_type" + index);
    var original_valve_protocol = $("#t1").data("valve_protocol" + index);
    var original_submeter_no = $("#t1").data("submeter_no" + index);
    var original_valve_no = $("#t1").data("valve_no" + index);
    var original_pay_type = $("#t1").data("pay_type" + index);
    var check = true;
    var numError = $('#edit-userinfo form .onError').length;
    if (numError > 0
            || user_address_community == ""
            || user_address_building == ""
            || user_address_unit == ""
            || location_name == ""
            || meter_type == ""
            || meter_model == ""
            || protocol_type == ""
            ) {
        alert("$user_add_024");
        check = false;
        return;
    }
    if (check) {
        jQuery.ajax({
            url: "../user/modifyUserConfirm.do",
            type: "post",
            data: {
                "user_id": user_id,
                "user_name": user_name,
                "province": province,
                "city": city,
                "district": district,
                "user_address_area": user_address_area,
                "user_address_community": user_address_community,
                "user_address_building": user_address_building,
                "user_address_unit": user_address_unit,
                "user_address_room": user_address_room,
                "user_address_original_room": user_address_original_room,
                "concentrator_name": concentrator_name,
                "supplier_name": location_name,
                "contact_info": contact_info,
                "id_card_no": id_card_no,
                "meter_type": meter_type,
                "meter_model": meter_model,
                "protocol_type": protocol_type,
                "valve_protocol": valve_protocol,
                "meter_no": new_meter_no,
                "submeter_no": submeter_no,
                "valve_no": valve_no,
                "operator_account": operator_account,
                "pay_type": pay_type,
                "initing_data": initing_data,
                "original_user_name": original_user_name,
                "original_user_address_area": original_user_address_area,
                "original_user_address_community": original_user_address_community,
                "original_user_address_building": original_user_address_building,
                "original_user_address_unit": original_user_address_unit,
                "original_user_address_room": original_user_address_room,
                "original_concentrator_name": original_concentrator_name,
                "original_supplier_name": original_supplier_name,
                "original_contact_info": original_contact_info,
                "original_id_card_no": original_id_card_no,
                "original_meter_type": original_meter_type,
                "original_meter_model": original_meter_model,
                "original_protocol_type": original_protocol_type,
                "original_valve_protocol": original_valve_protocol,
                "original_meter_no": meter_no,
                "original_submeter_no": original_submeter_no,
                "original_valve_no": original_valve_no,
                "original_pay_type": original_pay_type,
                "original_initing_data": original_initing_data
            },
            dataType: "json",
            success: function (result) {
                if (result.status == 1) {
                    $("#edit-userinfo").modal('hide');
                    alert("$public_041");
                    window.location.reload(true);
                } else if (result.status === 1062) {
                    alert("$user_manage_141");
                }
            },
            error: function () {
                alert("error");
            }
        });
    }
}

//删除用户按钮
function remove_user() {
    var id_arr = new Array();
    var name_arr = new Array();
    $("#t1 tbody :checked").each(function () {
        var index = $(this).parents("tr").find("td:eq(1)").text();
        //var meter_no = $(this).parents("tr").find("td:eq(10)").text();
        var user_name = $(this).parents("tr").find("td:eq(2)").text();
        var user_id = $("#t1").data("user_id" + index);
        id_arr.push(user_id);
        name_arr.push(user_name);
        $("#t1").data("id_arr", id_arr);
        $("#t1").data("name_arr", name_arr);
    });
    if (id_arr.length == 0) {
        alert("$user_manage_128");
    } else if (id_arr.length > 0) {
        $("#delete-user").modal('show');
    }
}

//删除用户
function remove_user_confirm() {
    var id_arr = $("#t1").data("id_arr");
    var name_arr = $("#t1").data("name_arr");
    var id_arrString = id_arr.toString();
    var name_arrString = name_arr.toString();
    jQuery.ajax({
        url: "../user/removeUser.do",
        type: "post",
        data: {
            "user_ids": id_arrString,
            "user_names": "removeUser"
        },
        dataType: "json",
        success: function (result) {
            if (result.status == 1) {
                $("#close_removeUserConfirm").click();
                alert("$public_050");
                findUser();
            }
        },
        error: function () {
            alert("error");
        }
    });
}

function loadUpDownForm() {
    var str = "";
    str += '<form id="fileUpload" enctype="multipart/form-data" action=';
    str += '../background/fileOperate/upload.do method="post" onsubmit="return fileUpLoad();">';
    str += '<input id="file1" type="file" name="file1"/>';
    str += '<input id="lang" type="hidden" name="lang"/>';
    str += '</form>';
    str += '<p>$user_manage_130</p>';
    str += '<form  enctype="multipart/form-data" action=';
    str += '../background/fileOperate/downloadModel.do method="post">';
    str += '<input id="lang1" type="hidden" name="lang1"/>';
    str += '<input type="submit" value="$user_manage_131" class="button white"/>';
    str += '</form>';
    $("#upLoadDiv").append($(str));
    var lan_cookie = getCookie("language");
    document.getElementById("lang").value = lan_cookie;
    document.getElementById("lang1").value = lan_cookie;
    $('#tab-import button:contains($user_manage_129)').click(function () {
        var file1 = $("#file1").val();
        if (file1 != "") {
            $('#fileUpload').submit();
        } else {
            alert("$user_manage_136");
        }
    });
}

function download() {
    jQuery.ajax({
        url: "../background/fileOperate/downloadModel.do",
        type: "post",
        success: function () {
            alert("download ok!");
        },
        error: function () {
            alert("error");
        }
    });
}

function fileUpLoad() {
    $('[name=upload-submit]').attr('disabled', true);
    $('[name=upload-cancel]').attr('disabled', true);
    $("#fileUpload").ajaxSubmit(function () {
        $('[name=upload-submit]').attr('disabled', false);
        $('[name=upload-cancel]').attr('disabled', false);
        alert("$importSuccess_001");
    });
    return false;
}

//导出房源信息
function exportRoomInfo() {
    var mask = $("<div class='window_mask'></div>");
    $("body").css("position", "relative");
    mask.appendTo("body");
    if (!window.confirm("$public_064")) {
        mask.remove();
        return;
    }
    ;
    window.location.href = "../report/exportRoomInfo.do";
    var f = setInterval(function () {
        jQuery.ajax({
            url: "../report/checkStatus.do",
            success: function (result) {
                if (result.status === 1) {
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
    var str = d.getFullYear() + "." + m + "." + date + " " + h + ":" + min
            + ":" + sec;
    return str;
}