-- 创建db10数据库
CREATE DATABASE db10;

-- 使用db10数据库
USE db10;

-- 创建user表
CREATE TABLE USER(
	id INT PRIMARY KEY AUTO_INCREMENT,	-- 主键id
	NAME VARCHAR(20),			-- 姓名
	age INT,				-- 年龄
	search_count INT                        -- 搜索数量
);

-- 插入测试数据
INSERT INTO USER VALUES (NULL,'张三',23,25),(NULL,'李四',24,5),(NULL,'王五',25,3)
,(NULL,'赵六',26,7),(NULL,'张三丰',93,20),(NULL,'张无忌',18,23),(NULL,'张强',33,21),(NULL,'张果老',65,6);