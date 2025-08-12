# 用户表结构优化说明

## 优化概述

本次优化主要针对用户表（`tb_user`）的设计，将原来冗余的上级用户信息字段改为使用上级用户ID作为外键关联，提高数据一致性和可维护性。

## 主要变更

### 1. 移除的冗余字段

- `upinviteCode` - 上级邀请码
- `superiorName` - 上级名称  
- `superiorCode` - 上级邀请码
- `superiorUa` - 上级U级账户余额
- `superiorUb` - 上级U级账户余额
- `superiorUc` - 上级U级账户余额

### 2. 新增的外键字段

- `superiorId` - 上级用户ID（外键关联）

### 3. 数据库索引优化

- 添加 `idx_superior_id` 索引，优化上级用户查询性能
- 移除不必要的冗余字段索引

## 代码结构变更

### 1. UserEntity 优化
- 移除冗余的上级用户信息字段
- 保留 `superiorId` 作为外键关联

### 2. UserInfoDTO 优化  
- 移除冗余的上级用户信息字段
- 添加 `superiorInfo` 字段，包含上级用户的基本信息

### 3. 新增 SuperiorUserInfoDTO
- 专门用于封装上级用户信息
- 包含上级用户的基本信息（ID、用户名、邀请码、代理信息等）

### 4. UserService 增强
- 新增 `getUserInfoWithSuperior()` 方法
- 通过关联查询获取上级用户信息
- 支持动态获取上级用户的实时数据

## 优势

1. **数据一致性**：避免上级用户信息冗余，确保数据同步
2. **性能提升**：减少数据存储，优化查询性能
3. **维护性**：通过外键关联，便于数据维护和更新
4. **扩展性**：支持更复杂的上级用户关系查询

## 使用方式

### 获取用户信息（包含上级信息）
```java
UserInfoDTO userInfo = userService.getUserInfoWithSuperior(userId);
```

### 上级用户信息结构
```java
SuperiorUserInfoDTO superiorInfo = userInfo.getSuperiorInfo();
if (superiorInfo != null) {
    Long superiorId = superiorInfo.getSuperiorId();
    String superiorUsername = superiorInfo.getSuperiorUsername();
    // ... 其他上级用户信息
}
```

## 注意事项

1. 执行 `user.sql` 前请备份现有数据
2. 上级用户信息通过关联查询动态获取，确保数据实时性
3. 可根据业务需求调整 `SuperiorUserInfoDTO` 中的字段
4. 建议在应用层添加适当的权限控制，避免敏感信息泄露

## 后续优化建议

1. 考虑添加用户关系表，支持多级代理关系
2. 实现缓存机制，优化频繁的上级用户信息查询
3. 添加用户关系变更的审计日志
4. 考虑使用图数据库优化复杂的用户关系查询


