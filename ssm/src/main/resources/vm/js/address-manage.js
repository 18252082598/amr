//地址管理
$(function () {
    $("#admin").text(account_name);
    $("#sum-to-do").text("(" + toDoNo + ")");
    $("#exit").click(logout);
    load_Sumtodo();
    addressManagePermission();
    //加载小区信息
    load_community();
    //点击某一个小区，显示对应的楼栋信息
    $("#xiaoqu-list").on("click", "li:not(:last)", load_building);
    //点击某一个楼栋，显示对应的单元信息
    $("#lou-list").on("click", "li:not(:last)", load_unit);
    //修改小区信息
    $("#xiaoqu-list").on("click", ".glyphicon.glyphicon-pencil.list-button",
            modify_community);
    //删除小区信息
    $("#xiaoqu-list").on("click", ".glyphicon.glyphicon-trash.list-button",
            del_community);
    //添加小区信息
    $("#xiaoqu-list").on("click", "#xiaoqu-add", add_community);
    //修改楼栋信息
    $("#lou-list").on("click", ".glyphicon.glyphicon-pencil.list-button",
            modify_building);
    //删除楼栋信息
    $("#lou-list").on("click", ".glyphicon.glyphicon-trash.list-button",
            del_building);
    //添加楼栋信息
    $("#lou-list").on("click", "#lou-add", add_building);
    //修改单元信息
    $("#danyuan-list").on("click", ".glyphicon.glyphicon-pencil.list-button",
            modify_unit);
    //删除单元信息
    $("#danyuan-list").on("click", ".glyphicon.glyphicon-trash.list-button",
            del_unit);
    //添加单元信息
    $("#danyuan-list").on("click", "#danyuan-add", add_unit);

});

//加载小区信息
function load_community() {
    $("#xiaoqu-list li").remove();
    jQuery.ajax({
        url: "../user/loadCommunity.do",
        type: "post",
        dataType: "json",
        success: function (result) {
            if (result.status == 1) {
                var communities = result.data;
                for (var i = 0; i < communities.length; i++) {
                    var community = communities[i];
                    var community_id = community.community_id;
                    var province = community.province;
                    var city = community.city;
                    var district = community.district;
                    var community_no = community.community_no;
                    var community_name = community.community_name;
                    var str = "";
                    str += '<li id="li' + i + '" class="list-group-item"><span>' + district + ':</span><span>' + community_name + '</span><a href="#" class="remove-item" ><span class="glyphicon glyphicon-trash list-button" ></span></a><a href="#"><span class="glyphicon glyphicon-pencil list-button"></span></a></li>';
                    $("#xiaoqu-list").append($(str));
                    $("#li" + i + "").data("community_id", community_id);
                    $("#li" + i + "").data("province", province);
                    $("#li" + i + "").data("city", city);
                    $("#li" + i + "").data("district", district);
                    $("#li" + i + "").data("community_no", community_no);
                }
                $("#xiaoqu-list").append(
                        $('<li class="list-group-item"><select id="add1" class="form-control-inline"></select><select id="add2" class="form-control-inline"></select><select id="add3" class="form-control-inline"></select><button type="button" class="form-control-inline address_btn" id="xiaoqu-add">$address_manage_002</button>\n\
                            <input name="xiaoquname"  class="form-control-inline  pull-right" placeholder="$address_manage_001" id="xiaoquname" maxlength="40"></li>'));
                addressInit('add1', 'add2', 'add3', 'zhejiang', 'hangzhou', 'xihu');
                $("#xiaoqu-list li:eq(0)").click();
            }
        },
        error: function () {
            alert("error");
        }
    });
}

//根据小区名称，加载相应的楼栋信息
function load_building() {
    $("#xiaoqu-list li").css("background-color", "white");
    $(this).css("background-color", "#D2E9FF");
    var province = $(this).data("province");
    var city = $(this).data("city");
    var district = $(this).find("span:eq(0)").text().trim();
    district = district.substring(0, district.length - 1);
    var community_name = $(this).find("span:eq(1)").text().trim();
    $("#xiaoqu-list").data("community_name", community_name);
    $("#xiaoqu-list").data("province", province);
    $("#xiaoqu-list").data("city", city);
    $("#xiaoqu-list").data("district", district);
    $("#lou-list li").remove();
    $("#danyuan-list li").remove();
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
                var buildings = result.data;
                buildings.sort(function (a, b) {
                    return a.building_name < b.building_name ? -1 : 1
                });
                for (var i = 0; i < buildings.length; i++) {
                    var building = buildings[i];
                    var building_name = building.building_name;
                    var str = "";
                    str += '<li class="list-group-item"><span>' + building_name + '</span><a href="#" class="remove-item" ><span class="glyphicon glyphicon-trash list-button" ></span></a><a href="#"><span class="glyphicon glyphicon-pencil list-button" ></span></a></li>';
                    $("#lou-list").append($(str));
                }
                $("#lou-list").append(
                        $('<li class="list-group-item"><input name="louname" class="form-control-inline" placeholder="$address_manage_012" id="louname" maxlength="40"><button type="button" class="form-control-inline  address_btn" id="lou-add">$address_manage_002</button></li>'));
                var lastBuildingName = $("#lou-list li").eq(-2).find(
                        "span").text();
                /*         if (lastBuildingName == "") {
                 $("#louname")[0].value = "1��";
                 } else if (lastBuildingName.match(/^[1-9]\d*[��]$/)) {
                 var index = parseInt(lastBuildingName.substring(0,
                 lastBuildingName.indexOf("��")));
                 $("#louname")[0].value = (index + 1) + "��";
                 } else if (lastBuildingName.match(/^[1-9]\d*[��]$/)) {
                 var index = parseInt(lastBuildingName.substring(0,
                 lastBuildingName.indexOf("��")));
                 $("#louname")[0].value = (index + 1) + "��";
                 } else {
                 $("#louname")[0].value = "";
                 }     */
                $("#lou-list li:eq(0)").click();
            }
        },
        error: function () {
            alert("error");
        }
    });
}
//根据小区、楼栋名称，加载相应的单元信息
function load_unit() {
    $("#lou-list li").css("background-color", "white");
    $(this).css("background-color", "#D2E9FF");
    var province = $("#xiaoqu-list").data("province");
    var city = $("#xiaoqu-list").data("city");
    var district = $("#xiaoqu-list").data("district");
    var community_name = $("#xiaoqu-list").data("community_name");
    var building_name = $(this).find("span:eq(0)").text().trim();
    $("#lou-list").data("building_name", building_name);
    $("#danyuan-list li").remove();
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
                var units = result.data;
                for (var i = 0; i < units.length; i++) {
                    var unit = units[i];
                    var unit_name = unit.unit_name;
                    var str = "";
                    str += '<li class="list-group-item"><span>' + unit_name + '</span><a href="#" class="remove-item" ><span class="glyphicon glyphicon-trash list-button" ></span></a><a href="#"><span class="glyphicon glyphicon-pencil list-button" ></span></a></li>';
                    $("#danyuan-list").append($(str));
                }
                $("#danyuan-list").append(
                        $('<li class="list-group-item"><input name="danyuanname"class="form-control-inline" placeholder="$address_manage_003" id="danyuanname" maxlength="40"><button type="button" class="form-control-inline  address_btn" id="danyuan-add">$address_manage_002</button></li>'));
                var lastUnitName = $("#danyuan-list li").eq(-2).find(
                        "span").text();
            }
        },
        error: function () {
            alert("error");
        }
    });
}
//添加小区信息
function add_community() {
    var province = $("#add1 option:selected").val().trim();
    var city = $("#add2 option:selected").val().trim();
    var district = $("#add3 option:selected").val().trim();
    var community_no = $("#xiaoquname").val().trim();
    var community_name = $("#xiaoquname").val().trim();
    var operator_account = getCookie("account_name");
    var lis = $("#xiaoqu-list").find("li");
    var check = true;
    for (var i = 0; i < lis.length - 1; i++) {
        var preDistrict = $(lis[i]).children().eq(0).text().trim();
        preDistrict = preDistrict.substring(0, preDistrict.length - 1);
        var name = $(lis[i]).children().eq(1).text().trim();
        if (community_name == name && district == preDistrict) {
            check = false;
            break;
        }
    }
    if (check == false) {
        alert("$address_manage_006");
    } else if (district == "Please Select" || district == "$address_manage_019") {
        alert('$address_manage_018');
    } else if (community_name != "" && community_no != ""
            && community_name != "$address_manage_007" && check == true) {
        jQuery.ajax({
            url: "../user/addCommunity.do",
            type: "post",
            data: {
                "province": province,
                "city": city,
                "district": district,
                "community_no": community_no,
                "community_name": community_name,
                "operator_account": operator_account
            },
            dataType: "json",
            success: function (result) {
                if (result.status == 1) {
                    load_community();
                }else if(result.status === 1062){
                    alert("$address_manage_020");
                }else {
                    alert("$public_036");
                }
            },
            error: function () {
                alert("error");
            }
        });
    } else {
        alert("$address_manage_018");
    }
}
//修改小区信息
function modify_community() {
    var index = $(this).parents("ul").find("li").index($(this).parents("li"));
    var li = $("#li" + index + "");
    var community_id = li.data("community_id");
    var province = li.data("province");
    var city = li.data("city");
    var district = li.data("district");
    var community_name = li.children().eq(1).text().trim();
    var check = false;
    jQuery.ajax({
        url: "../user/checkCommunity.do",
        type: "post",
        async: false,
        data: {
            "community_name": community_name
        },
        dataType: "json",
        success: function (result) {
            if (result.status == 1) {
                check = true;
            }
        },
        error: function () {
            alert("error");
        }
    });
    // 如果小区有人住，则弹出提示信息
    if ((check == true && confirm("$adress_manage_012")) || check == false) {
        var input = $("<select id='xadd1" + index 
                + "' class='form-control-inline  select_add1'></select><select id='xadd2" + index 
                + "' class='form-control-inline select_add2'></select><select id='xadd3" + index 
                + "' class='form-control-inline select_add3'></select><button class='save  form-control-inline address_btn' type='submit'>$address_manage_009</button><input  class='form-control-inline  pull-right' value='"
                + community_name + "'maxlength='40'/>");
        li.html(input);
        addressInit('xadd1' + index + '', 'xadd2' + index + '', 'xadd3' + index + '', '' + province + '', '' + city + '', '' + district + '');
        $(".save").click(function () {
            var new_community_name = $(this).next().val().trim();
            var new_province = $("#xadd1" + index + " option:selected").val().trim();
            var new_city = $("#xadd2" + index + " option:selected").val().trim();
            var new_district = $("#xadd3" + index + " option:selected").val().trim();
            if (new_community_name == "") {
                alert("$address_manage_018");
            } else if (new_community_name != community_name) {
                jQuery.ajax({
                    url: "../user/modifyCommunity.do",
                    type: "post",
                    data: {
                        "check": check,
                        "community_id": community_id,
                        "province": province,
                        "city": city,
                        "district": district,
                        "community_name": community_name,
                        "new_province": new_province,
                        "new_city": new_city,
                        "new_district": new_district,
                        "new_community_name": new_community_name,
                        "new_community_no": new_community_name
                    },
                    dataType: "json",
                    success: function (result) {
                        if (result.status == 1) {
                            window.location.href = "$navigation_href_014";
                        }
                    },
                    error: function () {
                        alert("error");
                    }
                });
            } else {
                li.html("<span>" + district + ":</span><span>"
                        + community_name
                        + "</span><a class='remove-item' ><span class='glyphicon glyphicon-trash list-button'></span></a><a><span class='glyphicon glyphicon-pencil list-button'></span></a></li>");
            }
        });
    }
}

//删除小区信息
function del_community() {
    var li = $(this).parents("li");
    var province = li.data("province");
    var city = li.data("city");
    var district = li.children().eq(0).text().trim();
    district = district.substring(0, district.length - 1);
    var community_name = $(this).parents("li").find("span:eq(1)").text().trim();
    if (confirm("$address_manage_010")) {
        jQuery.ajax({
            url: "../user/delCommunity.do",
            type: "post",
            data: {
                "province": province,
                "city": city,
                "district": district,
                "community_name": community_name,
            },
            dataType: "json",
            success: function (result) {
                if (result.status == 1) {
                    load_community();
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

//添加楼栋信息
function add_building() {
    var building_name = $("#louname").val().trim();
    var community_name = $("#xiaoqu-list").data("community_name");
    var province = $("#xiaoqu-list").data("province");
    var city = $("#xiaoqu-list").data("city");
    var district = $("#xiaoqu-list").data("district");
    var operator_account = getCookie("account_name");
    var lis = $("#lou-list").find("li");
    var check = true;
    for (var i = 0; i < lis.length - 1; i++) {
        var name = $(lis[i]).children().eq(0).text().trim();
        if (building_name == name) {
            check = false;
            break;
        }
    }
    if (check == false) {
        alert("$address_manage_011");
    } //else if (building_name.match(/^([1-9]\d*|[\u4E00-\u9FA5]+)[]$/)
    else if (building_name != ""
            && building_name != "$address_manage_012" && check == true) {
        jQuery.ajax({
            url: "../user/addBuilding.do",
            type: "post",
            data: {
                "province": province,
                "city": city,
                "district": district,
                "community_name": community_name,
                "building_name": building_name,
                "operator_account": operator_account,
            },
            dataType: "json",
            success: function (result) {
                if (result.status == 1) {
                    var str = '<li class="list-group-item"><span>' + building_name + '</span><a class="remove-item" href="#"><span class="glyphicon glyphicon-trash list-button"></span></a><a href="#"><span class="glyphicon glyphicon-pencil list-button"></span></a></li>';
                    $("#lou-add").parents("li").before($(str));
                    var input = $("#louname").val().trim();
                    if (input.match(/^[1-9]\d*[$public_017]$/)) {
                        var index = parseInt(input.substring(0, input
                                .indexOf("$public_017")));
                        $("#louname")[0].value = (index + 1) + "$public_017";
                    } else if (input.match(/^[1-9]\d*[$public_018]$/)) {
                        var index = parseInt(input.substring(0, input
                                .indexOf("$public_018")));
                        $("#louname")[0].value = (index + 1) + "$public_018";
                    } else {
                        $("#louname")[0].value = "";
                    }
                }
            },
            error: function () {
                alert("error");
            }
        });
    } else {
        // alert("$address_manage_013");    
        alert("$address_manage_018");
    }
}

function modify_building() {
    var province = $("#xiaoqu-list").data("province");
    var city = $("#xiaoqu-list").data("city");
    var district = $("#xiaoqu-list").data("district");
    var community_name = $("#xiaoqu-list").data("community_name");
    var building_name = $(this).parents("li").children().eq(0).text().trim();
    var li = $(this).parents("li");
    li.html("<input class='form-control-inline' value='" + building_name
            + "' maxlength='40'/><button class='save  form-control-inline address_btn' type='submit'>$address_manage_009</button>");
    $(".save").click(
            function () {
                var new_building_name = $(this).prev().val().trim();
                if (new_building_name != building_name) {
                    if (new_building_name != "") {
                        jQuery.ajax({
                            url: "../user/modifyBuilding.do",
                            type: "post",
                            data: {
                                "province": province,
                                "city": city,
                                "district": district,
                                "community_name": community_name,
                                "building_name": building_name,
                                "new_building_name": new_building_name
                            },
                            dataType: "json",
                            success: function (result) {
                                if (result.status == 1) {
                                    li.html("<span>"
                                            + new_building_name
                                            + "</span><a class='remove-item' ><span class='glyphicon glyphicon-trash list-button'></span></a><a><span class='glyphicon glyphicon-pencil list-button'></span></a></li>");
                                } else {
                                    alert("$public_036");
                                }
                            },
                            error: function () {
                                alert("error");
                            }
                        });
                    } else {
                        alert("$address_manage_013");
                    }
                } else {
                    li.html("<span>"
                            + building_name
                            + "</span><a class='remove-item' ><span class='glyphicon glyphicon-trash list-button'></span></a><a><span class='glyphicon glyphicon-pencil list-button'></span></a></li>");
                }
            });
}

function del_building() {
    var province = $("#xiaoqu-list").data("province");
    var city = $("#xiaoqu-list").data("city");
    var district = $("#xiaoqu-list").data("district");
    var community_name = $("#xiaoqu-list").data("community_name");
    var building_name = $(this).parents("li").find("span:eq(0)").text().trim();
    if (confirm("$address_manage_010")) {
        $(this).parents("li").remove();
        $("#louname")[0].value = building_name;
        jQuery.ajax({
            url: "../user/delBuilding.do",
            type: "post",
            data: {
                "province": province,
                "city": city,
                "district": district,
                "community_name": community_name,
                "building_name": building_name,
            },
            dataType: "json",
            success: function (result) {
                if (result.status == 1) {
                    $("#danyuan-list li").remove();
                }
            },
            error: function () {
                alert("error");
            }
        });
    }
}

function add_unit() {
    var province = $("#xiaoqu-list").data("province");
    var city = $("#xiaoqu-list").data("city");
    var district = $("#xiaoqu-list").data("district");
    var community_name = $("#xiaoqu-list").data("community_name");
    var building_name = $("#lou-list").data("building_name");
    var unit_name = $("#danyuanname").val().trim();
    var operator_account = getCookie("account_name");
    var lis = $("#danyuan-list").find("li");
    var check = true;
    for (var i = 0; i < lis.length - 1; i++) {
        var name = $(lis[i]).children().eq(0).text().trim();
        if (unit_name == name) {
            check = false;
            break;
        }
    }
    if (check == false) {
        alert("$address_manage_014");
    } //else if (unit_name.match(/^[1-9]\d*(��Ԫ)$/)
    else if (unit_name != ""
            && unit_name != "$address_manage_003" && check == true) {
        jQuery.ajax({
            url: "../user/addUnit.do",
            type: "post",
            data: {
                "province": province,
                "city": city,
                "district": district,
                "community_name": community_name,
                "building_name": building_name,
                "unit_name": unit_name,
                "operator_account": operator_account,
            },
            dataType: "json",
            success: function (result) {
                if (result.status == 1) {
                    var str = '<li class="list-group-item"><span>' + unit_name + '</span><a class="remove-item" ><span class="glyphicon glyphicon-trash list-button"></span></a><a><span class="glyphicon glyphicon-pencil list-button"></span></a></li>';
                    $("#danyuan-add").parents("li").before($(str));
                    $("#danyuanname")[0].value = "";
                }
            },
            error: function () {
                alert("error");
            }
        });
    } else {
        alert("$address_manage_018");
    }
}

function modify_unit() {
    var province = $("#xiaoqu-list").data("province");
    var city = $("#xiaoqu-list").data("city");
    var district = $("#xiaoqu-list").data("district");
    var community_name = $("#xiaoqu-list").data("community_name");
    var building_name = $("#lou-list").data("building_name");
    var unit_name = $(this).parents("li").children().eq(0).text().trim();
    var li = $(this).parents("li");
    li.html("<input class='form-control-inline' value='" + unit_name + "'maxlength='40'/><button class='save form-control-inline  address_btn' type='submit'>$address_manage_009</button>");
    $(".save").click(function () {
        var new_unit_name = $(this).prev().val().trim();
        if (new_unit_name != unit_name) {
            if (new_unit_name != "") {
                jQuery.ajax({
                    url: "../user/modifyUnit.do",
                    type: "post",
                    data: {
                        "province": province,
                        "city": city,
                        "district": district,
                        "community_name": community_name,
                        "building_name": building_name,
                        "unit_name": unit_name,
                        "new_unit_name": new_unit_name
                    },
                    dataType: "json",
                    success: function (result) {
                        if (result.status == 1) {
                            li.html("<span>" + new_unit_name + "</span><a class='remove-item' ><span class='glyphicon glyphicon-trash list-button'></span></a><a><span class='glyphicon glyphicon-pencil list-button'></span></a></li>");
                        } else {
                            alert("$public_036");
                        }
                    },
                    error: function () {
                        alert("error");
                    }
                });
            } else {
                alert("$address_manage_015");
            }
        } else {
            li.html("<span>" + unit_name + "</span><a class='remove-item' ><span class='glyphicon glyphicon-trash list-button'></span></a><a><span class='glyphicon glyphicon-pencil list-button'></span></a></li>");
        }
    });
}

function del_unit() {
    var province = $("#xiaoqu-list").data("province");
    var city = $("#xiaoqu-list").data("city");
    var district = $("#xiaoqu-list").data("district");
    var community_name = $("#xiaoqu-list").data("community_name");
    var building_name = $("#lou-list").data("building_name");
    var unit_name = $(this).parents("li").find("span:eq(0)").text().trim();
    if (confirm("$address_manage_010")) {
        $(this).parents("li").remove();
        $("#danyuanname")[0].value = unit_name;
        jQuery.ajax({
            url: "../user/delUnit.do",
            type: "post",
            data: {
                "province": province,
                "city": city,
                "district": district,
                "community_name": community_name,
                "building_name": building_name,
                "unit_name": unit_name,
            },
            dataType: "json",
            success: function (result) {
                if (result.status == 1) {
                }
            },
            error: function () {
                alert("error");
            }
        });
    }
}

