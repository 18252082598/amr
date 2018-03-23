//加载小区
function load_communities() {
    $("#user_address_community").empty();
    $("#user_address_community").append($("<option value=''>$public_016</option>"));
    $("#user_address_building").empty();
    $("#user_address_building").append($("<option value=''>$public_017</option>"));
    $("#user_address_unit").empty();
    $("#user_address_unit").append($("<option value=''>$public_018</option>"));
    $("#user_address_room").empty();
    $("#user_address_room").append($("<option value=''>$public_019</option>"));
    jQuery.ajax({
        url: "../user/loadCommunity.do",
        type: "post",
        async: false,
        dataType: "json",
        success: function (result) {
            if (result.status == 1) {
                var communities = result.data;
                for (var i = 0; i < communities.length; i++) {
                    var community_name = communities[i].community_name;
                    var str = "<option value='" + community_name + "'>" + community_name + "</option>";
                    $("#user_address_community").append($(str));
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
//加载楼栋
function findAllBuildings() {
    var community_name = $("#user_address_community option:selected").val().trim();
    if (community_name != "") {
        jQuery.ajax({
            url: "../user/loadBuilding.do",
            type: "post",
            data: {
                "province": "",
                "city": "",
                "district": "",
                "community_name": community_name
            },
            dataType: "json",
            success: function (result) {
                if (result.status == 1) {
                    $("#user_address_building").empty();
                    $("#user_address_building").append($("<option value=''>$public_017</option>"));
                    $("#user_address_unit").empty();
                    $("#user_address_unit").append($("<option value=''>$public_018</option>"));
                    $("#user_address_room").empty();
                    $("#user_address_room").append($("<option value=''>$public_019</option>"));
                    var buildings = result.data;
                    for (var i = 0; i < buildings.length; i++) {
                        var building_name = buildings[i].building_name;
                        var str = "<option value='" + building_name + "'>" + building_name + "</option>";
                        $("#user_address_building").append($(str));
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
        $("#user_address_building").empty();
        $("#user_address_building").append($("<option value=''>$public_017</option>"));
        $("#user_address_unit").empty();
        $("#user_address_unit").append($("<option value=''>$public_018</option>"));
        $("#user_address_room").empty();
        $("#user_address_room").append($("<option value=''>$public_019</option>"));
    }
}
//加载单元
function findAllUnits() {
    var community_name = $("#user_address_community option:selected").val().trim();
    var building_name = $("#user_address_building option:selected").val().trim();
    if (building_name != "") {
        jQuery.ajax({
            url: "../user/loadUnit.do",
            type: "post",
            data: {
                "province": "",
                "city": "",
                "district": "",
                "community_name": community_name,
                "building_name": building_name
            },
            dataType: "json",
            success: function (result) {
                if (result.status == 1) {
                    $("#user_address_unit").empty();
                    $("#user_address_unit").append($("<option value=''>$public_018</option>"));
                    $("#user_address_room").empty();
                    $("#user_address_room").append($("<option value=''>$public_019</option>"));
                    var units = result.data;
                    for (var i = 0; i < units.length; i++) {
                        var unit_name = units[i].unit_name;
                        var str = "<option value='" + unit_name + "'>" + unit_name + "</option>";
                        $("#user_address_unit").append($(str));
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
        $("#user_address_unit").empty();
        $("#user_address_unit").append($("<option value=''>$public_018</option>"));
        $("#user_address_room").empty();
        $("#user_address_room").append($("<option value=''>$public_019</option>"));
    }
}

//根据单元加载房号(从meter_user表内)
function findAllRoomsInUser() {
    var community_name = $("#user_address_community option:selected").val().trim();
    var building_name = $("#user_address_building option:selected").val().trim();
    var unit_name = $("#user_address_unit option:selected").val().trim();
    if (unit_name != "") {
        jQuery.ajax({
            url: "../user/findRoomsByAddress.do",
            type: "post",
            data: {
                "community_name": community_name,
                "building_name": building_name,
                "unit_name": unit_name
            },
            dataType: "json",
            success: function (result) {
                if (result.status == 1) {
                    var rooms = result.data;
                    $("#user_address_room").empty();
                    $("#user_address_room").append($("<option value=''>$public_019</option>"));
                    for (var i = 0; i < rooms.length; i++) {
                        var room_name = rooms[i];
                        var str = "<option value='" + room_name + "'>" + room_name + "</option>";
                        $("#user_address_room").append($(str));
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
        $("#user_address_room").empty();
        $("#user_address_room").append($("<option value=''>$public_019</option>"));
    }
}

function load_operators() {
    $("#operator_name").empty();
    $("#operator_name").append($("<option value=''>$public_03601</option>"));
    jQuery.ajax({
        url: "../user/loadAllOperators.do",
        type: "post",
        dataType: "json",
        success: function (result) {
            if (result.status == 1) {
                var admins = result.data;
                for (var i = 0; i < admins.length; i++) {
                    var admin_account = admins[i].admin_account;
                    var str = "<option value='" + admin_account + "'>" + admin_account + "</option>";
                    $("#operator_name").append($(str));
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




function load_communitiesFail() {
    $("#user_address_communityFail").empty();
    $("#user_address_communityFail").append($("<option value=''>$public_016</option>"));
    $("#user_address_buildingFail").empty();
    $("#user_address_buildingFail").append($("<option value=''>$public_017</option>"));
    $("#user_address_unitFail").empty();
    $("#user_address_unitFail").append($("<option value=''>$public_018</option>"));
    $("#user_address_roomFail").empty();
    $("#user_address_roomFail").append($("<option value=''>$public_019</option>"));
    jQuery.ajax({
        url: "../user/loadCommunity.do",
        type: "post",
        async: false,
        dataType: "json",
        success: function (result) {
            if (result.status == 1) {
                var communities = result.data;
                for (var i = 0; i < communities.length; i++) {
                    var community_name = communities[i].community_name;
                    var str = "<option value='" + community_name + "'>" + community_name + "</option>";
                    $("#user_address_communityFail").append($(str));
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

function findAllBuildingsFail() {
    var community_name = $("#user_address_communityFail option:selected").val().trim();
    if (community_name != "") {
        jQuery.ajax({
            url: "../user/loadBuilding.do",
            type: "post",
            data: {
                "province": "",
                "city": "",
                "district": "",
                "community_name": community_name
            },
            dataType: "json",
            success: function (result) {
                if (result.status == 1) {
                    $("#user_address_buildingFail").empty();
                    $("#user_address_buildingFail").append($("<option value=''>$public_017</option>"));
                    $("#user_address_unitFail").empty();
                    $("#user_address_unitFail").append($("<option value=''>$public_018</option>"));
                    $("#user_address_roomFail").empty();
                    $("#user_address_roomFail").append($("<option value=''>$public_019</option>"));
                    var buildings = result.data;
                    for (var i = 0; i < buildings.length; i++) {
                        var building_name = buildings[i].building_name;
                        var str = "<option value='" + building_name + "'>" + building_name + "</option>";
                        $("#user_address_buildingFail").append($(str));
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
        $("#user_address_buildingFail").empty();
        $("#user_address_buildingFail").append($("<option value=''>$public_017</option>"));
        $("#user_address_unitFail").empty();
        $("#user_address_unitFail").append($("<option value=''>$public_018</option>"));
        $("#user_address_roomFail").empty();
        $("#user_address_roomFail").append($("<option value=''>$public_019</option>"));
    }
}

function findAllUnitsFail() {
    var community_name = $("#user_address_communityFail option:selected").val().trim();
    var building_name = $("#user_address_buildingFail option:selected").val().trim();
    if (building_name != "") {
        jQuery.ajax({
            url: "../user/loadUnit.do",
            type: "post",
            data: {
                "province": "",
                "city": "",
                "district": "",
                "community_name": community_name,
                "building_name": building_name
            },
            dataType: "json",
            success: function (result) {
                if (result.status == 1) {
                    $("#user_address_unitFail").empty();
                    $("#user_address_unitFail").append($("<option value=''>$public_018</option>"));
                    $("#user_address_roomFail").empty();
                    $("#user_address_roomFail").append($("<option value=''>$public_019</option>"));
                    var units = result.data;
                    for (var i = 0; i < units.length; i++) {
                        var unit_name = units[i].unit_name;
                        var str = "<option value='" + unit_name + "'>" + unit_name + "</option>";
                        $("#user_address_unitFail").append($(str));
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
        $("#user_address_unitFail").empty();
        $("#user_address_unitFail").append($("<option value=''>$public_018</option>"));
        $("#user_address_roomFail").empty();
        $("#user_address_roomFail").append($("<option value=''>$public_019</option>"));
    }
}

//根据单元加载房号(从meter_user表内)
function findAllRoomsInUserFail() {
    var community_name = $("#user_address_communityFail option:selected").val().trim();
    var building_name = $("#user_address_buildingFail option:selected").val().trim();
    var unit_name = $("#user_address_unitFail option:selected").val().trim();
    if (unit_name != "") {
        jQuery.ajax({
            url: "../user/findRoomsByAddress.do",
            type: "post",
            data: {
                "community_name": community_name,
                "building_name": building_name,
                "unit_name": unit_name
            },
            dataType: "json",
            success: function (result) {
                if (result.status == 1) {
                    var rooms = result.data;
                    $("#user_address_roomFail").empty();
                    $("#user_address_roomFail").append($("<option value=''>$public_019</option>"));
                    for (var i = 0; i < rooms.length; i++) {
                        var room_name = rooms[i];
                        var str = "<option value='" + room_name + "'>" + room_name + "</option>";
                        $("#user_address_roomFail").append($(str));
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
        $("#user_address_roomFail").empty();
        $("#user_address_roomFail").append($("<option value=''>$public_019</option>"));
    }
}





function load_communitiesDoubtful() {
    $("#user_address_communityDoubtful").empty();
    $("#user_address_communityDoubtful").append($("<option value=''>$public_016</option>"));
    $("#user_address_buildingDoubtful").empty();
    $("#user_address_buildingDoubtful").append($("<option value=''>$public_017</option>"));
    $("#user_address_unitDoubtful").empty();
    $("#user_address_unitDoubtful").append($("<option value=''>$public_018</option>"));
    $("#user_address_roomDoubtful").empty();
    $("#user_address_roomDoubtful").append($("<option value=''>$public_019</option>"));
    jQuery.ajax({
        url: "../user/loadCommunity.do",
        type: "post",
        async: false,
        dataType: "json",
        success: function (result) {
            if (result.status == 1) {
                var communities = result.data;
                for (var i = 0; i < communities.length; i++) {
                    var community_name = communities[i].community_name;
                    var str = "<option value='" + community_name + "'>" + community_name + "</option>";
                    $("#user_address_communityDoubtful").append($(str));
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

function findAllBuildingsDoubtful() {
    var community_name = $("#user_address_communityDoubtful option:selected").val().trim();
    if (community_name != "") {
        jQuery.ajax({
            url: "../user/loadBuilding.do",
            type: "post",
            data: {
                "province": "",
                "city": "",
                "district": "",
                "community_name": community_name
            },
            dataType: "json",
            success: function (result) {
                if (result.status == 1) {
                    $("#user_address_buildingDoubtful").empty();
                    $("#user_address_buildingDoubtful").append($("<option value=''>$public_017</option>"));
                    $("#user_address_unitDoubtful").empty();
                    $("#user_address_unitDoubtful").append($("<option value=''>$public_018</option>"));
                    $("#user_address_roomDoubtful").empty();
                    $("#user_address_roomDoubtful").append($("<option value=''>$public_019</option>"));
                    var buildings = result.data;
                    for (var i = 0; i < buildings.length; i++) {
                        var building_name = buildings[i].building_name;
                        var str = "<option value='" + building_name + "'>" + building_name + "</option>";
                        $("#user_address_buildingDoubtful").append($(str));
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
        $("#user_address_buildingDoubtful").empty();
        $("#user_address_buildingDoubtful").append($("<option value=''>$public_017</option>"));
        $("#user_address_unitDoubtful").empty();
        $("#user_address_unitDoubtful").append($("<option value=''>$public_018</option>"));
        $("#user_address_roomDoubtful").empty();
        $("#user_address_roomDoubtful").append($("<option value=''>$public_019</option>"));
    }
}

function findAllUnitsDoubtful() {
    var community_name = $("#user_address_communityDoubtful option:selected").val().trim();
    var building_name = $("#user_address_buildingDoubtful option:selected").val().trim();
    if (building_name != "") {
        jQuery.ajax({
            url: "../user/loadUnit.do",
            type: "post",
            data: {
                "province": "",
                "city": "",
                "district": "",
                "community_name": community_name,
                "building_name": building_name
            },
            dataType: "json",
            success: function (result) {
                if (result.status == 1) {
                    $("#user_address_unitDoubtful").empty();
                    $("#user_address_unitDoubtful").append($("<option value=''>$public_018</option>"));
                    $("#user_address_roomDoubtful").empty();
                    $("#user_address_roomDoubtful").append($("<option value=''>$public_019</option>"));
                    var units = result.data;
                    for (var i = 0; i < units.length; i++) {
                        var unit_name = units[i].unit_name;
                        var str = "<option value='" + unit_name + "'>" + unit_name + "</option>";
                        $("#user_address_unitDoubtful").append($(str));
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
        $("#user_address_unitDoubtful").empty();
        $("#user_address_unitDoubtful").append($("<option value=''>$public_018</option>"));
        $("#user_address_roomDoubtful").empty();
        $("#user_address_roomDoubtful").append($("<option value=''>$public_019</option>"));
    }
}

//根据单元加载房号(从meter_user表内)
function findAllRoomsInUserDoubtful() {
    var community_name = $("#user_address_communityDoubtful option:selected").val().trim();
    var building_name = $("#user_address_buildingDoubtful option:selected").val().trim();
    var unit_name = $("#user_address_unitDoubtful option:selected").val().trim();
    if (unit_name != "") {
        jQuery.ajax({
            url: "../user/findRoomsByAddress.do",
            type: "post",
            data: {
                "community_name": community_name,
                "building_name": building_name,
                "unit_name": unit_name
            },
            dataType: "json",
            success: function (result) {
                if (result.status == 1) {
                    var rooms = result.data;
                    $("#user_address_roomDoubtful").empty();
                    $("#user_address_roomDoubtful").append($("<option value=''>$public_019</option>"));
                    for (var i = 0; i < rooms.length; i++) {
                        var room_name = rooms[i];
                        var str = "<option value='" + room_name + "'>" + room_name + "</option>";
                        $("#user_address_roomDoubtful").append($(str));
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
        $("#user_address_roomDoubtful").empty();
        $("#user_address_roomDoubtful").append($("<option value=''>$public_019</option>"));
    }
}

function load_communitiesCurrent() {
    $("#user_address_communityCurrent").empty();
    $("#user_address_communityCurrent").append($("<option value=''>$public_016</option>"));
    $("#user_address_buildingCurrent").empty();
    $("#user_address_buildingCurrent").append($("<option value=''>$public_017</option>"));
    $("#user_address_unitCurrent").empty();
    $("#user_address_unitCurrent").append($("<option value=''>$public_018</option>"));
    $("#user_address_roomCurrent").empty();
    $("#user_address_roomCurrent").append($("<option value=''>$public_019</option>"));
    jQuery.ajax({
        url: "../user/loadCommunity.do",
        type: "post",
        async: false,
        dataType: "json",
        success: function (result) {
            if (result.status === 1) {
                var communities = result.data;
                for (var i = 0; i < communities.length; i++) {
                    var community_name = communities[i].community_name;
                    var str = "<option value='" + community_name + "'>" + community_name + "</option>";
                    $("#user_address_communityCurrent").append($(str));
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

function findAllBuildingsCurrent() {
    var community_name = $("#user_address_communityCurrent option:selected").val().trim();
    if (community_name !== "") {
        jQuery.ajax({
            url: "../user/loadBuilding.do",
            type: "post",
            data: {
                "province": "",
                "city": "",
                "district": "",
                "community_name": community_name
            },
            dataType: "json",
            success: function (result) {
                if (result.status === 1) {
                    $("#user_address_buildingCurrent").empty();
                    $("#user_address_buildingCurrent").append($("<option value=''>$public_017</option>"));
                    $("#user_address_unitCurrent").empty();
                    $("#user_address_unitCurrent").append($("<option value=''>$public_018</option>"));
                    $("#user_address_roomCurrent").empty();
                    $("#user_address_roomCurrent").append($("<option value=''>$public_019</option>"));
                    var buildings = result.data;
                    for (var i = 0; i < buildings.length; i++) {
                        var building_name = buildings[i].building_name;
                        var str = "<option value='" + building_name + "'>" + building_name + "</option>";
                        $("#user_address_buildingCurrent").append($(str));
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
        $("#user_address_buildingCurrent").empty();
        $("#user_address_buildingCurrent").append($("<option value=''>$public_017</option>"));
        $("#user_address_unitCurrent").empty();
        $("#user_address_unitCurrent").append($("<option value=''>$public_018</option>"));
        $("#user_address_roomCurrent").empty();
        $("#user_address_roomCurrent").append($("<option value=''>$public_019</option>"));
    }
}

function findAllUnitsCurrent() {
    var community_name = $("#user_address_communityCurrent option:selected").val().trim();
    var building_name = $("#user_address_buildingCurrent option:selected").val().trim();
    if (building_name !== "") {
        jQuery.ajax({
            url: "../user/loadUnit.do",
            type: "post",
            data: {
                "province": "",
                "city": "",
                "district": "",
                "community_name": community_name,
                "building_name": building_name
            },
            dataType: "json",
            success: function (result) {
                if (result.status === 1) {
                    $("#user_address_unitCurrent").empty();
                    $("#user_address_unitCurrent").append($("<option value=''>$public_018</option>"));
                    $("#user_address_roomCurrent").empty();
                    $("#user_address_roomCurrent").append($("<option value=''>$public_019</option>"));
                    var units = result.data;
                    for (var i = 0; i < units.length; i++) {
                        var unit_name = units[i].unit_name;
                        var str = "<option value='" + unit_name + "'>" + unit_name + "</option>";
                        $("#user_address_unitCurrent").append($(str));
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
        $("#user_address_unitCurrent").empty();
        $("#user_address_unitCurrent").append($("<option value=''>$public_018</option>"));
        $("#user_address_roomCurrent").empty();
        $("#user_address_roomCurrent").append($("<option value=''>$public_019</option>"));
    }
}

//根据单元加载房号(从meter_user表内)
function findAllRoomsInUserCurrent() {
    var community_name = $("#user_address_communityCurrent option:selected").val().trim();
    var building_name = $("#user_address_buildingCurrent option:selected").val().trim();
    var unit_name = $("#user_address_unitCurrent option:selected").val().trim();
    if (unit_name != "") {
        jQuery.ajax({
            url: "../user/findRoomsByAddress.do",
            type: "post",
            data: {
                "community_name": community_name,
                "building_name": building_name,
                "unit_name": unit_name
            },
            dataType: "json",
            success: function (result) {
                if (result.status == 1) {
                    var rooms = result.data;
                    $("#user_address_roomCurrent").empty();
                    $("#user_address_roomCurrent").append($("<option value=''>$public_019</option>"));
                    for (var i = 0; i < rooms.length; i++) {
                        var room_name = rooms[i];
                        var str = "<option value='" + room_name + "'>" + room_name + "</option>";
                        $("#user_address_roomCurrent").append($(str));
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
        $("#user_address_roomCurrent").empty();
        $("#user_address_roomCurrent").append($("<option value=''>$public_019</option>"));
    }
}

