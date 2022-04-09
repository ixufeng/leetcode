package com.yx.leecode.aviator;

import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.AviatorEvaluatorInstance;
import com.googlecode.aviator.Expression;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author xufeng
 * @date 2022/3/10 1:59 下午
 **/
public class Demo {
    public static void main(String[] args) {
        AviatorEvaluatorInstance instance = AviatorEvaluator.getInstance();

        Expression exp = instance.compile("let map = seq.list();return map;");

        List<Map<String, Object>> resultList = new ArrayList<>();
        Map<String, Object> param = new HashMap<>();
        param.put("code", 1);

        Object execute = exp.execute(exp.newEnv("message", param, "result", resultList));

        System.out.println(execute.getClass());

    }
}
