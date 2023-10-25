-- Tạo bảng cho thành phố
CREATE TABLE cities
(
    id            SERIAL PRIMARY KEY,
    name          VARCHAR(255) NOT NULL,
    code          INT          NOT NULL,
    codename      VARCHAR(255) NOT NULL,
    division_type VARCHAR(255) NOT NULL,
    phone_code    INT          NOT NULL
);

-- Tạo bảng cho quận
CREATE TABLE districts
(
    id             SERIAL PRIMARY KEY,
    name           VARCHAR(255) NOT NULL,
    code           INT          NOT NULL,
    codename       VARCHAR(255) NOT NULL,
    division_type  VARCHAR(255) NOT NULL,
    short_codename VARCHAR(255) NOT NULL,
    city_id        INT REFERENCES cities (id)
);

-- Tạo bảng cho phường
CREATE TABLE wards
(
    id             SERIAL PRIMARY KEY,
    name           VARCHAR(255) NOT NULL,
    code           INT          NOT NULL,
    codename       VARCHAR(255) NOT NULL,
    division_type  VARCHAR(255) NOT NULL,
    short_codename VARCHAR(255) NOT NULL,
    district_id    INT REFERENCES districts (id)
);


