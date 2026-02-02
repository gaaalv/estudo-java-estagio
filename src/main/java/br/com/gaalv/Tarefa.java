package br.com.gaalv;

import lombok.*;

import javax.swing.text.DateFormatter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Tarefa {

    private Long id;
    private String titulo;
    private String descricao;

    @Builder.Default
    private boolean status = false;

    @Builder.Default
    private LocalDate data = LocalDate.now();

    private static final DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Override
    public String toString() {

        return String.format(
                "Data: %s | Tarefa: %s | Descrição: %s | Status: %s",
                this.data.format(format),
                this.titulo,
                this.descricao,
                this.status ? "Concluido" : "Pendente"
        );
    }
}

