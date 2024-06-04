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

            try (var stmt = conn.prepareStatement("BEGIN EXECUTE IMMEDIATE 'DROP TABLE " + DenunciasRepository.TB_NAME + " CASCADE CONSTRAINTS'; EXCEPTION WHEN OTHERS THEN NULL; END;")) {
                stmt.executeUpdate();
            } catch (SQLException e) {
                logError(e);
            }






            try (var stmt = conn.prepareStatement(
                    ("CREATE TABLE %s (" +
                            "%s NUMBER GENERATED AS IDENTITY CONSTRAINT DENUNCIA_JAVA_PK PRIMARY KEY, " +
                            "%s DATE NOT NULL, " +
                            "%s VARCHAR2(200), " +
                            "%s VARCHAR2(100) NOT NULL, " +
                            "%s NUMBER NOT NULL, " +
                            "%s NUMBER NOT NULL, " +
                            "%s NUMBER, " +
                            "%s NUMBER NOT NULL)")
                            .formatted(DenunciasRepository.TB_NAME,
                                    TB_COLUMNS.get("ID_DENUNCIA"),
                                    TB_COLUMNS.get("DATA_HORA"),
                                    TB_COLUMNS.get("DESCRICAO"),
                                    TB_COLUMNS.get("IMPACTO_PERCEBIDO"),
                                    TB_COLUMNS.get("ID_LOCALIZACAO"),
                                    TB_COLUMNS.get("ID_TIPO_INCIDENTE"),
                                    TB_COLUMNS.get("ID_COMENTARIO"),
                                    TB_COLUMNS.get("ID_FEEDBACK")
                            ))){
                stmt.executeUpdate();
                logInfo("Tabela "+ DenunciasRepository.TB_NAME +" criada com sucesso!");

            } catch (SQLException e) {
                logError(e);
            }

            try {
                var stmt = conn.prepareStatement(
                        ("CREATE TABLE " + DenunciasRepository.TB_NAME_E + " (ID_ESTADO NUMBER GENERATED AS IDENTITY CONSTRAINT ESTADO_JAVA_PK PRIMARY KEY, " +
                                "NOME VARCHAR2(50) NOT NULL)" ));
                stmt.executeUpdate();
                logInfo("Tabela "+ DenunciasRepository.TB_NAME_E +" criada com sucesso!");

            } catch (SQLException e) {
                logError(e);
            }

            try {
                var stmt = conn.prepareStatement(
                        ("CREATE TABLE " + DenunciasRepository.TB_NAME_C + " (ID_CIDADE NUMBER GENERATED AS IDENTITY CONSTRAINT PERFIL_JAVA_PK PRIMARY KEY, " +
                                "NOME VARCHAR2(50) NOT NULL)" ));
                stmt.executeUpdate();
                logInfo("Tabela "+ DenunciasRepository.TB_NAME_C +" criada com sucesso!");

            } catch (SQLException e) {
                logError(e);
            }

            try {
                var stmt = conn.prepareStatement(
                        ("CREATE TABLE " + DenunciasRepository.TB_NAME_B + " (ID_BAIRRO NUMBER GENERATED AS IDENTITY CONSTRAINT PERFIL_JAVA_PK PRIMARY KEY, " +
                                "NOME VARCHAR2(50) NOT NULL)" ));
                stmt.executeUpdate();
                logInfo("Tabela "+ DenunciasRepository.TB_NAME_B +" criada com sucesso!");

            } catch (SQLException e) {
                logError(e);
            }
            try {
                var stmt = conn.prepareStatement(
                        ("CREATE TABLE " + DenunciasRepository.TB_NAME_L + " (ID_LOCALIZACAO NUMBER GENERATED AS IDENTITY CONSTRAINT PERFIL_JAVA_PK PRIMARY KEY, " +
                                "CEP NUMBER(8), REFERENCIA VARCHAR2(30) NOT NULL)" ));
                stmt.executeUpdate();
                logInfo("Tabela "+ DenunciasRepository.TB_NAME_L +" criada com sucesso!");

            } catch (SQLException e) {
                logError(e);
            }

//            try {
//                var stmt = conn.prepareStatement(
//                        ("CREATE TABLE " + UsuariosRepository.TB_NAME_P + " (COD_PERFIL NUMBER GENERATED AS IDENTITY CONSTRAINT PERFIL_JAVA_PK PRIMARY KEY, " +
//                                "TIPO VARCHAR2(30) NOT NULL)" ));
//                stmt.executeUpdate();
//                logInfo("Tabela "+ UsuariosRepository.TB_NAME_P +" criada com sucesso!");
//
//            } catch (SQLException e) {
//                logError(e);
//            }
//
//            try {
//                var stmt = conn.prepareStatement(
//                        ("CREATE TABLE " + UsuariosRepository.TB_NAME_P + " (COD_PERFIL NUMBER GENERATED AS IDENTITY CONSTRAINT PERFIL_JAVA_PK PRIMARY KEY, " +
//                                "TIPO VARCHAR2(30) NOT NULL)" ));
//                stmt.executeUpdate();
//                logInfo("Tabela "+ UsuariosRepository.TB_NAME_P +" criada com sucesso!");
//
//            } catch (SQLException e) {
//                logError(e);
//            }
//
//            try {
//                var stmt = conn.prepareStatement(
//                        ("CREATE TABLE " + UsuariosRepository.TB_NAME_P + " (COD_PERFIL NUMBER GENERATED AS IDENTITY CONSTRAINT PERFIL_JAVA_PK PRIMARY KEY, " +
//                                "TIPO VARCHAR2(30) NOT NULL)" ));
//                stmt.executeUpdate();
//                logInfo("Tabela "+ UsuariosRepository.TB_NAME_P +" criada com sucesso!");
//
//            } catch (SQLException e) {
//                logError(e);
//            }
//            try {
//                var stmt = conn.prepareStatement(
//                        ("CREATE TABLE " + UsuariosRepository.TB_NAME_P + " (COD_PERFIL NUMBER GENERATED AS IDENTITY CONSTRAINT PERFIL_JAVA_PK PRIMARY KEY, " +
//                                "TIPO VARCHAR2(30) NOT NULL)" ));
//                stmt.executeUpdate();
//                logInfo("Tabela "+ UsuariosRepository.TB_NAME_P +" criada com sucesso!");
//
//            } catch (SQLException e) {
//                logError(e);
//            }

            conn.close();
        } catch (SQLException e) {
            logError(e);
        }


    }
}
