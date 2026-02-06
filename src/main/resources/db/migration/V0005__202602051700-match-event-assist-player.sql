ALTER TABLE evento_partida
  ADD COLUMN assistente_uuid CHAR(36) NULL AFTER jogador_uuid;

ALTER TABLE evento_partida
  ADD CONSTRAINT fk_evento_partida_assistente
  FOREIGN KEY (assistente_uuid) REFERENCES usuario(uuid);

CREATE INDEX idx_evento_partida_assistente_uuid ON evento_partida (assistente_uuid);
