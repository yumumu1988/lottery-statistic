package com.yumumu.lotterystatistic.dao.domain;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Builder;
import lombok.Data;

/**
 * 
 * @TableName lottery_num
 */
@Data
@Builder
public class LotteryNum implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 
     */
    private String code;

    /**
     * 
     */
    private String sn;

    /**
     * 
     */
    private Integer num1;

    /**
     * 
     */
    private Integer num2;

    /**
     * 
     */
    private Integer num3;

    /**
     * 
     */
    private Integer num4;

    /**
     * 
     */
    private Integer num5;

    /**
     * 
     */
    private Integer num6;

    /**
     * 
     */
    private Integer blue;

    /**
     * 
     */
    private Long bitNum;

    private static final long serialVersionUID = 1L;

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        LotteryNum other = (LotteryNum) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getCode() == null ? other.getCode() == null : this.getCode().equals(other.getCode()))
            && (this.getSn() == null ? other.getSn() == null : this.getSn().equals(other.getSn()))
            && (this.getNum1() == null ? other.getNum1() == null : this.getNum1().equals(other.getNum1()))
            && (this.getNum2() == null ? other.getNum2() == null : this.getNum2().equals(other.getNum2()))
            && (this.getNum3() == null ? other.getNum3() == null : this.getNum3().equals(other.getNum3()))
            && (this.getNum4() == null ? other.getNum4() == null : this.getNum4().equals(other.getNum4()))
            && (this.getNum5() == null ? other.getNum5() == null : this.getNum5().equals(other.getNum5()))
            && (this.getNum6() == null ? other.getNum6() == null : this.getNum6().equals(other.getNum6()))
            && (this.getBlue() == null ? other.getBlue() == null : this.getBlue().equals(other.getBlue()))
            && (this.getBitNum() == null ? other.getBitNum() == null : this.getBitNum().equals(other.getBitNum()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getCode() == null) ? 0 : getCode().hashCode());
        result = prime * result + ((getSn() == null) ? 0 : getSn().hashCode());
        result = prime * result + ((getNum1() == null) ? 0 : getNum1().hashCode());
        result = prime * result + ((getNum2() == null) ? 0 : getNum2().hashCode());
        result = prime * result + ((getNum3() == null) ? 0 : getNum3().hashCode());
        result = prime * result + ((getNum4() == null) ? 0 : getNum4().hashCode());
        result = prime * result + ((getNum5() == null) ? 0 : getNum5().hashCode());
        result = prime * result + ((getNum6() == null) ? 0 : getNum6().hashCode());
        result = prime * result + ((getBlue() == null) ? 0 : getBlue().hashCode());
        result = prime * result + ((getBitNum() == null) ? 0 : getBitNum().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", code=").append(code);
        sb.append(", sn=").append(sn);
        sb.append(", num1=").append(num1);
        sb.append(", num2=").append(num2);
        sb.append(", num3=").append(num3);
        sb.append(", num4=").append(num4);
        sb.append(", num5=").append(num5);
        sb.append(", num6=").append(num6);
        sb.append(", blue=").append(blue);
        sb.append(", bitNum=").append(bitNum);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}