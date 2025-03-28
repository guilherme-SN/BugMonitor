CREATE TABLE bug_customers (
	bug_id BIGINT NOT NULL,
	customer_id BIGINT NOT NULL,

	PRIMARY KEY (bug_id, customer_id),
	CONSTRAINT fk_bug_customers_bug_id FOREIGN KEY (bug_id) REFERENCES bugs(ccm_id) ON DELETE CASCADE,
	CONSTRAINT fk_bug_customers_customer_id FOREIGN KEY (customer_id) REFERENCES customers(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;