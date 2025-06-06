package api;

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
     * Converte um objeto para uma string JSON.
     * Atenção: o método só funciona se o objeto tiver getters ou métodos públicos,
     * senão aparece o erro No serializer found for class.
     * 
     * @param object o objeto a ser convertido
     * @return a string JSON representando o objeto, ou null em caso de erro
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
     * 
     * @param json               a string JSON a ser convertida
     * @param valueTypeReference a referência de tipo para o objeto de destino
     * @return o objeto convertido, ou null em caso de erro
     */
    public static <T> T parseJson(String json, TypeReference<T> valueTypeReference) {
        try {
            return mapper.readValue(json, valueTypeReference);
        } catch (JsonMappingException e) {
            System.out.println("Erro de mapeamento JSON: " + e.getMessage());
        } catch (JsonProcessingException e) {
            System.out.println("Erro ao processar JSON: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Erro desconhecido: " + e.getMessage());
        }
        return null;
    }

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
