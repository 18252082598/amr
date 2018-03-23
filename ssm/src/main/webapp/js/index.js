
/*
 * index href
 */
function indexHref(language){
     $.ajax({
            url: "user/saveLanguage.do",
            type: "post",
            data: {"language": language},
            dataType: "json",
            success: function (result) {
                switch (language) {
                    case "Chinese":
                        window.location.href = "cn/login.html";
                        break;
                    case "English":
                        window.location.href = "en/login.html";
                        break;
                }
            },
            error: function () {
                alert("error");
            }
        });  
}

$(function () {

    $("#cnHref").on('click',function(){
       indexHref("Chinese");
    } );
    $("#enHref").on('click',function(){
        indexHref("English") ;
});
});