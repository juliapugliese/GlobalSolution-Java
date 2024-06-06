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
            Map.entry("ID_DENUNCIANTE", "ID_DENUNCIANTE"),
            Map.entry("DATA", "DATA"),
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
            try (var stmt = conn.prepareStatement("BEGIN EXECUTE IMMEDIATE 'DROP TABLE " + DenunciasRepository.TB_NAME_E + " CASCADE CONSTRAINTS'; EXCEPTION WHEN OTHERS THEN NULL; END;")) {
                stmt.executeUpdate();
            } catch (SQLException e) {
                logError(e);
            }
            try (var stmt = conn.prepareStatement("BEGIN EXECUTE IMMEDIATE 'DROP TABLE " + DenunciasRepository.TB_NAME_C + " CASCADE CONSTRAINTS'; EXCEPTION WHEN OTHERS THEN NULL; END;")) {
                stmt.executeUpdate();
            } catch (SQLException e) {
                logError(e);
            }
            try (var stmt = conn.prepareStatement("BEGIN EXECUTE IMMEDIATE 'DROP TABLE " + DenunciasRepository.TB_NAME_B + " CASCADE CONSTRAINTS'; EXCEPTION WHEN OTHERS THEN NULL; END;")) {
                stmt.executeUpdate();
            } catch (SQLException e) {
                logError(e);
            }
            try (var stmt = conn.prepareStatement("BEGIN EXECUTE IMMEDIATE 'DROP TABLE " + DenunciasRepository.TB_NAME_L + " CASCADE CONSTRAINTS'; EXCEPTION WHEN OTHERS THEN NULL; END;")) {
                stmt.executeUpdate();
            } catch (SQLException e) {
                logError(e);
            }
            try (var stmt = conn.prepareStatement("BEGIN EXECUTE IMMEDIATE 'DROP TABLE " + DenunciasRepository.TB_NAME_CO + " CASCADE CONSTRAINTS'; EXCEPTION WHEN OTHERS THEN NULL; END;")) {
                stmt.executeUpdate();
            } catch (SQLException e) {
                logError(e);
            }
            try (var stmt = conn.prepareStatement("BEGIN EXECUTE IMMEDIATE 'DROP TABLE " + DenunciasRepository.TB_NAME_I + " CASCADE CONSTRAINTS'; EXCEPTION WHEN OTHERS THEN NULL; END;")) {
                stmt.executeUpdate();
            } catch (SQLException e) {
                logError(e);
            }
            try (var stmt = conn.prepareStatement("BEGIN EXECUTE IMMEDIATE 'DROP TABLE " + DenunciantesRepository.TB_NAME + " CASCADE CONSTRAINTS'; EXCEPTION WHEN OTHERS THEN NULL; END;")) {
                stmt.executeUpdate();
            } catch (SQLException e) {
                logError(e);
            }
            try (var stmt = conn.prepareStatement("BEGIN EXECUTE IMMEDIATE 'DROP TABLE " + FeedbacksRepository.TB_NAME + " CASCADE CONSTRAINTS'; EXCEPTION WHEN OTHERS THEN NULL; END;")) {
                stmt.executeUpdate();
            } catch (SQLException e) {
                logError(e);
            }







            try (var stmt = conn.prepareStatement(
                    ("CREATE TABLE %s (" +
                            "%s NUMBER GENERATED AS IDENTITY CONSTRAINT DENUNCIA_JAVA_PK PRIMARY KEY, " +
                            "%s DATE NOT NULL, " +
                            "%s VARCHAR2(200), " +
                            "%s NUMBER NOT NULL, " +
                            "%s NUMBER NOT NULL, " +
                            "%s NUMBER, " +
                            "%s NUMBER, " +
                            "%s NUMBER)")
                            .formatted(DenunciasRepository.TB_NAME,
                                    TB_COLUMNS.get("ID_DENUNCIA"),
                                    TB_COLUMNS.get("DATA"),
                                    TB_COLUMNS.get("DESCRICAO"),
                                    TB_COLUMNS.get("ID_DENUNCIANTE"),
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
                        ("CREATE TABLE " + DenunciasRepository.TB_NAME_C + " (ID_CIDADE NUMBER GENERATED AS IDENTITY CONSTRAINT CIDADE_JAVA_PK PRIMARY KEY, " +
                                "NOME VARCHAR2(50) NOT NULL," +
                                "ID_ESTADO NUMBER NOT NULL)" ));
                stmt.executeUpdate();
                logInfo("Tabela "+ DenunciasRepository.TB_NAME_C +" criada com sucesso!");

            } catch (SQLException e) {
                logError(e);
            }

            try {
                var stmt = conn.prepareStatement(
                        ("CREATE TABLE " + DenunciasRepository.TB_NAME_B + " (ID_BAIRRO NUMBER GENERATED AS IDENTITY CONSTRAINT BAIRRO_JAVA_PK PRIMARY KEY, " +
                                "NOME VARCHAR2(50) NOT NULL," +
                                "ID_CIDADE NUMBER NOT NULL)" ));
                stmt.executeUpdate();
                logInfo("Tabela "+ DenunciasRepository.TB_NAME_B +" criada com sucesso!");

            } catch (SQLException e) {
                logError(e);
            }
            try {
                var stmt = conn.prepareStatement(
                        ("CREATE TABLE " + DenunciasRepository.TB_NAME_L + " (ID_LOCALIZACAO NUMBER GENERATED AS IDENTITY CONSTRAINT LOCALIZACAO_JAVA_PK PRIMARY KEY, " +
                                "CEP NUMBER(8), " +
                                "ENDERECO VARCHAR2(300) NOT NULL," +
                                "ID_BAIRRO NUMBER NOT NULL)"));
                stmt.executeUpdate();
                logInfo("Tabela "+ DenunciasRepository.TB_NAME_L +" criada com sucesso!");

            } catch (SQLException e) {
                logError(e);
            }

            try {
                var stmt = conn.prepareStatement(
                        ("CREATE TABLE " + DenunciasRepository.TB_NAME_CO + " (ID_COMENTARIO NUMBER GENERATED AS IDENTITY CONSTRAINT COMENTARIO_JAVA_PK PRIMARY KEY, " +
                                "COMENTARIO VARCHAR2(200))" ));
                stmt.executeUpdate();
                logInfo("Tabela "+ DenunciasRepository.TB_NAME_CO +" criada com sucesso!");

            } catch (SQLException e) {
                logError(e);
            }

            try {
                var stmt = conn.prepareStatement(
                        ("CREATE TABLE " + DenunciasRepository.TB_NAME_I + " (ID_TIPO_INCIDENTE NUMBER GENERATED AS IDENTITY CONSTRAINT TIPO_INCIDENTE_JAVA_PK PRIMARY KEY, " +
                                "DESCRICAO VARCHAR2(200) NOT NULL, " +
                                "ORIGEM_RESIDUO VARCHAR2(50), " +
                                "RECORRENCIA VARCHAR2(50))" ));
                stmt.executeUpdate();
                logInfo("Tabela "+ DenunciasRepository.TB_NAME_I +" criada com sucesso!");

            } catch (SQLException e) {
                logError(e);
            }

            try {
                var stmt = conn.prepareStatement(
                        ("CREATE TABLE " + DenunciantesRepository.TB_NAME + " (ID_DENUNCIANTE NUMBER GENERATED AS IDENTITY CONSTRAINT DENUNCIANTE_JAVA_PK PRIMARY KEY, " +
                                "NOME VARCHAR2(50) NOT NULL," +
                                "EMAIL VARCHAR2(50) NOT NULL," +
                                "TELEFONE VARCHAR2(11) NOT NULL)" ));
                stmt.executeUpdate();
                logInfo("Tabela "+ DenunciantesRepository.TB_NAME  +" criada com sucesso!");

            } catch (SQLException e) {
                logError(e);
            }
            try {
                var stmt = conn.prepareStatement(
                        ("CREATE TABLE " + FeedbacksRepository.TB_NAME + " (ID_FEEDBACK NUMBER GENERATED AS IDENTITY CONSTRAINT FEEDBACK_JAVA_PK PRIMARY KEY, " +
                                "STATUS VARCHAR2(50)," +
                                "RETORNO VARCHAR2(200)," +
                                "DATA DATE)" ));
                stmt.executeUpdate();
                logInfo("Tabela "+ FeedbacksRepository.TB_NAME +" criada com sucesso!");

            } catch (SQLException e) {
                logError(e);
            }


            try (var stmt = conn.prepareStatement("ALTER TABLE " + DenunciasRepository.TB_NAME_C +" ADD CONSTRAINT ESTADO_CIDADE_FK FOREIGN KEY(ID_ESTADO) REFERENCES " + DenunciasRepository.TB_NAME_E + " (ID_ESTADO)")) {
                stmt.executeUpdate();
            } catch (SQLException e) {
                logError(e);
            }
            try (var stmt = conn.prepareStatement("ALTER TABLE "+ DenunciasRepository.TB_NAME_B +" ADD CONSTRAINT CIDADE_BAIRRO_FK FOREIGN KEY(ID_CIDADE) REFERENCES "+ DenunciasRepository.TB_NAME_C +" (ID_CIDADE)")) {
                stmt.executeUpdate();
            } catch (SQLException e) {
                logError(e);
            }
            try (var stmt = conn.prepareStatement("ALTER TABLE "+ DenunciasRepository.TB_NAME_L +" ADD CONSTRAINT BAIRRO_LOCALIZACAO_FK FOREIGN KEY(ID_BAIRRO) REFERENCES "+ DenunciasRepository.TB_NAME_B +" (ID_BAIRRO)")) {
                stmt.executeUpdate();
            } catch (SQLException e) {
                logError(e);
            }
            try (var stmt = conn.prepareStatement("ALTER TABLE "+ DenunciasRepository.TB_NAME +" ADD CONSTRAINT LOCALIZACAO_DENUNCIA_FK FOREIGN KEY(ID_LOCALIZACAO) REFERENCES "+ DenunciasRepository.TB_NAME_L +" (ID_LOCALIZACAO)")) {
                stmt.executeUpdate();
            } catch (SQLException e) {
                logError(e);
            }
            try (var stmt = conn.prepareStatement("ALTER TABLE "+ DenunciasRepository.TB_NAME +" ADD CONSTRAINT FEEDBACK_DENUNCIA_FK FOREIGN KEY(ID_FEEDBACK) REFERENCES "+ FeedbacksRepository.TB_NAME +" (ID_FEEDBACK) ON DELETE SET NULL")) {
                stmt.executeUpdate();
            } catch (SQLException e) {
                logError(e);
            }
            try (var stmt = conn.prepareStatement("ALTER TABLE "+ DenunciasRepository.TB_NAME +" ADD CONSTRAINT COMENTARIO_DENUNCIA_FK FOREIGN KEY(ID_COMENTARIO) REFERENCES "+ DenunciasRepository.TB_NAME_CO +" (ID_COMENTARIO) ON DELETE SET NULL")) {
                stmt.executeUpdate();
            } catch (SQLException e) {
                logError(e);
            }
            try (var stmt = conn.prepareStatement("ALTER TABLE "+ DenunciasRepository.TB_NAME +" ADD CONSTRAINT TIPO_INCIDENTE_DENUNCIA_FK FOREIGN KEY(ID_TIPO_INCIDENTE) REFERENCES "+ DenunciasRepository.TB_NAME_I +" (ID_TIPO_INCIDENTE) ON DELETE SET NULL")) {
                stmt.executeUpdate();
            } catch (SQLException e) {
                logError(e);
            }
            try (var stmt = conn.prepareStatement("ALTER TABLE "+ DenunciasRepository.TB_NAME +" ADD CONSTRAINT DENUNCIA_DENUNCIANTE_FK FOREIGN KEY(ID_DENUNCIANTE) REFERENCES "+ DenunciantesRepository.TB_NAME +" (ID_DENUNCIANTE) ON DELETE SET NULL")) {
                stmt.executeUpdate();
            } catch (SQLException e) {
                logError(e);
            }

            conn.close();
        } catch (SQLException e) {
            logError(e);
        }


    }
}
