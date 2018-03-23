//数据统计页面
$(function () {
    $("#admin").text(account_name);
    $("#sum-to-do").text("(" + toDoNo + ")");
    $("#exit").click(logout);
    load_Sumtodo();
    require.config({
        paths: {
            'echarts': '../echarts'
        }
    });
    require(
            [
                'echarts',
                'echarts/chart/map' // 使用柱状图就加载bar模块，按需加载
            ],
            function (ec) {
                $("main").html("");
                var myChart = ec.init(document.getElementById('main'));
                option = {
                    backgroundColor: '#1b1b1b',
                    title: {
                        text: '超仪智能表全国分布图',
                        subtext: '(不包含港澳台)',
                        textStyle: {
                            fontSize: 18,
                            fontWeight: 'bolder',
                            color: '#ffffff'
                        },
                        x: 'center',
                        padding: [30, 0, 0, 0]
                    },
                    tooltip: {
                        trigger: 'item',
                        formatter: function (params) {
                            var res = params.name + '<br/>';
                            var myseries = option.series;
                            for (var i = 0; i < myseries.length; i++) {
                                for (var j = 0; j < myseries[i].data.length; j++) {
                                    if (myseries[i].data[j].name === params.name) {
                                        res += myseries[i].name + ' : ' + myseries[i].data[j].value + '</br>';
                                    }
                                }
                            }
                            return res;
                        }
                    },
                    toolbox: {
                        show: true,
                        orient: 'vertical',
                        x: 'right',
                        y: 'center',
                        feature: {
                            mark: {show: true},
                            dataView: {show: true, readOnly: false},
                            restore: {show: true},
                            saveAsImage: {show: true}
                        }
                    },
                    roamController: {
                        show: true,
                        x: 'right',
                        mapTypeControl: {
                            'china': true
                        }
                    },
                    dataRange: {
                        min: 0,
                        max: 100,
                        calculable: true,
                        hoverLink: true,
                        color: ['#ff3333', 'orange', 'yellow', 'lime', 'aqua'],
                        textStyle: {
                            color: '#fff'
                        }
                    },
                    series: [{
                            name: '水表',
                            type: 'map',
                            mapType: 'china',
                            itemStyle: {
                                normal: {
                                    borderColor: 'rgba(100,149,237,1)',
                                    borderWidth: 0.5,
                                    areaStyle: {
                                        color: '#1b1b1b'
                                    }
                                }
                            },
                            data: [
                                {name: '上海', value: Math.round(Math.random() * 1000)},
                                {name: '山东', value: Math.round(Math.random() * 1000)},
                                {name: '陕西', value: Math.round(Math.random() * 1000)},
                                {name: '福建', value: Math.round(Math.random() * 1000)},
                                {name: '广西', value: Math.round(Math.random() * 1000)},
                                {name: '西藏', value: Math.round(Math.random() * 1000)},
                                {name: '浙江', value: Math.round(Math.random() * 1000)}
                            ],
                            markLine: {
                                smooth: true,
                                effect: {
                                    show: true,
                                    scaleSize: 1,
                                    period: 30,
                                    color: '#00ff00',
                                    shadowBlur: 10
                                },
                                itemStyle: {
                                    normal: {
                                        borderWidth: 1,
                                        lineStyle: {
                                            type: 'solid',
                                            shadowBlur: 10
                                        }
                                    }
                                },
                                data: [
                                    [{name: '浙江'}, {name: '上海'}],
                                    [{name: '浙江'}, {name: '山东'}],
                                    [{name: '浙江'}, {name: '陕西'}],
                                    [{name: '浙江'}, {name: '广西'}],
                                    [{name: '浙江'}, {name: '西藏'}],
                                    [{name: '浙江'}, {name: '福建'}]
                                ]
                            },
                            markPoint: {
                                symbol: 'emptyCircle',
                                symbolSize: function (v) {
                                    return 10 + Math.round(Math.random() * 5);
                                },
                                effect: {
                                    show: true,
                                    type: "scale",
                                    shadowBlur: 0,
                                    period: 10,
                                    color: '#ffff00',
                                    shadowColor: '#ffff00'
                                },
                                itemStyle: {
                                    normal: {
                                        color: '#ff0000'
                                    }
                                },
                                data: [
                                    {name: '上海', value: null},
                                    {name: '山东', value: null},
                                    {name: '陕西', value: null},
                                    {name: '浙江', value: null},
                                    {name: '广西', value: null},
                                    {name: '西藏', value: null},
                                    {name: '福建', value: null}
                                ]
                            },
                            geoCoord: {
                                '上海': [121.4648, 31.2891],
                                '新疆': [87.9236, 43.5883],
                                '北京': [116.4551, 40.2539],
                                '江苏': [119.14, 32.3734],
                                '广西': [108.479, 23.1152],
                                '江西': [116.0046, 28.6633],
                                '安徽': [117.29, 32.0581],
                                '浙江': [120.2, 30.3],
                                '内蒙古': [111.4124, 40.4901],
                                '黑龙江': [127.9688, 45.368],
                                '天津': [117.4219, 39.4189],
                                '山西': [112.3352, 37.9413],
                                '广东': [113.5107, 23.2196],
                                '四川': [103.9526, 30.7617],
                                '西藏': [91.1865, 30.1465],
                                '云南': [102.9199, 25.4663],
                                '湖北': [114.3896, 30.6628],
                                '辽宁': [123.1238, 42.1216],
                                '山东': [117.1582, 36.8701],
                                '海南': [110.3893, 19.8516],
                                '深圳': [114.5435, 22.5439],
                                '河北': [114.4995, 38.1006],
                                '福建': [119.4543, 25.9222],
                                '青海': [101.4038, 36.8207],
                                '陕西': [109.1162, 34.2004],
                                '贵州': [106.6992, 26.7682],
                                '河南': [113.4668, 34.6234],
                                '重庆': [107.7539, 30.1904],
                                '宁夏': [106.3586, 38.1775],
                                '吉林': [125.8154, 44.2584],
                                '湖南': [113.0823, 28.2568],
                                '甘肃': [103.73, 36.03]

                            }
                        },
                        {
                            name: '热水表',
                            type: 'map',
                            mapType: 'china',
                            itemStyle: {
                                normal: {
                                    borderColor: 'rgba(100,5,237,1)',
                                    borderWidth: 0.5,
                                    areaStyle: {
                                        color: '#1b1b1b'
                                    }
                                }
                            },
                            data: [
                                {name: '上海', value: Math.round(Math.random() * 1000)},
                                {name: '山东', value: Math.round(Math.random() * 1000)},
                                {name: '陕西', value: Math.round(Math.random() * 1000)},
                                {name: '福建', value: Math.round(Math.random() * 1000)},
                                {name: '广西', value: Math.round(Math.random() * 1000)},
                                {name: '西藏', value: Math.round(Math.random() * 1000)},
                                {name: '浙江', value: Math.round(Math.random() * 1000)}
                            ]

                        },
                        {
                            name: '热表',
                            type: 'map',
                            mapType: 'china',
                            itemStyle: {
                                normal: {
                                    borderColor: 'rgba(100,5,237,1)',
                                    borderWidth: 0.5,
                                    areaStyle: {
                                        color: '#1b1b1b'
                                    }
                                }
                            },
                            data: [
                                {name: '上海', value: Math.round(Math.random() * 1000)},
                                {name: '山东', value: Math.round(Math.random() * 1000)},
                                {name: '陕西', value: Math.round(Math.random() * 1000)},
                                {name: '福建', value: Math.round(Math.random() * 1000)},
                                {name: '广西', value: Math.round(Math.random() * 1000)},
                                {name: '西藏', value: Math.round(Math.random() * 1000)},
                                {name: '浙江', value: Math.round(Math.random() * 1000)}
                            ]

                        },
                        {
                            name: '气表',
                            type: 'map',
                            mapType: 'china',
                            itemStyle: {
                                normal: {
                                    borderColor: 'rgba(100,5,237,1)',
                                    borderWidth: 0.5,
                                    areaStyle: {
                                        color: '#1b1b1b'
                                    }
                                }
                            },
                            data: [
                                {name: '上海', value: Math.round(Math.random() * 1000)},
                                {name: '山东', value: Math.round(Math.random() * 1000)},
                                {name: '陕西', value: Math.round(Math.random() * 1000)},
                                {name: '福建', value: Math.round(Math.random() * 1000)},
                                {name: '广西', value: Math.round(Math.random() * 1000)},
                                {name: '西藏', value: Math.round(Math.random() * 1000)},
                                {name: '浙江', value: Math.round(Math.random() * 1000)}
                            ]
                        },
                        {
                            name: '电表',
                            type: 'map',
                            mapType: 'china',
                            itemStyle: {
                                normal: {
                                    borderColor: 'rgba(100,5,237,1)',
                                    borderWidth: 0.5,
                                    areaStyle: {
                                        color: '#1b1b1b'
                                    }
                                }
                            },
                            data: [
                                {name: '上海', value: Math.round(Math.random() * 1000)},
                                {name: '山东', value: Math.round(Math.random() * 1000)},
                                {name: '陕西', value: Math.round(Math.random() * 1000)},
                                {name: '福建', value: Math.round(Math.random() * 1000)},
                                {name: '广西', value: Math.round(Math.random() * 1000)},
                                {name: '西藏', value: Math.round(Math.random() * 1000)},
                                {name: '浙江', value: Math.round(Math.random() * 1000)}
                            ]
                        }
                    ]
                };
                // 为echarts对象加载数据 
                myChart.setOption(option);
            });
});

