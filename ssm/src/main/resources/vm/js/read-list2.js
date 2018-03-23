// 加载所有抄表信息
Ext.onReady(function () {
    var monthDoubtful = "";
    var meter_typeDoubtful = "";
    var flagDoubtful = 1;
    var page_indexDoubtful = 1;
    var page_sizeDoubtful = 20;
    var columnsDoubtful = [
        new Ext.grid.RowNumberer({
            header: "$public_015", //序号
            width: 30,
            align: "center",
            renderer: function (value, metadata, record, rowIndex) {
                var start_noDoubtful = (page_indexDoubtful - 1) * page_sizeDoubtful;
                return start_noDoubtful + 1 + rowIndex;
            }
        }),
        {header: '$read_list_017', dataIndex: 'operate_id', minWidth: 130},
        {header: '$public_001', dataIndex: 'user_name', maxWidth: 120},
        {header: '$public_016', dataIndex: 'user_address_community', maxWidth: 100},
        {header: '$public_017', dataIndex: 'user_address_building', maxWidth: 40},
        {header: '$public_018', dataIndex: 'user_address_unit', maxWidth: 60},
        {header: '$public_019', dataIndex: 'user_address_room', maxWidth: 80},
        {header: '$public_020', dataIndex: 'contact_info', maxWidth: 120},
        {header: '$public_021', dataIndex: 'concentrator_name', minWidth: 80},
        {header: '$public_004', dataIndex: 'meter_no', maxWidth: 120},
        {header: '$public_022', dataIndex: 'fee_type', maxWidth: 120},
        {header: '$read_list_018', dataIndex: 'this_read', renderer: formatFloatDoubtful, minWidth: 60},
        {header: '$read_list_019', dataIndex: 'last_read', renderer: formatFloatDoubtful, minWidth: 60},
        {header: '$read_list_020', dataIndex: 'this_cost', renderer: formatFloatDoubtful, minWidth: 60},
        {header: '$read_list_021', dataIndex: 'last_cost', renderer: formatFloatDoubtful, minWidth: 60},
        {header: '$read_list_055', dataIndex: 'data4', renderer: formatFloatDoubtful, minWidth: 40, hidden: true},
        {header: '$read_list_054', dataIndex: 'data5', renderer: formatFloatDoubtful, minWidth: 60, hidden: true},
        {header: '$read_list_056', dataIndex: 'data6', renderer: formatFloatDoubtful, minWidth: 80, hidden: true},
        {header: '$read_list_057', dataIndex: 'data7', renderer: formatFloatDoubtful, minWidth: 80, hidden: true},
        {header: '$read_list_022', dataIndex: 'fee_need', renderer: formatFloatDoubtful, maxWidth: 70},
        {header: '$read_list_023', dataIndex: 'balance', renderer: formatFloatDoubtful, maxWidth: 70},
        {header: '$read_list_024', dataIndex: 'exception', renderer: exceptionDoubtful, maxWidth: 60},
        {header: '$read_list_025', dataIndex: 'fee_status', renderer: fee_statusDoubtful, maxWidth: 60},
        {header: '$read_list_026', dataIndex: 'read_type', renderer: read_typeDoubtful, maxWidth: 60},
        {header: '$read_list_027', dataIndex: 'operator_account', maxWidth: 100},
        {header: '$read_list_028', dataIndex: 'operate_time', width: 150,
            renderer: function (value) {
                return Ext.Date.format(new Date(value), 'Y-m-d H:i:s');
            }}
    ];
    var storeDoubtful = new Ext.data.Store({
        reader: {root: 'data.readInfoDoubtful'},
        fields: ['operate_id', 'user_name', 'user_address_community', 'user_address_building', 'user_address_unit', 'user_address_room', 'contact_info', 'concentrator_name',
            'meter_no', 'fee_type', 'this_read', 'last_read', 'this_cost', 'last_cost', 'data4', 'data5', 'data6', 'data7', 'fee_need', 'balance', 'exception', 'fee_status', 'read_type',
            'operator_account', 'operate_time']
    });

    var mask = $("<div class='window_mask'></div>");

    function  exceptionDoubtful(value) {
        if (value == "1") {
            return  "<span class='glyphicon  glyphicon-ok'   title='抄表成功'></span>";
        } else {
            return  "<span  class='glyphicon  glyphicon-remove' title='抄表失败'></span>";
        }
    }

    function  fee_statusDoubtful(value) {
        if (value == "1") {
            return  "<span class='glyphicon  glyphicon-ok'   title='$read_list_035'></span>";
        } else if (value == "0") {
            return  "<span  class='glyphicon  glyphicon-remove' title='$read_list_036'></span>";
        } else if (value == "2") {
            return "<span  class='glyphicon glyphicon-warning-sign' title='$read_list_063'></span>";
        } else if (value == "3") {
            return "<span  class='glyphicon glyphicon-warning-sign' title='$read_list_064'></span>";
        } else {
            return "<img src='../images/all/loading_icon_u478.gif' title='$remote_read_045'>";
        }
    }

    function  read_typeDoubtful(value) {
        if (value == "1") {
            value = "$read_list_058";
        } else {
            value = "$read_list_059";
        }
        return value;
    }

    function  formatFloatDoubtful(value) {
        if (value !== null) {
            value = value === parseInt(value) ? value : value.toFixed(2);
        } else {
            value = 0;
        }
        return value;
    }

    var tbDoubtful = new Ext.Toolbar({
        style: 'height:30px;padding:3px 15px 3px 10px; width:auto',
        autoWidth: true,
        items: [
            {
                text: '$read_list_004',
                handler: function () {
                    monthDoubtful = "all";
                    findReadInfoByMonthDoubtful();
                    $('#query_monthDoubtful').text('$read_list_004');
                }
            },
            {
                text: '$read_list_005',
                handler: function () {
                    monthDoubtful = "1";
                    findReadInfoByMonthDoubtful();
                    $('#query_monthDoubtful').text('$read_list_005');
                }
            },
            {
                text: '$read_list_006',
                handler: function () {
                    monthDoubtful = "2";
                    findReadInfoByMonthDoubtful();
                    $('#query_monthDoubtful').text('$read_list_006');
                }
            },
            {
                text: '$read_list_007',
                handler: function () {
                    monthDoubtful = "3";
                    findReadInfoByMonthDoubtful();
                    $('#query_monthDoubtful').text('$read_list_007');
                }
            },
            {text: '$read_list_008',
                handler: function () {
                    monthDoubtful = "4";
                    findReadInfoByMonthDoubtful();
                    $('#query_monthDoubtful').text('$read_list_008');
                }
            },
            {text: '$read_list_009',
                handler: function () {
                    monthDoubtful = "5";
                    findReadInfoByMonthDoubtful();
                    $('#query_monthDoubtful').text('$read_list_009');
                }
            },
            {text: '$read_list_010',
                handler: function () {
                    monthDoubtful = "6";
                    findReadInfoByMonthDoubtful();
                    $('#query_monthDoubtful').text('$read_list_010');
                }
            },
            {text: '$read_list_011',
                handler: function () {
                    monthDoubtful = "7";
                    findReadInfoByMonthDoubtful();
                    $('#query_monthDoubtful').text('$read_list_011');
                }
            },
            {text: '$read_list_012',
                handler: function () {
                    monthDoubtful = "8";
                    findReadInfoByMonthDoubtful();
                    $('#query_monthDoubtful').text('$read_list_012');
                }
            },
            {text: '$read_list_013',
                handler: function () {
                    monthDoubtful = "9";
                    findReadInfoByMonthDoubtful();
                    $('#query_monthDoubtful').text('$read_list_013');
                }
            },
            {text: '$read_list_014',
                handler: function () {
                    monthDoubtful = "10";
                    findReadInfoByMonthDoubtful();
                    $('#query_monthDoubtful').text('$read_list_014');
                }
            },
            {text: '$read_list_015',
                handler: function () {
                    monthDoubtful = "11";
                    findReadInfoByMonthDoubtful();
                    $('#query_monthDoubtful').text('$read_list_015');
                }
            },
            {text: '$read_list_016',
                handler: function () {
                    monthDoubtful = "12";
                    findReadInfoByMonthDoubtful();
                    $('#query_monthDoubtful').text('$read_list_016');
                }
            }]
    });

    tbDoubtful.add('');
    tbDoubtful.add('-', {text: '$read_list_042', handler: function () {
            meter_typeDoubtful = "10";
            if (flagDoubtful === 1) {
                findReadInfoDoubtful();
            } else if (flagDoubtful === 0) {
                findReadInfoByMonthDoubtful();
            }
            gridDoubtful.columns[15].setVisible(false);
            gridDoubtful.columns[16].setVisible(false);
            gridDoubtful.columns[17].setVisible(false);
            gridDoubtful.columns[18].setVisible(false);
            $('#query_meterDoubtful').text('$read_list_042');
        }}, {text: '$read_list_043', handler: function () {
            meter_typeDoubtful = "20";
            if (flagDoubtful === 1) {
                findReadInfoDoubtful();
            } else if (flagDoubtful === 0) {
                findReadInfoByMonthDoubtful();
            }
            gridDoubtful.columns[15].setVisible(true);
            gridDoubtful.columns[16].setVisible(true);
            gridDoubtful.columns[17].setVisible(true);
            gridDoubtful.columns[18].setVisible(true);
            $('#query_meterDoubtful').text('$read_list_043');
        }}, {text: '$read_list_052', handler: function () {
            meter_typeDoubtful = "30";
            if (flagDoubtful === 1) {
                findReadInfoDoubtful();
            } else if (flagDoubtful === 0) {
                findReadInfoByMonthDoubtful();
            }
            gridDoubtful.columns[15].setVisible(false);
            gridDoubtful.columns[16].setVisible(false);
            gridDoubtful.columns[17].setVisible(false);
            gridDoubtful.columns[18].setVisible(false);
            $('#query_meterDoubtful').text('$read_list_052');
        }}, {text: '$remote_read_047_2', handler: function () {
            meter_typeDoubtful = "40";
            if (flagDoubtful === 1) {
                findReadInfoDoubtful();
            } else if (flagDoubtful === 0) {
                findReadInfoByMonthDoubtful();
            }
            gridDoubtful.columns[15].setVisible(false);
            gridDoubtful.columns[16].setVisible(false);
            gridDoubtful.columns[17].setVisible(false);
            gridDoubtful.columns[18].setVisible(false);
            $('#query_meterDoubtful').text('$remote_read_047_2');
        }});//水表 气表 热表 电表
    tbDoubtful.add("->");
    tbDoubtful.add('<span>$public_006</span>');
    tbDoubtful.add("<select id='pageSizeDoubtful' class='pageSizeDoubtful' ><option value='5'>5</option> <option value='10'>10</option> <option value='15'>15</option> <option value='20' selected='selected'>20</option> <option value='30'>30</option> <option value='50'>50</option> </select>");
    tbDoubtful.add('<a  href="javascript:void(0)" class="glyphicon glyphicon-step-backward  Doubtful3"></a>');
    tbDoubtful.add('<a  href="javascript:void(0)" class="triangle-left  Doubtful2"></a>');
    tbDoubtful.add('$public_008<input class="input-pageDoubtful" type="text" name="" value="1" id="pageIndexDoubtful">$public_009');
    tbDoubtful.add('<a  href="javascript:void(0)" class="triangle-right  Doubtful1"></a>');
    tbDoubtful.add('<a  href="javascript:void(0)" class="glyphicon glyphicon-step-forward  Doubtful4"></a>');
    tbDoubtful.add('<span>$public_010<span><b  class="pagDoubtful"  style=""></b></span>$public_009');
    tbDoubtful.add('');
    tbDoubtful.add('');

    //导出按钮组件
    var exportButtonDoubtful = Ext.create('Ext.button.Split', {
        renderTo: Ext.getBody(),
        text: '$read_list_029',
        pressed: true,
        menu: new Ext.menu.Menu({
            items: [
                {
                    text: 'Excel',
                    handler: function () {
                        exportReadInfoDoubtful('EXCEL');
                    }
                },
                {
                    text: 'Dbf',
                    handler: function () {
                        exportReadInfoDoubtful('DBF');
                    }
                }
            ]
        })
    });

    var tb2Doubtful = new Ext.Toolbar({
        style: 'height:30px;padding:3px 10px; width:100%)',
        autoWidth: true,
        items: [exportButtonDoubtful]
    });
    tb2Doubtful.add('');
    tb2Doubtful.add('');
    tb2Doubtful.add('<span>$user_manage_03701<span class="totalNoDoubtful" id="totalNoDoubtful"></span>$user_manage_03702</span>');
    tb2Doubtful.add('');
    tb2Doubtful.add('');
    tb2Doubtful.add('-');
    tb2Doubtful.add('<span>$read_list_060:<span class="totalNoDoubtful"  id="query_monthDoubtful">$read_list_004</span></span>');
    tb2Doubtful.add('');
    tb2Doubtful.add('<span>$read_list_061:<span class="totalNoDoubtful"  id="query_meterDoubtful">$read_list_004</span></span>');
    var smDoubtful = new Ext.selection.CheckboxModel();
    var gridDoubtful = new Ext.grid.GridPanel({
        height: 400,
        renderTo: 'gridDoubtful',
        layout: 'fit',
        store: storeDoubtful,
        autoWidth: true,
        loadMask: true,
        autoScroll: true,
        bodyStyle: 'overflow-x:hidden;overflow-y:auto;',
        columns: columnsDoubtful,
        selType: 'cellmodel',
        tbar: tbDoubtful,
        bbar: tb2Doubtful,
        selModel: smDoubtful
    });
    Ext.EventManager.onWindowResize(function () {
        gridDoubtful.getView().refresh();
    });
    gridDoubtful.setAutoScroll(true);

    //加载费率类型选项
    function findFeeTypes() {
        $("#feeTypeNameDoubtful").empty();
        $("#feeTypeNameDoubtful").append($("<option value=''>$user_manage_06701</option>"));
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
                        $("#feeTypeNameDoubtful").append($(str));
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
    function findReadInfoDoubtfulBefore() {
        page_indexDoubtful = 1;
        $(".input-pageDoubtful").val(1);
        findReadInfoDoubtful();
    }
    function findReadInfoDoubtful() {
        var meter_type = meter_typeDoubtful;
        var feeTypeName = $("#feeTypeNameDoubtful option:selected").val().trim();
        var meter_no = $("#meter_noDoubtful").val().trim();
        if (isNaN(meter_no)) {
            alert("$read_list_053");
            return;
        }
        var pageNo = $(".input-pageDoubtful").val().trim();
        var pageSize = $(".pageSizeDoubtful option:selected").val().trim();
        var user_name = $("#user_nameDoubtful").val().trim();
        //从浏览器URL中解析小区名
        var community_name = getQueryString("community_name");
        //如果浏览器中解析到小区名，就设置该小区为选中状态
        if (community_name != "") {
            $("#user_address_communityDoubtful option[value='" + community_name + "']").attr("selected", true);
            findAllBuildingsDoubtful();
        }
        //从搜索条件中获取小区名
        var community = $("#user_address_communityDoubtful option:selected").val().trim();
        var building = $("#user_address_buildingDoubtful option:selected").val().trim();
        var unit = $("#user_address_unitDoubtful option:selected").val().trim();
        var room = $("#user_address_roomDoubtful option:selected").val().trim();
        //var operator_name = $("#operator_name option:selected").text().trim();
        var startTime = $("#startTimeDoubtful").val().trim();
        var endTime = $("#endTimeDoubtful").val().trim();
        var startNo = (pageNo - 1) * pageSize;
        var queryTableType = "queryDoubtful";
        jQuery.ajax({
            url: "../user/findReadInfo.do",
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
                "queryTableType": queryTableType
            },
            dataType: "json",
            success: function (result) {
                if (result.status == 1) {
                    var map = result.data;
                    var readInfoDoubtful = map["readInfo"];
                    var totalDoubtful = map["totalNo"];
                    $("#totalNoDoubtful").text(totalDoubtful);
                    var page = parseInt(totalDoubtful / pageSize);
                    var pageMax = totalDoubtful % pageSize == 0 ? page : page + 1;
                    //$(".glyphicon.glyphicon-step-forward.span4").next().text(pageMax);
                    $("#gridDoubtful .pagDoubtful").text(pageMax);
                    storeDoubtful.loadData(readInfoDoubtful);
                    flagDoubtful = 1;
                }
            },
            error: function () {
                alert("error");
            }
        });
    }



    // 按月份查询抄表信息
    function findReadInfoByMonthDoubtful() {
        var pageNo = $(".input-pageDoubtful").val().trim();
        var pageSize = $(".pageSizeDoubtful option:selected").val().trim();
        var startNo = (pageNo - 1) * pageSize;
        var meter_type = meter_typeDoubtful;
        var queryTableType = "queryDoubtful";
        jQuery.ajax({
            url: "../user/findReadInfoByMonth.do",
            type: "post",
            data: {
                "month": monthDoubtful,
                "meter_type": meter_type,
                "startNo": startNo,
                "pageSize": pageSize,
                "queryTableType": queryTableType
            },
            dataType: "json",
            success: function (result) {
                if (result.status == 1) {
                    var map = result.data;
                    var readInfoDoubtful = map["readInfo"];
                    var totalNoDoubtful = map["totalNo"];
                    $("#totalNoDoubtful").text(totalNoDoubtful);
                    var page = parseInt(totalNoDoubtful / pageSize);
                    var pageMax = totalNoDoubtful % pageSize == 0 ? page : page + 1;
                    //$(".glyphicon.glyphicon-step-forward.span4").next().text(pageMax);
                    $("#gridDoubtful  .pagDoubtful").text(pageMax);
                    storeDoubtful.loadData(readInfoDoubtful);
                    flagDoubtful = 0;
                    //	month=null;
                }
            },
            error: function () {
                alert("error");
            }
        });
    }


    $("#pageSizeDoubtful").change(function () {
        //$(".pageSize").find("option[value=" + pageSize + "]").attr("selected", true);
        if (flagDoubtful == 1) {
            findReadInfoDoubtful();
        } else if (flagDoubtful == 0) {
            findReadInfoByMonthDoubtful();
        } else {
            alert('$public_036');
        }
    });


    // 下一页
    function nextPageDoubtful() {
        var pageIndex = $(".input-pageDoubtful").val().trim();
        pageIndex = parseInt(pageIndex) + 1;
        var totalNo = parseInt($("#totalNoDoubtful").text().trim());
        var pageSize = $(".pageSizeDoubtful option:selected").val().trim();
        page_sizeDoubtful = pageSize;
        var pageNo = parseInt(totalNo / pageSize);
        var pageMax = totalNo % pageSize == 0 ? pageNo : pageNo + 1;
        if (pageIndex <= 0) {
            alert("$public_038");
        } else if (pageIndex > pageMax) {
            alert("$public_039");
        } else {
            $(".input-pageDoubtful")[0].value = pageIndex;
            //$(".input-page")[1].value = pageIndex;
            page_indexDoubtful = pageIndex;
            if (flagDoubtful == 1) {
                findReadInfoDoubtful();
            } else if (flagDoubtful == 0) {
                findReadInfoByMonthDoubtful();
            } else {
                alert('$public_036');
            }
        }
    }



    // 上一页
    function prePageDoubtful() {
        var pageIndex = $(".input-pageDoubtful").val().trim();
        pageIndex = parseInt(pageIndex) - 1;
        if (pageIndex <= 0) {
            alert("$public_040");
        } else {
            $(".input-pageDoubtful")[0].value = pageIndex;
            //$(".input-page")[1].value = pageIndex;
            page_indexDoubtful = pageIndex;
            if (flagDoubtful == 1) {
                findReadInfoDoubtful();
            } else if (flagDoubtful == 0) {
                findReadInfoByMonthDoubtful();
            } else {
                alert('$public_036');
            }
        }
    }

    // 第一页
    function firstPageDoubtful() {
        $(".input-pageDoubtful")[0].value = 1;
        //$(".input-page")[1].value = 1;
        page_indexDoubtful = 1;
        if (flagDoubtful == 1) {
            findReadInfoDoubtful();
        } else if (flagDoubtful == 0) {
            findReadInfoByMonthDoubtful();
        } else {
            alert('$public_036');
        }
    }



    // 最后一页
    function lastPageDoubtful() {
        var totalNo = parseInt($("#totalNoDoubtful").text().trim());
        var pageSize = $(".pageSizeDoubtful option:selected").val().trim();
        // 根据记录总数，计算出最后一页的页数
        var pageNo = parseInt(totalNo / pageSize);
        var pageEnd = totalNo % pageSize == 0 ? pageNo : pageNo + 1;
        $(".input-pageDoubtful")[0].value = pageEnd;
        //$(".input-page")[1].value = pageEnd;
        page_indexDoubtful = pageEnd;
        if (flagDoubtful == 1) {
            findReadInfoDoubtful();
        } else if (flagDoubtful == 0) {
            findReadInfoByMonthDoubtful();
        } else {
            alert('$public_036');
        }
    }


    /**
     * 导出抄表记录
     * @returns {undefined}
     */
    function exportReadInfoDoubtful(fileType) {
        var meter_no = $("#meter_noDoubtful").val().trim();
        if (isNaN(meter_no)) {
            alert("public_065");
            return;
        }
        var pageNo = $(".input-pageDoubtful").val().trim();
        pageNo = pageNo == "" ? 1 : pageNo;
        var user_name = $("#user_nameDoubtful").val().trim();
        //从浏览器URL中解析小区名
        var community_name = getQueryString("community_name");
        //如果浏览器中解析到小区名，就设置该小区为选中状态
        if (community_name != "") {
            $("#user_address_communityDoubtful option[value=" + community_name + "]").attr("selected", true);
            findAllBuildingsDoubtful();
        }
        //从搜索条件中获取小区名
        var community = $("#user_address_communityDoubtful option:selected").val().trim();
        var building = $("#user_address_buildingDoubtful option:selected").val().trim();
        var unit = $("#user_address_unitDoubtful option:selected").val().trim();
        var room = $("#user_address_roomDoubtful option:selected").val().trim();
        var fee_type = $("#feeTypeNameDoubtful option:selected").val().trim();
        // var operator_name = $("#operator_name option:selected").text().trim();
        var startTime = $("#startTimeDoubtful").val().trim();
        var endTime = $("#endTimeDoubtful").val().trim();
        var language = getCookie("language");
        var totalNo = parseInt($("#totalNoDoubtful").text().trim());
        var method = "findReadInfoDoubtful";
        var title = "readinfoDoubtful";
        var url = "../report/exportReadInfo.do?user_name=" + user_name + "&community=" + community + "&building=" + building
                + "&unit=" + unit + "&room=" + room + "&meter_type=" + meter_typeDoubtful + "&operator_name=&meter_no=" + meter_no + "&startTime=" + startTime
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

    $(function () {
        load_communitiesDoubtful();
        // findReadInfoDoubtful();
        findFeeTypes();
        $("#ReadInfoDoubtful").click(findReadInfoDoubtful);
        $("#findReadInfoDoubtful").click(findReadInfoDoubtfulBefore);
        $(".Doubtful1").click(nextPageDoubtful);
        $(".Doubtful2").click(prePageDoubtful);
        $(".Doubtful3").click(firstPageDoubtful);
        $(".Doubtful4").click(lastPageDoubtful);
        $(".input-pageDoubtful").keypress(function (event) {
            var keycode = (event.keyCode ? event.keyCode : event.which);
            if (keycode == 13) {
                var pageIndex = $(this).val().trim();
                pageIndex = parseInt(pageIndex);
                var totalNo = parseInt($("#totalNoDoubtful").text().trim());
                var pageSize = $(".pageSizeDoubtful option:selected").val().trim();
                var pageNo = parseInt(totalNo / pageSize);
                var pageMax = totalNo % pageSize == 0 ? pageNo : pageNo + 1;
                if (pageIndex <= 0) {
                    alert("$public_038");
                } else if (pageIndex > pageMax) {
                    alert("$public_039");
                } else {
                    page_indexDoubtful = pageIndex;
                    $(".input-pageDoubtful")[0].value = pageIndex;
                    if (flagDoubtful == 1) {
                        findReadInfoDoubtful();
                    } else if (flagDoubtful == 0) {
                        findReadInfoByMonthDoubtful();
                    } else {
                        alert('error');
                    }
                }
                return false;
            }
        });


    });
});
