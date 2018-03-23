$(function () {
    $("#admin").text(account_name);
    $("#sum-to-do").text("(" + toDoNo + ")");
    $("#exit").click(logout);
    rechargeListPermission();
    load_Sumtodo();
    load_communities();//加载小区
    load_operators();
    loadOperationmethod();//加载操作方法
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
    $("#import-pay-record").click(exportOperateInfo);
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
    var operator_name = $("#operator_name option:selected").val().trim();
    var meter_no = $("#meter_no").val().trim();
    var payMethod = $("#payMethod option:selected").val().trim();//支付方式
    var operationMethod =  $("#operationMethod option:selected").val().trim();//操作方式
    var startTime = $("#startTime").val().trim();//开始时间
    var endTime = $("#endTime").val().trim();//结束时间
    var startNo = (pageNo - 1) * pageSize;
    jQuery.ajax({
        url: "../user/zuoQuanFindOperateInfo.do",
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
            "payMethod":payMethod,
            "operationMethod":operationMethod,
            "startTime":startTime,
            "endTime":endTime
        },
        dataType: "json",
        success: function (result) {
            if (result.status == 1) {
                var map = result.data;
                var info = map["rechargeInfo"];
                var totalNo = map["totalNo"];
                $("#totalNo").text(totalNo);
                var page = parseInt(totalNo / pageSize);
                var pageMax = totalNo % pageSize == 0 ? page : page + 1;
                $(".span4").next().text(pageMax);
                $("#t1").data("totalNo", totalNo);
                for (var i = 0; i < info.length; i++) {
                    var operate_time = info[i].operate_time;
                    var operate_type = info[i].operate_type;
                     if (operate_type === "1") {//充值
                        operate_type = "$recharge_052";
                    } else if (operate_type === "0") {//退费
                        operate_type = "$recharge_066";
                    } else if(operate_type ==="2") {
                        operate_type = "$recharge_084";//抄表扣费
                    }else if(operate_type === "3"){//公摊扣费
                       operate_type = "$recharge_086";
                    }else {
                         operate_type = "$recharge_087";//公摊退费
                    }
                    operate_time = format(operate_time, 'yyyy.MM.dd HH:mm:ss');
                    var balance = info[i].balance == parseInt(info[i].balance) ? info[i].balance : info[i].balance.toFixed(2);
                    var str = "";
                    str += '<tr><td>' + (startNo + i + 1) + '</td>';
                    str += '<td>' + info[i].operate_id + '</td>';
                    str += '<td>' + info[i].user_name + '</td>';
                    str += '<td>' + info[i].user_address_community + '</td>';
                    str += '<td>' + info[i].user_address_building + '</td>';
                    str += '<td>' + info[i].user_address_unit + '</td>';
                    str += '<td>' + info[i].user_address_room + '</td>';
                    str += '<td>' + info[i].contact_info + '</td>';
                    str += '<td>' + info[i].concentrator_name + '</td>';
                    str += '<td>' + info[i].meter_no + '</td>';
                    str += '<td>' + info[i].fee_type + '</td>';
                    str += '<td>' + info[i].recharge_money + '</td>';
                    str += '<td>' + balance + '</td>';
                    str += '<td>' + operate_type + '</td>';
                    str += '<td>' + info[i].operator_account + '</td>';
                    str += '<td>' + operate_time + '</td>';
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
function exportOperateInfo() {
    var user_name = $("#user_name").val().trim();
    var community = $("#user_address_community option:selected").val().trim();
    var building = $("#user_address_building option:selected").val().trim();
    var unit = $("#user_address_unit option:selected").val().trim();
    var room = $("#user_address_room option:selected").val().trim();
    var operator_name = $("#operator_name option:selected").val().trim();
    var meter_no = $("#meter_no").val().trim();
    var language = getCookie("language");
    var totalNo = parseInt($("#totalNo").text().trim());
    var fileType= "EXCEL";
    var url = "../report/exportRechargeInfo.do?user_name=" + user_name + "&community=" + community + "&building=" + building
            + "&unit=" + unit + "&room=" + room + "&operator_name=" + operator_name + "&meter_no=" + meter_no + "&language=" + language + "&totalNo=" + totalNo
            + "&fileType=" + fileType;
    window.location.href = url;
    var mask = $("<div class='window_mask'></div>");
    $("body").css("position", "relative");
    mask.appendTo("body");
    alert("$public_064");
    var f = setInterval(function () {
        jQuery.ajax({
            url: "../report/checkStatus.do",
            success: function (result) {
                if (result.status == 1) {
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
function loadOperationmethod() {
	$("#operationMethod").empty();
	$("#operationMethod").append($("<option value=''>操作类型</option>"));
	jQuery.ajax({
		url:"../user/loadOperationMethod.do",
		type: "post",
	    async: false,
	    dataType: "json",
		success: function (result) {
			if(result.status==1){
				var list = result.data;
				for(var i = 0; i< list.length; i++){
					var operate_type = list[i];
					if(operate_type == 0){
						operate_type_str = "退费";
					}else if(operate_type == 1){
						operate_type_str = "充值";
					}else if(operate_type == 2){
						operate_type_str = "抄表扣费";
					}else if(operate_type == 3){
						operate_type_str = "公摊扣费";
					}else{
						operate_type_str = "公摊退费";
					}
					var str = "<option value='" + operate_type + "'>" + operate_type_str + "</option>";
					$("#operationMethod").append(str);
				}
			}
		},
		error:function(){
			alert("error");
		}
	});
}


