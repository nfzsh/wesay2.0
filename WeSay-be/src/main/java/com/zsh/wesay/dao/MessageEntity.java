package com.zsh.wesay.dao;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author zhushihao
 * @version 1.0
 * @date 2022/4/10 23:12
 */
@Data
@AllArgsConstructor
public class MessageEntity {
    private LocalDateTime sendDateTime;
    private String userName;
    private String sessionId;
    private String message;
}
