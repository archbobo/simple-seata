package io.spring2go.seata.rm.datasource.undo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * Created by william on May, 2020
 */
public class JSONBasedUndoLogParser implements UndoLogParser {
    @Override
    public String encode(BranchUndoLog branchUndoLog) {
        return JSON.toJSONString(branchUndoLog, SerializerFeature.WriteDateUseDateFormat);
    }

    @Override
    public BranchUndoLog decode(String text) {
        return JSON.parseObject(text, BranchUndoLog.class);
    }
}
