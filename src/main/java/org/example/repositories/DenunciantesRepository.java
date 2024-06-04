package org.example.repositories;

import org.example.entities.UsuarioModel.Denunciante;

import java.util.List;
import java.util.Optional;

public class DenunciantesRepository  extends Starter implements _BaseRepository<Denunciante>, _Logger<String>{

    public static final String TB_NAME = "DENUNCIANTE_JAVA";
    @Override
    public void create(Denunciante obj) {

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
