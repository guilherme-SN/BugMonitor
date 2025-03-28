CREATE TABLE bug_epics (
	bug_id BIGINT NOT NULL,
	epic_id BIGINT NOT NULL,

	PRIMARY KEY (bug_id, epic_id),
	CONSTRAINT fk_bug_epics_bug_id FOREIGN KEY (bug_id) REFERENCES bugs(ccm_id) ON DELETE CASCADE,
	CONSTRAINT fk_bug_epics_epic_id FOREIGN KEY (epic_id) REFERENCES epics(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
