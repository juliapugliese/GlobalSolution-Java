package org.example.repositories;

import org.example.entities.ServicoModel.Denuncia;
import org.example.entities.UsuarioModel.Denunciante;
import org.example.infrastructure.OpenStreetMapUtils;
import org.example.infrastructure.OracleDatabaseConfiguration;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DenunciantesRepository  extends Starter implements _BaseRepository<Denunciante>, _Logger<String>{

    public static final String TB_NAME = "DENUNCIANTE_JAVA";

    public List<Integer> getIdTipoIncidente(Denunciante denunciante){
        var idTipoIncidente = new ArrayList<Integer>();
        try {var conn = new OracleDatabaseConfiguration().getConnection();
            var stmtGetId = conn.prepareStatement(
                    "SELECT ID_TIPO_INCIDENTE FROM " + DenunciasRepository.TB_NAME_I+ " WHERE ID_TIPO_INCIDENTE IN " +
                            "(SELECT ID_TIPO_INCIDENTE FROM " + TB_NAME + " WHERE ID_DENUNCIANTE = %s)"
                            .formatted(getIdDenunciante(denunciante).get(0)));{
                var resultSet = stmtGetId.executeQuery();
                while (resultSet.next()) {
                    idTipoIncidente.add(resultSet.getInt("ID_DENUNCIANTE"));
                }
            }
            conn.close();
        }catch (SQLException e) {
            logError(e);
        }
        return idTipoIncidente;
    }
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


        var idCargo = new ArrayList<Integer>();
        try (var conn = new OracleDatabaseConfiguration().getConnection()){
            var stmtRetrieve = conn.prepareStatement("SELECT ID_ESTADO FROM "+ DenunciasRepository.TB_NAME_E+" WHERE NOME = '%s'".formatted(denuncia.get()));
            try (var rs = stmtRetrieve.executeQuery()) {
                if (rs.next()) {
                    idCargo.add(rs.getInt(1));
                } else {
                    throw new SQLException("Falha ao obter o COD_CARGO.");
                }
            }
            conn.close();
        }catch (SQLException e) {
            logError(e);
        }
        return idCargo;
    }



    @Override
    public void create(Denunciante obj) {
        try{var conn = new OracleDatabaseConfiguration().getConnection();
            var stmt = conn.prepareStatement("INSERT INTO " + TB_NAME + " (NOME, EMAIL, TELEFONE) VALUES (?,?,?)");
            stmt.setString(1, obj.getNome());
            stmt.setString(2, obj.getEmail());
            stmt.setString(2, obj.getTelefone());
            stmt.executeUpdate();
            logInfo("Denunciante cadastrado com sucesso");


            obj.getDenuncias().forEach(denuncia ->{
                String[] partes = denuncia.getLocalizacao().split(",");

                if (partes.length > 0) {
                    var lat = Double.parseDouble(partes[0]);
                    var lon = Double.parseDouble(partes[1]);





                    try (var stmtVericador = conn.prepareStatement(
                            "SELECT COUNT(*) FROM %s WHERE %s = '%s'".formatted(UsuariosRepository.TB_NAME_CA, TB_COLUMNS.get("NOME_CARGO"), ((Cliente) usuario).getCargo())
                    )){
                        var rsVerifcador = stmtVericador.executeQuery();

                        while (rsVerifcador.next()) {
                            if(rsVerifcador.getInt(1) == 0){
                                try (var stmt = conn.prepareStatement(
                                        "INSERT INTO %s(%s) VALUES (?)".formatted(UsuariosRepository.TB_NAME_CA, TB_COLUMNS.get("NOME_CARGO")))) {
                                    stmt.setString(1, ((Cliente) usuario).getCargo());
                                    stmt.executeUpdate();

                                    logInfo("Dados inseridos na tabela "+ UsuariosRepository.TB_NAME_CA +"  com sucesso!");
                                } catch (SQLException e) {
                                    logError(e);
                                }
                            }
                        }
                    }







                    try (var stmtEstado =  conn.prepareStatement("INSERT INTO " + DenunciasRepository.TB_NAME_E +
                            " (NOME) VALUES (?)")){
                        stmtEstado.setString(1, OpenStreetMapUtils.getInstance().getEstado(lat, lon));
                    }catch (SQLException e) {
                        logError(e);
                    }

                    try (var stmtCidade =  conn.prepareStatement("INSERT INTO " + DenunciasRepository.TB_NAME_C +
                            " (NOME, ID_ESTADO) VALUES (?,?)")){
                        stmtCidade.setString(1, OpenStreetMapUtils.getInstance().getCidade(lat, lon));
                        stmtCidade.setInt(2, );
                    }catch (SQLException e) {
                        logError(e);
                    }

                    try (var stmtBairro =  conn.prepareStatement("INSERT INTO " + DenunciasRepository.TB_NAME_B +
                            " (NOME, ID_CIDADE) VALUES (?,?)")){
                        stmtBairro.setString(1, OpenStreetMapUtils.getInstance().getBairro(lat, lon));
                        stmtBairro.setInt(2, );
                    }catch (SQLException e) {
                        logError(e);
                    }


                    try (var stmtLocalizacao =  conn.prepareStatement("INSERT INTO " + DenunciasRepository.TB_NAME_L +
                            " (CEP, ENDERECO, ID_BAIRRO) VALUES (?,?,?)")){
                        stmtLocalizacao.setDouble(1, Double.parseDouble(OpenStreetMapUtils.getInstance().getCep(lat, lon)));
                        stmtLocalizacao.setString(2, OpenStreetMapUtils.getInstance().getAddress(lat, lon));
                        stmtLocalizacao.setInt(3, );
                    }catch (SQLException e) {
                        logError(e);
                    }
                }

                try (var stmtDenuncia =  conn.prepareStatement("INSERT INTO " + DenunciasRepository.TB_NAME +
                        " (DATA_HORA, DESCRICAO, ID_DENUNCIANTE, ID_LOCALIZACAO, ID_TIPO_INCIDENTE, ID_COMENTARIO, ID_FEEDBACK) " +
                        "VALUES (?,?,?,?,?,?,?)")){
                    stmtDenuncia.setDate(1, Date.valueOf(LocalDate.now()));
                    stmtDenuncia.setString(2, denuncia.getDescricao());
                    stmtDenuncia.setInt(3, getIdDenunciante(obj).get(0));
                    stmtDenuncia.setInt(4, getIdTipoIncidente(obj).get(0));
//                    stmtDenuncia.setInt(5, );
//                    stmtDenuncia.setInt(6, );
//                    stmtDenuncia.setInt(7, );

                    stmtDenuncia.executeUpdate();
                    logInfo("Dados inseridos na tabela "+ DenunciasRepository.TB_NAME +" com sucesso!");

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
        return null;
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
