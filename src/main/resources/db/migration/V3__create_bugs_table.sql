CREATE TABLE bugs (
	ccm_id BIGINT PRIMARY KEY,
	priority VARCHAR(255),
	name VARCHAR(255),
	url VARCHAR(255),
	reported_by VARCHAR(255),
	creator_id BIGINT,
	task_status VARCHAR(255),
	product_status VARCHAR(255),
	qa_status VARCHAR(255),
	backend_status VARCHAR(255),
	frontend_status VARCHAR(255),
	created_at DATETIME,
	last_edited_at DATETIME,
	completed_at DATETIME,
	notification_status ENUM('NOT_READY', 'READY', 'SENT') NOT NULL,

	CONSTRAINT fk_creator_id FOREIGN KEY (creator_id) REFERENCES users(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;