package com.bank.converters;

import com.bank.annotations.Attribute;
import com.bank.dto.ObjectDto;
import com.bank.dto.ValueDto;
import com.bank.editor.MyDateEditor;
import com.bank.entity.BaseEntity;
import griffon.core.editors.DoublePropertyEditor;
import lombok.Getter;
import lombok.Setter;

import java.beans.PropertyEditor;
import java.beans.PropertyEditorManager;
import java.lang.reflect.Field;
import java.math.BigInteger;
import java.util.*;

import griffon.core.editors.BigIntegerPropertyEditor;

public class EntityDtoConverter {
    static{
        PropertyEditorManager.registerEditor(BigInteger.class, BigIntegerPropertyEditor.class);
        PropertyEditorManager.registerEditor(Double.class, DoublePropertyEditor.class);
        PropertyEditorManager.registerEditor(Date.class, MyDateEditor.class);
    }

    public static <T extends BaseEntity> Pair<ObjectDto, List<ValueDto>> toDto(T entity) throws IllegalAccessException {
        ObjectDto objectDto = new ObjectDto(
                entity.getId(),
                entity.getName());

        List<ValueDto> paramList = new ArrayList<>();
        List<Field> fieldList = getClassFields(entity.getClass());
        HashMap<BigInteger, Field> filteredFields = getFieldsWithAttribute(fieldList);

        for(Map.Entry<BigInteger, Field> entry : filteredFields.entrySet()){
            BigInteger attrId = entry.getKey();
            Field field = entry.getValue();
            field.setAccessible(true);
            String value = String.valueOf(field.get(entity));
            ValueDto paramDto = new ValueDto(
                    entity.getId(),
                    attrId,
                    value
            );
            paramList.add(paramDto);
        }
        return new Pair<>(objectDto, paramList);
    }

    public static <T extends BaseEntity> T toEntity(ObjectDto objectDto, List<ValueDto> listValue, Class<T> clazz) throws IllegalAccessException, InstantiationException {
        T entity = clazz.newInstance();
        entity.setId(objectDto.getObjectId());
        entity.setName(objectDto.getObjectName());

        List<Field> fieldList = getClassFields(clazz);
        HashMap<BigInteger, Field> filteredFields = getFieldsWithAttribute(fieldList);

        for(ValueDto param : listValue){
            Field field = filteredFields.get(param.getAttributeId());
            if(field == null){
                continue;
            }

            PropertyEditor editor = PropertyEditorManager.findEditor(field.getType());
            if(editor ==null){
                throw new RuntimeException("" + param.getAttributeId() + " " + param.getParameterValue());
            }

            editor.setAsText(param.getParameterValue());
            field.setAccessible(true);
            field.set(entity, editor.getValue());

        }
        return entity;
    }

    private static List<Field> getClassFields(Class<? extends BaseEntity> clazz){
        if (clazz == null){
            return null;
        }

        List<Field> fields = new ArrayList<>();
        Class<?> c = clazz;
        do {
            fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
            c = c.getSuperclass();
        } while (BaseEntity.class.isAssignableFrom(c));

        return fields;
    }

    private static HashMap<BigInteger, Field> getFieldsWithAttribute(List<Field> fieldList){
        if(fieldList == null){
            return null;
        }

        HashMap<BigInteger, Field>  filteredFields = new HashMap<>();
        for(Field field : fieldList){
            Attribute attribute = field.getAnnotation(Attribute.class);
            if(attribute == null){
                continue;
            }
            BigInteger attrId = new BigInteger(attribute.value());
            filteredFields.put(attrId, field);
        }
        return filteredFields;
    }

    @Getter
    @Setter
    public static class Pair<K, V> {
        private final K key;
        private final V value;

        public Pair(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
