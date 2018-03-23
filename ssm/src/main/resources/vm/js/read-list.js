// 加载所有抄表信息
Ext.onReady(function () {
    var month = "";
    var meter_type = "";
    var flag = 1;
    var page_index = 1;
    var page_size = 20;
    var columns = [//列
        new Ext.grid.RowNumberer({
            header: "$public_015", //序号
            width: 30,
            align: "center",
            renderer: function (value, metadata, record, rowIndex) {
                var start_no = (page_index - 1) * page_size;
                return start_no + 1 + rowIndex;
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
        {header: '$read_list_018', dataIndex: 'this_read', renderer: formatFloat, minWidth: 60},
        {header: '$read_list_019', dataIndex: 'last_read', renderer: formatFloat, minWidth: 60},
        {header: '$read_list_020', dataIndex: 'this_cost', renderer: formatFloat, minWidth: 60},
        {header: '$read_list_021', dataIndex: 'last_cost', renderer: formatFloat, minWidth: 60},
        {header: '$read_list_067', dataIndex: 'data8', renderer: formatFloat, minWidth: 60},
        {header: '$read_list_055', dataIndex: 'data4', renderer: formatFloat, minWidth: 40, hidden: true},
        {header: '$read_list_054', dataIndex: 'data5', renderer: formatFloat, minWidth: 60, hidden: true},
        {header: '$read_list_056', dataIndex: 'data6', renderer: formatFloat, minWidth: 80, hidden: true},
        {header: '$read_list_057', dataIndex: 'data7', renderer: formatFloat, minWidth: 80, hidden: true},
        {header: '$read_list_022', dataIndex: 'fee_need', renderer: formatFloat, maxWidth: 70},
        {header: '$read_list_023', dataIndex: 'balance', renderer: formatFloat, maxWidth: 70},
        {header: '$read_list_024', dataIndex: 'exception', renderer: exception, maxWidth: 60},
        {header: '$read_list_025', dataIndex: 'fee_status', renderer: fee_status, maxWidth: 60},
        {header: '$read_list_026', dataIndex: 'read_type', renderer: read_type, maxWidth: 60},
        {header: '$read_list_027', dataIndex: 'operator_account', maxWidth: 100},
        {header: '$read_list_028', dataIndex: 'operate_time', width: 150,
            renderer: function (value) {
                return Ext.Date.format(new Date(value), 'Y-m-d H:i:s');
            }}
    ];
    var store = new Ext.data.Store({
        reader: {root: 'data.readInfo'},
        fields: ['operate_id', 'user_name', 'user_address_community', 'user_address_building', 'user_address_unit', 'user_address_room', 'contact_info', 'concentrator_name',
            'meter_no', 'fee_type', 'this_read', 'last_read', 'this_cost', 'last_cost', 'data8', 'data4', 'data5', 'data6', 'data7', 'fee_need', 'balance', 'exception', 'fee_status', 'read_type',
            'operator_account', 'operate_time']
    });

    var mask = $("<div class='window_mask'></div>");

    function  exception(value) {
        if (value == "1") {
            return  "<span class='glyphicon  glyphicon-ok'   title='$read_list_035'></span>";
        } else {
            return  "<span  class='glyphicon  glyphicon-remove' title='$read_list_036'></span>";
        }
    }

    function  fee_status(value) {
        if (value == "1") {
            return  "<span class='glyphicon  glyphicon-ok' title='$read_list_037' ></span>";
        } else if (value == "0") {
            return  "<span  class='glyphicon  glyphicon-remove' title='$read_list_038'></span>";
        } else {
            return  "<span  class='glyphicon  glyphicon-record' title='$read_list_039'></span>";
        }
    }

    function  read_type(value) {
        if (value == "1") {
            value = "$read_list_058";
        } else {
            value = "$read_list_059";
        }
        return value;
    }

    function  formatFloat(value) {
        if (value !== null) {
            value = value === parseInt(value) ? value : value.toFixed(5);
        } else {
            value = 0;
        }
        return value;
    }
    var sm = new Ext.selection.CheckboxModel();
    var tb = new Ext.Toolbar({
        style: 'height:30px;padding:3px 15px 3px 10px; width:auto',
        autoWidth: true,
        items: [
            {text: '$read_list_004',
                handler: function () {
                    month = "all";
                    findReadInfoByMonth();
                    $('#query_month').text('$read_list_004');
                }
            },
            {
                text: '$read_list_005',
                handler: function () {
                    month = "1";
                    findReadInfoByMonth();
                    $('#query_month').text('$read_list_005');
                }
            },
            {
                text: '$read_list_006',
                handler: function () {
                    month = "2";
                    findReadInfoByMonth();
                    $('#query_month').text('$read_list_006');
                }
            },
            {
                text: '$read_list_007',
                handler: function () {
                    month = "3";
                    findReadInfoByMonth();
                    $('#query_month').text('$read_list_007');
                }
            },
            {
                text: '$read_list_008',
                handler: function () {
                    month = "4";
                    findReadInfoByMonth();
                    $('#query_month').text('$read_list_008');
                }
            },
            {text: '$read_list_009',
                handler: function () {
                    month = "5";
                    findReadInfoByMonth();
                    $('#query_month').text('$read_list_009');
                }
            },
            {text: '$read_list_010',
                handler: function () {
                    month = "6";
                    findReadInfoByMonth();
                    $('#query_month').text('$read_list_010');
                }
            },
            {text: '$read_list_011',
                handler: function () {
                    month = "7";
                    findReadInfoByMonth();
                    $('#query_month').text('$read_list_011');
                }
            },
            {text: '$read_list_012',
                handler: function () {
                    month = "8";
                    findReadInfoByMonth();
                    $('#query_month').text('$read_list_012');
                }
            },
            {text: '$read_list_013',
                handler: function () {
                    month = "9";
                    findReadInfoByMonth();
                    $('#query_month').text('$read_list_013');
                }
            },
            {text: '$read_list_014',
                handler: function () {
                    month = "10";
                    findReadInfoByMonth();
                    $('#query_month').text('$read_list_014');
                }
            },
            {text: '$read_list_015',
                handler: function () {
                    month = "11";
                    findReadInfoByMonth();
                    $('#query_month').text('$read_list_015');
                }
            },
            {text: '$read_list_016',
                handler: function () {
                    month = "12";
                    findReadInfoByMonth();
                    $('#query_month').text('$read_list_016');
                }
            }
        ]
    });

    tb.add('');
    tb.add('-', {text: '$read_list_042', handler: function () {
            meter_type = "10";
            if (flag === 1) {
                findReadInfo();
            } else if (flag === 0) {
                findReadInfoByMonth();
            }
            grid.columns[15].setVisible(false);
            grid.columns[16].setVisible(false);
            grid.columns[17].setVisible(false);
            grid.columns[18].setVisible(false);
            $('#query_meter').text('$read_list_042');
        }}, {text: '$read_list_043', handler: function () {
            meter_type = "20";
            if (flag === 1) {
                findReadInfo();
            } else if (flag === 0) {
                findReadInfoByMonth();
            }
            grid.columns[15].setVisible(true);
            grid.columns[16].setVisible(true);
            grid.columns[17].setVisible(true);
            grid.columns[18].setVisible(true);
            $('#query_meter').text('$read_list_043');
        }}, {text: '$read_list_052', handler: function () {
            meter_type = "30";
            if (flag === 1) {
                findReadInfo();
            } else if (flag === 0) {
                findReadInfoByMonth();
            }
            grid.columns[15].setVisible(false);
            grid.columns[16].setVisible(false);
            grid.columns[17].setVisible(false);
            grid.columns[18].setVisible(false);
            $('#query_meter').text('$read_list_052');
        }}, {text: '$remote_read_047_2', handler: function () {
            meter_type = "40";
            if (flag === 1) {
                findReadInfo();
            } else if (flag === 0) {
                findReadInfoByMonth();
            }
            grid.columns[15].setVisible(false);
            grid.columns[16].setVisible(false);
            grid.columns[17].setVisible(false);
            grid.columns[18].setVisible(false);
            $('#query_meter').text('$remote_read_047_2');
        }});//水表 气表 热表 电表
    tb.add("->");
    tb.add('<span>$public_006</span>');
    tb.add("<select id='pageSize' class='pageSize' ><option value='5'>5</option> <option value='10'>10</option> <option value='15'>15</option> <option value='20' selected='selected'>20</option> <option value='30'>30</option> <option value='50'>50</option> </select>");
    tb.add('<a  href="javascript:void(0)" class="glyphicon glyphicon-step-backward  span3"></a>');
    tb.add('<a  href="javascript:void(0)" class="triangle-left  span2"></a>');
    tb.add('$public_008<input class="input-page" type="text" name="" value="1" id="pageIndex">$public_009');
    tb.add('<a  href="javascript:void(0)" class="triangle-right  span1"></a>');
    tb.add('<a  href="javascript:void(0)" class="glyphicon glyphicon-step-forward  span4"></a>');
    tb.add('<span>$public_010<span><b  class="pag"  style=""></b></span>$public_009');
    tb.add('');
    tb.add('');

    //导出按钮组件
    var exportButton = Ext.create('Ext.button.Split', {
        renderTo: Ext.getBody(),
        text: '$read_list_029',
        pressed: true,
        menu: new Ext.menu.Menu({
            items: [
                {
                    text: 'Excel',
                    handler: function () {
                        exportReadInfo('EXCEL');
                    }
                },
                {
                    text: 'Dbf',
                    handler: function () {
                        exportReadInfo('DBF');
                    }
                }
            ]
        })
    });

    var tb2 = new Ext.Toolbar({
        style: 'height:30px;padding:3px 10px; width:100%)',
        autoWidth: true,
        items: [exportButton]
    });
    tb2.add('');
    tb2.add('');
    tb2.add('<span>$user_manage_03701<span class="totalNo" id="totalNo"></span>$user_manage_03702</span>');
    tb2.add('');
    tb2.add('');
    tb2.add('-');
    tb2.add('<span>$read_list_060<span class="totalNo"  id="query_month">$read_list_004</span></span>');
    tb2.add('');
    tb2.add('<span>$read_list_061<span class="totalNo"  id="query_meter">$read_list_004</span></span>');

    var grid = new Ext.grid.GridPanel({
        height: 400,
        renderTo: 'grid',
        layout: 'fit',
        store: store,
        autoWidth: true,
        loadMask: true,
        autoScroll: true,
        bodyStyle: 'overflow-x:hidden;overflow-y:auto;',
        columns: columns,
        selType: 'cellmodel',
        selModel: sm,
        tbar: tb,
        bbar: tb2
    });
    Ext.EventManager.onWindowResize(function () {
        grid.getView().refresh();
    });
    grid.setAutoScroll(true);

    function findReadInfoBefore() {
        page_index = 1;
        $(".input-page").val(1);
        findReadInfo();
    }
    //加载费率类型选项
    function findFeeTypes() {
        $("#feeTypeName").empty();
        $("#feeTypeName").append($("<option value=''>$user_manage_06701</option>"));
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
                        $("#feeTypeName").append($(str));
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
    function findReadInfo() {
        var meter_no = $("#meter_no").val().trim();
        var feeTypeName = $("#feeTypeName option:selected").val().trim();
        if (isNaN(meter_no)) {
            alert("$read_list_053");
            return;
        }
        var pageNo = $(".input-page").val().trim();
        var pageSize = $(".pageSize option:selected").val().trim();
        var user_name = $("#user_name").val().trim();
        //从浏览器URL中解析小区名
        var community_name = getQueryString("community_name");
        //如果浏览器中解析到小区名，就设置该小区为选中状态
        if (community_name != "") {
            $("#user_address_community option[value='" + community_name + "']").attr("selected", true);
            findAllBuildings();
        }
        //从搜索条件中获取小区名
        var community = $("#user_address_community option:selected").val().trim();
        var building = $("#user_address_building option:selected").val().trim();
        var unit = $("#user_address_unit option:selected").val().trim();
        var room = $("#user_address_room option:selected").val().trim();
        //var operator_name = $("#operator_name option:selected").text().trim();
        var startTime = $("#startTime").val().trim();
        var endTime = $("#endTime").val().trim();
        var startNo = (pageNo - 1) * pageSize;
        var queryTableType = "queryRecord";
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
                    var readInfo = map["readInfo"];
                    var totalNo = map["totalNo"];
                    $("#totalNo").text(totalNo);
                    var page = parseInt(totalNo / pageSize);
                    var pageMax = totalNo % pageSize == 0 ? page : page + 1;
                    //$(".glyphicon.glyphicon-step-forward.span4").next().text(pageMax);
                    $("#grid  .pag").text(pageMax);
                    store.loadData(readInfo);
                    flag = 1;
                }
            },
            error: function () {
                alert("error");
            }
        });
    }



    // 按月份查询抄表信息
    function findReadInfoByMonth() {
        var pageNo = $(".input-page").val().trim();
        var pageSize = $(".pageSize option:selected").val().trim();
        var startNo = (pageNo - 1) * pageSize;
        var queryTableType = "queryRecord";
        jQuery.ajax({
            url: "../user/findReadInfoByMonth.do",
            type: "post",
            data: {
                "month": month,
                "meter_type": meter_type,
                "startNo": startNo,
                "pageSize": pageSize,
                "queryTableType": queryTableType
            },
            dataType: "json",
            success: function (result) {
                if (result.status == 1) {
                    var map = result.data;
                    var readInfo = map["readInfo"];
                    var totalNo = map["totalNo"];
                    $("#totalNo").text(totalNo);
                    var page = parseInt(totalNo / pageSize);
                    var pageMax = totalNo % pageSize == 0 ? page : page + 1;
                    //$(".glyphicon.glyphicon-step-forward.span4").next().text(pageMax);
                    $("#grid  .pag").text(pageMax);
                    store.loadData(readInfo);
                    flag = 0;
                    //	month=null;
                }
            },
            error: function () {
                alert("error");
            }
        });
    }


    $("#pageSize").change(function () {
        //$(".pageSize").find("option[value=" + pageSize + "]").attr("selected", true);
        if (flag == 1) {
            findReadInfo();
        } else if (flag == 0) {
            findReadInfoByMonth();
        } else {
            alert('$public_036');
        }
    });


    // 下一页
    function nextPage() {
        var pageIndex = $(".input-page").val().trim();
        pageIndex = parseInt(pageIndex) + 1;
        var totalNo = parseInt($("#totalNo").text().trim());
        var pageSize = $(".pageSize option:selected").val().trim();
        page_size = pageSize;
        var pageNo = parseInt(totalNo / pageSize);
        var pageMax = totalNo % pageSize == 0 ? pageNo : pageNo + 1;
        if (pageIndex <= 0) {
            alert("$public_038");
        } else if (pageIndex > pageMax) {
            alert("$public_039");
        } else {
            $(".input-page")[0].value = pageIndex;
            //$(".input-page")[1].value = pageIndex;
            page_index = pageIndex;
            if (flag == 1) {
                findReadInfo();
            } else if (flag == 0) {
                findReadInfoByMonth();
            } else {
                alert('$public_036');
            }
        }
    }



    // 上一页
    function prePage() {
        var pageIndex = $(".input-page").val().trim();
        pageIndex = parseInt(pageIndex) - 1;
        if (pageIndex <= 0) {
            alert("$public_040");
        } else {
            $(".input-page")[0].value = pageIndex;
            //$(".input-page")[1].value = pageIndex;
            page_index = pageIndex;
            if (flag == 1) {
                findReadInfo();
            } else if (flag == 0) {
                findReadInfoByMonth();
            } else {
                alert('$public_036');
            }
        }
    }

    // 第一页
    function firstPage() {
        $(".input-page")[0].value = 1;
        //$(".input-page")[1].value = 1;
        page_index = 1;
        if (flag == 1) {
            findReadInfo();
        } else if (flag == 0) {
            findReadInfoByMonth();
        } else {
            alert('$public_036');
        }
    }



    // 最后一页
    function lastPage() {
        var totalNo = parseInt($("#totalNo").text().trim());
        var pageSize = $(".pageSize option:selected").val().trim();
        // 根据记录总数，计算出最后一页的页数
        var pageNo = parseInt(totalNo / pageSize);
        var pageEnd = totalNo % pageSize == 0 ? pageNo : pageNo + 1;
        $(".input-page")[0].value = pageEnd;
        //$(".input-page")[1].value = pageEnd;
        page_index = pageEnd;
        if (flag == 1) {
            findReadInfo();
        } else if (flag == 0) {
            findReadInfoByMonth();
        } else {
            alert('$public_036');
        }
    }


    /**
     * 导出抄表记录
     * @returns {undefined}
     */
    function exportReadInfo(fileType) {
        $("body").css("position", "relative");
        mask.appendTo("body");
        if (!window.confirm("$public_064")) {
            mask.remove();
            return;
        }
        ;
        var meter_no = $("#meter_no").val().trim();
        if (isNaN(meter_no)) {
            alert("$public_065");
            return;
        }
        var pageNo = $(".input-page").val().trim();
        pageNo = pageNo == "" ? 1 : pageNo;
        var user_name = $("#user_name").val().trim();
        //从浏览器URL中解析小区名
        var community_name = getQueryString("community_name");
        //如果浏览器中解析到小区名，就设置该小区为选中状态
        if (community_name != "") {
            $("#user_address_community option[value=" + community_name + "]").attr("selected", true);
            findAllBuildings();
        }
        //从搜索条件中获取小区名
        var community = $("#user_address_community option:selected").val().trim();
        var building = $("#user_address_building option:selected").val().trim();
        var unit = $("#user_address_unit option:selected").val().trim();
        var room = $("#user_address_room option:selected").val().trim();
        var fee_type = $("#feeTypeName option:selected").val().trim();
        // var operator_name = $("#operator_name option:selected").text().trim();
        var startTime = $("#startTime").val().trim();
        var endTime = $("#endTime").val().trim();
        var language = getCookie("language");
        var totalNo = parseInt($("#totalNo").text().trim());
        var method = "findReadInfo";
        var title = "readinfo";
        if (!(startTime && endTime)) {
            alert("$read_list_065");
            mask.remove();
            return;
        }
        var url = "../report/exportReadInfo.do?user_name=" + user_name + "&community=" + community + "&building=" + building
                + "&unit=" + unit + "&room=" + room + "&meter_type=" + meter_type + "&operator_name=&meter_no=" + meter_no + "&startTime=" + startTime
                + "&endTime=" + endTime + "&language=" + language + "&totalNo=" + totalNo + "&fileType=" + fileType + "&method=" +
                method + "&title=" + title + "&fee_type=" + fee_type;
        window.location.href = url;
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
        $("#admin").text(account_name);
        $("#sum-to-do").text("(" + toDoNo + ")");
        $("#exit").click(logout);
        findFeeTypes();
        readListPermission();
        load_Sumtodo();
        load_communities();
        load_operators();
        findReadInfo();
        $("#findReadInfo").click(findReadInfoBefore);
        $("#readRecord").click(findReadInfo);
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
                    page_index = pageIndex;
                    $(".input-page")[0].value = pageIndex;
                    //$(".input-page")[1].value = pageIndex;
                    if (flag == 1) {
                        findReadInfo();
                    } else if (flag == 0) {
                        findReadInfoByMonth();
                    } else {
                        alert('error');
                    }
                }
                return false;
            }
        });


    });
});
