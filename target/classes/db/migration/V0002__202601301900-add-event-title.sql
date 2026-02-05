ALTER TABLE evento
  ADD COLUMN titulo VARCHAR(255) NULL AFTER organizador_uuid;

UPDATE evento
  SET titulo = COALESCE(titulo, local)
  WHERE titulo IS NULL;
