package com.jfinal.club.common.Enum;

import java.util.TreeMap;

/** 
 * 商品状态枚举
 *
 * Created by wangyao6 on 2017/1/20.
 */
public enum MlGoodsTypeKindEnum {
    LevelOne(0, "1级", "1级"),
    LevelTwo(1, "2级", "2级");



    MlGoodsTypeKindEnum(Integer code, String name, String description) {
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
    private MlGoodsTypeKindEnum(String description) {
        this.description = description;
    }

    /**
     * @param code        数字编码
     * @param description 中文描述
     */
    private MlGoodsTypeKindEnum(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    /**
     * @param name        英文编码名称
     * @param description 中文描述
     */
    private MlGoodsTypeKindEnum(String name, String description) {
        this.name = name;
        this.description = description;
    }


    /**
     * 按数值获取对应的枚举类型
     *
     * @param code 数值
     * @return 枚举类型
     */
    public static MlGoodsTypeKindEnum enumValueOf(Integer code) {
        MlGoodsTypeKindEnum[] values = MlGoodsTypeKindEnum.values();
        MlGoodsTypeKindEnum v = null;
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
    public static MlGoodsTypeKindEnum enumValueOf(String name) {
        MlGoodsTypeKindEnum[] values = MlGoodsTypeKindEnum.values();
        MlGoodsTypeKindEnum v = null;
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
        for (int i = 0; i < MlGoodsTypeKindEnum.values().length; i++) {
            map.put(MlGoodsTypeKindEnum.values()[i].toCode(), MlGoodsTypeKindEnum.values()[i].toName());
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
