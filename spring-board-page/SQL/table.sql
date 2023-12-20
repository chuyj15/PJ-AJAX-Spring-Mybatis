CREATE TABLE `board` (
  `board_no` int NOT NULL AUTO_INCREMENT,
  `title` varchar(100) NOT NULL,
  `writer` varchar(100) DEFAULT NULL,
  `content` text,
  `reg_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `upd_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `date` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `views` int DEFAULT '0',
  PRIMARY KEY (`board_no`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='게시판';

CREATE TABLE `comment` (
  `comment_no` int NOT NULL AUTO_INCREMENT,
  `parent_table` varchar(100) DEFAULT NULL,
  `parent_no` int DEFAULT '0',
  `writer` varchar(100) DEFAULT NULL,
  `content` text,
  `group_no` int DEFAULT '0',
  `super_no` int DEFAULT '0',
  `depth_no` int DEFAULT '0',
  `seq_no` int DEFAULT '0',
  `reg_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `upd_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `sub_count` int DEFAULT '0',
  PRIMARY KEY (`comment_no`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='댓글';

CREATE TABLE `files` (
  `file_no` int NOT NULL,
  `parent_table` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci,
  `parent_no` int DEFAULT NULL,
  `file_name` text,
  `file_path` text,
  PRIMARY KEY (`file_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `likes` (
  `like_no` int NOT NULL AUTO_INCREMENT,
  `parent_table` text,
  `username` text,
  `parent_no` int DEFAULT NULL,
  PRIMARY KEY (`like_no`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
