/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.joymeter.service;

import com.joymeter.entity.Result;

/**
 *
 * @author Administrator
 */
public interface TokenService {

    /**
     * 换表码
     *
     * @param meter_no 表号
     * @param new_meter_no 新表号
     * @param amount 表内余量
     * @return 8位返回设置码 + 充值码， 20位返回credit token
     */
    Result changeToken(final String meter_no, final String new_meter_no, int amount);

    /**
     * 清零码
     *
     * @param meter_no 表号
     * @param operator
     * @return 8位返回清零码， 20位返回clear token
     */
    Result clearToken(final String meter_no, final String operator);

    /**
     * 充值码
     *
     * @param meter_no 表号
     * @param amount
     * @param operator
     * @return 8位返回充值码， 20位返回credit token
     */
    Result creditToken(final String meter_no, int amount, final String operator);

    /**
     * 设置码
     *
     * @param meter_no 表号
     * @return 8位返回设置码， 20位返回key change token
     */
    Result setToken(final String meter_no);

}
