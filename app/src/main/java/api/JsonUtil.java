package api;

import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import exceptions.RequestException;

/**
 * JsonUtil
 * Classe utilitária para conversão entre objetos Java e JSON usando Jackson.
 * Fornece métodos para serializar objetos em JSON e desserializar JSON em
 * objetos.
 */
public class JsonUtil {
    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * Converte um objeto Java em uma string JSON.
     * 
     * @param object o objeto a ser convertido
     * @return a string JSON resultante, ou null em caso de erro
     */
    public static String parseObjectToJson(Object object) {
        try {
            return mapper.writeValueAsString(object);
        } catch (Exception e) {
            System.out.println("Erro ao converter o objeto para JSON: " + e.getMessage());
            return null;
        }
    }

    /**
     * Converte uma string JSON para um objeto do tipo especificado.
     * 
     * @param json      a string JSON a ser convertida
     * @param valueType o tipo do objeto de destino
     * @return o objeto convertido, ou null em caso de erro
     */
    public static <T> T parseJson(String json, Class<T> valueType) {
        try {
            return mapper.readValue(json, valueType);
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
     * Converte uma string JSON para um objeto do tipo especificado usando
     * TypeReference.
     * TypeReference é útil quando se trabalha com tipos genéricos ou coleções, como
     * Map<String, Object> ou List<MyClass>.
     * 
     * @param json               a string JSON a ser convertida
     * @param valueTypeReference o TypeReference do objeto de destino
     * @return o objeto convertido, ou null em caso de erro
     */
    public static Map<String, Object> parseJson(String json){
        try {
            return mapper.readValue(json, new TypeReference<Map<String, Object>>() {});
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
     * Lê uma propriedade específica de um objeto JSON.
     * 
     * @param json         a string JSON
     * @param propertyName o nome da propriedade a ser lida
     * @return o valor da propriedade, ou null em caso de erro
     */
    public static Object readProperty(String json, String propertyName) {
        try {
            return mapper.readTree(json).get(propertyName);
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
     * Método principal para testar a conversão de objeto para JSON e vice-versa.
     * 
     * @param args argumentos da linha de comando (não utilizados)
     * @throws RequestException
     */
    public static void main(String[] args) throws RequestException {
        String jsonRequest = parseObjectToJson(new Request());
        System.out.println(jsonRequest);
        Request parsedRequest = parseJson(jsonRequest, Request.class);
        System.out.println(parsedRequest.toString());
    }
}
