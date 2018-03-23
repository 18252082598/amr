package com.joymeter.util;

import com.joymeter.cache.FailingCache;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ParameterUtil {

    /**
     * 组合构成抄表参数
     *
     * @param list
     * @param url
     * @param isAutoClear
     * @param accountName
     * @param sendTime
     * @param balanceWarnning
     * @param valveClose
     * @return
     */
    public static String GetParameter(ArrayList<String[]> list,
            String url,
            int isAutoClear,
            String accountName,
            String sendTime,
            Double balanceWarnning,
            Double valveClose) {
        try {
            StringBuilder parameters = new StringBuilder("{DTU:[");
            for (int i = 0; i < list.size(); i++) {
                String[] ary = list.get(i);
                if (ary == null || ary.length < 6) {
                    continue;
                }
                String id = ary[0];
                String meter_no = ary[1];
                String meter_type = ary[2];
                String protocol_type = ary[3];
                String method = ary[6];
                String concentrator_model;
                if (method.equals("readByCommunity")) {
                    concentrator_model = "2";
                } else {
                    concentrator_model = ary[5];
                }
                if (protocol_type == null || protocol_type.isEmpty()
                        || protocol_type.toLowerCase().startsWith("jlaa")) {
                    continue;
                }
                // FailingCache.add(meter_no, JoyDatetime.TimeStampToDate(sendTime).getTime());
                if (list.size() == 1) {
                    parameters.append("{id:'").append(id).append("',url:'").append(url)
                            .append("',options:{isAutoClear:'").append(isAutoClear)
                            .append("',protocol:'").append(protocol_type)
                            .append("',accountName:'").append(URLDecoder.decode(accountName, "utf-8"))
                            .append("',sendTime:'").append(sendTime)
                            .append("',balanceWarnning:'").append(balanceWarnning == null ? "null" : balanceWarnning)
                            .append("',valveClose:'").append(valveClose == null ? "null" : valveClose)
                            .append("',concentrator_model:'").append(concentrator_model)
                            .append("'},meters:[{meter:'").append(meter_no)
                            .append("',category:'").append(meter_type)
                            .append("',protocol:'").append(protocol_type)
                            .append("'}]}");
                } else if (list.size() > 1) {
                    if (i == 0) {
                        parameters.append("{id:'").append(id).append("',url:'").append(url)
                                .append("',options:{isAutoClear:'").append(isAutoClear)
                                .append("',protocol:'").append(protocol_type)
                                .append("',accountName:'").append(URLDecoder.decode(accountName, "utf-8"))
                                .append("',sendTime:'").append(sendTime)
                                .append("',balanceWarnning:'").append(balanceWarnning == null ? "null" : balanceWarnning)
                                .append("',valveClose:'").append(valveClose == null ? "null" : valveClose)
                                .append("',concentrator_model:'").append(concentrator_model)
                                .append("'},meters:[{meter:'").append(meter_no)
                                .append("',category:'").append(meter_type)
                                .append("',protocol:'").append(protocol_type)
                                .append("'}");
                    } else if (i >= 1 && list.get(i)[0].equals(list.get(i - 1)[0])) {
                        parameters.append(",{meter:'").append(meter_no).append("',category:'")
                                .append(meter_type).append("',protocol:'").append(protocol_type)
                                .append("'}");
                    } else {
                        parameters.append("]},{id:'").append(id).append("',url:'").append(url)
                                .append("',options:{isAutoClear:'").append(isAutoClear)
                                .append("',protocol:'").append(protocol_type)
                                .append("',accountName:'").append(URLDecoder.decode(accountName, "utf-8"))
                                .append("',sendTime:'").append(sendTime)
                                .append("',balanceWarnning:'").append(balanceWarnning == null ? "null" : balanceWarnning)
                                .append("',valveClose:'").append(valveClose == null ? "null" : valveClose)
                                .append("',concentrator_model:'").append(concentrator_model)
                                .append("'},meters:[{meter:'").append(meter_no)
                                .append("',category:'").append(meter_type)
                                .append("',protocol:'").append(protocol_type)
                                .append("'}");
                    }
                    if (i == list.size() - 1) {
                        parameters.append("]}");
                    }
                }
            }
            parameters.append("]}");
            return parameters.toString();
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(ParameterUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }

    /**
     * 组装JLAA协议的请求JSON
     *
     * @param list
     * @return
     */
    public static String GetParameter(ArrayList<String[]> list) {
        StringBuilder parameters = new StringBuilder("{DTU:[");
        for (int i = 0; i < list.size(); i++) {
            String[] ary = list.get(i);
            if (ary == null || ary.length < 4) {
                continue;
            }
            String id = ary[0];
            String meter_no = ary[1];
            String meter_type = ary[2];
            String protocol_type = ary[3];
            if (protocol_type == null || protocol_type.isEmpty()
                    || !protocol_type.toLowerCase().startsWith("jlaa")) {
                continue;
            }
            if (list.size() == 1) {
                parameters.append("{id:'").append(id).append("',url:'").append("null")
                        .append("',options:{isAutoClear:'").append("null")
                        .append("',protocol:'").append(protocol_type)
                        .append("',accountName:'").append("null")
                        .append("',sendTime:'").append("null")
                        .append("',balanceWarnning:'").append("null")
                        .append("',valveClose:'").append("null")
                        .append("',concentrator_model:'").append("null")
                        .append("'},meters:[{meter:'").append(meter_no)
                        .append("',category:'").append(meter_type)
                        .append("',protocol:'").append(protocol_type)
                        .append("'}]}");
            } else if (list.size() > 1) {
                if (i == 0) {
                    parameters.append("{id:'").append(id).append("',url:'").append("null")
                            .append("',options:{isAutoClear:'").append("null")
                            .append("',protocol:'").append(protocol_type)
                            .append("',accountName:'").append("null")
                            .append("',sendTime:'").append("null")
                            .append("',balanceWarnning:'").append("null")
                            .append("',valveClose:'").append("null")
                            .append("',concentrator_model:'").append("null")
                            .append("'},meters:[{meter:'").append(meter_no)
                            .append("',category:'").append(meter_type)
                            .append("',protocol:'").append(protocol_type)
                            .append("'}");
                } else if (i >= 1 && list.get(i)[0].equals(list.get(i - 1)[0])) {
                    parameters.append(",{meter:'").append(meter_no).append("',category:'")
                            .append(meter_type).append("',protocol:'").append(protocol_type)
                            .append("'}");
                } else {
                    parameters.append("]},{id:'").append(id).append("',url:'").append("null")
                            .append("',options:{isAutoClear:'").append("null")
                            .append("',protocol:'").append(protocol_type)
                            .append("',accountName:'").append("null")
                            .append("',sendTime:'").append("null")
                            .append("',balanceWarnning:'").append("null")
                            .append("',valveClose:'").append("null")
                            .append("',concentrator_model:'").append("null")
                            .append("'},meters:[{meter:'").append(meter_no)
                            .append("',category:'").append(meter_type)
                            .append("',protocol:'").append(protocol_type)
                            .append("'}");
                }
                if (i == list.size() - 1) {
                    parameters.append("]}");
                }
            }
        }
        parameters.append("]}");
        return parameters.toString();
    }
}