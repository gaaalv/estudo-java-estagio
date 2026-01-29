package br.com.gaalv;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Gerenciador {

    private final Scanner sc = new Scanner(System.in);
    private final List<Tarefa> tarefas = new ArrayList<>();
    private static final String linha = "-------------------------";

    public Gerenciador () {

        tarefas.add(Tarefa.builder()
                    .titulo("teste")
                    .descricao("testandoo")
                .build());
    }

    @AllArgsConstructor
    @Getter
    public enum MENU {

        ADD(1, "Adicionar Tarefa"), LIST(2, "Listar Tarefas"),
        MARK(3, "Marcar todas como concluídas"),EXIT(4, "Sair");

        private final int id;
        private final String descricao;

        public static MENU buscaId(int idInformado) {

            return Arrays.stream(MENU.values())
                    .filter(m -> m.getId() == idInformado)
                    .findFirst()
                    .orElse(null);
            }

        public static void mostrarMenu() {

            System.out.println(linha);
            System.out.println("O que deseja fazer?\n");

            Arrays.stream(values()).forEach(System.out::println);
        }

        @Override
        public String toString() {

            return String.format(
                    "(%d)   -   %s",
                    this.id,
                    this.descricao
            );
        }
    }

    @AllArgsConstructor
    @Getter
    public enum SUBMENU {

        EDIT(1, "Editar Tarefa"), REMOVE(2, "Remover Tarefa"),
        DELETE_ALL(3, "Excluir todas as Tarefas"),
        CHECK(4, "Trocar Status Tarefa"), EXIT(5, "Sair");

        private final int id;
        private final String descricao;

        public static SUBMENU buscaId(int idInformado) {

            return Arrays.stream(SUBMENU.values())
                    .filter(sm -> sm.getId() == idInformado)
                    .findFirst()
                    .orElse(null);

        }

        public static void mostrarMenu() {

            System.out.println(linha);
            System.out.println("O que deseja fazer?\n");

            Arrays.stream(values()).forEach(System.out::println);
        }

        @Override
        public String toString() {

            return String.format(
                    "(%d)   -   %s",
                    this.id,
                    this.descricao
            );
        }
    }

    public int lerInt () {

        while (true) {

            try {

                String resp = sc.nextLine();

                return Integer.parseInt(resp);
            } catch (NumberFormatException e) {

                System.out.println("❌ Valor informado não é válido.");
                System.out.println("Digite um número válido:");
            }
        }
    }

    public void start () {

        MENU tarefaAtual;

        do {

            MENU.mostrarMenu();

            int input = lerInt();
            tarefaAtual = MENU.buscaId(input);
            System.out.println("\n");

            if (tarefaAtual == null) {

                System.out.println("❌ Código Inválido!\nTente Novamente.\n");
                continue;
            }

            switch(tarefaAtual) {

                case ADD -> addTarefa();

                case LIST -> listTarefa();

                case MARK -> markTarefa();

                case EXIT -> {

                    System.out.println("💾 Salvando Tarefas");
                    System.out.println("Finalizando Aplicação...");
                }

            }
        } while (tarefaAtual != MENU.EXIT);
    }

    public void addTarefa() {

        String inputTitulo = "";

        do {

            System.out.println("Digite o titulo da nova tarefa (Required)");
            inputTitulo = sc.nextLine().trim();
            if(inputTitulo.isBlank()) System.out.println("Digite um titulo válido!");
        } while (inputTitulo.isBlank());

        System.out.println("Digite a descrição da nova tarefa (Not Required)");
        String inputDescricao = sc.nextLine().trim();
        if(inputDescricao.isBlank()) inputDescricao = "";

        tarefas.add(Tarefa.builder()
                        .titulo(inputTitulo)
                        .descricao(inputDescricao)
                .build());
        System.out.println("✅ Tarefa salva com sucesso!\n");
    }

    public void listTarefa() {

        SUBMENU tarefaAtual;
        boolean noSub = true;

        do {

            mostrarTarefa();
            SUBMENU.mostrarMenu();

            int input = lerInt();
            tarefaAtual = SUBMENU.buscaId(input);
            System.out.println("\n");

            if (tarefaAtual == null) {

                System.out.println("❌ Código Inválido!\nTente Novamente.\n");
                continue;
            }

            switch (tarefaAtual) {

                case EDIT -> editTarefa();

                case REMOVE -> removeTarefa();

                case DELETE_ALL -> {

                    deleteAllTarefa();
                    noSub = false;
                }

                case CHECK -> checkTarefa();

                case EXIT -> {

                    System.out.println("Voltando ao Menu Principal...");
                    noSub = false;
                }
            }
        } while (noSub);
    }

    public void markTarefa() {

        if(validarCtz()) {

            tarefas.forEach(t -> t.setStatus(true));

            System.out.println("✅ Todas as tarefas foram concluídas.\n");
        } else System.out.println("❌ Operação Cancelada.");
    }

    public boolean validarCtz() {

        System.out.println("Tem certeza que deseja prosseguir? (s/n)");
        String input = sc.nextLine().trim().toLowerCase();

        return List.of("sim", "s", "y", "yes").contains(input);
    }

    public void mostrarTarefa() {

        System.out.printf("Foram encontrados %d tarefas.\n", tarefas.size());

        int cont = 1;
        for (Tarefa t : tarefas) {

            System.out.printf("(%d) -   %s\n", cont++, t);
        }
    }

    public void editTarefa() {}

    public void removeTarefa() {}

    public void deleteAllTarefa() {

        if(validarCtz()) {

            int excluidos = tarefas.size();
            tarefas.clear();

            System.out.printf("✅ Foram excluídos %s tarefas.\n", excluidos);
        } else System.out.println("❌ Operação Cancelada.");
    }

    public void checkTarefa() {}

}
