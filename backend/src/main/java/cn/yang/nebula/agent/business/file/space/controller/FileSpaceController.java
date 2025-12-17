package cn.yang.nebula.agent.business.file.space.controller;

import cn.yang.common.data.structure.enums.StatusCodeEnum;
import cn.yang.common.data.structure.exception.BusinessException;
import cn.yang.common.data.structure.vo.page.PageResult;
import cn.yang.common.data.structure.vo.result.ResultFactory;
import cn.yang.common.data.structure.vo.result.ResultVo;
import cn.yang.nebula.agent.aop.ParamLog;
import cn.yang.nebula.agent.enums.ErrorStatusCodeEnum;
import cn.yang.nebula.agent.business.file.space.dto.FileSpaceInsertDto;
import cn.yang.nebula.agent.business.file.space.dto.FileSpacePageQueryDto;
import cn.yang.nebula.agent.business.file.space.dto.FileSpaceUpdateDto;
import cn.yang.nebula.agent.business.file.space.facade.FileSpaceFacade;
import cn.yang.nebula.agent.business.file.space.vo.FileSpaceVo;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : 未见清海
 */
@ParamLog
@RestController
@RequestMapping("/file-space")
@RequiredArgsConstructor
public class FileSpaceController {

    private final FileSpaceFacade fileSpaceFacade;

    /**
     * 新增个人空间
     *
     * @param fileSpaceInsertDto 新增入参
     * @return id
     */
    @PostMapping("/add")
    public ResultVo<String> add(@RequestBody @Validated FileSpaceInsertDto fileSpaceInsertDto) {
        String id = fileSpaceFacade.insertFileSpace(fileSpaceInsertDto);
        return ResultFactory.success(StatusCodeEnum.SUCCESS, "新增成功", id);
    }

    /**
     * 更新个人空间
     *
     * @param fileSpaceUpdateDto 更新入参
     * @return 成功
     */
    @PostMapping("/update")
    public ResultVo<?> update(@RequestBody @Validated FileSpaceUpdateDto fileSpaceUpdateDto) {
        fileSpaceFacade.updateFileSpace(fileSpaceUpdateDto);
        return ResultFactory.success(StatusCodeEnum.SUCCESS, "查询成功");
    }

    /**
     * 分页查询
     *
     * @param query 查询条件
     * @return 分页结果
     */
    @PostMapping("/page")
    public ResultVo<PageResult<FileSpaceVo>> page(@RequestBody @Validated FileSpacePageQueryDto query) {
        if (query.getCurrent() == null || query.getSize() == null) {
            throw new BusinessException(ErrorStatusCodeEnum.PARAMETER_VERIFICATION_EXCEPTION, "分页查询数据不能为空");
        }
        PageResult<FileSpaceVo> page = fileSpaceFacade.page(query);
        return ResultFactory.success(StatusCodeEnum.SUCCESS, "查询成功", page);
    }
}
