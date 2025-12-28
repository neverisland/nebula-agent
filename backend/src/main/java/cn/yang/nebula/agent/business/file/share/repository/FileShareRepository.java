package cn.yang.nebula.agent.business.file.share.repository;

import cn.yang.common.data.structure.exception.BusinessException;
import cn.yang.common.data.structure.utils.bean.BeanConvertUtils;
import cn.yang.common.data.structure.vo.page.PageResult;
import cn.yang.common.data.structure.vo.page.PageUtils;
import cn.yang.foundational.capability.id.generator.IdGenerator;
import cn.yang.nebula.agent.business.authentication.entity.UserInfo;
import cn.yang.nebula.agent.business.file.share.dal.FileShareDo;
import cn.yang.nebula.agent.business.file.share.dal.FileSharingAssociationDo;
import cn.yang.nebula.agent.business.file.share.entity.FileShare;
import cn.yang.nebula.agent.business.file.share.enums.ShareTypeEnum;
import cn.yang.nebula.agent.business.file.share.mapper.FileShareMapper;
import cn.yang.nebula.agent.business.file.share.mapper.FileSharingAssociationMapper;
import cn.yang.nebula.agent.business.file.share.po.FileSharePageQueryPo;
import cn.yang.nebula.agent.enums.ErrorStatusCodeEnum;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 分享仓储层
 *
 * @author 未见清海
 */
@Repository
public class FileShareRepository {

    @Resource
    private FileShareMapper fileShareMapper;

    @Resource
    private FileSharingAssociationMapper fileSharingAssociationMapper;

    @Resource
    private IdGenerator idGenerator;

    /**
     * 新增分享记录（从PO转换）
     *
     * @param fileShare 创建入参
     * @return 分享ID
     */
    @Transactional(rollbackFor = Exception.class)
    public String insert(FileShare fileShare, UserInfo userInfo) {
        fileShare.setId(idGenerator.getId());
        fileShare.setCreateUserId(userInfo.getUserId());
        fileShare.setUpdateUserId(userInfo.getUserId());
        FileShareDo fileShareDo = BeanConvertUtils.convert(fileShare, FileShareDo.class);
        fileShareMapper.insert(fileShareDo);

        // 新增关联数据
        List<FileSharingAssociationDo> fileSharingAssociationDoList = new ArrayList<>();
        if (ShareTypeEnum.FILE.getType().equals(fileShare.getShareType())) {
            fileSharingAssociationDoList = fileShare.getFileIds().stream()
                    .map(fileId -> new FileSharingAssociationDo(idGenerator.getId(), fileShare.getId(), fileId))
                    .toList();
        } else {
            FileSharingAssociationDo associationDo = new FileSharingAssociationDo(idGenerator.getId(),
                    fileShare.getId());
            associationDo.setSpaceId(fileShare.getSpaceId());
            fileSharingAssociationDoList.add(associationDo);
        }
        fileSharingAssociationMapper.insertBatch(fileSharingAssociationDoList);
        return fileShareDo.getId();
    }

    /**
     * 更新分享记录
     *
     * @param fileShare 分享实体
     */
    @Transactional(rollbackFor = Exception.class)
    public void update(FileShare fileShare, UserInfo userInfo) {
        fileShare.setUpdateUserId(userInfo.getUserId());
        FileShareDo updateDo = BeanConvertUtils.convert(fileShare, FileShareDo.class);
        // 更新分享记录数据
        fileShareMapper.update(updateDo);

        // 更新关联表（仅文件类型且有文件列表时）
        if (ShareTypeEnum.FILE.getType().equals(fileShare.getShareType()) && !CollectionUtils.isEmpty(fileShare.getFileIds())) {
            fileSharingAssociationMapper.deleteByShareId(fileShare.getId());

            List<FileSharingAssociationDo> associationDoList = fileShare.getFileIds().stream()
                    .map(fileId -> new FileSharingAssociationDo(idGenerator.getId(), fileShare.getId(), fileId))
                    .toList();
            fileSharingAssociationMapper.insertBatch(associationDoList);
        }
    }

    /**
     * 根据ID删除分享记录
     *
     * @param id 分享ID
     */
    public void deleteById(String id) {
        fileShareMapper.deleteById(id);
    }

    /**
     * 根据ID批量删除分享记录
     *
     * @param ids ID列表
     */
    public void deleteBatchIds(List<String> ids) {
        int delete = fileShareMapper.deleteBatchIds(ids);
        if (delete == 0) {
            throw new BusinessException(ErrorStatusCodeEnum.DATA_DOES_NOT_EXIST, "分享记录不存在");
        }
    }

    /**
     * 根据ID查询分享记录
     *
     * @param id 分享ID
     * @return 分享记录
     */
    public FileShare selectById(String id) {
        FileShareDo fileShareDo = fileShareMapper.selectById(id);
        if (fileShareDo == null) {
            throw new BusinessException(ErrorStatusCodeEnum.DATA_DOES_NOT_EXIST, "分享记录不存在");
        }
        FileShare fileShare = BeanConvertUtils.convert(fileShareDo, FileShare.class);
        List<FileSharingAssociationDo> fileSharingAssociationDos = fileSharingAssociationMapper.selectByShareId(id);
        List<String> fileIds = fileSharingAssociationDos.stream().map(FileSharingAssociationDo::getFileId).filter(StringUtils::hasLength).toList();
        if (!CollectionUtils.isEmpty(fileIds)) {
            fileShare.setFileIds(fileIds);
        }
        List<String> spaceId = fileSharingAssociationDos.stream().map(FileSharingAssociationDo::getSpaceId).filter(StringUtils::hasLength).toList();
        if (!CollectionUtils.isEmpty(spaceId) && spaceId.size() == 1) {
            fileShare.setSpaceId(spaceId.getFirst());
        }
        return fileShare;
    }

    /**
     * 分页查询
     *
     * @param fileSharePageQueryPo 查询条件
     * @return 分页结果
     */
    public PageResult<FileShare> pageData(FileSharePageQueryPo fileSharePageQueryPo) {
        PageResult<FileShareDo> pageResult = PageUtils.doSelectPage(fileSharePageQueryPo,
                () -> fileShareMapper.selectPageData(fileSharePageQueryPo));
        if (CollectionUtils.isEmpty(pageResult.getList())) {
            return PageResult.empty(fileSharePageQueryPo);
        }
        List<FileShare> fileShareList = BeanConvertUtils.convert(pageResult.getList(), FileShare.class);
        return pageResult.transLate(fileShareList);
    }

    /**
     * 根据分享ID查询关联列表
     *
     * @param shareId 分享ID
     * @return 关联列表
     */
    public List<FileSharingAssociationDo> selectAssociationByShareId(String shareId) {
        return fileSharingAssociationMapper.selectByShareId(shareId);
    }
}
