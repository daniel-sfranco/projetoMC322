/*
* MusicSource.java
* 
* Material usado na disciplina MC322 - Programação orientada a objetos.
* 
* A documentação para javadoc deste arquivo foi feita com o uso de IA
* e posteriormente revisada e/ou corrigida.
*/

package music;

import java.util.ArrayList;

/**
 * <p>A interface {@code MusicSource} define um contrato para qualquer entidade que possa ser considerada uma fonte de música no sistema.</p>
 * <p>Isso inclui classes como {@code Artist}, {@code Album}, {@code Playlist} e {@code Category} garantindo que todas as fontes de música
 * possuam um ID único e um nome para identificação.</p>
 * 
 * @author Vinícius de Oliveira - 251527
 */
public interface MusicSource {
    /**
     * Retorna uma lista de faixas ({@link Track}) associadas a esta fonte de música.
     * Dependendo da implementação, pode ser as faixas de uma playlist, de um álbum, ou as faixas mais populares de um artista/categoria.
     *
     * @return Uma {@code ArrayList} de objetos {@link Track} contidos na fonte de música. Pode retornar uma lista vazia se não houver faixas.
     */
    public ArrayList<Track> getTracks();

    /**
     * Retorna o ID único da fonte de música.
     * Este ID é utilizado para identificar a fonte de forma exclusiva dentro do sistema ou da API integrada (e.g., Spotify).
     *
     * @return Uma {@code String} contendo o ID da fonte de música.
     */
    public String getId();

     /**
     * Retorna o nome legível da fonte de música.
     * Este nome é tipicamente exibido ao usuário e descreve a fonte de música (e.g., o nome de uma playlist).
     *
     * @return Uma {@code String} contendo o nome da fonte de música.
     */
    public String getName(); 
}
