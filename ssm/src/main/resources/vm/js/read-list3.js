// 加载所有抄表信息
Ext.onReady(function () {
    var monthCurrent = "";
    var meter_typeCurrent = "";
    var flagCurrent = 1;
    var page_indexCurrent = 1;
    var page_sizeCurrent = 20;
    var columnsCurrent = [
        new Ext.grid.RowNumberer({
            header: "$public_015", //序号
            width: 30,
            align: "center",
            renderer: function (value, metadata, record, rowIndex) {
                var start_noCurrent = (page_indexCurrent - 1) * page_sizeCurrent;
                return start_noCurrent + 1 + rowIndex;
            }
        }),
        {header: '$public_001', dataIndex: 'user_name', flex: 1},
        {header: '$public_016', dataIndex: 'user_address_community', flex: 2},
        {header: '$public_017', dataIndex: 'user_address_building', flex: 2},
        {header: '$public_018', dataIndex: 'user_address_unit', flex: 2},
        {header: '$public_019', dataIndex: 'user_address_room', flex: 2},
        {header: '$public_020', dataIndex: 'contact_info', flex: 2},
        {header: '$public_021', dataIndex: 'concentrator_name', flex: 2},
        {header: '$public_004', dataIndex: 'meter_no', flex: 2},
        {header: '$public_022', dataIndex: 'fee_type', flex: 2},
        {header: '$read_list_018', dataIndex: 'this_read', renderer: formatFloatCurrent, flex: 2},
        {header: '$read_list_067', dataIndex: 'data8', flex: 2},
        {header: '$read_list_068', dataIndex: 'data9', flex: 2},
        {header: '$read_list_023', dataIndex: 'balance',flex: 2},//2018-3-12余额
        {header: '$read_list_027', dataIndex: 'operator_account', flex: 2},
        {header: '$read_list_028', dataIndex: 'operate_time', flex: 3,
            renderer: function (value) {
                return Ext.Date.format(new Date(value), 'Y-m-d H:i:s');
            }}
    ];
    var storeCurrent = new Ext.data.Store({
        reader: {root: 'data.readInfoCurrent'},
        fields: ['operate_id', 'user_name', 'user_address_community', 'user_address_building', 'user_address_unit', 'user_address_room', 'contact_info', 'concentrator_name',
            'meter_no', 'fee_type', 'this_read', 'last_read', 'this_cost', 'last_cost', 'data4', 'data5', 'data6', 'data7', 'data8', 'data9', 'fee_need', 'balance', 'exception', 'fee_status', 'read_type',
            'operator_account', 'operate_time']
    });

    var mask = $("<div class='window_mask'></div>");
    function  formatFloatCurrent(value) {
        if (value !== null) {
            value = value === parseInt(value) ? value : value.toFixed(2);
        } else {
            value = 0;
        }
        return value;
    }

    var tbCurrent = new Ext.Toolbar({
        style: 'height:30px;padding:3px 15px 3px 10px; width:auto',
        autoWidth: true,
        items: [
            {
                text: '$read_list_004',
                handler: function () {
                    monthCurrent = "all";
                    findReadInfoByMonthCurrent();
                    $('#query_monthCurrent').text('$read_list_004');
                }
            },
            {
                text: '$read_list_005',
                handler: function () {
                    monthCurrent = "1";
                    findReadInfoByMonthCurrent();
                    $('#query_monthCurrent').text('$read_list_005');
                }
            },
            {
                text: '$read_list_006',
                handler: function () {
                    monthCurrent = "2";
                    findReadInfoByMonthCurrent();
                    $('#query_monthCurrent').text('$read_list_006');
                }
            },
            {
                text: '$read_list_007',
                handler: function () {
                    monthCurrent = "3";
                    findReadInfoByMonthCurrent();
                    $('#query_monthCurrent').text('$read_list_007');
                }
            },
            {text: '$read_list_008',
                handler: function () {
                    monthCurrent = "4";
                    findReadInfoByMonthCurrent();
                    $('#query_monthCurrent').text('$read_list_008');
                }
            },
            {text: '$read_list_009',
                handler: function () {
                    monthCurrent = "5";
                    findReadInfoByMonthCurrent();
                    $('#query_monthCurrent').text('$read_list_009');
                }
            },
            {text: '$read_list_010',
                handler: function () {
                    monthCurrent = "6";
                    findReadInfoByMonthCurrent();
                    $('#query_monthCurrent').text('$read_list_010');
                }
            },
            {text: '$read_list_011',
                handler: function () {
                    monthCurrent = "7";
                    findReadInfoByMonthCurrent();
                    $('#query_monthCurrent').text('$read_list_011');
                }
            },
            {text: '$read_list_012',
                handler: function () {
                    monthCurrent = "8";
                    findReadInfoByMonthCurrent();
                    $('#query_monthCurrent').text('$read_list_012');
                }
            },
            {text: '$read_list_013',
                handler: function () {
                    monthCurrent = "9";
                    findReadInfoByMonthCurrent();
                    $('#query_monthCurrent').text('$read_list_013');
                }
            },
            {text: '$read_list_014',
                handler: function () {
                    monthCurrent = "10";
                    findReadInfoByMonthCurrent();
                    $('#query_monthCurrent').text('$read_list_014');
                }
            },
            {text: '$read_list_015',
                handler: function () {
                    monthCurrent = "11";
                    findReadInfoByMonthCurrent();
                    $('#query_monthCurrent').text('$read_list_015');
                }
            },
            {text: '$read_list_016',
                handler: function () {
                    monthCurrent = "12";
                    findReadInfoByMonthCurrent();
                    $('#query_monthCurrent').text('$read_list_016');
                }
            }]
    });

    tbCurrent.add('');
    tbCurrent.add('-', {text: '$read_list_042', handler: function () {
            meter_typeCurrent = "10";
            if (flagCurrent === 1) {
                findReadInfoCurrent();
            } else if (flagCurrent === 0) {
                findReadInfoByMonthCurrent();
            }
            $('#query_meterCurrent').text('$read_list_042');
        }}, {text: '$read_list_043', handler: function () {
            meter_typeCurrent = "20";
            if (flagCurrent === 1) {
                findReadInfoCurrent();
            } else if (flagCurrent === 0) {
                findReadInfoByMonthCurrent();
            }
            $('#query_meterCurrent').text('$read_list_043');
        }}, {text: '$read_list_052', handler: function () {
            meter_typeCurrent = "30";
            if (flagCurrent === 1) {
                findReadInfoCurrent();
            } else if (flagCurrent === 0) {
                findReadInfoByMonthCurrent();
            }
            $('#query_meterCurrent').text('$read_list_052');
        }}, {text: '$remote_read_047_2', handler: function () {
            meter_typeCurrent = "40";
            if (flagCurrent === 1) {
                findReadInfoCurrent();
            } else if (flagCurrent === 0) {
                findReadInfoByMonthCurrent();
            }
            $('#query_meterCurrent').text('$remote_read_047_2');
        }});//水表 气表 热表 电表
    tbCurrent.add("->");
    tbCurrent.add('<span>$public_006</span>');
    tbCurrent.add("<select id='pageSizeCurrent' class='pageSizeCurrent' ><option value='5'>5</option> <option value='10'>10</option> <option value='15'>15</option> <option value='20' selected='selected'>20</option> <option value='30'>30</option> <option value='50'>50</option> </select>");
    tbCurrent.add('<a  href="javascript:void(0)" class="glyphicon glyphicon-step-backward  Current3"></a>');
    tbCurrent.add('<a  href="javascript:void(0)" class="triangle-left  Current2"></a>');
    tbCurrent.add('$public_008<input class="input-pageCurrent" type="text" name="" value="1" id="pageIndexCurrent">$public_009');
    tbCurrent.add('<a  href="javascript:void(0)" class="triangle-right  Current1"></a>');
    tbCurrent.add('<a  href="javascript:void(0)" class="glyphicon glyphicon-step-forward  Current4"></a>');
    tbCurrent.add('<span>$public_010<span><b  class="pagCurrent"  style=""></b></span>$public_009');
    tbCurrent.add('');
    tbCurrent.add('');

    //导出按钮组件
    var exportButtonCurrent = Ext.create('Ext.button.Split', {
        renderTo: Ext.getBody(),
        text: '$read_list_029',
        pressed: true,
        menu: new Ext.menu.Menu({
            items: [
                {
                    text: 'Excel',
                    handler: function () {
                        exportReadInfoCurrent('EXCEL');
                    }
                },
                {
                    text: 'Dbf',
                    handler: function () {
                        exportReadInfoCurrent('DBF');
                    }
                }
            ]
        })
    });

    var tb2Current = new Ext.Toolbar({
        style: 'height:30px;padding:3px 10px; width:100%)',
        autoWidth: true,
        items: [exportButtonCurrent]
    });
    tb2Current.add('');
    tb2Current.add('');
    tb2Current.add('<span>$user_manage_03701<span class="totalNoCurrent" id="totalNoCurrent"></span>$user_manage_03702</span>');
    tb2Current.add('');
    tb2Current.add('');
    tb2Current.add('-');
    tb2Current.add('<span>$read_list_060:<span class="totalNoCurrent"  id="query_monthCurrent">$read_list_004</span></span>');
    tb2Current.add('');
    tb2Current.add('<span>$read_list_061:<span class="totalNoCurrent"  id="query_meterCurrent">$read_list_004</span></span>');
    var smCurrent = new Ext.selection.CheckboxModel();
    var gridCurrent = new Ext.grid.GridPanel({
        height: 400,
        renderTo: 'gridCurrent',
        layout: 'fit',
        store: storeCurrent,
        autoWidth: true,
        loadMask: true,
        autoScroll: true,
        bodyStyle: 'overflow-x:hidden;overflow-y:auto;',
        columns: columnsCurrent,
        selType: 'cellmodel',
        tbar: tbCurrent,
        bbar: tb2Current,
        selModel: smCurrent
    });
    Ext.EventManager.onWindowResize(function () {
        gridCurrent.getView().refresh();
    });
    gridCurrent.setAutoScroll(true);

    //加载费率类型选项
    function findFeeTypes() {
        $("#feeTypeNameCurrent").empty();
        $("#feeTypeNameCurrent").append($("<option value=''>$user_manage_06701</option>"));
        jQuery.ajax({
            url: "../user/loadFeeTypes.do",
            type: "post",
            dataType: "json",
            success: function (result) {
                if (result.status == 1) {
                    var feeTypes = result.data;
                    for (var i = 0; i < feeTypes.length; i++) {
                        var fee_type = feeTypes[i].feeTypeName;
                        var str = "<option value='" + fee_type + "'>" + fee_type + "</option>";
                        $("#feeTypeNameCurrent").append($(str));
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
    function findReadInfoCurrentBefore() {
        page_indexCurrent = 1;
        $(".input-pageCurrent").val(1);
        findReadInfoCurrent();
    }
    function findReadInfoCurrent() {
        var meter_type = meter_typeCurrent;
        var meter_no = $("#meter_noCurrent").val().trim();
        var feeTypeName = $("#feeTypeNameCurrent option:selected").val().trim();
        if (isNaN(meter_no)) {
            alert("$read_list_053");
            return;
        }
        var pageNo = $(".input-pageCurrent").val().trim();
        var pageSize = $(".pageSizeCurrent option:selected").val().trim();
        var user_name = $("#user_nameCurrent").val().trim();
        //从浏览器URL中解析小区名
        var community_name = getQueryString("community_name");
        //如果浏览器中解析到小区名，就设置该小区为选中状态
        if (community_name !== "") {
            $("#user_address_communityCurrent option[value='" + community_name + "']").attr("selected", true);
            findAllBuildingsCurrent();
        }
        //从搜索条件中获取小区名
        var community = $("#user_address_communityCurrent option:selected").val().trim();
        var building = $("#user_address_buildingCurrent option:selected").val().trim();
        var unit = $("#user_address_unitCurrent option:selected").val().trim();
        var room = $("#user_address_roomCurrent option:selected").val().trim();
        //var operator_name = $("#operator_name option:selected").text().trim();
        var startTime = $("#startTimeCurrent").val().trim();
        var endTime = $("#endTimeCurrent").val().trim();
        var minBalance = $("#minBalance").val().trim();//最小金额
        var maxBalance = $("#maxBalance").val().trim();//最大金额
        var startNo = (pageNo - 1) * pageSize;
        var queryTableType = "queryCurrent";
        jQuery.ajax({
            url: "../user/zuoQuanFindReadInfo.do",
            type: "post",
            data: {
                "user_name": user_name,
                "community": community,
                "building": building,
                "unit": unit,
                "room": room,
                "meter_type": meter_type,
                "operator_name": "",
                "feeTypeName": feeTypeName,
                "meter_no": meter_no,
                "startTime": startTime,
                "endTime": endTime,
                "startNo": startNo,
                "pageSize": pageSize,
                "minBalance": minBalance,
                "maxBalance": maxBalance,
                "queryTableType": queryTableType
            },
            dataType: "json",
            success: function (result) {
                if (result.status === 1) {
                    var map = result.data;
                    var readInfoCurrent = map["readInfo"];
                    if (readInfoCurrent === null) {
                        readInfoCurrent = [];
                    }
                    for (var i = 0; i < readInfoCurrent.length; i++) {
                        readInfoCurrent[i].data9 = (readInfoCurrent[i].data8 + readInfoCurrent[i].this_read).toFixed(5);//总量保留5位小数
                        readInfoCurrent[i].data8 = (readInfoCurrent[i].data8).toFixed(5);//公摊用余量保留5位
                    }
                    var totalCurrent = map["totalNo"];
                    $("#totalNoCurrent").text(totalCurrent);
                    var page = parseInt(totalCurrent / pageSize);
                    var pageMax = totalCurrent % pageSize === 0 ? page : page + 1;
                    //$(".glyphicon.glyphicon-step-forward.span4").next().text(pageMax);
                    $("#gridCurrent .pagCurrent").text(pageMax);
                    storeCurrent.loadData(readInfoCurrent);
                    flagCurrent = 1;
                }
            },
            error: function () {
                alert("error");
            }
        });
    }



    // 按月份查询抄表信息
    function findReadInfoByMonthCurrent() {
        var pageNo = $(".input-pageCurrent").val().trim();
        var pageSize = $(".pageSizeCurrent option:selected").val().trim();
        var startNo = (pageNo - 1) * pageSize;
        var meter_type = meter_typeCurrent;
        var queryTableType = "queryCurrent";
        jQuery.ajax({
            url: "../user/findReadInfoByMonth.do",
            type: "post",
            data: {
                "month": monthCurrent,
                "meter_type": meter_type,
                "startNo": startNo,
                "pageSize": pageSize,
                "queryTableType": queryTableType
            },
            dataType: "json",
            success: function (result) {
                if (result.status === 1) {
                    var map = result.data;
                    var readInfoCurrent = map["readInfo"];
                    var totalNoCurrent = map["totalNo"];
                    $("#totalNoCurrent").text(totalNoCurrent);
                    var page = parseInt(totalNoCurrent / pageSize);
                    var pageMax = totalNoCurrent % pageSize === 0 ? page : page + 1;
                    //$(".glyphicon.glyphicon-step-forward.span4").next().text(pageMax);
                    $("#gridCurrent  .pagCurrent").text(pageMax);
                    storeCurrent.loadData(readInfoCurrent);
                    flagCurrent = 0;
                    //	month=null;
                }
            },
            error: function () {
                alert("error");
            }
        });
    }


    $("#pageSizeCurrent").change(function () {
        //$(".pageSize").find("option[value=" + pageSize + "]").attr("selected", true);
        if (flagCurrent === 1) {
            findReadInfoCurrent();
        } else if (flagCurrent === 0) {
            findReadInfoByMonthCurrent();
        } else {
            alert('$public_036');
        }
    });


    // 下一页
    function nextPageCurrent() {
        var pageIndex = $(".input-pageCurrent").val().trim();
        pageIndex = parseInt(pageIndex) + 1;
        var totalNo = parseInt($("#totalNoCurrent").text().trim());
        var pageSize = $(".pageSizeCurrent option:selected").val().trim();
        page_sizeCurrent = pageSize;
        var pageNo = parseInt(totalNo / pageSize);
        var pageMax = totalNo % pageSize === 0 ? pageNo : pageNo + 1;
        if (pageIndex <= 0) {
            alert("$public_038");
        } else if (pageIndex > pageMax) {
            alert("$public_039");
        } else {
            $(".input-pageCurrent")[0].value = pageIndex;
            //$(".input-page")[1].value = pageIndex;
            page_indexCurrent = pageIndex;
            if (flagCurrent === 1) {
                findReadInfoCurrent();
            } else if (flagCurrent === 0) {
                findReadInfoByMonthCurrent();
            } else {
                alert('$public_036');
            }
        }
    }



    // 上一页
    function prePageCurrent() {
        var pageIndex = $(".input-pageCurrent").val().trim();
        pageIndex = parseInt(pageIndex) - 1;
        if (pageIndex <= 0) {
            alert("$public_040");
        } else {
            $(".input-pageCurrent")[0].value = pageIndex;
            //$(".input-page")[1].value = pageIndex;
            page_indexCurrent = pageIndex;
            if (flagCurrent === 1) {
                findReadInfoCurrent();
            } else if (flagCurrent === 0) {
                findReadInfoByMonthCurrent();
            } else {
                alert('$public_036');
            }
        }
    }

    // 第一页
    function firstPageCurrent() {
        $(".input-pageCurrent")[0].value = 1;
        //$(".input-page")[1].value = 1;
        page_indexCurrent = 1;
        if (flagCurrent === 1) {
            findReadInfoCurrent();
        } else if (flagCurrent === 0) {
            findReadInfoByMonthCurrent();
        } else {
            alert('$public_036');
        }
    }



    // 最后一页
    function lastPageCurrent() {
        var totalNo = parseInt($("#totalNoCurrent").text().trim());
        var pageSize = $(".pageSizeCurrent option:selected").val().trim();
        // 根据记录总数，计算出最后一页的页数
        var pageNo = parseInt(totalNo / pageSize);
        var pageEnd = totalNo % pageSize === 0 ? pageNo : pageNo + 1;
        $(".input-pageCurrent")[0].value = pageEnd;
        //$(".input-page")[1].value = pageEnd;
        page_indexCurrent = pageEnd;
        if (flagCurrent === 1) {
            findReadInfoCurrent();
        } else if (flagCurrent === 0) {
            findReadInfoByMonthCurrent();
        } else {
            alert('$public_036');
        }
    }


    /**
     * 导出抄表记录
     * @returns {undefined}
     */
    function exportReadInfoCurrent(fileType) {
        var meter_no = $("#meter_noCurrent").val().trim();
        if (isNaN(meter_no)) {
            alert("public_065");
            return;
        }
        var pageNo = $(".input-pageCurrent").val().trim();
        pageNo = pageNo === "" ? 1 : pageNo;
        var user_name = $("#user_nameCurrent").val().trim();
        //从浏览器URL中解析小区名
        var community_name = getQueryString("community_name");
        //如果浏览器中解析到小区名，就设置该小区为选中状态
        if (community_name !== "") {
            $("#user_address_communityCurrent option[value=" + community_name + "]").attr("selected", true);
            findAllBuildingsCurrent();
        }
        //从搜索条件中获取小区名
        var community = $("#user_address_communityCurrent option:selected").val().trim();
        var building = $("#user_address_buildingCurrent option:selected").val().trim();
        var unit = $("#user_address_unitCurrent option:selected").val().trim();
        var room = $("#user_address_roomCurrent option:selected").val().trim();
        var fee_type = $("#feeTypeNameCurrent option:selected").val().trim();
        // var operator_name = $("#operator_name option:selected").text().trim();
        var startTime = $("#startTimeCurrent").val().trim();
        var endTime = $("#endTimeCurrent").val().trim();
        var language = getCookie("language");
        var totalNo = parseInt($("#totalNoCurrent").text().trim());
        var method = "findReadInfoCurrent";
        var title = "readInfoCurrent";
        var url = "../report/exportReadInfo.do?user_name=" + user_name + "&community=" + community + "&building=" + building
                + "&unit=" + unit + "&room=" + room + "&meter_type=" + meter_typeCurrent + "&operator_name=&meter_no=" + meter_no + "&startTime=" + startTime
                + "&endTime=" + endTime + "&language=" + language + "&totalNo=" + totalNo + "&fileType=" + fileType +
                "&method=" + method + "&title=" + title + "&fee_type=" + fee_type;
        window.location.href = url;
        $("body").css("position", "relative");
        mask.appendTo("body");
        alert("$public_064");
        var f = setInterval(function () {
            jQuery.ajax({
                url: "../report/checkStatus.do",
                success: function (result) {
                    if (result.status === 1) {
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

    $(function () {
        load_communitiesCurrent();
        findFeeTypes();
        // findReadInfoCurrent();
        $("#currentReading").click(findReadInfoCurrent);
        $("#findReadInfoCurrent").click(findReadInfoCurrentBefore);
        $(".Current1").click(nextPageCurrent);
        $(".Current2").click(prePageCurrent);
        $(".Current3").click(firstPageCurrent);
        $(".Current4").click(lastPageCurrent);
        $(".input-pageCurrent").keypress(function (event) {
            var keycode = (event.keyCode ? event.keyCode : event.which);
            if (keycode === 13) {
                var pageIndex = $(this).val().trim();
                pageIndex = parseInt(pageIndex);
                var totalNo = parseInt($("#totalNoCurrent").text().trim());
                var pageSize = $(".pageSizeCurrent option:selected").val().trim();
                var pageNo = parseInt(totalNo / pageSize);
                var pageMax = totalNo % pageSize === 0 ? pageNo : pageNo + 1;
                if (pageIndex <= 0) {
                    alert("$public_038");
                } else if (pageIndex > pageMax) {
                    alert("$public_039");
                } else {
                    page_indexCurrent = pageIndex;
                    $(".input-pageCurrent")[0].value = pageIndex;
                    if (flagCurrent === 1) {
                        findReadInfoCurrent();
                    } else if (flagCurrent === 0) {
                        findReadInfoByMonthCurrent();
                    } else {
                        alert('error');
                    }
                }
                return false;
            }
        });


    });
});
