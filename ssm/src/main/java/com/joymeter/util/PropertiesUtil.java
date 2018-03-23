/*
 * Copyright 2015-2102 RonCoo(http://www.roncoo.com) Group.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.joymeter.util;

import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;


public class PropertiesUtil {


    /**
     * 通过静态代码块读取上传文件的验证格式配置文件,静态代码块只执行一次(单例)
     */
    private static Properties properties = new Properties();

    private PropertiesUtil() {

    }
    /**
     * 函数功能说明 ：读取配置项 
     *
     * @参数：
     * @return void
     * @throws
     */
    public static String readConfig(String key,String fileName) {
        
        
        try {
            // 从类路径下读取属性文件
            properties.load(PropertiesUtil.class.getClassLoader()
                    .getResourceAsStream(fileName));
        } catch (IOException e) {
            Logger.getLogger(PropertiesUtil.class.getName()).log(Level.SEVERE, null, e);
        }
        
        return (String) properties.get(key);
    }
}
