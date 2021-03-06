package com.commons.utils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * bean 操作相关工具类
 *
 * @author Administrator
 */
public class BeanUtils {
    /**
     * 功能描述: bean转换map
     *
     * @author: DoubleLi
     * @date: 2019/4/16 14:56
     */
    public static Map<String, Object> transBeanToMap(Object obj) {
        if (obj == null) {
            return null;
        }
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor property : propertyDescriptors) {
                String key = property.getName();

                // 过滤class属性
                if (!key.equals("class")) {
                    // 得到property对应的getter方法
                    Method getter = property.getReadMethod();
                    Object value = getter.invoke(obj);

                    map.put(key, value);
                }

            }
        } catch (Exception e) {
            System.out.println("transBeanToMap Error " + e);
        }
        return map;
    }

    /**
     * 功能描述: map转bean
     *
     * @param:
     * @return:
     * @author: DoubleLi
     * @date: 2019/4/16 14:57
     */
    public static <T> T transMapToBean(Class<T> type, @SuppressWarnings("rawtypes") Map map) throws Exception {
        T obj = null;
        /** 创建 JavaBean 对象*/
        obj = type.newInstance();
        /** 获取类属性*/
        BeanInfo beanInfo = Introspector.getBeanInfo(type);
        // 给 JavaBean 对象的属性赋值
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (int i = 0; i < propertyDescriptors.length; i++) {
            PropertyDescriptor descriptor = propertyDescriptors[i];
            String propertyName = descriptor.getName();

            if (map.containsKey(propertyName)) {
                // 下面一句可以 try 起来，这样当一个属性赋值失败的时候就不会影响其他属性赋值。
                Object value = map.get(propertyName);

                Object[] args = new Object[1];
                args[0] = value;

                descriptor.getWriteMethod().invoke(obj, args);
            }
        }
        return obj;
    }

    public static <T> List<T> transListMapToBean(Class<T> type, List<Map<String, Object>> list) throws Exception {
        // 获取类属性
        BeanInfo beanInfo = Introspector.getBeanInfo(type);
        // 给 JavaBean 对象的属性赋值
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        List<T> retList = new ArrayList<>();
        for (Map<String, Object> map : list) {
            // 创建 JavaBean 对象
            T obj = type.newInstance();
            for (int i = 0; i < propertyDescriptors.length; i++) {
                PropertyDescriptor descriptor = propertyDescriptors[i];
                String propertyName = descriptor.getName();

                if (map.containsKey(propertyName)) {
                    // 下面一句可以 try 起来，这样当一个属性赋值失败的时候就不会影响其他属性赋值。
                    Object value = map.get(propertyName);

                    Object[] args = new Object[1];
                    args[0] = value;
                    descriptor.getWriteMethod().invoke(obj, args);
                }
            }
            retList.add(obj);
        }
        return retList;
    }
}
