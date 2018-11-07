package com.snxy.user.agent.service;

import com.snxy.user.agent.domain.ShortMessage;
import lombok.Data;

/**
 * Created by 24398 on 2018/11/5.
 */

public interface ShortMessageService {
    void saveShortMessage(ShortMessage shortMessage);
}
