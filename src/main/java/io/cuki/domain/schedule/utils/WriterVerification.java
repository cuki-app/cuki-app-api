package io.cuki.domain.schedule.utils;

import io.cuki.domain.participation.exception.WriterAuthorityException;
import io.cuki.global.util.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.util.Objects;

@Slf4j
@Component
public class WriterVerification {

    public static boolean isWriter(Long comparingID, Long writerId) {
        if (Objects.nonNull(comparingID)) {
            return comparingID.equals(writerId);
        }
        return false;
    }

    public static boolean hasWriterAuthority(Long comparingID, Long writerId) {
        if (!isWriter(comparingID, writerId)) {
            log.debug("요청한 사용자 = {}, 게시글 작성자 = {}", comparingID, writerId);
            throw new WriterAuthorityException("해당 기능은 게시글 작성자만 사용 가능합니다.");
        }
        return true;
    }
}
