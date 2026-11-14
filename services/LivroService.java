package br.edu.atitus.bibliodigital.services;

import br.edu.atitus.bibliodigital.entities.Livro;
import br.edu.atitus.bibliodigital.repositories.LivroRepository;

import java.util.List;

public class LivroService {

    private final LivroRepository repository = new LivroRepository();

    public void inserir(Livro livro) {
        validarLivro(livro, -1);
        repository.inserir(livro);
    }

    public List<Livro> findAll() {
        return repository.findAll();
    }

    public List<Livro> findByTitulo(String parametro) {
        validarParametroPesquisa(parametro);
        return repository.findByTitulo(parametro.trim());
    }

    public List<Livro> findByAutor(String parametro) {
        validarParametroPesquisa(parametro);
        return repository.findByAutor(parametro.trim());
    }

    public void alterar(int indice, Livro livro) {
        validarIndice(indice);
        validarLivro(livro, indice);
        repository.alterar(indice, livro);
    }

    public void excluir(int indice) {
        validarIndice(indice);
        repository.excluir(indice);
    }

    private void validarLivro(Livro livro, int indiceIgnorado) {
        if (livro == null) {
            throw new RegraNegocioException("Livro inválido.");
        }

        if (livro.getTitulo() == null || livro.getTitulo().isBlank()) {
            throw new RegraNegocioException("O título não pode ficar vazio.");
        }

        if (livro.getAutor() == null || livro.getAutor().isBlank()) {
            throw new RegraNegocioException("O autor não pode ficar vazio.");
        }

        if (livro.getAnoPublicacao() < 1900) {
            throw new RegraNegocioException(
                    "O ano de publicação deve ser igual ou superior a 1900."
            );
        }

        livro.setTitulo(livro.getTitulo().trim());
        livro.setAutor(livro.getAutor().trim());

        if (repository.existeTitulo(livro.getTitulo(), indiceIgnorado)) {
            throw new RegraNegocioException(
                    "Já existe um livro cadastrado com este título."
            );
        }
    }

    private void validarParametroPesquisa(String parametro) {
        if (parametro == null || parametro.isBlank()) {
            throw new RegraNegocioException(
                    "Informe um título ou autor para realizar a pesquisa."
            );
        }
    }

    private void validarIndice(int indice) {
        if (indice < 0 || indice >= repository.findAll().size()) {
            throw new RegraNegocioException("Índice de livro inválido.");
        }
    }
}