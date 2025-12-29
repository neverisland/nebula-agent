package cn.yang.nebula.agent.business.file.share.facade;

import cn.yang.nebula.agent.business.file.share.po.FileShareCreatePo;
import cn.yang.nebula.agent.business.file.share.po.FileSharePageQueryPo;
import cn.yang.nebula.agent.business.file.share.po.FileShareUpdatePo;
import cn.yang.nebula.agent.business.file.share.vo.FileShareFileInfoVo;
import cn.yang.nebula.agent.business.file.share.vo.FileSharePublicVo;
import cn.yang.nebula.agent.business.file.share.vo.FileShareVo;
import cn.yang.common.data.structure.vo.page.PageResult;

import java.util.List;

/**
 * 分享业务层接口
 *
 * @author QingHai
 */
public interface FileShareFacade {

    /**
     * 新增分享
     *
     * @param createPo 创建参数
     * @return 分享ID
     */
    String create(FileShareCreatePo createPo);

    /**
     * 修改分享
     *
     * @param updatePo 更新参数
     */
    void update(FileShareUpdatePo updatePo);

    /**
     * 删除分享
     *
     * @param id 分享ID
     */
    void delete(String id);

    /**
     * 分享详情
     *
     * @param id 分享ID
     * @return 详情
     */
    FileShareVo detail(String id);

    /**
     * 分页查询
     *
     * @param pageQuery 查询参数
     * @return 分页结果
     */
    PageResult<FileShareVo> page(FileSharePageQueryPo pageQuery);

    /**
     * 公开访问获取分享信息
     *
     * @param shareId 分享ID
     * @return 分享信息
     */
    FileSharePublicVo getPublicInfo(String shareId);

    /**
     * 增加访问次数
     * 使用分布式锁保证并发安全
     *
     * @param id 分享ID
     */
    void incrementVisitCount(String id);

    /**
     * 下载全部文件（公开接口）
     * 流式压缩并返回文件流
     *
     * @param shareId 分享ID
     * @param password 密码（可选）
     * @param response HTTP响应
     */
    void downloadAll(String shareId, String password, jakarta.servlet.http.HttpServletResponse response);

    /**
     * 验证分享密码（公开接口）
     *
     * @param shareId 分享ID
     * @param password 密码
     */
    void verifyPassword(String shareId, String password);

    /**
     * 获取分享文件列表（公开接口）
     *
     * @param shareId 分享ID
     * @param password 密码
     * @return 文件列表
     */
    List<FileShareFileInfoVo> getFileList(String shareId, String password);
}
