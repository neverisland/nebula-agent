package cn.yang.nebula.agent.business.file.share.service;

import cn.yang.common.data.structure.exception.BusinessException;
import cn.yang.common.data.structure.utils.bean.BeanConvertUtils;
import cn.yang.common.data.structure.vo.page.PageResult;
import cn.yang.nebula.agent.business.authentication.entity.UserInfo;
import cn.yang.nebula.agent.business.authentication.facade.AuthenticationFacade;
import cn.yang.nebula.agent.business.file.library.repository.FileLibraryRepository;
import cn.yang.nebula.agent.business.file.share.entity.FileShare;
import cn.yang.nebula.agent.business.file.share.enums.ShareTypeEnum;
import cn.yang.nebula.agent.business.file.share.facade.FileShareFacade;
import cn.yang.nebula.agent.business.file.share.po.FileShareCreatePo;
import cn.yang.nebula.agent.business.file.share.po.FileSharePageQueryPo;
import cn.yang.nebula.agent.business.file.share.po.FileShareUpdatePo;
import cn.yang.nebula.agent.business.file.share.repository.FileShareRepository;
import cn.yang.nebula.agent.business.file.share.vo.FileSharePublicVo;
import cn.yang.nebula.agent.business.file.share.vo.FileShareVo;
import cn.yang.nebula.agent.business.file.space.entity.FileSpace;
import cn.yang.nebula.agent.business.file.space.repository.FileSpaceRepository;
import cn.yang.nebula.agent.business.file.space.vo.FileSpaceVo;
import cn.yang.nebula.agent.enums.ErrorStatusCodeEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 分享业务实现层
 *
 * @author 未见清海
 */
@Service
@RequiredArgsConstructor
public class FileShareService implements FileShareFacade {

    private final FileShareRepository fileShareRepository;

    private final FileLibraryRepository fileLibraryRepository;

    private final FileSpaceRepository fileSpaceRepository;

    private final AuthenticationFacade authenticationFacade;

    @Value("${file.share.address}")
    private String shareAddress;

    /**
     * 创建分享
     *
     * @param fileShareCreatePo 创建参数
     * @return 分享ID
     */
    @Override
    public String create(FileShareCreatePo fileShareCreatePo) {
        UserInfo userInfo = authenticationFacade.getCurrentUserInfo();
        FileShare fileShare = BeanConvertUtils.convert(fileShareCreatePo, FileShare.class);
        return fileShareRepository.insert(fileShare, userInfo);
    }

    /**
     * 修改分享
     *
     * @param fileShareUpdatePo 更新参数
     */
    @Override
    public void update(FileShareUpdatePo fileShareUpdatePo) {
        // 校验当前用户是否有权限
        UserInfo userInfo = authenticationFacade.getCurrentUserInfo();
        FileShare fileShare = getShareDetail(fileShareUpdatePo.getId(), userInfo);
        // 校验类型是否修改了
        if (!fileShare.getShareType().equals(fileShareUpdatePo.getShareType())) {
            throw new BusinessException(ErrorStatusCodeEnum.PARAMETER_VERIFICATION_EXCEPTION, "分享类型不能修改.");
        }
        // 校验是否已经过期
        if (fileShare.getIsExpired()) {
            throw new BusinessException(ErrorStatusCodeEnum.PARAMETER_VERIFICATION_EXCEPTION, "分享已过期.");
        }

        // 更新文件分享数据
        FileShare updateFileShare = BeanConvertUtils.convert(fileShareUpdatePo, FileShare.class);
        fileShareRepository.update(updateFileShare, userInfo);
    }

    /**
     * 删除分享
     *
     * @param id 分享ID
     */
    @Override
    public void delete(String id) {
        UserInfo userInfo = authenticationFacade.getCurrentUserInfo();
        getShareDetail(id, userInfo);
        // 删除数据
        fileShareRepository.deleteById(id);
    }

    /**
     * 获取分享详情并校验权限
     *
     * @param id       分享ID
     * @param userInfo 用户信息
     * @return 分享详情
     */
    private FileShare getShareDetail(String id, UserInfo userInfo) {
        FileShare fileShare = fileShareRepository.selectById(id);
        if (!userInfo.getUserId().equals(fileShare.getCreateUserId())) {
            throw new BusinessException(ErrorStatusCodeEnum.PERMISSION_ERROR, "无权限.");
        }
        return fileShare;
    }

    /**
     * 查询分享详情
     *
     * @param id 分享ID
     * @return 分享详情
     */
    @Override
    public FileShareVo detail(String id) {
        UserInfo userInfo = authenticationFacade.getCurrentUserInfo();
        FileShare fileShare = getShareDetail(id, userInfo);
        FileShareVo fileShareVo = BeanConvertUtils.convert(fileShare, FileShareVo.class);
        // 补充文件相关信息
        if (ShareTypeEnum.FILE.getType().equals(fileShare.getShareType())) {
            fileShareVo.setFileCount(fileShare.getFileIds().size());
        } else {
            FileSpace fileSpace = fileSpaceRepository.selectById(fileShare.getSpaceId());
            fileShareVo.setFileSpaceVo(BeanConvertUtils.convert(fileSpace, FileSpaceVo.class));
        }
        // 构建分享链接
        fileShareVo.setShareUrl(shareAddress + id);

        return fileShareVo;
    }

    /**
     * 分页查询分享列表
     *
     * @param fileSharePageQueryPo 分页查询参数
     * @return 分页结果
     */
    @Override
    public PageResult<FileShareVo> page(FileSharePageQueryPo fileSharePageQueryPo) {
        UserInfo userInfo = authenticationFacade.getCurrentUserInfo();
        fileSharePageQueryPo.setUserId(userInfo.getUserId());
        PageResult<FileShare> fileSharePageResult = fileShareRepository.pageData(fileSharePageQueryPo);

        List<FileShare> fileShareList = fileSharePageResult.getList();
        if (CollectionUtils.isEmpty(fileShareList)) {
            return PageResult.empty(fileSharePageQueryPo);
        }

        // 获取所有的个人空间id
        List<String> fileSpaceIdList = fileShareList.stream().filter(item -> ShareTypeEnum.SPACE.getType().equals(item.getShareType())).map(FileShare::getSpaceId).toList();
        List<FileSpace> fileSpaceList = fileSpaceRepository.selectByIds(fileSpaceIdList);
        Map<String, FileSpace> fileSpaceMap = fileSpaceList.stream().collect(Collectors.toMap(FileSpace::getId, item -> item));
        // 补充数据
        List<FileShareVo> fileShareVoList = fileShareList.stream().map(fileShare -> {
            FileShareVo fileShareVo = BeanConvertUtils.convert(fileShare, FileShareVo.class);

            // 补充路径
            fileShareVo.setShareUrl(shareAddress + fileShare.getId());
            // 补充数量和信息
            if (ShareTypeEnum.SPACE.getType().equals(fileShare.getShareType()) && fileSpaceMap.get(fileShare.getSpaceId()) != null) {
                FileSpaceVo fileSpaceVo = BeanConvertUtils.convert(fileSpaceMap.get(fileShare.getSpaceId()), FileSpaceVo.class);
                fileShareVo.setFileSpaceVo(fileSpaceVo);
            }
            return fileShareVo;
        }).collect(Collectors.toList());

        return fileSharePageResult.transLate(fileShareVoList);
    }

    /**
     * 公开访问获取分享信息
     *
     * @param shareId 分享ID
     * @return 分享信息
     */
    @Override
    public FileSharePublicVo getPublicInfo(String shareId) {
        FileShare fileShare = fileShareRepository.selectById(shareId);
        FileSharePublicVo fileSharePublicVo = BeanConvertUtils.convert(fileShare, FileSharePublicVo.class);
        // 补充文件相关信息
        if (ShareTypeEnum.FILE.getType().equals(fileShare.getShareType())) {
            fileSharePublicVo.setFileCount(fileShare.getFileIds().size());
        } else {
            FileSpace fileSpace = fileSpaceRepository.selectById(fileShare.getSpaceId());
            fileSharePublicVo.setFileSpaceVo(BeanConvertUtils.convert(fileSpace, FileSpaceVo.class));
        }

        return fileSharePublicVo;
    }

}
