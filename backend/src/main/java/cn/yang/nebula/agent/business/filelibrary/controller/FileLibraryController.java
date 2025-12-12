package cn.yang.nebula.agent.business.filelibrary.controller;

import cn.yang.common.data.structure.enums.StatusCodeEnum;
import cn.yang.common.data.structure.exception.BusinessException;
import cn.yang.common.data.structure.vo.page.PageResult;
import cn.yang.common.data.structure.vo.result.ResultFactory;
import cn.yang.common.data.structure.vo.result.ResultVo;
import cn.yang.nebula.agent.business.filelibrary.dto.FileLibraryPageDto;
import cn.yang.nebula.agent.business.filelibrary.dto.FileLibraryPageVo;
import cn.yang.nebula.agent.business.filelibrary.dto.FileLibraryRenameDto;
import cn.yang.nebula.agent.business.filelibrary.dto.FileLibraryUploadVo;
import cn.yang.nebula.agent.business.filelibrary.facade.FileLibraryFacade;
import cn.yang.nebula.agent.enums.ErrorStatusCodeEnum;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件库接口
 *
 * @author 未见清海
 */
@RestController
@RequestMapping("/file-library")
public class FileLibraryController {

    @Resource
    private FileLibraryFacade fileLibraryFacade;

    /**
     * 上传文件
     *
     * @param file 文件
     * @return 上传结果
     */
    @PostMapping("/upload")
    public ResultVo<FileLibraryUploadVo> upload(@RequestPart("file") MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BusinessException(ErrorStatusCodeEnum.PARAMETER_VERIFICATION_EXCEPTION, "文件不能为空");
        }

        FileLibraryUploadVo fileLibraryUploadVo = fileLibraryFacade.upload(file);
        return ResultFactory.success(StatusCodeEnum.SUCCESS, "upload success", fileLibraryUploadVo);
    }

    /**
     * 分页查询文件
     *
     * @param fileLibraryPageDto 查询条件
     * @return 分页结果
     */
    @PostMapping("/page")
    public ResultVo<PageResult<FileLibraryPageVo>> page(@Validated @RequestBody FileLibraryPageDto fileLibraryPageDto) {
        if (fileLibraryPageDto == null || fileLibraryPageDto.getCurrent() == null || fileLibraryPageDto.getSize() == null) {
            throw new BusinessException(ErrorStatusCodeEnum.PARAMETER_VERIFICATION_EXCEPTION, "分页参数不能为空");
        }

        PageResult<FileLibraryPageVo> pageResult = fileLibraryFacade.page(fileLibraryPageDto);
        return ResultFactory.success(StatusCodeEnum.SUCCESS, "success", pageResult);
    }

    /**
     * 重命名文件
     *
     * @param renameReq 重命名入参
     * @return 是否成功
     */
    @PostMapping("/rename")
    public ResultVo<?> rename(@Validated @RequestBody FileLibraryRenameDto renameReq) {
        fileLibraryFacade.rename(renameReq);
        return ResultFactory.success(StatusCodeEnum.SUCCESS, "重命名成功");
    }

    /**
     * 删除文件
     *
     * @param deleteReq 删除入参
     * @return 是否成功
     */
    @GetMapping("/delete/{id}")
    public ResultVo<?> delete(@PathVariable("id") String id) {
        fileLibraryFacade.delete(id);
        return ResultFactory.success(StatusCodeEnum.SUCCESS, "删除成功");
    }


}

