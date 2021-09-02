package com.cuki.service;

import com.cuki.controller.dto.VerificationCodeRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Random;

@Slf4j
@RequiredArgsConstructor
@Service
public class EmailService {

    private final JavaMailSender mailSender;
    public static String verificationCode;

    private MimeMessage createMessage(String to) throws Exception {
        // 인증번호 생성
        verificationCode = createVerificationCode();

        log.info("보내는 대상 : " + to);
        log.info("인증 번호 : " + verificationCode);
        MimeMessage message = mailSender.createMimeMessage();

        message.addRecipients(Message.RecipientType.TO, to); // 보내는 대상
        message.setSubject("[cuki] 인증번호를 확인해주세요.");  // 제목

        String msg = "";
        msg += "<img width=\"150\" height=\"100\" style=\"margin: 0px 0px 32px 0px; padding: 0px 30px 0px 30px;\" src=\"https://upload.wikimedia.org/wikipedia/commons/thumb/8/80/Cookie_stack.jpg/1920px-Cookie_stack.jpg\" alt=\"\" loading=\"lazy\">";
        msg += "<h1 style=\"font-size: 30px; padding: 0px 30px 0px 30px;\">이메일 주소 확인</h1>";
        msg += "<p style=\"font-size: 17px; padding: 0px 30px 0px 30px;\">아래 인증 코드를 쿠키앱에서 입력하십시오.</p>";
        msg += "<div style=\"padding: 0px 30px 0px 30px; margin: 32px 0px 40px 0px;\"><table style=\"border-collapse: collapse; border: 0px; background-color: F4F4F4'; height: 70px; table-layout: fixed; word-wrap: break-word; border-radius: 6px;\"><tbody><tr><td style=\"text-align: center; verical-align: middle; font-size: 30px;\">";
        msg += verificationCode;
        msg += "</td></tr></tbody></table></div>";
        msg += "<a href=\"https://cuki.io\" style=\"text-decoration: none; color: #434245;\" rel=\"noreferrer noopener\" target=\"_blank\">cuki app, Inc</a>";

        message.setText(msg, "utf-8", "html");  // 내용
        message.setFrom(new InternetAddress("verycona@gmail.com", "cuki")); // 보내는 사람

        return message;
    }

    // 인증코드 만들기
    private static String createVerificationCode() {
        StringBuilder verificationCode = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < 8; i++) {   // 인증코드 8자리
            verificationCode.append((random.nextInt(10)));
        }
        return verificationCode.toString();
    }

    public void sendVerificationCode(VerificationCodeRequestDto verificationCodeRequestDto) throws Exception {
        MimeMessage message = createMessage(verificationCodeRequestDto.getEmail());
        try {
            mailSender.send(message);
        } catch (MailException e){
            e.printStackTrace();
            throw new IllegalArgumentException();
        }
    }

}
