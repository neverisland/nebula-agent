# 文件库 (FileLibraryController)

## 1. 上传文件

**URL**: `/file-library/upload`
**Method**: `POST` (multipart/form-data)

### 请求参数

| 参数名 | 类型 | 必填 | 描述 |
| :--- | :--- | :--- | :--- |
| file | File | 是 | 待上传的文件 |

### 响应参数

| 字段名 | 类型 | 描述 |
| :--- | :--- | :--- |
| code | Number | 状态码（0 成功） |
| msg | String | 提示信息 |
| data | Object | 上传结果 |
| data.id | String | 文件ID |
| data.name | String | 文件名称 |
| data.url | String | 文件访问地址 |
| data.thumbnailsUrl | String | 缩略图访问地址（可为空） |

### 响应示例

```json
{
  "code": 0,
  "msg": "上传成功",
  "data": {
    "id": "1",
    "name": "demo.jpg",
    "url": "/file/2025/12/11/xxxxx.jpg",
    "thumbnailsUrl": null
  }
}
```

---

## 2. 分页查询

**URL**: `/file-library/page`
**Method**: `POST`

### 请求参数

| 参数名 | 类型 | 必填 | 描述 |
| :--- | :--- | :--- | :--- |
| current | Number | 是 | 当前页 |
| size | Number | 是 | 每页大小 |
| searchText | String | 否 | 名称或路径模糊匹配 |
| mimeType | String | 否 | MIME 前缀过滤，如 `image/` |
| spaceId | String | 否 | 空间ID（预留） |

### 响应参数

| 字段名 | 类型 | 描述 |
| :--- | :--- | :--- |
| code | Number | 状态码（0 成功） |
| msg | String | 提示信息 |
| data | Object | 分页数据 |
| data.records | Array | 数据列表 |
| data.total | Number | 总数 |
| data.records[].id | String | 主键 |
| data.records[].name | String | 文件名 |
| data.records[].mimeType | String | 类型 |
| data.records[].size | Number | 大小（字节） |
| data.records[].url | String | 文件访问地址 |
| data.records[].thumbnailsUrl | String | 缩略图访问地址 |
| data.records[].createTime | String | 上传时间 |

### 响应示例

```json
{
  "code": 0,
  "msg": "success",
  "data": {
    "records": [
      {
        "id": "1",
        "name": "demo.jpg",
        "mimeType": "image/jpeg",
        "size": 10240,
        "url": "2025/12/11/xxxxx.jpg",
        "thumbnailsUrl": null,
        "createTime": "2025-12-11 10:00:00"
      }
    ],
    "total": 1,
    "current": 1,
    "size": 10
  }
}
```

---

## 3. 重命名

**URL**: `/file-library/rename`
**Method**: `POST`

### 请求参数

| 参数名 | 类型 | 必填 | 描述 |
| :--- | :--- | :--- | :--- |
| id | String | 是 | 文件ID |
| name | String | 是 | 新文件名 |

### 响应参数

| 字段名 | 类型 | 描述 |
| :--- | :--- | :--- |
| code | Number | 状态码（0 成功） |
| msg | String | 提示信息 |
| data | null | - |

---

## 4. 删除

**URL**: `/file-library/delete/{id}`
**Method**: `GET`

### 请求参数

| 参数名 | 类型 | 必填 | 描述 |
| :--- | :--- | :--- | :--- |
| id | String | 是 | 文件ID（路径参数）|

### 响应参数

| 字段名 | 类型 | 描述 |
| :--- | :--- | :--- |
| code | Number | 状态码（0 成功） |
| msg | String | 提示信息 |
| data | null | - |

---

## 5. 下载/预览

**URL**: `/file/{yyyy}/{MM}/{dd}/{filename}`  
**Method**: `GET`

### 请求参数

- 路径参数直接携带相对路径，如 `/file/2025/12/11/xxxxx.jpg`。

### 响应参数

- 返回文件流，`Content-Type` 为文件 MIME。


