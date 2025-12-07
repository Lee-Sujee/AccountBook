DROP DATABASE IF EXISTS account_book;
CREATE DATABASE account_book DEFAULT CHARACTER SET utf8mb4;

USE account_book;

CREATE TABLE `users` (
	`id` 				CHAR(36) NOT NULL,       						-- UUID  
    `email`				VARCHAR(255) NOT NULL UNIQUE,  
	`password`			VARCHAR(255) NOT NULL,        
    `name`				VARCHAR(100) NOT NULL,        
    `age`				INT,                         
    `job`				VARCHAR(100),                 
    `created_at`		TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,  
    PRIMARY KEY (id)
);


CREATE TABLE `book` (
	`id`					INT AUTO_INCREMENT,  
    `user_id`				CHAR(36) NOT NULL,
    `created_at`			TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, 
    `category`				VARCHAR(40) NOT NULL,                          
    `content`				VARCHAR(200) NOT NULL,                          
    `type`					ENUM('income', 'expense') NOT NULL,             
    `amount`				INT NOT NULL,                                    
    `memo`					TEXT NULL,                                   
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);


CREATE TABLE `board` (
	`id`					INT AUTO_INCREMENT,    
    `user_id`				CHAR(36),
    `title`					VARCHAR(200) NOT NULL,   
    `content`				TEXT NOT NULL,          
    `likes`					INT DEFAULT(0),            
    `views`					INT DEFAULT(0),           
    `created_at`			TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, 
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE SET NULL
);


CREATE TABLE `comment` (
	`id`					INT AUTO_INCREMENT,   
    `user_id`				CHAR(36),
    `board_id`				INT NOT NULL,
    `content`				VARCHAR(500) NOT NULL,  
    `likes`					INT DEFAULT(0),           
    `created_at`			TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, 
    PRIMARY KEY (id),
    FOREIGN KEY (board_id) REFERENCES board(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE SET NULL
);


CREATE TABLE `challenge` (
	`user_id`				CHAR(36) NOT NULL,
    `id`					INT AUTO_INCREMENT,  
    `start_date`			DATE NOT NULL,         
	`end_date`				DATE NOT NULL,         
    `target`     			INT NOT NULL,          
    `current_amount`    	INT DEFAULT 0 NOT NULL,      
    `category`				VARCHAR(100) NOT NULL, 
    `details`				VARCHAR(200) NOT NULL, 
    `status`				ENUM('ongoing', 'success', 'fail') NOT NULL,              
    `created_at`			TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,  
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE `stats` (
	`id`					INT AUTO_INCREMENT,     
	`menu`					VARCHAR(200) NOT NULL,   
    `price`					INT NOT NULL,                      
    `category`				VARCHAR(100) NOT NULL,    
    PRIMARY KEY (id)
);


select * from users;
