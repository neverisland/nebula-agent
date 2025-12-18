# 个人空间 (FileSpaceController)

## 1. 新增个人空间

**URL**: `/file-space/add`
**Method**: `POST`
**Description**: 新增个人空间

### 请求参数 (FileSpaceInsertDto)

| 参数名 | 类型 | 必填 | 描述 |
| :--- | :--- | :--- | :--- |
| name | String | 是 | 个人空间名称 |
| remark | String | 否 | 备注 |

### 响应参数 (ResultVo<String>)

| 字段名 | 类型 | 描述 |
| :--- | :--- | :--- |
| data | String | 新增的个人空间 ID |

## 2. 更新个人空间

**URL**: `/file-space/update`
**Method**: `POST`
**Description**: 更新个人空间

### 请求参数 (FileSpaceUpdateDto)

| 参数名 | 类型 | 必填 | 描述 |
| :--- | :--- | :--- | :--- |
| id | String | 是 | 个人空间 ID |
| name | String | 否 | 个人空间名称 |
| remark | String | 否 | 备注 |

### 响应参数 (ResultVo<String>)

| data | String | null |

## 3. 分页查询个人空间

**URL**: `/file-space/page`
**Method**: `POST`
**Description**: 分页查询个人空间(支持模糊搜索名字)

### 请求参数 (FileSpacePageQueryDto)

| 参数名 | 类型 | 必填 | 描述 |
| :--- | :--- | :--- | :--- |
| current | Integer | 是 | 当前页 |
| size | Integer | 是 | 每页条数 |
| name | String | 否 | 个人空间名称(模糊查询) |

### 响应参数 (ResultVo<PageResult<FileSpaceVo>>)

| 字段名 | 类型 | 描述 |
| :--- | :--- | :--- |
| total | long | 总条数 |
| list | List<FileSpaceVo> | 数据列表 |

#### FileSpaceVo 结构

| 字段名 | 类型 | 描述 |
| :--- | :--- | :--- |
| id | String | ID |
| name | String | 名称 |
| remark | String | 备注 |
| createTime | Date | 创建时间 |

## 4. 删除个人空间

**URL**: `/file-space/delete`
**Method**: `GET`
**Description**: 删除个人空间（仅限创建人）

### 请求参数

| 参数名 | 类型 | 必填 | 描述 |
| :--- | :--- | :--- | :--- |
| id | String | 是 | 个人空间 ID |

### 响应参数 (ResultVo)

| 字段名 | 类型 | 描述 |
| :--- | :--- | :--- |
| code | Integer | 状态码 (200 成功) |
| msg | String | 消息 |

## 5. 分配文件至个人空间

**URL**: `/file-space/allocate`
**Method**: `POST`
**Description**: 分配文件至个人空间（校验空间归属，批量更新文件关联）

### 请求参数 (FileSpaceAllocateDto)

| 参数名 | 类型 | 必填 | 描述 |
| :--- | :--- | :--- | :--- |
| spaceId | String | 是 | 个人空间 ID |
| fileIds | List<String> | 是 | 文件 ID 列表 |

### 响应参数 (ResultVo)

| 字段名 | 类型 | 描述 |
| :--- | :--- | :--- |
| code | Integer | 状态码 (200 成功) |
| msg | String | 消息 |

## 6. 获取当前用户的文件空间列表

**URL**: `/file-space/selectFileSpaces`
**Method**: `GET`
**Description**: 获取当前用户的所有文件空间列表（用于下拉选择）

### 请求参数
无

### 响应参数 (ResultVo<List<FileSpaceSelectVo>>)

| 字段名 | 类型 | 描述 |
| :--- | :--- | :--- |
| data | List<FileSpaceSelectVo> | 空间列表 |

#### FileSpaceSelectVo 结构

| 字段名 | 类型 | 描述 |
| :--- | :--- | :--- |
| id | String | ID |
| name | String | 名称 |
