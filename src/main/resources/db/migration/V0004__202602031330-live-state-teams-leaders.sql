ALTER TABLE live_state
  ADD COLUMN teams_json TEXT NULL,
  ADD COLUMN queue_json TEXT NULL,
  ADD COLUMN leaders_json TEXT NULL;
