$(function () {
    $("#admin").text(account_name);
    $("#sum-to-do").text("(" + toDoNo + ")");
    $("#exit").click(logout);
    load_Sumtodo();
    priceManagePermission();
    loadFeeTypes();
    $(".span1").click(nextPage);
    $(".span2").click(prePage);
    $(".span3").click(firstPage);
    $(".span4").click(lastPage);
    $(".input-page").keypress(function (event) {
        var keycode = (event.keyCode ? event.keyCode : event.which);
        if (keycode == 13) {
            var pageIndex = $(this).val().trim();
            pageIndex = parseInt(pageIndex);
            var totalNo = parseInt($("#t1").data("feeTypes.length"));
            var pageSize = $("#pageSize option:selected").val().trim();
            var pageNo = parseInt(totalNo / pageSize);
            var pageMax = totalNo % pageSize == 0 ? pageNo : pageNo + 1;
            if (pageIndex <= 0) {
                alert("$public_038");
            } else if (pageIndex > pageMax) {
                alert("$public_039");
            } else {
                document.getElementById("pageIndex").value = pageIndex;
                loadFeeTypes();
            }
            return false;
        }
    });
    $("#addFeeType").click(addFeeType);
    $("#reset").click(clearAll);
    $("#t1").on("click", ".glyphicon-pencil", modify);//点击费率修改
    $("#d5 button:eq(0)").click(modifyConfirm);
    $("#d5 button:eq(1)").click(emptyAll);
    $("#t1").on("click", ".glyphicon-trash", del);//点击删除
})

var loadPage = function () {
    loadFeeTypes();
};

function loadFeeTypes() {
    $("#t1 tbody tr").remove();
//    var pageNo = $("#pageIndex").val().trim();
     var pageNo = $(".input-page").val().trim();
    pageNo = pageNo == "" ? 1 : pageNo;
    var pageSize = $(".pageSize option:selected").val().trim();
    jQuery.ajax({
        url: "../user/loadFeeTypes.do",
        type: "post",
        dataType: "json",
        success: function (result) {
            if (result.status == 1) {
                var feeTypes = result.data;
                var page = parseInt(feeTypes.length / pageSize);
                var pageMax = feeTypes.length % pageSize == 0 ? page : page + 1;
                $(".span4").next().text(pageMax);
                $("#t1").data("feeTypes.length", feeTypes.length);
                $("#t1").data("totalNo", feeTypes.length);
                for (var i = (pageNo - 1) * pageSize; i < pageNo * pageSize && i < feeTypes.length; i++) {
                    var feeType = feeTypes[i];
                    var feeTypeId = feeType.feeTypeId;
                    var feeTypeName = feeType.feeTypeName;
                    var meterType = feeType.meterType;
                    var basicUnitPrice = feeType.basicUnitPrice;
                    var disposingUnitCost = feeType.disposingUnitCost;
                    var otherCost = feeType.otherCost;
                    var totalUnitCost = feeType.totalUnitCost;
                    var extraCost = feeType.extraCost;
                    var paymentMethod = feeType.paymentMethod;
                    var isLevelPrice = feeType.isLevelPrice;
                    var levelOneStartVolume = feeType.levelOneStartVolume;
                    var levelOneUnitPrice = feeType.levelOneUnitPrice;
                    var levelTwoStartVolume = feeType.levelTwoStartVolume;
                    var levelTwoUnitPrice = feeType.levelTwoUnitPrice;
                    var levelThreeStartVolume = feeType.levelThreeStartVolume;
                    var levelThreeUnitPrice = feeType.levelThreeUnitPrice;
                    var levelFourStartVolume = feeType.levelFourStartVolume;
                    var levelFourUnitPrice = feeType.levelFourUnitPrice;
                    var levelFiveStartVolume = feeType.levelFiveStartVolume;
                    var levelFiveUnitPrice = feeType.levelFiveUnitPrice;
                    var feeTypeStatus = feeType.feeTypeStatus;
                    var feeTypeStatus_str = feeTypeStatus == 1 ? "$price_manage_045" : "$price_manage_043";//已使用:未使用
                    var addTime = feeType.addTime;
                    addTime = format(addTime, 'yyyy.MM.dd HH:mm:ss');
                    var operatorName = feeType.operatorName;
                    var str = "";
                    str += '<tr><td>' + (i + 1) + '</td>';
                    str += '<td>' + feeTypeName + '</td>';
                    str += '<td>' + feeTypeStatus_str + '</td>';
                    str += '<td>' + addTime + '</td>';
                    str += '<td>' + operatorName + '</td>';
                    str += '<td><a title="$price_manage_005" data-toggle="modal" href="#price-edit"><span class="glyphicon glyphicon-pencil list-button"></span></a><a class="remove-item" title="$price_manage_00501" href=""><span class="glyphicon glyphicon-trash list-button"></span></a></td></tr>';
                    $("#t1 tbody").append($(str));
                    $("#t1").data("feeTypeId" + (i + 1),
                            feeTypeId);
                    $("#t1").data("feeTypeStatus" + (i + 1),
                            feeTypeStatus);
                    $("#t1").data("feeTypeName" + (i + 1),
                            feeTypeName);
                    $("#t1").data("meterType" + (i + 1),
                            meterType);
                    $("#t1").data("basicUnitPrice" + (i + 1),
                            basicUnitPrice);
                    $("#t1").data("disposingUnitCost" + (i + 1),
                            disposingUnitCost);
                    $("#t1").data("otherCost" + (i + 1),
                            otherCost);
                    $("#t1").data("extraCost" + (i + 1),
                            extraCost);
                    $("#t1").data("paymentMethod" + (i + 1),
                            paymentMethod);
                    $("#t1").data("isLevelPrice" + (i + 1),
                            isLevelPrice);
                    $("#t1").data("levelOneStartVolume" + (i + 1),
                            levelOneStartVolume);
                    $("#t1").data("levelOneUnitPrice" + (i + 1),
                            levelOneUnitPrice);
                    $("#t1").data("levelTwoStartVolume" + (i + 1),
                            levelTwoStartVolume);
                    $("#t1").data("levelTwoUnitPrice" + (i + 1),
                            levelTwoUnitPrice);
                    $("#t1").data("levelThreeStartVolume" + (i + 1),
                            levelThreeStartVolume);
                    $("#t1").data("levelThreeUnitPrice" + (i + 1),
                            levelThreeUnitPrice);
                    $("#t1").data("levelFourStartVolume" + (i + 1),
                            levelFourStartVolume);
                    $("#t1").data("levelFourUnitPrice" + (i + 1),
                            levelFourUnitPrice);
                    $("#t1").data("levelFiveStartVolume" + (i + 1),
                            levelFiveStartVolume);
                    $("#t1").data("levelFiveUnitPrice" + (i + 1),
                            levelFiveUnitPrice);
                }
            }
        },
        error: function () {
            alert("error");
        }
    });
}

function total() {
	//基本单价
    var basicUnitPrice = $("#basicUnitPrice").val().trim();
    basicUnitPrice = basicUnitPrice == "" ? 0 : basicUnitPrice;
    //排污费
    var disposingUnitCost = $("#disposingUnitCost").val().trim();
    disposingUnitCost = disposingUnitCost == "" ? 0 : disposingUnitCost;
    //其他费用
    var otherCost = $("#otherCost").val().trim();
    otherCost = otherCost == "" ? 0 : otherCost;
    //合计单价
    var totalUnitCost = parseFloat(basicUnitPrice)
            + parseFloat(disposingUnitCost) + parseFloat(otherCost);
    var unitCost = Math.round(totalUnitCost*100)/100;//修改了精度问题
    document.getElementById("totalUnitCost").value = unitCost;
}

function check() {
    var checked = $(this).is(':checked');
    $(":checkbox").attr("checked", true);
}

function total1() {
	//排污费
    var disposingUnitCost = $("#disposingUnitCost").val().trim();
    //其他费用
    var otherCost = $("#otherCost").val().trim();
    //一阶
    var levelOneUnitPrice = $("#t2 tbody tr:eq(0) input:eq(1)").val().trim();
    $("#t2 tbody tr:eq(0) input:eq(2)")[0].value = parseFloat(disposingUnitCost)
            + parseFloat(otherCost) + parseFloat(levelOneUnitPrice);
}

function total2() {
    var disposingUnitCost = $("#disposingUnitCost").val().trim();
    var otherCost = $("#otherCost").val().trim();
    //二阶
    var levelOneUnitPrice = $("#t2 tbody tr:eq(1) input:eq(1)").val().trim();
    $("#t2 tbody tr:eq(1) input:eq(2)")[0].value = parseFloat(disposingUnitCost)
            + parseFloat(otherCost) + parseFloat(levelOneUnitPrice);
}

function total3() {
    var disposingUnitCost = $("#disposingUnitCost").val().trim();
    var otherCost = $("#otherCost").val().trim();
    //三阶
    var levelOneUnitPrice = $("#t2 tbody tr:eq(2) input:eq(1)").val().trim();
    $("#t2 tbody tr:eq(2) input:eq(2)")[0].value = parseFloat(disposingUnitCost)
            + parseFloat(otherCost) + parseFloat(levelOneUnitPrice);
}

function total4() {
    var disposingUnitCost = $("#disposingUnitCost").val().trim();
    var otherCost = $("#otherCost").val().trim();
    //四阶
    var levelOneUnitPrice = $("#t2 tbody tr:eq(3) input:eq(1)").val().trim();
    $("#t2 tbody tr:eq(3) input:eq(2)")[0].value = parseFloat(disposingUnitCost)
            + parseFloat(otherCost) + parseFloat(levelOneUnitPrice);
}

function total5() {
    var disposingUnitCost = $("#disposingUnitCost").val().trim();
    var otherCost = $("#otherCost").val().trim();
    //五阶
    var levelOneUnitPrice = $("#t2 tbody tr:eq(4) input:eq(1)").val().trim();
    $("#t2 tbody tr:eq(4) input:eq(2)")[0].value = parseFloat(disposingUnitCost)
            + parseFloat(otherCost) + parseFloat(levelOneUnitPrice);
}

function clearAll() {
    document.getElementById("feeTypeName").value = "";
    $("#basicUnitPrice")[0].value = "";
    $("#disposingUnitCost")[0].value = "";
    $("#otherCost")[0].value = "";
    $("#totalUnitCost")[0].value = "";
    $("#extraCost")[0].value = "";
    $("#paymentMethod option:first").attr("selected");
    $(":checkbox").attr("checked", false);
    $("#t2 tbody tr:eq(0) input:eq(0)")[0].value = "";
    $("#t2 tbody tr:eq(0) input:eq(1)")[0].value = "";
    $("#t2 tbody tr:eq(0) input:eq(2)")[0].value = "";
    $("#t2 tbody tr:eq(1) input:eq(0)")[0].value = "";
    $("#t2 tbody tr:eq(1) input:eq(1)")[0].value = "";
    $("#t2 tbody tr:eq(1) input:eq(2)")[0].value = "";
    $("#t2 tbody tr:eq(2) input:eq(0)")[0].value = "";
    $("#t2 tbody tr:eq(2) input:eq(1)")[0].value = "";
    $("#t2 tbody tr:eq(2) input:eq(2)")[0].value = "";
    $("#t2 tbody tr:eq(3) input:eq(0)")[0].value = "";
    $("#t2 tbody tr:eq(3) input:eq(1)")[0].value = "";
    $("#t2 tbody tr:eq(3) input:eq(2)")[0].value = "";
    $("#t2 tbody tr:eq(4) input:eq(0)")[0].value = "";
    $("#t2 tbody tr:eq(4) input:eq(1)")[0].value = "";
    $("#t2 tbody tr:eq(4) input:eq(2)")[0].value = "";
}

function addFeeType() {
    var feeTypeName = $("#feeTypeName").val().trim();
    var meterType = $("#meterType option:selected").val().trim();
    var basicUnitPrice = $("#basicUnitPrice").val().trim();
    var disposingUnitCost = $("#disposingUnitCost").val().trim();
    var otherCost = $("#otherCost").val().trim();
    var totalUnitCost = $("#totalUnitCost").val().trim();
    var extraCost = $("#extraCost").val().trim();
    var paymentMethod = $("#paymentMethod option:selected").val().trim();
    var isLevelPrice = $("#isLevelPrice").is(":checked") == true ? "1" : "0";
    var levelOneStartVolume = $("#t3 tbody tr:eq(0) input:eq(0)").val().trim();
    levelOneStartVolume = levelOneStartVolume == "" ? 0 : levelOneStartVolume;
    var levelOneUnitPrice = $("#t3 tbody tr:eq(0) input:eq(1)").val().trim();
    levelOneUnitPrice = levelOneUnitPrice == "" ? 0 : levelOneUnitPrice;
    var levelOneTotalPrice = $("#t3 tbody tr:eq(0) input:eq(2)").val().trim();
    levelOneTotalPrice = levelOneTotalPrice == "" ? 0 : levelOneTotalPrice;
    var levelTwoStartVolume = $("#t3 tbody tr:eq(1) input:eq(0)").val().trim();
    levelTwoStartVolume = levelTwoStartVolume == "" ? 0 : levelTwoStartVolume;
    var levelTwoUnitPrice = $("#t3 tbody tr:eq(1) input:eq(1)").val().trim();
    levelTwoUnitPrice = levelTwoUnitPrice == "" ? 0 : levelTwoUnitPrice;
    var levelTwoTotalPrice = $("#t3 tbody tr:eq(1) input:eq(2)").val().trim();
    levelTwoTotalPrice = levelTwoTotalPrice == "" ? 0 : levelTwoTotalPrice;
    var levelThreeStartVolume = $("#t3 tbody tr:eq(2) input:eq(0)").val().trim();
    levelThreeStartVolume = levelThreeStartVolume == "" ? 0 : levelThreeStartVolume;
    var levelThreeUnitPrice = $("#t3 tbody tr:eq(2) input:eq(1)").val().trim();
    levelThreeUnitPrice = levelThreeUnitPrice == "" ? 0 : levelThreeUnitPrice;
    var levelThreeTotalPrice = $("#t3 tbody tr:eq(2) input:eq(2)").val().trim();
    levelThreeTotalPrice = levelThreeTotalPrice == "" ? 0 : levelThreeTotalPrice;
    var levelFourStartVolume = $("#t3 tbody tr:eq(3) input:eq(0)").val().trim();
    levelFourStartVolume = levelFourStartVolume == "" ? 0 : levelFourStartVolume;
    var levelFourUnitPrice = $("#t3 tbody tr:eq(3) input:eq(1)").val().trim();
    levelFourUnitPrice = levelFourUnitPrice == "" ? 0 : levelFourUnitPrice;
    var levelFourTotalPrice = $("#t3 tbody tr:eq(3) input:eq(2)").val().trim();
    levelFourTotalPrice = levelFourTotalPrice == "" ? 0 : levelFourTotalPrice;
    var levelFiveStartVolume = $("#t3 tbody tr:eq(4) input:eq(0)").val().trim();
    levelFiveStartVolume = levelFiveStartVolume == "" ? 0 : levelFiveStartVolume;
    var levelFiveUnitPrice = $("#t3 tbody tr:eq(4) input:eq(1)").val().trim();
    levelFiveUnitPrice = levelFiveUnitPrice == "" ? 0 : levelFiveUnitPrice;
    var levelFiveTotalPrice = $("#t3 tbody tr:eq(4) input:eq(2)").val().trim();
    levelFiveTotalPrice = levelFiveTotalPrice == "" ? 0 : levelFiveTotalPrice;
    var feeTypeStatus = "0";
    var operatorName = getCookie("account_name");
    var check = false;
    if (feeTypeName != "" && basicUnitPrice >= 0
            && disposingUnitCost >= 0 && otherCost >= 0
            && totalUnitCost >= 0 && extraCost >= 0) {
        check = true;
    }
    if (isLevelPrice == "1" && (levelOneStartVolume == "" || levelOneStartVolume <= 0
            || levelOneUnitPrice == "" || levelOneUnitPrice <= 0
            || levelOneTotalPrice == "" || levelOneTotalPrice <= 0)) {
        check = false;
    }
    if (check == false) {
        alert("$price_manage_044");
    }
    if (check) {
        jQuery.ajax({
            url: "../user/addFeeType.do",
            type: "post",
            data: {
                "feeTypeName": feeTypeName,
                "meterType": meterType,
                "basicUnitPrice": basicUnitPrice,
                "disposingUnitCost": disposingUnitCost,
                "otherCost": otherCost,
                "totalUnitCost": totalUnitCost,
                "extraCost": extraCost,
                "paymentMethod": paymentMethod,
                "isLevelPrice": isLevelPrice,
                "levelOneStartVolume": levelOneStartVolume,
                "levelOneUnitPrice": levelOneUnitPrice,
                "levelOneTotalPrice": levelOneTotalPrice,
                "levelTwoStartVolume": levelTwoStartVolume,
                "levelTwoUnitPrice": levelTwoUnitPrice,
                "levelTwoTotalPrice": levelTwoTotalPrice,
                "levelThreeStartVolume": levelThreeStartVolume,
                "levelThreeUnitPrice": levelThreeUnitPrice,
                "levelThreeTotalPrice": levelThreeTotalPrice,
                "levelFourStartVolume": levelFourStartVolume,
                "levelFourUnitPrice": levelFourUnitPrice,
                "levelFourTotalPrice": levelFourTotalPrice,
                "levelFiveStartVolume": levelFiveStartVolume,
                "levelFiveUnitPrice": levelFiveUnitPrice,
                "levelFiveTotalPrice": levelFiveTotalPrice,
                "feeTypeStatus": feeTypeStatus,
                "operatorName": operatorName
            },
            dataType: "json",
            success: function (result) {
                if (result.status == 1) { 
                    $('#tab-price-add').modal('hide');
                    alert("$public_051");                  
                    loadFeeTypes();
                }
            },
            error: function () {
                alert("error");
            }
        });
    }
}

function renew() {
    window.location.href = window.location.href;
}

function modify() {
    var input = $("#t3 tbody input");
    for (var i = 0; i < input.length; i++) {
        input[i].value = "";
    }
    var index = $(this).parents("tr").find("td:eq(0)").text().trim();
    var feeTypeId = $("#t1").data("feeTypeId" + index);
    $("#t1").data("index", index);
    $("#t1").data("feeTypeId", feeTypeId);
    jQuery.ajax({
        url: "../user/findFeeTypeById.do",
        type: "post",
        data: {
            "feeTypeId": feeTypeId
        },
        dataType: "json",
        success: function (result) {
            if (result.status == 1) {
                var feeType = result.data;
                $("#d1 input")[0].value = feeType.feeTypeName;
                $("#d1 select option[value='" + feeType.meterType + "']").attr("selected", true);
                $("#d2 input")[0].value = feeType.basicUnitPrice;
                $("#d2 input")[1].value = feeType.disposingUnitCost;
                $("#d2 input")[2].value = feeType.otherCost;
                $("#d3 input")[0].value = feeType.totalUnitCost;
                $("#d3 input")[1].value = feeType.extraCost;
                $("#d3 select option[value='" + feeType.paymentMethod + "']").attr("selected", true);
                var isLevelPrice = feeType.isLevelPrice;
                if (isLevelPrice == "0") {
                    $("#d4 input").attr("checked", false);
                    $("#t3 tbody input").attr("disabled", true);
                } else if (isLevelPrice == "1") {
                    $("#d4 input").attr("checked", true);
                    $("#t3 tbody input").removeAttr("disabled");
                    $("#t3 tbody tr:eq(0) td:eq(3) input").attr("readonly", true);
                    $("#t3 tbody tr:eq(1) td:eq(3) input").attr("readonly", true);
                    $("#t3 tbody tr:eq(2) td:eq(3) input").attr("readonly", true);
                    $("#t3 tbody tr:eq(3) td:eq(3) input").attr("readonly", true);
                    $("#t3 tbody tr:eq(4) td:eq(3) input").attr("readonly", true);
                    $("#t3 tbody tr:eq(0) td:eq(1) input")[0].value = feeType.levelOneStartVolume;
                    $("#t3 tbody tr:eq(0) td:eq(2) input")[0].value = feeType.levelOneUnitPrice;
                    $("#t3 tbody tr:eq(0) td:eq(3) input")[0].value = feeType.levelOneTotalPrice;
                    $("#t3 tbody tr:eq(1) td:eq(1) input")[0].value = feeType.levelTwoStartVolume;
                    $("#t3 tbody tr:eq(1) td:eq(2) input")[0].value = feeType.levelTwoUnitPrice;
                    $("#t3 tbody tr:eq(1) td:eq(3) input")[0].value = feeType.levelTwoTotalPrice;
                    $("#t3 tbody tr:eq(2) td:eq(1) input")[0].value = feeType.levelThreeStartVolume;
                    $("#t3 tbody tr:eq(2) td:eq(2) input")[0].value = feeType.levelThreeUnitPrice;
                    $("#t3 tbody tr:eq(2) td:eq(3) input")[0].value = feeType.levelThreeTotalPrice;
                    $("#t3 tbody tr:eq(3) td:eq(1) input")[0].value = feeType.levelFourStartVolume;
                    $("#t3 tbody tr:eq(3) td:eq(2) input")[0].value = feeType.levelFourUnitPrice;
                    $("#t3 tbody tr:eq(3) td:eq(3) input")[0].value = feeType.levelFourTotalPrice;
                    $("#t3 tbody tr:eq(4) td:eq(1) input")[0].value = feeType.levelFiveStartVolume;
                    $("#t3 tbody tr:eq(4) td:eq(2) input")[0].value = feeType.levelFiveUnitPrice;
                    $("#t3 tbody tr:eq(4) td:eq(3) input")[0].value = feeType.levelFiveTotalPrice;
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

function sum() {
    var basicUnitPrice = $("#d2 input:eq(0)").val().trim();
    basicUnitPrice = basicUnitPrice == "" ? 0 : basicUnitPrice;
    var disposingUnitCost = $("#d2 input:eq(1)").val().trim();
    disposingUnitCost = disposingUnitCost == "" ? 0 : disposingUnitCost;
    var otherCost = $("#d2 input:eq(2)").val().trim();
    otherCost = otherCost == "" ? 0 : otherCost;
    
    var totalUnitCost = parseFloat(basicUnitPrice)
            + parseFloat(disposingUnitCost) + parseFloat(otherCost);
    var unitCost = Math.round(totalUnitCost*100)/100;//修改了精度问题
    $("#d3 input:eq(0)")[0].value = unitCost;
}

function sum1() {
    var disposingUnitCost = $("#d2 input:eq(1)").val().trim();
    var otherCost = $("#d2 input:eq(2)").val().trim();
    var levelOneUnitPrice = $("#t3 tbody tr:eq(0) input:eq(1)").val().trim();
    $("#t3 tbody tr:eq(0) input:eq(2)")[0].value = parseFloat(disposingUnitCost)
            + parseFloat(otherCost) + parseFloat(levelOneUnitPrice);
}

function sum2() {
    var disposingUnitCost = $("#d2 input:eq(1)").val().trim();
    var otherCost = $("#d2 input:eq(2)").val().trim();
    var levelOneUnitPrice = $("#t3 tbody tr:eq(1) input:eq(1)").val().trim();
    $("#t3 tbody tr:eq(1) input:eq(2)")[0].value = parseFloat(disposingUnitCost)
            + parseFloat(otherCost) + parseFloat(levelOneUnitPrice);
}

function sum3() {
    var disposingUnitCost = $("#d2 input:eq(1)").val().trim();
    var otherCost = $("#d2 input:eq(2)").val().trim();
    var levelOneUnitPrice = $("#t3 tbody tr:eq(2) input:eq(1)").val().trim();
    $("#t3 tbody tr:eq(2) input:eq(2)")[0].value = parseFloat(disposingUnitCost)
            + parseFloat(otherCost) + parseFloat(levelOneUnitPrice);
}

function sum4() {
    var disposingUnitCost = $("#d2 input:eq(1)").val().trim();
    var otherCost = $("#d2 input:eq(2)").val().trim();
    var levelOneUnitPrice = $("#t3 tbody tr:eq(3) input:eq(1)").val().trim();
    $("#t3 tbody tr:eq(3) input:eq(2)")[0].value = parseFloat(disposingUnitCost)
            + parseFloat(otherCost) + parseFloat(levelOneUnitPrice);
}

function sum5() {
    var disposingUnitCost = $("#d2 input:eq(1)").val().trim();
    var otherCost = $("#d2 input:eq(2)").val().trim();
    var levelOneUnitPrice = $("#t3 tbody tr:eq(4) input:eq(1)").val().trim();
    $("#t3 tbody tr:eq(4) input:eq(2)")[0].value = parseFloat(disposingUnitCost)
            + parseFloat(otherCost) + parseFloat(levelOneUnitPrice);
}

function modifyConfirm() {
    var feeTypeId = $("#t1").data("feeTypeId");
    var feeTypeName = $("#d1 input").val().trim();
    var meterType = $("#d1 select option:selected").val().trim();
    var basicUnitPrice = $("#d2 input:eq(0)").val().trim();
    var disposingUnitCost = $("#d2 input:eq(1)").val().trim();
    var otherCost = $("#d2 input:eq(2)").val().trim();
    var totalUnitCost = $("#d3 input:eq(0)").val().trim();
    var extraCost = $("#d3 input:eq(1)").val().trim();
    var paymentMethod = $("#d3 select option:selected").val().trim();
    var isLevelPrice = $("#d4 input").is(":checked") === true ? "1" : "0";
    var levelOneStartVolume = $("#t3 tbody tr:eq(0) input:eq(0)").val().trim();
    levelOneStartVolume = levelOneStartVolume == "" ? 0 : levelOneStartVolume;
    var levelOneUnitPrice = $("#t3 tbody tr:eq(0) input:eq(1)").val().trim();
    levelOneUnitPrice = levelOneUnitPrice == "" ? 0 : levelOneUnitPrice;
    var levelOneTotalPrice = $("#t3 tbody tr:eq(0) input:eq(2)").val().trim();
    levelOneTotalPrice = levelOneTotalPrice == "" ? 0 : levelOneTotalPrice;
    var levelTwoStartVolume = $("#t3 tbody tr:eq(1) input:eq(0)").val().trim();
    levelTwoStartVolume = levelTwoStartVolume == "" ? 0 : levelTwoStartVolume;
    var levelTwoUnitPrice = $("#t3 tbody tr:eq(1) input:eq(1)").val().trim();
    levelTwoUnitPrice = levelTwoUnitPrice == "" ? 0 : levelTwoUnitPrice;
    var levelTwoTotalPrice = $("#t3 tbody tr:eq(1) input:eq(2)").val().trim();
    levelTwoTotalPrice = levelTwoTotalPrice == "" ? 0 : levelTwoTotalPrice;
    var levelThreeStartVolume = $("#t3 tbody tr:eq(2) input:eq(0)").val().trim();
    levelThreeStartVolume = levelThreeStartVolume == "" ? 0 : levelThreeStartVolume;
    var levelThreeUnitPrice = $("#t3 tbody tr:eq(2) input:eq(1)").val().trim();
    levelThreeUnitPrice = levelThreeUnitPrice == "" ? 0 : levelThreeUnitPrice;
    var levelThreeTotalPrice = $("#t3 tbody tr:eq(2) input:eq(2)").val().trim();
    levelThreeTotalPrice = levelThreeTotalPrice == "" ? 0 : levelThreeTotalPrice;
    var levelFourStartVolume = $("#t3 tbody tr:eq(3) input:eq(0)").val().trim();
    levelFourStartVolume = levelFourStartVolume == "" ? 0 : levelFourStartVolume;
    var levelFourUnitPrice = $("#t3 tbody tr:eq(3) input:eq(1)").val().trim();
    levelFourUnitPrice = levelFourUnitPrice == "" ? 0 : levelFourUnitPrice;
    var levelFourTotalPrice = $("#t3 tbody tr:eq(3) input:eq(2)").val().trim();
    levelFourTotalPrice = levelFourTotalPrice == "" ? 0 : levelFourTotalPrice;
    var levelFiveStartVolume = $("#t3 tbody tr:eq(4) input:eq(0)").val().trim();
    levelFiveStartVolume = levelFiveStartVolume == "" ? 0 : levelFiveStartVolume;
    var levelFiveUnitPrice = $("#t3 tbody tr:eq(4) input:eq(1)").val().trim();
    levelFiveUnitPrice = levelFiveUnitPrice == "" ? 0 : levelFiveUnitPrice;
    var levelFiveTotalPrice = $("#t3 tbody tr:eq(4) input:eq(2)").val().trim();
    levelFiveTotalPrice = levelFiveTotalPrice == "" ? 0 : levelFiveTotalPrice;
    var index = $("#t1").data("index");
    var original_feeTypeName = $("#t1").data("feeTypeName" + index);
    var original_meterType = $("#t1").data("meterType" + index);
    var original_basicUnitPrice = $("#t1").data("basicUnitPrice" + index);
    var original_disposingUnitCost = $("#t1").data("disposingUnitCost" + index);
    var original_otherCost = $("#t1").data("otherCost" + index);
    var original_extraCost = $("#t1").data("extraCost" + index);
    var original_paymentMethod = $("#t1").data("paymentMethod" + index);
    var original_isLevelPrice = $("#t1").data("isLevelPrice" + index);
    var original_levelOneStartVolume = $("#t1").data("levelOneStartVolume" + index);
    var original_levelOneUnitPrice = $("#t1").data("levelOneUnitPrice" + index);
    var original_levelTwoStartVolume = $("#t1").data("levelTwoStartVolume" + index);
    var original_levelTwoUnitPrice = $("#t1").data("levelTwoUnitPrice" + index);
    var original_levelThreeStartVolume = $("#t1").data("levelThreeStartVolume" + index);
    var original_levelThreeUnitPrice = $("#t1").data("levelThreeUnitPrice" + index);
    var original_levelFourStartVolume = $("#t1").data("levelFourStartVolume" + index);
    var original_levelFourUnitPrice = $("#t1").data("levelFourUnitPrice" + index);
    var original_levelFiveStartVolume = $("#t1").data("levelFiveStartVolume" + index);
    var original_levelFiveUnitPrice = $("#t1").data("levelFiveUnitPrice" + index);
    var check = false;
    if (feeTypeName != "" && basicUnitPrice >= 0
            && disposingUnitCost >= 0 && otherCost >= 0
            && totalUnitCost >= 0 && extraCost >= 0) {
        check = true;
    }
    if (isLevelPrice == "1" && (
            levelOneStartVolume == "" || levelOneStartVolume <= 0
            || levelOneUnitPrice == "" || levelOneUnitPrice <= 0
            || levelOneTotalPrice == "" || levelOneTotalPrice <= 0)) {
        check = false;
    }
    if (check == false) {
        alert("$price_manage_044");
    }
    if (check) {
        jQuery.ajax({
            url: "../user/modifyFeeType.do",
            type: "post",
            data: {
                "feeTypeId": feeTypeId,
                "feeTypeName": feeTypeName,
                "meterType": meterType,
                "basicUnitPrice": basicUnitPrice,
                "disposingUnitCost": disposingUnitCost,
                "otherCost": otherCost,
                "totalUnitCost": totalUnitCost,
                "extraCost": extraCost,
                "paymentMethod": paymentMethod,
                "isLevelPrice": isLevelPrice,
                "levelOneStartVolume": levelOneStartVolume,
                "levelOneUnitPrice": levelOneUnitPrice,
                "levelOneTotalPrice": levelOneTotalPrice,
                "levelTwoStartVolume": levelTwoStartVolume,
                "levelTwoUnitPrice": levelTwoUnitPrice,
                "levelTwoTotalPrice": levelTwoTotalPrice,
                "levelThreeStartVolume": levelThreeStartVolume,
                "levelThreeUnitPrice": levelThreeUnitPrice,
                "levelThreeTotalPrice": levelThreeTotalPrice,
                "levelFourStartVolume": levelFourStartVolume,
                "levelFourUnitPrice": levelFourUnitPrice,
                "levelFourTotalPrice": levelFourTotalPrice,
                "levelFiveStartVolume": levelFiveStartVolume,
                "levelFiveUnitPrice": levelFiveUnitPrice,
                "levelFiveTotalPrice": levelFiveTotalPrice,
                "original_feeTypeName": original_feeTypeName,
                "original_meterType": original_meterType,
                "original_basicUnitPrice": original_basicUnitPrice,
                "original_disposingUnitCost": original_disposingUnitCost,
                "original_otherCost": original_otherCost,
                "original_extraCost": original_extraCost,
                "original_paymentMethod": original_paymentMethod,
                "original_isLevelPrice": original_isLevelPrice,
                "original_levelOneStartVolume": original_levelOneStartVolume,
                "original_levelOneUnitPrice": original_levelOneUnitPrice,
                "original_levelTwoStartVolume": original_levelTwoStartVolume,
                "original_levelTwoUnitPrice": original_levelTwoUnitPrice,
                "original_levelThreeStartVolume": original_levelThreeStartVolume,
                "original_levelThreeUnitPrice": original_levelThreeUnitPrice,
                "original_levelFourStartVolume": original_levelFourStartVolume,
                "original_levelFourUnitPrice": original_levelFourUnitPrice,
                "original_levelFiveStartVolume": original_levelFiveStartVolume,
                "original_levelFiveUnitPrice": original_levelFiveUnitPrice
            },
            dataType: "json",
            success: function (result) {
                if (result.status == 1) {
                    $("#tab-price-xg").modal('hide');
                    alert("$public_041");
                    loadFeeTypes();
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

function emptyAll() {
    $("#d1 input")[0].value = "";
    $("#d2 input:eq(0)")[0].value = "";
    $("#d2 input:eq(1)")[0].value = "";
    $("#d2 input:eq(2)")[0].value = "";
    $("#d3 input:eq(0)")[0].value = "";
    $("#d3 input:eq(1)")[0].value = "";
    $("#t3 tbody tr:eq(0) td:eq(1) input")[0].value = "";
    $("#t3 tbody tr:eq(0) td:eq(2) input")[0].value = "";
    $("#t3 tbody tr:eq(0) td:eq(3) input")[0].value = "";
    $("#t3 tbody tr:eq(1) td:eq(1) input")[0].value = "";
    $("#t3 tbody tr:eq(1) td:eq(2) input")[0].value = "";
    $("#t3 tbody tr:eq(1) td:eq(3) input")[0].value = "";
    $("#t3 tbody tr:eq(2) td:eq(1) input")[0].value = "";
    $("#t3 tbody tr:eq(2) td:eq(2) input")[0].value = "";
    $("#t3 tbody tr:eq(2) td:eq(3) input")[0].value = "";
    $("#t3 tbody tr:eq(3) td:eq(1) input")[0].value = "";
    $("#t3 tbody tr:eq(3) td:eq(2) input")[0].value = "";
    $("#t3 tbody tr:eq(3) td:eq(3) input")[0].value = "";
    $("#t3 tbody tr:eq(4) td:eq(1) input")[0].value = "";
    $("#t3 tbody tr:eq(4) td:eq(2) input")[0].value = "";
    $("#t3 tbody tr:eq(4) td:eq(3) input")[0].value = "";
}

function del() {
    var index = $(this).parents("tr").find("td:eq(0)").text().trim();
    var feeTypeId = $("#t1").data("feeTypeId" + index);
    var feeTypeName = $(this).parents("tr").find("td:eq(1)").text().trim();
    var feeTypeStatus = $("#t1").data("feeTypeStatus" + index);
    if (feeTypeStatus == "0" && confirm("$public_056")) {
        jQuery.ajax({
            url: "../user/delFeeType.do",
            type: "post",
            data: {
                "feeTypeId": feeTypeId,
                "feeTypeName": feeTypeName
            },
            dataType: "json",
            success: function (result) {
                if (result.status == 1) {
                    loadFeeTypes();
                }
            },
            error: function () {
                alert("error");
            }
        });
    } else if (feeTypeStatus == "1") {
        alert("$price_manage_046");
    }
}