package fr.formiko.twoofortyeight;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
// import org.junit.jupiter.params.ParameterizedTest;
// import org.junit.jupiter.params.provider.CsvSource;


public class FieldTest extends Assertions {
    @Test
    public void testField() {
        Field field = new Field(0, 0);
        assertEquals(4, field.getChildren().items.length);
        // assertEquals(, field);
        
    }
    @Test
    public void testEquals(){
        Field field1 = new Field(0, 0);
        Field field2 = new Field(0, 0);
        assertEquals(field1, field2);
        assertFalse(true);
    }
}