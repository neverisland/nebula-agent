# 文件分享管理 (FileShareController)

## 1. 新增分享

**URL**: `/file-share/create`
**Method**: `POST`

### 请求参数

| 参数名 | 类型 | 必填 | 描述 |
| :--- | :--- | :--- | :--- |
| shareType | Integer | 是 | 分享类型: 1-个人文件, 2-个人空间 |
| name | String | 是 | 分享名称 |
| enablePassword | Boolean | 是 | 是否开启密码访问 |
| password | String | 否 | 访问密码 (开启密码必填) |
| enableExpire | Boolean | 是 | 是否开启过期时间 |
| expireTime | String | 否 | 过期时间 (格式: yyyy-MM-dd, 开启过期必填) |
| fileIds | Array<String> | 否 | 关联文件ID列表 (类型为1必填) |
| spaceId | String | 否 | 关联空间ID (类型为2必填) |

### 响应参数

| 字段名 | 类型 | 描述 |
| :--- | :--- | :--- |
| code | Number | 状态码 (200 成功) |
| msg | String | 消息 |
| data | String | 分享ID |

### 响应示例

```json
{
  "code": 200,
  "msg": "success",
  "data": "1f8e..."
}
```

## 2. 修改分享

**URL**: `/file-share/update`
**Method**: `POST`

### 请求参数

| 参数名 | 类型 | 必填 | 描述 |
| :--- | :--- | :--- | :--- |
| id | String | 是 | 分享ID |
| name | String | 是 | 分享名称 |
| enablePassword | Boolean | 是 | 是否开启密码访问 |
| password | String | 否 | 访问密码 (若需修改则填) |
| enableExpire | Boolean | 是 | 是否开启过期时间 |
| expireTime | String | 否 | 过期时间 (格式: yyyy-MM-dd) |
| fileIds | Array<String> | 否 | 关联文件ID列表 (全量覆盖) |
| spaceId | String | 否 | 关联空间ID |

### 响应参数

| 字段名 | 类型 | 描述 |
| :--- | :--- | :--- |
| code | Number | 状态码 |
| msg | String | 消息 |
| data | Null | 无 |

## 3. 删除分享

**URL**: `/file-share/delete`
**Method**: `GET`

### 请求参数

| 参数名 | 类型 | 必填 | 描述 |
| :--- | :--- | :--- | :--- |
| id | String | 是 | 分享ID |

### 响应参数

| 字段名 | 类型 | 描述 |
| :--- | :--- | :--- |
| code | Number | 状态码 |
| msg | String | 消息 |
| data | Null | 无 |

## 4. 分享详情 (管理端)

**URL**: `/file-share/selectById`
**Method**: `GET`

### 请求参数

| 参数名 | 类型 | 必填 | 描述 |
| :--- | :--- | :--- | :--- |
| id | String | 是 | 分享ID |

### 响应参数

| 字段名 | 类型 | 描述 |
| :--- | :--- | :--- |
| code | Number | 状态码 |
| msg | String | 消息 |
| data | Object | 详情对象 |
| data.id | String | 分享ID |
| data.shareType | Integer | 类型 |
| data.name | String | 名称 |
| data.shareUrl | String | 分享链接 |
| data.enablePassword | Boolean | 是否开启密码 |
| data.enableExpire | Boolean | 是否开启过期 |
| data.expireTime | String | 过期时间 (yyyy-MM-dd) |
| data.isExpired | Boolean | 是否已过期 |
| data.visitCount | Integer | 访问次数 |
| data.downloadCount | Integer | 下载次数 |
| data.fileCount | Integer | 文件数量 |
| data.fileSpaceVo | Object | 空间信息 (如果是空间分享) |

## 5. 分页查询分享

**URL**: `/file-share/page`
**Method**: `POST`

### 请求参数

| 参数名 | 类型 | 必填 | 描述 |
| :--- | :--- | :--- | :--- |
| pageNo | Integer | 是 | 页码 |
| pageSize | Integer | 是 | 页大小 |
| name | String | 否 | 搜索名称 |
| shareType | Integer | 否 | 类型筛选 |
| isExpired | Boolean | 否 | 状态筛选 |

### 响应参数

| 字段名 | 类型 | 描述 |
| :--- | :--- | :--- |
| code | Number | 状态码 |
| msg | String | 消息 |
| data | Object | 分页结果 |
| data.records | Array | 数据列表 (结构同详情) |
| data.total | Number | 总数 |

## 6. 公开访问信息

**URL**: `/file-share/public/info`
**Method**: `GET`

### 请求参数

| 参数名 | 类型 | 必填 | 描述 |
| :--- | :--- | :--- | :--- |
| shareId | String | 是 | 分享ID |

### 响应参数

| 字段名 | 类型 | 描述 |
| :--- | :--- | :--- |
| code | Number | 状态码 |
| msg | String | 消息 |
| data | Object | 脱敏后的分享信息 |
| data.id | String | 分享ID |
| data.shareType | Integer | 类型 |
| data.name | String | 名称 |
| data.enablePassword | Boolean | 是否有密码 |
| data.enableExpire | Boolean | 是否有过期时间 |
| data.expireTime | String | 过期时间 |
| data.isExpired | Boolean | 是否已过期 |
| data.visitCount | Integer | 访问次数 |
| data.downloadCount | Integer | 下载次数 |
| data.createTime | String | 创建时间 |
| data.fileCount | Integer | 关联文件数量 |
| data.createUserName | String | 创建人名称 |
| data.fileSpaceVo | Object | 文件空间信息（如果是空间分享） |

---

## 7. 增加访问次数（公开接口）

**URL**: `/file-share/public/incrementVisit`
**Method**: `GET`

### 请求参数

| 参数名 | 类型 | 必填 | 描述 |
| :--- | :--- | :--- | :--- |
| id | String | 是 | 分享ID |

### 响应参数

| 字段名 | 类型 | 描述 |
| :--- | :--- | :--- |
| code | Number | 状态码 |
| msg | String | 消息 |
| data | Null | 无 |

---

## 8. 验证分享密码（公开接口）

**URL**: `/file-share/public/verifyPassword`
**Method**: `POST`

### 请求参数

| 参数名 | 类型 | 必填 | 描述 |
| :--- | :--- | :--- | :--- |
| shareId | String | 是 | 分享ID |
| password | String | 是 | 访问密码 |

### 请求示例

```json
{
  "shareId": "1f8e...",
  "password": "123456"
}
```

### 响应参数

| 字段名 | 类型 | 描述 |
| :--- | :--- | :--- |
| code | Number | 状态码（0 成功） |
| msg | String | 消息 |
| data | Null | 无 |

### 响应示例

```json
{
  "code": 0,
  "msg": "密码验证成功",
  "data": null
}
```

### 说明

1. **验证规则**：
   - 分享必须存在
   - 分享必须设置了密码（`enablePassword` 为 `true`），如果未设置密码，返回错误："该分享未设置密码"
   - 提供的密码必须与分享的密码完全匹配，否则返回错误："密码错误"
   - 只有分享设置了密码且密码正确时，才返回验证成功

2. **使用场景**：
   - 前端在用户输入密码后，可以先调用此接口验证密码是否正确
   - 验证成功后，可以继续访问分享内容或下载文件

### 错误响应

如果验证失败，返回 JSON 格式错误信息：

```json
{
  "code": 400,
  "msg": "该分享未设置密码",
  "data": null
}
```

或

```json
{
  "code": 400,
  "msg": "密码错误",
  "data": null
}
```

常见错误：
- `分享记录不存在`：分享ID无效
- `该分享未设置密码`：分享未启用密码保护
- `密码错误`：提供的密码不正确

---

## 9. 获取文件列表（公开接口）

**URL**: `/file-share/public/fileList`
**Method**: `POST`

### 请求参数

| 参数名 | 类型 | 必填 | 描述 |
| :--- | :--- | :--- | :--- |
| shareId | String | 是 | 分享ID |
| password | String | 是 | 访问密码（如果分享设置了密码则必填） |

### 请求示例

```json
{
  "shareId": "1f8e...",
  "password": "123456"
}
```

### 响应参数

| 字段名 | 类型 | 描述 |
| :--- | :--- | :--- |
| code | Number | 状态码（0 成功） |
| msg | String | 消息 |
| data | Array | 文件列表 |
| data[].id | String | 文件ID |
| data[].name | String | 文件名 |
| data[].mimeType | String | 文件类型 |
| data[].size | Number | 文件大小（字节） |
| data[].url | String | 文件访问地址 |
| data[].thumbnailsUrl | String | 缩略图访问地址（可为空） |
| data[].createTime | String | 上传时间 |
| data[].spaceId | String | 空间ID（可为空） |

### 响应示例

```json
{
  "code": 0,
  "msg": "success",
  "data": [
    {
      "id": "1",
      "name": "demo.jpg",
      "mimeType": "image/jpeg",
      "size": 10240,
      "url": "http://127.0.0.1:9000/file/2025/12/11/xxxxx.jpg",
      "thumbnailsUrl": "http://127.0.0.1:9000/file/2025/12/11/xxxxx_thumb.jpg",
      "createTime": "2025-12-11 10:00:00",
      "spaceId": null
    }
  ]
}
```

### 说明

1. **验证规则**：
   - 分享必须存在
   - 分享不能已过期（`isExpired` 为 `true`）
   - 如果分享设置了密码（`enablePassword` 为 `true`），必须提供正确的密码

2. **文件获取**：
   - 文件类型分享（`shareType = 1`）：根据分享关联的 `fileIds` 查询文件列表
   - 空间类型分享（`shareType = 2`）：根据分享关联的 `spaceId` 查询空间下的所有文件

3. **返回数据**：
   - 返回文件列表，包含文件的完整信息（ID、名称、类型、大小、访问地址等）
   - 如果分享没有文件，返回空数组

### 错误响应

如果验证失败，返回 JSON 格式错误信息：

```json
{
  "code": 400,
  "msg": "分享已过期",
  "data": null
}
```

常见错误：
- `分享记录不存在`：分享ID无效
- `分享已过期`：分享已过期
- `请输入密码`：分享设置了密码但未提供密码
- `密码错误`：提供的密码不正确

---

## 10. 下载全部文件（公开接口）

**URL**: `/file-share/public/downloadAll`
**Method**: `GET`

### 请求参数

| 参数名 | 类型 | 必填 | 描述 |
| :--- | :--- | :--- | :--- |
| shareId | String | 是 | 分享ID |
| password | String | 否 | 访问密码（如果分享设置了密码则必填） |

### 响应参数

- 返回 ZIP 压缩文件流
- `Content-Type`: `application/zip`
- `Content-Disposition`: `attachment; filename*=UTF-8''{分享名称}.zip`

### 说明

1. **验证规则**：
   - 分享必须存在
   - 分享不能已过期（`isExpired` 为 `true`）
   - 如果分享设置了密码（`enablePassword` 为 `true`），必须提供正确的密码

2. **文件获取**：
   - 文件类型分享（`shareType = 1`）：根据分享关联的 `fileIds` 查询文件列表
   - 空间类型分享（`shareType = 2`）：根据分享关联的 `spaceId` 查询空间下的所有文件

3. **下载方式**：
   - 使用流式压缩，一边从 OSS 读取文件流，一边压缩，一边输出给客户端
   - 避免占用服务器内存，支持大文件下载

4. **下载统计**：
   - 下载成功后，会自动更新分享的下载次数（`downloadCount` +1）
   - 使用分布式锁保证并发安全

### 错误响应

如果验证失败，返回 JSON 格式错误信息：

```json
{
  "code": 400,
  "msg": "分享已过期",
  "data": null
}
```

常见错误：
- `分享记录不存在`：分享ID无效
- `分享已过期`：分享已过期
- `请输入密码`：分享设置了密码但未提供密码
- `密码错误`：提供的密码不正确
- `分享文件列表为空`：文件类型分享没有关联文件
- `没有可下载的文件`：查询不到任何文件