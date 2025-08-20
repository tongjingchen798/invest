package io.renren.modules.mail.service;

import io.renren.common.page.PageData;
import io.renren.modules.mail.dto.MailDTO;

import java.util.Map;

/**
 * 站内信
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2025-08-19
 */
public interface MailService {

    /**
     * 分页查询
     */
    PageData<MailDTO> page(Map<String, Object> params);

}
