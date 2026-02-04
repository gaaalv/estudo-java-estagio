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
            CHECK(4, "Trocar Status Tarefa"), BACK(5, "Voltar");

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
                        sc.close();
                    }

                }
            } while (tarefaAtual != MENU.EXIT);
        }

        public void addTarefa() {

            String inputTitulo = "";

            do {

                System.out.println("Digite o titulo da nova tarefa (Required)");
                inputTitulo = sc.nextLine().trim();
                if(inputTitulo.isEmpty()) System.out.println("Digite um titulo válido!");
            } while (inputTitulo.isEmpty());

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

            boolean noSub = true;

            SUBMENU tarefaAtual;

            while (noSub) {

                if(tarefas.isEmpty()) {

                    System.out.println("Nenhuma tarefa restante.");
                    noSub = false;
                    continue;

                }

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

                    case BACK -> {

                        System.out.println("Voltando ao Menu Principal...");
                        noSub = false;
                    }
                }
            }
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

            System.out.printf("Foram encontrado(s) %d tarefa(s).\n", tarefas.size());

            int cont = 1;
            for (Tarefa t : tarefas) {

                System.out.printf("(%d) -   %s\n", cont++, t);
            }
        }

        public void editTarefa() {

            Tarefa tarefaEditada = tarefas.get(escolhaId());

            while (true) {

                System.out.printf("Digite o titulo atualizado: (atual: %s) (Aperte ENTER caso queira manter o titulo antigo) (required)\n", tarefaEditada.getTitulo());
                String inputTitulo = sc.nextLine();

                if (inputTitulo.isEmpty()) {

                    System.out.println("Mantendo o titulo original...");
                    break;
                }

                String inputTituloTratado = inputTitulo.trim();
                if (!inputTituloTratado.isBlank()) {

                    tarefaEditada.setTitulo(inputTituloTratado);
                    break;
                } else System.out.println("Não pode titulo vazio.");
            }

            System.out.println("Agora, digite a descrição atualizada: (Aperte ENTER caso queira manter a descrição antiga)");
            String inputDescricao = sc.nextLine();

            if (inputDescricao.isEmpty()) {

                System.out.println("Mantendo a descrição original...");
            } else {

                tarefaEditada.setDescricao(inputDescricao.trim());
                System.out.println("Descrição Atualizada");
            }

            System.out.printf("Tarefa atualizada -> Titulo:    %s | Descrição:  %s\n", tarefaEditada.getTitulo(), tarefaEditada.getDescricao());
        }

        public void removeTarefa() {

            boolean loop = true;

            while (loop) {

                tarefas.remove(escolhaId());

                System.out.println("🗑️Tarefa removida com sucesso.\n");


                if (!tarefas.isEmpty()) {

                    mostrarTarefa();
                    System.out.println("Deseja excluir outra tarefa? (s/n)");
                    String input = sc.nextLine().trim().toLowerCase();

                    loop = List.of("s", "sim", "y", "yes").contains(input);
                } else {

                    loop = false;
                }

                if (!loop) System.out.println("Voltando ao menu...");
            }
        }

        public void deleteAllTarefa() {

            if(validarCtz()) {

                int excluidos = tarefas.size();
                tarefas.clear();

                System.out.printf("✅ Foram excluída(s) %s tarefa(s).\n", excluidos);
            } else System.out.println("❌ Operação Cancelada.");
        }

        public void checkTarefa() {

            Tarefa tarefaEditada = tarefas.get(escolhaId());

            tarefaEditada.setStatus(!tarefaEditada.isStatus());

            System.out.println("Status atualizado.");
        }

        public int escolhaId () {

            int input;
            do {

                System.out.println("Digite o n. da tarefa:");
                input = lerInt();

                if (input <= 0 || input > tarefas.size()) System.out.println("Número inválido! Tente algo entre 1 e " + tarefas.size());

            } while (input <= 0 || input > tarefas.size());

            return input - 1;
        }
    }
