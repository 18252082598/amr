//数据统计页面
$(function () {
    $("#admin").text(account_name);
    $("#sum-to-do").text("(" + toDoNo + ")");
    $("#exit").click(logout);
    load_Sumtodo();
    dataAnalyzePermission();
    BusinessQueries();
    rechargeRefundAnalyze();
});

//本年度业务统计
function BusinessQueries() {
    var province = $("#add5").val().trim();
    var city = $("#add6").val().trim();
    var district = $("#add7").val().trim();
    var beginDate = $("#beginDate").val().trim();
    var endDate = $("#endDate").val().trim();
    var rechargeNo = 0;
    var refundNo = 0;
    var changeMeterNo = 0;
    var delUserNo = 0;
    var dataArr = [];
    var titleText = "业务量统计";
    if (district !== "$user_manage_148") {
        titleText = district + titleText;
    }
    //按时间(月)查找业务办理量
    jQuery.ajax({
        url: "../user/BusinessQueries.do",
        type: "post",
        data: {
            "province": province,
            "city": city,
            "district": district,
            "beginDate": beginDate,
            "endDate": endDate
        },
        dataType: "json",
        async: false,
        traditional: true,
        success: function (result) {
            if (result.status === 1) {
                var map = result.data;
                rechargeNo = map["rechargeNo"];
                refundNo = map["refundNo"];
                changeMeterNo = map["changeMeterNo"];
                delUserNo = map["delUserNo"];
                dataArr.push(rechargeNo);
                dataArr.push(refundNo);
                dataArr.push(changeMeterNo);
                dataArr.push(delUserNo);
            } else {
                alert("$public_066");
            }
        },
        error: function () {
            alert("error");
        }
    });

    require.config({
        paths: {
            echarts: '../echarts'
        }
    });
    require(
            [
                'echarts',
                'echarts/chart/bar',
                'echarts/chart/line'
            ],
            function (ec) {
                var myChart = ec.init(document.getElementById('echarts'));
                var option = {
                    title: {
                        x: "center",
                        text: titleText,
                        textStyle: {
                            fontSize: 18,
                            fontWeight: 'bolder',
                            color: '#333',
                            align: 'center'
                        }
                    },
                    tooltip: {
                        trigger: 'axis'
                    },
                    toolbox: {
                        show: true,
                        feature: {
                            mark: {show: false},
                            dataView: {show: false, readOnly: false},
                            magicType: {show: true, type: ['line', 'bar']},
                            restore: {show: true},
                            saveAsImage: {show: true}
                        }
                    },
                    xAxis: [{
                            type: 'category',
                            data: ['$recharge_analyze_007', '$recharge_analyze_008', '$recharge_analyze_009', '$recharge_analyze_010']
                        }],
                    yAxis: [{
                            type: 'value',
                            name: '$data_analyze_026$data_analyze_027'
                        }],
                    series: [{
                            "name": "$data_analyze_026",
                            "type": "bar",
                            stack: 'sum',
                            barCategoryGap: '40%',
                            itemStyle: {// 系列级个性化
                                normal: {
                                    color: 'red'
                                }
                            },
                            data: dataArr
                        }]
                };
                window.onresize = myChart.resize;
                // 载入动画---------------------
                myChart.setOption(option);
            }
    );
}
//充值退费总额统计
function rechargeRefundAnalyze() {
    var province = $("#add1").val().trim();
    var city = $("#add2").val().trim();
    var district = $("#add3").val().trim();
    var beginDate = $("#beginDate1").val().trim();
    var endDate = $("#endDate1").val().trim();
    var rechargeTotal = 0.0;
    var refundTotal = 0.0;
    var dataArr = [];
    var titleText = "充值退费统计";
    if (district !== "$user_manage_148") {
        titleText = district + titleText;
    }
    //按时间(月)查找业务办理量
    jQuery.ajax({
        url: "../user/rechargeRefundAnalyze.do",
        type: "post",
        data: {
            "province": province,
            "city": city,
            "district": district,
            "beginDate": beginDate,
            "endDate": endDate
        },
        dataType: "json",
        async: false,
        traditional: true,
        success: function (result) {
            if (result.status === 1) {
                var map = result.data;
                rechargeTotal = map["rechargeTotal"];
                if (rechargeTotal === null) {
                    rechargeTotal = 0.0;
                }
                refundTotal = map["refundTotal"];
                if (refundTotal === null) {
                    refundTotal = 0.0;
                }
                dataArr.push(rechargeTotal);
                dataArr.push(refundTotal);
            } else {
                alert("$public_066");
            }
        },
        error: function () {
            alert("error");
        }
    });

    require.config({
        paths: {
            echarts: '../echarts'
        }
    });
    require(
            [
                'echarts',
                'echarts/chart/bar',
                'echarts/chart/line'
            ],
            function (ec) {
                var myChart = ec.init(document.getElementById('echarts1'));
                var option = {
                    title: {
                        x: "center",
                        text: titleText,
                        textStyle: {
                            fontSize: 18,
                            fontWeight: 'bolder',
                            color: '#333',
                            align: 'center'
                        }
                    },
                    tooltip: {
                        trigger: 'axis'
                    },
                    toolbox: {
                        show: true,
                        feature: {
                            mark: {show: false},
                            dataView: {show: false, readOnly: false},
                            magicType: {show: true, type: ['line', 'bar']},
                            restore: {show: true},
                            saveAsImage: {show: true}
                        }
                    },
                    xAxis: [{
                            type: 'category',
                            data: ['$recharge_analyze_007', '$recharge_analyze_008']
                        }],
                    yAxis: [{
                            type: 'value',
                            name: '$recharge_analyze_029'
                        }],
                    series: [{
                            "name": "$public_077",
                            "type": "bar",
                            stack: 'sum',
                            barCategoryGap: '40%',
                            itemStyle: {// 系列级个性化
                                normal: {
                                    color: 'red'
                                }
                            },
                            data: dataArr
                        }]
                };
                window.onresize = myChart.resize;
                // 载入动画---------------------
                myChart.setOption(option);
            }
    );
}
