package org.example.repositories;

import org.example.entities.ServicoModel.Denuncia;
import org.example.entities.UsuarioModel.Denunciante;
import org.example.infrastructure.OracleDatabaseConfiguration;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
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



//    public List<Integer> getIdLocalizacao(Denuncia denuncia){
//        var idLocalizacao = new ArrayList<Integer>();
//        try {var conn = new OracleDatabaseConfiguration().getConnection();
//            var stmtGetId = conn.prepareStatement(
//                    "SELECT * FROM %s WHERE %s = '%s'"
//                            .formatted(TB_NAME_L, "ENDERECO", denuncia.getLocalizacao()));{
//                var resultSet = stmtGetId.executeQuery();
//                while (resultSet.next()) {
//                    idLocalizacao.add(resultSet.getInt("ID_LOCALIZACAO"));
//                }
//            }
//            conn.close();
//        }catch (SQLException e) {
//            logError(e);
//        }
//        return idLocalizacao;
//    }
//
//    public List<Integer> getIdComentario(Denuncia denuncia){
//        var idDenunciante = new ArrayList<Integer>();
//        try {var conn = new OracleDatabaseConfiguration().getConnection();
//            var stmtGetId = conn.prepareStatement(
//                    "SELECT * FROM %s WHERE %s = '%s'"
//                            .formatted(TB_NAME, "EMAIL", denuncia.getEmail()));{
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

    public void lerImagem(String img){
        try {
            BufferedImage imagem = ImageIO.read(new File(""));
            ImageIO.write(imagem, "PNG", new File("caminho/para/salvar/imagem.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void create(Denuncia obj) {
        var conn = new OracleDatabaseConfiguration().getConnection();
        {
            try (var stmtLocalizacao = conn.prepareStatement("INSERT INTO " + DenunciasRepository.TB_NAME_L +
                    " (CEP, ENDERECO, REFERENCIA, ID_BAIRRO) VALUES (?,?,?,?)")) {
//                    stmtLocalizacao.setInt(1, denuncia.getLocalizacao());

            } catch (SQLException e) {
                logError(e);
            }
        }
    }


            @Override
    public List<Denuncia> readAll(String orderBy, String direction, int limit, int offset) {
        return null;
    }

    @Override
    public Optional<Denuncia> read(int id) {
        return Optional.empty();
    }

    @Override
    public void update(int id, Denuncia obj) {

    }

    @Override
    public void delete(int id) {

    }
}
