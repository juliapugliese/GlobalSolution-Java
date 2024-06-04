package org.example.repositories;


import org.example.entities.ServicoModel.Feedback;

import java.util.List;
import java.util.Optional;

public class FeedbacksRepository extends Starter implements _BaseRepository<Feedback>, _Logger<String>{

    public static final String TB_NAME = "FEEDBACK_JAVA";


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
