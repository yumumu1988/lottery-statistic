package com.yumumu.lotterystatistic.dao.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

import lombok.Builder;
import lombok.Data;

/**
 * 
 * @TableName lottery_relationship
 */
@TableName(value ="lottery_relationship")
@Data
@Builder
public class LotteryRelationship implements Serializable {
    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 期号（小）
     */
    private String snSmall;

    /**
     * 期号（大）
     */
    private String snBig;

    /**
     * ID（小）
     */
    private Integer idSmall;

    /**
     * ID（大）
     */
    private Integer idBig;

    /**
     * 同号个数
     */
    private Integer sameSize;

    /**
     * 同号集合
     */
    private String sameNums;

    @TableField(exist = false)
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
        LotteryRelationship other = (LotteryRelationship) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getSnSmall() == null ? other.getSnSmall() == null : this.getSnSmall().equals(other.getSnSmall()))
            && (this.getSnBig() == null ? other.getSnBig() == null : this.getSnBig().equals(other.getSnBig()))
            && (this.getIdSmall() == null ? other.getIdSmall() == null : this.getIdSmall().equals(other.getIdSmall()))
            && (this.getIdBig() == null ? other.getIdBig() == null : this.getIdBig().equals(other.getIdBig()))
            && (this.getSameSize() == null ? other.getSameSize() == null : this.getSameSize().equals(other.getSameSize()))
            && (this.getSameNums() == null ? other.getSameNums() == null : this.getSameNums().equals(other.getSameNums()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getSnSmall() == null) ? 0 : getSnSmall().hashCode());
        result = prime * result + ((getSnBig() == null) ? 0 : getSnBig().hashCode());
        result = prime * result + ((getIdSmall() == null) ? 0 : getIdSmall().hashCode());
        result = prime * result + ((getIdBig() == null) ? 0 : getIdBig().hashCode());
        result = prime * result + ((getSameSize() == null) ? 0 : getSameSize().hashCode());
        result = prime * result + ((getSameNums() == null) ? 0 : getSameNums().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", snSmall=").append(snSmall);
        sb.append(", snBig=").append(snBig);
        sb.append(", idSmall=").append(idSmall);
        sb.append(", idBig=").append(idBig);
        sb.append(", sameSize=").append(sameSize);
        sb.append(", sameNums=").append(sameNums);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}