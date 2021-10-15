package io.cuki.domain.schedule.utils;

import org.springframework.stereotype.Component;
import java.util.Objects;


// 요청한 클라이언트가 게시글 작성자가 맞는지 확인
@Component
public class WriterVerification {

    public static boolean isWriter(Long currentMemberId, Long writerId) {
        if (Objects.nonNull(currentMemberId)) {
            return currentMemberId.equals(writerId);
        }
        return false;
    }
}
