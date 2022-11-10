package com.yumumu.lotterystatistic.dao.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName history_num
 */
@TableName(value ="history_num")
@Data
public class HistoryNum implements Serializable {
    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 红球
     */
    private String reds;

    /**
     * 蓝球
     */
    private String blue;

    /**
     * 期号
     */
    private String sn;

    /**
     * 等级
     */
    private Integer bingoLevel;

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
        HistoryNum other = (HistoryNum) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getReds() == null ? other.getReds() == null : this.getReds().equals(other.getReds()))
            && (this.getBlue() == null ? other.getBlue() == null : this.getBlue().equals(other.getBlue()))
            && (this.getSn() == null ? other.getSn() == null : this.getSn().equals(other.getSn()))
            && (this.getBingoLevel() == null ? other.getBingoLevel() == null : this.getBingoLevel().equals(other.getBingoLevel()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getReds() == null) ? 0 : getReds().hashCode());
        result = prime * result + ((getBlue() == null) ? 0 : getBlue().hashCode());
        result = prime * result + ((getSn() == null) ? 0 : getSn().hashCode());
        result = prime * result + ((getBingoLevel() == null) ? 0 : getBingoLevel().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", reds=").append(reds);
        sb.append(", blue=").append(blue);
        sb.append(", sn=").append(sn);
        sb.append(", bingoLevel=").append(bingoLevel);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}