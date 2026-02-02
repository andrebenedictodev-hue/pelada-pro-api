ALTER TABLE evento
  ADD COLUMN title VARCHAR(255) NULL AFTER owner_uuid;

UPDATE evento
  SET title = COALESCE(title, location)
  WHERE title IS NULL;
