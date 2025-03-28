CREATE TABLE bug_backend_responsibles (
	bug_id BIGINT NOT NULL,
	user_id BIGINT NOT NULL,

	PRIMARY KEY (bug_id, user_id),
	CONSTRAINT fk_bug_backend_responsibles_bug_id FOREIGN KEY (bug_id) REFERENCES bugs(ccm_id) ON DELETE CASCADE,
	CONSTRAINT fk_bug_backend_responsibles_user_id FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;