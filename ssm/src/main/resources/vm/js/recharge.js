
$(function () {
    $("#admin").text(account_name);
    $("#sum-to-do").text("(" + toDoNo + ")");
    $("#exit").click(logout);
    rechargePermission();
    load_Sumtodo();
    load_communities();
    load_operators();
    findUser();
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
            var totalNo = parseInt($("#totalNo").text().trim());
            var pageSize = $(".pageSize option:selected").text().trim();
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
    $("#t1").on("click", "button", toRecharge);//点击充值按钮
    //$("#recharge_sure").click(recharge_confirm);
      $("#recharge_sure").click(totalMoney);

    
    $("#pay").click(pay);
    $("#recharge-success-panel").on("hidden.bs.modal", function () {
        window.location.href = "recharge.html";
    });
    $("#t1").on("click", "td", loadOperateInfo);
    $(".s1").click(nextPage2);
    $(".s2").click(prePage2);
    $(".s3").click(firstPage2);
    $(".s4").click(lastPage2);
    $("#input-page").keypress(function (event) {
        var keycode = (event.keyCode ? event.keyCode : event.which);
        if (keycode == 13) {
            var pageIndex = $(this).val().trim();
            pageIndex = parseInt(pageIndex);
            var totalNo = parseInt($("#totalNo2").text().trim());
            var pageSize = $(".pageSize2 option:selected").text().trim();
            var pageNo = parseInt(totalNo / pageSize);
            var pageMax = totalNo % pageSize == 0 ? pageNo : pageNo + 1;
            if (pageIndex <= 0) {
                alert("$public_038");
            } else if (pageIndex > pageMax) {
                alert("$public_039");
            } else {
                $("#input-page")[0].value = pageIndex;
                loadOperateInfo();
            }
            return false;
        }
    });
    $("#t7").on("click", ".refund", refund);
    $("#refund").click(refund_confirm);
    $("#return-success-panel").on("hidden.bs.modal", function () {
        window.location.href = "recharge.html";
    });
    $("#refund_cancel").click(function () {
        $("#close3").click();
    });
    //打印
    $("#t7,.modal-footer").on("click", ".print", function () {
        var that = this;
        print(getRechargeView(that));
    });
    //$(".modal-footer").on("click",".print", function(){alert($("#t4 td:eq(1)").text())});
    //$("iframe") && $("iframe").remove();
});



var loadPage = function () {//加载页面
    findUser();
};
function findUserBefore(){
    $(".input-page").val(1);
    findUser();
}
function findUser() {
    $("#t1 tbody tr").remove();
    $("#t7 tbody tr").remove();
    var pageNo = $(".input-page").val().trim();
    pageNo = pageNo == "" ? 1 : pageNo;
    var pageSize = $(".pageSize option:selected").val();
    var user_name = $("#user_name").val().trim();
    var community = $("#user_address_community option:selected").val().trim();
    var building = $("#user_address_building option:selected").val().trim();
    var unit = $("#user_address_unit option:selected").val().trim();
    var room = $("#user_address_room option:selected").val().trim();
    var operator_name = $("#operator_name option:selected").val().trim();
    var meter_no = $("#meter_no").val().trim();
    var startNo = (pageNo - 1) * pageSize;
    jQuery.ajax({
        url: "../user/findUser.do",
        type: "post",
        data: {
            "user_name": user_name,
            "user_address_community": community,
            "user_address_building": building,
            "user_address_unit": unit,
            "user_address_room":room,
            "operator_name": operator_name,
            "contact_info": "",
            "meter_no": meter_no,
            "status": "",
            "startNo": startNo,
            "pageSize": pageSize,
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
                $(".glyphicon.glyphicon-step-forward.span4").next().text(pageMax);
                $("#t1").data("totalNo", totalNo);
                for (var i = 0; i < users.length; i++) {
                    var user = users[i];
                    var user_id = user.user_id;
                    var meter_no = user.meter_no;
                    var user_name = user.user_name;
                    var meter_model = user.meter_model;
                    $("#t1").data("meter_model" + meter_no, meter_model);
                    var fee_type = user.fee_type;
                    $("#t1").data("fee_type" + meter_no, fee_type);
                    var meter_type = user.meter_type;
                    if (meter_type == 10) {
                        meter_type = "$user_add_017";
                    } else if (meter_type == 20) {
                        meter_type = "$user_add_018";
                    } else if (meter_type == 30) {
                        meter_type = "$remote_read_047_1";
                    } else if (meter_type == 40) {
                        meter_type = "$remote_read_047_2";
                    }
                    var province = user.province;
                    var city = user.city;
                    var district = user.district;
                    $("#t1").data("meter_model" + meter_no, meter_model);
                    $("#t1").data("fee_type" + meter_no, fee_type);
                    $("#t1").data("meter_type" + meter_no, meter_type);
                    $("#t1").data("province" + meter_no, province);
                    $("#t1").data("city" + meter_no, city);
                    $("#t1").data("district" + meter_no, district);
                    var user_address_area = user.user_address_area;
                    $("#t1").data("user_address_area" + meter_no, user_address_area);
                    var user_address_community = user.user_address_community;
                    $("#t1").data("user_address_community" + meter_no, user_address_community);
                    var user_address_building = user.user_address_building;
                    $("#t1").data("user_address_building" + meter_no, user_address_building);
                    var user_address_unit = user.user_address_unit;
                    $("#t1").data("user_address_unit" + meter_no, user_address_unit);
                    var user_address_room = user.user_address_room;
                    $("#t1").data("user_address_room" + meter_no, user_address_room);
                    var contact_info = user.contact_info;
                    var id_card_no = user.id_card_no;//新加
                    
                    var house_area = user.house_area;
                    var house_coefficient = user.house_coefficient;
                    var concentrator_name = user.concentrator_name;
                    var meter_status = user.meter_status;
                    $("#t1").data("meter_status" + meter_no, meter_status);
                    var user_status = user.user_status;
                    $("#t1").data("user_status" + meter_no, user_status);
                    var user_status_str = user_status == 0 ? "$user_manage_101" : "$user_manage_102";
                    var operator_account = user.operator_account;
                    var last_balance = user.last_balance;
                    if (last_balance !== parseInt(last_balance)) {
                        last_balance = last_balance.toFixed(2);
                    }
                    $("#t1").data("last_balance" + meter_no, last_balance);
                    var last_read_time = user.last_read_time;
                    $("#t1").data("last_read_time" + meter_no, last_read_time);
                    var supplier_name = user.supplier_name;
                    $("#t1").data("supplier_name" + meter_no, supplier_name);
                    var str = "";
                    str += '<tr><td>' + (startNo + (i + 1)) + '</td>';
                    str += '<td>' + user_name + '</td>';
                    str += '<td>' + user_address_community + '</td>';
                    str += '<td>' + user_address_building + '</td>';
                    str += '<td>' + user_address_unit + '</td>';
                    str += '<td>' + user_address_room + '</td>';
                    str += '<td>' + contact_info + '</td>';                 
                    str += '<td>' + id_card_no + '</td>';//新加    
                    str += '<td>' + concentrator_name + '</td>';
                    str += '<td>' + meter_no + '</td>';
                    str += '<td>' + fee_type + '</td>';
                    str += '<td>' + last_balance + '</td>';
                    str += '<td>' + user_status_str + '</td>';
                    str += '<td ><button class="button blue" type="button" data-toggle="modal" data-target="#recharge-panel">$recharge_012</button></td></tr>';
                    $("#t1 tbody").append($(str));
                }
                $("#t1 tbody tr:eq(0) td:eq(0)").click();
            } else {
                alert("$public_036");
            }
        },
        error: function () {
            alert("error");
        }
    });
}



function toRecharge() {
    var user_name = $(this).parents("tr").find("td:eq(1)").text().trim();//用户名称
    var meter_no = $(this).parents("tr").find("td:eq(9)").text().trim();//表出厂编号	//8
    var id_card_no = $(this).parents("tr").find("td:eq(7)").text().trim();//账号+新加
    var meter_type = $("#t1").data("meter_type" + meter_no);
    var meter_model = $("#t1").data("meter_model" + meter_no);
    var user_address_area = $("#t1").data("user_address_area" + meter_no);
    var user_address_community = $(this).parents("tr").find("td:eq(2)").val().trim();//小区
    var user_address_building = $(this).parents("tr").find("td:eq(3)").val().trim();//楼栋
    var user_address_unit = $(this).parents("tr").find("td:eq(4)").val().trim();//单元
    var user_address_room = $(this).parents("tr").find("td:eq(5)").val().trim();//房号
    var user_address = user_address_area + user_address_community
        + user_address_building + user_address_unit + user_address_room;
    var contact_info = $(this).parents("tr").find("td:eq(6)").text().trim();//联系电话
    var concentrator_name = $(this).parents("tr").find("td:eq(8)").text().trim();//网关名称//7
    var fee_type = $(this).parents("tr").find("td:eq(10)").text().trim();//费率类型//9
    $("#t1").data("fee_type" + meter_no, fee_type);
    $("#t1").data("concentrator_name" + meter_no, concentrator_name);
    var last_balance = $("#t1").data("last_balance" + meter_no);
    var last_read_time = $("#t1").data("last_read_time" + meter_no);//上次抄表时间
    if (last_read_time == null) {
        last_read_time = "";
    } else {
        last_read_time = format(last_read_time, 'yyyy.MM.dd HH:mm:ss');
    }
    //点击充值后模板上的属性值
    $("#t2 tbody tr:eq(0) td:eq(1)").text(user_name);
    $("#t2 tbody tr:eq(1) td:eq(1)").text(id_card_no);//新加
    $("#t2 tbody tr:eq(2) td:eq(1)").text(meter_type);//1
    $("#t2 tbody tr:eq(3) td:eq(1)").text(meter_no);//2
    $("#t2 tbody tr:eq(4) td:eq(1)").text(meter_model);//3
    $("#t2 tbody tr:eq(5) td:eq(1)").text(user_address);//4
    $("#t2 tbody tr:eq(6) td:eq(1)").text(contact_info);//5
    $("#t2 tbody tr:eq(7) td:eq(1)").text(last_balance + "$price_manage_025");//6
    $("#t2 tbody tr:eq(8) td:eq(1)").text(last_read_time);//7
    //document.getElementById("recharge_money").value = "";//
    document.getElementById("recharge_amount").value = "";//
}
//收费确认
function recharge_confirm() {
   // var recharge_money = $("#recharge_money").val().trim();
	 var recharge_money = $("#totalPrice").val().trim();
	 var basicPrice = $("#basicPrice").val().trim();
	 var disposingPrice = $("#disposingPrice").val().trim();
	 var otherPriceAll = $("#otherPriceAll").val().trim();
	 var extraCost = $("#extraCost").val().trim();
    if (!isNaN(recharge_money) && parseFloat(recharge_money) > 0) {
        $("#close1").click();
        $('#choose-pay-panel').modal('show');
    } else {
        alert("$recharge_077");//请输入正确的充值金额
    }
    $("#span1").text(recharge_money);
    $("#span2").text(basicPrice);
    $("#span3").text(disposingPrice);
    $("#span4").text(otherPriceAll);
    $("#span5").text(extraCost);
    var user_name = $("#t2 tbody tr:eq(0) td:eq(1)").text().trim();//
    var id_card_no= $("#t2 tbody tr:eq(1) td:eq(1)").text().trim();//账号+新加
    var meter_type = $("#t2 tbody tr:eq(2) td:eq(1)").text().trim();//1
    var meter_no = $("#t2 tbody tr:eq(3) td:eq(1)").text().trim();//2
    var meter_model = $("#t2 tbody tr:eq(4) td:eq(1)").text().trim();//3
    var user_address = $("#t2 tbody tr:eq(5) td:eq(1)").text().trim();//4
    var contact_info = $("#t2 tbody tr:eq(6) td:eq(1)").text().trim();//5
    var last_balance = $("#t2 tbody tr:eq(7) td:eq(1)").text().trim();//6
    var last_read_time = $("#t2 tbody tr:eq(8) td:eq(1)").text().trim();//7
    $("#t3 tbody tr:eq(0) td:eq(1)").text(user_name);//用户名称
    $("#t3 tbody tr:eq(1) td:eq(1)").text(id_card_no);//账号+新加
    $("#t3 tbody tr:eq(2) td:eq(1)").text(meter_type);//仪表类型1
    $("#t3 tbody tr:eq(3) td:eq(1)").text(meter_no);//原表出场编号2
    $("#t3 tbody tr:eq(4) td:eq(1)").text(meter_model);//仪表型号3
    $("#t3 tbody tr:eq(5) td:eq(1)").text(user_address);//用户地址4
    $("#t3 tbody tr:eq(6) td:eq(1)").text(contact_info);//联系电话5
    $("#t3 tbody tr:eq(7) td:eq(1)").text(last_balance);//账户余额6
    $("#t3 tbody tr:eq(8) td:eq(1)").text(last_read_time);//上次抄表时间7
}
function cancel() {
	$("#recharge_amount").val("");
	$("#basicPrice").val("");
	$("#disposingPrice").val("");
	$("#otherPriceAll").val("");
	$("#extraCost").val("");
	$("#totalPrice").val("");
}
//计算价格，计算出来才调用recharge_confirm 这个函数
function totalMoney(){
	if($("#rechargeByAmount").is(":checked")){
		var recharge_amount = $("#recharge_amount").val().trim();
		if(!isNaN(recharge_amount) && (parseFloat(recharge_amount) > 0)&&(/^([1-9][\d]{0,7}|0)(\.[\d]{1,1})?$/.test(recharge_amount))){
			var recharge_amount = $("#recharge_amount").val().trim()==null?"0":$("#recharge_amount").val().trim();
			var meter_no = $("#t2 tbody tr:eq(3) td:eq(1)").text().trim();
			//console.log("打印页面"+meter_no+"按照数量");
			jQuery.ajax({
		        url: "../wechatController/findNeedCost.do",
		        type: "post",
		        async:false,
		        data: {
		            "meterNo": meter_no,
		            "amount": recharge_amount,
		        },
		        dataType: "json",
		        success: function (result) {
		        	if (result.status == 1) {
		        		var map = result.data;
		        		var basicPrice = map["basicPrice"];
		        		var disposingPrice = map["disposingPrice"];
		        		var otherPriceAll = map["otherPriceAll"];
		        		var extraCost = map["extraCost"];
		        		var totalPrice = map["totalPrice"];
		        		$("#basicPrice").val(basicPrice);
		        		$("#disposingPrice").val(disposingPrice);
		        		$("#otherPriceAll").val(otherPriceAll);
		        		$("#totalPrice").val(totalPrice);
	        			$("#extraCost").val(extraCost);   
	        			recharge_confirm();//价格传入,开始调用函数	
		        	}else{
		        		alert(result.data);
		        	}
		        },
		        error: function () {
		            alert("error");
		        }
		    });
		}else{	
			//$("#close1").click();
			alert("请输入正确的充值数:");
		}
	}else{
//		var totalPrice = $("#totalPrice").val().trim()==null?"0":$("#totalPrice").val().trim();
		var totalPrice = $("#recharge_amount").val().trim();
		if(!isNaN(totalPrice) && parseFloat(totalPrice) > 0 && (/^([1-9][\d]{0,7}|0)(\.[\d]{1,2})?$/.test(totalPrice))){
			var totalPrice = $("#recharge_amount").val().trim()==null?"0":$("#recharge_amount").val().trim();
			var meter_no = $("#t2 tbody tr:eq(3) td:eq(1)").text().trim();
			//console.log("打印页面"+meter_no+"按照金额");
			jQuery.ajax({
		        url: "../wechatController/findAmountByPrice.do",
		        type: "post",
		        data: {
		            "meterNo": meter_no,
		            "totalPrice": totalPrice,
		        },
		        dataType: "json",
		        success: function (result) {
		        	if (result.status == 1) {
		        		var map = result.data;
		        		var basicPrice = map["basicPrice"];
		        		var disposingPrice = map["disposingPrice"];
		        		var otherPriceAll = map["otherPriceAll"];
		        		var extraCost = map["extraCost"];
		        		//var totalPrice = map["totalPrice"];
		        		var amount = map["amount"];
		        		$("#basicPrice").val(basicPrice);
		        		$("#disposingPrice").val(disposingPrice);
		        		$("#otherPriceAll").val(otherPriceAll);
	        			$("#totalPrice").val(totalPrice);
	        			$("#extraCost").val(extraCost);
	        			recharge_confirm();//价格传入,开始调用函数
		        	}else{
		        		alert(result.data);
		        	}
		        },
		        error: function () {
		            alert("error");
		        }
		    });
			
		}else{
			alert("请输入正确的充值数:");
		}	
	}
}

function paySuccess(){
      $("#paySuccess").click();
      $("#close2").click();
}
/**
 * 充值成功页面的数据加载
 * @returns {undefined}
 * 
 */
function pay() {
    var operate_id = "CZ" + format(new Date(), "yyyyMMddHHmmssS");
    var user_name = $("#t3 tbody tr:eq(0) td:eq(1)").text().trim();//
    var id_card_no= $("#t3 tbody tr:eq(1) td:eq(1)").text().trim();//账号+新加
    var meter_type = $("#t3 tbody tr:eq(2) td:eq(1)").text().trim();//1
    var meter_no = $("#t3 tbody tr:eq(3) td:eq(1)").text().trim();//2
    var meter_model = $("#t3 tbody tr:eq(4) td:eq(1)").text().trim();//3
    var user_address = $("#t3 tbody tr:eq(5) td:eq(1)").text().trim();//4
    var contact_info = $("#t3 tbody tr:eq(6) td:eq(1)").text().trim();//5
    var fee_type = $("#t1").data("fee_type" + meter_no);
    var recharge_loc = $("#t1").data("supplier_name" + meter_no);
    var recharge_money = $("#span1").text().trim();
    var province = $("#t1").data("province" + meter_no);
    var city = $("#t1").data("city" + meter_no);
    var district = $("#t1").data("district" + meter_no);
    var user_address_area = $("#t1").data("user_address_area" + meter_no);
    var user_address_community = $("#t1").data("user_address_community" + meter_no);
    var user_address_building = $("#t1").data("user_address_building" + meter_no);
    var user_address_unit = $("#t1").data("user_address_unit" + meter_no);
    var user_address_room = $("#t1").data("user_address_room" + meter_no);
    var concentrator_name = $("#t1").data("concentrator_name" + meter_no);
    var operate_type = "1";
    var operator_account = getCookie("account_name");
    var pay_method = $(":radio[name=optionsRadios]:checked").val();
    $("#t1").data("pay_method" + meter_no, pay_method);
    var isPrint = "0";
    var operate_time = format(new Date(), 'yyyy-MM-dd HH:mm:ss');
    var last_balance = $("#t3 tbody tr:eq(7) td:eq(1)").text().trim();//6
    var current_balance = parseFloat(last_balance) + parseFloat(recharge_money);
    current_balance = current_balance == parseInt(current_balance) ? current_balance : current_balance.toFixed(2);    
  //支付成功后打印页面数据展示
    $("#t4 tbody tr:eq(0) td:eq(1)").text(operate_id);
    $("#t4 tbody tr:eq(1) td:eq(1)").text(user_name);
    $("#t4 tbody tr:eq(2) td:eq(1)").text(meter_type);
    $("#t4 tbody tr:eq(3) td:eq(1)").text(meter_no);
    $("#t4 tbody tr:eq(4) td:eq(1)").text(meter_model);
    $("#t4 tbody tr:eq(5) td:eq(1)").text(user_address);
    $("#t4 tbody tr:eq(6) td:eq(1)").text(contact_info);
    $("#t5 tbody tr:eq(0) td:eq(1)").text(recharge_money + "$recharge_059");
    $("#t5 tbody tr:eq(1) td:eq(1)").text("$recharge_012");
    $("#t5 tbody tr:eq(2) td:eq(1)").text(operator_account);
    $("#t5 tbody tr:eq(3) td:eq(1)").text(recharge_loc);
    $("#t5 tbody tr:eq(4) td:eq(1)").text(format(new Date(operate_time),"yyyy.MM.dd HH:mm:ss"));
    $("#t6 tbody tr:eq(0) td:eq(1)").text(current_balance + "$recharge_059"); 
    if(!(pay_method=='现金'||pay_method=='Cash')){
    	jQuery.ajax({
            url: "../user/payOnLine.do",
            type: "post",
            //async : false,
            data: {
                "operate_id": operate_id,
                "user_name": user_name,
                "province": province,
                "city": city,
                "district": district,
                "user_address_area": user_address_area,
                "user_address_community": user_address_community,
                "user_address_building": user_address_building,
                "user_address_unit": user_address_unit,
                "user_address_room": user_address_room,
                "contact_info": contact_info,
                "concentrator_name": concentrator_name,
                "meter_model": meter_model,
                "meter_type": meter_type,
                "meter_no": meter_no,
                "fee_type": fee_type,
                "recharge_money": recharge_money,
                "operate_type": operate_type,
                "balance": current_balance,
                "operator_account": operator_account,
                "recharge_loc": recharge_loc,
                "pay_method": pay_method,
                "isPrint": isPrint,
                "id_card_no": id_card_no,
                "operate_time":operate_time,//操作时间
            },
            dataType: "json",
            success: function (result) {
            	if (result.status == 1) {
//                  $("#close2").click();
               var obj= window.open("about:blank");
               var html="";
                 html += "<html><head></head><body><form id='formid' method='post' action='"    + result.postUrl + "'>";
                 html += "<input type='hidden' name='cjoyId' value='" + result.cjoyId + "'/>";
                 html += "<input type='hidden' name='meterNo' value='" + result.meterNo + "'/>";
                 html += "<input type='hidden' name='money' value='" + result.money + "'/>";
                 html += "<input type='hidden' name='access_token' value='" + result.access_token + "'/>";
                 html += "<input type='hidden' name='style' value='" + result.style + "'/>";
                 html += "<input type='hidden' name='returnUrl' value='" + result.returnUrl + "'/>";
                 html += "<input type='hidden' name='time' value='" + operate_time + "'/>";
                 html += "<input type='hidden' name='operateId' value='" + result.operateId + "'/>";
                 html += "</form><script type='text/javascript'>document.getElementById(\"formid\").submit()</script></body></html>";
                  obj.document.write(html);
                  return obj;
              }
            },
            error: function () {
                alert("error");
            }
        });   	
    }
    else{
    	jQuery.ajax({
            url: "../user/payOnCash.do",
            type: "post",
            //async : false,
            data: {
                "operate_id": operate_id,
                "user_name": user_name,
                "province": province,
                "city": city,
                "district": district,
                "user_address_area": user_address_area,
                "user_address_community": user_address_community,
                "user_address_building": user_address_building,
                "user_address_unit": user_address_unit,
                "user_address_room": user_address_room,
                "contact_info": contact_info,
                "concentrator_name": concentrator_name,
                "meter_model": meter_model,
                "meter_type": meter_type,
                "meter_no": meter_no,
                "fee_type": fee_type,
                "recharge_money": recharge_money,
                "operate_type": operate_type,
                "balance": current_balance,
                "operator_account": operator_account,
                "recharge_loc": recharge_loc,
                "pay_method": pay_method,
                "isPrint": isPrint,
                "id_card_no": id_card_no,
                "operate_time":operate_time,//操作时间
            },
            dataType: "json",
            success: function (result) {
            	if (result.status == 1) {
            		$("#paySuccess").click();
              }
            },
            error: function () {
                alert("error");
            }
        }); 
    }
    
    $("#close2").click();
}


/**    $("#t6 tbody tr:eq(0) td:eq(1)").text(current_balance + "$recharge_059");
 * 操作明细数据加载
 * @returns {undefined}
 */
function loadOperateInfo() {
    var user_name = $(this).parents("tr").find("td:eq(1)").text().trim();
    var meter_no  = $(this).parents("tr").find("td:eq(9)").text().trim();//8
    if (meter_no == "") {
        meter_no = $("#t7 tbody tr:eq(0) td:eq(2)").text().trim();
    }
    var last_balance = $(this).parents("tr").find("td:eq(11)").text().trim();//10
    if (last_balance == "") {
        last_balance = $("#t7").data("last_balance");
    }
    $("#t7").data("last_balance", last_balance);
    $("#t7 tbody tr").remove();
    var pageNo = $("#input-page").val().trim();
    pageNo = pageNo == "" ? 1 : pageNo;
    var pageSize = $(".pageSize2 option:selected").val().trim();
    var startNo = pageSize * (pageNo - 1);
    jQuery.ajax({
        url: "../user/loadOperateInfoByMeterNo.do",
        type: "post",
        data: {
            "meter_no": meter_no,
            "startNo": startNo,
            "pageSize": pageSize,
        },
        dataType: "json",
        success: function (result) {
            if (result.status == 1) {
                var map = result.data;
                var information = map["information"];
                var totalNo = map["totalNo"];
                $("#totalNo2").text(totalNo);
                var page = parseInt(totalNo / pageSize);
                var pageMax = totalNo % pageSize == 0 ? page : page + 1;
                $(".glyphicon.glyphicon-step-forward.s4").next().text(pageMax);
                for (var i = 0; i < information.length; i++) {
                    var info = information[i];
                    var operate_id = info.operate_id;
                    var user_name = info.user_name;
                    var meter_model = info.meter_model;
                    $("#t7").data("meter_model" + meter_no, meter_model);
                    var meter_type = info.meter_type;
                    var meter_no = info.meter_no;
                    var meter_status = $("#t1").data("meter_status" + meter_no);
                    var user_status = $("#t1").data("user_status" + meter_no);
                    var operate_money = info.recharge_money;
                    var pay_method = info.pay_method;
                    var operate_type = info.operate_type;
                    var operate_type_str;
                    if (operate_type == 1) {//充值
                        operate_type_str = "$recharge_052";
                    } else if (operate_type == 0) {//退费
                        operate_type_str = "$recharge_066";
                    } else if(operate_type == 2) {
                        operate_type_str = "$recharge_084";//抄表扣费
                    }else if(operate_type == 3){//公摊扣费
                       operate_type_str = "$recharge_086";
                    }else {
                         operate_type_str = "$recharge_087";//公摊退费
                    }
                    var balance = info.balance;
                    var fee_type = info.fee_type;
                    $("#t7").data("fee_type" + meter_no, fee_type);
                    var operate_time = info.operate_time;
                    var operator_account = info.operator_account;
                    operate_time = format(operate_time, 'yyyy.MM.dd HH:mm:ss');
                    var user_address = info.user_address_area;
                    user_address += info.user_address_community;
                    user_address += info.user_address_building;
                    user_address += info.user_address_unit;
                    user_address += info.user_address_room;
                    $("#t7").data("user_name" + operate_id, user_name);
                    $("#t7").data("meter_no" + operate_id, meter_no);
                    $("#t7").data("operate_money" + operate_id, operate_money);
                    $("#t7").data("pay_method" + operate_id, pay_method);
                    $("#t7").data("operate_type" + operate_id, operate_type);
                    $("#t7").data("fee_type" + operate_id, fee_type);
                    $("#t7").data("operate_time" + operate_id, operate_time);
                    $("#t7").data("operator_account" + operate_id, operator_account);
                    $("#t7").data("user_address" + meter_no, user_address);
                    var contact_info = info.contact_info;
                    $("#t7").data("contact_info" + meter_no, contact_info);
                    var concentrator_name = info.concentrator_name;
                    $("#t7").data("concentrator_name" + meter_no, concentrator_name);
                    var recharge_loc = info.recharge_loc;
                    $("#t7").data("recharge_loc" + meter_no, recharge_loc);
                    $("#t7").data("meter_model" + meter_no, meter_model);
                    $("#t7").data("meter_type" + meter_no, meter_type);
                    var str = "";
                    str += '<tr><td>' + operate_id + '</td>';
                    str += '<td>' + user_name + '</td>';
                    str += '<td>' + meter_no + '</td>';
                    str += '<td>' + operate_money + '</td>';
                    str += '<td>' + pay_method + '</td>';
                    str += '<td>' + operate_type_str + '</td>';
                    str += '<td>' + balance + '</td>';
                    str += '<td>' + fee_type + '</td>';
                    str += '<td>' + operate_time + '</td>';
                    str += '<td>' + operator_account + '</td>';
                    if (operate_type == "0") {
                        str += '<td><span   style="visibility:hidden;">$recharge_066</span>&nbsp;&nbsp;<a style="cursor:pointer" class="print">$recharge_072</a></td></tr>';
                    } else {
                        if (user_status == "1" && meter_status == "1") {
                            str += '<td><a class="refund" data-toggle="modal" href="#return-panel">$recharge_066</a>&nbsp;&nbsp;<a style="cursor:pointer" class="print">$recharge_072</a></td></tr>';
                        } else {
                            str += '<td><a style="cursor:text;color:#afafaf" class="refund disableCss" data-toggle="modal" href="#">$recharge_066</a>&nbsp;&nbsp;<a style="cursor:pointer" class="print">$recharge_072</a></td></tr>';
                        }
                    }
                    $("#t7 tbody").append($(str));
                }
            }
        },
        error: function () {
            alert("error");
        }
    });
}

function nextPage2() {
    var pageIndex = $("#input-page").val().trim();
    pageIndex = parseInt(pageIndex) + 1;
    var totalNo = parseInt($("#totalNo2").text().trim());
    var pageSize = $(".pageSize2 option:selected").val().trim();
    var pageNo = parseInt(totalNo / pageSize);
    var pageMax = totalNo % pageSize == 0 ? pageNo : pageNo + 1;
    if (pageIndex <= 0) {
        alert("$public_038");
    } else if (pageIndex > pageMax) {
        alert("$public_039");
    } else {
        $("#input-page")[0].value = pageIndex;
        loadOperateInfo();
    }
}

function prePage2() {
    var pageIndex = $("#input-page").val().trim();
    pageIndex = parseInt(pageIndex) - 1;
    if (pageIndex <= 0) {
        alert("$public_040");
    } else {
        $("#input-page")[0].value = pageIndex;
        loadOperateInfo();
    }
}

function firstPage2() {
    $("#input-page")[0].value = 1;
    loadOperateInfo();
}

function lastPage2() {
    var totalNo = parseInt($("#totalNo2").text().trim());
    var pageSize = $(".pageSize2 option:selected").val().trim();
    var pageNo = parseInt(totalNo / pageSize);
    var pageEnd = totalNo % pageSize == 0 ? pageNo : pageNo + 1;
    $("#input-page")[0].value = pageEnd;
    loadOperateInfo();
}
//退费
function refund() {
    var user_name = $(this).parents("tr").find("td:eq(1)").text().trim();
    var meter_model = $("#t7").data("meter_model" + meter_no);
    var meter_no = $(this).parents("tr").find("td:eq(2)").text().trim();
    var pay_method = $(this).parents("tr").find("td:eq(4)").text().trim();
    $("#t7").data("pay_method" + meter_no, pay_method);
    var meter_type = $("#t7").data("meter_type" + meter_no);
    var user_address = $("#t7").data("user_address" + meter_no);
    var contact_info = $("#t7").data("contact_info" + meter_no);
    var last_balance = $("#t7").data("last_balance");
    var last_recharge_time = $(this).parents("tr").find("td:eq(8)").text().trim();//7
    $("#t8 tbody tr:eq(0) td:eq(1)").text(user_name);
    $("#t8 tbody tr:eq(1) td:eq(1)").text(meter_type);
    $("#t8 tbody tr:eq(2) td:eq(1)").text(meter_no);
    $("#t8 tbody tr:eq(3) td:eq(1)").text(meter_model);
    $("#t8 tbody tr:eq(4) td:eq(1)").text(user_address);
    $("#t8 tbody tr:eq(5) td:eq(1)").text(contact_info);
    $("#t8 tbody tr:eq(6) td:eq(1)").text(last_balance);
    $("#t8 tbody tr:eq(7) td:eq(1)").text(last_recharge_time);
    document.getElementById("refund_money").value = "";
}
//退费确认
function refund_confirm() {
    if (confirm("$recharge_080")) {
        var operate_id = "TF" + format(new Date(), "yyyyMMddHHmmssS");
        var user_name = $("#t8 tbody tr:eq(0) td:eq(1)").text().trim();
        var meter_type = $("#t8 tbody tr:eq(1) td:eq(1)").text().trim();
        var meter_no = $("#t8 tbody tr:eq(2) td:eq(1)").text().trim();
        var meter_model = $("#t8 tbody tr:eq(3) td:eq(1)").text().trim();
        var user_address = $("#t8 tbody tr:eq(4) td:eq(1)").text().trim();
        var contact_info = $("#t8 tbody tr:eq(5) td:eq(1)").text().trim();
        var province = $("#t1").data("province" + meter_no);
        var city = $("#t1").data("city" + meter_no);
        var district = $("#t1").data("district" + meter_no);
        var user_address_area = $("#t1").data("user_address_area" + meter_no);
        var user_address_community = $("#t1").data("user_address_community" + meter_no);
        var user_address_building = $("#t1").data("user_address_building" + meter_no);
        var user_address_unit = $("#t1").data("user_address_unit" + meter_no);
        var user_address_room = $("#t1").data("user_address_room" + meter_no);
        var fee_type = $("#t7").data("fee_type" + meter_no);
        var concentrator_name = $("#t7").data("concentrator_name" + meter_no);
        var refund_money = $("#refund_money").val().trim();
        var check = false;
        var max = $("#t8 tbody tr:eq(6) td:eq(1)").text().trim();
        if (refund_money == "" || isNaN(refund_money) || parseFloat(refund_money) <= 0) {
            alert("$recharge_077");
            $('#return-success-panel').modal('hide');
        } else if (parseFloat(refund_money) > parseFloat(max)) {
            alert("$recharge_081");
            $('#return-success-panel').modal('hide');
        } else {
            check = true;
            $('#return-success-panel').modal('show');
        }
        var operate_type = "0";
        var operate_type_str = "$recharge_066";
        var operator_account = getCookie("account_name");
        var operate_time = format(new Date(), 'yyyy-MM-dd HH:mm:ss');
        var recharge_loc = $("#t7").data("recharge_loc" + meter_no);
        var total_money = $("#t7").data("last_balance");
        var current_balance = parseFloat(total_money) - parseFloat(refund_money);
        current_balance = current_balance.toFixed(2);
        var isPrint = "0";
        var pay_method = "$recharge_063";
        $("#t9 tbody tr:eq(0) td:eq(1)").text(user_name);
        $("#t9 tbody tr:eq(1) td:eq(1)").text(meter_type);
        $("#t9 tbody tr:eq(2) td:eq(1)").text(meter_no);
        $("#t9 tbody tr:eq(3) td:eq(1)").text(meter_model);
        $("#t9 tbody tr:eq(4) td:eq(1)").text(user_address);
        $("#t9 tbody tr:eq(5) td:eq(1)").text(contact_info);
        $("#t10 tbody tr:eq(0) td:eq(1)").text(refund_money + "$recharge_059");
        $("#t10 tbody tr:eq(1) td:eq(1)").text(operate_type_str);
        $("#t10 tbody tr:eq(2) td:eq(1)").text(operator_account);
        $("#t10 tbody tr:eq(3) td:eq(1)").text(recharge_loc);
        $("#t10 tbody tr:eq(4) td:eq(1)").text(format(new Date(operate_time),"yyyy.MM.dd HH:mm:ss"));
        $("#t11 tbody tr:eq(0) td:eq(1)").text(current_balance + "$recharge_059");
        if (check) {
            jQuery.ajax({
                url: "../user/refund.do",
                type: "post",
                data: {
                    "operate_id": operate_id,
                    "user_name": user_name,
                    "province": province,
                    "city": city,
                    "district": district,
                    "user_address_area": user_address_area,
                    "user_address_community": user_address_community,
                    "user_address_building": user_address_building,
                    "user_address_unit": user_address_unit,
                    "user_address_room": user_address_room,
                    "contact_info": contact_info,
                    "concentrator_name": concentrator_name,
                    "meter_model": meter_model,
                    "meter_type": meter_type,
                    "meter_no": meter_no,
                    "fee_type": fee_type,
                    "recharge_money": refund_money,
                    "operate_type": operate_type,
                    "balance": current_balance,
                    "operator_account": operator_account,
                    "recharge_loc": recharge_loc,
                    "pay_method": pay_method,
                    "isPrint": isPrint,
                    "operate_time":operate_time,
                },
                dataType: "json",
                success: function (result) {
                    if (result.status == 1) {
                        $("#close3").click();
                        var str = "";
                        str += '<tr><td>' + operate_id + '</td>';
                        str += '<td>' + user_name + '</td>';
                        str += '<td>' + meter_no + '</td>';
                        str += '<td>' + refund_money + '</td>';
                        str += '<td>' + pay_method + '</td>';
                        str += '<td>' + operate_type_str + '</td>';
                        str += '<td>' + fee_type + '</td>';
                        str += '<td>' + format(new Date(operate_time),"yyyy.MM.dd HH:mm:ss") + '</td>';
                        str += '<td>' + operator_account + '</td>';
                        str += '<td>&nbsp;&nbsp;&nbsp;&nbsp;<a style="cursor:pointer" class="print">$recharge_072</a></td></tr>';
                        $("#t7 tbody").append($(str));
                    }
                },
                error: function () {
                    alert("error");
                }
            });
        }
    }
}

/**
 * 提供Recharge页面的打印数据
 * @param {type} that 当前对象
 * @returns {Array|getRechargeView.view} 
 */
function getRechargeView(that) {
    var operate_id, user_name, meter_no, operate_money, pay_method,
        operate_type, fee_type, operate_time, operator_account, balance;
    operate_id = $(that).parents("tr").find("td:eq(0)").text().trim();
    if (operate_id !== "") {
        user_name = $(that).parents("tr").find("td:eq(1)").text().trim();
        meter_no = $(that).parents("tr").find("td:eq(2)").text().trim();
        operate_money = $(that).parents("tr").find("td:eq(3)").text().trim();
        pay_method = $(that).parents("tr").find("td:eq(4)").text().trim();
        operate_type = $(that).parents("tr").find("td:eq(5)").text().trim();
        fee_type = $(that).parents("tr").find("td:eq(7)").text().trim();
        operate_time = $(that).parents("tr").find("td:eq(8)").text().trim();
        operator_account = $(that).parents("tr").find("td:eq(9)").text().trim();
        balance = $(that).parents("tr").find("td:eq(6)").text().trim();

    } else if (operate_id === "") {
        operate_id = $("#t4 tr:eq(0) td:eq(1)").text().trim();
        if (operate_id !== "") {
            user_name = $("#t4 tr:eq(1) td:eq(1)").text().trim();
            meter_no = $("#t4 tr:eq(3) td:eq(1)").text().trim();
            operate_money = $("#t5 tr:eq(0) td:eq(1)").text().trim();
            pay_method = $("#t1").data("pay_method" + meter_no);
            operate_type = $("#t5 tr:eq(1) td:eq(1)").text().trim();
            fee_type = $("#t1").data("fee_type" + meter_no)
            operate_time = $("#t5 tr:eq(4) td:eq(1)").text().trim();
            operator_account = $("#t5 tr:eq(2) td:eq(1)").text().trim();
            balance = $("#t6 tr:eq(0) td:eq(1)").text().trim();
        } else if (operate_id === "") {
            operate_id = $("#t9 tr:eq(0) td:eq(1)").text().trim();
            user_name = $("#t9 tr:eq(1) td:eq(1)").text().trim();
            meter_no = $("#t9 tr:eq(2) td:eq(1)").text().trim();
            operate_money = $("#t10 tr:eq(0) td:eq(1)").text().trim();
            pay_method = $("#t7").data("pay_method" + meter_no);
            operate_type = $("#t10 tr:eq(1) td:eq(1)").text().trim();
            fee_type = $("#t1").data("fee_type" + meter_no);
            operate_time = $("#t10 tr:eq(4) td:eq(1)").text().trim();
            operator_account = $("#t10 tr:eq(2) td:eq(1)").text().trim();
            balance = $("#t11 tr:eq(0) td:eq(1)").text().trim();
        }
    }
    var view = [];
    view.push("<td width='200px'>" + $("#t7 th:eq(0)").text().trim() + ":</td><td>" + operate_id + "</td>");
    view.push("<td>" + $("#t7 th:eq(1)").text().trim() + ":</td><td>" + user_name + "</td>");
    view.push("<td>" + $("#t7 th:eq(2)").text().trim() + ":</td><td>" + meter_no + "</td>");
    view.push("<td>" + $("#t7 th:eq(3)").text().trim() + ":</td><td>" + operate_money + "</td>");
    view.push("<td>" + $("#t7 th:eq(4)").text().trim() + ":</td><td>" + pay_method + "</td>");
    view.push("<td>" + $("#t7 th:eq(5)").text().trim() + ":</td><td>" + operate_type + "</td>");
    view.push("<td>" + $("#t7 th:eq(7)").text().trim() + ":</td><td>" + fee_type + "</td>");
    view.push("<td>" + $("#t7 th:eq(8)").text().trim() + ":</td><td>" + operate_time + "</td>");
    view.push("<td>" + $("#t7 th:eq(9)").text().trim() + ":</td><td>" + operator_account + "</td>");
    view.push("<td>" + $("#t7 th:eq(6)").text().trim() + ":</td><td>" + balance + "</td>");
    return view;
}

/**
 * 打印接口
 * @param {type} view 打印参数
 * @returns {print}
 */
function print(view) {
    this.view = view;
    var divStr = '<div width="200px"><table><tbody>';
    for (var i = 0; i < view.length; i++) {
        divStr += ('<tr>' + view[i] + '</tr>');
    }
    divStr += '</tbody></table></div>';
    var printArea = $(divStr);
    printArea.jqprint({ importCSS: false });
    setTimeout(function () {
        var iframe = $("iframe");
        iframe && iframe.remove();
    }, 2000);
}