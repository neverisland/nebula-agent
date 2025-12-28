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
| expireTime | String | 否 | 过期时间 (yyyy-MM-dd, 开启过期必填) |
| fileIds | Array | 否 | 关联文件ID列表 (类型为1必填) |
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
| expireTime | String | 否 | 过期时间 (yyyy-MM-dd) |
| fileIds | Array | 否 | 关联文件ID列表 (全量覆盖) |
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
| data.expireTime | String | 过期时间 |
| data.isExpired | Boolean | 是否已过期 |
| data.visitCount | Integer | 访问次数 |
| data.downloadCount | Integer | 下载次数 |
| data.fileCount | Integer | 文件数量 |

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
| data.records | Array | 数据列表 |
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
