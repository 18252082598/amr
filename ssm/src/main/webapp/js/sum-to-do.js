/**
 * Created by lenovo on 2015/10/30.
 */

function load_Sumtodo() {
    // 2015.10.30 待处理数量如果=0，则背景色为灰色，否则为橙色
    var sum = $("#sum-to-do").text();
    if (sum == "(0)") {
        $("#to-do").css("background-color", "#c9c9c9");
    }
}

// 点击表格某一行，该行背景高亮，其他行不高亮；
$().ready(function () {
    //$("tbody>tr:first").addClass("trselected");
    $("table>tr").on("click", "tr", function () {
        $(this).addClass("trselected").siblings().removeClass("trselected");
    });
})

