package com.codeflu.postgreson.service;


import com.codeflu.postgreson.models.Column;

import java.util.List;

public class ViewBuilder {

    public static String build(String viewName, List<Column> columns){
        return String.format("CREATE OR replace VIEW public.%s \n", viewName) + " AS \n" +
                "\t SELECT \n" +
                buildColumns(columns) +
                " FROM public.json_b" +
                " WHERE id=" +
                String.format("'%s'", viewName);
    }

    private static String buildColumns(List<Column> columns) {
        StringBuilder sb = new StringBuilder();
        for (Column col : columns) {
            sb.append("\t\t(json -> ")
                    .append(String.format("'%s')",col.getName()));
            switch (col.getType()){
                case ARRAY:
                case JSONB:
                    break;
                case INTEGER:
                    sb.append("::integer");
                    break;
                case BIGINT:
                    sb.append("::bigint");
                    break;
                case DECIMAL:
                    sb.append("::decimal");
                    break;
                case BOOLEAN:
                    sb.append("::boolean");
                    break;
                default:
                    sb.insert(sb.lastIndexOf(">"),">");
            }
            sb.append(" AS ")
                    .append(col.getName())
                    .append(",\n");
        }
        return sb.deleteCharAt(sb.lastIndexOf(",")).toString();
    }
}
