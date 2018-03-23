/* var loadPage = function(){
 load_suppliers();
 };    */

$(function () {
    $("#admin").text(account_name);
    $("#sum-to-do").text("(" + toDoNo + ")");
    $("#exit").click(logout);
    load_Sumtodo();
    locationManagePermission();
    load_suppliers();
    $(".span1").click(nextPage);
    $(".span2").click(prePage);
    $(".span3").click(firstPage);
    $(".span4").click(lastPage);
    $(".input-page").keypress(function (event) {
        var keycode = (event.keyCode ? event.keyCode : event.which);
        if (keycode == 13) {
            var pageIndex = $(this).val().trim();
            pageIndex = parseInt(pageIndex);
            var totalNo = parseInt($("#t1").data("suppliers.length"));
            var pageSize = parseInt($("#pageSize option:selected").val().trim());
            var pageNo = parseInt(totalNo / pageSize);
            var pageMax = totalNo % pageSize == 0 ? pageNo : pageNo + 1;
            if (pageIndex <= 0) {
                alert("$public_038");
            } else if (pageIndex > pageMax) {
                alert("$$public_039");
            } else {
                $(".pageIndex")[0].value = pageIndex;
                $(".pageIndex")[1].value = pageIndex;
                load_suppliers();
            }
            return false;
        }
    });
    $("#t1").on("click", ".glyphicon.glyphicon-pencil.list-button", modify);
    $("#t1").on("click", ".glyphicon.glyphicon-trash.list-button", del);
    $("#add_clear").click(clear);
    $("#add_supplier").click(add_supplier);
});

function load_suppliers() {
    $("#t1 tbody tr").remove();
    var pageNo = $("#pageIndex").val().trim();
    pageNo = pageNo == "" ? 1 : pageNo;
    var pageSize = parseInt($("#pageSize option:selected").val().trim());
    var startNo = (pageNo - 1) * pageSize;
    jQuery.ajax({
        url: "../user/loadSuppliers.do",
        type: "post",
        data: {
            "province": "",
            "city": "",
            "district": "",
            "startNo": startNo,
            "pageSize": pageSize
        },
        dataType: "json",
        success: function (result) {
            if (result.status == 1) {
                var map = result.data;
                var suppliers = map["suppliers"];
                var totalNo = map["totalNo"];
                $("#t1").data("totalNo", totalNo);
                var page = parseInt(totalNo / pageSize);
                var pageMax = totalNo % pageSize == 0 ? page : page + 1;
                $("#span4").next().text(pageMax);
                for (var i = 0; i < suppliers.length; i++) {
                    var supplier = suppliers[i];
                    var supplier_id = supplier.supplier_id;
                    var supplier_name = supplier.supplier_name;
                    var province = supplier.province;
                    var city = supplier.city;
                    var district = supplier.district;
                    var address = supplier.supplier_address;
                    var supplier_address = province + city + district + address;
                    var add_time = supplier.add_time;
                    add_time = format(add_time, 'yyyy.MM.dd HH:mm:ss');
                    var operator_name = supplier.operator_name;
                    var str = "";
                    str += '<tr><td>' + (i + 1 + startNo) + '</td>';
                    str += '<td>' + supplier_name + '</td>';
                    str += '<td>' + supplier_address + '</td>';
                    str += '<td>' + add_time + '</td>';
                    str += '<td>' + operator_name + '</td>';
                    str += '<td><a href="#"><span class="glyphicon glyphicon-pencil list-button" ></span></a><a href="#"><span class="glyphicon glyphicon-trash list-button" ></span></a></td></tr>';
                    $("#t1 tbody").append($(str));
                    $("#t1").data("supplier_id" + (i + 1 + startNo), supplier_id);
                    $("#t1").data("supplier_name" + (i + 1 + startNo), supplier_name);
                    $("#t1").data("province" + (i + 1 + startNo), province);
                    $("#t1").data("city" + (i + 1 + startNo), city);
                    $("#t1").data("district" + (i + 1 + startNo), district);
                    $("#t1").data("address" + (i + 1 + startNo), address);
                }
            }
        },
        error: function () {
            alert("error");
        }
    });
}
// 显示下一页
function nextPage() {
    var pageIndex = $("#pageIndex").val().trim();
    pageIndex = parseInt(pageIndex) + 1;
    var totalNo = parseInt($("#t1").data("totalNo"));
    var pageSize = $("#pageSize option:selected").val().trim();
    var pageNo = parseInt(totalNo / pageSize);
    var pageMax = totalNo % pageSize == 0 ? pageNo : pageNo + 1;
    if (pageIndex <= 0) {
        alert("$public_038");
    } else if (pageIndex > pageMax) {
        alert("$public_039");
    } else {
        $(".pageIndex")[0].value = pageIndex;
        $(".pageIndex")[1].value = pageIndex;
        load_suppliers();
    }
}
// 显示上一页
function prePage() {
    var pageIndex = $("#pageIndex").val().trim();
    pageIndex = parseInt(pageIndex) - 1;
    if (pageIndex <= 0) {
        alert("$public_040");
    } else {
        $(".pageIndex")[0].value = pageIndex;
        $(".pageIndex")[1].value = pageIndex;
        load_suppliers();
    }
}
// 显示第一页
function firstPage() {
    $(".pageIndex")[0].value = 1;
    $(".pageIndex")[1].value = 1;
    load_suppliers();
}

// 显示最后一页
function lastPage() {
    var totalNo = parseInt($("#t1").data("totalNo"));
    var pageSize = $("#pageSize option:selected").val().trim();
    // 根据记录总数，计算出最后一页的页数
    var pageNo = parseInt(totalNo / pageSize);
    var pageEnd = totalNo % pageSize == 0 ? pageNo : pageNo + 1;
    $(".pageIndex")[0].value = pageEnd;
    $(".pageIndex")[1].value = pageEnd;
    load_suppliers();
}

//修改网点信息
function modify() {
    var supplier_index = $(this).parents("tr").find("td:eq(0)").text().trim();
    $("#t1").data("supplier_index", supplier_index);
    // 根据当前序号，拿到当前网点的id
    var supplier_id = $("#t1").data("supplier_id" + supplier_index);
    $("#t1").data("supplier_id", supplier_id);
    var supplier_name = $(this).parents("tr").find("td:eq(1)").text();
    var province = $("#t1").data("province" + supplier_index);
    var city = $("#t1").data("city" + supplier_index);
    var district = $("#t1").data("district" + supplier_index);
    var address = $("#t1").data("address" + supplier_index);
    addressInit('Select1', 'Select2', 'Select3', '' + province + '', '' + city + '', '' + district + '');
    $("#supplier_name")[0].value = supplier_name;
    $("#d1 select:eq(0)").find("option[value='" + province + "']").attr("selected", true);
    $("#d1 select:eq(1)").find("option[value='" + city + "']").attr("selected", true);
    $("#d1 select:eq(2)").find("option[value='" + district + "']").attr("selected", true);
    $("#address")[0].value = address;
}

function del() {
    var supplier_index = $(this).parents("tr").find("td:eq(0)").text().trim();
    var supplier_id = $("#t1").data("supplier_id" + supplier_index);
    var supplier_name = $("#t1").data("supplier_name" + supplier_index);
    if (confirm("$public_056")) {
        jQuery.ajax({
            url: "../user/deleteSupplier.do",
            type: "post",
            data: {
                "supplier_id": supplier_id,
                "supplier_name": supplier_name
            },
            dataType: "json",
            success: function (result) {
                if (result.status == 1) {
                    load_suppliers();
                }
            },
            error: function () {
                alert("error");
            }
        });
    }
}

function clear() {
    document.getElementById("supplier_name").value = "";
    document.getElementById("address").value = "";
}

function add_supplier() {
    $('#location-add  form :input.required').trigger('blur');
    var numError = $('form  .onError').length;
    if (numError) {
        return;
    }
    var supplier_name = $("#supplier_name").val().trim();
    var province = $("#d1 select:eq(0) option:selected").val().trim();
    var city = $("#d1 select:eq(1) option:selected").val().trim();
    var district = $("#d1 select:eq(2) option:selected").val().trim();
    var address = $("#address").val().trim();
    var operator_name = getCookie("account_name");
    if (supplier_name == "" || district == "" || address == "") {
        alert("$location_manage_034");
    } else if ($("#myModalLabel").text().trim() == "$location_manage_006") {
        jQuery.ajax({
            url: "../user/addSupplier.do",
            type: "post",
            data: {
                "supplier_name": supplier_name,
                "province": province,
                "city": city,
                "district": district,
                "supplier_address": address,
                "operator_name": operator_name,
            },
            dataType: "json",
            success: function (result) {
                if (result.status == 1) {
                    $("#cancel").click();
                    load_suppliers();
                }
            },
            error: function () {
                alert("error");
            }
        });
    } else if ($("#myModalLabel").text().trim() == "$location_manage_026") {
        var supplier_index = $("#t1").data("supplier_index");
        var supplier_id = $("#t1").data("supplier_id" + supplier_index);
        var original_supplier_name = $("#t1").data("supplier_name" + supplier_index);
        var original_province = $("#t1").data("province" + supplier_index);
        var original_city = $("#t1").data("city" + supplier_index);
        var original_district = $("#t1").data("district" + supplier_index);
        var original_supplier_address = $("#t1").data("address" + supplier_index);
        jQuery.ajax({
            url: "../user/saveSupplier.do",
            type: "post",
            data: {
                "original_supplier_name": original_supplier_name,
                "original_province": original_province,
                "original_city": original_city,
                "original_district": original_district,
                "original_supplier_address": original_supplier_address,
                "supplier_id": supplier_id,
                "supplier_name": supplier_name,
                "province": province,
                "city": city,
                "district": district,
                "supplier_address": address,
                "operator_name": operator_name
            },
            dataType: "json",
            success: function (result) {
                if (result.status == 1) {
                    $("#cancel").click();
                    load_suppliers();
                }
            },
            error: function () {
                alert("error");
            }
        });
    }
}

