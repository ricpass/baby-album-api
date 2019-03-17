create TABLE client(
    id VARCHAR(36) PRIMARY KEY
);

create TABLE baby (
  id VARCHAR(36) PRIMARY KEY,
  name VARCHAR(100),
  gender VARCHAR(50),
  date_of_birth date,
  client_id VARCHAR(36) NOT NULL UNIQUE,

  FOREIGN KEY (client_id) references client(id)
);

create TABLE baby_image (
  id VARCHAR(36) PRIMARY KEY,
  latitude DOUBLE,
  longitude DOUBLE,
  full_address VARCHAR(1000),
  image_datetime datetime,
  client_id VARCHAR(36) NOT NULL,

  FOREIGN KEY (client_id) references client(id)
);

