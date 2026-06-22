package trelud.energienutzung.service;

import lombok.extern.slf4j.Slf4j;
import trelud.energienutzung.annotation.DtoEntity;
import trelud.energienutzung.annotation.ToDto;

import java.lang.reflect.Field;
import java.util.*;

@Slf4j
public class DtoService {
    public static List<Map<String, Object>> convertList(List<?> objectsToDto){
        return objectsToDto.stream()
                .map(DtoService::convertObject)
                .toList();
    }

    public static Map<String, Object> convertObject(Object o){
        Class<?> clazz = o.getClass();
        Map<String, Object> map = new HashMap<>();
        if(clazz.isAnnotationPresent(DtoEntity.class)){
            for (Field field : clazz.getDeclaredFields()){
                if(field.isAnnotationPresent(ToDto.class)){
                    ToDto annotation = field.getAnnotation(ToDto.class);
                    String name = annotation.key().isEmpty() ? field.getName() : annotation.key();
                    try {
                        field.setAccessible(true);
                        Object object = field.get(o);
                        Class<?> objectClass = object.getClass();
                        if(objectClass.isAnnotationPresent(DtoEntity.class)){
                            object = convertObject(object);
                        }
                        map.put(name, object);
                    } catch (Exception e) {
                        map.put(name, null);
                    }
                }
            }
        }

        return map;
    }
}
