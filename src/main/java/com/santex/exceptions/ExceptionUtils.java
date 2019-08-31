package com.santex.exceptions;

import java.util.Arrays;
import java.util.List;

public class ExceptionUtils {

    public static boolean existsInChain(Throwable throwable, Class<?> clazz) {
        return org.apache.commons.lang3.exception.ExceptionUtils.indexOfType(throwable, clazz) != -1;
    }

    public static Throwable getFromChain(Throwable throwable, Class<?> clazz) {
        return ExceptionUtils.existsInChain(throwable, clazz) ?
                org.apache.commons.lang3.exception.ExceptionUtils.getThrowables(throwable)[org.apache.commons.lang3.exception.ExceptionUtils.indexOfType(throwable, clazz)] :
                throwable;
    }

    public static String getLogMessage(Throwable t) {
        String className = getCleanName(t.getClass().getName());
        StackTraceElement wellKnownClass = Arrays.stream(t.getStackTrace()).filter(element -> element.getClassName().contains("com.santex")).findFirst().orElse(null);
        StackTraceElement element = wellKnownClass != null ? wellKnownClass : t.getStackTrace()[0];
        StringBuilder errorMessage = new StringBuilder();
        errorMessage.append(String.format("%s: %s at %s.%s(%s:%d)",
                className,
                t.getMessage(),
                getCleanName(element.getClassName()),
                element.getMethodName(),
                element.getFileName(),
                element.getLineNumber())
        );
        return errorMessage.toString();
    }

    public static String getCleanName(String fullName) {
        List<String> list = Arrays.asList(fullName.split("\\."));
        return list.get(list.size()-1);
    }
}
