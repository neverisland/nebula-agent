import request from "@/utils/request";
import type { HomeStatisticsVo } from "@/type/home/HomeStatisticsVo";
import type { ResultVo } from "@/type/ResultVo";
import { AxiosResponse } from "axios";

/**
 * 主页接口
 */

/**
 * 获取主页统计数据
 * @returns Promise<AxiosResponse<ResultVo<HomeStatisticsVo>>> 统计数据
 */
export const getHomeStatistics = (): Promise<AxiosResponse<ResultVo<HomeStatisticsVo>>> => {
    return request({
        url: '/home/statistics',
        method: 'get',
    });
}
