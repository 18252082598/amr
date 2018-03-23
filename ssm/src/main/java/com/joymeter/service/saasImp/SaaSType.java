/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.joymeter.service.saasImp;

/**
 *
 * @author Zhgong Fuqiang
 * @version 1.0.0
 */
public class SaaSType {

    public static final String JSON_FORMAT_SAVE_INFO = "{\"meter\":\"%s\",\"category\":\"%s\",\"result\":\"%s\","
            + "\"data1\":\"%s\",\"data2\":\"%s\",\"data3\":\"%s\",\"data4\":\"%s\",\"data5\":\"%s\",\"data6\":\"%s\",\"data7\":\"%s\",\"data8\":\"%s\",\"date9\":\"%s\",\"date\":\"%s\","
            + "\"options\":{"
            + "\"isAutoClear\":\"%s\","
            + "\"protocol\":\"%s\","
            + "\"balanceWarnning\":\"%s\","
            + "\"concentrator_model\":\"%s\","
            + "\"accountName\":\"%s\","
            + "\"valveClose\":\"%s\","
            + "\"sendTime\":\"%s\""
            + "}}";

    public static final String JSON_FORMAT_SAAS_ADD = "{\"access_token\":\"%s\",\"client_id\":\"%s\",\"device_id\":\"%s\",\"type\":\"%s\"}";
    public static final String JSON_FORMAT_SAAS_UPDATE = "{\"access_token\":\"%s\",\"client_id\":\"%s\",\"old_device_id\":\"%s\",\"new_device_id\":\"%s\",\"type\":\"%s\"}";
    public static final String JSON_FORMAT_SAAS_DEL = "{\"access_token\":\"%s\",\"client_id\":\"%s\",\"device_id\":\"%s\"}";
    public static final String JSON_FORMAT_SAAS_DOWN = "{\"access_token\":\"%s\",\"client_id\":\"%s\",\"device_id\":\"%s\",\"payload\":\"%s\"}";
    public static final String JSON_FORMAT_SAAS_EVENT = "{\"event\":\"%s\",device:{\"id\" :\"%s\", \"error_code\":\"%s\",\"error_msg\":\"%s\", \"datetime\":\"%s\",\"operator_id\":\"%s\",\"operator_name\":\"%s\"}}";
}
