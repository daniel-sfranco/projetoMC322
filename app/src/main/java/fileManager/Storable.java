/*
 * Storable.java
 * 
 * Material usado na disciplina MC322 - Programação orientada a objetos.
 * 
 * A documentação para javadoc deste arquivo foi feita com o uso de IA
 * e posteriormente revisada e/ou corrigida.
 */

package fileManager;

/**
 * Interface que marca classes cujas instâncias podem ser salvas em um arquivo.
 * Classes que implementam esta interface indicam que possuem um mecanismo
 * para persistir seus dados, geralmente através do método {@code saveData()}.
 * Classes que implementam esta interface também podem possuir mecanismos para
 * ler seus arquivos.
 * 
 * @author Vinícius de Oliveira - 251527
 */
public interface Storable {
    /**
     * Salva os dados da instância da classe que implementa esta interface
     * em um arquivo ou outro meio de armazenamento persistente.
     * A implementação específica deste método dependerá da lógica de salvamento
     * de cada classe.
     */
    public void saveData();
}
