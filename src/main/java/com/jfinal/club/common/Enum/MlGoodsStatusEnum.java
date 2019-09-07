package com.jfinal.club.common.Enum;

import com.jfinal.club.common.model.EnumDomain;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

/** 
 * good表任务状态枚举
 *
 * Created by wangyao6 on 2017/1/20.
 */
public enum MlGoodsStatusEnum {
    Selling(0, "上架", "商品上架"),
    StopSelling(1, "下架", "商品下架"),
    Delete(2, "删除", "商品删除");


    MlGoodsStatusEnum(Integer code, String name, String description) {
        this.code = code;
        this.name = name;
        this.description = description;
    }

    private Integer code;
    private String name;
    private String description;

    /**
     * @param description 中文描述
     */
    private MlGoodsStatusEnum(String description) {
        this.description = description;
    }

    /**
     * @param code        数字编码
     * @param description 中文描述
     */
    private MlGoodsStatusEnum(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    /**
     * @param name        英文编码名称
     * @param description 中文描述
     */
    private MlGoodsStatusEnum(String name, String description) {
        this.name = name;
        this.description = description;
    }


    /**
     * 按数值获取对应的枚举类型
     *
     * @param code 数值
     * @return 枚举类型
     */
    public static MlGoodsStatusEnum enumValueOf(Integer code) {
        MlGoodsStatusEnum[] values = MlGoodsStatusEnum.values();
        MlGoodsStatusEnum v = null;
        for (int i = 0; i < values.length; i++) {
            if (code.equals(values[i].toCode())) {
                v = values[i];
                break;
            }
        }
        return v;
    }

    /**
     * 按英文编码获取对应的枚举类型
     *
     * @param name 英文编码
     * @return 枚举类型
     */
    public static MlGoodsStatusEnum enumValueOf(String name) {
        MlGoodsStatusEnum[] values = MlGoodsStatusEnum.values();
        MlGoodsStatusEnum v = null;
        for (int i = 0; i < values.length; i++) {
            if (values[i].toName().equalsIgnoreCase(name)) {
                v = values[i];
                break;
            }
        }
        return v;
    }

    /**
     * 获取枚举类型的所有<数字编码,中文描述>对
     *
     * @return
     */
    public static TreeMap<Integer, String> toCodeDescriptionMap() {
        TreeMap<Integer, String> map = new TreeMap<Integer, String>();
        for (int i = 0; i < MlGoodsStatusEnum.values().length; i++) {
            map.put(MlGoodsStatusEnum.values()[i].toCode(), MlGoodsStatusEnum.values()[i].toName());
        }
        return map;
    }



    /**
     * 获取枚举类型的所有<数字编码,中文描述>对
     *
     * @return
     */
    public static List<EnumDomain> toCodeNameList() {
        List dataList=new ArrayList();
        for (int i = 0; i < MlGoodsStatusEnum.values().length; i++) {
            dataList.add(new EnumDomain(MlGoodsStatusEnum.values()[i].toCode(), MlGoodsStatusEnum.values()[i].toName()));
        }
        return dataList;
    }

    /**
     * 获取枚举类型数值编码
     */
    public Integer toCode() {
        return this.code == null ? this.ordinal() : this.code;
    }

    /**
     * 获取枚举类型英文编码名称
     */
    public String toName() {
        return this.name == null ? this.name() : this.name;
    }

    /**
     * 获取枚举类型中文描述
     */
    public String toDescription() {
        return this.description;
    }

    /**
     * 获取枚举类型中文描述
     */
    public String toString() {
        return this.description;
    }
}
