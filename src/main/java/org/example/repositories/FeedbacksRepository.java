package org.example.repositories;


import org.example.entities.ServicoModel.Feedback;
import org.example.entities.UsuarioModel.Denunciante;
import org.example.infrastructure.OracleDatabaseConfiguration;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FeedbacksRepository extends Starter implements _BaseRepository<Feedback>, _Logger<String>{

    public static final String TB_NAME = "FEEDBACK_JAVA";

//    public List<Integer> getIdFeedback(Denunciante denunciante){
//        var idDenunciante = new ArrayList<Integer>();
//        try {var conn = new OracleDatabaseConfiguration().getConnection();
//            var stmtGetId = conn.prepareStatement(
//                    "SELECT * FROM %s WHERE %s = '%s'"
//                            .formatted(TB_NAME, "EMAIL", denunciante.getEmail()));{
//                var resultSet = stmtGetId.executeQuery();
//                while (resultSet.next()) {
//                    idDenunciante.add(resultSet.getInt("ID_DENUNCIANTE"));
//                }
//            }
//            conn.close();
//        }catch (SQLException e) {
//            logError(e);
//        }
//        return idDenunciante;
//    }


    @Override
    public void create(Feedback obj) {

    }

    @Override
    public List<Feedback> readAll(String orderBy, String direction, int limit, int offset) {
        return null;
    }

    @Override
    public Optional<Feedback> read(int id) {
        return Optional.empty();
    }

    @Override
    public void update(int id, Feedback obj) {

    }

    @Override
    public void delete(int id) {

    }
}
