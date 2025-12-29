
export interface MockFileItem {
    id: string;
    name: string;
    size: number; // bytes
    fileType: string; // 'image', 'document', 'video', 'other'
    thumbnailUrl?: string; // for images
    updateTime: string;
}

// 模拟文件列表数据
export const getMockFileList = (): MockFileItem[] => {
    return [
        {
            id: '1',
            name: '项目演示截图.png',
            size: 1024 * 1024 * 2.5,
            fileType: 'image',
            thumbnailUrl: 'https://cdn.pixabay.com/photo/2024/02/05/16/23/labrador-8554882_1280.jpg',
            updateTime: '2025-01-02 10:00:00'
        },
        {
            id: '2',
            name: '需求文档.pdf',
            size: 1024 * 500,
            fileType: 'document',
            thumbnailUrl: '',
            updateTime: '2025-01-01 14:30:00'
        },
        {
            id: '3',
            name: '设计原稿.fig',
            size: 1024 * 1024 * 15,
            fileType: 'other',
            thumbnailUrl: '',
            updateTime: '2024-12-30 09:15:00'
        },
        {
            id: '4',
            name: '风景照.jpg',
            size: 1024 * 1024 * 4.2,
            fileType: 'image',
            thumbnailUrl: 'https://cdn.pixabay.com/photo/2023/11/01/11/13/landscape-8357116_1280.jpg',
            updateTime: '2025-01-03 11:20:00'
        },
        {
            id: '5',
            name: '会议记录.docx',
            size: 1024 * 25,
            fileType: 'document',
            thumbnailUrl: '',
            updateTime: '2024-12-29 16:45:00'
        }
    ];
};

// 模拟状态控制 (用于调试)
export const mockState = {
    // 强制模拟过期状态 (调试用)
    forceExpired: false,
    // 强制模拟密码锁定 (调试用)
    forceLocked: false,
    // 正确密码 (模拟)
    correctPassword: '123'
};
