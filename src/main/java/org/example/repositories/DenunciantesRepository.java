package org.example.repositories;

import org.example.entities.UsuarioModel.Denunciante;
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
                try (var stmtLocalizacao =  conn.prepareStatement("INSERT INTO " + DenunciasRepository.TB_NAME_L +
                        " (CEP, ENDERECO, REFERENCIA, ID_BAIRRO) VALUES (?,?,?,?)")){
//                    stmtLocalizacao.setInt(1, denuncia.getLocalizacao());

                }catch (SQLException e) {
                    logError(e);
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
