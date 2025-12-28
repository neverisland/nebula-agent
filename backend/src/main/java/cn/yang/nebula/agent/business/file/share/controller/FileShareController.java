package cn.yang.nebula.agent.business.file.share.controller;

import cn.yang.common.data.structure.enums.StatusCodeEnum;
import cn.yang.common.data.structure.exception.BusinessException;
import cn.yang.common.data.structure.vo.page.PageResult;
import cn.yang.common.data.structure.vo.result.ResultFactory;
import cn.yang.common.data.structure.vo.result.ResultVo;
import cn.yang.nebula.agent.aop.ParamLog;
import cn.yang.nebula.agent.business.file.share.enums.ShareTypeEnum;
import cn.yang.nebula.agent.business.file.share.facade.FileShareFacade;
import cn.yang.nebula.agent.business.file.share.po.FileShareCreatePo;
import cn.yang.nebula.agent.business.file.share.po.FileSharePageQueryPo;
import cn.yang.nebula.agent.business.file.share.po.FileShareUpdatePo;
import cn.yang.nebula.agent.business.file.share.vo.FileSharePublicVo;
import cn.yang.nebula.agent.business.file.share.vo.FileShareVo;
import cn.yang.nebula.agent.enums.ErrorStatusCodeEnum;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotBlank;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

/**
 * 文件分享 Controller
 *
 * @author 未见清海
 */
@ParamLog
@Validated
@RestController
@RequestMapping("/file-share")
public class FileShareController {

    @Resource
    private FileShareFacade fileShareFacade;

    /**
     * 新增分享
     *
     * @param fileShareCreatePo 创建参数
     * @return 分享ID
     */
    @PostMapping("/create")
    public ResultVo<String> create(@RequestBody @Validated FileShareCreatePo fileShareCreatePo) {
        // 校验数据
        checkCommon(fileShareCreatePo);

        String shareId = fileShareFacade.create(fileShareCreatePo);
        return ResultFactory.success(StatusCodeEnum.SUCCESS, "create success", shareId);
    }

    /**
     * 校验通用的数据
     *
     * @param fileShareCreatePo 基础数据
     */
    private void checkCommon(FileShareCreatePo fileShareCreatePo) {
        // 参数文件数据是否存在校验
        if (!ShareTypeEnum.isExists(fileShareCreatePo.getShareType())) {
            throw new BusinessException(ErrorStatusCodeEnum.PARAMETER_VERIFICATION_EXCEPTION, "分享类型不存在");
        }
        if (ShareTypeEnum.FILE.getType().equals(fileShareCreatePo.getShareType()) && CollectionUtils.isEmpty(fileShareCreatePo.getFileIds())) {
            throw new BusinessException(ErrorStatusCodeEnum.PARAMETER_VERIFICATION_EXCEPTION, "分享文件不能为空");
        } else if (ShareTypeEnum.SPACE.getType().equals(fileShareCreatePo.getShareType()) && !StringUtils.hasLength(fileShareCreatePo.getSpaceId())) {
            throw new BusinessException(ErrorStatusCodeEnum.PARAMETER_VERIFICATION_EXCEPTION, "分享空间不能为空");
        }

        // 校验过期时间是否在今天之前
        if (fileShareCreatePo.getEnableExpire() && fileShareCreatePo.getExpireTime().isBefore(LocalDate.now())) {
            throw new BusinessException(ErrorStatusCodeEnum.PARAMETER_VERIFICATION_EXCEPTION, "分享过期时间不能在今天之前");
        }

        // 校验密码
        if (fileShareCreatePo.getEnablePassword() && !StringUtils.hasLength(fileShareCreatePo.getPassword())) {
            throw new BusinessException(ErrorStatusCodeEnum.PARAMETER_VERIFICATION_EXCEPTION, "密码不能为空");
        }
    }

    /**
     * 修改分享
     *
     * @param fileShareUpdatePo 更新参数
     * @return 成功
     */
    @PostMapping("/update")
    public ResultVo<?> update(@RequestBody @Validated FileShareUpdatePo fileShareUpdatePo) {
        // 校验数据
        checkCommon(fileShareUpdatePo);

        fileShareFacade.update(fileShareUpdatePo);
        return ResultFactory.success(StatusCodeEnum.SUCCESS, "update success");
    }

    /**
     * 删除分享
     *
     * @param id 分享ID
     * @return 成功
     */
    @GetMapping("/delete")
    public ResultVo<?> delete(@RequestParam("id") @NotBlank(message = "分享ID不能为空") String id) {
        fileShareFacade.delete(id);
        return ResultFactory.success(StatusCodeEnum.SUCCESS, "delete success");
    }

    /**
     * 分享详情
     *
     * @param id 分享ID
     * @return 详情
     */
    @GetMapping("/selectById")
    public ResultVo<FileShareVo> detail(@RequestParam("id") @NotBlank(message = "分享ID不能为空") String id) {
        FileShareVo fileShareVo = fileShareFacade.detail(id);
        return ResultFactory.success(StatusCodeEnum.SUCCESS, "success", fileShareVo);
    }

    /**
     * 分页查询
     *
     * @param pageQuery 查询参数
     * @return 分页结果
     */
    @PostMapping("/page")
    public ResultVo<PageResult<FileShareVo>> page(@RequestBody @Validated FileSharePageQueryPo pageQuery) {
        PageResult<FileShareVo> shareVoPageResult = fileShareFacade.page(pageQuery);
        return ResultFactory.success(StatusCodeEnum.SUCCESS, "success", shareVoPageResult);
    }

    /**
     * 公开访问获取分享信息
     *
     * @param shareId 分享ID
     * @return 分享信息
     */
    @GetMapping("/public/info")
    public ResultVo<FileSharePublicVo> getPublicInfo(@RequestParam("shareId") String shareId) {
        FileSharePublicVo fileSharePublicVo = fileShareFacade.getPublicInfo(shareId);
        return ResultFactory.success(StatusCodeEnum.SUCCESS, "success", fileSharePublicVo);
    }
}
