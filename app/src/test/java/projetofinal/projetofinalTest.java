/*
 * projetofinalTest.java
 * 
 * Material usado na disciplina MC322 - Programação orientada a objetos.
 * 
 * A documentação para javadoc deste arquivo foi feita com o uso de IA
 * e posteriormente revisada e/ou corrigida.
 */
package projetofinal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Map;

import org.junit.jupiter.api.Test;

import fileManager.ClientDataFileManager;
import fileManager.RefreshTokenFileManager;
import search.AlbumResearcher;
import search.ArtistResearcher;
import search.GenreResearcher;
import search.SearchManager;
import search.SearchResult;
import search.TrackResearcher;
import user.User;

/**
 * <p>Classe de testes unitários para verificar o correto funcionamento das funcionalidades
 * principais do projeto de integração com a API do Spotify.</p>
 * <p>Esta classe abrange testes para leitura de credenciais, token de atualização,
 * e a funcionalidade de busca por diferentes tipos de conteúdo musical (artista, faixa, álbum, gênero).</p>
 * 
 * @author Vinícius de Oliveira - 251527
 */
public class projetofinalTest {

    /**
     * Testa a leitura das credenciais (Client ID e Client Secret) a partir do arquivo de dados do cliente.
     * Verifica se ambos os valores {@code clientId} e {@code clientSecret} não são nulos,
     * indicando que as credenciais foram lidas com sucesso.
     */
    @Test
    public void VerifyReadCredentials() {
        Map<String, String> credentials = ClientDataFileManager.readClientData();
        String clientId = credentials.get("clientId");
        String clientSecret = credentials.get("clientSecret");
        
        assertTrue(clientId != null);
        assertTrue(clientSecret != null);
    }

    /**
     * Testa a leitura do token de atualização (refresh token) a partir do arquivo.
     * Primeiramente, invoca {@code User.getInstance().getRequest()} para garantir que
     * o arquivo do token de atualização seja criado ou atualizado.
     * Em seguida, verifica se o token lido não é nulo.
     * <p>Nota: Como o token de atualização é específico para cada usuário e dinâmico,
     * o teste se limita a verificar sua não-nulidade.</p>
     */
    @Test
    public void VerifyReadRefreshToken() {
        // Para garantir que o arquivo seja criado corretamente
        User.getInstance().getRequest();

        String requestToken = RefreshTokenFileManager.readRefreshToken();
        
        /* Como requestToken é diferente para cada usuário, é possível apenas
        verificar se ele não é nulo */
        assertTrue(requestToken != null);
    }

    /**
     * Testa a funcionalidade de busca de artistas.
     * Realiza uma busca por "Elvis Pre" e verifica se o nome do primeiro resultado
     * corresponde exatamente a "Elvis Presley".
     */
    @Test
    public void VerifyArtistSearch() {
        ArrayList<SearchResult> artistResults = SearchManager.search("Elvis Pre", new ArtistResearcher());

        String firstArtistName = artistResults.get(0).getName();

        assertEquals("Elvis Presley", firstArtistName);
    }

    /**
     * Testa a funcionalidade de busca de faixas (músicas).
     * Realiza uma busca por "Lá(r)" e verifica se o nome da primeira faixa
     * corresponde exatamente a "Lá(R)".
     */
    @Test
    public void VerifyTrackSearch() {
        ArrayList<SearchResult> trackResults = SearchManager.search("Lá(r)", new TrackResearcher());

        String firstTrackName = trackResults.get(0).getName();

        assertEquals("Lá(R)", firstTrackName);
    }
    
    /**
     * Testa a funcionalidade de busca de álbuns.
     * Realiza uma busca por "guerra e paz" e verifica se o nome do primeiro álbum
     * corresponde exatamente a "Guerra e Paz (Ao Vivo)".
     */
    @Test
    public void VerifyAlbumSearch() {
        ArrayList<SearchResult> albumResults = SearchManager.search("guerra e paz", new AlbumResearcher());

        String firsAlbumName = albumResults.get(0).getName();

        assertEquals("Guerra e Paz (Ao Vivo)", firsAlbumName);
    }

    /**
     * Testa a funcionalidade de busca de gêneros.
     * Realiza uma busca por "pop" e verifica se o nome do primeiro gênero
     * corresponde exatamente a "Pop Acústico".
     */
    @Test
    public void VerifyGenreSearch() {
        ArrayList<SearchResult> genreResults = SearchManager.search("pop", new GenreResearcher());

        String firstGenreName = genreResults.get(0).getName();

        assertEquals("Pop Acústico", firstGenreName);
    }
}
