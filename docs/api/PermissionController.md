# 权限管理 (PermissionController)

## 1. 获取所有权限

**URL**: `/permission/selectAllData`
**Method**: `GET`
**Description**: 获取系统中所有的权限数据

### 请求参数

无

### 响应参数 (ResultVo<List<PermissionDto>>)

| 字段名 | 类型 | 描述 |
| :--- | :--- | :--- |
| data | Array | 权限列表 |
| data[i].id | String | 权限 ID |
| data[i].permissionName | String | 权限名称 |
| data[i].permissionMark | String | 权限标识 |
| data[i].url | String | 权限 URL |
