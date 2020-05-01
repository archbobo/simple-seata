package io.spring2go.seata.core.protocol;

import java.io.Serializable;

/**
 * Created by william on May, 2020
 */
public class RegisterTMResponse extends AbstractIdentityResponse implements Serializable {
    private static final long serialVersionUID = 3629846050062228749L;

    public RegisterTMResponse() {
        this(true);
    }

    public RegisterTMResponse(boolean result) {
        super();
        setIdentified(result);
    }

    @Override
    public short getTypeCode() {
        return TYPE_REG_CLT_RESULT;
    }
}
