/**
 * 用户注册验证
 * 
 */
$(document).ready(function () {
    $('#tab-register form input').blur(function () {
        var $parent = $(this).parent();
        $parent.find(".formtips").remove();
        var $tip = $('<label  class="formtips"></label>');
        value = jQuery.trim(this.value);
        //验证用户名
        if ($(this).is('#user-name2')) {
            if (!value) {
                $tip.addClass('onError').$form_validation_001;
            } else if (value.length > 50) {
                $tip.addClass('onError').$form_validation_003;
            } else {
                $tip.addClass('glyphicon  glyphicon-ok').text('').css('color', '#666');
            }
            $parent.append($tip);
        }

        if ($(this).is('#user-mobile2')) {
            if (!value) {
                $tip.addClass('onError').$form_validation_004;
            } else if (!/^[0-9-_]{1,20}$/.test(value)) {
                $tip.addClass('onError').$form_validation_005;
            } else {
                $tip.addClass('glyphicon  glyphicon-ok').text('').css('color', '#666');
            }
            $parent.append($tip);
        }
        //房间号码验证֤
        if ($(this).is('#user-room2')) {
            var unit = $('#unit  option:selected').val();
            if (!value) {
                $tip.addClass('onError').$form_validation_006;
            } else if (value != "" && unit == "") {
                $tip.addClass('onError').$form_validation_017;
            } else {
                $tip.addClass('glyphicon  glyphicon-ok').text('').css('color', '#666');
            }
            $parent.append($tip);

        }
        // 身份证号码验证֤
        if ($(this).is('#user-cardID4')) {
            if (!value) {
                $tip.addClass('onError').$form_validation_008;
            } else if (!/^[0-9A-Za-z]{1,20}$/.test(value)) {
                $tip.addClass('onError').$public_053;
            } else {
                $tip.addClass('glyphicon  glyphicon-ok').text('').css('color', '#666');
            }
            $(this).parent().append($tip);
        }
        //  阀门编号0-254之间的数字
        if ($(this).is('#valve-cardID2')) {
            if (!value) {
                $tip.addClass('onError').$form_validation_009;
            } else if (!/^[0-9A-Za-z]{1,20}$/.test(value)) {
                $tip.addClass('onError').$public_053;
            } else {
                $tip.addClass('glyphicon  glyphicon-ok').text('').css('color', '#666');
            }
            $parent.append($tip);
        }
        // 子表号(0-254)验证֤
//        if ($(this).is('#meter-subID2')) {
//            if (!value) {
//                $tip.addClass('onError').$form_validation_010;
//            } else if (!/^(0?\d{1,2}|1\d{2}|2([0-4]\d|5[0-4]))$/.test(value)) {
//                $tip.addClass('onError').$public_053;
//            } else {
//                $tip.addClass('glyphicon  glyphicon-ok').text('').css('color', '#666');
//            }
//            $parent.append($tip);
//        }
        //12位表出厂编号
        if ($(this).is('#meter-cardID2')) {
            if (!value) {
                $tip.addClass('onError').$form_validation_011;
            } else if (!/^[0-9]{8,12}$/.test(value)) {
                $tip.addClass('onError').$public_053;
            } else {
                $tip.addClass('glyphicon  glyphicon-ok').text('').css('color', '#666');
            }
            $parent.append($tip);
        }
        //面积匹配
        if ($(this).is('#house-area2')) {

            if (!value) {
                $tip.addClass('onError').$form_validation_012;
            } else if (!/^([1-9]\d*(\.\d+)?|0)$/.test(value)) {
                $tip.addClass('onError').$public_053;
            } else {
                $tip.addClass('glyphicon  glyphicon-ok').text('').css('color', '#666');
            }
            $parent.append($tip);
        }

    });
    /*******注册下拉框事件*********/
    function fun_meter() {
        var obj = $('#meter-type2').parent();
        var $tip = $('<label  class="formtips"></label>');
        obj.find('.formtips').remove();
        var select0 = $('#meter-type2  option:selected');
        var select1 = $('#meter-no2  option:selected');
        var select2 = $('#meter-protocol2  option:selected');
        if (select0.text().trim() != '$user_add_009' &&
                select1.text().trim() != '$user_add_019' &&
                select2.text().trim() != '$user_add_020') {
            $tip.addClass('glyphicon  glyphicon-ok').text('').css('color', '#666');
        } else {
            $tip.addClass('onError').$user_manage_135;
        }
        obj.append($tip);
    }
    /******注册下拉框事件******/
    $('form  select').change(function () {
        var $parent = $(this).parent();
        $parent.find(".formtips").remove();
        var $tip = $('<label  class="formtips"></label>');
        value = jQuery.trim(this.value);
        // 选择网点
        if ($(this).is('#location-name2')) {
            if (this.options[this.selectedIndex].text == '$user_manage_060') {
                $tip.addClass('onError').$form_validation_014;
            } else {

                $tip.addClass('glyphicon  glyphicon-ok').text('').css('color', '#666');
            }
            $parent.append($tip);
        }
        //选择费率类型
        if ($(this).is('#price-type2')) {
            if (this.options[this.selectedIndex].text == '$user_manage_06701') {
                $tip.addClass('onError').$form_validation_016;
            } else {
                $tip.addClass('glyphicon  glyphicon-ok').text('').css('color', '#666');
            }
            $parent.append($tip);
        }
        //选择集中器
        if ($(this).is('#mbus-name2')) {
            if (this.options[this.selectedIndex].text == '$user_manage_058') {
                $tip.addClass('onError').$form_validation_013;
            } else {

                $tip.addClass('glyphicon  glyphicon-ok').text('').css('color', '#666');
            }
            $parent.append($tip);
        }
    });

    /*****修改模态框*******/
    $('#edit-userinfo form input').blur(function () {
        var $parent = $(this).parent();
        $parent.find(".formtips").remove();
        var $tip = $('<label  class="formtips"></label>');
        value = jQuery.trim(this.value);
        //验证用户名
        if ($(this).is('#user-name4')) {
            if (!value) {
                $tip.addClass('onError').$form_validation_001;
            } else if (value.length > 50) {
                $tip.addClass('onError').$form_validation_003;
            } else {
                $tip.addClass('glyphicon  glyphicon-ok').text('').css('color', '#666');
            }
            $(this).parent().append($tip);
        }

        if ($(this).is('#user-mobile4')) {
            if (!value) {
                $tip.addClass('onError').$form_validation_004;
            } else if (!/^[0-9-_]{1,20}$/.test(value)) {
                $tip.addClass('onError').$form_validation_005;
            } else {
                $tip.addClass('glyphicon  glyphicon-ok').text('').css('color', '#666');
            }
            $(this).parent().append($tip);
        }
        //房间号码验证֤
        if ($(this).is('#user-room4')) {
            var unit = $('#address_unit  option:selected').val();
            if (!value) {
                $tip.addClass('onError').$form_validation_006;
            } else if (value != "" && unit == "") {
                $tip.addClass('onError').$form_validation_017;
            } else {
                $tip.addClass('glyphicon  glyphicon-ok').text('').css('color', '#666');
            }
            $parent.append($tip);

        }
        //身份证号码验证֤
        if ($(this).is('#user-cardID4')) {
            if (!value) {
                $tip.addClass('onError').$form_validation_008;
            } else if (!/^[0-9A-Za-z]{1,20}$/.test(value)) {
                $tip.addClass('onError').$public_053;
            } else {
                $tip.addClass('glyphicon  glyphicon-ok').text('').css('color', '#666');
            }
            $(this).parent().append($tip);
        }
        //  阀门编号0-254之间的数字
        if ($(this).is('#valve-cardID4')) {
            if (!value) {
                $tip.addClass('onError').$form_validation_009;
            } else if (!/^[0-9A-Za-z]{1,20}$/.test(value)) {
                $tip.addClass('onError').$public_053;
            } else {
                $tip.addClass('glyphicon  glyphicon-ok').text('').css('color', '#666');
            }
            $(this).parent().append($tip);
        }
        //子表号验证֤
//        if ($(this).is('#meter-subID4')) {
//            if (!value) {
//                $tip.addClass('onError').$form_validation_010;
//            } else if (!/^(0?\d{1,2}|1\d{2}|2([0-4]\d|5[0-4]))$/.test(value)) {
//                $tip.addClass('onError').$public_053;
//            } else {
//                $tip.addClass('glyphicon  glyphicon-ok').text('').css('color', '#666');
//            }
//            $(this).parent().append($tip);
//        }
        //12位表出厂编号
        if ($(this).is('#meter-no4')) {
            if (!value) {
                $tip.addClass('onError').$form_validation_011;
            } else if (!/^[0-9]{8,12}$/.test(value)) {
                $tip.addClass('onError').$public_053;
            } else {
                $tip.addClass('glyphicon  glyphicon-ok').text('').css('color', '#666');
            }
            $(this).parent().append($tip);
        }
        //面积匹配
        if ($(this).is('#house-area4')) {
            if (!value) {
                $tip.addClass('onError').$form_validation_012;
            } else if (!/^(?!(0[0-9]{0,}$))[0-9]{1,}[.]{0,}[0-9]{0,}$/.test(value)) {
                $tip.addClass('onError').$public_053;
            } else {
                $tip.addClass('glyphicon  glyphicon-ok').text('').css('color', '#666');
            }
            $(this).parent().append($tip);
        }
    });

    /*******修改仪表型号类型验证********/
    function fun_meter2() {
        var obj = $('#meter-type4').parent();
        var $tip = $('<label  class="formtips"></label>');
        obj.find('.formtips').remove();
        var select0 = $('#meter-type4  option:selected');
        var select1 = $('#meter-no4  option:selected');
        var select2 = $('#meter-protocol4  option:selected');
        if (select0.text().trim() != '$user_add_009' &&
                select1.text().trim() != '$user_add_019' &&
                select2.text().trim() != '$user_add_020') {
            $tip.addClass('glyphicon  glyphicon-ok').text('').css('color', '#666');
        } else {
            $tip.addClass('onError').$user_manage_135;
        }
        obj.append($tip);
    }

    /********下拉框验证**********/
    $('#edit-userinfo form  select').change(function () {
        var $parent = $(this).parent();
        $parent.find(".formtips").remove();
        var $tip = $('<label  class="formtips"></label>');
        value = jQuery.trim(this.value);
        // 选择网点
        if ($(this).is('#location-name4')) {
            if (this.options[this.selectedIndex].text == '$user_manage_060') {
                $tip.addClass('onError').$form_validation_014;
            } else {
                $tip.addClass('glyphicon  glyphicon-ok').text('').css('color', '#666');
            }
            $parent.append($tip);
        }
        //选择费率类型
        if ($(this).is('#price-type4')) {
            if (this.options[this.selectedIndex].text == '$user_manage_06701') {
                $tip.addClass('onError').$form_validation_016;
            } else {
                $tip.addClass('glyphicon  glyphicon-ok').text('').css('color', '#666');
            }
            $parent.append($tip);
        }
        //选择集中器
        if ($(this).is('#mbus-name4')) {
            if (this.options[this.selectedIndex].text == '$user_manage_058') {
                $tip.addClass('onError').$form_validation_013;
            } else {
                $tip.addClass('glyphicon  glyphicon-ok').text('').css('color', '#666');
            }
            $parent.append($tip);
        }
    });

    //提交，最终验证。
    $('#regist').click(function () {
        $("#tab-register form input.required").trigger('blur');
        $('#tab-register form  select.required').trigger('change');
        fun_meter();
    });

    /*****注册仪表类型型号协议类型change事件****/
    $('#meter-type2').change(fun_meter);
    $('#meter-no2').change(fun_meter);
    $('#meter-protocol2').change(fun_meter);

    //修改模态框提交事件
    $('#modify_user_confirm').click(function () {
        $("#edit-userinfo form input").trigger('blur');
        $('#edit-userinfo form select.required').trigger('change');
        fun_meter2();
    });

    /*****修改模态框仪表类型型号协议类型change事件****/
    $('#meter-type4').change(fun_meter2);
    $('#meter-no4').change(fun_meter2);
    $('#meter-protocol4').change(fun_meter2);
    //修改模态框消失是移除提示
    $('#edit-userinfo').on("hidden.bs.modal", function () {
        $("form  .formtips").remove();
    });

    //注册页面消失移除提示
    $('#tab-register').on("hidden.bs.modal", function () {
        $("#tab-register form  .formtips").remove();
        window.location.href = "user-manage.html";
    });
});