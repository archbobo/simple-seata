package io.spring2go.seata.core.protocol;

import java.io.Serializable;

/**
 * Created by william on May, 2020
 */
public class RegisterRMResponse extends AbstractIdentityResponse implements Serializable {
    private static final long serialVersionUID = 6391375605848221420L;

    public RegisterRMResponse() {
        this(true);
    }

    public RegisterRMResponse(boolean result) {
        super();
        setIdentified(result);
    }

    @Override
    public short getTypeCode() {
        return TYPE_REG_RM_RESULT;
    }
}
