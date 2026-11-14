package br.edu.atitus.bibliodigital.views;

import br.edu.atitus.bibliodigital.entities.AudioLivro;
import br.edu.atitus.bibliodigital.entities.Livro;
import br.edu.atitus.bibliodigital.entities.LivroDigital;
import br.edu.atitus.bibliodigital.entities.LivroFisico;
import br.edu.atitus.bibliodigital.services.LivroService;
import br.edu.atitus.bibliodigital.services.RegraNegocioException;

import java.util.List;

public class BiblioDigitalApplication {

    private final LivroService service = new LivroService();

    public static void main(String[] args) {
        new BiblioDigitalApplication().executar();
    }

    private void executar() {
        int opcao;

        do {
            mostrarMenu();

            try {
                opcao = lerInteiro("Escolha uma das opções acima: ");

                switch (opcao) {
                    case 1 -> cadastrarLivro();
                    case 2 -> pesquisarPorTitulo();
                    case 3 -> pesquisarPorAutor();
                    case 4 -> alterarLivro();
                    case 5 -> excluirLivro();
                    case 0 -> IO.println("Até logo!");
                    default -> IO.println("Opção inexistente. Tente novamente.");
                }
            } catch (RegraNegocioException | NumberFormatException ex) {
                IO.println("Erro: " + ex.getMessage());
                opcao = -1;
            }

            IO.println("");
        } while (opcao != 0);
    }

    private void mostrarMenu() {
        IO.println("""
                ===== Bem-vindo ao BiblioDigital =====

                Menu:

                1 - Cadastrar novo Livro
                2 - Pesquisar Livro por título
                3 - Pesquisar Livro por autor
                4 - Alterar dados do Livro
                5 - Excluir Livro
                0 - Sair
                """);
    }

    private void cadastrarLivro() {
        int tipo = escolherTipoLivro();
        Livro livro = criarLivro(tipo);

        service.inserir(livro);
        IO.println("Livro cadastrado com sucesso.");
    }

    private void pesquisarPorTitulo() {
        String titulo = IO.readln("Informe o título ou parte dele: ");
        exibirResultado(service.findByTitulo(titulo));
    }

    private void pesquisarPorAutor() {
        String autor = IO.readln("Informe o autor ou parte dele: ");
        exibirResultado(service.findByAutor(autor));
    }

    private void alterarLivro() {
        List<Livro> livros = service.findAll();

        if (livros.isEmpty()) {
            IO.println("Não existem livros cadastrados para alterar.");
            return;
        }

        listarLivrosComIndice(livros);
        int indice = lerIndice(livros.size());

        Livro livroAtual = livros.get(indice);
        IO.println("Dados atuais: " + livroAtual);

        Livro livroAlterado = criarLivro(obterTipo(livroAtual));
        service.alterar(indice, livroAlterado);

        IO.println("Livro alterado com sucesso.");
    }

    private void excluirLivro() {
        List<Livro> livros = service.findAll();

        if (livros.isEmpty()) {
            IO.println("Não existem livros cadastrados para excluir.");
            return;
        }

        listarLivrosComIndice(livros);
        int indice = lerIndice(livros.size());

        String confirmacao = IO.readln(
                "Deseja excluir \"" + livros.get(indice).getTitulo() + "\"? (S/N): "
        );

        if (confirmacao.trim().equalsIgnoreCase("S")
                || confirmacao.trim().equalsIgnoreCase("SIM")) {
            service.excluir(indice);
            IO.println("Livro excluído com sucesso.");
        } else {
            IO.println("Exclusão cancelada.");
        }
    }

    private int escolherTipoLivro() {
        IO.println("""
                Qual tipo de livro deseja cadastrar?

                1 - Livro físico
                2 - Livro digital
                3 - Audiolivro
                """);

        int tipo = lerInteiro("Escolha o tipo: ");

        if (tipo < 1 || tipo > 3) {
            throw new RegraNegocioException("Tipo de livro inválido.");
        }

        return tipo;
    }

    private Livro criarLivro(int tipo) {
        String titulo = IO.readln("Título: ");
        String autor = IO.readln("Autor: ");
        int anoPublicacao = lerInteiro("Ano de publicação: ");

        return switch (tipo) {
            case 1 -> {
                int quantidade = lerInteiro("Quantidade de exemplares: ");
                String localizacao = IO.readln("Localização: ");
                yield new LivroFisico(
                        titulo, autor, anoPublicacao, quantidade, localizacao
                );
            }
            case 2 -> {
                String formato = IO.readln("Formato do arquivo: ");
                double tamanho = lerDouble("Tamanho do arquivo em MB: ");
                yield new LivroDigital(
                        titulo, autor, anoPublicacao, formato, tamanho
                );
            }
            case 3 -> {
                int duracao = lerInteiro("Duração em minutos: ");
                String narrador = IO.readln("Narrador: ");
                yield new AudioLivro(
                        titulo, autor, anoPublicacao, duracao, narrador
                );
            }
            default -> throw new RegraNegocioException("Tipo de livro inválido.");
        };
    }

    private void exibirResultado(List<Livro> livros) {
        if (livros.isEmpty()) {
            IO.println("Nenhum livro encontrado para a pesquisa realizada.");
            return;
        }

        IO.println("Livros encontrados:");
        for (Livro livro : livros) {
            IO.println(livro);
        }
    }

    private void listarLivrosComIndice(List<Livro> livros) {
        IO.println("===== Livros cadastrados =====");

        for (int i = 0; i < livros.size(); i++) {
            IO.println((i + 1) + " - " + livros.get(i).getTitulo());
        }
    }

    private int lerIndice(int quantidadeLivros) {
        int indiceInformado = lerInteiro(
                "Informe o índice do livro desejado: "
        );

        if (indiceInformado < 1 || indiceInformado > quantidadeLivros) {
            throw new RegraNegocioException("Índice de livro inválido.");
        }

        return indiceInformado - 1;
    }

    private int obterTipo(Livro livro) {
        if (livro instanceof LivroFisico) {
            return 1;
        }

        if (livro instanceof LivroDigital) {
            return 2;
        }

        if (livro instanceof AudioLivro) {
            return 3;
        }

        throw new RegraNegocioException("Tipo de livro desconhecido.");
    }

    private int lerInteiro(String mensagem) {
        return Integer.parseInt(IO.readln(mensagem).trim());
    }

    private double lerDouble(String mensagem) {
        String valor = IO.readln(mensagem).trim().replace(",", ".");
        return Double.parseDouble(valor);
    }
}