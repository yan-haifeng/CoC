package com.coc.user.pay.core.rollback.pay;

import com.coc.user.pay.core.domain.BasePayBackDTO;

public abstract class PayRollBack {
    public abstract String business(BasePayBackDTO payBackDTO);
}
