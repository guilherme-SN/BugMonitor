CREATE TABLE bug_backend_services (
	bug_id BIGINT NOT NULL,
	backend_service_id BIGINT NOT NULL,

	PRIMARY KEY (bug_id, backend_service_id),
	CONSTRAINT fk_bug_backend_services_bug_id FOREIGN KEY (bug_id) REFERENCES bugs(ccm_id) ON DELETE CASCADE,
	CONSTRAINT fk_bug_backend_services_backend_service_id FOREIGN KEY (backend_service_id) REFERENCES backend_services(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
