// 需要跳转的页数
function getPageIndex() {
    var pageIndex = $(".input-page").val().trim();
    return parseInt(pageIndex);
}

// 每页显示的数量
function getPageSize() {
    var pageSize = $(".pageSize option:selected").val().trim();
    return parseInt(pageSize);
}

// 数据的总条数
function getTotalNo() {
    var totalNo = $("#t1").data("totalNo");
    return parseInt(totalNo);
}

// 最大页数
function getPageMax() {
    var totalNo = getTotalNo();
    var pageSize = getPageSize();
    var pageNo = parseInt(totalNo / pageSize);
    var pageMax = totalNo % pageSize === 0 ? pageNo : pageNo + 1;
    return pageMax;
}

// 更改每页显示的数量
function change(pageSize) {
    $(".pageSize").find("option[value=" + pageSize + "]").attr("selected", true);
    loadPage();
}

//显示下一页
function nextPage() {
    var pageMax = getPageMax();
    var pageIndex = getPageIndex() + 1;
    if (pageIndex <= 0) {
        alert("请输入正确的页数");
    } else if (pageIndex > pageMax) {
        alert("已经是最后一页啦");
    } else {
        $(".input-page")[0].value = pageIndex;
        $(".input-page")[1].value = pageIndex;
        loadPage();
    }
}

// 显示上一页
function prePage() {
    var pageIndex = getPageIndex() - 1;
    if (pageIndex <= 0) {
        alert("已经是第一页啦");
    } else {
        $(".input-page")[0].value = pageIndex;
        $(".input-page")[1].value = pageIndex;
        loadPage();
    }
}

// 显示第一页
function firstPage() {
    $(".input-page")[0].value = 1;
    $(".input-page")[1].value = 1;
    loadPage();
}

// 显示最后一页
function lastPage() {
    var pageMax = getPageMax();
    $(".input-page")[0].value = pageMax;
    $(".input-page")[1].value = pageMax;
    loadPage();
}

// 点击Enter跳转
var enterKeyAction = function (event) {
    var keycode = (event.keyCode ? event.keyCode : event.which);
    if (keycode === 13) {
        var pageIndex = parseInt($(this).val().trim());
        var pageMax = getPageMax();
        if (pageIndex <= 0) {
            alert("请输入正确的页数");
        } else if (pageIndex > pageMax) {
            alert("已经是最后一页啦");
        } else {
            $(".input-page")[0].value = pageIndex;
            $(".input-page")[1].value = pageIndex;
            loadPage();
        }
        return false;
    }
}

