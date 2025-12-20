package cn.yang.nebula.agent.business.file.space.controller;

import cn.yang.common.data.structure.enums.StatusCodeEnum;
import cn.yang.common.data.structure.exception.BusinessException;
import cn.yang.common.data.structure.vo.page.PageResult;
import cn.yang.common.data.structure.vo.result.ResultFactory;
import cn.yang.common.data.structure.vo.result.ResultVo;
import cn.yang.nebula.agent.aop.ParamLog;
import cn.yang.nebula.agent.enums.ErrorStatusCodeEnum;
import cn.yang.nebula.agent.business.file.space.dto.FileSpaceAllocateDto;
import cn.yang.nebula.agent.business.file.space.dto.FileSpaceInsertDto;
import cn.yang.nebula.agent.business.file.space.dto.FileSpacePageQueryDto;
import cn.yang.nebula.agent.business.file.space.dto.FileSpaceUpdateDto;
import cn.yang.nebula.agent.business.file.space.facade.FileSpaceFacade;
import cn.yang.nebula.agent.business.file.space.vo.FileSpaceVo;
import cn.yang.nebula.agent.business.file.space.vo.FileSpaceSelectVo;
import cn.yang.nebula.agent.business.file.space.vo.FileSpaceStatisticsVo;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

/**
 * @author : 未见清海
 */
@ParamLog
@Validated
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

    /**
     * 删除个人空间
     *
     * @param id id
     * @return 成功
     */
    @GetMapping("/delete")
    public ResultVo<?> delete(@RequestParam("id") @NotBlank(message = "空间id不能为空") String id) {
        fileSpaceFacade.deleteFileSpace(id);
        return ResultFactory.success(StatusCodeEnum.SUCCESS, "删除成功");
    }

    /**
     * 分配文件至个人空间
     *
     * @param allocateDto 分配入参
     * @return 成功
     */
    @PostMapping("/allocate")
    public ResultVo<?> allocate(@RequestBody @Validated FileSpaceAllocateDto allocateDto) {
        fileSpaceFacade.allocateFilesToSpace(allocateDto);
        return ResultFactory.success(StatusCodeEnum.SUCCESS, "分配成功");
    }

    /**
     * 获取当前用户的所有的文件空间列表
     *
     * @return 列表
     */
    @GetMapping("/selectFileSpaces")
    public ResultVo<List<FileSpaceSelectVo>> selectFileSpaces() {
        List<FileSpaceSelectVo> list = fileSpaceFacade.selectFileSpaces();
        return ResultFactory.success(StatusCodeEnum.SUCCESS, "查询成功", list);
    }

    /**
     * 获取统计数据
     *
     * @return 统计数据
     */
    @GetMapping("/statistics")
    public ResultVo<FileSpaceStatisticsVo> getStatistics() {
        FileSpaceStatisticsVo statistics = fileSpaceFacade.getFileSpaceStatistics();
        return ResultFactory.success(StatusCodeEnum.SUCCESS, "查询成功", statistics);
    }
}
