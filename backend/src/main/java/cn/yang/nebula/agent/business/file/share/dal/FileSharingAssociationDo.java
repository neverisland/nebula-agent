package cn.yang.nebula.agent.business.file.share.dal;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 分享关联 DO
 *
 * @author 未见清海
 */
@Data
public class FileSharingAssociationDo implements Serializable {

    @Serial
    private static final long serialVersionUID = -4718706175252785684L;

    /**
     * 主键ID
     */
    private String id;

    /**
     * 关联的分享记录ID
     */
    private String shareId;

    /**
     * 关联的文件ID，可为空
     */
    private String fileId;

    /**
     * 关联的空间ID，可为空
     */
    private String spaceId;

    public FileSharingAssociationDo() {
    }

    public FileSharingAssociationDo(String id, String shareId) {
        this.id = id;
        this.shareId = shareId;
    }

    // 构造文件关联关系
    public FileSharingAssociationDo(String id, String shareId, String fileId) {
        this.id = id;
        this.shareId = shareId;
        this.fileId = fileId;
    }

}
