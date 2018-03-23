$(function () {
    $("#admin").text(account_name);
    $("#sum-to-do").text("(" + toDoNo + ")");
    $("#exit").click(logout);
    rechargeListPermission();
    load_Sumtodo();
    load_communities();
    load_operators();
    findOperateInfo();
    $("#findOperateInfo").click(findOperateInfoBefore);
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
                findOperateInfo();
            }
            return false;
        }
    });
   // $("#import-pay-record").click(exportOperateInfo);
})

var loadPage = function () {
    findOperateInfo();
};
function findOperateInfoBefore(){
    $(".input-page").val(1);
    findOperateInfo();
}
function findOperateInfo() {
    $("#t1 tbody tr").remove();
    var pageNo = $(".input-page").val().trim();
    pageNo = pageNo == "" ? 1 : pageNo;
    var pageSize = $(".pageSize option:selected").val().trim();
    var user_name = $("#user_name").val().trim();
    var community = $("#user_address_community option:selected").val().trim();
    var building = $("#user_address_building option:selected").val().trim();
    var unit = $("#user_address_unit option:selected").val().trim();
    var room = $("#user_address_room option:selected").val().trim();
    var operator_name = "";
    var meter_no = $("#meter_no").val().trim();
    var startNo = (pageNo - 1) * pageSize;
    jQuery.ajax({
        url: "../motherMeterConf/qryShareInfo.do",
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
            if (result.status == 1) {
                var map = result.data;
                var info = map["pubReductionFees"];
                var totalNo = map["totalNo"];
                $("#totalNo").text(totalNo);
                var page = parseInt(totalNo / pageSize);
                var pageMax = totalNo % pageSize == 0 ? page : page + 1;
                $(".span4").next().text(pageMax);
                $("#t1").data("totalNo", totalNo);
                for (var i = 0; i < info.length; i++) {
                    var pef_allotType = info[i].pef_allotType;
                    var pef_belongPar = info[i].pef_belongPar;
                    if(pef_allotType ==="0"){
                        pef_allotType = "$mother_meter_confg_011";
                    }else if(pef_allotType ==="1"){
                        pef_allotType ="$mother_meter_confg_012";
                    }else if(pef_allotType ==="2"){
                        pef_allotType ="$mother_meter_confg_013";
                    }else if(pef_allotType ==="3"){
                        pef_allotType ="$parent_meter_confg_001";
                    }
                    if(pef_belongPar == null || pef_belongPar === "0"){
                        pef_belongPar = "";                       
                    }
                    var str = "";
                    str += '<tr><td>' + (startNo + i + 1) + '</td>';
                    str += '<td>' + info[i].pef_bussinessNum + '</td>';
                    str += '<td>' + info[i].pef_userName + '</td>';
                    str += '<td>' + info[i].pef_community + '</td>';
                    str += '<td>' + info[i].pef_buildNo + '</td>';
                    str += '<td>' + info[i].pef_unitNo + '</td>';
                    str += '<td>' + info[i].pef_roomNo + '</td>';
                    str += '<td>' + info[i].pef_meterNo + '</td>';
                    str += '<td>' + info[i].pef_rateType + '</td>';
                    str += '<td>' + info[i].pef_Total+ '</td>';
                    str += '<td>' + info[i].pef_shareSize + '</td>';
                    str += '<td>' + info[i].pef_shareFee + '</td>';
                    str += '<td>' + pef_allotType + '</td>';
                    str += '<td>' + pef_belongPar + '</td>';
                    str += '<td>' + info[i].pef_refund + '</td>';
                    str += '<td>' + info[i].pef_accountBalance + '</td>';
                    str += '<td>' + info[i].pef_applyTime + '</td>';                    
                    $("#t1 tbody").append($(str));
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
//function exportOperateInfo() {
//    var user_name = $("#user_name").val().trim();
//    var community = $("#user_address_community option:selected").val().trim();
//    var building = $("#user_address_building option:selected").val().trim();
//    var unit = $("#user_address_unit option:selected").val().trim();
//    var operator_name = $("#operator_name option:selected").val().trim();
//    var meter_no = $("#meter_no").val().trim();
//    var language = getCookie("language");
//    var totalNo = parseInt($("#totalNo").text().trim());
//    var fileType= "EXCEL";
//    var url = "../report/exportRechargeInfo.do?user_name=" + user_name + "&community=" + community + "&building=" + building
//            + "&unit=" + unit + "&operator_name=" + operator_name + "&meter_no=" + meter_no + "&language=" + language + "&totalNo=" + totalNo
//            + "&fileType=" + fileType;
//    window.location.href = url;
//    var mask = $("<div class='window_mask'></div>");
//    $("body").css("position", "relative");
//    mask.appendTo("body");
//    alert("$public_064");
//    var f = setInterval(function () {
//        jQuery.ajax({
//            url: "../report/checkStatus.do",
//            success: function (result) {
//                if (result.status == 1) {
//                    mask.remove();
//                    clearInterval(f);
//                }
//            },
//            error: function () {
//                alert("error");
//            }
//        });
//    }, 5000);
//}
