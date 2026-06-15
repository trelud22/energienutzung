package trelud.energienutzung.service;

import org.junit.jupiter.api.Test;
import trelud.energienutzung.annotation.DtoEntity;
import trelud.energienutzung.annotation.ToDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class DtoServiceTest {

    @DtoEntity
    static class Address {
        @ToDto
        String city = "Vienna";

        @ToDto(key = "zip")
        String postalCode = "1010";
    }

    static class PersonWithDefaultKey {
        @ToDto
        String name = "Max";

        @ToDto
        int age = 30;
    }

    static class PersonWithCustomKey {
        @ToDto(key = "fullName")
        String name = "Anna";
    }

    static class PersonWithNestedList {
        @ToDto
        List<Address> addresses = List.of(new Address());
    }

    static class PersonWithUnAnnotatedField {
        String ignored = "should not appear";

        @ToDto
        String visible = "yes";
    }

    static class EmptyObject {}

    @Test
    void annotatedWithDefaultKey() {
        Map<String, Object> result = DtoService.convertObject(new PersonWithDefaultKey());

        assertEquals("Max", result.get("name"));
        assertEquals(30, result.get("age"));
    }

    @Test
    void customKeyFromAnnotation() {
        Map<String, Object> result = DtoService.convertObject(new PersonWithCustomKey());

        assertTrue(result.containsKey("fullName"));
        assertEquals("Anna", result.get("fullName"));
        assertFalse(result.containsKey("name"));
    }

    @Test
    void skipsFieldsWithoutAnnotation() {
        Map<String, Object> result = DtoService.convertObject(new PersonWithUnAnnotatedField());

        assertFalse(result.containsKey("ignored"));
        assertTrue(result.containsKey("visible"));
        assertEquals("yes", result.get("visible"));
    }

    @Test
    void testAnnotatedNestedList() {
        Map<String, Object> result = DtoService.convertObject(new PersonWithNestedList());

        assertTrue(result.containsKey("addresses"));
        Object value = result.get("addresses");
        assertInstanceOf(List.class, value);

        List<Address> list = (List<Address>) value;
        assertEquals(1, list.size());

        Map<String, Object> addressMap = (Map<String, Object>) list.get(0);
        assertEquals("Vienna", addressMap.get("city"));
        assertEquals("1010", addressMap.get("zip"));
    }

    @Test
    void testEmptyObject() {
        Map<String, Object> result = DtoService.convertObject(new EmptyObject());
        assertTrue(result.isEmpty());
    }
}