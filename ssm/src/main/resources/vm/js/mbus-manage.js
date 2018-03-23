//集中器管理
$(function () {
    $("#admin").text(account_name);
    $("#sum-to-do").text("(" + toDoNo + ")");
    $("#exit").click(logout);
    load_Sumtodo();
    mbusManagePermission();
    //加载集中器信息
    load_concentrators();
    //查询集中器的在线状态
    checkMbusOnline();
    //下一页
    $("#span1").click(nextPage);
    //上一页
    $("#span2").click(prePage);
    //第一页
    $("#span3").click(firstPage);
    //最后一页
    $("#span4").click(lastPage);
    // 按下Enter键，实现页面跳转
    $(".input-page").keypress(function (event) {
        var keycode = (event.keyCode ? event.keyCode : event.which);
        if (keycode == 13) {
            var pageIndex = $(this).val().trim();
            pageIndex = parseInt(pageIndex);
            var totalNo = parseInt($("#t1").data("concentrators.length"));
            var pageSize = $("#pageSize option:selected").val().trim();
            var pageNo = parseInt(totalNo / pageSize);
            var pageMax = totalNo % pageSize == 0 ? pageNo : pageNo + 1;
            if (pageIndex <= 0) {
                alert("$public_038");
            } else if (pageIndex > pageMax) {
                alert("$public_039");
            } else {
                document.getElementById("pageIndex").value = pageIndex;
                load_concentrators();
            }
            return false;
        }
    });
    //添加集中器	
    $("#add_concentrator").click(add_concentrator);
    //修改集中器信息
    $("#t1").on("click", ".glyphicon.glyphicon-pencil.list-button", modify);
    //删除集中器信息
    $("#t1").on("click", ".glyphicon.glyphicon-trash.list-button", del);
    //清空之前填写的信息
    $("#add").click(clear);
    /*****添加集中器change事件****/
    $('#community').change(fun_community);
    $('#building').change(fun_community);
    $('#unit').change(fun_community);
});
/*********集中器下拉框事件******/
function fun_community() {
    var obj = $('#community').parent();
    var $tip = $('<label  class="formtips"></label>');
    obj.find('.formtips').remove();
    var select0 = $('#community  option:selected');
    var select1 = $('#building  option:selected');
    var select2 = $('#unit  option:selected');
    if (select0.val().trim() != '' &&
            select1.val().trim() != '' &&
            select2.val().trim() != '') {
        $tip.addClass('glyphicon  glyphicon-ok').text('').css('color', '#666');
    } else {
        $tip.addClass('onError').$mbus_manage_032;
    }
    obj.append($tip);
}
//加载集中器信息
function load_concentrators() {
    $("#t1 tbody tr").remove();
    var pageNo = $("#pageIndex").val().trim();
    var pageSize = $("#pageSize option:selected").val().trim();
    var startNo = (pageNo - 1) * pageSize;
    jQuery.ajax({
        url: "../user/loadConcentrators.do",
        type: "post",
        data: {
            "community_name": "",
            "building_name": "",
            "unit_name": "",
            "startNo": startNo,
            "pageSize": pageSize
        },
        dataType: "json",
        async: false,
        success: function (result) {
            if (result.status == 1) {
                var map = result.data;
                var concentrators = map["concentrators"];
                if (concentrators == null) {
                    return;
                }
                var totalNo = map["totalNo"];
                var page = parseInt(totalNo / pageSize);
                var pageMax = totalNo % pageSize === 0 ? page : page + 1;
                $("#span4").next().text(pageMax);
                $("#t1").data("totalNo", totalNo);
                for (var i = 0; i < concentrators.length; i++) {
                    var concentrator = concentrators[i];
                    var concentrator_id = concentrator.concentrator_id;
                    var concentrator_name = concentrator.concentrator_name;
                    var concentrator_no = concentrator.concentrator_no;
                    var gateway_id = concentrator.gateway_id;
                    var concentrator_ip = concentrator.concentrator_ip;
                    var concentrator_port = concentrator.concentrator_port;
                    var DTU_sim_no = concentrator.dtu_sim_no;
                    var concentrator_model = concentrator.concentrator_model;
                    //默认集中器模式为安装状态
                    var concentrator_model1 = "$public_068";
                    //数据库中的concentrator_model状态值为0,1,2,3，其中0代表安装状态，1代表调试状态
                    //                                                 2代表启用状态，3代表关闭状态
                    if (concentrator_model == '0') {
                        concentrator_model1 = "$public_068";
                    } else if (concentrator_model == '1') {
                        concentrator_model1 = "$public_069";
                    } else if (concentrator_model == '2') {
                        concentrator_model1 = "$public_070";
                    } else {
                        concentrator_model1 = "$public_071";
                    }
//                    var concentrator_status = concentrator.concentrator_state;
                    var user_address_community = concentrator.user_address_community;
                    var user_address_building = concentrator.user_address_building;
                    var user_address_unit = concentrator.user_address_unit;
                    var add_time = concentrator.add_time;
                    add_time = format(add_time, 'yyyy.MM.dd HH:mm:ss');
                    var operator_name = concentrator.operator_account;
                    var str = "";
                    str += '<tr><td>' + (i + 1 + startNo) + '</td>';
                    str += '<td>' + concentrator_name + '</td>';
                    str += '<td>' + concentrator_no + '</td>';
                    str += '<td>' + gateway_id + '</td>';
                    str += '<td>' + DTU_sim_no + '</td>';
                    str += '<td>' + concentrator_model1 + '</td>';
//                    if (concentrator_status == 1) {
//                        str += '<td><span class="glyphicon glyphicon-ok"></span></td>';
//                    } else {
//                        str += '<td><span class="glyphicon glyphicon-remove"></span></td>';
//                    }
                    str += '<td><img src="../images/all/loading_icon_u478.gif"></td>';
                    str += '<td>' + user_address_community + '</td>';
                    str += '<td>' + user_address_building + '</td>';
                    str += '<td>' + user_address_unit + '</td>';
                    str += '<td>' + add_time + '</td>';
                    str += '<td>' + operator_name + '</td>';
                    str += '<td><a href="#"><span class="glyphicon glyphicon-pencil list-button" ></span> </a><a href="#"><span class="glyphicon glyphicon-trash list-button" ></span> </a></td></tr>';
                    $("#t1 tbody").append($(str));
                    $("#t1").data("concentrator_id" + (i + 1 + startNo), concentrator_id);
                    $("#t1").data("concentrator_name" + (i + 1 + startNo), concentrator_name);
                    $("#t1").data("concentrator_no" + (i + 1 + startNo), concentrator_no);
                    $("#t1").data("gateway_id" + (i + 1 + startNo), gateway_id);
                    $("#t1").data("concentrator_ip" + (i + 1 + startNo), concentrator_ip);
                    $("#t1").data("concentrator_port" + (i + 1 + startNo), concentrator_port);
                    $("#t1").data("DTU_sim_no" + (i + 1 + startNo), DTU_sim_no);
                    $("#t1").data("concentrator_model1" + (i + 1 + startNo), concentrator_model1);
                    $("#t1").data("user_address_community" + (i + 1 + startNo), user_address_community);
                    $("#t1").data("user_address_building" + (i + 1 + startNo), user_address_building);
                    $("#t1").data("user_address_unit" + (i + 1 + startNo), user_address_unit);
                }
            }
        },
        error: function () {
            alert("error");
        }
    });
}

function checkMbusOnline() {
    if ($("#t1 tbody tr").length == 0) {
        return;
    }
    var check = setInterval(function () {
        $("#t1 tbody tr").each(function () {
            var $td = $(this).find("td").eq(6);
            var index = $(this).find("td").eq(0).text().trim();
            var mbus_id = $(this).find("td").eq(2).text().trim();
            var mbus_ip = $("#t1").data("concentrator_ip" + index);
            var mbus_port = $("#t1").data("concentrator_port" + index);
            var mbus_status = "0";
            var url = "http://" + mbus_ip + ":" + mbus_port + "/arm/api/online";
            $td.html('<img src="../images/all/loading_icon_u478.gif">');
            // 查询集中器的在线状态
            jQuery.ajax({
                url: url,
                type: "post",
                data: mbus_id,
                dataType: "text",
                //async: false,
                success: function (result) {
                    // String --> JSON
                    var jsonResult = eval("(" + result + ")");
                    mbus_status = jsonResult.result;
                    console.log(mbus_id + ":" + mbus_status);
                    if (mbus_status == 0) {
                        $td.html('<span class="glyphicon glyphicon-ok"></span>');
                    } else {
                        $td.html('<span class="glyphicon glyphicon-remove"></span>');
                    }
                },
                error: function () {
                    $td.html('<span class="glyphicon glyphicon-remove"></span>');
                }
            });

        });
    }, 60 * 1000);
}
//显示下一页
function nextPage() {
    var pageIndex = $("#pageIndex").val().trim();
    pageIndex = parseInt(pageIndex) + 1;
    var totalNo = parseInt($("#t1").data("totalNo"));
    var pageSize = $("#pageSize option:selected").val().trim();
    var pageNo = parseInt(totalNo / pageSize);
    var pageMax = totalNo % pageSize == 0 ? pageNo : pageNo + 1;
    if (pageIndex <= 0) {
        alert("$public_038");
    } else if (pageIndex > pageMax) {
        alert("$public_039");
    } else {
        document.getElementById("pageIndex").value = pageIndex;
        load_concentrators();
    }
}
//显示上一页
function prePage() {
    var pageIndex = $("#pageIndex").val().trim();
    pageIndex = parseInt(pageIndex) - 1;
    if (pageIndex <= 0) {
        alert("$public_040");
    } else {
        $("#pageIndex")[0].value = pageIndex;
        load_concentrators();
    }
}
//显示第一页
function firstPage() {
    $("#pageIndex")[0].value = 1;
    load_concentrators();
}
//显示最后一页
function lastPage() {
    var totalNo = parseInt($("#t1").data("totalNo"));
    var pageSize = $("#pageSize option:selected").val().trim();
    var pageNo = parseInt(totalNo / pageSize);
    var pageEnd = totalNo % pageSize == 0 ? pageNo : pageNo + 1;
    document.getElementById("pageIndex").value = pageEnd;
    load_concentrators();
}

//修改集中器信息
function modify() {
    var concentrator_index = $(this).parents("tr").find("td:eq(0)").text().trim();
    $("#t1").data("concentrator_index", concentrator_index);
    var concentrator_name = $(this).parents("tr").find("td:eq(1)").text().trim();
    var concentrator_no = $(this).parents("tr").find("td:eq(2)").text().trim();
    var gateway_id = $(this).parents("tr").find("td:eq(3)").text().trim();
    var concentrator_ip = $("#t1").data("concentrator_ip" + concentrator_index);
    var concentrator_port = $("#t1").data("concentrator_port" + concentrator_index);
    var DTU_sim_no = $(this).parents("tr").find("td:eq(4)").text().trim();
    var concentrator_model = $(this).parents("tr").find("td:eq(5)").text().trim();
    var community = $(this).parents("tr").find("td:eq(7)").text().trim();
    var building = $(this).parents("tr").find("td:eq(8)").text().trim();
    var unit = $(this).parents("tr").find("td:eq(9)").text().trim();
    $("#concentrator_name")[0].value = concentrator_name;
    $("#concentrator_no")[0].value = concentrator_no;
    $("#gateway_id")[0].value = gateway_id;
    $("#concentrator_ip")[0].value = concentrator_ip;
    $("#concentrator_port")[0].value = concentrator_port;
    $("#sim_no")[0].value = DTU_sim_no;
    $("#concentrator_model").find("option[value='" + concentrator_model + "']").attr("selected", true);
    findAllCommunities();
    $("#community option[value='" + community + "']").attr("selected", true);
    findAllBuildings();
    $("#building option[value='" + building + "']").attr("selected", true);
    findAllUnits();
    $("#unit option[value='" + unit + "']").attr("selected", true);
}

//清空之前填写的信息
function clear() {
    document.getElementById("concentrator_name").value = "";
    document.getElementById("sim_no").value = "";
    $("#add_concentrator").text("$public_054");
    $("#s1").text("");
    $("#s2").text("");
    $("#s3").text("");
    findAllCommunities();
}
//加载小区选项信息
function findAllCommunities() {
    jQuery.ajax({
        url: "../user/loadCommunity.do",
        type: "post",
        async: false,
        dataType: "json",
        success: function (result) {
            $("#community").empty();
            $("#community").append($("<option value=''>$public_016</option>"));
            $("#building").empty();
            $("#building").append($("<option value=''>$public_017</option>"));
            $("#unit").empty();
            $("#unit").append($("<option value=''>$public_018</option>"));
            var communities = result.data;
            for (var i = 0; i < communities.length; i++) {
                var community_name = communities[i].community_name;
                var str = "<option value='" + community_name + "'>" + community_name + "</option>";
                $("#community").append($(str));
            }
        },
        error: function () {
            alert("error");
        }
    });
}

//根据小区名称查找楼栋信息
function findAllBuildings() {
    var community_name = $("#community option:selected").val().trim();
    if (community_name != "") {
        jQuery.ajax({
            url: "../user/loadBuilding.do",
            type: "post",
            async: false,
            data: {
                "province": "",
                "city": "",
                "district": "",
                "community_name": community_name
            },
            dataType: "json",
            success: function (result) {
                $("#building").empty();
                $("#building").append($("<option value=''>$public_017</option>"));
                $("#unit").empty();
                $("#unit").append($("<option value=''>$public_018</option>"));
                var buildings = result.data;
                for (var i = 0; i < buildings.length; i++) {
                    var building_name = buildings[i].building_name;
                    var str = "<option value='" + building_name + "'>" + building_name + "</option>";
                    $("#building").append($(str));
                }
            },
            error: function () {
                alert("error");
            }
        });
    } else {
        $("#building").empty();
        $("#building").append($("<option value=''>$public_017</option>"));
        $("#unit").empty();
        $("#unit").append($("<option value=''>$public_018</option>"));
    }
}

//根据楼栋名称查找单元信息
function findAllUnits() {
    var community_name = $("#community option:selected").val().trim();
    var building_name = $("#building option:selected").val().trim();
    if (community_name != "" && building_name != "") {
        jQuery.ajax({
            url: "../user/loadUnit.do",
            type: "post",
            async: false,
            data: {
                "province": "",
                "city": "",
                "district": "",
                "community_name": community_name,
                "building_name": building_name
            },
            dataType: "json",
            success: function (result) {
                $("#unit").empty();
                $("#unit").append($("<option value=''>$public_018</option>"));
                var units = result.data;
                for (var i = 0; i < units.length; i++) {
                    var unit_name = units[i].unit_name;
                    var str = "<option value='" + unit_name + "'>" + unit_name + "</option>";
                    $("#unit").append($(str));
                }
            },
            error: function () {
                alert("error");
            }
        });
    } else {
        $("#unit").empty();
        $("#unit").append($("<option value=''>$public_018</option>"));
    }
}

//添加或者修改集中器信息
function add_concentrator() {
    var concentrator_name = $("#concentrator_name").val().trim();
    var concentrator_no = $("#concentrator_no").val().trim();
    var gateway_id = $("#gateway_id").val().trim();
    var concentrator_ip = $("#concentrator_ip").val().trim();
    var concentrator_port = $("#concentrator_port").val().trim();
    var DTU_sim_no = $("#sim_no").val().trim();
    //获取前端显示的集中器模式
    var concentrator_model1 = $("#concentrator_model option:selected").val();
    //默认发送到后端的模式值为0
    var concentrator_model = "0";
    //当前端模式为安装时，设置模式值为0，发送到后端
    if (concentrator_model1 == '$public_068') {
        concentrator_model = "0";
    } else if (concentrator_model1 == '$public_069') {
        concentrator_model = "1";
    } else if (concentrator_model1 == '$public_070') {
        concentrator_model = "2";
    } else {
        concentrator_model = "3";
    }
    var user_address_community = $("#community option:selected").val().trim();
    var user_address_building = $("#building option:selected").val().trim();
    var user_address_unit = $("#unit option:selected").val().trim();
    var operator_account = getCookie("account_name");
    var check = true;
    $('#mbus-add  form input.required').trigger('blur');
    fun_community();
    var numError = $('form .onError').length;
    if (numError > 0
            || user_address_community == ""
            || user_address_building == ""
            || user_address_unit == ""
            ) {
        alert("$mbus_manage_022");
        check = false;
        return;
    }
    if (check) {
        var operate_type = $("#add_concentrator").text().trim();
        if (operate_type == "$public_054") {
            $.ajax({
                url: "../user/addConcentratorValidate.do",
                type: "post",
                data: {
                    "concentrator_no": concentrator_no
                },
                dataType: "json",
                success: function (result) {
                    var status = result.status;
                    //表示数据库中不存在该集中器编号,添加该集中器
                    if (status == 1) {
                        jQuery.ajax({
                            url: "../user/addConcentrator.do",
                            type: "post",
                            data: {
                                "concentrator_name": concentrator_name,
                                "concentrator_no": concentrator_no,
                                "gateway_id": gateway_id,
                                "concentrator_ip": concentrator_ip,
                                "concentrator_port": concentrator_port,
                                "DTU_sim_no": DTU_sim_no,
                                "concentrator_model": concentrator_model,
                                "user_address_community": user_address_community,
                                "user_address_building": user_address_building,
                                "user_address_unit": user_address_unit,
                                "operator_account": operator_account
                            },
                            dataType: "json",
                            success: function (result) {
                                if (result.status == 1) {
                                    $("#add_cancel").click();
                                    //alert(result.msg);
                                    load_concentrators();
                                    alert("$public_051");
                                } else if (result.status == 2) {
                                    alert("$mbus_manage_034");
                                } else {
                                    alert("$public_036");
                                }
                            },
                            error: function () {
                                alert("error");
                            }
                        });
                    } else if (status == 0) {
                        //如果数据库中已经存在该集中器编号，弹出框弹出信息，让用户自主决定
                        //是否添加该集中器，如果添加，则该集中器模式默认设置为已经存在的集中器模式值        
                        var boolean = confirm("$public_072");
                        if (boolean) {
                            jQuery.ajax({
                                url: "../user/addConcentrator.do",
                                type: "post",
                                data: {
                                    "concentrator_name": concentrator_name,
                                    "concentrator_no": concentrator_no,
                                    "gateway_id": gateway_id,
                                    "concentrator_ip": concentrator_ip,
                                    "concentrator_port": concentrator_port,
                                    "DTU_sim_no": DTU_sim_no,
                                    "concentrator_model": concentrator_model,
                                    "user_address_community": user_address_community,
                                    "user_address_building": user_address_building,
                                    "user_address_unit": user_address_unit,
                                    "operator_account": operator_account
                                },
                                dataType: "json",
                                success: function (result) {
                                    if (result.status == 1) {
                                        $("#add_cancel").click();
                                        //alert(result.msg);
                                        load_concentrators();
                                        alert("$public_051");
                                    } else if (result.status == 2) {
                                        alert("$mbus_manage_034");
                                    } else {
                                        alert("$public_036");
                                    }
                                },
                                error: function () {
                                    alert("error");
                                }
                            });
                        } else {
                            return false;
                        }
                    } else {
                        alert("$public_067");
                    }
                }, error: function () {
                    alert("error");
                }
            });

        } else if (operate_type == "$mbus_manage_011") {
            var concentrator_index = $("#t1").data("concentrator_index");
            var concentrator_id = $("#t1").data("concentrator_id" + concentrator_index);
            var original_concentrator_name = $("#t1").data("concentrator_name" + concentrator_index);
            var original_concentrator_no = $("#t1").data("concentrator_no" + concentrator_index);
            var original_gateway_id = $("#t1").data("gateway_id" + concentrator_index);
            var original_DTU_sim_no = $("#t1").data("DTU_sim_no" + concentrator_index);
            //获取修改前集中器模式的值
            var original_concentrator_model1 = $("#t1").data("concentrator_model1" + concentrator_index);
            //默认发送到后端修改前集中器的值为0
            var original_concentrator_model = "0";
            //当前端集中器模式为调试模式时，设置发送到后端的集中器模式值为1
            if (original_concentrator_model1 == "$public_068") {
                original_concentrator_model = "0";
            } else if (original_concentrator_model1 == '$public_069') {
                original_concentrator_model = "1";
            } else if (original_concentrator_model1 == '$public_070') {
                original_concentrator_model = "2";
            } else {
                original_concentrator_model = "3";
            }
            var original_concentrator_ip = $("#t1").data("concentrator_ip" + concentrator_index);
            var original_concentrator_port = $("#t1").data("concentrator_port" + concentrator_index);
            var original_community = $("#t1").data("user_address_community" + concentrator_index);
            var original_building = $("#t1").data("user_address_building" + concentrator_index);
            var original_unit = $("#t1").data("user_address_unit" + concentrator_index);
            jQuery.ajax({
                url: "../user/modifyConcentrator.do",
                type: "post",
                data: {
                    "original_concentrator_name": original_concentrator_name,
                    "original_concentrator_no": original_concentrator_no,
                    "original_gateway_id": original_gateway_id,
                    "original_concentrator_ip": original_concentrator_ip,
                    "original_concentrator_port": original_concentrator_port,
                    "original_DTU_sim_no": original_DTU_sim_no,
                    "original_concentrator_model": original_concentrator_model,
                    "original_community": original_community,
                    "original_building": original_building,
                    "original_unit": original_unit,
                    "concentrator_id": concentrator_id,
                    "concentrator_name": concentrator_name,
                    "concentrator_no": concentrator_no,
                    "gateway_id": gateway_id,
                    "concentrator_ip": concentrator_ip,
                    "concentrator_port": concentrator_port,
                    "DTU_sim_no": DTU_sim_no,
                    "concentrator_model": concentrator_model,
                    "user_address_community": user_address_community,
                    "user_address_building": user_address_building,
                    "user_address_unit": user_address_unit,
                    "operator_account": operator_account
                },
                dataType: "json",
                success: function (result) {
                    if (result.status == 1) {
                        $("#add_cancel").click();
                        load_concentrators();
                        alert("$public_073");
                    } else if (result.status == 2) {
                        alert("$mbus_manage_034")
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
}

//删除集中器信息
function del() {
    var concentrator_index = $(this).parents("tr").find("td:eq(0)").text().trim();
    var concentrator_id = $("#t1").data("concentrator_id" + concentrator_index);
    var concentrator_name = $(this).parents("tr").find("td:eq(1)").text().trim();
    if (confirm("$public_056")) {
        jQuery.ajax({
            url: "../user/deleteConcentrator.do",
            type: "post",
            data: {
                "concentrator_id": concentrator_id,
                "concentrator_name": concentrator_name
            },
            dataType: "json",
            success: function (result) {
                if (result.status == 1) {
                    alert("$public_050");
                    load_concentrators();
                } else if (result.status == 2) {
                    alert("$mbus_manage_031");
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
