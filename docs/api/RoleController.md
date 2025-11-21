# 角色管理 (RoleController)

## 1. 角色分页查询

**URL**: `/role/page`
**Method**: `POST`
**Description**: 分页查询角色数据

### 请求参数 (RolePageQuery)

| 参数名 | 类型 | 必填 | 描述 |
| :--- | :--- | :--- | :--- |
| current | Long | 是 | 当前页码 |
| size | Long | 是 | 每页条数 |
| roleName | String | 否 | 角色名称 |
| roleMark | String | 否 | 角色标识 |

### 响应参数 (ResultVo<PageResult<RolePageDto>>)

| 字段名 | 类型 | 描述 |
| :--- | :--- | :--- |
| data | Object | 分页结果 |
| data.records | Array | 角色列表 |

## 2. 新增自定义角色

**URL**: `/role/insert`
**Method**: `POST`
**Description**: 新增自定义角色

### 请求参数 (RoleInsertDto)

| 参数名 | 类型 | 必填 | 描述 |
| :--- | :--- | :--- | :--- |
| roleName | String | 是 | 角色名称 |
| roleMark | String | 是 | 角色标识 |
| remark | String | 否 | 备注 |

### 响应参数 (ResultVo<String>)

| 字段名 | 类型 | 描述 |
| :--- | :--- | :--- |
| data | String | 新增角色的 ID |

## 3. 修改自定义角色

**URL**: `/role/update`
**Method**: `POST`
**Description**: 修改自定义角色

### 请求参数 (RoleUpdateDto)

| 参数名 | 类型 | 必填 | 描述 |
| :--- | :--- | :--- | :--- |
| id | String | 是 | 角色 ID |
| roleName | String | 是 | 角色名称 |
| roleMark | String | 是 | 角色标识 |
| remark | String | 否 | 备注 |

### 响应参数 (ResultVo<String>)

| 字段名 | 类型 | 描述 |
| :--- | :--- | :--- |
| data | String | 提示信息 |

## 4. 获取角色详情

**URL**: `/role/selectById`
**Method**: `GET`
**Description**: 根据角色 ID 获取角色详情

### 请求参数

| 参数名 | 类型 | 必填 | 描述 |
| :--- | :--- | :--- | :--- |
| id | String | 是 | 角色 ID |

### 响应参数 (ResultVo<RoleDto>)

| 字段名 | 类型 | 描述 |
| :--- | :--- | :--- |
| data | Object | 角色详情 |

## 5. 获取所有角色列表

**URL**: `/role/selectAll`
**Method**: `GET`
**Description**: 获取所有角色列表（不分页）

### 请求参数

无

### 响应参数 (ResultVo<List<RoleDto>>)

| 字段名 | 类型 | 描述 |
| :--- | :--- | :--- |
| data | Array | 角色列表 |
| data[].id | String | 角色 ID |
| data[].name | String | 角色名称 |
| data[].description | String | 角色描述 |
| data[].permissionList | Array | 权限列表 |

## 6. 删除自定义角色

**URL**: `/role/deleteById`
**Method**: `GET`
**Description**: 根据角色 ID 删除自定义角色

### 请求参数

| 参数名 | 类型 | 必填 | 描述 |
| :--- | :--- | :--- | :--- |
| id | String | 是 | 角色 ID |

### 响应参数 (ResultVo<?>)

| 字段名 | 类型 | 描述 |
| :--- | :--- | :--- |
| data | Null | 无 |
