package trelud.energienutzung.service;

import lombok.extern.slf4j.Slf4j;
import trelud.energienutzung.annotation.ToDto;

import java.lang.reflect.Field;
import java.util.*;

@Slf4j
public class DtoService {
    public static List<Map<String, Object>> convertList(List<?> objectsToDto){
        List<Map<String, Object>> dtos = new ArrayList<>();
        while(!objectsToDto.isEmpty()){
            dtos.add(convertObject(objectsToDto.removeLast()));
        }
        return dtos;
    }

    public static Map<String, Object> convertObject(Object o){
        Class<?> clazz = o.getClass();
        Map<String, Object> map = new HashMap<>();
        for (Field field : clazz.getDeclaredFields()){
            if(field.isAnnotationPresent(ToDto.class)){
                ToDto annotation = field.getAnnotation(ToDto.class);
                String name = annotation.key().isEmpty() ? field.getName() : annotation.key();
                try {
                    field.setAccessible(true);
                    Object object = field.get(o);

                    map.put(name, object);
                } catch (IllegalAccessException e) {
                    map.put(name, null);
                }
            }
        }
        return map;
    }
}
