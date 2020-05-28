CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE pet (
  uuid UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
  name TEXT NOT NULL,
  category TEXT NOT NULL,
  status TEXT NOT NULL
);

CREATE TABLE customer (
  uuid UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
  username TEXT NOT NULL,
  firstName TEXT NOT NULL,
  lastName TEXT NOT NULL,
  email TEXT NOT NULL,
  password TEXT NOT NULL,
  phone TEXT NOT NULL
);

CREATE TABLE purchase (
  uuid UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
  pet UUID NOT NULL REFERENCES pet(uuid) ON DELETE CASCADE ON UPDATE CASCADE,
  quantity INTEGER NOT NULL,
  shipDate TIMESTAMP NOT NULL,
  status TEXT NOT NULL,
  COMPLETE BOOLEAN NOT NULL
);
