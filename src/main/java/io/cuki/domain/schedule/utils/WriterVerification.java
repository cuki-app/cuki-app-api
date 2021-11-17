package io.cuki.domain.schedule.utils;

import io.cuki.domain.participation.exception.WriterAuthorityException;
import org.springframework.stereotype.Component;
import java.util.Objects;


// 요청한 클라이언트가 게시글 작성자가 맞는지 확인
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
            throw new WriterAuthorityException("게시글 작성자만 확인할 수 있는 정보입니다.");
        }
        return true;
    }
}
