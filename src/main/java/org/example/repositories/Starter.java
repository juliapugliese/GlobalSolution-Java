package org.example.repositories;

import org.example.infrastructure.OracleDatabaseConfiguration;

import java.sql.SQLException;
import java.util.Map;

public class Starter implements _Logger<String>{


    public static final Map<String, String> TB_COLUMNS = Map.ofEntries(
            Map.entry("ID_LOCALIZACAO", "ID_LOCALIZACAO"),
            Map.entry("ID_TIPO_INCIDENTE", "ID_TIPO_INCIDENTE"),
            Map.entry("ID_COMENTARIO", "ID_COMENTARIO"),
            Map.entry("ID_FEEDBACK", "ID_FEEDBACK"),
            Map.entry("ID_DENUNCIA", "ID_DENUNCIA"),
            Map.entry("DATA_HORA", "NOME_USUARIO"),
            Map.entry("DESCRICAO", "DESCRICAO"),
            Map.entry("IMPACTO_PERCEBIDO", "IMPACTO_PERCEBIDO")
    );

    public void initialize() {
        try (var conn = new OracleDatabaseConfiguration().getConnection()) {

            conn.close();
        } catch (SQLException e) {
            logError(e);
        }


    }
}
