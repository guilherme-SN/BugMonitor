CREATE TABLE bugs (
	ccm_id BIGINT PRIMARY KEY,
	priority INTEGER DEFAULT 0,
	name VARCHAR(255) NOT NULL,
	url VARCHAR(255) NOT NULL,
	reported_by VARCHAR(255) NOT NULL,
	creator_id BIGINT NOT NULL,
	task_status VARCHAR(255) NOT NULL,
	product_status VARCHAR(255) NOT NULL,
	qa_status VARCHAR(255) NOT NULL,
	backend_status VARCHAR(255) NOT NULL,
	frontend_status VARCHAR(255) NOT NULL,
	created_at DATETIME NOT NULL,
	last_edited_at DATETIME NOT NULL,

	CONSTRAINT fk_creator_id FOREIGN KEY (creator_id) REFERENCES users(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;