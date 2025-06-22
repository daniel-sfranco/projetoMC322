package api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

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
     * Obs: não funciona para tipos compostos, como Map ou ArrayList.
     * Para array use primeiro parseJsonArray e depois itere sobre os
     * elementos do ArrayList<Json> recebido
     * 
     * @param valueType a classe do objeto a ser lido
     * @param <T>       a classe do objeto a ser retornado
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
     * Converte o valor armazenado para um Map de String e Json.
     * 
     * @return o Map resultante, ou null em caso de erro
     */
    public Map<String, Json> parseJson() {
        try {
            Map<String, Object> map = mapper.readValue(this.value, new TypeReference<Map<String, Object>>() {
            });
            Map<String, Json> jsonMap = new java.util.HashMap<>();
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                jsonMap.put(entry.getKey(), new Json(entry.getValue()));
            }
            return jsonMap;
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
     * Converte o valor armazenado para um ArrayList de objetos Json.
     * 
     * @return o ArrayList resultante, ou null em caso de erro
     */
    public ArrayList<Json> parseJsonArray() {
        try {
            ArrayList<Object> list = mapper.readValue(this.value, new TypeReference<ArrayList<Object>>() {
            });
            ArrayList<Json> jsonList = new ArrayList<>();
            for (Object item : list) {
                jsonList.add(new Json(item));
            }
            return jsonList;
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
     * @param propertyName o nome da propriedade a ser lida
     * @return o valor da propriedade, ou null em caso de erro
     */
    public Json get(String propertyName) {
        try {
            ArrayList<String> propertyPath = new ArrayList<>(Arrays.asList(propertyName.split("\\.")));
            JsonNode currentNode = mapper.readTree(this.value);
            for (String property : propertyPath) {
                if (currentNode.has(property)) {
                    currentNode = currentNode.get(property);
                } else {
                    return null; // Propriedade não encontrada
                }
            }
            return new Json(currentNode.toString());
        } catch (JsonMappingException e) {
            System.err.println("Erro de mapeamento JSON: " + e.getMessage());
        } catch (JsonProcessingException e) {
            System.err.println("Erro ao processar JSON: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Erro desconhecido: " + e.getMessage());
        }
        return null;
    }

    /**
     * Imprime o json de forma legível diretamente no terminal
     */
    public void print() {
        JsonNode node;
        String returnString = value;
        try {
            node = mapper.readTree(value);
            returnString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(node);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        System.out.println(returnString);
    }

    /**
     * Retorna a representação em string do objeto JSON.
     *
     * @return o valor armazenado
     */
    public String toString() {
        return value;
    }
}
