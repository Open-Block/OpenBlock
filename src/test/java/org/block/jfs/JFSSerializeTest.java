package org.block.jfs;


import org.block.jsf.data.JSFPart;
import org.block.jsf.data.jsffunction.JSFConstructor;
import org.block.jsf.data.jsfvalue.JSFParameter;
import org.block.serialization.json.JSONConfigNode;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Optional;

public class JFSSerializeTest {

    @Test
    public void testConstructorDeserializeTwoParameter() {
        JSFParameter param1 = JSFPart.BUILDER_PARAMETER.clone().setName("param1").setType("java.io.File").build();
        JSFParameter param2 = JSFPart.BUILDER_PARAMETER.clone().setName("param1").setType("java.io.File").build();
        JSFConstructor cons = JSFPart.BUILDER_CONSTRUCTOR.clone().setReturning("test").setFinal(true).addParameters(param1, param2).build();
        JSONObject obj = new JSONObject();
        JSFPart.BUILDER_CONSTRUCTOR.clone().serialize(new JSONConfigNode(obj), "test", cons);
        String output = obj.toString();
        System.out.println(output);
    }

    @Test
    public void testConstructorDeserializeNoParameter() {
        JSFConstructor cons = JSFPart.BUILDER_CONSTRUCTOR.clone().setReturning("test").setFinal(true).build();
        JSONObject obj = new JSONObject();
        JSFPart.BUILDER_CONSTRUCTOR.clone().serialize(new JSONConfigNode(obj), "test", cons);
        String output = obj.toString();
        if (!output.contains("\"return\":\"test\"")) {
            Assertions.fail("name didn't equal test or is not present");
        }
        if (!output.contains("\"isFinal\":true")) {
            Assertions.fail("isFinal didn't equal true or is not present");
        }
        if (!output.contains("\"visibility\":\"PUBLIC\"")) {
            Assertions.fail("return didn't equal java.io.File or is not present");
        }
        if (!output.contains("\"parameters\":[]")) {
            Assertions.fail("parameters didn't equal [] or is not present");
        }
    }

    @Test
    public void testParameterDeserialize() {
        JSFParameter parameter = JSFPart.BUILDER_PARAMETER.clone().setName("test").setType("java.io.File").setFinal(true).build();
        JSONObject obj = new JSONObject();
        JSFPart.BUILDER_PARAMETER.clone().serialize(new JSONConfigNode(obj), "test", parameter);
        String output = obj.toString();
        if (!output.contains("\"name\":\"test\"")) {
            Assertions.fail("name didn't equal test or is not present");
            return;
        }
        if (!output.contains("\"isFinal\":true")) {
            Assertions.fail("isFinal didn't equal true or is not present");
            return;
        }
        if (!output.contains("\"return\":\"java.io.File\"")) {
            Assertions.fail("return didn't equal java.io.File or is not present");
        }
    }

    @Test
    public void testParameterSerialize() {
        JSONObject obj = new JSONObject("{\"test\":{\"name\":\"test\",\"isFinal\":true,\"return\":\"java.io.File\"}}\n");
        JSONConfigNode node = new JSONConfigNode(obj);
        Optional<JSFParameter> opParameter = JSFPart.BUILDER_PARAMETER.clone().deserialize(node, "test");
        if (!opParameter.isPresent()) {
            Assertions.fail("Could not deserialize");
        }
        JSFParameter param = opParameter.get();
        if (!param.getName().equals("test")) {
            Assertions.fail("Name was not 'test' but it was '" + param.getName() + "'");
            return;
        }
        if (!param.isFinal()) {
            Assertions.fail("isFinal was not true but it was " + param.isFinal());
            return;
        }
        Assertions.assertEquals(param.getType(), "java.io.File");
    }

    @Test
    public void testParameterReadWrite() {
        JSFParameter parameter = JSFPart.BUILDER_PARAMETER.clone().setName("test").setType("java.io.File").setFinal(true).build();
        JSONObject obj = new JSONObject();
        JSFPart.BUILDER_PARAMETER.clone().serialize(new JSONConfigNode(obj), "test", parameter);
        String output = obj.toString();
        obj = new JSONObject(output);
        Optional<JSFParameter> opParameter = JSFPart.BUILDER_PARAMETER.clone().deserialize(new JSONConfigNode(obj), "test");
        if (!opParameter.isPresent()) {
            Assertions.fail("Could not create Parameter from json");
        }
        Assertions.assertEquals(parameter, opParameter.get());
    }

}
