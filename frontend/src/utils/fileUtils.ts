/**
 * 文件相关工具方法
 */

/**
 * 判断是否为图片类型
 * @param mimeType MIME类型
 * @returns 是否为图片
 */
export function isImage(mimeType: string): boolean {
  return !!(mimeType && mimeType.startsWith('image/'));
}

/**
 * 根据 MIME 类型获取对应的文件图标路径
 * @param mimeType MIME类型
 * @returns 图标路径
 */
export function getFileIcon(mimeType: string): string {
  if (!mimeType) return '/file-icon/file.png';

  // 根据 MIME 类型映射到对应的图标
  const mimeIconMap: Record<string, string> = {
    // 图片类型
    'image/': '/file-icon/image.png',
    // 视频类型
    'video/': '/file-icon/video.png',
    // 音频类型
    'audio/': '/file-icon/mp3.png',
    // 文档类型
    'application/pdf': '/file-icon/pdf.png',
    'application/msword': '/file-icon/file.png',
    'application/vnd.openxmlformats-officedocument.wordprocessingml.document': '/file-icon/file.png',
    'application/vnd.ms-powerpoint': '/file-icon/ppt.png',
    'application/vnd.openxmlformats-officedocument.presentationml.presentation': '/file-icon/ppt.png',
    'application/vnd.ms-excel': '/file-icon/file.png',
    'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet': '/file-icon/file.png',
    // 压缩文件
    'application/zip': '/file-icon/zip.png',
    'application/x-zip-compressed': '/file-icon/zip.png',
    'application/x-rar-compressed': '/file-icon/rar.png',
    'application/rar': '/file-icon/rar.png',
    // 可执行文件
    'application/x-msdownload': '/file-icon/exe.png',
    'application/octet-stream': '/file-icon/file.png',
    // 文本文件
    'text/': '/file-icon/txt.png',
    'application/json': '/file-icon/txt.png',
    'application/javascript': '/file-icon/txt.png',
    'application/xml': '/file-icon/txt.png',
  };

  // 优先匹配具体的 MIME 类型
  if (mimeIconMap[mimeType]) {
    return mimeIconMap[mimeType];
  }

  // 其次匹配 MIME 类型前缀
  for (const [prefix, icon] of Object.entries(mimeIconMap)) {
    if (prefix.endsWith('/') && mimeType.startsWith(prefix)) {
      return icon;
    }
  }

  // 默认图标
  return '/file-icon/file.png';
}

/**
 * 根据文件名判断是否为图片类型
 * @param filename 文件名
 * @returns 是否为图片
 */
export function isImageByFilename(filename: string): boolean {
  if (!filename) return false;
  const imageExtensions = ['.jpg', '.jpeg', '.png', '.gif', '.bmp', '.webp', '.svg', '.ico'];
  const ext = filename.toLowerCase().substring(filename.lastIndexOf('.'));
  return imageExtensions.includes(ext);
}

/**
 * 根据文件名获取对应的文件图标路径
 * @param filename 文件名
 * @returns 图标路径
 */
export function getFileIconByFilename(filename: string): string {
  if (!filename) return '/file-icon/file.png';

  const ext = filename.toLowerCase().substring(filename.lastIndexOf('.'));

  // 根据文件扩展名映射到对应的图标
  const extensionIconMap: Record<string, string> = {
    // 图片类型
    '.jpg': '/file-icon/image.png',
    '.jpeg': '/file-icon/image.png',
    '.png': '/file-icon/image.png',
    '.gif': '/file-icon/image.png',
    '.bmp': '/file-icon/image.png',
    '.webp': '/file-icon/image.png',
    '.svg': '/file-icon/image.png',
    '.ico': '/file-icon/image.png',

    // 视频类型
    '.mp4': '/file-icon/video.png',
    '.avi': '/file-icon/video.png',
    '.mkv': '/file-icon/video.png',
    '.mov': '/file-icon/video.png',
    '.wmv': '/file-icon/video.png',
    '.flv': '/file-icon/video.png',

    // 音频类型
    '.mp3': '/file-icon/mp3.png',
    '.wav': '/file-icon/mp3.png',
    '.flac': '/file-icon/mp3.png',
    '.aac': '/file-icon/mp3.png',
    '.ogg': '/file-icon/mp3.png',

    // 文档类型
    '.pdf': '/file-icon/pdf.png',
    '.doc': '/file-icon/file.png',
    '.docx': '/file-icon/file.png',
    '.ppt': '/file-icon/ppt.png',
    '.pptx': '/file-icon/ppt.png',
    '.xls': '/file-icon/file.png',
    '.xlsx': '/file-icon/file.png',

    // 压缩文件
    '.zip': '/file-icon/zip.png',
    '.rar': '/file-icon/rar.png',
    '.7z': '/file-icon/zip.png',
    '.tar': '/file-icon/zip.png',
    '.gz': '/file-icon/zip.png',

    // 可执行文件
    '.exe': '/file-icon/exe.png',
    '.msi': '/file-icon/exe.png',
    '.dmg': '/file-icon/exe.png',

    // 文本文件
    '.txt': '/file-icon/txt.png',
    '.json': '/file-icon/txt.png',
    '.js': '/file-icon/txt.png',
    '.css': '/file-icon/txt.png',
    '.html': '/file-icon/txt.png',
    '.xml': '/file-icon/txt.png',
    '.md': '/file-icon/txt.png',
  };

  return extensionIconMap[ext] || '/file-icon/file.png';
}

/**
 * 格式化文件大小
 * @param size 文件大小（字节）
 * @returns 格式化后的文件大小字符串
 */
export function formatSize(size: number): string {
  if (size < 1024) return `${size} B`;
  if (size < 1024 * 1024) return `${(size / 1024).toFixed(1)} KB`;
  if (size < 1024 * 1024 * 1024) return `${(size / 1024 / 1024).toFixed(1)} MB`;
  return `${(size / 1024 / 1024 / 1024).toFixed(1)} GB`;
}
