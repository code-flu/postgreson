package com.codeflu.postgreson.core;

import com.codeflu.postgreson.core.models.Column;

import java.util.List;

public class QueryBuilder {

    public static String baseView(String view, List<Column> columns){
        StringBuilder sb = new StringBuilder(String.format("CREATE OR replace VIEW public.%s \n", view));
        return sb.append(" AS \n")
                .append("\t SELECT \n")
                .append(buildColumns(columns))
                .append(" FROM public.json_b ")
                .append(" WHERE id=")
                .append(String.format("'%s'",view)).toString();
    }

    private static String buildColumns(List<Column> columns){
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
