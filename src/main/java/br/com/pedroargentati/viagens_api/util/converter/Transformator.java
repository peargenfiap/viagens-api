package br.com.pedroargentati.viagens_api.util.converter;

import br.com.pedroargentati.viagens_api.exceptions.TransformatorException;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class Transformator {

    private Transformator() { }

    /**
     * Método responsável por transformar um objeto em outro, suportando classes que são `records`.
     *
     * @param input - Objeto de entrada.
     * @return O - Objeto de saída.
     */
    public static <I, O> O transform(I input) {
        try {
            Class<?> source = input.getClass();
            System.out.println(source.getSimpleName() + "DTO");
            Class<?> target = Class.forName(source.getSimpleName() + "DTO");

            // Se o target for um `record`, usa o construtor
            if (target.isRecord()) {
                return createRecordFromSource(input, source, target);
            } else {
                // Para classes normais (não records)
                O targetClass = (O) target.getDeclaredConstructor().newInstance();
                mapFields(input, targetClass);
                return targetClass;
            }

        } catch (ClassNotFoundException | InvocationTargetException | NoSuchMethodException | InstantiationException |
                 IllegalAccessException e) {
            throw new TransformatorException("Erro ao transformar objeto", e);
        }
    }

    /**
     * Mapeia os campos de uma classe normal (não `record`) para a classe de destino.
     */
    private static <I, O> void mapFields(I source, O targetClass) {
        Field[] sourceFields = source.getClass().getDeclaredFields();
        Field[] targetFields = targetClass.getClass().getDeclaredFields();

        Arrays.stream(sourceFields).forEach(sourceField ->
                Arrays.stream(targetFields).forEach(targetField -> {
                    validate(sourceField, targetField);
                    try {
                        targetField.set(targetClass, sourceField.get(source));
                    } catch (IllegalAccessException e) {
                        throw new TransformatorException("Erro de acesso aos campos", e);
                    }
                }));
    }

    /**
     * Cria uma instância de um `record` utilizando o construtor e os valores dos campos da fonte.
     */
    private static <I, O> O createRecordFromSource(I source, Class<?> sourceClass, Class<?> targetClass) {
        try {
            // Mapeia os campos de origem em um Map
            Field[] sourceFields = sourceClass.getDeclaredFields();
            Map<String, Object> sourceFieldValues = Arrays.stream(sourceFields)
                    .peek(field -> field.setAccessible(true))
                    .collect(Collectors.toMap(Field::getName, field -> {
                        try {
                            return field.get(source);
                        } catch (IllegalAccessException e) {
                            throw new TransformatorException("Erro ao acessar campo do source", e);
                        }
                    }));

            // Obtém o construtor do `record`
            Constructor<?> constructor = targetClass.getDeclaredConstructors()[0];

            // Prepara os valores de acordo com o construtor
            Object[] constructorArgs = Arrays.stream(constructor.getParameters())
                    .map(param -> sourceFieldValues.get(param.getName()))
                    .toArray();

            // Invoca o construtor com os valores
            return (O) constructor.newInstance(constructorArgs);

        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new TransformatorException("Erro ao criar o record", e);
        }
    }

    private static void validate(Field sourceField, Field targetField) {
        if (sourceField.getName().equals(targetField.getName())
                && sourceField.getType().equals(targetField.getType())) {
            sourceField.setAccessible(true);
            targetField.setAccessible(true);
        }
    }
}
