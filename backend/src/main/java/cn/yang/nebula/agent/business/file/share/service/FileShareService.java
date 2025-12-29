package cn.yang.nebula.agent.business.file.share.service;

import cn.yang.common.data.structure.exception.BusinessException;
import cn.yang.common.data.structure.utils.bean.BeanConvertUtils;
import cn.yang.common.data.structure.vo.page.PageResult;
import cn.yang.nebula.agent.business.authentication.entity.UserInfo;
import cn.yang.nebula.agent.business.authentication.facade.AuthenticationFacade;
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
import cn.yang.nebula.agent.enums.CacheSpaceEnum;
import cn.yang.nebula.agent.enums.ErrorStatusCodeEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 分享业务实现层
 *
 * @author QingHai
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FileShareService implements FileShareFacade {

    private final FileShareRepository fileShareRepository;

    private final FileSpaceRepository fileSpaceRepository;

    private final AuthenticationFacade authenticationFacade;

    private final RedissonClient redissonClient;

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

    /**
     * 增加访问次数
     * 使用 Redisson 分布式锁保证并发安全
     *
     * @param id 分享ID
     */
    @Override
    public void incrementVisitCount(String id) {
        String lockKey = CacheSpaceEnum.FILE_SHARE_VISIT_COUNT.getMark() + id;
        RLock lock = redissonClient.getLock(lockKey);

        try {
            // 尝试获取锁，等待 3 秒，锁自动释放时间 10 秒
            boolean acquired = lock.tryLock(3, 10, TimeUnit.SECONDS);
            if (acquired) {
                try {
                    fileShareRepository.incrementVisitCount(id);
                } finally {
                    lock.unlock();
                }
            } else {
                throw new BusinessException(ErrorStatusCodeEnum.SYSTEM_BUSY, "系统繁忙，请稍后重试");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new BusinessException(ErrorStatusCodeEnum.SYSTEM_BUSY, "操作被中断");
        }
    }

    /**
     * 定时任务：检查并更新过期分享状态
     * 每天凌晨 00:00:00 执行
     * 使用分布式锁保证多服务实例不重复执行，不进行重试
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void updateExpiredShareStatus() {
        log.info("[分享过期任务] 开始执行分享过期状态检查任务...");

        String lockKey = CacheSpaceEnum.FILE_SHARE_EXPIRE_TASK.getMark();
        RLock lock = redissonClient.getLock(lockKey);

        // 尝试获取锁，不等待，获取不到立即返回（不重试）
        boolean acquired = lock.tryLock();
        if (!acquired) {
            log.info("[分享过期任务] 获取分布式锁失败，其他服务实例正在执行，本次任务跳过");
            return;
        }

        try {
            log.info("[分享过期任务] 成功获取分布式锁，开始处理过期分享...");

            // 获取当前日期
            LocalDate currentDate = LocalDate.now();

            // 查询需要标记为过期的分享ID列表
            List<String> expiredShareIds = fileShareRepository.selectExpiredShareIds(currentDate);

            if (CollectionUtils.isEmpty(expiredShareIds)) {
                log.info("[分享过期任务] 没有需要标记过期的分享记录");
                return;
            }

            log.info("[分享过期任务] 查询到 {} 条需要标记过期的分享记录，ID列表: {}", expiredShareIds.size(), expiredShareIds);

            // 批量更新过期状态
            int updateCount = fileShareRepository.batchUpdateExpiredStatus(expiredShareIds);
            log.info("[分享过期任务] 成功更新 {} 条分享记录的过期状态", updateCount);

        } finally {
            // 释放锁
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
                log.info("[分享过期任务] 分布式锁已释放");
            }
        }

        log.info("[分享过期任务] 分享过期状态检查任务执行完成");
    }
}
