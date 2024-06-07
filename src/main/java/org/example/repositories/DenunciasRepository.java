package org.example.repositories;

import org.example.entities.ServicoModel.Denuncia;
import org.example.entities.ServicoModel.Feedback;
import org.example.entities.UsuarioModel.Denunciante;
import org.example.entities._BaseEntity;
import org.example.infrastructure.OpenStreetMapUtils;
import org.example.infrastructure.OracleDatabaseConfiguration;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class DenunciasRepository extends Starter implements _BaseRepository<Denuncia>, _Logger<String>{
    public DenunciasRepository() {
    }

    public static final String TB_NAME = "DENUNCIA_JAVA";

    public static final String TB_NAME_E = "ESTADO_JAVA";
    public static final String TB_NAME_C = "CIDADE_JAVA";
    public static final String TB_NAME_B = "BAIRRO_JAVA";
    public static final String TB_NAME_L = "LOCALIZACAO_JAVA";

    public static final String TB_NAME_CO = "COMENTARIO_JAVA";
    public static final String TB_NAME_I = "TIPO_INCIDENTE_JAVA";

    public static final String TB_NAME_F = "FEEDBACK_JAVA";

    public List<Integer> getIdEstado(Denuncia denuncia){

        String[] partes = denuncia.getLocalizacao().split(",");

        if (partes.length > 0) {
            var lat = Double.parseDouble(partes[0]);
            var lon = Double.parseDouble(partes[1]);

            var idEstado = new ArrayList<Integer>();
            try (var conn = new OracleDatabaseConfiguration().getConnection()){
                var stmtRetrieve = conn.prepareStatement("SELECT ID_ESTADO FROM "+
                        DenunciasRepository.TB_NAME_E+" WHERE NOME = '%s'"
                        .formatted(OpenStreetMapUtils.getInstance().getEstado(lat, lon)));
                try (var rs = stmtRetrieve.executeQuery()) {
                    if (rs.next()) {
                        idEstado.add(rs.getInt(1));
                    } else {
                        throw new SQLException("Falha ao obter o ID_ESTADO.");
                    }
                }
                conn.close();
            }catch (SQLException e) {
                logError(e);
            }return idEstado;
        }
        return null;

    }

    public List<Integer> getIdCidade(Denuncia denuncia){

        String[] partes = denuncia.getLocalizacao().split(",");

        if (partes.length > 0) {
            var lat = Double.parseDouble(partes[0]);
            var lon = Double.parseDouble(partes[1]);

            var idCidade = new ArrayList<Integer>();
            try (var conn = new OracleDatabaseConfiguration().getConnection()){
                var stmtRetrieve = conn.prepareStatement("SELECT ID_CIDADE FROM "+
                        DenunciasRepository.TB_NAME_C+" WHERE NOME = '%s'"
                        .formatted(OpenStreetMapUtils.getInstance().getCidade(lat, lon)));
                try (var rs = stmtRetrieve.executeQuery()) {
                    if (rs.next()) {
                        idCidade.add(rs.getInt(1));
                    } else {
                        throw new SQLException("Falha ao obter o ID_CIDADE.");
                    }
                }
                conn.close();
            }catch (SQLException e) {
                logError(e);
            }return idCidade;
        }
        return null;

    }

    public List<Integer> getIdBairro(Denuncia denuncia){

        String[] partes = denuncia.getLocalizacao().split(",");

        if (partes.length > 0) {
            var lat = Double.parseDouble(partes[0]);
            var lon = Double.parseDouble(partes[1]);

            var idBairro = new ArrayList<Integer>();
            try (var conn = new OracleDatabaseConfiguration().getConnection()){
                var stmtRetrieve = conn.prepareStatement("SELECT ID_BAIRRO FROM "+
                        DenunciasRepository.TB_NAME_B+" WHERE NOME = '%s'"
                        .formatted(OpenStreetMapUtils.getInstance().getBairro(lat, lon)));
                try (var rs = stmtRetrieve.executeQuery()) {
                    if (rs.next()) {
                        idBairro.add(rs.getInt(1));
                    } else {
                        throw new SQLException("Falha ao obter o ID_BAIRRO.");
                    }
                }
                conn.close();
            }catch (SQLException e) {
                logError(e);
            }return idBairro;
        }
        return null;

    }

    public List<Integer> getIdLocalizacao(Denuncia denuncia){

        String[] partes = denuncia.getLocalizacao().split(",");

        if (partes.length > 0) {
            var lat = Double.parseDouble(partes[0]);
            var lon = Double.parseDouble(partes[1]);

            var idLocalizacao = new ArrayList<Integer>();
            try (var conn = new OracleDatabaseConfiguration().getConnection()){
                var stmtRetrieve = conn.prepareStatement("SELECT ID_LOCALIZACAO FROM "+
                        DenunciasRepository.TB_NAME_L+" WHERE ENDERECO = '%s'"
                        .formatted(OpenStreetMapUtils.getInstance().getEndereco(lat, lon)));
                try (var rs = stmtRetrieve.executeQuery()) {
                    if (rs.next()) {
                        idLocalizacao.add(rs.getInt(1));
                    } else {
                        throw new SQLException("Falha ao obter o ID_LOCALIZACAO.");
                    }
                }
                conn.close();
            }catch (SQLException e) {
                logError(e);
            }return idLocalizacao;
        }
        return null;

    }


    @Override
    public List<Denuncia> readAll(String orderBy, String direction, int limit, int offset) {

        try{var conn = new OracleDatabaseConfiguration().getConnection();

            var denuncias = new ArrayList<Denuncia>();

            var stmtDenuncia = conn.prepareStatement("SELECT * FROM "+DenunciasRepository.TB_NAME
                    +" ORDER BY " + orderBy + " " +(direction == null || direction.isEmpty() ? "ASC" : direction)
                    + " OFFSET "+offset+" ROWS FETCH NEXT "+ (limit == 0 ? 10 : limit) +" ROWS ONLY");
            var resultSetDenuncia = stmtDenuncia.executeQuery();

            while (resultSetDenuncia.next()) {

                var feedback = new ArrayList<Feedback>();
                var stmtFeedback = conn.prepareStatement("SELECT * FROM " +DenunciasRepository.TB_NAME_F+ " WHERE ID_FEEDBACK IN (SELECT ID_FEEDBACK FROM "
                        + DenunciasRepository.TB_NAME + " WHERE ID_DENUNCIA = %s)"
                        .formatted(resultSetDenuncia.getInt("ID_DENUNCIA")));
                var resultSetFeedback = stmtFeedback.executeQuery();
                while (resultSetFeedback.next()){
                    feedback.add(new Feedback(
                            resultSetFeedback.getInt("ID_FEEDBACK"),
                            resultSetFeedback.getString("STATUS"),
                            resultSetFeedback.getString("RETORNO"),
                            resultSetFeedback.getDate("DATA").toLocalDate()
                    ));
                }

                var comentario = new ArrayList<String>();
                var stmtComentario = conn.prepareStatement(
                        "SELECT * FROM " + DenunciasRepository.TB_NAME_CO+ " WHERE ID_COMENTARIO IN " +
                                "(SELECT ID_COMENTARIO FROM " + DenunciasRepository.TB_NAME + " WHERE ID_DENUNCIA = %s)"
                                .formatted(resultSetDenuncia.getInt("ID_DENUNCIA")));{
                    var resultSet = stmtComentario.executeQuery();
                    while (resultSet.next()) {
                        comentario.add(resultSet.getString("COMENTARIO"));
                    }
                }

                var localizacao = new ArrayList<String>();
                var stmtLocalizacao = conn.prepareStatement(
                        "SELECT ENDERECO FROM " + DenunciasRepository.TB_NAME_L+ " WHERE ID_LOCALIZACAO IN " +
                                "(SELECT ID_LOCALIZACAO FROM " + DenunciasRepository.TB_NAME + " WHERE ID_DENUNCIA = %s)"
                                .formatted(resultSetDenuncia.getInt("ID_DENUNCIA")));{
                    var resultSet = stmtLocalizacao.executeQuery();
                    while (resultSet.next()) {
                        localizacao.add(resultSet.getString("ENDERECO"));
                    }
                }

                String incidente = null;
                String query = "SELECT * FROM " + DenunciasRepository.TB_NAME_I + " WHERE ID_TIPO_INCIDENTE IN (SELECT ID_TIPO_INCIDENTE FROM " + DenunciasRepository.TB_NAME + " WHERE ID_DENUNCIA =?)";
                try {
                    var stmtIncidente = conn.prepareStatement(query);
                    stmtIncidente.setInt(1, resultSetDenuncia.getInt("ID_DENUNCIA"));
                    var resultIncidente = stmtIncidente.executeQuery();

                    while (resultIncidente.next()) {
                        String description = resultIncidente.getString("DESCRICAO");
                        String originResidue = resultIncidente.getString("ORIGEM_RESIDUO");
                        String occurrence = resultIncidente.getString("RECORRENCIA");

                        incidente = description +", "+ originResidue+ ", " + occurrence;

                    }
                } catch (SQLException e) {
                    logError(e);
                }


                var denuncia = new Denuncia();
                denuncia.setId(resultSetDenuncia.getInt("ID_DENUNCIA"));
                denuncia.setDescricao(resultSetDenuncia.getString("DESCRICAO"));
                denuncia.setData(resultSetDenuncia.getDate("DATA").toLocalDate());
                denuncia.setLocalizacao(localizacao.get(0));
                denuncia.setFeedback(feedback.get(0));

                if (incidente!= null) {
                    String[] dadosIncidente = incidente.split(", ");
                    denuncia.setTipoIncidente(dadosIncidente[0]);
                    if (dadosIncidente.length > 1) {
                        denuncia.setOrigemResiduo(dadosIncidente[1]);
                    }
                    if (dadosIncidente.length > 2) {
                        denuncia.setRecorrenciaProblema(dadosIncidente[2]);
                    }
                }

                if (comentario.isEmpty()){
                    denuncia.setComentariosAdicionais(null);
                }else {
                    denuncia.setComentariosAdicionais(comentario.get(0));
                }

                denuncias.add(denuncia);
            }
            return denuncias;
        } catch (SQLException e) {
            logError(e);
        }
        return null;
    }

    @Override
    public Optional<Denuncia> read(int id) {
        try{var conn = new OracleDatabaseConfiguration().getConnection();

            var stmtDenuncia = conn.prepareStatement("SELECT * FROM %s WHERE ID_DENUNCIA = %s".formatted(DenunciasRepository.TB_NAME, id));
            var resultSetDenuncia = stmtDenuncia.executeQuery();

            while (resultSetDenuncia.next()) {

                var feedback = new ArrayList<Feedback>();
                var stmtFeedback = conn.prepareStatement("SELECT * FROM " +DenunciasRepository.TB_NAME_F+ " WHERE ID_FEEDBACK IN (SELECT ID_FEEDBACK FROM "
                        + DenunciasRepository.TB_NAME + " WHERE ID_DENUNCIA = %s)"
                        .formatted(resultSetDenuncia.getInt("ID_DENUNCIA")));
                var resultSetFeedback = stmtFeedback.executeQuery();
                while (resultSetFeedback.next()){
                    feedback.add(new Feedback(
                            resultSetFeedback.getInt("ID_FEEDBACK"),
                            resultSetFeedback.getString("STATUS"),
                            resultSetFeedback.getString("RETORNO"),
                            resultSetFeedback.getDate("DATA").toLocalDate()
                    ));
                }

                var comentario = new ArrayList<String>();
                var stmtComentario = conn.prepareStatement(
                        "SELECT * FROM " + DenunciasRepository.TB_NAME_CO+ " WHERE ID_COMENTARIO IN " +
                                "(SELECT ID_COMENTARIO FROM " + DenunciasRepository.TB_NAME + " WHERE ID_DENUNCIA = %s)"
                                .formatted(resultSetDenuncia.getInt("ID_DENUNCIA")));{
                    var resultSet = stmtComentario.executeQuery();
                    while (resultSet.next()) {
                        comentario.add(resultSet.getString("COMENTARIO"));
                    }
                }

                var localizacao = new ArrayList<String>();
                var stmtLocalizacao = conn.prepareStatement(
                        "SELECT ENDERECO FROM " + DenunciasRepository.TB_NAME_L+ " WHERE ID_LOCALIZACAO IN " +
                                "(SELECT ID_LOCALIZACAO FROM " + DenunciasRepository.TB_NAME + " WHERE ID_DENUNCIA = %s)"
                                .formatted(resultSetDenuncia.getInt("ID_DENUNCIA")));{
                    var resultSet = stmtLocalizacao.executeQuery();
                    while (resultSet.next()) {
                        localizacao.add(resultSet.getString("ENDERECO"));
                    }
                }

                String incidente = null;
                String query = "SELECT * FROM " + DenunciasRepository.TB_NAME_I + " WHERE ID_TIPO_INCIDENTE IN (SELECT ID_TIPO_INCIDENTE FROM " + DenunciasRepository.TB_NAME + " WHERE ID_DENUNCIA =?)";
                try {
                    var stmtIncidente = conn.prepareStatement(query);
                    stmtIncidente.setInt(1, resultSetDenuncia.getInt("ID_DENUNCIA"));
                    var resultIncidente = stmtIncidente.executeQuery();

                    while (resultIncidente.next()) {
                        String description = resultIncidente.getString("DESCRICAO");
                        String originResidue = resultIncidente.getString("ORIGEM_RESIDUO");
                        String occurrence = resultIncidente.getString("RECORRENCIA");

                        incidente = description +", "+ originResidue+ ", " + occurrence;

                    }
                } catch (SQLException e) {
                    logError(e);
                }

                var denuncia = new Denuncia();
                denuncia.setId(resultSetDenuncia.getInt("ID_DENUNCIA"));
                denuncia.setDescricao(resultSetDenuncia.getString("DESCRICAO"));
                denuncia.setData(resultSetDenuncia.getDate("DATA").toLocalDate());
                denuncia.setLocalizacao(localizacao.get(0));

                if (incidente!= null) {
                    String[] dadosIncidente = incidente.split(", ");
                    denuncia.setTipoIncidente(dadosIncidente[0]);
                    if (dadosIncidente.length > 1) {
                        denuncia.setOrigemResiduo(dadosIncidente[1]);
                    }
                    if (dadosIncidente.length > 2) {
                        denuncia.setRecorrenciaProblema(dadosIncidente[2]);
                    }
                }

                if (feedback.isEmpty()){
                    denuncia.setFeedback(null);
                }else {
                    denuncia.setFeedback(feedback.get(0));
                }

                if (comentario.isEmpty()){
                    denuncia.setComentariosAdicionais(null);
                }else {
                    denuncia.setComentariosAdicionais(comentario.get(0));
                }
                return Optional.of(denuncia);
            }



            } catch (SQLException e) {
            logError(e);
        }

        return Optional.empty();
    }

    @Override
    public void update(int id, Denuncia obj) {
        var conn = new OracleDatabaseConfiguration().getConnection();


        var idIncidente = 0;
        String[] partes = obj.getLocalizacao().split(",");

        if (partes.length > 0) {
            var lat = Double.parseDouble(partes[0]);
            var lon = Double.parseDouble(partes[1]);


            try (var stmtVericador = conn.prepareStatement(
                    "SELECT COUNT(*) FROM %s WHERE %s = '%s'"
                            .formatted(DenunciasRepository.TB_NAME_E, "NOME",
                                    OpenStreetMapUtils.getInstance().getEstado(lat, lon)))) {
                var rsVerifcador = stmtVericador.executeQuery();

                while (rsVerifcador.next()) {
                    if (rsVerifcador.getInt(1) == 0) {
                        try (var stmtEstado = conn.prepareStatement("INSERT INTO " + DenunciasRepository.TB_NAME_E +
                                " (NOME) VALUES (?)")) {
                            stmtEstado.setString(1, OpenStreetMapUtils.getInstance().getEstado(lat, lon));
                            stmtEstado.executeUpdate();
                            logInfo("Dados inseridos na tabela " + DenunciasRepository.TB_NAME_E + " com sucesso!");
                        } catch (SQLException e) {
                            logError(e);
                        }
                    }
                }
            } catch (SQLException e) {
                logError(e);
            }

            try (var stmtVericador = conn.prepareStatement(
                    "SELECT COUNT(*) FROM %s WHERE %s = '%s'"
                            .formatted(DenunciasRepository.TB_NAME_C, "NOME",
                                    OpenStreetMapUtils.getInstance().getCidade(lat, lon)))) {
                var rsVerifcador = stmtVericador.executeQuery();

                while (rsVerifcador.next()) {
                    if (rsVerifcador.getInt(1) == 0) {
                        try (var stmtCidade = conn.prepareStatement("INSERT INTO " + DenunciasRepository.TB_NAME_C +
                                " (NOME, ID_ESTADO) VALUES (?,?)")) {
                            stmtCidade.setString(1, OpenStreetMapUtils.getInstance().getCidade(lat, lon));
                            stmtCidade.setInt(2, getIdEstado(obj).get(0));
                            stmtCidade.executeUpdate();
                            logInfo("Dados inseridos na tabela " + DenunciasRepository.TB_NAME_C + " com sucesso!");
                        } catch (SQLException e) {
                            logError(e);
                        }
                    }
                }
            } catch (SQLException e) {
                logError(e);
            }


            try (var stmtVericador = conn.prepareStatement(
                    "SELECT COUNT(*) FROM %s WHERE %s = '%s'"
                            .formatted(DenunciasRepository.TB_NAME_B, "NOME",
                                    OpenStreetMapUtils.getInstance().getBairro(lat, lon)))) {
                var rsVerifcador = stmtVericador.executeQuery();

                while (rsVerifcador.next()) {
                    if (rsVerifcador.getInt(1) == 0) {
                        try (var stmtBairro = conn.prepareStatement("INSERT INTO " + DenunciasRepository.TB_NAME_B +
                                " (NOME, ID_CIDADE) VALUES (?,?)")) {
                            stmtBairro.setString(1, OpenStreetMapUtils.getInstance().getBairro(lat, lon));
                            stmtBairro.setInt(2, getIdCidade(obj).get(0));
                            stmtBairro.executeUpdate();
                            logInfo("Dados inseridos na tabela " + DenunciasRepository.TB_NAME_B + " com sucesso!");
                        } catch (SQLException e) {
                            logError(e);
                        }
                    }
                }
            } catch (SQLException e) {
                logError(e);
            }

            try (var stmtVericador = conn.prepareStatement(
                    "SELECT COUNT(*) FROM %s WHERE %s = '%s'"
                            .formatted(DenunciasRepository.TB_NAME_L, "ENDERECO",
                                    OpenStreetMapUtils.getInstance().getEndereco(lat, lon)))) {
                var rsVerifcador = stmtVericador.executeQuery();

                while (rsVerifcador.next()) {
                    if (rsVerifcador.getInt(1) == 0) {
                        try (var stmtLocalizacao = conn.prepareStatement("UPDATE " + DenunciasRepository.TB_NAME_L +
                                " SET CEP = ?, ENDERECO = ?, ID_BAIRRO = ? WHERE ID_LOCALIZACAO IN (SELECT ID_LOCALIZACAO FROM " +
                                DenunciasRepository.TB_NAME + " WHERE ID_DENUNCIA = ?)")) {
                            stmtLocalizacao.setDouble(1, Double.parseDouble(OpenStreetMapUtils.getInstance().getCep(lat, lon).replace("-", "")));
                            stmtLocalizacao.setString(2, OpenStreetMapUtils.getInstance().getEndereco(lat, lon));
                            stmtLocalizacao.setInt(3, getIdBairro(obj).get(0));
                            stmtLocalizacao.setInt(4, id);
                            stmtLocalizacao.executeUpdate();
                            logInfo("Dados inseridos na tabela " + DenunciasRepository.TB_NAME_L + " com sucesso!");
                        } catch (SQLException e) {
                            logError(e);
                        }
                    }
                }
            } catch (SQLException e) {
                logError(e);
            }

        }

        if (obj.getFeedback() != null) {
            try (var stmtFeedback = conn.prepareStatement("UPDATE " + DenunciasRepository.TB_NAME_F +
                    " SET STATUS = ?, RETORNO = ?, DATA = ? WHERE ID_FEEDBACK IN (SELECT ID_FEEDBACK FROM " +
                    DenunciasRepository.TB_NAME + " WHERE ID_DENUNCIA = ?)")) {
                stmtFeedback.setString(1, obj.getFeedback().getStatus());
                stmtFeedback.setString(2, obj.getFeedback().getRetorno());
                stmtFeedback.setDate(3, Date.valueOf(LocalDate.now()));
                stmtFeedback.setInt(4, id);

                stmtFeedback.executeUpdate();

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        if (obj.getComentariosAdicionais() != null) {
            try (var stmtComentario = conn.prepareStatement("UPDATE " + DenunciasRepository.TB_NAME_CO +
                    " SET COMENTARIO = ? WHERE ID_COMENTARIO IN (SELECT ID_COMENTARIO FROM " +
                    DenunciasRepository.TB_NAME + " WHERE ID_DENUNCIA = ?)")) {
                stmtComentario.setString(1, obj.getComentariosAdicionais());
                stmtComentario.setInt(2, id);

                stmtComentario.executeUpdate();

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        }

        if (obj.getTipoIncidente() != null) {
            try (var stmtIncidente = conn.prepareStatement("UPDATE " + DenunciasRepository.TB_NAME_I +
                    " SET DESCRICAO = ?, ORIGEM_RESIDUO = ?, RECORRENCIA = ? WHERE ID_TIPO_INCIDENTE IN " +
                    "(SELECT ID_TIPO_INCIDENTE FROM " + DenunciasRepository.TB_NAME +
                    " WHERE ID_DENUNCIA = ?)")) {

                stmtIncidente.setString(1, obj.getTipoIncidente());
                stmtIncidente.setString(2, obj.getOrigemResiduo());
                stmtIncidente.setString(3, obj.getRecorrenciaProblema());
                stmtIncidente.setInt(4, id);

                stmtIncidente.executeUpdate();

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }


        try (var stmtDenuncia = conn.prepareStatement("UPDATE " + DenunciasRepository.TB_NAME +
                " SET DATA = ?, DESCRICAO = ?, ID_LOCALIZACAO = ? WHERE ID_DENUNCIA = ?")) {

            stmtDenuncia.setDate(1, Date.valueOf(LocalDate.now()));
            stmtDenuncia.setString(2, obj.getDescricao());
            stmtDenuncia.setInt(3, getIdLocalizacao(obj).get(0));
            stmtDenuncia.setInt(4, id);
            stmtDenuncia.executeUpdate();

        } catch (SQLException e) {
            logError(e);
        }
    }

    @Override
    public void delete(int id) {
        try (var conn = new OracleDatabaseConfiguration().getConnection()) {
            try {var stmt = conn.prepareStatement("DELETE FROM " + DenunciasRepository.TB_NAME_I +
                    " WHERE ID_TIPO_INCIDENTE IN (SELECT ID_TIPO_INCIDENTE FROM " + DenunciasRepository.TB_NAME +
                    " WHERE ID_DENUNCIA =?)");
                stmt.setInt(1, id);
                stmt.executeUpdate();
                logWarn("Incidente deletado com sucesso");
            } catch (SQLException e) {
                logError(e);
            }

            try {
                var stmt = conn.prepareStatement("DELETE FROM " +DenunciasRepository.TB_NAME_CO +
                        " WHERE ID_COMENTARIO IN (SELECT ID_COMENTARIO FROM " + DenunciasRepository.TB_NAME +
                        " WHERE ID_DENUNCIA =?)");
                stmt.setInt(1, id);
                stmt.executeUpdate();
                logWarn("Comentário deletado com sucesso");
            } catch (SQLException e) {
                logError(e);
            }

            try {var stmt = conn.prepareStatement("DELETE FROM " + DenunciasRepository.TB_NAME_F +
                    " WHERE ID_FEEDBACK IN (SELECT ID_FEEDBACK FROM " + DenunciasRepository.TB_NAME +
                    " WHERE ID_DENUNCIA = ? AND ID_FEEDBACK != 1)");
                stmt.setInt(1, id);
                stmt.executeUpdate();
                logWarn("Feedback deletado com sucesso");
            } catch (SQLException e) {
                logError(e);
            }

            try {
                var stmt = conn.prepareStatement("DELETE FROM " +DenunciasRepository.TB_NAME +
                        " WHERE ID_DENUNCIA = ?");
                stmt.setInt(1, id);
                stmt.executeUpdate();
                logWarn("Denuncia deletado com sucesso");
                conn.close();
            } catch (SQLException e) {
                logError(e);
            }
        }catch (SQLException e) {
            logError(e);
        }
    }
}
