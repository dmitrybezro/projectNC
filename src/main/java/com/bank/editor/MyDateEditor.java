package com.bank.editor;

import org.springframework.util.StringUtils;
import java.beans.PropertyEditorSupport;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MyDateEditor extends PropertyEditorSupport {
    DateFormat formatter = new SimpleDateFormat("yyyy-MM-d");

    @Override
    public void setAsText(String s) {
        if(StringUtils.hasText(s))
            try{
                Date date = formatter.parse(s);
                setValue(date);
            } catch (java.text.ParseException exception) {
                throw new RuntimeException("setAsText " + exception.getMessage());
            }
        else
            setValue(null);
    }

    @Override
    public String getAsText() {
        if(getValue() == null) {
            return "Bad";
        }

        Date date = (Date) getValue();
        return date.toString();
    }
}
