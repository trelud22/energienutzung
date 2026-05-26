package trelud.energienutzung.service;

import lombok.extern.slf4j.Slf4j;
import trelud.energienutzung.annotation.ToDto;
import trelud.energienutzung.annotation.DtoEntity;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class DtoService {
    public static List<Map<String, Object>> toDto(List<?> objectsToDto){
        return objectsToDto.stream()
                .map(DtoService::convertObject)
                .collect(Collectors.toList());
    }

    private static Map<String, Object> convertObject(Object o){
        Class<?> clazz = o.getClass();
        Map<String, Object> map = new HashMap<>();
        for (Field field : clazz.getDeclaredFields()){
            if(field.isAnnotationPresent(ToDto.class)){
                ToDto annotation = field.getAnnotation(ToDto.class);
                String name = annotation.key().isEmpty() ? field.getName() : annotation.key();
                try {
                    field.setAccessible(true);
                    Object object = field.get(o);
                    if(object instanceof List<?>){
                        List<Map<String, Object>> list = new ArrayList<>();
                        ((List<?>) object)
                                .forEach(listObject -> {
                                    if(listObject.getClass().isAnnotationPresent(DtoEntity.class)){
                                        list.add(convertObject(listObject));
                                    }
                                });
                        map.put(name, list);
                    }else{
                        map.put(name, object);
                    }
                } catch (IllegalAccessException e) {
                    map.put(name, null);
                }
            }

        }
        log.info("Converted object of type {}", o.getClass().getSimpleName());
        return map;
    }
}
