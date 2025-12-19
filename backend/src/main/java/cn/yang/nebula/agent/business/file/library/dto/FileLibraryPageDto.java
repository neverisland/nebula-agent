package cn.yang.nebula.agent.business.file.library.dto;

import cn.yang.common.data.structure.vo.page.PageQuery;
import lombok.Data;

import java.io.Serial;

/**
 * 文件库分页查询入参
 *
 * @author 未见清海
 */
@Data
public class FileLibraryPageDto extends PageQuery {

    @Serial
    private static final long serialVersionUID = -8422875729679494206L;
    
    /**
     * 关键词（匹配名称或路径）
     */
    private String searchText;

    /**
     * 空间id（预留）
     */
    private String spaceId;


    /**
     * 用户id
     */
    private String userId;
}

