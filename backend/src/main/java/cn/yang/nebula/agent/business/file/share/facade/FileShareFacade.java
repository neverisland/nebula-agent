package cn.yang.nebula.agent.business.file.share.facade;

import cn.yang.nebula.agent.business.file.share.po.FileShareCreatePo;
import cn.yang.nebula.agent.business.file.share.po.FileSharePageQueryPo;
import cn.yang.nebula.agent.business.file.share.po.FileShareUpdatePo;
import cn.yang.nebula.agent.business.file.share.vo.FileSharePublicVo;
import cn.yang.nebula.agent.business.file.share.vo.FileShareVo;
import cn.yang.common.data.structure.vo.page.PageResult;

import java.util.List;

/**
 * 分享业务层接口
 *
 * @author 未见清海
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
}
