package com.sq.repository.callback;

import com.sq.entity.search.MatchType;
import com.sq.entity.search.Searchable;
import com.sq.entity.search.condition.AndCondition;
import com.sq.entity.search.condition.Condition;
import com.sq.entity.search.condition.OrCondition;
import com.sq.entity.search.condition.SearchFilter;
import com.sq.util.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import javax.persistence.Query;

/**
 * Created with IntelliJ IDEA.
 * User: shuiqing
 * Date: 2015/3/25
 * Time: 16:31
 * Email: shuiqing301@gmail.com
 * _
 * |_)._ _
 * | o| (_
 */
public class DefaultSearchCallback implements SearchCallback {

    private static final String paramPrefix = "param_";

    private String alias;
    private String aliasWithDot;

    public DefaultSearchCallback() {
        this("");
    }

    public DefaultSearchCallback(String alias) {
        this.alias = alias;
        if (!StringUtils.isEmpty(alias)) {
            this.aliasWithDot = alias + ".";
        } else {
            this.aliasWithDot = "";
        }
    }

    public String getAlias() {
        return alias;
    }

    public String getAliasWithDot() {
        return aliasWithDot;
    }

    @Override
    public void prepareQL(StringBuilder ql, Searchable search) {
        if (!search.hasSearchFilter()) {
            return;
        }

        int paramIndex = 1;
        for (SearchFilter searchFilter : search.getSearchFilters()) {

            if (searchFilter instanceof Condition) {
                Condition condition = (Condition) searchFilter;
                if (condition.getOperator() == MatchType.CUSTOM) {
                    continue;
                }
            }

            ql.append(" and ");

            paramIndex = genCondition(ql, paramIndex, searchFilter);

        }
    }

    private int genCondition(StringBuilder ql, int paramIndex, SearchFilter searchFilter) {
        boolean needAppendBracket = searchFilter instanceof OrCondition || searchFilter instanceof AndCondition;

        if (needAppendBracket) {
            ql.append("(");
        }

        if (searchFilter instanceof Condition) {
            Condition condition = (Condition) searchFilter;
            //自定义条件
            String entityProperty = condition.getEntityProperty();
            String operatorStr = condition.getOperatorStr();
            //实体名称
            ql.append(getAliasWithDot());
            ql.append(entityProperty);
            //操作符
            //1、如果是自定义查询符号，则使用SearchPropertyMappings中定义的默认的操作符
            ql.append(" ");
            ql.append(operatorStr);

            if (!condition.isUnaryFilter()) {
                ql.append(" :");
                ql.append(paramPrefix);
                ql.append(paramIndex++);
                return paramIndex;
            }
        } else if (searchFilter instanceof OrCondition) {
            boolean isFirst = true;
            for (SearchFilter orSearchFilter : ((OrCondition) searchFilter).getOrFilters()) {
                if (!isFirst) {
                    ql.append(" or ");
                }
                paramIndex = genCondition(ql, paramIndex, orSearchFilter);
                isFirst = false;
            }
        } else if (searchFilter instanceof AndCondition) {
            boolean isFirst = true;
            for (SearchFilter andSearchFilter : ((AndCondition) searchFilter).getAndFilters()) {
                if (!isFirst) {
                    ql.append(" and ");
                }
                paramIndex = genCondition(ql, paramIndex, andSearchFilter);
                isFirst = false;
            }
        }

        if (needAppendBracket) {
            ql.append(")");
        }
        return paramIndex;
    }


    @Override
    public void setValues(Query query, Searchable search) {

        int paramIndex = 1;

        for (SearchFilter searchFilter : search.getSearchFilters()) {
            paramIndex = setValues(query, searchFilter, paramIndex);
        }

    }

    private int setValues(Query query, SearchFilter searchFilter, int paramIndex) {
        if (searchFilter instanceof Condition) {

            Condition condition = (Condition) searchFilter;
            if (condition.getOperator() == MatchType.CUSTOM) {
                return paramIndex;
            }
            if (condition.isUnaryFilter()) {
                return paramIndex;
            }
            query.setParameter(paramPrefix + paramIndex++, formtValue(condition, condition.getMatchValue()));

        } else if (searchFilter instanceof OrCondition) {

            for (SearchFilter orSearchFilter : ((OrCondition) searchFilter).getOrFilters()) {
                paramIndex = setValues(query, orSearchFilter, paramIndex);
            }

        } else if (searchFilter instanceof AndCondition) {
            for (SearchFilter andSearchFilter : ((AndCondition) searchFilter).getAndFilters()) {
                paramIndex = setValues(query, andSearchFilter, paramIndex);
            }
        }
        return paramIndex;
    }

    private Object formtValue(Condition condition, Object value) {
        MatchType operator = condition.getOperator();
        if (operator == MatchType.LIKE || operator == MatchType.notLike) {
            return "%" + value + "%";
        }
        if (operator == MatchType.prefixLike || operator == MatchType.prefixNotLike) {
            return value + "%";
        }

        if (operator == MatchType.suffixLike || operator == MatchType.suffixNotLike) {
            return "%" + value;
        }
        return value;
    }

    public void setPageable(Query query, Searchable search) {
        if (search.hasPageable()) {
            Pageable pageable = search.getPage();
            query.setFirstResult(pageable.getOffset());
            query.setMaxResults(pageable.getPageSize());
        }
    }

    public void prepareOrder(StringBuilder ql, Searchable search) {
        if (search.hashSort()) {
            ql.append(" order by ");
            for (Sort.Order order : search.getSort()) {
                ql.append(String.format("%s%s %s, ", getAliasWithDot(), order.getProperty(), order.getDirection().name().toLowerCase()));
            }

            ql.delete(ql.length() - 2, ql.length());
        }
    }


}

