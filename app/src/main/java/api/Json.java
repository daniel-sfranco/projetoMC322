package api;

import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import exceptions.RequestException;

/**
 * Json
 * Classe para objetos do tipo Json, para conversão entre tipos
 * Fornece métodos para serializar objetos em JSON e desserializar JSON em
 * objetos.
 *
 * @author Daniel Soares Franco - 259083
 */
public class Json {
    private static final ObjectMapper mapper = new ObjectMapper();
    private final String value;

    /**
     * Construtor que recebe uma string JSON.
     * 
     * @param value a string JSON a ser armazenada
     */
    public Json(String value) {
        this.value = value;
    }

    /**
     * Construtor que recebe um objeto e o converte para uma string JSON.
     * 
     * @param object o objeto a ser convertido em JSON
     * @throws JsonProcessingException se ocorrer um erro ao processar o objeto
     */
    public Json(Object object) throws JsonProcessingException {
        this.value = mapper.writeValueAsString(object);
    }

    /**
     * Converte o valor armazenado para um objeto do tipo especificado.
     * 
     * @param json      a string JSON a ser convertida
     * @param valueType o tipo do objeto de destino
     * @return o objeto convertido, ou null em caso de erro
     */
    public <T> T parseJson(Class<T> valueType) {
        try {
            return mapper.readValue(this.value, valueType);
        } catch (JsonMappingException e) {
            System.out.println("Erro de mapeamento JSON: " + e.getMessage());
        } catch (JsonProcessingException e) {
            System.out.println("Erro ao processar JSON: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Erro desconhecido: " + e.getMessage());
        }
        return null;
    }

    /**
     * Converte o valor armazenado para um objeto do tipo especificado usando
     * TypeReference.
     * TypeReference é útil quando se trabalha com tipos genéricos ou coleções, como
     * Map<String, Object> ou List<MyClass>.
     * 
     * @param json               a string JSON a ser convertida
     * @param valueTypeReference o TypeReference do objeto de destino
     * @return o objeto convertido, ou null em caso de erro
     */
    public <T> T parseJson(TypeReference<T> valueTypeReference) {
        try {
            return mapper.readValue(this.value, valueTypeReference);
        } catch (JsonMappingException e) {
            System.out.println("Erro de mapeamento JSON: " + e.getMessage());
        } catch (JsonProcessingException e) {
            System.out.println("Erro ao processar JSON: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Erro desconhecido: " + e.getMessage());
        }
        return null;
    }

    /**
     * Converte o valor armazenado para um Map<String, Object>.
     * 
     * @param json a string JSON a ser convertida
     * @return o Map resultante, ou null em caso de erro
     */
    public Map<String, Object> parseJson() {
        try {
            return mapper.readValue(this.value, new TypeReference<Map<String, Object>>() {
            });
        } catch (JsonMappingException e) {
            System.out.println("Erro de mapeamento JSON: " + e.getMessage());
        } catch (JsonProcessingException e) {
            System.out.println("Erro ao processar JSON: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Erro desconhecido: " + e.getMessage());
        }
        return null;
    }

    /**
     * Lê uma propriedade específica do valor armazenado
     * 
     * @param json         a string JSON
     * @param propertyName o nome da propriedade a ser lida
     * @return o valor da propriedade, ou null em caso de erro
     */
    public Object readProperty(String propertyName) {
        try {
            return mapper.readTree(this.value).get(propertyName);
        } catch (JsonMappingException e) {
            System.out.println("Erro de mapeamento JSON: " + e.getMessage());
        } catch (JsonProcessingException e) {
            System.out.println("Erro ao processar JSON: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Erro desconhecido: " + e.getMessage());
        }
        return null;
    }

    /**
     * Retorna a representação em string do objeto JSON.
     * 
     * @return a string JSON
     */
    public String toString() {
        return value;
    }

    /**
     * Método principal para testar a conversão de objeto para JSON e vice-versa.
     * 
     * @param args argumentos da linha de comando (não utilizados)
     * @throws RequestException
     */
    public static void main(String[] args) throws RequestException, JsonProcessingException {
        Map<String, String> info = Map.of(
                "name", "John Doe",
                "email", "123@gmail.com");
        Json json;
        try {
            json = new Json(info);
            System.out.println(json);
            Map<String, String> parsedMap = json.parseJson(new TypeReference<Map<String, String>>() {
            });
            System.out.println(parsedMap.toString());
        } catch (JsonProcessingException e) {
            System.out.println(e.getMessage());
        }
    }
}
