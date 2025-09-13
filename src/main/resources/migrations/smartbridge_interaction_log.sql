CREATE TABLE smartbridge_interaction_log (
                                             id INT IDENTITY(1,1) PRIMARY KEY,
                                             service_name NVARCHAR(100) NOT NULL,
                                             senderid NVARCHAR(100),
                                             date DATETIME NOT NULL DEFAULT GETDATE(),
                                             request_xml NVARCHAR(MAX),
                                             response_xml NVARCHAR(MAX),
                                             status NVARCHAR(255),
                                             ipaddress NVARCHAR(16)
);

-- Create an index on service_name and date for better query performance
CREATE INDEX idx_smartbridge_log_service_date ON smartbridge_interaction_log (service_name, date);