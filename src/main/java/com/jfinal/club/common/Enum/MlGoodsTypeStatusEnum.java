package com.jfinal.club.common.Enum;

import java.util.TreeMap;

/** 
 * 商品状态枚举
 *
 * Created by wangyao6 on 2017/1/20.
 */
public enum MlGoodsTypeStatusEnum {
    Valid(0, "有效", "有效"),
    Invalid(1, "无效", "无效");


    MlGoodsTypeStatusEnum(Integer code, String name, String description) {
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
    private MlGoodsTypeStatusEnum(String description) {
        this.description = description;
    }

    /**
     * @param code        数字编码
     * @param description 中文描述
     */
    private MlGoodsTypeStatusEnum(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    /**
     * @param name        英文编码名称
     * @param description 中文描述
     */
    private MlGoodsTypeStatusEnum(String name, String description) {
        this.name = name;
        this.description = description;
    }


    /**
     * 按数值获取对应的枚举类型
     *
     * @param code 数值
     * @return 枚举类型
     */
    public static MlGoodsTypeStatusEnum enumValueOf(Integer code) {
        MlGoodsTypeStatusEnum[] values = MlGoodsTypeStatusEnum.values();
        MlGoodsTypeStatusEnum v = null;
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
    public static MlGoodsTypeStatusEnum enumValueOf(String name) {
        MlGoodsTypeStatusEnum[] values = MlGoodsTypeStatusEnum.values();
        MlGoodsTypeStatusEnum v = null;
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
        for (int i = 0; i < MlGoodsTypeStatusEnum.values().length; i++) {
            map.put(MlGoodsTypeStatusEnum.values()[i].toCode(), MlGoodsTypeStatusEnum.values()[i].toName());
        }
        return map;
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
