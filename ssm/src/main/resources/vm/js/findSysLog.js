function findSysLog() {
    $("#t2 tbody tr").remove();
    var pageLogNo = $("#pageLogIndex").val().trim();
    pageLogNo == "" ? 1 : pageLogNo;
    var pageLogSize = parseInt($(".pageLogSize option:selected").val());
    jQuery.ajax({
        url: "../admin/findSysLog.do",
        type: "post",
        dataType: "json",
        success: function (result) {
            if (result.status == 1) {
                var sysLogs = result.data;
                var pageLog = parseInt(sysLogs.length / pageLogSize);
                var pageLogMax = sysLogs.length % pageLogSize == 0 ? pageLog : pageLog + 1;
                $(".span8").next().text(pageLogMax);
                $("#t2").data("sysLogs.length", sysLogs.length);
                for (var i = (pageLogNo - 1) * pageLogSize; i < pageLogNo * pageLogSize && i < sysLogs.length; i++) {
                    var sysLog = sysLogs[i];
                    var loginName = sysLog.loginName;
                    var operatingcontent = sysLog.operatingcontent;
                    var operateDate = sysLog.operateDate;
                    var str = "";
                    str += '<tr><td>' + operateDate + '</td>';
                    str += '<td>' + loginName + '</td>';
                    str += '<td>' + operatingcontent + '</td></tr>';
                    $("#t2 tbody").append($(str));
                }
                ;
            }
            ;
        },
        error: function () {
            alert("error");
        }
    });
}
function getAdminAccount() {
    var admin_account = $(this).find("td:eq(1)").text().trim();
    $("#t2").data("admin_account", admin_account);
    if (admin_account == "") {
    }
}
;

function loadAdminManageInfoFirst() {
    var admin_account = $("#t2").data("admin_account");
    $(".input-page")[2].value = 1;
    $("#t2 tbody tr").remove();
    $("#operator").remove();
    var pageLogNo = $("#pageLogIndex").val().trim();
    pageLogNo == "" ? 1 : pageLogNo;
    var pageLogSize = parseInt($(".pageLogSize option:selected").val());   
    jQuery.ajax({
        url: "../admin/findAdminManageInfo.do",
        type: "post",
        data: {"admin_account": admin_account},
        dataType: "json",
        success: function (result) {
            if (result.status == 1) {
                var adminManageInfo = result.data;
                var pageLog = parseInt(adminManageInfo.length / pageLogSize);
                var pageLogMax = adminManageInfo.length % pageLogSize == 0 ? pageLog : pageLog + 1;
                $(".span8").next().text(pageLogMax);
                $("#t2").data("adminManageInfo.length", adminManageInfo.length);

                for (var i = (pageLogNo - 1) * pageLogSize; i < pageLogNo * pageLogSize && i < adminManageInfo.length; i++) {
                    var info = adminManageInfo[i];
                    var oparate_time = info.operateDate;
                    var str = "";
                    str += "<tr><td>" + oparate_time + "</td>";
                    str += "<td>" + info.operatingcontent + "</td></tr>";
                    $("#t2 tbody").append(str);
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

function loadAdminManageInfo() {
    var admin_account = $("#t2").data("admin_account");
    if (admin_account != null) {
        $("#t2 tbody tr").remove();
        $("#operator").remove();
        var pageLogNo = $("#pageLogIndex").val().trim();
        pageLogNo == "" ? 1 : pageLogNo;
        var pageLogSize = parseInt($(".pageLogSize option:selected").val());
        jQuery.ajax({
            url: "../admin/findAdminManageInfo.do",
            type: "post",
            data: {"admin_account": admin_account},
            dataType: "json",
            success: function (result) {
                if (result.status == 1) {
                    var adminManageInfo = result.data;
                    var pageLog = parseInt(adminManageInfo.length / pageLogSize);
                    var pageLogMax = adminManageInfo.length % pageLogSize == 0 ? pageLog : pageLog + 1;
                    $(".span8").next().text(pageLogMax);
                    $("#t2").data("adminManageInfo.length", adminManageInfo.length);
                    for (var i = (pageLogNo - 1) * pageLogSize; i < pageLogNo * pageLogSize && i < adminManageInfo.length; i++) {
                        var info = adminManageInfo[i];
                        var oparate_time = info.operateDate;
                        var str = "";
                        str += "<tr><td>" + oparate_time + "</td>";
                        str += "<td>" + info.operatingcontent + "</td></tr>";
                        $("#t2 tbody").append(str);
                    }
                } else {
                    alert("$public_036");
                }
            },
            error: function () {
                alert("error");
            }
        });
    } else {
        findSysLog();
    }
}

function preLogPage() {
    var pageLogIndex = $("#pageLogIndex").val().trim();
    pageLogIndex = parseInt(pageLogIndex) - 1;
    if (pageLogIndex <= 0) {
        alert("$public_039");
    } else {
        $(".input-page")[2].value = pageLogIndex;
        loadAdminManageInfo();
    }
}

function firstLogPage() {
    $(".input-page")[2].value = 1;
    loadAdminManageInfo();
}

function lastLogPage() {
    var totalLogNo = parseInt($("#t2").data("adminManageInfo.length"));
    var pageLogSize = parseInt($(".pageLogSize option:selected").val().trim());
    var pageLogNo = parseInt(totalLogNo / pageLogSize);
    var pageLogEnd = totalLogNo % pageLogSize == 0 ? pageLogNo : pageLogNo + 1;
    $(".input-page")[2].value = pageLogEnd;
    loadAdminManageInfo();
}

function nextLogPage() {
    var pageLogIndex = parseInt($("#pageLogIndex").val().trim());
    nextPageLogIndex = pageLogIndex + 1;
    var totalLogNo = parseInt($("#t2").data("adminManageInfo.length"));
    var pageLogSize = parseInt($(".pageLogSize option:selected").text());
    var pageLogNo = parseInt(totalLogNo / pageLogSize);
    var pageLogMax = totalLogNo % pageLogSize == 0 ? pageLogNo : pageLogNo + 1;
    if (pageLogIndex <= 0) {
        alert("$public_038");
    } else if
            (nextPageLogIndex > pageLogMax) {
        alert("$public_039");
    } else {
        $(".input-page")[2].value = nextPageLogIndex;
        loadAdminManageInfo();
    }
}