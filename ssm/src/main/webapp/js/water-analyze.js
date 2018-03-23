//数据统计页面
$(function () {
    $("#admin").text(account_name);
    $("#sum-to-do").text("(" + toDoNo + ")");
    $("#exit").click(logout);
    $("#pipe").hide();
    load_Sumtodo();
    require.config({
        paths: {
            'echarts': '../echarts'
        }
    });
    
    function loadPipe(param) {
        var data = [100, 100, 100, 100, 100, param, 100, 100, 100, 100, 100, 100];
        require(
                [
                    'echarts',
                    'echarts/chart/line' // 使用柱状图就加载bar模块，按需加载
                ],
                function (ec) {
                    $("#pipe").html("");
                    var myChart = ec.init(document.getElementById('pipe'));
                    option = {
                        title: {
                            subtext: '单位(%)',
                            subtextStyle: {
                                color: '#CD00CD'
                            }
                        },
                        tooltip: {
                            trigger: 'axis',
                            axisPointer: {
                                type: 'cross'
                            }
                        },
                        toolbox: {
                            show: true,
                            feature: {
                                saveAsImage: {}
                            }
                        },
                        xAxis: {
                            type: 'category',
                            boundaryGap: false,
                            splitNumber: 24,
                            axisTick: {length: 2},
                            axisLine: {onZero: true},
                            data: ['00:00', '02:00', '04:00', '06:00', '08:00', '10:00', '12:00', '14:00', '16:00', '18:00', '20:00', '22:00']
                        },
                        yAxis: {
                            type: 'value',
                            axisLabel: {
                                formatter: '{value}'
                            },
                            axisPointer: {
                                snap: true
                            }
                        },
                        visualMap: {
                            show: false,
                            dimension: 0,
                            pieces: [{
                                    lte: 6,
                                    color: 'green'
                                }, {
                                    gt: 6,
                                    lte: 8,
                                    color: 'red'
                                }, {
                                    gt: 8,
                                    lte: 14,
                                    color: 'green'
                                }, {
                                    gt: 14,
                                    lte: 17,
                                    color: 'red'
                                }, {
                                    gt: 17,
                                    color: 'green'
                                }]
                        },
                        series: [
                            {
                                name: '管道压力值',
                                type: 'line',
                                smooth: true,
                                data: data
                            }
                        ]
                    };
                    // 为echarts对象加载数据 
                    myChart.setOption(option, true);
                    $("#pipe").click(function () {
                        $("#pipe").hide();
                        $("#PipeBurstAlarm").show();
                        $("#PipeBurstAlarm1").show();
                        $("#PipeBurstAlarm2").show();
                    });
                });
    }

    require(
            [
                'echarts',
                'echarts/chart/gauge' // 使用柱状图就加载bar模块，按需加载
            ],
            function (ec) {
                $("#PipeBurstAlarm").html("");
                var myChart = ec.init(document.getElementById('PipeBurstAlarm'));
                option = {
                    tooltip: {
                        formatter: "{a} <br/>{b} : {c}%"
                    },
                    series: [
                        {
                            name: '压力指数',
                            type: 'gauge',
                            splitNumber: 5, // 分割段数，默认为5
                            axisLine: {// 坐标轴线
                                lineStyle: {// 属性lineStyle控制线条样式
                                    color: [[0.95, '#ff4500'], [1, '#228b22']],
                                    width: 8
                                }
                            },
                            axisTick: {// 坐标轴小标记
                                splitNumber: 10, // 每份split细分多少段
                                length: 12, // 属性length控制线长
                                lineStyle: {// 属性lineStyle控制线条样式
                                    color: 'auto'
                                }
                            },
                            axisLabel: {// 坐标轴文本标签，详见axis.axisLabel
                                textStyle: {// 其余属性默认使用全局文本样式，详见TEXTSTYLE
                                    color: 'auto'
                                }
                            },
                            splitLine: {// 分隔线
                                show: true, // 默认显示，属性show控制显示与否
                                length: 30, // 属性length控制线长
                                lineStyle: {// 属性lineStyle（详见lineStyle）控制线条样式
                                    color: 'auto'
                                }
                            },
                            pointer: {
                                width: 5
                            },
                            title: {
                                show: true,
                                offsetCenter: [0, '100%'], // x, y，单位px
                                textStyle: {// 其余属性默认使用全局文本样式，详见TEXTSTYLE
                                    fontWeight: 'bolder'
                                }
                            },
                            detail: {
                                offsetCenter: [0, '40%'],
                                formatter: '{value}%',
                                textStyle: {// 其余属性默认使用全局文本样式，详见TEXTSTYLE
                                    color: 'auto',
                                    fontWeight: 'bolder'
                                }
                            },
                            data: [{value: 90, name: '管道001'}]
                        }
                    ]
                };
                // 为echarts对象加载数据 
                myChart.setOption(option, true);
                $("#PipeBurstAlarm").click(function () {
                    loadPipe(90);
                    $("#pipe").show();
                    $("#PipeBurstAlarm").hide();
                    $("#PipeBurstAlarm1").hide();
                    $("#PipeBurstAlarm2").hide();
                });
            });

    require(
            [
                'echarts',
                'echarts/chart/gauge' // 使用柱状图就加载bar模块，按需加载
            ],
            function (ec) {
                $("#PipeBurstAlarm1").html("");
                var myChart = ec.init(document.getElementById('PipeBurstAlarm1'));
                option = {
                    tooltip: {
                        formatter: "{a} <br/>{b} : {c}%"
                    },
                    series: [
                        {
                            name: '压力指数',
                            type: 'gauge',
                            splitNumber: 5, // 分割段数，默认为5
                            axisLine: {// 坐标轴线
                                lineStyle: {// 属性lineStyle控制线条样式
                                    color: [[0.95, '#ff4500'], [1, '#228b22']],
                                    width: 8
                                }
                            },
                            axisTick: {// 坐标轴小标记
                                splitNumber: 10, // 每份split细分多少段
                                length: 12, // 属性length控制线长
                                lineStyle: {// 属性lineStyle控制线条样式
                                    color: 'auto'
                                }
                            },
                            axisLabel: {// 坐标轴文本标签，详见axis.axisLabel
                                textStyle: {// 其余属性默认使用全局文本样式，详见TEXTSTYLE
                                    color: 'auto'
                                }
                            },
                            splitLine: {// 分隔线
                                show: true, // 默认显示，属性show控制显示与否
                                length: 30, // 属性length控制线长
                                lineStyle: {// 属性lineStyle（详见lineStyle）控制线条样式
                                    color: 'auto'
                                }
                            },
                            pointer: {
                                width: 5
                            },
                            title: {
                                show: true,
                                offsetCenter: [0, '100%'], // x, y，单位px
                                textStyle: {// 其余属性默认使用全局文本样式，详见TEXTSTYLE
                                    fontWeight: 'bolder'
                                }
                            },
                            detail: {
                                formatter: '{value}%',
                                textStyle: {// 其余属性默认使用全局文本样式，详见TEXTSTYLE
                                    color: 'auto',
                                    fontWeight: 'bolder'
                                }
                            },
                            data: [{value: 80,
                                    name: '管道002',
                                    textStyle: {
                                        fontSize: 40
                                    }
                                }]
                        }
                    ]
                };
                // 为echarts对象加载数据 
                myChart.setOption(option, true);
                $("#PipeBurstAlarm1").click(function () {
                    loadPipe(80);
                    $("#pipe").show();
                    $("#PipeBurstAlarm").hide();
                    $("#PipeBurstAlarm1").hide();
                    $("#PipeBurstAlarm2").hide();
                });
            });

    require(
            [
                'echarts',
                'echarts/chart/gauge' // 使用柱状图就加载bar模块，按需加载
            ],
            function (ec) {
                $("#PipeBurstAlarm2").html("");
                var myChart = ec.init(document.getElementById('PipeBurstAlarm2'));
                option = {
                    tooltip: {
                        formatter: "{a} <br/>{b} : {c}%"
                    },
                    series: [
                        {
                            name: '压力指数',
                            type: 'gauge',
                            splitNumber: 5, // 分割段数，默认为5
                            axisLine: {// 坐标轴线
                                lineStyle: {// 属性lineStyle控制线条样式
                                    color: [[0.95, '#ff4500'], [1, '#228b22']],
                                    width: 8
                                }
                            },
                            axisTick: {// 坐标轴小标记
                                splitNumber: 10, // 每份split细分多少段
                                length: 12, // 属性length控制线长
                                lineStyle: {// 属性lineStyle控制线条样式
                                    color: 'auto'
                                }
                            },
                            axisLabel: {// 坐标轴文本标签，详见axis.axisLabel
                                textStyle: {// 其余属性默认使用全局文本样式，详见TEXTSTYLE
                                    color: 'auto'
                                }
                            },
                            splitLine: {// 分隔线
                                show: true, // 默认显示，属性show控制显示与否
                                length: 30, // 属性length控制线长
                                lineStyle: {// 属性lineStyle（详见lineStyle）控制线条样式
                                    color: 'auto'
                                }
                            },
                            pointer: {
                                width: 5
                            },
                            title: {
                                show: true,
                                offsetCenter: [0, '100%'], // x, y，单位px
                                textStyle: {// 其余属性默认使用全局文本样式，详见TEXTSTYLE
                                    fontWeight: 'bolder'
                                }
                            },
                            detail: {
                                formatter: '{value}%',
                                textStyle: {// 其余属性默认使用全局文本样式，详见TEXTSTYLE
                                    color: 'auto',
                                    fontWeight: 'bolder'
                                }
                            },
                            data: [{value: 70,
                                    name: '管道003',
                                    textStyle: {
                                        fontSize: 40
                                    }
                                }]
                        }
                    ]
                };
                // 为echarts对象加载数据 
                myChart.setOption(option, true);
                $("#PipeBurstAlarm2").click(function () {
                    loadPipe(70);
                    $("#pipe").show();
                    $("#PipeBurstAlarm").hide();
                    $("#PipeBurstAlarm1").hide();
                    $("#PipeBurstAlarm2").hide();
                });
            });

    require(
            [
                'echarts',
                'echarts/chart/line' // 使用柱状图就加载bar模块，按需加载
            ],
            function (ec) {
                $("#totalUserWater").html("");
                var myChart = ec.init(document.getElementById('totalUserWater'));
                option = {
                    title: {
                        subtext: '单位(吨)',
                        subtextStyle: {
                            color: '#CD00CD'
                        }
                    },
                    tooltip: {
                        trigger: 'axis',
                        axisPointer: {
                            type: 'cross'
                        }
                    },
                    toolbox: {
                        show: true,
                        feature: {
                            saveAsImage: {}
                        }
                    },
                    xAxis: {
                        type: 'category',
                        boundaryGap: false,
                        data: ['00:00', '01:00', '02:00', '03:00', '04:00', '05:00', '06:00', '07:00', '08:00', '09:00', '10:00', '11:00', '12:00', '13:00', '14:00', '15:00', '16:00', '17:00', '18:00', '19:00', '20:00', '21:00', '22:00', '23:00']
                    },
                    yAxis: {
                        type: 'value',
                        axisLabel: {
                            formatter: '{value}'
                        },
                        axisPointer: {
                            snap: true
                        }
                    },
                    visualMap: {
                        show: false,
                        dimension: 0,
                        pieces: [{
                                lte: 6,
                                color: 'green'
                            }, {
                                gt: 6,
                                lte: 8,
                                color: 'red'
                            }, {
                                gt: 8,
                                lte: 14,
                                color: 'green'
                            }, {
                                gt: 14,
                                lte: 17,
                                color: 'red'
                            }, {
                                gt: 17,
                                color: 'green'
                            }]
                    },
                    series: [
                        {
                            name: '用水量',
                            type: 'line',
                            smooth: true,
                            data: [10, 27, 29, 26, 27, 30, 35, 60, 40, 39, 38, 39, 70, 43, 35, 40, 45, 40, 70, 70, 26, 27, 30, 35],
                            markArea: {
                                data: [[{
                                            name: '早高峰',
                                            xAxis: '07:30'
                                        }, {
                                            xAxis: '10:00'
                                        }], [{
                                            name: '晚高峰',
                                            xAxis: '17:30'
                                        }, {
                                            xAxis: '21:15'
                                        }]]
                            }
                        }
                    ]
                };
                // 为echarts对象加载数据 
                myChart.setOption(option, true);
            });

    require(
            [
                'echarts',
                'echarts/chart/line' // 使用柱状图就加载bar模块，按需加载
            ],
            function (ec) {
                $("#totalUserWater").html("");
                var myChart = ec.init(document.getElementById('ACommunityUserWater'));
                option = {
                    title: {
                        subtext: '单位(吨)',
                        subtextStyle: {
                            color: '#CD00CD'
                        }
                    },
                    tooltip: {
                        trigger: 'axis',
                        axisPointer: {
                            type: 'cross'
                        }
                    },
                    toolbox: {
                        show: true,
                        feature: {
                            saveAsImage: {}
                        }
                    },
                    xAxis: {

                        type: 'category',
                        boundaryGap: false,
                        splitNumber: 24,
                        axisTick: {length: 2},
                        axisLine: {onZero: true},
                        data: ['00:00', '01:00', '02:00', '03:00', '04:00', '05:00', '06:00', '07:00', '08:00', '09:00', '10:00', '11:00', '12:00', '13:00', '14:00', '15:00', '16:00', '17:00', '18:00', '19:00', '20:00', '21:00', '22:00', '23:00']
                    },
                    yAxis: {
                        type: 'value',
                        axisLabel: {
                            formatter: '{value}'
                        },
                        axisPointer: {
                            snap: true
                        }
                    },
                    visualMap: {
                        show: false,
                        dimension: 0,
                        pieces: [{
                                lte: 6,
                                color: 'green'
                            }, {
                                gt: 6,
                                lte: 8,
                                color: 'red'
                            }, {
                                gt: 8,
                                lte: 14,
                                color: 'green'
                            }, {
                                gt: 14,
                                lte: 17,
                                color: 'red'
                            }, {
                                gt: 17,
                                color: 'green'
                            }]
                    },
                    series: [
                        {
                            name: '用水量',
                            type: 'line',
                            smooth: true,
                            data: [3, 9, 8, 8, 9, 10, 11, 20, 13, 13, 12, 13, 23, 14, 11, 12, 15, 13, 23, 23, 08, 09, 10, 11]

                        }
                    ]
                };
                // 为echarts对象加载数据 
                myChart.setOption(option, true);
            });

    require(
            [
                'echarts',
                'echarts/chart/line' // 使用柱状图就加载bar模块，按需加载
            ],
            function (ec) {
                $("#BCommunityUserWater").html("");
                var myChart = ec.init(document.getElementById('BCommunityUserWater'));
                option = {
                    title: {
                        subtext: '单位(吨)',
                        subtextStyle: {
                            color: '#CD00CD'
                        }
                    },
                    tooltip: {
                        trigger: 'axis',
                        axisPointer: {
                            type: 'cross'
                        }
                    },
                    toolbox: {
                        show: true,
                        feature: {
                            saveAsImage: {}
                        }
                    },
                    xAxis: {

                        type: 'category',
                        boundaryGap: false,
                        splitNumber: 24,
                        axisTick: {length: 2},
                        axisLine: {onZero: true},
                        data: ['00:00', '01:00', '02:00', '03:00', '04:00', '05:00', '06:00', '07:00', '08:00', '09:00', '10:00', '11:00', '12:00', '13:00', '14:00', '15:00', '16:00', '17:00', '18:00', '19:00', '20:00', '21:00', '22:00', '23:00']
                    },
                    yAxis: {
                        type: 'value',
                        axisLabel: {
                            formatter: '{value}'
                        },
                        axisPointer: {
                            snap: true
                        }
                    },
                    visualMap: {
                        show: false,
                        dimension: 0,
                        pieces: [{
                                lte: 6,
                                color: 'green'
                            }, {
                                gt: 6,
                                lte: 8,
                                color: 'red'
                            }, {
                                gt: 8,
                                lte: 14,
                                color: 'green'
                            }, {
                                gt: 14,
                                lte: 17,
                                color: 'red'
                            }, {
                                gt: 17,
                                color: 'green'
                            }]
                    },
                    series: [
                        {
                            name: '用水量',
                            type: 'line',
                            smooth: true,
                            data: [2, 8, 7, 7, 8, 9, 10, 19, 12, 12, 11, 12, 22, 13, 10, 11, 14, 12, 22, 22, 7, 8, 9, 10]
                        }
                    ]
                };
                // 为echarts对象加载数据 
                myChart.setOption(option, true);
            });

    require(
            [
                'echarts',
                'echarts/chart/line' // 使用柱状图就加载bar模块，按需加载
            ],
            function (ec) {
                $("#CCommunityUserWater").html("");
                var myChart = ec.init(document.getElementById('CCommunityUserWater'));
                option = {
                    title: {
                        subtext: '单位(吨)',
                        subtextStyle: {
                            color: '#CD00CD'
                        }
                    },
                    tooltip: {
                        trigger: 'axis',
                        axisPointer: {
                            type: 'cross'
                        }
                    },
                    toolbox: {
                        show: true,
                        feature: {
                            saveAsImage: {}
                        }
                    },
                    xAxis: {

                        type: 'category',
                        boundaryGap: false,
                        splitNumber: 24,
                        axisTick: {length: 2},
                        axisLine: {onZero: true},
                        data: ['00:00', '01:00', '02:00', '03:00', '04:00', '05:00', '06:00', '07:00', '08:00', '09:00', '10:00', '11:00', '12:00', '13:00', '14:00', '15:00', '16:00', '17:00', '18:00', '19:00', '20:00', '21:00', '22:00', '23:00']
                    },
                    yAxis: {
                        type: 'value',
                        axisLabel: {
                            formatter: '{value}'
                        },
                        axisPointer: {
                            snap: true
                        }
                    },
                    visualMap: {
                        show: false,
                        dimension: 0,
                        pieces: [{
                                lte: 6,
                                color: 'green'
                            }, {
                                gt: 6,
                                lte: 8,
                                color: 'red'
                            }, {
                                gt: 8,
                                lte: 14,
                                color: 'green'
                            }, {
                                gt: 14,
                                lte: 17,
                                color: 'red'
                            }, {
                                gt: 17,
                                color: 'green'
                            }]
                    },
                    series: [
                        {
                            name: '用水量',
                            type: 'line',
                            smooth: true,
                            data: [4, 10, 9, 9, 10, 11, 12, 21, 14, 14, 13, 14, 24, 15, 12, 13, 16, 14, 24, 24, 09, 10, 11, 12]
                        }
                    ]
                };
                // 为echarts对象加载数据 
                myChart.setOption(option, true);
            });

    require(
            [
                'echarts',
                'echarts/chart/line', // 使用柱状图就加载bar模块，按需加载
                'echarts/chart/bar'
            ],
            function (ec) {
                $("#WaterLeakageRate").html("");
                var myChart = ec.init(document.getElementById('WaterLeakageRate'));
                option = {
                    tooltip: {
                        trigger: 'axis',
                        axisPointer: {
                            type: 'cross',
                            crossStyle: {
                                color: '#999'
                            }
                        }
                    },
                    toolbox: {
                        feature: {
                            dataView: {show: true, readOnly: false},
                            magicType: {show: true, type: ['line', 'bar']},
                            restore: {show: true},
                            saveAsImage: {show: true}
                        }
                    },
                    legend: {
                        data: ['小区用水量之和', '总共用水量', '漏损率']
                    },
                    xAxis: [
                        {
                            type: 'category',
                            data: ['0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '10', '11', '12', '13', '14', '15', '16', '17', '18', '19', '20', '21', '22', '23'],
                            axisPointer: {
                                type: 'shadow'
                            }
                        }
                    ],
                    yAxis: [
                        {
                            type: 'value',
                            name: '单位(吨)',
                            nameTextStyle:{
                                color:'#CD00CD'
                            },
                            interval: 50,
                            axisLabel: {
                            }
                        },
                        {
                            type: 'value',
                            name: '单位(%)',
                            nameTextStyle:{
                                color:'#CD00CD'
                            },
                            interval: 5,
                            axisLabel: {
                                // formatter: '{value} °C'
                            }
                        }
                    ],
                    series: [
                        {
                            name: '小区用水量之和',
                            type: 'bar',
                            data: [9, 27, 24, 24, 27, 30, 33, 60, 39, 39, 36, 39, 60, 42, 33, 36, 45, 39, 69, 69, 24, 27, 30, 33]
                        },
                        {
                            name: '总共用水量',
                            type: 'bar',
                            data: [10, 98, 95, 26, 27, 30, 35, 60, 40, 39, 38, 39, 70, 43, 35, 40, 45, 40, 70, 70, 26, 27, 30, 35]
                        },

                        {
                            name: '漏损率',
                            type: 'line',
                            yAxisIndex: 1,
                            data: [10, 72, 75, 8, 0, 0, 6, 0, 3, 0, 5, 0, 14, 2, 6, 10, 0, 3, 1, 1, 8, 0, 0, 6]
                        }
                    ]
                };

                // 为echarts对象加载数据 
                myChart.setOption(option, true);
            });

    require(
            [
                'echarts',
                'echarts/chart/line' // 使用柱状图就加载bar模块，按需加载
            ],
            function (ec) {
                $("#readMeterSucRate").html("");
                var myChart = ec.init(document.getElementById('readMeterSucRate'));
                option = {
                    title: {
                        subtext: '单位(%)',
                        subtextStyle: {
                            color: '#CD00CD'
                        }
                    },
                    tooltip: {
                        trigger: 'axis',
                        axisPointer: {
                            type: 'cross'
                        }
                    },
                    toolbox: {
                        show: true,
                        feature: {
                            saveAsImage: {}
                        }
                    },
                    axis:{
                        lineStyle: {
                            color: '#aad',
                            width: 5,
                            
                            type: 'solid'
                        }
                    },
                    xAxis: {

                        type: 'category',
                        boundaryGap: false,
                        axisLabel: {
                            formatter: '{value}'
                        },
                        splitNumber: 12,
                        axisTick: {length: 2},
                        axisLine: {onZero: true},
                        data: ['00:00', '02:00', '04:00', '06:00', '08:00', '10:00', '12:00', '14:00', '16:00', '18:00', '20:00', '22:00']
                    },
                    yAxis: {
                        type: 'value',
                        axisLabel: {
                            formatter: '{value}',
                            //show:false   用来隐藏刻度值的
                        },
                        min: 0,
                        max: 100,
                        splitNumber: 10
                    },
                    grid:{
                        x:80,
                        y:60
                    },
                    visualMap: {
                        show: false,
                        dimension: 0,
                        pieces: [{
                                lte: 6,
                                color: 'green'
                            }, {
                                gt: 6,
                                lte: 8,
                                color: 'red'
                            }, {
                                gt: 8,
                                lte: 14,
                                color: 'green'
                            }, {
                                gt: 14,
                                lte: 17,
                                color: 'red'
                            }, {
                                gt: 17,
                                color: 'green'
                            }]
                    },
                    series: [
                        {
                            name: '抄表成功率',
                            type: 'line',
                            smooth: true,
                            data: [100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, ]
                        }
                    ]
                };
                // 为echarts对象加载数据 
                myChart.setOption(option, true);
            });
});





