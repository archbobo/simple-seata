package io.spring2go.seata.core.protocol;

import java.io.Serializable;

/**
 * Created by william on May, 2020
 */
public class RegisterTMRequest extends AbstractIdentifyRequest implements Serializable {
    private static final long serialVersionUID = -5929081344190543690L;

    public RegisterTMRequest() {
        this(null, null);
    }

    public RegisterTMRequest(String applicationId, String transactionServiceGroup, String extraData) {
        super(applicationId, transactionServiceGroup, extraData);

    }

    public RegisterTMRequest(String applicationId, String transactionServiceGroup) {
        super(applicationId, transactionServiceGroup);
    }

    @Override
    public short getTypeCode() {
        return TYPE_REG_CLT;
    }

    @Override
    public String toString() {
        return "RegisterTMRequest{" +
                "applicationId='" + applicationId + '\'' +
                ", transactionServiceGroup='" + transactionServiceGroup + '\'' +
                '}';
    }
}
