// 加载所有抄表信息
Ext.onReady(function () {
    var monthFail = "";
    var meter_typeFail = "";
    var flagFail = 1;
    var page_indexFail = 1;
    var page_sizeFail = 20;
    var dataFail = '';
    var columnsFail = [
        new Ext.grid.RowNumberer({
            header: "$public_015", //序号
            width: 30,
            align: "center",
            renderer: function (value, metadata, record, rowIndex) {
                var start_noFail = (page_indexFail - 1) * page_sizeFail;
                return start_noFail + 1 + rowIndex;
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
        {header: '$read_list_018', dataIndex: 'this_read', minWidth: 60},
        {header: '$read_list_019', dataIndex: 'last_read', renderer: formatFloatFail, minWidth: 60},
        {header: '$read_list_020', dataIndex: 'this_cost', renderer: formatFloatFail, minWidth: 60},
        {header: '$read_list_021', dataIndex: 'last_cost', renderer: formatFloatFail, minWidth: 60},
        {header: '$read_list_055', dataIndex: 'data4', renderer: formatFloatFail, minWidth: 40, hidden: true},
        {header: '$read_list_054', dataIndex: 'data5', renderer: formatFloatFail, minWidth: 60, hidden: true},
        {header: '$read_list_056', dataIndex: 'data6', renderer: formatFloatFail, minWidth: 80, hidden: true},
        {header: '$read_list_057', dataIndex: 'data7', renderer: formatFloatFail, minWidth: 80, hidden: true},
        {header: '$read_list_022', dataIndex: 'fee_need', renderer: formatFloatFail, maxWidth: 70},
        {header: '$read_list_023', dataIndex: 'balance', renderer: formatFloatFail, maxWidth: 70},
        {header: '$read_list_024', dataIndex: 'exception', renderer: exceptionFail, maxWidth: 60},
        {header: '$read_list_025', dataIndex: 'fee_status', renderer: fee_statusFail, maxWidth: 60},
        {header: '$read_list_026', dataIndex: 'read_type', renderer: read_typeFail, maxWidth: 60},
        {header: '$read_list_027', dataIndex: 'operator_account', maxWidth: 100},
        {header: '$read_list_028', dataIndex: 'operate_time', width: 150,
            renderer: function (value) {
                return Ext.Date.format(new Date(value), 'Y-m-d H:i:s');
            }}
    ];

    var storeFail = new Ext.data.Store({
        reader: {root: 'data.readInfoFail'},
        fields: ['operate_id', 'user_name', 'user_address_community', 'user_address_building', 'user_address_unit', 'user_address_room', 'contact_info', 'concentrator_name',
            'meter_no', 'fee_type', 'this_read', 'last_read', 'this_cost', 'last_cost', 'data4', 'data5', 'data6', 'data7', 'fee_need', 'balance', 'exception', 'fee_status', 'read_type',
            'operator_account', 'operate_time']
    });
    //storeFail.load();
    var mask = $("<div class='window_mask'></div>");

    function  exceptionFail(value) {
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

    function  fee_statusFail(value) {
        if (value == "1") {
            return  "<span class='glyphicon  glyphicon-ok' title='$read_list_037' ></span>";
        } else if (value == "0") {
            return  "<span  class='glyphicon  glyphicon-remove' title='$read_list_038'></span>";
        } else {
            return  "<span  class='glyphicon  glyphicon-record' title='$read_list_039'></span>";
        }
    }

    function  read_typeFail(value) {
        if (value == "1") {
            value = "$read_list_058";
        } else {
            value = "$read_list_059";
        }
        return value;
    }

    function  formatFloatFail(value) {
        if (value !== null) {
            value = value === parseInt(value) ? value : value.toFixed(2);
        } else {
            value = 0;
        }
        return value;
    }

    var tbFail = new Ext.Toolbar({
        style: 'height:30px;padding:3px 15px 3px 10px; width:auto',
        autoWidth: true,
        items: [
            {
                text: '$read_list_004',
                handler: function () {
                    monthFail = "all";
                    findReadInfoByMonthFail();
                    $('#query_monthFail').text('$read_list_004');
                }
            },
            {
                text: '$read_list_005',
                handler: function () {
                    monthFail = "1";
                    findReadInfoByMonthFail();
                    $('#query_monthFail').text('$read_list_005');
                }
            },
            {
                text: '$read_list_006',
                handler: function () {
                    monthFail = "2";
                    findReadInfoByMonthFail();
                    $('#query_monthFail').text('$read_list_006');
                }
            },
            {
                text: '$read_list_007',
                handler: function () {
                    monthFail = "3";
                    findReadInfoByMonthFail();
                    $('#query_monthFail').text('$read_list_007');
                }
            },
            {text: '$read_list_008',
                handler: function () {
                    monthFail = "4";
                    findReadInfoByMonthFail();
                    $('#query_monthFail').text('$read_list_008');
                }
            },
            {text: '$read_list_009',
                handler: function () {
                    monthFail = "5";
                    findReadInfoByMonthFail();
                    $('#query_monthFail').text('$read_list_009');
                }
            },
            {text: '$read_list_010',
                handler: function () {
                    monthFail = "6";
                    findReadInfoByMonthFail();
                    $('#query_monthFail').text('$read_list_010');
                }
            },
            {text: '$read_list_011',
                handler: function () {
                    monthFail = "7";
                    findReadInfoByMonthFail();
                    $('#query_monthFail').text('$read_list_011');
                }
            },
            {text: '$read_list_012',
                handler: function () {
                    monthFail = "8";
                    findReadInfoByMonthFail();
                    $('#query_month').text('$read_list_012');
                }
            },
            {text: '$read_list_013',
                handler: function () {
                    monthFail = "9";
                    findReadInfoByMonthFail();
                    $('#query_monthFail').text('$read_list_013');
                }
            },
            {text: '$read_list_014',
                handler: function () {
                    monthFail = "10";
                    findReadInfoByMonthFail();
                    $('#query_monthFail').text('$read_list_014');
                }
            },
            {text: '$read_list_015',
                handler: function () {
                    monthFail = "11";
                    findReadInfoByMonthFail();
                    $('#query_monthFail').text('$read_list_015');
                }
            },
            {text: '$read_list_016',
                handler: function () {
                    monthFail = "12";
                    findReadInfoByMonthFail();
                    $('#query_monthFail').text('$read_list_016');
                }
            }]
    });

    tbFail.add('');
    tbFail.add('-', {text: '$read_list_042', handler: function () {
            meter_typeFail = "10";
            if (flagFail === 1) {
                findReadInfoFail();
            } else if (flagFail === 0) {
                findReadInfoByMonthFail();
            }
            gridFail.columns[15].setVisible(false);
            gridFail.columns[16].setVisible(false);
            gridFail.columns[17].setVisible(false);
            gridFail.columns[18].setVisible(false);
            $('#query_meterFail').text('$read_list_042');
        }}, {text: '$read_list_043', handler: function () {
            meter_typeFail = "20";
            if (flagFail === 1) {
                findReadInfoFail();
            } else if (flagFail === 0) {
                findReadInfoByMonthFail();
            }
            gridFail.columns[15].setVisible(true);
            gridFail.columns[16].setVisible(true);
            gridFail.columns[17].setVisible(true);
            gridFail.columns[18].setVisible(true);
            $('#query_meterFail').text('$read_list_043');
        }}, {text: '$read_list_052', handler: function () {
            meter_typeFail = "30";
            if (flagFail === 1) {
                findReadInfoFail();
            } else if (flagFail === 0) {
                findReadInfoByMonthFail();
            }
            gridFail.columns[15].setVisible(false);
            gridFail.columns[16].setVisible(false);
            gridFail.columns[17].setVisible(false);
            gridFail.columns[18].setVisible(false);
            $('#query_meterFail').text('$read_list_052');
        }}, {text: '$remote_read_047_2', handler: function () {
            meter_typeFail = "40";
            if (flagFail === 1) {
                findReadInfoFail();
            } else if (flagFail === 0) {
                findReadInfoByMonthFail();
            }
            gridFail.columns[15].setVisible(false);
            gridFail.columns[16].setVisible(false);
            gridFail.columns[17].setVisible(false);
            gridFail.columns[18].setVisible(false);
            $('#query_meterFail').text('$remote_read_047_2');
        }});//水表 气表 热表 电表
    tbFail.add("->");
    tbFail.add('<span>$public_006</span>');
    tbFail.add("<select id='pageSizeFail' class='pageSizeFail' ><option value='5'>5</option> <option value='10'>10</option> <option value='15'>15</option> <option value='20' selected='selected'>20</option> <option value='30'>30</option> <option value='50'>50</option> </select>");
    tbFail.add('<a  href="javascript:void(0)" class="glyphicon glyphicon-step-backward  Fail3"></a>');
    tbFail.add('<a  href="javascript:void(0)" class="triangle-left  Fail2"></a>');
    tbFail.add('$public_008<input class="input-pageFail"type="text" name="" value="1" id="pageIndexFail">$public_009');
    tbFail.add('<a  href="javascript:void(0)" class="triangle-right  Fail1"></a>');
    tbFail.add('<a  href="javascript:void(0)" class="glyphicon glyphicon-step-forward  Fail4"></a>');
    tbFail.add('<span>$public_010<span><b  class="pagFail"  style=""></b></span>$public_009');
    tbFail.add('');
    tbFail.add('');

    //导出按钮组件
    var exportButtonFail = Ext.create('Ext.button.Split', {
        renderTo: Ext.getBody(),
        text: '$read_list_029',
        pressed: true,
        menu: new Ext.menu.Menu({
            items: [
                {
                    text: 'Excel',
                    handler: function () {
                        exportReadInfoFail('EXCEL');
                    }
                },
                {
                    text: 'Dbf',
                    handler: function () {
                        exportReadInfoFail('DBF');
                    }
                }
            ]
        })
    });

    var tb2Fail = new Ext.Toolbar({
        style: 'height:30px;padding:3px 10px; width:100%)',
        autoWidth: true,
        items: [exportButtonFail]
    });
    tb2Fail.add('');
    tb2Fail.add('<button class="btn" style="background-color:#a4c2f4;line-height:9px;font-size:8px;font-width:normal" id="againRecord">$read_list_030</button>');
    tb2Fail.add('<button class="btn" style="background-color:#a4c2f4;line-height:9px;font-size:8px;font-width:normal" id="manualEntry">$read_list_031</button>');
    tb2Fail.add('<span>$user_manage_03701<span class="totalNoFail" id="totalNoFail"></span>$user_manage_03702</span>');
    tb2Fail.add('');
    tb2Fail.add('');
    tb2Fail.add('-');
    tb2Fail.add('<span>$read_list_060<span class="totalNoFail"  id="query_monthFail">$read_list_004</span></span>');
    tb2Fail.add('');
    tb2Fail.add('<span>$read_list_061<span class="totalNoFail"  id="query_meterFail">$read_list_004</span></span>');
    var smFail = new Ext.selection.CheckboxModel();
    var gridFail = new Ext.grid.GridPanel({
        height: 400,
        renderTo: 'gridFail',
        layout: 'fit',
        store: storeFail,
        autoWidth: true,
        loadMask: true,
        autoScroll: true,
        bodyStyle: 'overflow-x:hidden;overflow-y:auto;',
        columns: columnsFail,
        selType: 'cellmodel',
        tbar: tbFail,
        bbar: tb2Fail,
        selModel: smFail
    });
    Ext.EventManager.onWindowResize(function () {
        gridFail.getView().refresh();
    });
    gridFail.setAutoScroll(true);

    //加载费率类型选项
    function findFeeTypes() {
        $("#feeTypeNameFail").empty();
        $("#feeTypeNameFail").append($("<option value=''>$user_manage_06701</option>"));
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
                        $("#feeTypeNameFail").append($(str));
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
    function findReadInfoFailBefore() {
        page_indexFail = 1;
        $(".input-pageFail").val(1);
        findReadInfoFail();
    }
    function findReadInfoFail() {
        var meter_type = meter_typeFail;
        var feeTypeName = $("#feeTypeNameFail option:selected").val().trim();
        var meter_no = $("#meter_noFail").val().trim();
        if (isNaN(meter_no)) {
            alert("$read_list_053");
            return;
        }
        var pageNo = $(".input-pageFail").val().trim();
        var pageSize = $(".pageSizeFail option:selected").val().trim();
        var user_name = $("#user_nameFail").val().trim();
        //从浏览器URL中解析小区名
        var community_name = getQueryString("community_name");
        //如果浏览器中解析到小区名，就设置该小区为选中状态
        if (community_name != "") {
            $("#user_address_communityFail option[value='" + community_name + "']").attr("selected", true);
            findAllBuildingsFail();
        }
        //从搜索条件中获取小区名
        var community = $("#user_address_communityFail option:selected").val().trim();
        var building = $("#user_address_buildingFail option:selected").val().trim();
        var unit = $("#user_address_unitFail option:selected").val().trim();
        var room = $("#user_address_roomFail option:selected").val().trim();
        //var operator_name = $("#operator_name option:selected").text().trim();
        var startTime = $("#startTimeFail").val().trim();
        var endTime = $("#endTimeFail").val().trim();
        var startNo = (pageNo - 1) * pageSize;
        var queryTableType = "queryFail";
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
                    var readInfoFail = map["readInfo"];
                    var totalNoFail = map["totalNo"];
                    $("#totalNoFail").text(totalNoFail);
                    var page = parseInt(totalNoFail / pageSize);
                    var pageMax = totalNoFail % pageSize == 0 ? page : page + 1;
                    //$(".glyphicon.glyphicon-step-forward.span4").next().text(pageMax);
                    $("#gridFail  .pagFail").text(pageMax);
                    storeFail.loadData(readInfoFail);
                    dataFail = readInfoFail;
                    flagFail = 1;
                }
            },
            error: function () {
                alert("error");
            }
        });
    }



    // 按月份查询抄表信息
    function findReadInfoByMonthFail() {
        var pageNo = $(".input-pageFail").val().trim();
        var pageSize = $(".pageSizeFail option:selected").val().trim();
        var startNo = (pageNo - 1) * pageSize;
        var meter_type = meter_typeFail;
        var queryTableType = "queryFail";
        jQuery.ajax({
            url: "../user/findReadInfoByMonth.do",
            type: "post",
            data: {
                "month": monthFail,
                "meter_type": meter_type,
                "startNo": startNo,
                "pageSize": pageSize,
                "queryTableType": queryTableType
            },
            dataType: "json",
            success: function (result) {
                if (result.status == 1) {
                    var map = result.data;
                    var readInfoFail = map["readInfo"];
                    var totalNoFail = map["totalNo"];
                    $("#totalNoFail").text(totalNoFail);
                    var page = parseInt(totalNoFail / pageSize);
                    var pageMax = totalNoFail % pageSize == 0 ? page : page + 1;
                    //$(".glyphicon.glyphicon-step-forward.span4").next().text(pageMax);
                    $("#gridFail .pagFail").text(pageMax);
                    storeFail.loadData(readInfoFail);
                    flagFail = 0;
                    //	month=null;
                }
            },
            error: function () {
                alert("error");
            }
        });
    }


    $("#pageSizeFail").change(function () {
        //$(".pageSize").find("option[value=" + pageSize + "]").attr("selected", true);
        if (flagFail == 1) {
            findReadInfoFail();
        } else if (flagFail == 0) {
            findReadInfoByMonthFail();
        } else {
            alert('$public_036');
        }
    });


    // 下一页
    function nextPageFail() {
        var pageIndex = $(".input-pageFail").val().trim();
        pageIndex = parseInt(pageIndex) + 1;
        var totalNo = parseInt($("#totalNoFail").text().trim());
        var pageSize = $(".pageSizeFail option:selected").val().trim();
        page_sizeFail = pageSize;
        var pageNo = parseInt(totalNo / pageSize);
        var pageMax = totalNo % pageSize == 0 ? pageNo : pageNo + 1;
        if (pageIndex <= 0) {
            alert("$public_038");
        } else if (pageIndex > pageMax) {
            alert("$public_039");
        } else {
            $(".input-pageFail")[0].value = pageIndex;
            //$(".input-page")[1].value = pageIndex;
            page_indexFail = pageIndex;
            if (flagFail == 1) {
                findReadInfoFail();
            } else if (flagFail == 0) {
                findReadInfoByMonthFail();
            } else {
                alert('$public_036');
            }
        }
    }



    // 上一页
    function prePageFail() {
        var pageIndex = $(".input-pageFail").val().trim();
        pageIndex = parseInt(pageIndex) - 1;
        if (pageIndex <= 0) {
            alert("$public_040");
        } else {
            $(".input-pageFail")[0].value = pageIndex;
            //$(".input-page")[1].value = pageIndex;
            page_indexFail = pageIndex;
            if (flagFail == 1) {
                findReadInfoFail();
            } else if (flagFail == 0) {
                findReadInfoByMonthFail();
            } else {
                alert('$public_036');
            }
        }
    }

    // 第一页
    function firstPageFail() {
        $(".input-pageFail")[0].value = 1;
        //$(".input-page")[1].value = 1;
        page_indexFail = 1;
        if (flagFail == 1) {
            findReadInfoFail();
        } else if (flagFail == 0) {
            findReadInfoByMonthFail();
        } else {
            alert('$public_036');
        }
    }



    // 最后一页
    function lastPageFail() {
        var totalNo = parseInt($("#totalNoFail").text().trim());
        var pageSize = $(".pageSizeFail option:selected").val().trim();
        // 根据记录总数，计算出最后一页的页数
        var pageNo = parseInt(totalNo / pageSize);
        var pageEnd = totalNo % pageSize == 0 ? pageNo : pageNo + 1;
        $(".input-pageFail")[0].value = pageEnd;
        //$(".input-page")[1].value = pageEnd;
        page_indexFail = pageEnd;
        if (flagFail == 1) {
            findReadInfoFail();
        } else if (flagFail == 0) {
            findReadInfoByMonthFail();
        } else {
            alert('$public_036');
        }
    }


    /**
     * 导出抄表记录
     * @returns {undefined}
     */
    function exportReadInfoFail(fileType) {
        var meter_no = $("#meter_noFail").val().trim();
        if (isNaN(meter_no)) {
            alert("public_065");
            return;
        }
        var pageNo = $(".input-pageFail").val().trim();
        pageNo = pageNo == "" ? 1 : pageNo;
        var user_name = $("#user_nameFail").val().trim();
        //从浏览器URL中解析小区名
        var community_name = getQueryString("community_name");
        //如果浏览器中解析到小区名，就设置该小区为选中状态
        if (community_name != "") {
            $("#user_address_communityFail option[value=" + community_name + "]").attr("selected", true);
            findAllBuildingsFail();
        }
        //从搜索条件中获取小区名
        var community = $("#user_address_communityFail option:selected").val().trim();
        var building = $("#user_address_buildingFail option:selected").val().trim();
        var unit = $("#user_address_unitFail option:selected").val().trim();
        var room = $("#user_address_roomFail option:selected").val().trim();
        var fee_type = $("#feeTypeNameFail option:selected").val().trim();
        // var operator_name = $("#operator_name option:selected").text().trim();
        var startTime = $("#startTimeFail").val().trim();
        var endTime = $("#endTimeFail").val().trim();
        var language = getCookie("language");
        var totalNo = parseInt($("#totalNoFail").text().trim());
        var method = "findReadInfoFail";
        var title = "readinfoFail";
        var url = "../report/exportReadInfo.do?user_name=" + user_name + "&community=" + community + "&building=" + building
                + "&unit=" + unit + "&room=" + room + "&meter_type=" + meter_typeFail + "&operator_name=&meter_no=" + meter_no + "&startTime=" + startTime
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
    function againRecord() {
        var recordList = gridFail.getSelectionModel().getSelection();
        gridFail.getSelectionModel().clearSelections();
        gridFail.getView().refresh();
        var meter_noFalseList = new Array();
        var meter_noFalseList1 = new Array();
        var meter_noSucList = new Array();
        var arr = new Array();
        var isAutoClear = "";
        var accountName = "";
        var sendTime = format(new Date(), "yyyy-MM-dd HH:mm:ss");
        if (recordList.length == 0) {
            alert('$read_list_032');
        } else {
            $('#re_meter').modal();
            $("#send_parameters button").click(function () {
                var auto = $(this).text().trim();    //去掉当前文本的两端的空格
                isAutoClear = auto == "$public_057" ? 0 : 1;
                accountName = getCookie("account_name");
                // 获取抄表请求参数
                for (var i = 0; i < recordList.length; i++) {
                    recordList[i].set('exception', '<img src="../images/all/loading_icon_u478.gif" title="$remote_read_045">');
                    var concentrator_name = recordList[i].data.concentrator_name.trim();
                    var meter_no = recordList[i].data.meter_no.trim();
                    var ary = new Array(concentrator_name, meter_no, "", "");
                    arr.push(ary);
                }
                // 添加空字符串数组，解决单个数组无法正确传参的问题
                arr.push(new Array("", "", "", ""));

                // 后台发送抄表请求
                jQuery.ajax({
                    url: "../user/sendFailedParameters.do",
                    type: "post",
                    data: {"list": arr, "isAutoClear": isAutoClear, "accountName": accountName},
                    dataType: "json",
                    traditional: true,
                    success: function (result) {
                        if (result.status == 1) {
                            var map = result.data;
                            meter_noFalseList = map.meter_noFalseList;
                            meter_noFalseList1 = map.meter_noFalseList1;
                            meter_noSucList = map.meter_noSucList;
                            sendTime = map.sendTime;
                            var check = setInterval(function () {
                                jQuery.ajax({
                                    url: "../user/findReadResult.do",
                                    data: {"sendTime": sendTime},
                                    type: "post",
                                    dataType: "json",
                                    success: function (result) {
                                        if (result.status === 1) {
                                            for (var i = 0; i < meter_noFalseList.length; i++) {
                                                var meter_noFalse = meter_noFalseList[i];
                                                for (var j = 0; j < recordList.length; j++) {
                                                    if (meter_noFalse === recordList[j].data.meter_no) {
                                                        recordList[j].set('exception', '2');
                                                        recordList[j].set('fee_status', '2');
                                                    }
                                                }
                                            }
                                            for (var i = 0; i < meter_noFalseList1.length; i++) {
                                                var meter_noFalse1 = meter_noFalseList1[i];
                                                for (var j = 0; j < recordList.length; j++) {
                                                    if (meter_noFalse1 === recordList[j].data.meter_no) {
                                                        recordList[j].set('exception', '3');
                                                        recordList[j].set('fee_status', '2');
                                                    }
                                                }
                                            }
                                            var readInfo = result.data;
                                            for (var i = 0; i < meter_noSucList.length; i++) {
                                                var meter_noSuc = meter_noSucList[i];
                                                for (var j = 0; j < readInfo.length; j++) {
                                                    var meter_no = readInfo[j].meter_no;
                                                    if (meter_noSuc == meter_no) {
                                                        var exception = readInfo[j].exception;
                                                        if (exception == "1") {
                                                            recordList[j].set('exception', '1');
                                                        } else {
                                                            recordList[j].set('exception', '0');
                                                        }
                                                        var fee_status = readInfo[j].fee_status;
                                                        if (fee_status == "1") {
                                                            recordList[j].set('fee_status', '1');
                                                        } else {
                                                            recordList[j].set('fee_status', '0');
                                                        }
                                                    }
                                                }
                                            }
                                            if (meter_noSucList.length === readInfo.length) {
                                                recordList = "";
                                                clearInterval(check);
                                            }
                                        } else {
                                            alert("$public_067");
                                        }
                                    },
                                    error: function () {
                                        alert("error");
                                    }
                                });
                            }, 3000);
                        } else {
                            for (var i = 0; i < recordList.length; i++) {
                                recordList[i].set('exception', '<span class="glyphicon glyphicon-remove" title="$remote_read_050"></span>');
                                recordList[i].set('fee_status', '<span class="glyphicon glyphicon-remove"></span>');
                            }
                            alert("$public_066");
                        }
                    },
                    error: function () {
                        for (var i = 0; i < recordList.length; i++) {
                            recordList[i].set('exception', '<span class="glyphicon glyphicon-remove" title="$remote_read_050"></span>');
                            recordList[i].set('fee_status', '<span class="glyphicon glyphicon-remove"></span>');
                            alert("$remote_read_072");
                        }
                    }
                });
            });
        }
    }
    function ManualEntry() {
        var rowIndexList = gridFail.getSelectionModel().getSelection();
        if (rowIndexList.length > 1) {
            alert("$read_list_048");
            return false;
        }
        if (rowIndexList.length < 1) {
            alert("$read_list_047");
            return false;
        } else {
            rowIndexList[0].set('this_read', '<input id="inputId" type="text">');
            rowIndexList[0].commit();
            $("#inputId").focus();
            $('#inputId').blur(function () {
                if (confirm('$read_list_050')) {
                    var this_read = $(this).val().trim();
                    this_read = Number(this_read);
                    if (!(this_read > 0)) {
                        alert("$read_list_049");
                        return false;
                    }
                    rowIndexList[0].set('this_read', this_read);
                    var meter_no = rowIndexList[0].data.meter_no;
                    var operator_account = getCookie("account_name");
                    $.ajax({
                        url: "../user/inputByHand.do",
                        type: "post",
                        data: {
                            "meter_no": meter_no,
                            "this_read": this_read,
                            "operator_account": operator_account
                        },
                        dataType: "json",
                        success: function (result) {
                            if (result.status == 1) {
                                findReadInfoFail();
                            }
                        },
                        error: function () {
                            alert("error");
                        }
                    });
                } else {
                    rowIndexList[0].set("this_read", this_read);
                }
            });

        }

    }

    $(function () {
        $("#againRecord").click(againRecord);
        $("#manualEntry").click(ManualEntry);
        load_communitiesFail();
        findFeeTypes();
        //findReadInfoFail();
        $("#readFail").click(findReadInfoFail);
        $("#findReadInfoFail").click(findReadInfoFailBefore);
        $(".Fail1").click(nextPageFail);
        $(".Fail2").click(prePageFail);
        $(".Fail3").click(firstPageFail);
        $(".Fail4").click(lastPageFail);
        $(".input-pageFail").keypress(function (event) {
            var keycode = (event.keyCode ? event.keyCode : event.which);
            if (keycode == 13) {
                var pageIndex = $(this).val().trim();
                pageIndex = parseInt(pageIndex);
                var totalNo = parseInt($("#totalNoFail").text().trim());
                var pageSize = $(".pageSizeFail option:selected").val().trim();
                var pageNo = parseInt(totalNo / pageSize);
                var pageMax = totalNo % pageSize == 0 ? pageNo : pageNo + 1;
                if (pageIndex <= 0) {
                    alert("$public_038");
                } else if (pageIndex > pageMax) {
                    alert("$public_039");
                } else {
                    page_indexFail = pageIndex;
                    $(".input-pageFail")[0].value = pageIndex;
                    //$(".input-pageFail")[1].value = pageIndex;
                    if (flagFail == 1) {
                        findReadInfoFail();
                    } else if (flagFail == 0) {
                        findReadInfoByMonthFail();
                    } else {
                        alert('error');
                    }
                }
                return false;
            }
        });


    });
});
