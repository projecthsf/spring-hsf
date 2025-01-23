package io.github.projecthsf.binding;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HsfRequestBody {
    private String classInterface;
    private String version;
    private String method;
    private Object[] params;
    private Class<?>[] paramTypes;
}
