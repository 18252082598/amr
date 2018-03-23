//语言设置
function save() {
    var mark = $('#language_mark').text();
    mark = mark == "控制台_Joymeter远程抄表管理系统" ? true : false;
    var language = $('#text').text();
    if (mark == true) {
        $.ajax({
            url: "user/saveLanguage.do",
            type: "post",
            data: {"language": language},
            dataType: "json",
            success: function (result) {
                switch (language) {
                    case "Chinese":
                        window.location.href = "cn/to-do.html";
                        break;
                    case "English":
                        window.location.href = "en/to-do.html";
                        break;
                }
            },
            error: function () {
                alert("error");
            }
        });
    } else {
        $.ajax({
            url: "../user/saveLanguage.do",
            type: "post",
            data: {"language": language},
            dataType: "json",
            success: function (result) {
                if (result.status == 1) {
                    switch (language) {
                        case "Chinese":                     
                            window.location.href = "../cn/to-do.html";
                            break;
                        case "English":
                            window.location.href = "../en/to-do.html";
                            break;
                    }
                }
            },
            error: function () {
                alert("error");
            }
        });
    }
}

$(function () {
    //语言设置
    $('.dropdown-menu').on('click', function (e) {
        var $target = $(e.target);
        $target.is('li a') && $('#text').text($target.text());
        save();
    });  
});