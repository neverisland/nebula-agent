export interface FileSpaceInsertDto {
    name: string;
    remark?: string;
}

export interface FileSpaceUpdateDto {
    id: string;
    name?: string;
    remark?: string;
}

export interface FileSpacePageQueryDto {
    current: number;
    size: number;
    name?: string;
    createUserId?: string;
}

export interface FileSpaceVo {
    id: string;
    name: string;
    remark: string;
    createTime: string;
}
