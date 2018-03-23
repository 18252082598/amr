// 正则法获取URL中的参数
function getQueryString(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
    //获取url中"?"符(含)后的字符串并正则匹配
    var r = window.location.search.substr(1).match(reg);
    return r == null ? "" : decodeURI(r[2]);
}