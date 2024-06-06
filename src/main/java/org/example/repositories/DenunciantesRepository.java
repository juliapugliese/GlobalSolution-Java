package org.example.repositories;

import org.example.entities.ServicoModel.Denuncia;
import org.example.entities.ServicoModel.Feedback;
import org.example.entities.UsuarioModel.Denunciante;
import org.example.entities._BaseEntity;
import org.example.infrastructure.OpenStreetMapUtils;
import org.example.infrastructure.OracleDatabaseConfiguration;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class DenunciantesRepository  extends Starter implements _BaseRepository<Denunciante>, _Logger<String>{

    public static final String TB_NAME = "DENUNCIANTE_JAVA";

    public List<Integer> getIdDenunciante(Denunciante denunciante){
        var idDenunciante = new ArrayList<Integer>();
        try {var conn = new OracleDatabaseConfiguration().getConnection();
            var stmtGetId = conn.prepareStatement(
                    "SELECT * FROM %s WHERE %s = '%s'"
                            .formatted(TB_NAME, "EMAIL", denunciante.getEmail()));{
                var resultSet = stmtGetId.executeQuery();
                while (resultSet.next()) {
                    idDenunciante.add(resultSet.getInt("ID_DENUNCIANTE"));
                }
            }
            conn.close();
        }catch (SQLException e) {
            logError(e);
        }
        return idDenunciante;
    }
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
    public void create(Denunciante obj) {
        try{var conn = new OracleDatabaseConfiguration().getConnection();
            var stmt = conn.prepareStatement("INSERT INTO " + TB_NAME + " (NOME, EMAIL, TELEFONE) VALUES (?,?,?)");
            stmt.setString(1, obj.getNome());
            stmt.setString(2, obj.getEmail());
            stmt.setString(3, obj.getTelefone());
            stmt.executeUpdate();
            logInfo("Denunciante cadastrado com sucesso");


            obj.getDenuncias().forEach(denuncia ->{
                int idFeedback = 0;
                int idComentario = 0;
                var idIncidente = 0;
                String[] partes = denuncia.getLocalizacao().split(",");

                if (partes.length > 0) {
                    var lat = Double.parseDouble(partes[0]);
                    var lon = Double.parseDouble(partes[1]);


                    try (var stmtVericador = conn.prepareStatement(
                            "SELECT COUNT(*) FROM %s WHERE %s = '%s'"
                                    .formatted(DenunciasRepository.TB_NAME_E, "NOME",
                                            OpenStreetMapUtils.getInstance().getEstado(lat, lon)))) {
                        var rsVerifcador = stmtVericador.executeQuery();

                        while (rsVerifcador.next()) {
                            if(rsVerifcador.getInt(1) == 0){
                                try (var stmtEstado =  conn.prepareStatement("INSERT INTO " + DenunciasRepository.TB_NAME_E +
                                        " (NOME) VALUES (?)")){
                                    stmtEstado.setString(1, OpenStreetMapUtils.getInstance().getEstado(lat, lon));
                                    stmtEstado.executeUpdate();
                                    logInfo("Dados inseridos na tabela "+ DenunciasRepository.TB_NAME_E +" com sucesso!");
                                }catch (SQLException e) {
                                    logError(e);
                                }
                            }
                        }
                    }
                    catch (SQLException e) {
                        logError(e);
                    }

                    try (var stmtVericador = conn.prepareStatement(
                            "SELECT COUNT(*) FROM %s WHERE %s = '%s'"
                                    .formatted(DenunciasRepository.TB_NAME_C, "NOME",
                                            OpenStreetMapUtils.getInstance().getCidade(lat, lon)))) {
                        var rsVerifcador = stmtVericador.executeQuery();

                        while (rsVerifcador.next()) {
                            if(rsVerifcador.getInt(1) == 0){
                                try (var stmtCidade =  conn.prepareStatement("INSERT INTO " + DenunciasRepository.TB_NAME_C +
                                        " (NOME, ID_ESTADO) VALUES (?,?)")){
                                    stmtCidade.setString(1, OpenStreetMapUtils.getInstance().getCidade(lat, lon));
                                    stmtCidade.setInt(2, getIdEstado(denuncia).get(0));
                                    stmtCidade.executeUpdate();
                                    logInfo("Dados inseridos na tabela "+ DenunciasRepository.TB_NAME_C +" com sucesso!");
                                }catch (SQLException e) {
                                    logError(e);
                                }
                            }
                        }
                    }
                    catch (SQLException e) {
                        logError(e);
                    }


                    try (var stmtVericador = conn.prepareStatement(
                            "SELECT COUNT(*) FROM %s WHERE %s = '%s'"
                                    .formatted(DenunciasRepository.TB_NAME_B, "NOME",
                                            OpenStreetMapUtils.getInstance().getBairro(lat, lon)))) {
                        var rsVerifcador = stmtVericador.executeQuery();

                        while (rsVerifcador.next()) {
                            if(rsVerifcador.getInt(1) == 0){
                                try (var stmtBairro =  conn.prepareStatement("INSERT INTO " + DenunciasRepository.TB_NAME_B +
                                        " (NOME, ID_CIDADE) VALUES (?,?)")){
                                    stmtBairro.setString(1, OpenStreetMapUtils.getInstance().getBairro(lat, lon));
                                    stmtBairro.setInt(2, getIdCidade(denuncia).get(0));
                                    stmtBairro.executeUpdate();
                                    logInfo("Dados inseridos na tabela "+ DenunciasRepository.TB_NAME_B +" com sucesso!");
                                }catch (SQLException e) {
                                    logError(e);
                                }
                            }
                        }
                    }
                    catch (SQLException e) {
                        logError(e);
                    }

                    try (var stmtVericador = conn.prepareStatement(
                            "SELECT COUNT(*) FROM %s WHERE %s = '%s'"
                                    .formatted(DenunciasRepository.TB_NAME_L, "ENDERECO",
                                            OpenStreetMapUtils.getInstance().getEndereco(lat, lon)))) {
                        var rsVerifcador = stmtVericador.executeQuery();

                        while (rsVerifcador.next()) {
                            if(rsVerifcador.getInt(1) == 0){
                                try (var stmtLocalizacao =  conn.prepareStatement("INSERT INTO " + DenunciasRepository.TB_NAME_L +
                                        " (CEP, ENDERECO, ID_BAIRRO) VALUES (?,?,?)")){
                                    stmtLocalizacao.setDouble(1, Double.parseDouble(OpenStreetMapUtils.getInstance().getCep(lat, lon).replace("-", "")));
                                    stmtLocalizacao.setString(2, OpenStreetMapUtils.getInstance().getEndereco(lat, lon));
                                    stmtLocalizacao.setInt(3, getIdBairro(denuncia).get(0));
                                    stmtLocalizacao.executeUpdate();
                                    logInfo("Dados inseridos na tabela "+ DenunciasRepository.TB_NAME_L +" com sucesso!");
                                }catch (SQLException e) {
                                    logError(e);
                                }
                            }
                        }
                    }
                    catch (SQLException e) {
                        logError(e);
                    }

                }

                if (denuncia.getFeedback()!=null){
                    try (var stmtFeedback = conn.prepareStatement("INSERT INTO " + FeedbacksRepository.TB_NAME +
                            " (STATUS, RETORNO, DATA) VALUES (?,?,?)", new String[]{"ID_FEEDBACK"})) {
                        stmtFeedback.setString(1, denuncia.getFeedback().getStatus());
                        stmtFeedback.setString(2, denuncia.getFeedback().getRetorno());
                        stmtFeedback.setDate(3, Date.valueOf(LocalDate.now()));

                        int affectedRows = stmtFeedback.executeUpdate();

                        if (affectedRows > 0) {
                            ResultSet rs = stmtFeedback.getGeneratedKeys();
                            if (rs.next()) {
                                idFeedback = rs.getInt(1);
                            }
                        }
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }

                if (denuncia.getComentariosAdicionais()!=null){
                    try (var stmtComentario = conn.prepareStatement("INSERT INTO " + DenunciasRepository.TB_NAME_CO +
                            " (COMETARIO) VALUES (?)", new String[]{"ID_COMENTARIO"})) {

                        stmtComentario.setNull(1, Types.VARCHAR);

                        int affectedRows = stmtComentario.executeUpdate();
//                        logInfo("Dados inseridos na tabela "+ DenunciasRepository.TB_NAME_CO +" com sucesso!");

                        if (affectedRows > 0) {
                            ResultSet rs = stmtComentario.getGeneratedKeys();
                            if (rs.next()) {
                                idComentario = rs.getInt(1);
                            }
                        }
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }

                }

                if (denuncia.getTipoIncidente()!=null){
                    try (var stmtIncidente = conn.prepareStatement("INSERT INTO " + DenunciasRepository.TB_NAME_I +
                            " (DESCRICAO, ORIGEM_RESIDUO, RECORRENCIA) VALUES (?,?,?)", new String[]{"ID_TIPO_INCIDENTE"})) {

                        stmtIncidente.setString(1, denuncia.getTipoIncidente());
                        stmtIncidente.setString(2, denuncia.getOrigemResiduo());
                        stmtIncidente.setString(3, denuncia.getRecorrenciaProblema());

                        int affectedRows = stmtIncidente.executeUpdate();

                        if (affectedRows > 0) {
                            ResultSet rs = stmtIncidente.getGeneratedKeys();
                            if (rs.next()) {
                                idIncidente = rs.getInt(1);
                            }
                        }
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }


                try (var stmtDenuncia =  conn.prepareStatement("INSERT INTO " + DenunciasRepository.TB_NAME +
                        " (DATA, DESCRICAO, ID_DENUNCIANTE, ID_LOCALIZACAO, ID_TIPO_INCIDENTE, ID_COMENTARIO, ID_FEEDBACK) " +
                        "VALUES (?,?,?,?,?,?,?)")){

                    stmtDenuncia.setDate(1, Date.valueOf(LocalDate.now()));
                    stmtDenuncia.setString(2, denuncia.getDescricao());
                    stmtDenuncia.setInt(3, getIdDenunciante(obj).get(0));
                    stmtDenuncia.setInt(4, getIdLocalizacao(denuncia).get(0));
                    stmtDenuncia.setInt(5, idIncidente);
                    if (denuncia.getComentariosAdicionais().isEmpty()) {
                        stmtDenuncia.setNull(6, Types.INTEGER);
                    } else {
                        stmtDenuncia.setInt(6, idComentario);
                    }
                    if (denuncia.getFeedback() == null) {
                        stmtDenuncia.setNull(7, Types.INTEGER);
                    } else {
                        stmtDenuncia.setInt(7, idFeedback);
                    }
                    stmtDenuncia.executeUpdate();

                } catch (SQLException e) {
                    logError(e);
                }

            });

            conn.close();
        }
        catch (SQLException e) {
            logError(e);
        }

    }

    @Override
    public List<Denunciante> readAll(String orderBy, String direction, int limit, int offset) {
        var denunciantes = new ArrayList<Denunciante>();
        try{var conn = new OracleDatabaseConfiguration().getConnection();
            var stmt = conn.prepareStatement("SELECT * FROM " + TB_NAME +" ORDER BY " + orderBy + " " +
                    (direction == null || direction.isEmpty() ? "ASC" : direction)
                    + " OFFSET "+offset+" ROWS FETCH NEXT "+ (limit == 0 ? 10 : limit) +" ROWS ONLY");
            var rs = stmt.executeQuery();
            while(rs.next()){

                var denuncias = new ArrayList<Denuncia>();

                var stmtDenuncia = conn.prepareStatement("SELECT * FROM %s WHERE ID_DENUNCIANTE = %s".formatted(DenunciasRepository.TB_NAME, rs.getString("ID_DENUNCIANTE")));
                var resultSetDenuncia = stmtDenuncia.executeQuery();

                while (resultSetDenuncia.next()) {

                    var feedback = new ArrayList<Feedback>();
                    var stmtFeedback = conn.prepareStatement("SELECT * FROM " +FeedbacksRepository.TB_NAME+ " WHERE ID_FEEDBACK IN (SELECT ID_FEEDBACK FROM "
                            + DenunciasRepository.TB_NAME + " WHERE ID_DENUNCIA = %s)"
                            .formatted(resultSetDenuncia.getInt("ID_DENUNCIA")));
                    var resultSetFeedback = stmtFeedback.executeQuery();
                    while (resultSetFeedback.next()){
                        feedback.add(new Feedback(
                                resultSetFeedback.getString("STATUS"),
                                resultSetFeedback.getString("RETORNO"),
                                resultSetFeedback.getDate("DATA").toLocalDate()
                        ));
                    }

                    var comentario = new ArrayList<String>();
                    var stmtComentario = conn.prepareStatement(
                            "SELECT COMETARIO FROM " + DenunciasRepository.TB_NAME_CO+ " WHERE ID_COMENTARIO IN " +
                                    "(SELECT ID_COMENTARIO FROM " + DenunciasRepository.TB_NAME + " WHERE ID_DENUNCIA = %s)"
                                    .formatted(resultSetDenuncia.getInt("ID_DENUNCIA")));{
                        var resultSet = stmtComentario.executeQuery();
                        while (resultSet.next()) {
                            comentario.add(resultSet.getString("COMETARIO"));
                        }
                    }

                    var localizacao = new ArrayList<String>();
                    var stmtLocalizacao = conn.prepareStatement(
                            "SELECT ENDERECO FROM " + DenunciasRepository.TB_NAME_CO+ " WHERE ID_LOCALIZACAO IN " +
                                    "(SELECT ID_LOCALIZACAO FROM " + DenunciasRepository.TB_NAME + " WHERE ID_DENUNCIA = %s)"
                                    .formatted(resultSetDenuncia.getInt("ID_DENUNCIA")));{
                        var resultSet = stmtLocalizacao.executeQuery();
                        while (resultSet.next()) {
                            localizacao.add(resultSet.getString("ENDERECO"));
                        }
                    }

                    var incidente = new ArrayList<String>();
                    var stmtIncidente = conn.prepareStatement("SELECT * FROM " + DenunciasRepository.TB_NAME_I+ " WHERE ID_TIPO_INCIDENTE IN (SELECT ID_TIPO_INCIDENTE FROM "
                            + DenunciasRepository.TB_NAME + " WHERE ID_DENUNCIA = %s)"
                            .formatted(resultSetDenuncia.getInt("ID_DENUNCIA")));
                    var resultIncidente = stmtIncidente.executeQuery();
                    while (resultIncidente.next()){
                        incidente.add(
                                resultIncidente.getString("DESCRICAO") +
                                        resultIncidente.getString("ORIGEM_RESIDUO") +
                                        resultIncidente.getString("RECORRENCIA")
                        );
                    }


                    var denuncia = new Denuncia();
                    denuncia.setId(resultSetDenuncia.getInt("ID_DENUNCIA"));
                    denuncia.setDescricao(resultSetDenuncia.getString("DESCRICAO"));
                    denuncia.setData(resultSetDenuncia.getDate("DATA").toLocalDate());
                    denuncia.setComentariosAdicionais(comentario.get(0));
                    denuncia.setLocalizacao(localizacao.get(0));
                    denuncia.setTipoIncidente(incidente.get(0));
                    denuncia.setOrigemResiduo(incidente.get(1));
                    denuncia.setRecorrenciaProblema(incidente.get(2));
                    if (feedback.isEmpty()){
                        denuncia.setFeedback(null);
                    }
                    else {
                        denuncia.setFeedback(feedback.get(0));
                    }
                    denuncias.add(denuncia);

                }

                Denunciante denunciante = new Denunciante();
                denunciante.setId(rs.getInt("ID_DENUNCIANTE"));
                denunciante.setNome(rs.getString("NOME"));
                denunciante.setEmail(rs.getString("EMAIL"));
                denunciante.setTelefone(rs.getString("TELEFONE"));
                if (denuncias.isEmpty()){
                    denunciante.setDenuncias(null);
                }
                else {
                    denunciante.setDenuncias(denuncias);
                }


                denunciantes.add(denunciante);
            }
            conn.close();
        }
        catch (SQLException e) {
            logError(e);

        }
        denunciantes.sort(Comparator.comparingInt(_BaseEntity::getId));
        logInfo("Lendo denunciantes: " + denunciantes);
        return denunciantes;
    }


    public List<Denunciante> readAll1() {
        var denunciantes = new ArrayList<Denunciante>();
        try{var conn = new OracleDatabaseConfiguration().getConnection();
            var stmt = conn.prepareStatement("SELECT * FROM " + TB_NAME +" " );
            var rs = stmt.executeQuery();
            while(rs.next()){

                var denuncias = new ArrayList<Denuncia>();

                var stmtDenuncia = conn.prepareStatement("SELECT * FROM %s WHERE ID_DENUNCIANTE = %s".formatted(DenunciasRepository.TB_NAME, rs.getString("ID_DENUNCIANTE")));
                var resultSetDenuncia = stmtDenuncia.executeQuery();

                while (resultSetDenuncia.next()) {

                    var feedback = new ArrayList<Feedback>();
                    var stmtFeedback = conn.prepareStatement("SELECT * FROM " +FeedbacksRepository.TB_NAME+ " WHERE ID_FEEDBACK IN (SELECT ID_FEEDBACK FROM "
                            + DenunciasRepository.TB_NAME + " WHERE ID_DENUNCIA = %s)"
                            .formatted(resultSetDenuncia.getInt("ID_DENUNCIA")));
                    var resultSetFeedback = stmtFeedback.executeQuery();
                    while (resultSetFeedback.next()){
                        feedback.add(new Feedback(
                                resultSetFeedback.getString("STATUS"),
                                resultSetFeedback.getString("RETORNO"),
                                resultSetFeedback.getDate("DATA").toLocalDate()
                        ));
                        System.out.println(feedback);
                    }

                    var comentario = new ArrayList<String>();
                    var stmtComentario = conn.prepareStatement(
                            "SELECT COMETARIO FROM " + DenunciasRepository.TB_NAME_CO+ " WHERE ID_COMENTARIO IN " +
                                    "(SELECT ID_COMENTARIO FROM " + DenunciasRepository.TB_NAME + " WHERE ID_DENUNCIA = %s)"
                                    .formatted(resultSetDenuncia.getInt("ID_DENUNCIA")));{
                        var resultSet = stmtComentario.executeQuery();
                        while (resultSet.next()) {
                            comentario.add(resultSet.getString("COMETARIO"));
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

                    var incidente = new ArrayList<String>();

                    String query = "SELECT * FROM " + DenunciasRepository.TB_NAME_I + " WHERE ID_TIPO_INCIDENTE IN (SELECT ID_TIPO_INCIDENTE FROM " + DenunciasRepository.TB_NAME + " WHERE ID_DENUNCIA =?)";
                    try {
                        var stmtIncidente = conn.prepareStatement(query);
                        stmtIncidente.setInt(1, resultSetDenuncia.getInt("ID_DENUNCIA")); // Set the parameter value
                        var resultIncidente = stmtIncidente.executeQuery();

                        while (resultIncidente.next()) {
                            String description = resultIncidente.getString("DESCRICAO");
                            String originResidue = resultIncidente.getString("ORIGEM_RESIDUO");
                            String occurrence = resultIncidente.getString("RECORRENCIA");

                            incidente.add(description + originResidue + occurrence);

                            System.out.println(incidente.get(incidente.size() - 1));
                        }
                    } catch (SQLException e) {
                        logError(e);
                    }


                    var denuncia = new Denuncia();
                    denuncia.setId(resultSetDenuncia.getInt("ID_DENUNCIA"));
                    denuncia.setDescricao(resultSetDenuncia.getString("DESCRICAO"));
                    denuncia.setData(resultSetDenuncia.getDate("DATA").toLocalDate());
                    denuncia.setComentariosAdicionais(comentario.get(0));
                    denuncia.setLocalizacao(localizacao.get(0));

                    if (!incidente.isEmpty()) {
                    denuncia.setTipoIncidente(incidente.get(0));
                    denuncia.setOrigemResiduo(incidente.get(1));
                    denuncia.setRecorrenciaProblema(incidente.get(2));
                    }
                    if (feedback.isEmpty()){
                        denuncia.setFeedback(null);
                    }
                    else {
                        denuncia.setFeedback(feedback.get(0));
                    }
                    denuncias.add(denuncia);

                }

                Denunciante denunciante = new Denunciante();
                denunciante.setId(rs.getInt("ID_DENUNCIANTE"));
                denunciante.setNome(rs.getString("NOME"));
                denunciante.setEmail(rs.getString("EMAIL"));
                denunciante.setTelefone(rs.getString("TELEFONE"));
                if (denuncias.isEmpty()){
                    denunciante.setDenuncias(null);
                }
                else {
                    denunciante.setDenuncias(denuncias);
                }


                denunciantes.add(denunciante);
            }
            conn.close();
        }
        catch (SQLException e) {
            logError(e);

        }
        denunciantes.sort(Comparator.comparingInt(_BaseEntity::getId));
        logInfo("Lendo denunciantes: " + denunciantes);
        System.out.println(denunciantes);
        return denunciantes;
    }













    @Override
    public Optional<Denunciante> read(int id) {
        return Optional.empty();
    }

    @Override
    public void update(int id, Denunciante obj) {

    }

    @Override
    public void delete(int id) {

    }
}
