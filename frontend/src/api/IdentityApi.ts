import {ResultVo} from "../type/ResultVo.ts";
import {AxiosResponse} from "axios";


type ConfigType<T = ResultVo> = Promise<AxiosResponse<T>>

// /**
//  * 查询角色分页列表接口
//  * @param data 查询入参
//  */
// export const assigningRoles = (data: SysRoleQueryDto): ConfigType<ResultVo<PageResult<SysRolePageDto[]>>> => {
//     return request({
//         url: "/role/page",
//         method: 'POST',
//         data: data
//     });
// }