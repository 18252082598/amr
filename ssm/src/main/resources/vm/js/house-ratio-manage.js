$(function () {
    $("#admin").text(account_name);
    $("#sum-to-do").text("(" + toDoNo + ")");
    $("#exit").click(logout);
    load_Sumtodo();
    load_house_coefficient();
    $("#span1").click(nextPage);
    $("#span2").click(prePage);
    $("#span3").click(firstPage);
    $("#span4").click(lastPage);
    $("#add_show").click(add_show);
    $("#add_coefficient").click(addHouseCoefficient);
    $("#t1").on("click", ".glyphicon.glyphicon-pencil.list-button", modify);
    $("#t1").on("click", ".glyphicon.glyphicon-trash.list-button", del);
});

function load_house_coefficient() {
    $("#t1 tbody tr").remove();
    var pageNo = $("#pageIndex").val().trim();
    pageNo = pageNo == "" ? 1 : pageNo;
    var pageSize = $("#pageSize option:selected").val().trim();
    $
            .ajax({
                url: "../user/load_house_coefficient.do",
                type: "post",
                dataType: "json",
                success: function (result) {
                    if (result.status == 1) {
                        var coes = result.data;
                        var page = parseInt(coes.length / pageSize);
                        var pageMax = coes.length % pageSize == 0 ? page : page + 1;
                        $("#span4").next().text(pageMax);
                        $("#t1").data("coes.length", coes.length);
                        for (var i = (pageNo - 1) * pageSize; i < pageNo
                                * pageSize
                                && i < coes.length; i++) {
                            var coe = coes[i];
                            var coefficient_id = coe.coefficient_id;
                            var add_time = coe.add_time;
                            add_time = format(add_time, 'yyyy.MM.dd HH:mm:ss');
                            var str = "";
                            str += '<tr><td>' + (i + 1) + '</td>';
                            str += '<td>' + coe.coefficient_name + '</td>';
                            str += '<td>' + coe.coefficient + '</td>';
                            str += '<td>' + add_time + '</td>';
                            str += '<td>' + coe.operator_account + '</td>';
                            str += '<td><a href="#"><span class="glyphicon glyphicon-pencil list-button"></span> </a><a href="#"><span class="glyphicon glyphicon-trash list-button"></span> </a></td></tr>';
                            $("#t1 tbody").append($(str));
                            $("#t1").data("coefficient_id" + (i + 1),
                                    coefficient_id);
                        }
                    }
                },
                error: function () {
                    alert("error");
                }
            });
}
function nextPage() {
    var pageIndex = $("#pageIndex").val().trim();
    pageIndex = parseInt(pageIndex) + 1;
    var totalNo = parseInt($("#t1").data("coes.length"));
    var pageSize = $("#pageSize option:selected").val().trim();
    var pageNo = parseInt(totalNo / pageSize);
    var pageMax = totalNo % pageSize == 0 ? pageNo : pageNo + 1;
    if (pageIndex <= 0) {
        alert("$public_038");
    } else if (pageIndex > pageMax) {
        alert("$public_039");
    } else {
        document.getElementById("pageIndex").value = pageIndex;
        load_house_coefficient();
    }
}
function prePage() {
    var pageIndex = $("#pageIndex").val().trim();
    pageIndex = parseInt(pageIndex) - 1;
    if (pageIndex <= 0) {
        alert("$public_040");
    } else {
        $("#pageIndex")[0].value = pageIndex;
        load_house_coefficient();
    }
}

function firstPage() {
    $("#pageIndex")[0].value = 1;
    load_house_coefficient();
}

function lastPage() {
    var totalNo = parseInt($("#t1").data("coes.length"));
    var pageSize = $("#pageSize option:selected").val().trim();
    var pageNo = parseInt(totalNo / pageSize);
    var pageEnd = totalNo % pageSize == 0 ? pageNo : pageNo + 1;
    document.getElementById("pageIndex").value = pageEnd;
    load_house_coefficient();
}

function add_show() {
    document.getElementById("coefficient_name").value = "";
    document.getElementById("coefficient").value = "";
}

function modify() {
    var index = $(this).parents("tr").find("td:eq(0)").text().trim();
    var coefficient_id = $("#t1").data("coefficient_id" + index);
    $("#t1").data("coefficient_id", coefficient_id);
    var originalCoefficientName = $(this).parents("tr").find("td:eq(1)").text().trim();
    var originalCoefficient = $(this).parents("tr").find("td:eq(2)").text().trim();
    $("#t1").data("originalCoefficientName", originalCoefficientName);
    $("#t1").data("originalCoefficient", originalCoefficient);
}

function addHouseCoefficient() {
    var coefficient_name = $("#coefficient_name").val().trim();
    var coefficient = $("#coefficient").val().trim();
    var operator_account = getCookie("account_name");
    var operateType = $("#add_coefficient").text().trim();
    var formatCheck = false;
    var dataCheck = 0;
    if (coefficient_name.match(/^[\u4E00-\u9FA5]{1,}$/)
            && coefficient.match(/^\d+$/)) {
        formatCheck = true;
    } else {
        alert("$house_ratio_manage_001");
    }
    if (formatCheck == true && operateType == "$public_054") {
        jQuery.ajax({
            url: "../user/checkCoefficient.do",
            type: "post",
            async: false,
            data: {
                "coefficient_name": coefficient_name,
                "coefficient": coefficient,
            },
            dataType: "json",
            success: function (result) {
                if (result.status == 1) {
                    dataCheck = 1;
                } else if (result.status == 2) {
                    dataCheck = 2;
                    alert("house_ratio_manage_002");
                } else if (result.status == 3) {
                    dataCheck = 3;
                    alert("house_ratio_manage_002");
                } else if (result.status == 4) {
                    dataCheck = 4;
                    alert("house_ratio_manage_002");
                }
            },
            error: function () {
                alert("error");
            }
        });
    }
    if (formatCheck == true && operateType == "$public_055") {
        jQuery.ajax({
            url: "../user/checkCoefficient.do",
            type: "post",
            async: false,
            data: {
                "coefficient_name": coefficient_name,
                "coefficient": coefficient,
            },
            dataType: "json",
            success: function (result) {
                if (result.status == 1) {
                    dataCheck = 1;
                } else if (result.status == 2) {
                    dataCheck = 2;
                } else if (result.status == 3) {
                    dataCheck = 3;
                } else if (result.status == 4) {
                    dataCheck = 4;
                }
            },
            error: function () {
                alert("error");
            }
        });
    }
    if (operateType == "$public_054" && dataCheck == 1) {
        jQuery.ajax({
            url: "../user/addCoefficient.do",
            type: "post",
            data: {
                "coefficient_name": coefficient_name,
                "coefficient": coefficient,
                "operator_account": operator_account
            },
            dataType: "json",
            success: function (result) {
                if (result.status == 1) {
                    $("#add_cancel").click();
                    load_house_coefficient();
                }
            },
            error: function () {
                alert("error");
            }
        });
    }

    if (operateType == "$public_055") {
        var coefficient_id = $("#t1").data("coefficient_id");
        var originalCoefficientName = $("#t1").data("originalCoefficientName");
        var originalCoefficient = $("#t1").data("originalCoefficient");
        var check = false;
        if (dataCheck == 1 || (dataCheck == 2 && originalCoefficient == coefficient) ||
                (dataCheck == 3 && originalCoefficientName == coefficient_name)) {
            jQuery.ajax({
                url: "../user/modifyCoefficient.do",
                type: "post",
                data: {
                    "coefficient_id": coefficient_id,
                    "coefficient_name": coefficient_name,
                    "coefficient": coefficient,
                },
                dataType: "json",
                success: function (result) {
                    if (result.status == 1) {
                        $("#add_cancel").click();
                        load_house_coefficient();
                    } else {
                        alert("$public_036");
                    }
                },
                error: function () {
                    alert("error");
                }
            });
        } else {
            alert("$house_ratio_manage_002");
        }
    }
}

function del() {
    var coefficient_name = $(this).parents("tr").find("td:eq(1)").text().trim();
    if (confirm("$public_056")) {
        jQuery.ajax({
            url: "../user/deleteCoefficient.do",
            type: "post",
            data: {
                "coefficient_name": coefficient_name
            },
            dataType: "json",
            success: function (result) {
                if (result.status == 1) {
                    load_house_coefficient()
                }
            },
            error: function () {
                alert("error");
            }
        });
    }
}
