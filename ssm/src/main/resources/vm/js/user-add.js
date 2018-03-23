$(function () {
    $("#admin").text(account_name);
    $("#sum-to-do").text("(" + toDoNo + ")");
    $("#exit").click(logout);
    addressInit('add1', 'add2', 'add3', 'zhejiang', 'hangzhou', 'xihu');
    clear();
    $("#regist").click(regist);
    $('#res').click(clear);
})

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
  //  $("#house-area2")[0].value = "0";
    $('form .formtips').remove();
    findCommunities();
    findSuppliers();
    findFeeTypes();
    findHouseCoefficients();
}

function findCommunities() {
    $("#community").empty();
    $("#community").append($("<option val=''>$user_manage_009</option>"));
    $("#building").empty();
    $("#building").append($("<option val=''>$user_manage_010</option>"));
    $("#unit").empty();
    $("#unit").append($("<option val=''>$user_manage_011</option>"));
    $("#mbus-name2").empty();
    $("#mbus-name2").append($("<option val=''>$user_manage_058</option>"));
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
                    alert("$public_035");
                }
            },
            error: function () {
                alert("error");
            }
        });
    } else {
        $("#unit").empty();
        $("#unit").append($("<option value=''>$user_manage_011</option>"));
        $("#mbus-name2").empty();
        $("#mbus-name2").append($("<option value=''>$user_manage_058</option>"));
    }
}
function findConcentrators() {
    var community_name = $("#community option:selected").val().trim();
    var building_name = $("#building option:selected").val().trim();
    var unit_name = $("#unit option:selected").val().trim();
    if (unit_name != "$user_manage_011") {
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
        $("#mbus-name2").append($("<option value=''>$user_manage_058</option>"));
    }
}

function findSuppliers() {
    $("#location-name2").empty();
    $("#location-name2").append($("<option value=''>$user_manage_060</option>"));
    jQuery.ajax({
        url: "../user/loadSuppliers.do",
        type: "post",
        data: {
            "province": "",
            "city": "",
            "district": "",
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
                alert("public_036");
            }
        },
        error: function () {
            alert("error");
        }
    });
}

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

function findHouseCoefficients() {
    $("#house-coefficient2").empty();
    $("#house-coefficient2").append($("<option value=''>$user_add_023</option>"));
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
                    $("#house-coefficient2").append($(str));
                }
                $("#house-coefficient2").append("<option selected='selected'>0</option>");
            } else {
                alert("$public_036");
            }
        },
        error: function () {
            alert("error");
        }
    });
}

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
    //var user_address_original_room = $("#user-user_original_room").val().trim();
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
    var house_coefficient = $("#house_coefficient2 option:selected").val();
    var operator_account = getCookie("account_name");
    var check = true;
    var numError = $('#tab-register form .onError').length;
    if (numError > 0 || user_name == "" || user_address_community == "" || user_address_building == ""
            || user_address_unit == "" || user_address_room == ""
            || user_address_room == "" || concentrator_name == ""
            || supplier_name == "" || id_card_no == ""
            || meter_type == "" || meter_model == "" || protocol_type == ""
            || house_coefficient == "" || meter_no == "" 
            || submeter_no == "" || valve_no == "") {
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
               "user_address_original_room": 0,
                "concentrator_name": concentrator_name,
                "supplier_name": supplier_name,
                "contact_info": contact_info,
                "id_card_no": id_card_no,
                "house_area": 0,
                "coefficient_name": "",
                "meter_type": meter_type,
                "meter_model": meter_model,
                "protocol_type": protocol_type,
                "valve_protocol" : valve_protocol,
                "fee_type": fee_type,
                "meter_no": meter_no,
                "submeter_no": submeter_no,
                "valve_no": valve_no,
                "auto_deduction": "1",
                "meter_status": "1",
                "user_status": "1",
                "operator_account": operator_account
            },
            dataType: "json",
            success: function (result) {
                if (result.status == 1) {
                    alert("$public_037");
                    window.location.href = "user-manage.html";
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

