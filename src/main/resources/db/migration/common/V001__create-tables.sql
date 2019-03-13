create TABLE client(
    id VARCHAR(36) PRIMARY KEY
);

create TABLE baby (
  id VARCHAR(36) PRIMARY KEY,
  name VARCHAR(100),
  gender VARCHAR(50),
  date_of_birth datetime,
  client_id VARCHAR(36) references client(id)
);

create TABLE baby_image (
  id VARCHAR(36) PRIMARY KEY,
  latitude DOUBLE,
  longitude DOUBLE,
  image_datetime datetime,
  client_id VARCHAR(36) references client(id)
);

