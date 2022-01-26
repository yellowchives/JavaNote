-- 创建db11数据库
CREATE DATABASE db11;

-- 使用db11数据库
USE db11;

-- 创建数据表
CREATE TABLE news(
	id INT PRIMARY KEY AUTO_INCREMENT,	-- 主键id
	title VARCHAR(999)                      -- 新闻标题
);

-- 插入数据
DELIMITER $$
CREATE PROCEDURE create_data()		
BEGIN
DECLARE i INT;		
SET i=1;
WHILE i<=100 DO	
	INSERT INTO news VALUES (NULL,CONCAT('奥巴马罕见介入美国2020大选，警告民主党参选人须“基于现实”',i));	
SET i=i+1;		
END WHILE;
END 
$$

-- 调用存储过程
CALL create_data();