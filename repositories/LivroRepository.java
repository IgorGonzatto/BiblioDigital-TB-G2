package br.edu.atitus.bibliodigital.repositories;

import br.edu.atitus.bibliodigital.entities.Livro;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class LivroRepository {

    private final List<Livro> acervo = new ArrayList<>();

    public void inserir(Livro livro) {
        acervo.add(livro);
    }

    public List<Livro> findAll() {
        return List.copyOf(acervo);
    }

    public List<Livro> findByTitulo(String parametro) {
        List<Livro> resultado = new ArrayList<>();
        String busca = parametro.toLowerCase(Locale.ROOT);

        for (Livro livro : acervo) {
            if (livro.getTitulo().toLowerCase(Locale.ROOT).contains(busca)) {
                resultado.add(livro);
            }
        }

        return resultado;
    }

    public List<Livro> findByAutor(String parametro) {
        List<Livro> resultado = new ArrayList<>();
        String busca = parametro.toLowerCase(Locale.ROOT);

        for (Livro livro : acervo) {
            if (livro.getAutor().toLowerCase(Locale.ROOT).contains(busca)) {
                resultado.add(livro);
            }
        }

        return resultado;
    }

    public boolean existeTitulo(String titulo, int indiceIgnorado) {
        for (int i = 0; i < acervo.size(); i++) {
            if (i != indiceIgnorado
                    && acervo.get(i).getTitulo().equalsIgnoreCase(titulo)) {
                return true;
            }
        }

        return false;
    }

    public Livro findByIndex(int indice) {
        return acervo.get(indice);
    }

    public void alterar(int indice, Livro livro) {
        acervo.set(indice, livro);
    }

    public void excluir(int indice) {
        acervo.remove(indice);
    }
}
