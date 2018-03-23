$(function () {
    $("#admin").text(account_name);
    $("#exit").click(logout);
    load_Sumtodo();
    findReadSuccessNo();
    findReadFailNo();
    findDeductFailNo();
    nextReadTime();
    urgeToDo();
    findInfo();
    rechargeReport();
    rechargeAtOneWeek();
    rechargeRanking();
    var lan_mark = $("#language_mark").text().trim();
    var lan_cookie = lan_mark == "Console_Joy AMR System" ? "English" : "Chinese";
    addCookie("language", lan_cookie, 24);
});

//查询抄表成功的数量
function findReadSuccessNo() {
    jQuery.ajax({
        url: "../user/findReadSuccessNo.do",
        type: "post",
        dataType: "json",
        success: function (result) {
            if (result.status === 1) {
                $("#clear1 p").text(result.data);
                $("#batteryLowNo p").text(0);
            }
        },
        error: function () {}
    });
}

//查询抄表失败的数量
function findReadFailNo() {
    jQuery.ajax({
        url: "../user/findReadFailNo.do",
        type: "post",
        dataType: "json",
        success: function (result) {
            if (result.status === 1) {
                $("#readFailNo p").text(result.data);
            }
        },
        error: function () {}
    });
}

//查询扣费失败的数量
function findDeductFailNo() {
    jQuery.ajax({
        url: "../user/findDeductFailNo.do",
        type: "post",
        dataType: "json",
        success: function (result) {
            if (result.status === 1) {
                $("#deductFailNo p").text(result.data);
            }
        },
        error: function () {}
    });
}

//下次抄表时间
function nextReadTime() {
    jQuery.ajax({
        url: "../user/showReadParameter.do",
        type: "post",
        dataType: "json",
        success: function (result) {
            if (result.status === 1) {
                var readParameters = result.data;
                if (readParameters == null) {
                    return;
                }
                var day = "01";
                var hour = "00";
                var minute = "00";
                var second = "00";
                for (var i = 0; i < readParameters.length; i++) {
                    var readParameter = readParameters[i];
                    var parameter_id = readParameter.parameter_id;
                    if (parameter_id === "1") {
                        day = readParameter.day;
                        hour = readParameter.hour;
                        minute = readParameter.minute;
                        second = readParameter.second;
                    }
                }
                var now = new Date();
                var setTime;
                if (day === "00") {
                    setTime = new Date(now.getFullYear(), now.getMonth(), now.getDate(), hour, minute, second);
                    if (now >= setTime) {
                        setTime = new Date(setTime.getTime() + 24 * 60 * 60 * 1000);
                    }
                } else {
                    setTime = new Date(now.getFullYear(), now.getMonth(), day, hour, minute, second);
                    if (now >= setTime) {
                        setTime = new Date(now.getFullYear(), now.getMonth() + 1, day, hour, minute, second);
                    }
                }
                var str = "$to_do_022" + format(setTime, "yyyy.MM.dd HH:mm:ss");
                str += '<a href="remote-read.html?class=active">$to_do_023</a>';
                $("#table1 tbody tr td span:eq(1)").html(str);
            }
        },
        error: function () {
            alert("error");
        }
    });
}

//尽快处理信息
function urgeToDo() {
    jQuery.ajax({
        url: "../user/urgeToDo.do",
        type: "post",
        dataType: "json",
        success: function (result) {
            if (result.status == 1) {
                var map = result.data;
                var count1 = map["meterChangeNo"];
                var count2 = map["userDelNo"];
                var count3 = map["overdueNo"];
                $("#clear2 div:eq(0) p").text(count1);
                $("#clear2 div:eq(1) p").text(count2);
                $("#clear2 div:eq(2) p").text(count3);
                addCookie("toDoNo", count1 + count2 + count3, 24);
                $("#sum-to-do").text("(" + (count1 + count2 + count3) + ")");
            }
        },
        error: function () {
            alert("error");
        }
    });
}

//小区楼栋单元等信息
function findInfo() {
    jQuery.ajax({
        url: "../user/findInfo.do",
        type: "post",
        dataType: "json",
        success: function (result) {
            if (result.status == 1) {
                var map = result.data;
                var communityNumber = map["communityNumber"];
                var buildingNumber = map["buildingNumber"];
                var unitNumber = map["unitNumber"];
                var mbusNumber = map["mbusNumber"];
                var meterNumber = map["meterNumber"];
                $("#t1 tbody tr:eq(1) td:eq(0)").text(communityNumber);
                $("#t1 tbody tr:eq(1) td:eq(1)").text(buildingNumber);
                $("#t1 tbody tr:eq(1) td:eq(2)").text(unitNumber);
                $("#t1 tbody tr:eq(1) td:eq(3)").text(mbusNumber);
                $("#t1 tbody tr:eq(1) td:eq(4)").text(meterNumber);
            }
        },
        error: function () {
            alert("error");
        }
    });
}

//function beenDone() {
//    jQuery.ajax({
//        url: "../user/beenDone.do",
//        type: "post",
//        dataType: "json",
//        success: function (result) {
//            if (result.status == 1) {
//                var map = result.data;
//                var rechargeNo = map["rechargeNo"];
//                var refundNo = map["refundNo"];
//                var changeMeterNo = map["changeMeterNo"];
//                var delUserNo = map["delUserNo"];
//            }
//        },
//        error: function () {
//            alert("error");
//        }
//    });
//}

//近期(七天内)充值信息
function rechargeReport() {
    jQuery.ajax({
        url: "../user/rechargeReport.do",
        type: "post",
        dataType: "json",
        success: function (result) {
            if (result.status == 1) {
                var map = result.data;
                var totalRecharge = map["totalRecharge"];
                var totalRefund = map["totalRefund"];
                $("#table2 div:eq(0) p").text(fix2(totalRecharge) + "$to_do_028");
                $("#table2 div:eq(1) p").text(fix2(totalRefund) + "$to_do_028");
            }
        },
        error: function () {
            alert("error");
        }
    });
}

//近期(七天内)充值报告
function rechargeAtOneWeek() {
    var date = new Date();
    var day0 = format(date.getTime(), "yyyy.MM.dd");
    var day1 = format(date.getTime() - 24 * 60 * 60 * 1000, "yyyy.MM.dd");
    var day2 = format(date.getTime() - 2 * 24 * 60 * 60 * 1000, "yyyy.MM.dd");
    var day3 = format(date.getTime() - 3 * 24 * 60 * 60 * 1000, "yyyy.MM.dd");
    var day4 = format(date.getTime() - 4 * 24 * 60 * 60 * 1000, "yyyy.MM.dd");
    var day5 = format(date.getTime() - 5 * 24 * 60 * 60 * 1000, "yyyy.MM.dd");
    var day6 = format(date.getTime() - 6 * 24 * 60 * 60 * 1000, "yyyy.MM.dd");
    $("#weeklyRecharge tbody tr:eq(0) td:eq(0)").text(day6);
    $("#weeklyRecharge tbody tr:eq(0) td:eq(1)").text(day5);
    $("#weeklyRecharge tbody tr:eq(0) td:eq(2)").text(day4);
    $("#weeklyRecharge tbody tr:eq(0) td:eq(3)").text(day3);
    $("#weeklyRecharge tbody tr:eq(0) td:eq(4)").text(day2);
    $("#weeklyRecharge tbody tr:eq(0) td:eq(5)").text(day1);
    $("#weeklyRecharge tbody tr:eq(0) td:eq(6)").text(day0);
    jQuery.ajax({
        url: "../user/rechargeReportAtOneWeek.do",
        type: "post",
        dataType: "json",
        success: function (result) {
            if (result.status == 1) {
                var map = result.data;
                var rechargeAmount0 = map["rechargeAmount0"];
                var rechargeAmount1 = map["rechargeAmount1"] - map["rechargeAmount0"];
                var rechargeAmount2 = map["rechargeAmount2"] - map["rechargeAmount1"];
                var rechargeAmount3 = map["rechargeAmount3"] - map["rechargeAmount2"];
                var rechargeAmount4 = map["rechargeAmount4"] - map["rechargeAmount3"];
                var rechargeAmount5 = map["rechargeAmount5"] - map["rechargeAmount4"];
                var rechargeAmount6 = map["rechargeAmount6"] - map["rechargeAmount5"];
                $("#weeklyRecharge tbody tr:eq(1) td:eq(0)").text(fix2(rechargeAmount6));
                $("#weeklyRecharge tbody tr:eq(1) td:eq(1)").text(fix2(rechargeAmount5));
                $("#weeklyRecharge tbody tr:eq(1) td:eq(2)").text(fix2(rechargeAmount4));
                $("#weeklyRecharge tbody tr:eq(1) td:eq(3)").text(fix2(rechargeAmount3));
                $("#weeklyRecharge tbody tr:eq(1) td:eq(4)").text(fix2(rechargeAmount2));
                $("#weeklyRecharge tbody tr:eq(1) td:eq(5)").text(fix2(rechargeAmount1));
                $("#weeklyRecharge tbody tr:eq(1) td:eq(6)").text(fix2(rechargeAmount0));
            }
        },
        error: function () {
            alert("error");
        }
    });
}

//小区充值排行
function rechargeRanking() {
    $("#table3 tbody tr").remove();
    jQuery.ajax({
        url: "../user/rechargeRanking.do",
        type: "post",
        dataType: "json",
        success: function (result) {
            if (result.status == 1) {
                var list = result.data;
                list.sort(function (a, b) {
                    return b[1] - a[1];
                });
                var str = "";
                for (var i = 0; i < list.length; i++) {
                    str += "<tr class='ch" + (i + 1) + "'><td><p>" + (i + 1) + "</p></td>";
                    str += "<td><span>" + list[i][0] + "</span></td><td><p>" + fix2(list[i][1]) + "$to_do_028</p></td></tr>";
                }
                $("#table3 tbody").append(str);
            }
        },
        error: function () {
            alert("error");
        }
    });
}

function fix2(n) {//保留2位小数
    return n === parseInt(n) ? n : n.toFixed(2);
}