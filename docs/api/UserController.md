# 用户管理 (UserController)

## 1. 分页查询用户数据

**URL**: `/user/page`
**Method**: `POST`
**Description**: 分页查询用户数据

### 请求参数 (UserPageQuery)

| 参数名 | 类型 | 必填 | 描述 |
| :--- | :--- | :--- | :--- |
| current | Long | 是 | 当前页码 |
| size | Long | 是 | 每页条数 |
| username | String | 否 | 用户名 (模糊查询) |
| phone | String | 否 | 手机号 (模糊查询) |

### 响应参数 (ResultVo<PageResult<UserPageDto>>)

| 字段名 | 类型 | 描述 |
| :--- | :--- | :--- |
| code | Number | 状态码 |
| msg | String | 消息 |
| data | Object | 分页结果 |
| data.records | Array | 用户列表 |
| data.total | Long | 总记录数 |

## 2. 新增用户数据

**URL**: `/user/insert`
**Method**: `POST`
**Description**: 新增用户数据，必须指定角色列表

### 请求参数 (UserInsertDto)

| 参数名 | 类型 | 必填 | 描述 |
| :--- | :--- | :--- | :--- |
| username | String | 否 | 用户名 (若为空则自动生成) |
| nickname | String | 否 | 昵称 |
| phone | String | 是 | 手机号 |
| email | String | 否 | 邮箱 |
| roleIdList | Array<String> | 是 | 角色ID列表 |

### 响应参数 (ResultVo<String>)

| 字段名 | 类型 | 描述 |
| :--- | :--- | :--- |
| data | String | 新增用户的 ID |

## 3. 修改用户数据

**URL**: `/user/update`
**Method**: `POST`
**Description**: 修改用户基础数据并更新用户角色

### 请求参数 (UserUpdateDto)

| 参数名 | 类型 | 必填 | 描述 |
| :--- | :--- | :--- | :--- |
| id | String | 是 | 用户 ID |
| username | String | 是 | 用户名 |
| phone | String | 是 | 手机号 |
| nickname | String | 否 | 昵称 |
| email | String | 否 | 邮箱 |
| roleIdList | Array<String> | 是 | 角色ID列表 |

### 响应参数 (ResultVo<?>)

| 字段名 | 类型 | 描述 |
| :--- | :--- | :--- |
| data | Null | 无 |

## 4. 获取用户详情

**URL**: `/user/selectById`
**Method**: `GET`
**Description**: 根据用户 ID 获取用户详情

### 请求参数

| 参数名 | 类型 | 必填 | 描述 |
| :--- | :--- | :--- | :--- |
| id | String | 是 | 用户 ID |

### 响应参数 (ResultVo<UserDto>)

| 字段名 | 类型 | 描述 |
| :--- | :--- | :--- |
| data | Object | 用户详情对象 |
| data.id | String | 用户 ID |
| data.username | String | 用户名 |
| data.nickname | String | 昵称 |
| data.phone | String | 手机号 |
| data.roles | Array | 角色列表 |

## 5. 禁用/启用用户

**URL**: `/user/disableEnableUser`
**Method**: `POST`
**Description**: 禁用或启用用户

### 请求参数 (DisableEnableUserDto)

| 参数名 | 类型 | 必填 | 描述 |
| :--- | :--- | :--- | :--- |
| userId | String | 是 | 用户 ID |
| enabled | Boolean | 是 | true: 启用, false: 禁用 |

### 响应参数 (ResultVo<?>)

| 字段名 | 类型 | 描述 |
| :--- | :--- | :--- |
| data | Null | 无 |

## 6. 删除用户

**URL**: `/user/deleteById`
**Method**: `GET`
**Description**: 根据用户 ID 删除用户数据

### 请求参数

| 参数名 | 类型 | 必填 | 描述 |
| :--- | :--- | :--- | :--- |
| id | String | 是 | 用户 ID |

### 响应参数 (ResultVo<?>)

| 字段名 | 类型 | 描述 |
| :--- | :--- | :--- |
| data | Null | 无 |

## 7. 分配角色

**URL**: `/user/assignRole`
**Method**: `POST`
**Description**: 为用户分配角色，会替换用户的所有现有角色

### 请求参数 (AssignRoleDto)

| 参数名 | 类型 | 必填 | 描述 |
| :--- | :--- | :--- | :--- |
| userId | String | 是 | 用户 ID |
| roleIdList | Array<String> | 是 | 角色 ID 列表 |

### 响应参数 (ResultVo<?>)

| 字段名 | 类型 | 描述 |
| :--- | :--- | :--- |
| data | Null | 无 |
| msg | String | 操作结果消息 |
