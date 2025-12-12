export interface FileLibraryPageVo {
  id: string
  name: string
  mimeType: string
  size: number
  url: string
  thumbnailsUrl?: string | null
  createTime?: string
}

export interface FileLibraryPageDto {
  current: number
  size: number
  searchText?: string
  mimeType?: string
  spaceId?: string
}

export interface FileLibraryUploadVo {
  id: string
  name: string
  url: string
  thumbnailsUrl?: string | null
}

