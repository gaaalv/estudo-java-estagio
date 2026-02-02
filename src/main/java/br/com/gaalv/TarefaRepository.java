package br.com.gaalv;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class TarefaRepository {

    private final List<Tarefa> tarefas = new ArrayList<>();
    private long contId = 1;


    public void saveTarefa(Tarefa tarefa) {

        if (tarefa.getId() == null) {

            tarefa.setId(contId++);
            tarefas.add(tarefa);
        } else {

            searchById(tarefa.getId()).ifPresent(tarefaAntiga -> {

                int index = tarefas.indexOf(tarefaAntiga);

                tarefas.set(index, tarefa);
            });
        }
    }

    public List<Tarefa> findAll() {return Collections.unmodifiableList(tarefas);}

    public boolean deleteById(long id) {

        return tarefas.removeIf(t -> t.getId() == id);
    }

    public Optional<Tarefa> searchById(long id) {

        return tarefas.stream()
                .filter(t -> t.getId() == id)
                .findFirst();
    }

    public int total() {

        return tarefas.size();
    }

    public void markTarefa() {

        tarefas.forEach(t -> t.setStatus(true));
    }
}
