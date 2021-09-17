package com.cuki.schedule.utils;

import org.springframework.stereotype.Component;


// 요청한 클라이언트가 게시글 작성자가 맞는지 확인
@Component
public class WriterVerification {

//    public static boolean isWriter(Member currentMember, Member writer) {
//        if (Objects.nonNull(currentMember)) {
//            return currentMember.getId().equals(writer.getId());
//        }
//        return false;
//    }

    public static boolean isWriter(Long currentMemberId, Long writerId) {
        if (currentMemberId != null) {
            return currentMemberId.equals(writerId);
        }
        return false;
    }
}
