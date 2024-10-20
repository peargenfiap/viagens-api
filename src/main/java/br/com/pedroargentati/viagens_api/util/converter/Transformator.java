package br.com.pedroargentati.viagens_api.util.converter;

import br.com.pedroargentati.viagens_api.exceptions.TransformatorException;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

public class Transformator {

    private Transformator() { }

    /**
     * Método responsável por transformar um objeto em outro.
     *
     * @param input - Objeto de entrada.
     * @return O - Objeto de saída.
     */
    public static <I, O> O transform(I input) {
        try {
            Class<?> source = input.getClass();
            Class<?> target = Class.forName(source.getName() + "DTO");

            O targetClass = (O) target.getDeclaredConstructor().newInstance();

            Field[] sourceFields = source.getDeclaredFields();
            Field[] targetFields = target.getDeclaredFields();

            Arrays.stream(sourceFields).forEach(sourceField ->
                    Arrays.stream(targetFields).forEach(targetField -> {
                        validate(sourceField, targetField);
                        try {
                            targetField.set(targetClass, sourceField.get(input));
                        } catch (IllegalAccessException e) {
                            throw new TransformatorException("Erro de acesso aos campos", e);
                        }
                    }));

            return targetClass;
        } catch (ClassNotFoundException | InvocationTargetException | NoSuchMethodException | InstantiationException |
                 IllegalAccessException e) {
            throw new TransformatorException("Erro ao transformar objeto", e);
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