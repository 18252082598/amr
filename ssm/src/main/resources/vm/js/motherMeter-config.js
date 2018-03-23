$(function () {
    $("#admin").text(account_name);
    $("#sum-to-do").text("(" + toDoNo + ")");
    $("#exit").click(logout);
    load_Sumtodo();
    findMotherMeterConf();
    loadUpDownForm();
    $("#add_MotherMeterConf").click(clear);
    $("#addCommit").click(regist);
    $("#findMeterConfig").click(findMotherMeterConfBefore);
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
    //修改按钮
    $("#modify_motherMeterConf").click(modify_motherMeter);
    $("#modify_motherMeter_confirm").click(modify_motherMeter_confirm);
    //删除按钮
    $("#delete_motherMeterConf").click(delete_motherMeterConf);
    $("#delCommit").click(delCommit);
    $("#fileDown").click(downLoadConfExcel);
    $("#privateMeter").click(allShareTpye);
    $("#allotType").click(modifyShareTpye);
    $("#publicMeter").click(publicMeter);
    $("#modifyPub").click(modifyPub);
    $("#modifyPri").click(modifyPri);
});

var loadPage = function () {
    findMotherMeterConf();
};
function findMotherMeterConfBefore() {
    $(".input-page").val(1);
    findMotherMeterConf();
}
//加载用户
function findMotherMeterConf() {
    //按照表号查询用户
    var pageNo = $(".input-page").val().trim();
    var pageSize = parseInt($(".pageSize option:selected").val().trim());
    var meter_no = $("#meter_no").val().trim();
    var startNo = (pageNo - 1) * pageSize;
    jQuery.ajax({
        url: "../user/findMotherMeterConf.do",
        type: "post",
        data: {
            "meter_no": meter_no,
            "startNo": startNo,
            "pageSize": pageSize
        },
        dataType: "json",
        success: function (result) {
            if (result.status === 1) {
                var map = result.data;
                var motherMeterConfs = map["motherMeterConfs"];
                var totalNo = map["totalNo"];
                $("#totalNo").text(totalNo);
                var page = parseInt(totalNo / pageSize);
                var pageMax = totalNo % pageSize === 0 ? page : page + 1;
                $(".span4").next().text(pageMax);
                $("#t1").data("totalNo", totalNo);
                $("#t1 tbody tr").remove();
                for (var i = 0; i < motherMeterConfs.length; i++) {
                    var index = startNo + (i + 1);
                    var motherMeterConf = motherMeterConfs[i];
                    var id = motherMeterConf.id;
                    var meter_no = motherMeterConf.meter_no;
                    var meter_type = motherMeterConf.meter_type;
                    var allot_type = motherMeterConf.allot_type;
                    var add_time = motherMeterConf.add_time;
                    add_time = format(add_time, 'yyyy.MM.dd HH:mm:ss');
                    var operator_account = motherMeterConf.operator_account;
                    $("#t1").data("id" + index, id);
                    $("#t1").data("meter_no" + index, meter_no);
                    $("#t1").data("meter_type" + index, meter_type);
                    $("#t1").data("allot_type" + index, allot_type);
                    if (meter_type === "0") {
                        meter_type = "$mother_meter_confg_009";
                    } else {
                        meter_type = "$mother_meter_confg_010";
                    }
                    switch (allot_type) {
                        case "0":
                            allot_type = "$mother_meter_confg_011";
                            break;
                        case "1":
                            allot_type = "$mother_meter_confg_012";
                            break;
                        case "2":
                            allot_type = "$mother_meter_confg_013";
                            break;
                        case "3":
                            allot_type = "$parent_meter_confg_001";
                            break;
                    }
                    var str = "";
                    str += '<tr><td><input type="checkbox" value="' + id + '"></td>';
                    str += '<td>' + index + '</td>';
                    str += '<td>' + meter_no + '</td>';
                    str += '<td>' + meter_type + '</td>';
                    str += '<td>' + allot_type + '</td>';
                    str += '<td>' + add_time + '</td>';
                    str += '<td>' + operator_account + '</td></tr>';
                    $("#t1 tbody").append(str);
                }
            }
        },
        error: function () {
            alert("error");
        }
    });
}

//清空上一次注册信息
function clear() {
    $("#addMeterNo")[0].value = "";
    $("#motherType input[value='0']").attr("checked", true);
    $("#allotType input[value='0']").attr("checked", true);
}

//注册用户
function regist() {
    var meter_no = $("#addMeterNo").val().trim();
    var meter_type = $("#addMotherType input[type='radio']:checked").val();
    var allot_type = $("#addAllotType input[type='radio']:checked").val();
    var operator_account = getCookie("account_name");
    var check = true;
    if (!/^[0-9]{8,12}$/.test(meter_no)) {
        alert("请输入正确的表号");
        check = false;
    }
    if (check) {
        jQuery.ajax({
            url: "../user/addMotherMeterConf.do",
            type: "post",
            data: {
                "meter_no": meter_no,
                "meter_type": meter_type,
                "allot_type": allot_type,
                "operator_account": operator_account
            },
            dataType: "json",
            success: function (result) {
                if (result.status === 1) {
                    alert("$public_037");
                    $('#tab-register').modal('hide');
                    window.location.reload(true);
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

//用户信息修改按钮
function modify_motherMeter() {
    var userNo = $("#t1 tbody :checked").length;
    if (userNo === 0) {
        alert("$mother_meter_confg_015");
    } else if (userNo > 1) {
        alert("$mother_meter_confg_014");
    } else if (userNo === 1) {
        $("#modifyMeterInfo").modal('show');
    }
    $("#t1 tbody :checked").each(function () {
        var index = $(this).parents("tr").find("td:eq(1)").text().trim();
        var meter_no = $("#t1").data("meter_no" + index);       
        var meter_no = $("#t1").data("meter_no" + index);
        $("#modifyMeterNo")[0].value = meter_no;
        var meter_type = $("#t1").data("meter_type" + index);
        var allot_type = $("#t1").data("allot_type" + index);
        if (meter_type === "0") {
            document.getElementById("perHead").style.display = "none";
            document.getElementById("ratio").style.display = "none";
            document.getElementById("blower").style.display = "none";
        } else if (meter_type === "1") {
            document.getElementById("blower").style.display = "";
            document.getElementById("perHead").style.display = "";
            document.getElementById("ratio").style.display = "";
        }
        $("#motherType input[value='" + meter_type + "']").attr("checked", false);//去除已有的checked
        $("#allotType input[value='" + allot_type + "']").attr("checked", false);
        $("#motherType input[value='" + meter_type + "']").prop("checked", true);
        $("#allotType input[value='" + allot_type + "']").prop("checked", true);
    });
}

//用户信息修改确认
function modify_motherMeter_confirm() {
    var index = $("#t1 tbody :checked").parents("tr").find("td:eq(1)").text().trim();
    var check = true;
    var id = $("#t1").data("id" + index);
    var meter_no = $("#modifyMeterNo")[0].value;
    if (!/^[0-9]{8,12}$/.test(meter_no)) {
        alert("请输入正确的表号");
        check = false;
    }
    var meter_type = $("#motherType input[type='radio']:checked").val();
    var allot_type = $("#allotType input[type='radio']:checked").val();
    var org_meter_no = $("#t1").data("meter_no" + index);
    var org_meter_type = $("#t1").data("meter_type" + index);
    var org_allot_type = $("#t1").data("allot_type" + index);
    if (check) {
        jQuery.ajax({
            url: "../motherMeterConf/modifyMotherMeterConfirm.do",
            type: "post",
            data: {
                "id": id,
                "meter_no": meter_no,
                "meter_type": meter_type,
                "allot_type": allot_type,
                "org_meter_no": org_meter_no,
                "org_meter_type": org_meter_type,
                "org_allot_type": org_allot_type
            },
            dataType: "json",
            success: function (result) {
                if (result.status === 1) {
                    $("#modifyMeterInfo").modal('hide');
                    alert("$public_041");
                    window.location.reload(true);
                } else if (result.status === 1062) {
                    alert("$user_manage_141");
                } else {
                    alert("$public_035");
                }
            },
            error: function () {
                alert("error");
            }
        });
    }
}

//删除母表配置参数按钮
function delete_motherMeterConf() {
    var userNo = $("#t1 tbody :checked").length;
    if (userNo === 0) {
        alert("请选择一个");
    } else if (userNo > 1) {
        alert("只能选择一个");
    } else if (userNo === 1) {
        $("#delete-motherConf").modal('show');
    }
    $("#t1 tbody :checked").each(function () {
        var index = $(this).parents("tr").find("td:eq(1)").text();
        var mother_no = $(this).parents("tr").find("td:eq(2)").text();
        var id = $("#t1").data("user_id" + index);
        $("#t1").data("mother_no", mother_no);
        $("#t1").data("id", id);     
    });
}

//删除用户
function delCommit() {
    var id_arr = $("#t1").data("mother_no");
    var id_arrString = id_arr.toString();
    jQuery.ajax({
        url: "../motherMeterConf/removeParentMeterConf.do",
        type: "post",
        data: {
            "meter_Nums": id_arrString //表号         
        },
        dataType: "json",
        success: function (result) {
            if (result.status == 1) {
                $("#delete-motherConf").modal('hide');
                alert("$public_050");
                findMotherMeterConf();
            } else if (result.status === 2) {
                $("#delete-motherConf").modal('hide');
                alert("mother_meter_confg_018");

            }
        },
        error: function () {
            alert("error");
        }
    });
}
function loadUpDownForm() {
    $('#tab-import button:contains($user_manage_129)').click(function () {
        var file1 = $("#file1").val();
        var fileExtension = file1.substring(file1.lastIndexOf('.') + 1);
        if (fileExtension != "xlsx") {
            alert("只能导入excel文件！");
            return;
        }
        document.getElementById("hideDiv").style.display = "";
        $('[name=upload-submit]').attr('disabled', true);
        $('[name=upload-cancel]').attr('disabled', true);
        var exist = "";
        var success = "";
        var arr = new Array();
        if (file1 != "") {
            $("#fileUpload").ajaxSubmit({
                url: "../motherMeterConf/upFile.do",
                type: "post",
                dataType: "json",
                success: function (result) {
                    if (result.status === 1) {
                        for (var i = 0; i < result.data.length; i++) {
                            if (result.data[i] instanceof Object) {
                                exist = result.data[i].exist;
                                success = result.data[i].success;
                            } else {
                                resultNo = result.data[i];
                                arr.push(resultNo);
                            }
                        }
                        $("#tab-import").modal('hide');
                        if (exist === 0) {
                            alert("文件完成上传！成功插入：" + success + "条记录;插入失败:" + exist + "条记录");
                            window.location.reload();
                        } else {
                            alert("文件完成上传！成功插入：" + success + "条记录;插入失败:" + exist + "条记录。失败表号为：" + arr.toString());
                            window.location.reload();
                        }
                    } else {
                        $("#tab-import").modal("hide");
                        alert("error");
                    }
                },
                error: function () {
                    alert("error");
                }
            });

        } else {
            alert("$user_manage_136");
        }
    });
}
function downLoadConfExcel() {
    var lan_cookie = getCookie("language");
    document.getElementById("lang1").value = lan_cookie;
    $("#fileDown").submit();
    $("#tab-import").modal("hide");
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

function allShareTpye() {
    document.getElementById("addPerHead").style.display = "";
    document.getElementById("addRatio").style.display = "";
    document.getElementById("addBlower").style.display = "";
}
function publicMeter() {
    document.getElementById("addPerHead").style.display = "none";
    document.getElementById("addRatio").style.display = "none";
    document.getElementById("addBlower").style.display = "none";
}
function modifyShareTpye() {
    $("#modifyallShare").show();
    $("#modifyshareChoice").hide();
}
function modifyPub() {
    document.getElementById("perHead").style.display = "none";
    document.getElementById("ratio").style.display = "none";
    document.getElementById("blower").style.display = "none";
    $("#allotType input[value='0']").prop("checked", true);
}
function modifyPri() {
    document.getElementById("perHead").style.display = "";
    document.getElementById("ratio").style.display = "";
    document.getElementById("blower").style.display = "";
}
