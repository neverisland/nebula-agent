package cn.yang.nebula.agent.business.file.space.vo;

import lombok.Builder;
import lombok.Data;

/**
 * 文件空间统计 VO
 *
 * @author 未见清海
 */
@Data
@Builder
public class FileSpaceStatisticsVo {

    /**
     * 空间数量
     */
    private Integer spaceCount;
}
